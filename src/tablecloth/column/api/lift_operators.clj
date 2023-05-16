(ns tablecloth.column.api.lift-operators
  (:require [tablecloth.utils.codegen :refer [do-lift]]
            [tablecloth.column.api :refer [column]]
            [tech.v3.datatype.argtypes :refer [arg-type]]))

(defn return-scalar-or-column [item]
  (let [item-type (arg-type item)]
    (if (= item-type :reader)
      (column item)
      item)))

(defn lift-op
  ([fn-sym fn-meta]
   (lift-op fn-sym fn-meta nil))
  ([fn-sym fn-meta {:keys [new-args]}]
   (let [defn (symbol "defn")
         let  (symbol "let")
         docstring (:doc fn-meta)
         original-args (:arglists fn-meta)
         sort-by-arg-count (fn [argslist]
                             (sort #(< (count %1) (count %2)) argslist))]
     (if new-args
      `(~defn ~(symbol (name fn-sym))
        ~(or docstring "")
        ~@(for [[new-arg new-arg-lookup original-arg]
                (map vector (sort-by-arg-count (keys new-args))
                            (sort-by-arg-count (vals new-args))
                            (sort-by-arg-count original-args))
                :let [filtered-original-arg (filter (partial not= '&) original-arg)]]
            (list
             (if new-arg new-arg original-arg)
            `(~let [original-result# (~fn-sym
                                      ~@(for [oldarg filtered-original-arg]
                                          (if (nil? (get new-arg-lookup oldarg))
                                            oldarg
                                            (get new-arg-lookup oldarg))))]
              (return-scalar-or-column original-result#)))))
      `(~defn ~(symbol (name fn-sym)) 
        ~(or docstring "") 
        ~@(for [arg original-args
                :let [[explicit-args rest-arg-expr] (split-with (partial not= '&) arg)]]
            (list
            arg
            `(~let [original-result# ~(if (empty? rest-arg-expr)
                                        `(~fn-sym ~@explicit-args)
                                        `(apply ~fn-sym ~@explicit-args ~(second rest-arg-expr)))]
              (return-scalar-or-column original-result#)))))))))

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


(comment
  (do-lift {:target-ns 'tablecloth.column.api.operators
            :source-ns 'tech.v3.datatype.functional
            :lift-fn-lookup serialized-lift-fn-lookup
            :deps ['tablecloth.column.api.lift_operators]
            :exclusions
            '[* + - / < <= > >= abs and bit-and bit-and-not bit-clear bit-flip
             bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor
             even? identity infinite? max min neg? not odd? odd? or pos? quot rem
             unsigned-bit-shift-right zero?]})
  ,)
