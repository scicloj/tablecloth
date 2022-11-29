(ns tablecloth.column.api.lift-operators
  (:require [tablecloth.column.api.utils :refer [do-lift lift-op]]))

(def serialized-lift-fn-lookup
  {['+
    '-
    '/
    '>
    '>=
    '<
    '<=
    'distance
    'dot-product
    'eq
    'not-eq
    'or
    'distance-squared
    'and] lift-op
   ['kurtosis
    'sum
    'mean
    'skew
    'variance
    'standard-deviation
    'quartile-3
    'quartile-1
    'median] (fn [fn-sym fn-meta]
              (lift-op
               fn-sym fn-meta
               {:new-args '([col] [col options])
                :new-args-lookup {'data 'col
                                  'options 'options}}))
   ['finite?
    'pos?
    'neg?
    'mathematical-integer?
    'nan?
    'even?
    'zero?
    'not
    'infinite?
    'round
    'odd?] (fn [fn-sym fn-meta]
             (lift-op
              fn-sym fn-meta
              {:new-args '([col] [col options])
               :new-args-lookup {'arg 'col
                                 'options 'options}}))
   ['percentiles] (fn [fn-sym fn-meta]
                    (lift-op
                     fn-sym fn-meta
                     {:new-args '([col percentiles] [col percentiles options])
                      :new-args-lookup {'data 'col,
                                        'percentages 'percentiles,
                                        'options 'options}}))})


(defn deserialize-lift-fn-lookup []
  (reduce (fn [m [symlist liftfn]]
            (loop [syms symlist
                   result m]

              (if (empty? syms)
                result
                (recur (rest syms) (assoc result (first syms) liftfn)))))
          {}
          serialized-lift-fn-lookup))

(comment
  (def fun-mappings (ns-publics 'tech.v3.datatype.functional))

  (-> fun-mappings
      (get 'kurtosis)
      meta)

  (do-lift (deserialize-lift-fn-lookup)
           'tablecloth.column.api.operators
           'tech.v3.datatype.functional
           '[+ - / < <= > >= neg? pos? odd? even? zero? not odd?]
           "src/tablecloth/column/api/operators.clj")
  ,)


