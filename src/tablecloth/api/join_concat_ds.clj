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
            [tablecloth.api.utils :refer [column-names grouped? process-group-data]])
  (:import [org.roaringbitmap RoaringBitmap]))

;; joins

(defn- multi-join
  [ds-left ds-right join-fn cols-left cols-right options]
  (let [join-column-name (gensym "^___join_column_hash")
        dsl (join-columns ds-left join-column-name cols-left {:result-type hash
                                                              :drop-columns? false})
        dsr (join-columns ds-right join-column-name cols-right {:result-type hash
                                                                :drop-columns? false})
        joined-ds (join-fn join-column-name dsl dsr options)]
    (-> joined-ds
        (ds/drop-columns [join-column-name (-> joined-ds
                                               (meta)
                                               :right-column-names
                                               (get join-column-name))]))))

(defn- resolve-join-column-names
  [ds-left ds-right columns-selector]
  (if (map? columns-selector)
    (-> columns-selector
        (update :left (partial column-names ds-left))
        (update :right (partial column-names ds-right)))
    (let [left (column-names ds-left columns-selector)
          right  (column-names ds-right columns-selector)]
      {:left left :right right})))

(defmacro make-join-fns
  [join-fns-list]
  `(do
     ~@(for [[n impl] join-fns-list]
         `(defn ~n
            ([~'ds-left ~'ds-right ~'columns-selector] (~n ~'ds-left ~'ds-right ~'columns-selector nil))
            ([~'ds-left ~'ds-right ~'columns-selector ~'options]
             (let [cols# (resolve-join-column-names ~'ds-left ~'ds-right ~'columns-selector)
                   cols-left# (:left cols#)
                   cols-right# (:right cols#)
                   opts# (or ~'options {})]
               (if (= 1 (count cols-left#))
                 (~impl [(first cols-left#) (first cols-right#)] ~'ds-left ~'ds-right opts#)
                 (multi-join ~'ds-left ~'ds-right ~impl cols-left# cols-right# opts#))))))))

(make-join-fns [[left-join j/left-join]
                [right-join j/right-join]
                [inner-join j/inner-join]
                [asof-join j/left-join-asof]])

(defn full-join
  "Join keeping all rows"
  ([ds-left ds-right columns-selector] (full-join ds-left ds-right columns-selector nil))
  ([ds-left ds-right columns-selector options]
   (let [rj (right-join ds-left ds-right columns-selector options)]
     (-> (->> rj
              (ds/concat (left-join ds-left ds-right columns-selector options)))
         (ds/unique-by identity)
         (with-meta (assoc (meta rj) :name "full-join"))))))

(defn- anti-semi-join-fn
  ([nm rows-fn join-column-name dsl dsr] (anti-semi-join-fn nm rows-fn join-column-name dsl dsr nil))
  ([nm rows-fn join-column-name dsl dsr options]
   (let [lj (j/left-join join-column-name dsl dsr options)
         right-columns (:right-column-names (meta lj))
         ^RoaringBitmap missing-column (col/missing (lj (right-columns (if (vector? join-column-name)
                                                                         (second join-column-name)
                                                                         join-column-name))))
         ^RoaringBitmap left-column (col/missing (lj (if (vector? join-column-name)
                                                       (first join-column-name)
                                                       join-column-name)))]
     (-> lj
         (rows-fn (RoaringBitmap/andNot missing-column left-column))
         (drop-columns (vals right-columns))
         (ds/unique-by identity)
         (vary-meta assoc :name nm)))))

(make-join-fns [[anti-join (partial anti-semi-join-fn "anti-join" select-rows)]
                [semi-join (partial anti-semi-join-fn "semi-join" drop-rows)]])


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
