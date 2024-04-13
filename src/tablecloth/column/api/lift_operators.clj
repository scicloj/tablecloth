(ns tablecloth.column.api.lift-operators
  (:require [tablecloth.utils.codegen :refer [do-lift]]
            [tablecloth.column.api.column :refer [column]]
            [tech.v3.datatype.argtypes :refer [arg-type]]))

(defn get-meta [fn-sym]
  (-> fn-sym resolve meta))

(defn get-arglists [fn-sym]
  (-> fn-sym get-meta :arglists))

(defn get-docstring [fn-sym]
  (-> fn-sym get-meta :doc))

(defn return-scalar-or-column [item]
  (let [item-type (arg-type item)]
    (if (= item-type :reader)
      (column item)
      item)))

(defn lift-op
  ([fn-sym]
   (lift-op fn-sym nil))
  ([fn-sym {:keys [new-args]}]
   (let [defn (symbol "defn")
         let  (symbol "let")
         docstring (get-docstring fn-sym)
         original-args (get-arglists fn-sym)
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
  {['<
    '<=
    '>
    '>=
    '*
    '+
    '-
    '/
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
    'cumprod
    'cumsum
    'cummax
    'cummin
    'descriptive-statistics
    'distance
    'distance-squared
    'dot-product
    'even?
    'equals
    'exp
    'expm1
    'eq
    'fill-range
    'finite?
    'floor
    'get-significand
    'hypot
    'identity
    'ieee-remainder
    'infinite?
    'kendalls-correlation
    'kurtosis
    'log
    'log10
    'log1p
    'logistic
    'mathematical-integer?
    'magnitude
    'magnitude-squared
    'max
    'mean
    'mean-fast
    'median
    'min
    'nan?
    'neg?
    'next-down
    'next-up
    'normalize
    'not
    'not-eq
    'odd?
    'or
    'pearsons-correlation
    'percentiles
    'pos?
    'pow
    'quartiles
    'quartile-1
    'quartile-3
    'quot
    'reduce-min
    'reduce-max
    'reduce-*
    'reduce-+
    'rem
    'rint
    'round
    'skew
    'shift
    'signum
    'sin
    'sinh
    'spearmans-correlation
    'sq
    'sqrt
    'standard-deviation
    'sum
    'sum-fast
    'tan
    'tanh
    'to-degrees
    'to-radians
    'ulp
    'unsigned-bit-shift-right
    'variance
    'zero?] {:lift-fn lift-op}})


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
