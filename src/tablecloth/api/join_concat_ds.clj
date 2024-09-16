(ns tablecloth.api.join-concat-ds
  (:refer-clojure :exclude [concat])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.join :as j]
            [tech.v3.dataset.column :as col]

            [clojure.set :as s]

            [tablecloth.api.dataset :refer [dataset?]]
            [tablecloth.api.rows :refer [select-rows drop-rows]]
            [tablecloth.api.join-separate :refer [join-columns]]
            [tablecloth.api.columns :refer [drop-columns select-columns]]
            [tablecloth.api.utils :refer [column-names grouped? process-group-data]]))

;; joins

(defn- multi-join
  [join-fn ds-left ds-right cols-left cols-right {:keys [hashing drop-join-column?]
                                                  :or {hashing identity drop-join-column? true}
                                                  :as options}]
  (let [join-column-name (gensym "^___join_column_hash")
        dsl (join-columns ds-left join-column-name cols-left (assoc options
                                                                    :result-type hashing
                                                                    :drop-columns? false))
        dsr (join-columns ds-right join-column-name cols-right (assoc options
                                                                      :result-type hashing
                                                                      :drop-columns? false))
        joined-ds (join-fn join-column-name dsl dsr (or options {}))]
    (if drop-join-column?
      (ds/drop-columns joined-ds [join-column-name (-> joined-ds
                                                       (meta)
                                                       :right-column-names
                                                       (get join-column-name))])
      joined-ds)))

(defn- resolve-join-column-names
  [ds-left ds-right columns-selector]
  (if (map? columns-selector)
    (-> columns-selector
        (update :left (partial column-names ds-left))
        (update :right (partial column-names ds-right)))
    (let [left (column-names ds-left columns-selector)
          right  (column-names ds-right columns-selector)]
      {:left left :right right})))

(defn- apply-join
  [impl ds-left ds-right columns-selector options]
  (let [cols (resolve-join-column-names ds-left ds-right columns-selector)
        cols-left (:left cols)
        cols-right (:right cols)
        hashing (:hashing options)]
    (if (and (= 1 (count cols-left)) (not hashing))
      (impl [(first cols-left) (first cols-right)] ds-left ds-right (or options {}))
      (multi-join impl ds-left ds-right cols-left cols-right options))))

(defn- automatic-columns-selector [ds-left ds-right]
   (let [cols-l (set (column-names ds-left))
         cols-r (set (column-names ds-right))]
         (vec (s/intersection cols-l cols-r))) )

(defn left-join
  "Applies the left-join operation on the datasets. If no automatic selector is
  provided, common columns between two datasets are used as column-selectors. Options is a map with following keys -
   - hashing - Hashing function to use (default identity)
   - drop-join-column? - Remove joined columns (default true)"
  ([ds-left ds-right]
   (left-join ds-left ds-right (automatic-columns-selector ds-left ds-right)))
  ([ds-left ds-right columns-selector] (left-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (apply-join j/left-join ds-left ds-right columns-selector options)))

(defn right-join
  ([ds-left ds-right]
   (right-join ds-left ds-right (automatic-columns-selector ds-left ds-right)))
  ([ds-left ds-right columns-selector] (right-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (apply-join j/right-join ds-left ds-right columns-selector options)))

(defn inner-join
  ([ds-left ds-right]
   (inner-join ds-left ds-right (automatic-columns-selector ds-left ds-right)))
  ([ds-left ds-right columns-selector] (inner-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (apply-join j/inner-join ds-left ds-right columns-selector options)))

(defn asof-join
  ([ds-left ds-right]
   (asof-join ds-left ds-right (automatic-columns-selector ds-left ds-right)))
  ([ds-left ds-right columns-selector] (asof-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (apply-join j/left-join-asof ds-left ds-right columns-selector options)))

(defn- full-join-wrapper
  [columns-selector ds-left ds-right options]
  (let [[left right] (if (sequential? columns-selector)
                       columns-selector
                       (repeat 2 columns-selector))]
    (j/pd-merge ds-left ds-right (assoc options :left-on left :right-on right :how :outer))))

(defn full-join
  "Join keeping all rows"
  ([ds-left ds-right]
   (full-join ds-left ds-right (automatic-columns-selector ds-left ds-right)))
  ([ds-left ds-right columns-selector] (full-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (apply-join full-join-wrapper ds-left ds-right columns-selector options)))

(defn- semi-anti-join-indexes
  [ds-left ds-right columns-selector options]
  (-> (:lhs-indexes (apply-join j/hash-join ds-left ds-right columns-selector
                                (assoc options
                                       :lhs-missing? true
                                       :drop-join-column? false)))
      (distinct)))

(defn semi-join
  ([ds-left ds-right]
   (semi-join ds-left ds-right (automatic-columns-selector ds-left ds-right)))
  ([ds-left ds-right columns-selector] (semi-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (->> (semi-anti-join-indexes ds-left ds-right columns-selector options)
        (select-rows ds-left))))

(defn anti-join
  ([ds-left ds-right]
    (anti-join ds-left ds-right (automatic-columns-selector ds-left ds-right)))
  ([ds-left ds-right columns-selector] (anti-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (->> (semi-anti-join-indexes ds-left ds-right columns-selector options)
        (drop-rows ds-left))))


(defn cross-join
  "Cross product from selected columns"
  ([ds-left ds-right] (cross-join ds-left ds-right :all))
  ([ds-left ds-right columns-selector] (cross-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector {:keys [unique?] :or {unique? false} :as options}]
   (let [{:keys [left right]} (resolve-join-column-names ds-left ds-right columns-selector)
         dl (select-columns ds-left left)
         dr (select-columns ds-right right)]
     (j/pd-merge (if unique? (ds/unique-by dl identity) dl)
                 (if unique? (ds/unique-by dr identity) dr)
                 (merge options {:how :cross})))))

(defn expand
  "TidyR expand.

  Creates all possible combinations of selected columns."
  [ds columns-selector & r]
  (if (grouped? ds)
    (process-group-data ds #(apply expand % columns-selector r) true)
    (let [ds1 (if (dataset? columns-selector)
                columns-selector
                (ds/unique-by (select-columns ds (column-names ds columns-selector)) identity))]
      (if-not (seq r)
        ds1
        (cross-join ds1 (apply expand ds r))))))

(defn complete
  "TidyR complete.

  Fills a dataset with all possible combinations of selected columns. When a given combination doesn't exist, missing values are created."
  [ds columns-selector & r]
  (if (grouped? ds)
    (process-group-data ds #(apply complete % columns-selector r) true)
    (let [expanded (apply expand ds columns-selector r)
          ecnames (column-names expanded)
          lj (left-join expanded ds ecnames)]
      (drop-columns lj (vals (select-keys (:right-column-names (meta lj)) ecnames))))))

;; set operations

(defn intersect
  ([ds-left ds-right] (intersect ds-left ds-right nil))
  ([ds-left ds-right options]
   (-> (semi-join ds-left ds-right (distinct (clojure.core/concat (ds/column-names ds-left)
                                                                  (ds/column-names ds-right))) options)
       (vary-meta assoc :name "intersection"))))

(defn difference
  ([ds-left ds-right] (difference ds-left ds-right nil))
  ([ds-left ds-right options]
   (-> (anti-join ds-left ds-right (distinct (clojure.core/concat (ds/column-names ds-left)
                                                                  (ds/column-names ds-right))) options)
       (vary-meta assoc :name "difference"))))

(defn union
  [ds & datasets]
  (-> (apply ds/concat ds datasets)
      (ds/unique-by identity)
      (vary-meta assoc :name "union")))

(defn- add-empty-missing-column
  [ds name]
  (let [cnt (ds/row-count ds)]
    (->> (repeat cnt nil)
         (col/new-column name)
         (ds/add-column ds))))

(defn- add-empty-missing-columns
  [ds-left ds-right]
  (let [cols-l (set (ds/column-names ds-left))
        cols-r (set (ds/column-names ds-right))
        diff-l (s/difference cols-r cols-l)
        diff-r (s/difference cols-l cols-r)
        ds-left+ (reduce add-empty-missing-column ds-left diff-l)
        ds-right+ (reduce add-empty-missing-column ds-right diff-r)]
    (ds/concat ds-left+ ds-right+)))

(defn bind
  [ds & datasets]
  (reduce #(add-empty-missing-columns %1 %2) ds datasets))

;;

(defn append
  "Concats columns of several datasets"
  [ds & datasets]
  (reduce #(ds/append-columns %1 (ds/columns %2)) ds datasets))
