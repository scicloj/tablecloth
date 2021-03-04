(ns tablecloth.api.split
  (:refer-clojure :exclude [group-by])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.datatype.argops :as aop]

            [tablecloth.api.utils :refer [grouped? process-group-data]]
            [tablecloth.api.group-by :refer [group-by groups->map]]
            [tablecloth.api.columns :refer [add-columns]]
            [tablecloth.api.rows :as rows])
  (:import [java.util ArrayList Collection Random]))

;; some utils

(defn- shuffle-with-rng
  [^Collection coll rng]
  (let [al (ArrayList. coll)]
    (java.util.Collections/shuffle al rng)
    al))

(defn- rand-int-with-rng
  (^long [^Random rng ^long n]
   (.nextInt rng n))
  (^long [^Random rng ^long n1 ^long n2]
   (+ n1 (.nextInt rng (- n2 n1)))))

(defn- drop-nth [coll n]
  (keep-indexed #(when (not= %1 n) %2) coll))

;;

(defn- bootstrap
  [cnt rng {:keys [ratio]
            :or {ratio 1.0}}]
  (if (zero? cnt)
    [[]]
    (let [^ArrayList idxs (shuffle-with-rng (range cnt) rng)
          amount (max 1 (* ratio cnt))]
      [(repeatedly amount #(.get idxs (rand-int-with-rng rng cnt)))])))

(defn- kfold
  [cnt rng {:keys [k]
            :or {k 5}}]
  (let [k (min cnt k)
        idxs (partition-all (/ cnt k) (shuffle-with-rng (range cnt) rng))]
    (for [i (range k)]
      (mapcat identity (drop-nth idxs i)))))

(defn- loo
  [cnt rng opts] (kfold cnt rng (assoc opts :k cnt)))

(defn- holdout
  [cnt rng {:keys [ratio]
            :or {ratio (/ 2.0 3.0)}}]
  (let [amount (min (dec cnt) (max 1 (* cnt ratio)))]
    [(take amount (shuffle-with-rng (range cnt) rng))]))

(def ^:private split-types {:bootstrap bootstrap
                            :kfold kfold
                            :loo loo
                            :holdout holdout})

;;

(defn- make-subdataset
  [ds row-ids train-or-test split-col-name split-id-col-name id]
  (let [select-or-drop-fn (if (= train-or-test :train)
                            ds/select-rows
                            ds/drop-rows)]
    
    (-> (select-or-drop-fn ds row-ids)
        (add-columns {split-col-name train-or-test split-id-col-name id}))))

(defn- split-ds
  [ds split-fn rng {:keys [repeats split-col-name split-id-col-name]
                    :or {repeats 1 split-col-name :$split-name split-id-col-name :$split-id}
                    :as opts}]
  (let [cnt (ds/row-count ds)]
    (->> #(split-fn cnt rng opts)
         (repeatedly repeats)
         (mapcat identity)
         (map-indexed (fn [id ids]
                        (ds/concat-copying
                         (make-subdataset ds ids :train split-col-name split-id-col-name id)
                         (make-subdataset ds ids :test split-col-name split-id-col-name id))))
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
      (ds/set-dataset-name (str (ds/dataset-name ds) ", (train,test)"))))

(defn split
  "Split given dataset into train and test.

  As the result two new columns are added:

  * `:$split-name` - with `:train` or `:test` values
  * `:$split-id` - id of train/test pair

  `split-type` can be one of the following:

  * `:kfold` - k-fold strategy, `:k` defines number of folds (defaults to `5`), produces `k` splits
  * `:bootstrap` - `:ratio` defines ratio of observations put into result (defaults to `1.0`), produces `1` split
  * `:holdout` - split into two parts with given ratio (defaults to `2/3`), produces `1` split
  * `:loo` - leave one out, produces the same number of splits as number of observations

  Additionally you can provide:

  * `:seed` - for random number generator
  * `:repeats` - repeat procedure `:repeats` times
  * `:partition-selector` - same as in `group-by` for stratified splitting to reflect dataset structure in splits.
  * `:split-col-name` - a column where name of split is stored, either `:train` or `:test` values (default: `:$split-name`)
  * `:split-id-col-name` - a column where id of the train/test pair is stored (default: `:$split-id`)

  Rows are shuffled before splitting.
  
  In case of grouped dataset each group is processed separately.

  See [more](https://www.mitpressjournals.org/doi/pdf/10.1162/EVCO_a_00069)"
  ([ds] (split ds :kfold))
  ([ds split-type] (split ds split-type {}))
  ([ds split-type {:keys [seed parallel?] :as opts}]
   (let [rng (if seed (Random. seed) (Random.))
         split-fn (get split-types split-type :kfold)]
     (if (grouped? ds)
       (process-group-data ds #(split-single-ds % split-fn rng opts) parallel?)
       (split-single-ds ds split-fn rng opts)))))

(defn- splitted-ds->seq
  [splitted split-col-name split-id-col-name]
  (->> (group-by splitted split-id-col-name {:result-type :as-seq})
       (map (fn [ds]
              (let [ids (aop/argfilter #(= :train %) (ds split-col-name))
                    nds (ds/drop-columns ds [split-col-name split-id-col-name])]
                {:train (ds/select-rows nds ids)
                 :test (ds/drop-rows nds ids)})))))

(defn split->seq
  "Returns split as a sequence of train/test datasets or map of sequences (grouped dataset)"
  ([ds] (split->seq ds :kfold))
  ([ds split-type] (split->seq ds split-type {}))
  ([ds split-type {:keys [split-col-name split-id-col-name]
                   :or {split-col-name :$split-name split-id-col-name :$split-id}
                   :as opts}]
   (let [splitted (split ds split-type opts)]
     (if (grouped? ds)
       (->> (groups->map splitted)
            (map (fn [[g d]]
                   [g (splitted-ds->seq d split-col-name split-id-col-name)]))
            (into {}))
       (splitted-ds->seq splitted split-col-name split-id-col-name)))))

