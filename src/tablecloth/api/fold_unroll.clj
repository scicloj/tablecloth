(ns tablecloth.api.fold-unroll
  (:require [tech.v3.dataset :as ds]

            [tablecloth.api.unique-by :refer [unique-by]]
            [tablecloth.api.columns :refer [select-columns reorder-columns]]
            [tablecloth.api.utils :refer [column-names grouped? process-group-data]]))

(defn fold-by
  "Group-by and pack columns into vector - the output data set has a row for each unique combination
  of the provided columns while each remaining column has its valu(es) collected into a vector, similar
  to how clojure.core/group-by works.
  See https://scicloj.github.io/tablecloth/index.html#Fold-by"
  ([ds columns-selector] (fold-by ds columns-selector vec))
  ([ds columns-selector folding-function]
   (unique-by ds columns-selector {:strategy folding-function
                                   :add-group-as-column true})))

(defn- process-unroll
  [ds colnames-set colnames options]
  (let [unrolled-dss (map (fn [colname]
                            (let [opts (assoc options
                                              :datatype (get-in options [:datatypes colname] :object))]
                              [colname (-> ds
                                           (select-columns (complement (partial contains? (disj colnames-set colname))))
                                           (ds/unroll-column colname opts))])) colnames)]
    (-> (fn [[_ curr] [n uds]]
          [_ (ds/add-column curr (uds n))])
        (reduce unrolled-dss)
        (second)
        (reorder-columns (complement (partial contains? colnames-set))))))

(defn unroll
  "Unfolds sequences stored inside a column(s), turning it into multiple columns. Opposite of [[fold-by]].
  Add each of the provided columns to the set that defines the \"uniqe key\" of each row.
  Thus there will be a new row for each value inside the target column(s)' value sequence.
  If you want instead to split the content of the columns into a set of new _columns_, look at [[separate-column]].
  See https://scicloj.github.io/tablecloth/index.html#Unroll"
  ([ds columns-selector] (unroll ds columns-selector nil))
  ([ds columns-selector options]
   (let [colnames (column-names ds columns-selector)
         colnames-set (set colnames)]
     (if (grouped? ds)
       (process-group-data ds #(process-unroll % colnames-set colnames options) (:parallel options))
       (process-unroll ds colnames-set colnames options)))))
