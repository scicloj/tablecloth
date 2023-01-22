(ns tablecloth.column.api.lift-operators
  (:require [tablecloth.column.api.utils :refer [do-lift lift-op]]))

(def serialized-lift-fn-lookup
  {['*
    '+
    '-
    '/
    '<
    '<=
    '>
    '>=
    'abs
    'acos
    'and
    'asin
    'atan
    'atan2
    'bit-and
    'bit-and-not
    'bit-clear
    'bit-flip
    'bit-not
    'bit-or
    'bit-set
    'bit-shift-left
    'bit-shift-right
    #_bit-test ;; can't get this to work yet.
    'bit-xor
    'cbrt
    'ceil
    'cos
    'cosh
    'distance
    'distance-squared
    'dot-product
    'eq
    'equals
    'exp
    'expm1
    'floor
    'get-significand
    'hypot
    'identity
    'ieee-remainder
    'log
    'log10
    'log1p
    'logistic
    'magnitude
    'max
    'min
    'next-down
    'next-up
    'normalize
    'not-eq
    'or
    'pow
    'quot
    'rem
    'rint
    'signum
    'sin
    'sinh
    'sq
    'sqrt
    'tan
    'tanh
    'to-degrees
    'to-radians
    'ulp
    'unsigned-bit-shift-right] lift-op
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
   ['even?
    'finite?
    'infinite?
    'mathematical-integer?
    'nan?
    'neg?
    'not
    'odd?
    'pos?
    'round
    'zero?]
   (fn [fn-sym fn-meta]
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
                                        'options 'options}}))
   ['shift] (fn [fn-sym fn-meta]
              (lift-op
               fn-sym fn-meta
               {:new-args '([col n])
                :new-args-lookup {'rdr 'col
                                  'n 'n}}))})

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
           '[* + - / < <= > >= abs and bit-and bit-and-not bit-clear bit-flip
             bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor
             even? identity infinite? max min neg? not odd? odd? or pos? quot rem
             unsigned-bit-shift-right zero?]
           "src/tablecloth/column/api/operators.clj")
  ,)


