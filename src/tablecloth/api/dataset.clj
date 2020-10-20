(ns tablecloth.api.dataset
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            [tech.v3.protocols.dataset :as prot]
            [tech.v3.dataset.print :as p]
            
            [tablecloth.api.utils :refer [iterable-sequence?]]))

;;;;;;;;;;;;;;;;;;;;;
;; DATASET CREATION
;;;;;;;;;;;;;;;;;;;;;

(defn dataset?
  "Is `ds` a `dataset` type?"
  [ds]
  (satisfies? prot/PColumnarDataset ds))

(defn- fix-map-dataset
  "If map contains value which is not a sequence, convert it to a sequence."
  [map-ds]
  (let [c (if-let [first-seq (->> map-ds
                                  (vals)
                                  (filter iterable-sequence?)
                                  (first))]
            (count first-seq)
            1)]
    (apply array-map (interleave (keys map-ds)
                                 (map #(if (iterable-sequence? %) % (repeat c %)) (vals map-ds))))))

(defn dataset
  "Create `dataset`.
  
  Dataset can be created from:

  * single value
  * map of values and/or sequences
  * sequence of maps
  * sequence of columns
  * file or url"
  ([] (ds/new-dataset nil))
  ([data]
   (dataset data nil))
  ([data {:keys [single-value-column-name]
          :or {single-value-column-name :$value}
          :as options}]
   (cond
     (dataset? data) data
     (map? data) (ds/->dataset (fix-map-dataset data) options)
     (and (iterable-sequence? data)
          (every? iterable-sequence? data)
          (every? #(= 2 (count %)) data)) (dataset (apply array-map (mapcat identity data)) options)
     (and (iterable-sequence? data)
          (every? col/is-column? data)) (ds/new-dataset options data)
     (not (seqable? data)) (ds/->dataset [{single-value-column-name data}] options)
     :else (ds/->dataset data options))))

(defn shape
  "Returns shape of the dataset [rows, cols]"
  [ds]
  [(ds/row-count ds)
   (ds/column-count ds)])

(defn- columns-info
  [ds]
  (dataset (->> (ds/columns ds)
                (map meta))
           {:dataset-name (str (ds/dataset-name ds) " :column info")}))

(defn info
  ([ds] (info ds :descriptive))
  ([ds result-type]
   (condp = result-type
     :descriptive (ds/descriptive-stats ds)
     :columns (columns-info ds)
     (let [grouped? (boolean (:grouped? (meta ds)))
           nm (ds/dataset-name ds)
           inf {:name nm
                :grouped? grouped?}]
       (dataset (if grouped?
                  (assoc inf :groups (ds/row-count inf))
                  (assoc inf
                         :rows (ds/row-count ds)
                         :columns (ds/column-count ds)))
                {:dataset-name (str nm " :basic info")})))))

(defn columns
  ([ds] (columns ds :as-seq))
  ([ds result-type]
   (let [cols (ds/columns ds)]
     (if (= :as-map result-type)
       (zipmap (ds/column-names ds) cols)
       cols))))

(defn rows
  ([ds] (rows ds :as-seq))
  ([ds result-type]
   (if (= :as-maps result-type)
     (ds/mapseq-reader ds)
     (ds/value-reader ds))))

(defn print-dataset
  ([ds] (println (p/dataset->str ds)))
  ([ds options] (println (p/dataset->str ds options))))
