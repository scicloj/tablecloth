(ns tablecloth.api.split
  (:refer-clojure :exclude [group-by])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.datatype.argops :as aop]

            [tablecloth.api.utils :refer [grouped? process-group-data]]
            [tablecloth.api.group-by :refer [group-by groups->map]]
            [tablecloth.api.columns :refer [add-columns]]

            [clojure.set :refer [difference]])
  (:import [java.util ArrayList Collection Random]))

;; some utils

(defonce ^:private ^Random private-rng (Random.))

(defn- shuffle-with-rng
  [^Collection coll rng]
  (if-not rng
    coll
    (let [al (ArrayList. coll)]
      (java.util.Collections/shuffle al rng)
      al)))

(defn- rand-int-with-rng
  ^long [^Random rng ^long n]
  (.nextInt (or rng private-rng) n))

(defn- drop-nth [coll n]
  (keep-indexed #(when (not= %1 n) %2) coll))

;;

(defn- bootstrap
  [cnt rng {:keys [ratio]
            :or {ratio 1.0}}]
  (if (zero? cnt)
    [[]]
    (let [^ArrayList idxs (shuffle-with-rng (range cnt) rng)
          amount (max 1 (* ratio cnt))
          b (repeatedly amount #(.get idxs (rand-int-with-rng rng cnt)))]
      [[b (difference (set (range cnt)) (set b))]])))

(defn- kfold
  [cnt rng {:keys [k]
            :or {k 5}}]
  (let [k (min cnt k)
        idxs (partition-all (/ cnt k) (shuffle-with-rng (range cnt) rng))]
    (for [i (range (count idxs))]
      [(mapcat identity (drop-nth idxs i))
       (nth idxs i)])))

(defn- loo
  [cnt rng opts] (kfold cnt rng (assoc opts :k cnt)))

(defn- fix-ratio
  [cnt ratio]
  (if (number? ratio)
    (if (< ratio 1.0)
      (fix-ratio cnt [ratio (- 1.0 ratio)])
      (fix-ratio cnt [(int ratio) (- cnt (int ratio))]))
    (let [s (reduce + ratio)]
      (map #(int (* cnt (/ % s))) ratio))))

(defn- holdout
  [cnt rng {:keys [ratio]
            :or {ratio (/ 2.0 3.0)}}]
  (let [ratios (butlast (fix-ratio cnt ratio))
        idxs (shuffle-with-rng (range cnt) rng)
        [rst coll] (reduce (fn [[curr res] n]
                             [(drop n curr)
                              (conj res (take n curr))]) [idxs []] ratios)]
    [(conj coll rst)]))

(defn- fix-steps
  [cnt [start end step]]
  (let [upper (dec cnt)
        start (max 1 (min (dec upper) (int (if (< start 1.0) (* start cnt) start))))
        end (max 1 (min upper (int (if (< end 1.0) (* end cnt) end))))
        [start end] (cond
                      (< end start) [end start]
                      (= start end) [start (inc start)]
                      :else [start end])
        step (max 1 step)]
    [start end step]))

(defn- holdouts
  [cnt rng {:keys [steps]
            :or {steps [0.05 0.95 1]}}]
  (let [[start end step] (fix-steps cnt steps)]
    (mapcat #(holdout cnt rng {:ratio (/ % cnt)}) (range start end step))))

(def ^:private split-types {:bootstrap bootstrap
                            :kfold kfold
                            :loo loo
                            :holdout holdout
                            :holdouts holdouts})

(def ^:private default-split-names (concat [:train :test]
                                           (map #(keyword (str "split-" (+ 2 %))) (range))))

;;

(defn- make-subdataset
  [ds row-ids train-or-test split-col-name split-id-col-name id]
  (-> (ds/select-rows ds row-ids)
      (add-columns {split-col-name train-or-test split-id-col-name id})))

(defn- split-ds
  [ds split-fn rng {:keys [repeats split-col-name split-id-col-name split-names]
                    :or {repeats 1 split-col-name :$split-name split-id-col-name :$split-id
                         split-names default-split-names}
                    :as opts}]
  (let [cnt (ds/row-count ds)]
    (->> #(split-fn cnt rng opts)
         (repeatedly repeats)
         (mapcat identity)
         (map-indexed (fn [id idss]
                        (apply ds/concat-copying
                               (map (fn [nm ids]
                                      (make-subdataset ds ids nm split-col-name split-id-col-name id)) split-names idss))))
         (reduce ds/concat-copying))))

(defn- split-stratified-ds
  [ds split-fn rng partition-selector opts]
  (->> (group-by ds partition-selector {:result-type :as-seq})
       (map (fn [ds] (split-ds ds split-fn rng opts)))
       (reduce ds/concat-copying)))

(defn- split-single-ds
  [ds split-fn rng {:keys [partition-selector]
                    :as opts}]
  (-> (if partition-selector
        (split-stratified-ds ds split-fn rng partition-selector opts)
        (split-ds ds split-fn rng opts))
      (ds/set-dataset-name (str (ds/dataset-name ds) ", (splitted)"))))

(defn split
  "Split given dataset into 2 or more (holdout) splits

  As the result two new columns are added:

  * `:$split-name` - with subgroup name
  * `:$split-id` - fold id/repetition id

  `split-type` can be one of the following:

  * `:kfold` - k-fold strategy, `:k` defines number of folds (defaults to `5`), produces `k` splits
  * `:bootstrap` - `:ratio` defines ratio of observations put into result (defaults to `1.0`), produces `1` split
  * `:holdout` - split into two parts with given ratio (defaults to `2/3`), produces `1` split
  * `:loo` - leave one out, produces the same number of splits as number of observations

  `:holdout` can accept also probabilites or ratios and can split to more than 2 subdatasets
  
  Additionally you can provide:

  * `:seed` - for random number generator
  * `:repeats` - repeat procedure `:repeats` times
  * `:partition-selector` - same as in `group-by` for stratified splitting to reflect dataset structure in splits.
  * `:split-names` names of subdatasets different than default, ie. `[:train :test :split-2 ...]`
  * `:split-col-name` - a column where name of split is stored, either `:train` or `:test` values (default: `:$split-name`)
  * `:split-id-col-name` - a column where id of the train/test pair is stored (default: `:$split-id`)

  Rows are shuffled before splitting.
  
  In case of grouped dataset each group is processed separately.

  See [more](https://www.mitpressjournals.org/doi/pdf/10.1162/EVCO_a_00069)"
  ([ds] (split ds :kfold))
  ([ds split-type] (split ds split-type {}))
  ([ds split-type {:keys [seed parallel? shuffle?] :or {shuffle? true} :as options}]
   (let [rng (when shuffle? (if seed (Random. seed) private-rng))
         split-fn (get split-types split-type :kfold)]
     (if (grouped? ds)
       (process-group-data ds #(split-single-ds % split-fn rng options) parallel?)
       (split-single-ds ds split-fn rng options)))))

(defn- splitted-ds->seq
  [splitted split-col-name split-id-col-name]
  (->> (group-by splitted split-id-col-name {:result-type :as-seq})
       (map (fn [ds]
              (let [nds (ds/drop-columns ds [split-col-name split-id-col-name])]
                (->> (ds split-col-name)
                     (aop/arggroup)
                     (map (fn [[k v]]
                            [k (ds/select-rows nds v)]))
                     (into {})))))))

(defn split->seq
  "Returns split as a sequence of train/test datasets or map of sequences (grouped dataset)"
  ([ds] (split->seq ds :kfold))
  ([ds split-type] (split->seq ds split-type {}))
  ([ds split-type {:keys [split-col-name split-id-col-name]
                   :or {split-col-name :$split-name split-id-col-name :$split-id}
                   :as options}]
   (let [splitted (split ds split-type options)]
     (if (grouped? ds)
       (->> (groups->map splitted)
            (map (fn [[g d]]
                   [g (splitted-ds->seq d split-col-name split-id-col-name)]))
            (into {}))
       (splitted-ds->seq splitted split-col-name split-id-col-name)))))

