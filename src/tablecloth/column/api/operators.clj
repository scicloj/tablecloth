(ns
 tablecloth.column.api.operators
 (:require
  [tech.v3.datatype.functional]
  [tablecloth.column.api.column])
 (:refer-clojure
  :exclude
  [*
   +
   -
   /
   <
   <=
   >
   >=
   abs
   and
   bit-and
   bit-and-not
   bit-clear
   bit-flip
   bit-not
   bit-or
   bit-set
   bit-shift-left
   bit-shift-right
   bit-test
   bit-xor
   even?
   identity
   infinite?
   max
   min
   neg?
   not
   odd?
   odd?
   or
   pos?
   quot
   rem
   unsigned-bit-shift-right
   zero?]))

(defn
 kurtosis
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/kurtosis x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/kurtosis x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-set
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-set x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/bit-set x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 finite?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/finite? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/finite? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 distance
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/distance x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 reduce-min
 "Invokes a reduce operation with the operand min find the minimum item of a list.\n\n  Examples:\n\n  ```clojure\n  user> (dfn/reduce-min [1 2 3 4])\n  1\n  user> (dfn/reduce-min [10 -10 20 100 -1])\n  -10\n  ```"
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/reduce-min x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 to-radians
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/to-radians x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/to-radians x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-shift-right
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-shift-right x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply
     tech.v3.datatype.functional/bit-shift-right
     x
     y
     args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 ieee-remainder
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/ieee-remainder x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply
     tech.v3.datatype.functional/ieee-remainder
     x
     y
     args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 log
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/log x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/log x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-shift-left
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-shift-left x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply
     tech.v3.datatype.functional/bit-shift-left
     x
     y
     args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 acos
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/acos x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/acos x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 to-degrees
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/to-degrees x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/to-degrees x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 <
 ""
 ([x y z]
  (let
   [original-result (tech.v3.datatype.functional/< x y z)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/< x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 floor
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/floor x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/floor x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 atan2
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/atan2 x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/atan2 x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 normalize
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/normalize x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 hypot
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/hypot x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/hypot x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 tanh
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/tanh x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/tanh x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 sq
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/sq x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/sq x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 fill-range
 "Given a reader of numeric data and a max span amount, produce\n  a new reader where the difference between any two consecutive elements\n  is less than or equal to the max span amount.  Also return a bitmap of the added\n  indexes.  Uses linear interpolation to fill in areas, operates in double space.\n  Returns\n  {:result :missing}"
 ([x max-span]
  (let
   [original-result
    (tech.v3.datatype.functional/fill-range x max-span)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 sum
 "Double sum of data using\n  [Kahan compensated summation](https://en.wikipedia.org/wiki/Kahan_summation_algorithm)."
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/sum x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/sum x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 pos?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/pos? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/pos? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 shift
 "Shift by n and fill in with the first element for n>0 or last element for n<0.\n\n  Examples:\n\n```clojure\nuser> (dfn/shift (range 10) 2)\n[0 0 0 1 2 3 4 5 6 7]\nuser> (dfn/shift (range 10) -2)\n[2 3 4 5 6 7 8 9 9 9]\n```"
 ([x n]
  (let
   [original-result (tech.v3.datatype.functional/shift x n)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 ceil
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/ceil x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/ceil x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-xor
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-xor x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/bit-xor x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 unsigned-bit-shift-right
 ""
 ([x y]
  (let
   [original-result
    (tech.v3.datatype.functional/unsigned-bit-shift-right x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply
     tech.v3.datatype.functional/unsigned-bit-shift-right
     x
     y
     args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 neg?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/neg? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/neg? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 <=
 ""
 ([x y z]
  (let
   [original-result (tech.v3.datatype.functional/<= x y z)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/<= x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 *
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/* x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/* x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 min
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/min x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/min x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/min x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 atan
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/atan x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/atan x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 mathematical-integer?
 ""
 ([x options]
  (let
   [original-result
    (tech.v3.datatype.functional/mathematical-integer? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result
    (tech.v3.datatype.functional/mathematical-integer? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 cumprod
 "Cumulative running product; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/cumprod x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/cumprod x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 expm1
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/expm1 x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/expm1 x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 identity
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/identity x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/identity x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 reduce-max
 "Invokes a reduce operation with the operand max find the maximum item of a list.\n\n  Examples:\n\n  ```clojure\n  user> (dfn/reduce-max [1 2 3 4])\n  4\n  user> (dfn/reduce-max [10 -10 20 100 -1])\n  100\n  ```"
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/reduce-max x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 cumsum
 "Cumulative running summation; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/cumsum x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/cumsum x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 descriptive-statistics
 "Calculate a set of descriptive statistics on a single reader.\n\n  Available stats:\n  #{:min :quartile-1 :sum :mean :mode :median :quartile-3 :max\n    :variance :standard-deviation :skew :n-elems :kurtosis}\n\n  options\n    - `:nan-strategy` - defaults to :remove, one of\n    [:keep :remove :exception]. The fastest option is :keep but this\n    may result in your results having NaN's in them.  You can also pass\n  in a double predicate to filter custom double values."
 ([x stats-names stats-data options]
  (let
   [original-result
    (tech.v3.datatype.functional/descriptive-statistics
     x
     stats-names
     stats-data
     options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x stats-names options]
  (let
   [original-result
    (tech.v3.datatype.functional/descriptive-statistics
     x
     stats-names
     options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x stats-names]
  (let
   [original-result
    (tech.v3.datatype.functional/descriptive-statistics x stats-names)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result
    (tech.v3.datatype.functional/descriptive-statistics x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 nan?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/nan? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/nan? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-and-not
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-and-not x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply
     tech.v3.datatype.functional/bit-and-not
     x
     y
     args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 logistic
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/logistic x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/logistic x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 cos
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/cos x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/cos x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 log10
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/log10 x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/log10 x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 quot
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/quot x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/quot x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 dot-product
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/dot-product x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 tan
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/tan x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/tan x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 cbrt
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/cbrt x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/cbrt x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 eq
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/eq x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 mean
 "double mean of x"
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/mean x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/mean x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 >
 ""
 ([x y z]
  (let
   [original-result (tech.v3.datatype.functional/> x y z)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/> x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 not-eq
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/not-eq x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 even?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/even? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/even? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 spearmans-correlation
 ""
 ([x y options]
  (let
   [original-result
    (tech.v3.datatype.functional/spearmans-correlation x y options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result
    (tech.v3.datatype.functional/spearmans-correlation x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 sqrt
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/sqrt x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/sqrt x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 reduce-*
 "Invokes a reduce operation with the operand * to multiply items together.\n\n  Examples:\n\n  ```clojure\n  user> (dfn/reduce-* [1 2 3 4])\n  24\n  user> (dfn/reduce-* [-1 1.1 4])\n  -4.4\n  ```"
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/reduce-* x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 next-down
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/next-down x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/next-down x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 -
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/- x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/- x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/- x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 or
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/or x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 distance-squared
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/distance-squared x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 pow
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/pow x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/pow x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 next-up
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/next-up x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/next-up x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 skew
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/skew x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/skew x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 exp
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/exp x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/exp x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 mean-fast
 "Take the mean of the x.  This operation doesn't know anything about nan hence it is\n  a bit faster than the base [[mean]] fn."
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/mean-fast x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 zero?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/zero? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/zero? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 rem
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/rem x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/rem x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 cosh
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/cosh x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/cosh x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 variance
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/variance x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/variance x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 reduce-+
 "Invokes a reduce operation with the operand + to add items together.\n\n  Examples:\n\n  ```clojure\n  user> (dfn/reduce-+ [1 2 3 4])\n  10\n  user> (dfn/reduce-+ [-1 1.1 4])\n  4.1\n  ```"
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/reduce-+ x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 get-significand
 ""
 ([x options]
  (let
   [original-result
    (tech.v3.datatype.functional/get-significand x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/get-significand x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-and
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-and x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/bit-and x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 kendalls-correlation
 ""
 ([x y options]
  (let
   [original-result
    (tech.v3.datatype.functional/kendalls-correlation x y options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result
    (tech.v3.datatype.functional/kendalls-correlation x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 not
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/not x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/not x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 standard-deviation
 ""
 ([x options]
  (let
   [original-result
    (tech.v3.datatype.functional/standard-deviation x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/standard-deviation x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 cummin
 "Cumulative running min; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/cummin x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/cummin x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 magnitude
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/magnitude x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 cummax
 "Cumulative running max; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/cummax x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/cummax x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 /
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional// x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional// x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional// x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-or
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-or x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/bit-or x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 equals
 ""
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/equals x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 >=
 ""
 ([x y z]
  (let
   [original-result (tech.v3.datatype.functional/>= x y z)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/>= x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-flip
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-flip x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/bit-flip x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 log1p
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/log1p x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/log1p x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 asin
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/asin x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/asin x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 quartiles
 "return [min, 25 50 75 max] of item"
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/quartiles x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/quartiles x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 quartile-3
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/quartile-3 x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/quartile-3 x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 infinite?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/infinite? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/infinite? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 round
 "Vectorized implementation of Math/round.  Operates in double space\n  but returns a long or long reader."
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/round x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/round x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 quartile-1
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/quartile-1 x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/quartile-1 x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 odd?
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/odd? x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/odd? x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-clear
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/bit-clear x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply
     tech.v3.datatype.functional/bit-clear
     x
     y
     args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 +
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/+ x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/+ x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/+ x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 abs
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/abs x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/abs x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 median
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/median x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/median x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 pearsons-correlation
 ""
 ([x y options]
  (let
   [original-result
    (tech.v3.datatype.functional/pearsons-correlation x y options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result
    (tech.v3.datatype.functional/pearsons-correlation x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 sinh
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/sinh x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/sinh x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 rint
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/rint x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/rint x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 bit-not
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/bit-not x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/bit-not x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 max
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/max x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/max x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x y & args]
  (let
   [original-result
    (clojure.core/apply tech.v3.datatype.functional/max x y args)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 ulp
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/ulp x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/ulp x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 percentiles
 "Create a reader of percentile values, one for each percentage passed in.\n  Estimation types are in the set of #{:r1,r2...legacy} and are described\n  here: https://commons.apache.org/proper/commons-math/javadocs/api-3.3/index.html.\n\n  nan-strategy can be one of [:keep :remove :exception] and defaults to :exception."
 ([x percentages options]
  (let
   [original-result
    (tech.v3.datatype.functional/percentiles x percentages options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x percentages]
  (let
   [original-result
    (tech.v3.datatype.functional/percentiles x percentages)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 sin
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/sin x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/sin x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 sum-fast
 "Find the sum of the data.  This operation is neither nan-aware nor does it implement\n  kahans compensation although via parallelization it implements pairwise summation\n  compensation.  For a more but slightly slower but far more correct sum operator,\n  use [[sum]]."
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/sum-fast x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 signum
 ""
 ([x options]
  (let
   [original-result (tech.v3.datatype.functional/signum x options)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result)))
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/signum x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 magnitude-squared
 ""
 ([x]
  (let
   [original-result (tech.v3.datatype.functional/magnitude-squared x)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

(defn
 and
 ""
 ([x y]
  (let
   [original-result (tech.v3.datatype.functional/and x y)]
   (tablecloth.column.api.column/return-scalar-or-column
    original-result))))

