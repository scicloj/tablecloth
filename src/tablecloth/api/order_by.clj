(ns tablecloth.api.order-by
  (:require [tech.v3.dataset :as ds]
            
            [tablecloth.api.utils :refer [iterable-sequence? grouped? process-group-data]]))

(set! *unchecked-math* :warn-on-boxed)

(defn- comparator->fn
  [c]
  (cond
    (fn? c) c
    (= :desc c) #(compare %2 %1)
    :else compare))

(defn asc-desc-comparator
  [orders]
  (if-not (iterable-sequence? orders)
    (comparator->fn orders)
    (cond
      (every? #(= % :asc) orders) nil
      (every? #(= % :desc) orders) #(compare %2 %1)
      :else (let [comparators (mapv comparator->fn orders)]
              (fn ^long [v1 v2]
                (loop [v1 v1
                       v2 v2
                       cmptrs comparators]
                  (if-let [cmptr (first cmptrs)]
                    (let [^long c (cmptr (first v1) (first v2))]
                      (if-not (zero? c)
                        c
                        (recur (rest v1) (rest v2) (rest cmptrs))))
                    0)))))))

(defn- sort-fn
  [columns-or-fn comp-fn]
  (cond
    (iterable-sequence? columns-or-fn) (if comp-fn
                                         (fn [ds]
                                           (ds/sort-by ds
                                                       (apply juxt (map #(if (fn? %)
                                                                           %
                                                                           (fn [ds] (get ds %))) columns-or-fn))
                                                       comp-fn))
                                         (fn [ds]
                                           (ds/sort-by ds
                                                       (apply juxt (map #(if (fn? %)
                                                                           %
                                                                           (fn [ds] (get ds %))) columns-or-fn)))))
    (fn? columns-or-fn) (if comp-fn
                          (fn [ds] (ds/sort-by ds columns-or-fn comp-fn))
                          (fn [ds] (ds/sort-by ds columns-or-fn)))
    :else (if comp-fn
            (fn [ds] (ds/sort-by-column ds columns-or-fn comp-fn))
            (fn [ds] (ds/sort-by-column ds columns-or-fn)))))

(defn order-by
  "Order dataset by:
  - column name
  - columns (as sequence of names)
  - key-fn
  - sequence of columns / key-fn
  Additionally you can ask the order by:
  - :asc
  - :desc
  - custom comparator function"
  ([ds columns-or-fn] (order-by ds columns-or-fn nil))
  ([ds columns-or-fn comparators] (order-by ds columns-or-fn comparators nil))
  ([ds columns-or-fn comparators {:keys [parallel?]}]
   (let [comparators (or comparators (if (iterable-sequence? columns-or-fn)
                                       (repeat (count columns-or-fn) :asc)
                                       [:asc]))
         sorting-fn (->> comparators
                         asc-desc-comparator
                         (sort-fn columns-or-fn))]
     
     (if (grouped? ds)
       (process-group-data ds sorting-fn parallel?)
       (sorting-fn ds)))))
