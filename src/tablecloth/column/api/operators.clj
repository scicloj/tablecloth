(ns tablecloth.column.api.operators
  (:require [tablecloth.column.api.utils :refer [do-lift lift-op]]))

(def serialized-lift-fn-lookup
  {['+
    '-
    '/
    '>
    '>=
    '<
    '<=] lift-op
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
  (do-lift (deserialize-lift-fn-lookup)
           'tablecloth.column.api.lifted-operators
           'tech.v3.datatype.functional
           "src/tablecloth/column/api/lifted_operators.clj")
  ,)
