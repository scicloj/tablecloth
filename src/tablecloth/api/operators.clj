(ns tablecloth.api.operators
  (:refer-clojure :exclude [max min + - * / bit-xor unsigned-bit-shift-right quot bit-test
                            bit-and rem bit-or bit-flip bit-shift-left bit-clear bit-shift-right bit-set
                            bit-and-not bit-not identity < <= > >= = not= and even? neg? not odd? or pos? zero?])
  (:require
   [tech.v3.datatype.unary-op :as unary-op]
   [tech.v3.datatype.binary-op :as binary-op]
   [tech.v3.datatype.unary-pred :as unary-pred]
   [tech.v3.datatype.binary-pred :as binary-pred]
   [tech.v3.datatype.functional :as dfn]
   [tech.v3.datatype.export-symbols :as exporter]
   [tablecloth.api.utils :as u]))

(u/defalias = tech.v3.datatype.functional/eq)
(u/defalias not= tech.v3.datatype.functional/not-eq)
(u/defalias emax tech.v3.datatype.functional/reduce-max)
(u/defalias emin tech.v3.datatype.functional/reduce-min)

(exporter/export-symbols
 tech.v3.datatype.functional
 sum
 cumsum
 cummin
 cummax
 cumprod
 descriptive-statistics
 variance
 standard-deviation
 median
 skew
 mean
 kurtosis
 quartile-1
 quartile-3
 pearsons-correlation
 spearmans-correlation
 kendalls-correlation
 percentiles
 quartiles
 quartile-outlier-fn
 ;; (->> (concat (keys binary-op/builtin-ops) (keys unary-op/builtin-ops)
 ;;              (keys binary-pred/builtin-ops) (keys unary-pred/builtin-ops))
 ;;      (map (comp symbol name))
 ;;      (distinct)
 ;;      (sort))
 * + - / < <= > >= abs acos and asin atan atan2 bit-and bit-and-not bit-clear bit-flip bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor cbrt ceil cos cosh eq even? exp expm1 finite? floor get-significand hypot identity ieee-remainder infinite? log log10 log1p logistic mathematical-integer? max min nan? neg? next-down next-up not not-eq odd? or pos? pow quot rem rint round signum sin sinh sq sqrt tan tanh to-degrees to-radians ulp unsigned-bit-shift-right zero?)
