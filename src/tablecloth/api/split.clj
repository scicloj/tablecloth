(ns tablecloth.api.split
  (:refer-clojure :exclude [group-by])
  (:require [tech.v3.dataset :as ds]

            [tablecloth.api.utils :refer [grouped?]]
            [tablecloth.api.group-by :refer [group-by groups->seq]])
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
  ([cnt] (bootstrap cnt {}))
  ([cnt {:keys [ratio rng]
         :or {ratio 1.0}}]
   (if (zero? cnt)
     [[]]
     (let [^ArrayList idxs (shuffle-with-rng (range cnt) rng)
           amount (max 1 (* ratio cnt))]
       [(repeatedly amount #(.get idxs (rand-int-with-rng rng cnt)))]))))

(defn- kfold
  ([cnt] (kfold cnt {}))
  ([cnt {:keys [k rng]
         :or {k 5}}]
   (let [idxs (partition-all (/ cnt k) (shuffle-with-rng (range cnt) rng))]
     (for [i (range k)]
       (mapcat identity (drop-nth idxs i))))))

(defn- loo
  ([cnt] (kfold cnt {:k cnt}))
  ([cnt opts] (kfold cnt (assoc opts :k cnt))))

(defn- holdout
  ([cnt] (holdout cnt {}))
  ([cnt {:keys [ratio rng]
         :or {ratio (/ 2.0 3.0)}}]
   (let [amount (min (dec cnt) (max 1 (* cnt ratio)))]
     [(take amount (shuffle-with-rng (range cnt) rng))])))

(def ^:private split-types {:bootstrap bootstrap
                            :kfold kfold
                            :loo loo
                            :holdout holdout})

(defn- train-test-split
  [ds ids]
  {:train (ds/select-rows ds ids)
   :test (ds/drop-rows ds ids)})

(defn- split-single-ds
  [ds split-type {:keys [repeats split-fn partition-selector]
                  :or {repeats 1}
                  :as opts}]
  (if partition-selector
    (let [newopts (dissoc opts :partition-selector)]
      (->> (group-by ds partition-selector {:result-type :as-seq})
           (map #(split-single-ds % split-type newopts))
           (apply map (fn [& gs]
                        {:train (-> (apply ds/concat (map :train gs))
                                    (ds/set-dataset-name (str "Train set, " (ds/dataset-name ds))))
                         :test (-> (apply ds/concat (map :test gs))
                                   (ds/set-dataset-name (str "Test set, " (ds/dataset-name ds))))}))))
    (let [cnt (ds/row-count ds)]
      (->> (range repeats)
           (mapcat (fn [_] (split-fn cnt opts)))
           (map (partial train-test-split ds))))))

(defn split
  "Split given dataset into train and test datasets as a lazy sequence of maps containing with `:train` and `:test` keys.

  `split-type` can be one of the following:

  * `:kfold` - k-fold strategy, `:k` defines number of folds (defaults to `5`), produces `k` splits
  * `:bootstrap` - `:ratio` defines ratio of observations put into result (defaults to `1.0`), produces `1` split
  * `:holdout` - split into two parts with given ratio (defaults to `2/3`), produces `1` split
  * `:loo` - leave one out, produces the same number of splits as number of observations

  Additionally you can provide:

  * `:seed` - for random number generator
  * `:repeats` - repeat procedure `:repeats` times
  * `:partition-selector` - same as in `group-by` for stratified splitting to reflect dataset structure in splits.

  Rows are shuffled before splitting.
  
  In case of grouped dataset each group is processed separately, pairs of grouped dataset are returned.

  See [more](https://www.mitpressjournals.org/doi/pdf/10.1162/EVCO_a_00069)"
  ([ds] (split ds :kfold))
  ([ds split-type] (split ds split-type {}))
  ([ds split-type {:keys [seed parallel?] :as opts}]
   (let [rng (if seed (Random. seed) (Random.))
         newopts (assoc opts :rng rng :split-fn (get split-types split-type :kfold))]
     (if (grouped? ds)
       (->> (groups->seq ds)
            ((if parallel? pmap map) #(split-single-ds % split-type newopts))
            (apply map (fn [& gs]
                         {:train (ds/add-or-update-column ds :data (map :train gs))
                          :test (ds/add-or-update-column ds :data (map :test gs))})))
       (split-single-ds ds split-type newopts)))))

