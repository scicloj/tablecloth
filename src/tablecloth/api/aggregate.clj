(ns tablecloth.api.aggregate
  (:refer-clojure :exclude [group-by concat])
  (:require [tech.v3.dataset :refer [row-count concat]]
            
            [tablecloth.api.utils :refer [iterable-sequence? ->str column-names grouped? process-group-data]]
            [tablecloth.api.columns :refer [add-column drop-columns rename-columns]]
            [tablecloth.api.group-by :refer [process->ungroup ungroup group-by]]
            [tablecloth.api.dataset :refer [dataset rows]]
            [tablecloth.api.reshape :refer [pivot->wider]]
            [tablecloth.api.missing :refer [replace-missing]]))

(defn- add-agg-result-from-seq
  [ds k agg-res]
  (reduce (fn [d [id v]]
            (add-column d (keyword (str (->str k) "-" (->str id))) [v])) ds agg-res))

(defn- add-agg-result
  [ds k agg-res]
  (cond
    (map? agg-res) (add-agg-result-from-seq ds k agg-res)  ;(reduce conj tot-res agg-res)
    (iterable-sequence? agg-res) (add-agg-result-from-seq ds k (map-indexed vector agg-res))
    :else (add-column ds k [agg-res])))

(defn- aggregate-map->dataset
  [ds aggregator options]
  (reduce (fn [d [k f]]
            (add-agg-result d k (f ds))) (dataset nil options) aggregator))

(defn- aggregate-map
  [ds aggregator options]
  (-> (aggregate-map->dataset ds aggregator options)
      (rows :as-maps)
      (first)))

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
                      (fn? aggregator) {default-column-name-prefix aggregator}
                      (iterable-sequence? aggregator) (->> aggregator
                                                           (interleave (map #(->> %
                                                                                  (str default-column-name-prefix "-")
                                                                                  keyword) (range)))
                                                           (apply array-map))
                      :else aggregator)]
     (if (grouped? ds)
       (cond
         (true? ungroup?) (process->ungroup ds #(seq (aggregate-map % aggregator options)) (assoc options :add-group-as-column true))
         ungroup? (ungroup (process-group-data ds #(aggregate-map->dataset % aggregator options) parallel?)
                           (assoc options :add-group-as-column true))
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

;;

(defn- default-marginal
  [marginal-fn?]
  (when marginal-fn?
    (if (fn? marginal-fn?) marginal-fn? (partial reduce +))))

(defn- crosstab-impl
  [ds row-names row-vals-fn col-names col-vals-fn
   {:keys [replace-missing? pivot? aggregator missing-value marginal-rows marginal-cols]
    :or {replace-missing? true pivot? true aggregator row-count missing-value 0}}]
  (as-> (-> ds
            (group-by (fn [data-row]
                        {:rows (row-vals-fn (map data-row row-names))
                         :cols (col-vals-fn (map data-row col-names))}))
            (aggregate aggregator)) ds
    (if pivot?
      (as-> (rename-columns ds {:rows "rows/cols"}) ds
        (pivot->wider ds :cols "summary" {:drop-missing? false})
        (if replace-missing?
          (replace-missing ds :all :value missing-value)
          ds)
        (if-let [marginal-fn (default-marginal marginal-rows)]
          (add-column ds :summary (mapv marginal-fn (rows (drop-columns ds ["rows/cols"]))))
          ds)
        (if-let [marginal-fn (default-marginal marginal-cols)]
          (concat ds (aggregate ds (apply array-map (mapcat (fn [col-name]
                                                              [col-name (if (= col-name "rows/cols")
                                                                          (constantly :summary)
                                                                          #(marginal-fn (% col-name)))])
                                                            (column-names ds)))))
          ds))
      ds)))

(defn crosstab
  "Cross tabulation of two sets of columns.

  Creates grouped dataset by [row-selector, col-selector] pairs and calls aggregation on each group.

  Options:

  * pivot? - create pivot table or just flat structure (default: true)
  * replace-missing? - replace missing values? (default: true)
  * missing-value - a missing value (default: 0)
  * aggregator - aggregating function (default: row-count)
  * marginal-rows, marginal-cols - adds row and/or cols, it's a sum if true. Can be a custom fn."
  ([ds row-selector col-selector] (crosstab ds row-selector col-selector nil))
  ([ds row-selector col-selector options]
   (let [row-names (column-names ds row-selector)
         col-names (column-names ds col-selector)
         row-vals-fn (if (= 1 (count row-names)) first vec)
         col-vals-fn (if (= 1 (count col-names)) first vec)]
     (if (grouped? ds)
       (let [res (process-group-data ds #(crosstab-impl % row-names row-vals-fn col-names col-vals-fn options)
                                     (:parallel? options))]
         (if (:ungroup? options) (ungroup res) res))
       (crosstab-impl ds row-names row-vals-fn col-names col-vals-fn options)))))

#_(def ds (dataset {:a [:foo :foo :bar :bar :foo :foo]
                  :b [:one :one :two :one :two :one]
                  :c [:dull :dull :shiny :dull :dull :shiny]}))

#_(crosstab ds :a [:b :c] {:marginal-rows true :marginal-cols true :pivot? true})
;; => _unnamed [3 6]:
;;    | rows/cols | [:one :dull] | [:two :shiny] | [:two :dull] | [:one :shiny] | :summary |
;;    |-----------|-------------:|--------------:|-------------:|--------------:|---------:|
;;    |      :foo |            2 |             0 |            1 |             1 |        4 |
;;    |      :bar |            1 |             1 |            0 |             0 |        2 |
;;    |  :summary |            3 |             1 |            1 |             1 |        6 |

