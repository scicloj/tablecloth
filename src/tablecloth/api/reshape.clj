(ns tablecloth.api.reshape
  (:refer-clojure :exclude [group-by])
  (:require [tech.ml.dataset :as ds]
            [tech.ml.dataset.column :as col]
            [tech.v2.datatype :as dtype]
            [clojure.string :as str]

            [tablecloth.api.utils :refer [iterable-sequence? column-names]]
            [tablecloth.api.columns :refer [drop-columns convert-types
                                            reorder-columns rename-columns select-columns]]
            [tablecloth.api.join-separate :refer [join-columns separate-column]]
            [tablecloth.api.unique-by :refer [strategy-fold unique-by]]
            [tablecloth.api.group-by :refer [group-by]]
            [tablecloth.api.missing :refer [drop-missing]]))

(defn- regroup-cols-from-template
  [ds cols target-columns value-name column-split-fn]
  (let [template? (some nil? target-columns)
        pre-groups (->> cols
                        (map (fn [col-name]
                               (let [col (ds col-name)
                                     split (column-split-fn col-name)
                                     buff (if template? {} {value-name col})]
                                 (into buff (mapv (fn [k v] (if k [k v] [v col]))
                                                  target-columns split))))))
        groups (-> (->> target-columns
                        (remove nil?)
                        (map #(fn [n] (get n %)))
                        (apply juxt))
                   (clojure.core/group-by pre-groups)
                   (vals))]
    (map #(reduce merge %) groups)))

(defn- cols->pre-longer
  ([ds cols names value-name column-splitter]
   (let [column-split-fn (cond (instance? java.util.regex.Pattern column-splitter) (comp rest #(re-find column-splitter (str %)))
                               (fn? column-splitter) column-splitter
                               :else vector)]
     (regroup-cols-from-template ds cols names value-name column-split-fn))))

(defn- pre-longer->target-columns
  [ds cnt m]
  (let [new-cols (map (fn [[col-name maybe-column]]
                        (if (col/is-column? maybe-column)
                          (col/set-name maybe-column col-name)
                          (col/new-column col-name (dtype/const-reader maybe-column cnt {:datatype :object})))) m)]
    (ds/append-columns ds new-cols)))

(defn pivot->longer
  "`tidyr` pivot_longer api"
  ([ds columns-selector] (pivot->longer ds columns-selector nil))
  ([ds columns-selector {:keys [target-columns value-column-name splitter drop-missing? datatypes]
                         :or {target-columns :$column
                              value-column-name :$value
                              drop-missing? true}}]
   (let [cols (column-names ds columns-selector)
         target-columns (if (iterable-sequence? target-columns) target-columns [target-columns])
         groups (cols->pre-longer ds cols target-columns value-column-name splitter)
         cols-to-add (keys (clojure.core/first groups))
         ds-template (drop-columns ds cols)
         cnt (ds/row-count ds)]
     (as-> (ds/set-metadata (->> groups                                        
                                 (map (partial pre-longer->target-columns ds-template cnt))
                                 (apply ds/concat))
                            (ds/metadata ds)) final-ds
       (if drop-missing? (drop-missing final-ds cols-to-add) final-ds)
       (if datatypes (convert-types final-ds datatypes) final-ds)
       (reorder-columns final-ds (ds/column-names ds-template) (remove nil? target-columns))))))

(defn- drop-join-leftovers
  [data join-name]
  (drop-columns data (-> (meta data)
                         :right-column-names
                         (get join-name))))

(defn- perform-join
  [join-ds curr-ds join-name]
  (ds/left-join join-name curr-ds join-ds))

(defn- process-column-name
  [concat-columns-with names]
  (if (> (count names) 1)
    (cond
      (string? concat-columns-with) (str/join concat-columns-with names)
      (fn? concat-columns-with) (concat-columns-with names)
      :else names)
    (first names)))

(defn- process-target-name
  [value concat-value-with col-name]
  (cond
    (string? concat-value-with) (str value concat-value-with col-name)
    (fn? concat-value-with) (concat-value-with col-name value)
    :else [col-name value]))

(defn- make-apply-join-fn
  "Perform left-join on groups and create new columns"
  [group-name->names single-value? value-names join-name starting-ds-count fold-fn concat-columns-with concat-value-with]
  (fn [curr-ds {:keys [name data]}]
    (let [col-name (process-column-name concat-columns-with (->> name
                                                                 group-name->names
                                                                 (remove nil?))) ;; source names
          target-names (if single-value?
                         [col-name]
                         (map #(process-target-name % concat-value-with col-name) value-names)) ;; traget column names
          rename-map (zipmap value-names target-names) ;; renaming map
          data (-> data
                   (rename-columns rename-map) ;; rename value column
                   (select-columns (conj target-names join-name)) ;; select rhs for join
                   (perform-join curr-ds join-name) ;; perform left join
                   (drop-join-leftovers join-name))] ;; drop unnecessary leftovers
      (if (> (ds/row-count data) starting-ds-count) ;; in case when there were multiple values, create vectors
        (if fold-fn
          (strategy-fold data (->> target-names
                                   (set)
                                   (partial contains?)
                                   (complement)
                                   (column-names data)) fold-fn {:add-group-as-column true})
          (do (println "WARNING: multiple values in result detected, data should be rolled in.")
              data))
        data))))

(defn pivot->wider
  ([ds columns-selector value-columns] (pivot->wider ds columns-selector value-columns nil))
  ([ds columns-selector value-columns {:keys [fold-fn concat-columns-with concat-value-with]
                                       :or {concat-columns-with "_" concat-value-with "-"}}]
   (let [col-names (column-names ds columns-selector) ;; columns to be unrolled
         value-names (column-names ds value-columns) ;; columns to be used as values
         single-value? (= (count value-names) 1) ;; maybe this is one column? (different name creation rely on this)
         rest-cols (->> (clojure.core/concat col-names value-names)
                        (set)
                        (partial contains?)
                        (complement)
                        (column-names ds)) ;; the columns used in join
         join-on-single? (= (count rest-cols) 1) ;; mayve this is one column? (different join column creation)
         join-name (if join-on-single?
                     (clojure.core/first rest-cols)
                     (gensym (apply str "^____" rest-cols))) ;; generate join column name
         ;; col-to-drop (col-to-drop-name join-name) ;; what to drop after join
         pre-ds (if join-on-single?
                  ds
                  (join-columns ds join-name rest-cols {:result-type :seq
                                                        :drop-columns? true})) ;; t.m.ds doesn't have join on multiple columns, so we need to create single column to be used fo join
         starting-ds (unique-by (select-columns pre-ds join-name)) ;; left join source dataset
         starting-ds-count (ds/row-count starting-ds) ;; how much records to expect
         grouped-ds (group-by pre-ds col-names) ;; group by columns which values will create new columns
         group-name->names (->> col-names
                                (map #(fn [m] (get m %)))
                                (apply juxt)) ;; create function which extract new column name
         result (reduce (make-apply-join-fn group-name->names single-value? value-names
                                            join-name starting-ds-count fold-fn
                                            concat-columns-with concat-value-with)
                        starting-ds
                        (ds/mapseq-reader grouped-ds))] ;; perform join on groups and create new columns
     (-> (if join-on-single? ;; finalize, recreate original columns from join column, and reorder stuff
           result
           (-> (separate-column result join-name rest-cols identity {:drop-column? true})
               (reorder-columns rest-cols)))
         (ds/set-dataset-name (ds/dataset-name ds))))))
