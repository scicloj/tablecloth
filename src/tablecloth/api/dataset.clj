(ns tablecloth.api.dataset
  (:refer-clojure :exclude [concat])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            [tech.v3.protocols.dataset :as prot]
            [tech.v3.dataset.print :as p]
            [tech.v3.tensor :as tensor]
            [tech.v3.dataset.tensor :as ds-tensor]
            
            [tablecloth.api.utils :refer [iterable-sequence? grouped? mark-as-group map-inst?]]))

;;;;;;;;;;;;;;;;;;;;;
;; DATASET CREATION
;;;;;;;;;;;;;;;;;;;;;

(defn dataset?
  "Is `ds` a `dataset` type?"
  [ds]
  (satisfies? prot/PColumnarDataset ds))

(defn empty-ds?
  [ds]
  (zero? (ds/row-count ds)))

#_(defn- fix-map-dataset
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

(def ^:private numerical-classes (set (map #(Class/forName %) ["[[B" "[[S" "[[I" "[[J" "[[F" "[[D"])))

(defn- from-tensor
  [data column-names layout dataset-name]
  (let [t (tensor/->tensor data)
        t (-> (if (= layout :as-columns) (tensor/transpose t [1 0]) t)
              (ds-tensor/tensor->dataset dataset-name))]
    (if column-names
      (ds/rename-columns t (zipmap (range (ds/column-count t)) column-names))
      t)))

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
  ([data {:keys [single-value-column-name column-names layout dataset-name]
          :or {single-value-column-name :$value layout :as-rows}
          :as options}]
   (cond
     (dataset? data) data
     (map-inst? data) (ds/->dataset data options)
     (and (iterable-sequence? data)
          (every? iterable-sequence? data)
          (every? #(and (= 2 (count %))
                        (or (keyword? (first %))
                            (string? (first %)))) data)) (dataset (apply array-map (mapcat identity data)) options)
     (and (iterable-sequence? data)
          (every? col/is-column? data)) (ds/new-dataset options data)
     (or (numerical-classes (class data))
         (and (iterable-sequence? data)
              (not-every? map? data))) (from-tensor data column-names layout dataset-name)
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
  ([ds] (columns ds :as-seqs))
  ([ds result-type]
   (let [cols (ds/columns ds)]
     (case result-type
       :as-map (zipmap (ds/column-names ds) cols)
       :as-double-arrays (into-array (map double-array (ds/columns ds)))
       :as-seqs cols
       cols))))

(defn rows
  ([ds] (rows ds :as-seqs))
  ([ds result-type]
   (case result-type
     :as-maps (ds/mapseq-reader ds)
     :as-double-arrays (into-array (map double-array (ds/value-reader ds)))
     :as-seqs (ds/value-reader ds)
     (ds/value-reader ds))))

(defn print-dataset
  ([ds] (println (p/dataset->str ds)))
  ([ds options] (println (p/dataset->str ds options))))

;;

(defn- do-concat
  [concat-fs ds & datasets]
  (let [res (apply concat-fs ds datasets)]
    (if (and (grouped? ds)
             (every? grouped? datasets))
      (-> res
          (ds/add-or-update-column :group-id (range (ds/row-count res)))
          (mark-as-group))
      res)))

(defn concat [dataset & datasets] (apply do-concat ds/concat dataset datasets))
(defn concat-copying [dataset & datasets] (apply do-concat ds/concat-copying dataset datasets))
