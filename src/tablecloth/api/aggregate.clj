(ns tablecloth.api.aggregate
  (:require [tablecloth.api.utils :refer [iterable-sequence? ->str column-names grouped? process-group-data]]
            [tablecloth.api.group-by :refer [process->ungroup ungroup]]
            [tablecloth.api.dataset :refer [dataset]]))

(defn- add-agg-result-from-seq
  [tot-res k agg-res]
  (reduce (fn [b [id v]]
            (conj b [(keyword (str (->str k) "-" (->str id))) v])) tot-res agg-res))

(defn- add-agg-result
  [tot-res k agg-res]
  (cond
    (map? agg-res) (add-agg-result-from-seq tot-res k agg-res)  ;(reduce conj tot-res agg-res)
    (iterable-sequence? agg-res) (add-agg-result-from-seq tot-res k (map-indexed vector agg-res))
    :else (conj tot-res [k agg-res])))

(defn- aggregate-map
  [ds aggregator]
  (reduce (fn [res [k f]]
            (add-agg-result res k (f ds))) [] aggregator))

(defn- aggregate-map->dataset
  [ds aggregator options]
  (dataset (aggregate-map ds aggregator) options))

(defn aggregate
  "Aggregate dataset by providing:

  - aggregation function
  - map with column names and functions
  - sequence of aggregation functions

  Aggregation functions can return:
  - single value
  - seq of values
  - map of values with column names"
  ([ds aggregator] (aggregate ds aggregator nil))
  ([ds aggregator {:keys [default-column-name-prefix ungroup? parallel?]
                   :or {default-column-name-prefix "summary" ungroup? true}
                   :as options}]
   (let [aggregator (cond
                      (fn? aggregator) {:summary aggregator}
                      (iterable-sequence? aggregator) (->> aggregator
                                                           (interleave (map #(->> %
                                                                                  (str default-column-name-prefix "-")
                                                                                  keyword) (range)))
                                                           (apply array-map))
                      :else aggregator)]
     
     (if (grouped? ds)
       (cond (true? ungroup?) (process->ungroup ds #(seq (aggregate-map % aggregator)) (merge {:add-group-as-column true} options))
             ungroup? (ungroup (process-group-data ds #(aggregate-map->dataset % aggregator options) parallel?)
                               (merge {:add-group-as-column true} options))
             :else (process-group-data ds #(aggregate-map->dataset % aggregator options) parallel?))
       (aggregate-map->dataset ds aggregator options)))))

(defn aggregate-columns
  "Aggregates each column separately"
  ([ds columns-selector column-aggregators] (aggregate-columns ds columns-selector column-aggregators nil))
  ([ds columns-selector column-aggregators options]
   (let [aggregators (if (iterable-sequence? column-aggregators)
                       (cycle column-aggregators)
                       (repeat column-aggregators))
         colnames (column-names ds columns-selector)]
     (aggregate ds (apply array-map (mapcat (fn [aggr col-name]
                                              [col-name #(aggr (% col-name))])
                                            aggregators colnames))
                options))))
