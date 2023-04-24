(ns tablecloth.column.api.lift-operators
  (:require [tablecloth.column.api.utils :refer [do-lift lift-op]]))

(def serialized-lift-fn-lookup
  {['*
    '+
    '-
    '/
    'abs
    'acos
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
    'max
    'min
    'next-down
    'next-up
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
               {:new-args {'[x] {'data 'x}
                           '[x options] {'data 'x}}}))
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
              {:new-args {'[x] {'arg 'x}
                          '[x options] {'arg 'x}}}))
   ['percentiles] (fn [fn-sym fn-meta]
                    (lift-op
                     fn-sym fn-meta
                     {:new-args {'[x percentiles] {'data 'x
                                                   'percentages 'percentiles}
                                 '[x percentiles options] {'data 'x
                                                           'percentages 'percentiles}}}))
   ['shift] (fn [fn-sym fn-meta]
              (lift-op
               fn-sym fn-meta
               {:new-args {'[x n] {'rdr 'x}}}))
   ['descriptive-statistics] (fn [fn-sym fn-meta]
                              (lift-op
                               fn-sym fn-meta
                               {:new-args {'[x] {'rdr 'x}
                                           '[x stats-names] {'rdr 'x}
                                           '[x stats-names options] {'rdr 'x}
                                           '[x stats-names stats-data options] {'src-rdr 'x}}}))
   ['quartiles] (fn [fn-sym fn-meta]
                  (lift-op
                   fn-sym fn-meta
                   {:new-args {'[x] {'item 'x}
                               '[x options] {'item 'x}}}))
   ['fill-range] (fn [fn-sym fn-meta]
                  (lift-op
                   fn-sym fn-meta
                   {:new-args {'[x max-span] {'numeric-data 'x}}}))
   ['reduce-min
    'reduce-max
    'reduce-*
    'reduce-+] (fn [fn-sym fn-meta]
                 (lift-op

                  fn-sym fn-meta
                  {:new-args {'[x] {'rdr 'x}}}))
  ['mean-fast
   'sum-fast
   'magnitude-squared] (fn [fn-sym fn-meta]
                         (lift-op
                          fn-sym fn-meta
                          {:new-args {'[x] {'data 'x}}}))
   ['kendalls-correlation
    'pearsons-correlation
    'spearmans-correlation] (fn [fn-sym fn-meta]
                              (lift-op
                               fn-sym fn-meta
                               {:new-args {'[x y] {'lhs 'x
                                                   'rhs 'y}
                                           '[x y options] {'lhs 'x
                                                           'rhs 'y}}}))
   ['cumprod
    'cumsum
    'cummax
    'cummin] (fn [fn-sym fn-meta]
                (lift-op
                 fn-sym fn-meta
                 {:new-args {'[x] {'data 'x}}}))
   ['normalize] (fn [fn-sym fn-meta]
                  (lift-op
                   fn-sym fn-meta
                   {:new-args {'[x] {'item 'x}}}))
   ['<
    '<=
    '>
    '>=] (fn [fn-sym fn-meta]
           (lift-op
            fn-sym fn-meta
            {:new-args {'[x y] {'lhs 'x
                                'rhs 'y}
                        '[x y z] {'lhs 'x
                                  'mid 'y
                                  'rhs 'z}}}))
   ['equals] (fn [fn-sym fn-meta]
               (lift-op
                fn-sym fn-meta
                {:new-args {'[x y & args] {'lhs 'x
                                           'rhs 'y}}}))
   ['distance
    'dot-product
    'eq
    'not-eq
    'or
    'distance-squared
    'and] (fn [fn-sym fn-meta]
            (lift-op
             fn-sym fn-meta
             {:new-args {'[x y] {'lhs 'x
                                 'rhs 'y}}}))
   ['magnitude] (fn [fn-sym fn-meta]
                  (lift-op
                   fn-sym fn-meta
                   {:new-args {'[x] {'item 'x}
                               '[x options] {'item 'x
                                             '_options 'options}}}))})


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
           'tablecloth.column.api.operators
           'tech.v3.datatype.functional
           '[* + - / < <= > >= abs and bit-and bit-and-not bit-clear bit-flip
             bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor
             even? identity infinite? max min neg? not odd? odd? or pos? quot rem
             unsigned-bit-shift-right zero?]
           "src/tablecloth/column/api/operators.clj")
  ,)


(comment

  (def mylift (fn [fn-sym fn-meta]
           (lift-op
            fn-sym fn-meta
            {:new-args {'[x y] {'lhs 'x
                                'rhs 'y}
                        '[x y z] {'lhs 'x
                                  'mid 'y
                                  'rhs 'z}}}
            #_{:new-args '([x y z])
             :new-args-lookup {'lhs 'x
                               'mid 'y
                               'rhs 'z}})))

  (def mappings (ns-publics 'tech.v3.datatype.functional))

  (mylift 'tech.v3.datatype.functional/> (meta (get mappings '>)))

  tech.v3.datatype.functional/>

  )
