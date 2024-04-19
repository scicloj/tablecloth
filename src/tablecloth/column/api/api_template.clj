(ns tablecloth.column.api.api-template
  "Tablecloth Column API"
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle * + / - < <= > >=
                            abs bit-and bit-and-not bit-clear bit-flip bit-not bit-or bit-set
                            bit-shift-left bit-shift-right bit-xor and even? identity infinite?
                            max min neg? not odd? or pos? quot rem unsigned-bit-shift-right
                            zero?])  
  (:require [tech.v3.datatype.export-symbols :as exporter]))

(exporter/export-symbols tablecloth.column.api.column
                         column
                         column?
                         column-map
                         typeof
                         typeof?
                         slice
                         zeros
                         ones
                         sort-column)

(exporter/export-symbols tablecloth.column.api.missing
                         count-missing
                         drop-missing
                         replace-missing)

(exporter/export-symbols tech.v3.dataset.column
                         is-missing?
                         missing
                         select)

(exporter/export-symbols tablecloth.column.api.operators
  * + - / < <= > >= abs acos and asin atan atan2 bit-and bit-and-not bit-clear bit-flip bit-not bit-or bit-set bit-shift-left bit-shift-right bit-xor cbrt ceil cos cosh cummax cummin cumprod cumsum descriptive-statistics distance distance-squared dot-product eq equals even? exp expm1 fill-range finite? floor get-significand hypot identity ieee-remainder infinite? kendalls-correlation kurtosis log log10 log1p logistic magnitude magnitude-squared mathematical-integer? max mean mean-fast median min nan? neg? next-down next-up normalize not not-eq odd? or pearsons-correlation percentiles pos? pow quartile-1 quartile-3 quartiles quot reduce-* reduce-+ reduce-max reduce-min rem rint round shift signum sin sinh skew spearmans-correlation sq sqrt standard-deviation sum sum-fast tan tanh to-degrees to-radians ulp unsigned-bit-shift-right variance zero?)

(comment
  ;; Use this to generate the column api
  (exporter/write-api! 'tablecloth.column.api.api-template
                       'tablecloth.column.api
                       "src/tablecloth/column/api.clj"
                       '[* + - / < <= >= >
                         abs and bit-and bit-and-not bit-clear bit-flip bit-not bit-or bit-set
                         bit-shift-left bit-shift-right bit-xor even? identity infinite? max min
                         neg? not odd? or pos? quot rem unsigned-bit-shift-right zero?])
  ,)
