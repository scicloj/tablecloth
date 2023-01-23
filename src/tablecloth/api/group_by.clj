(ns tablecloth.api.group-by
  (:refer-clojure :exclude [group-by pmap])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]
            [tech.v3.parallel.for :refer [pmap]]
            
            [tablecloth.api.utils :refer [iterable-sequence? ->str column-names parallel-concat grouped? mark-as-group map-inst?]]
            [tablecloth.api.dataset :refer [dataset]]))

(defn- find-group-indexes
  "Calulate indexes for groups"
  [ds grouping-selector selected-keys]
  (cond
    (map-inst? grouping-selector) grouping-selector
    (iterable-sequence? grouping-selector) (ds/group-by->indexes
                                            (ds/select-columns ds (column-names ds grouping-selector)) identity)
    (fn? grouping-selector) (ds/group-by->indexes
                             (ds/select-columns ds (column-names ds selected-keys)) grouping-selector)
    :else (ds/group-by-column->indexes ds grouping-selector)))

(defn- subdataset
  [ds id k idxs]
  (-> ds
      (ds/select :all idxs)
      (ds/set-dataset-name (str "Group: " k))
      (vary-meta assoc :group-id id)))

(defn- group-indexes->map
  "Create map representing grouped dataset from indexes"
  [ds id [k idxs]]
  {:name k
   :group-id id
   :data (subdataset ds id k idxs)})

(defn- group-by->dataset
  "Create grouped dataset from indexes"
  [ds group-indexes options]
  (-> (map-indexed (partial group-indexes->map ds) group-indexes)
      (dataset options)
      (mark-as-group)))

;; TODO: maybe make possible nested grouping and ungrouping?
(defn group-by
  "Group dataset by:

  - column name
  - list of columns
  - map of keys and row indexes
  - function getting map of values

  Options are:

  - select-keys - when grouping is done by function, you can limit fields to a `select-keys` seq.
  - result-type - return results as dataset (`:as-dataset`, default) or as map of datasets (`:as-map`) or as map of row indexes (`:as-indexes`) or as sequence of (sub)datasets
  - other parameters which are passed to `dataset` fn

  When dataset is returned, meta contains `:grouped?` set to true. Columns in dataset:

  - name - group name
  - group-id - id of the group (int)
  - data - group as dataset"
  ([ds grouping-selector] (group-by ds grouping-selector nil))
  ([ds grouping-selector {:keys [select-keys result-type]
                          :or {result-type :as-dataset select-keys :all}
                          :as options}]
   (let [selected-keys (column-names ds select-keys)
         group-indexes (find-group-indexes ds grouping-selector selected-keys)]
     (condp = result-type
       :as-indexes group-indexes
       :as-seq (->> group-indexes ;; java.util.HashMap
                    (map-indexed (fn [id [k idxs]] (subdataset ds id k idxs))))
       :as-map (->> group-indexes ;; java.util.HashMap
                    (map-indexed (fn [id [k idxs]] [k (subdataset ds id k idxs)])) 
                    (into {}))
       (group-by->dataset ds group-indexes options)))))

(defn groups->map
  "Convert grouped dataset to the map of groups"
  [ds]
  (assert (grouped? ds) "Apply on grouped dataset only")
  (zipmap (ds :name) (ds :data)))

(defn groups->seq
  "Convert grouped dataset to seq of the groups"
  [ds]
  (assert (grouped? ds) "Apply on grouped dataset only")
  (seq (ds :data)))

;; ungrouping

(defn- add-groups-as-columns
  "Add group columns to the result of ungrouping"
  [ds col1 col2 columns]
  (->> columns
       (clojure.core/concat col1 col2)
       (remove nil?)
       (ds/new-dataset)
       (assoc ds :data)))

(defn- group-name-map->cols
  "create columns with repeated value `v` from group name if it's map."
  [name count]
  (->> name
       (repeat count)
       (ds/->dataset)
       (ds/columns)))

(defn- maybe-name
  [possible-name default-name]
  (if (true? possible-name)
    default-name
    possible-name))

(defn- group-name-seq->map
  [name add-group-as-column]
  (let [cn (if (iterable-sequence? add-group-as-column)
             add-group-as-column
             (let [tn (maybe-name add-group-as-column :$group-name)]
               (map-indexed #(keyword (str %2 "-" %1)) (repeat (->str tn)))))]
    (zipmap cn name)))

(defn- group-name-seq->cols
  [name add-group-as-column count]
  (-> name
      (group-name-seq->map add-group-as-column)
      (group-name-map->cols count)))

(defn- group-as-column->seq
  "Convert group name to a seq of columns"
  [add-group-as-column separate? name count]
  (when add-group-as-column
    (cond
      (and separate? (map? name)) (group-name-map->cols name count)
      (and separate? (iterable-sequence? name)) (group-name-seq->cols name add-group-as-column count)
      :else [(col/new-column (maybe-name add-group-as-column :$group-name) (repeat count name))])))

(defn- group-id-as-column->seq
  "Convert group id to as seq of columns"
  [add-group-id-as-column count group-id]
  (when add-group-id-as-column
    [(col/new-column (maybe-name add-group-id-as-column :$group-id) (dtype/const-reader group-id count))]))

(defn- prepare-ds-for-ungrouping
  "Add optional group name and/or group-id as columns to a result of ungrouping."
  [ds add-group-as-column add-group-id-as-column separate? parallel?]
  (let [mapper (if parallel? pmap map)]
    (->> ds
         (ds/mapseq-reader)
         (mapper (fn [{:keys [name group-id data] :as ds}]
                   (if (or add-group-as-column
                           add-group-id-as-column)
                     (let [count (ds/row-count data)]
                       (add-groups-as-columns ds
                                              (group-as-column->seq add-group-as-column separate? name count)
                                              (group-id-as-column->seq add-group-id-as-column count group-id)
                                              (ds/columns data)))
                     ds))))))

(defn- order-ds-for-ungrouping
  "Order results by group name or leave untouched."
  [prepared-ds order-by-group?]
  (cond
    (= :desc order-by-group?) (sort-by :name #(compare %2 %1) prepared-ds)
    order-by-group? (sort-by :name prepared-ds)
    :else prepared-ds))

(defn ungroup
  "Concat groups into dataset.

  When `add-group-as-column` or `add-group-id-as-column` is set to `true` or name(s), columns with group name(s) or group id is added to the result.

  Before joining the groups groups can be sorted by group name."
  ([ds] (ungroup ds nil))
  ([ds {:keys [order? add-group-as-column add-group-id-as-column separate? dataset-name parallel?]
        :or {separate? true}
        :as options}]
   (assert (grouped? ds) "Works only on grouped dataset")
   (let [concatter (if parallel? parallel-concat ds/concat)]
     (-> ds
         (prepare-ds-for-ungrouping add-group-as-column add-group-id-as-column separate? parallel?)
         (order-ds-for-ungrouping order?)
         (->> (map :data)
              (apply concatter))
         (ds/set-dataset-name (or dataset-name (ds/dataset-name ds)))))))

;; processing and ungrouping in-place
;; instead of creating subdatasets create seq of maps

(defn- add-groups-to-a-map
  "Convert group name to a seq of columns"
  [curr-row add-group-as-column separate? name]
  (cond
    (and separate? (map? name)) (concat name curr-row)
    (and separate? (iterable-sequence? name)) (concat (group-name-seq->map name add-group-as-column) curr-row)
    :else (conj curr-row [(maybe-name add-group-as-column :$group-name) name])))

(defn- process-and-ungroup
  [ds process-fn add-group-as-column add-group-id-as-column separate? parallel?]
  (let [mapper (if parallel? pmap map)]
    (->> ds
         (ds/mapseq-reader)
         (mapper (fn [{:keys [name group-id data]}]
                   (into (array-map) (cond-> (process-fn data)
                                       add-group-id-as-column (conj [(maybe-name add-group-id-as-column :$group-id) group-id])
                                       add-group-as-column (add-groups-to-a-map add-group-as-column separate? name))))))))

(defn process->ungroup
  ([ds process-fn] (process->ungroup ds process-fn nil))
  ([ds process-fn {:keys [order? add-group-as-column add-group-id-as-column separate? dataset-name parallel?]
                   :or {separate? true}
                   :as options}]
   (assert (grouped? ds) "Works only on grouped dataset")
   (-> ds
       (order-ds-for-ungrouping order?)
       (process-and-ungroup process-fn add-group-as-column add-group-id-as-column separate? parallel?)
       (dataset)
       (ds/set-dataset-name (or dataset-name (ds/dataset-name ds))))))
