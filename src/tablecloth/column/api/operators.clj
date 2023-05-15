(ns
 tablecloth.column.api.operators
 (:require
  [tech.v3.datatype.functional]
  [tablecloth.column.api.lift_operators])
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
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/kurtosis x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/kurtosis x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 bit-set
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-set x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/bit-set x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 finite?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/finite? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/finite? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 distance
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/distance x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 reduce-min
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/reduce-min x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 to-radians
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/to-radians x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/to-radians x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 bit-shift-right
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-shift-right x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply
     tech.v3.datatype.functional/bit-shift-right
     x
     y
     args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 ieee-remainder
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/ieee-remainder x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply
     tech.v3.datatype.functional/ieee-remainder
     x
     y
     args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 log
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/log x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/log x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 bit-shift-left
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-shift-left x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply
     tech.v3.datatype.functional/bit-shift-left
     x
     y
     args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 acos
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/acos x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/acos x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 to-degrees
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/to-degrees x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/to-degrees x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 <
 ""
 ([x y]
  (let
   [original-result__29872__auto__ (tech.v3.datatype.functional/< x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x y z]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/< x y z)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 floor
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/floor x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/floor x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 atan2
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/atan2 x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/atan2 x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 normalize
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/normalize x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 hypot
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/hypot x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/hypot x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 tanh
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/tanh x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/tanh x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 sq
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/sq x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/sq x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 fill-range
 "Given a reader of numeric data and a max span amount, produce\n  a new reader where the difference between any two consecutive elements\n  is less than or equal to the max span amount.  Also return a bitmap of the added\n  indexes.  Uses linear interpolation to fill in areas, operates in double space.\n  Returns\n  {:result :missing}"
 ([x max-span]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/fill-range x max-span)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 sum
 "Double sum of data using\n  [Kahan compensated summation](https://en.wikipedia.org/wiki/Kahan_summation_algorithm)."
 ([x]
  (let
   [original-result__29872__auto__ (tech.v3.datatype.functional/sum x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/sum x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 pos?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/pos? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/pos? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 shift
 "Shift by n and fill in with the first element for n>0 or last element for n<0.\n\n  Examples:\n\n```clojure\nuser> (dfn/shift (range 10) 2)\n[0 0 0 1 2 3 4 5 6 7]\nuser> (dfn/shift (range 10) -2)\n[2 3 4 5 6 7 8 9 9 9]\n```"
 ([x n]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/shift x n)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 ceil
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/ceil x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/ceil x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 bit-xor
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-xor x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/bit-xor x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 unsigned-bit-shift-right
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/unsigned-bit-shift-right x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply
     tech.v3.datatype.functional/unsigned-bit-shift-right
     x
     y
     args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 neg?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/neg? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/neg? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 <=
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/<= x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x y z]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/<= x y z)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 *
 ""
 ([x y]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/* x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/* x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 min
 ""
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/min x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/min x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/min x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 atan
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/atan x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/atan x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 mathematical-integer?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/mathematical-integer? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/mathematical-integer? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 cumprod
 "Cumulative running product; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/cumprod x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 expm1
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/expm1 x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/expm1 x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 identity
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/identity x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/identity x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 reduce-max
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/reduce-max x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 cumsum
 "Cumulative running summation; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/cumsum x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 descriptive-statistics
 "Calculate a set of descriptive statistics on a single reader.\n\n  Available stats:\n  #{:min :quartile-1 :sum :mean :mode :median :quartile-3 :max\n    :variance :standard-deviation :skew :n-elems :kurtosis}\n\n  options\n    - `:nan-strategy` - defaults to :remove, one of\n    [:keep :remove :exception]. The fastest option is :keep but this\n    may result in your results having NaN's in them.  You can also pass\n  in a double predicate to filter custom double values."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/descriptive-statistics x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x stats-names]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/descriptive-statistics stats-names x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x stats-names options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/descriptive-statistics
     stats-names
     options
     x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x stats-names stats-data options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/descriptive-statistics
     stats-names
     stats-data
     options
     x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 nan?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/nan? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/nan? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 bit-and-not
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-and-not x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply
     tech.v3.datatype.functional/bit-and-not
     x
     y
     args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 logistic
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/logistic x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/logistic x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 cos
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/cos x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/cos x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 log10
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/log10 x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/log10 x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 quot
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/quot x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/quot x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 dot-product
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/dot-product x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 tan
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/tan x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/tan x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 cbrt
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/cbrt x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/cbrt x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 eq
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/eq x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 mean
 "double mean of data"
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/mean x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/mean x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 >
 ""
 ([x y]
  (let
   [original-result__29872__auto__ (tech.v3.datatype.functional/> x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x y z]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/> x y z)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 not-eq
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/not-eq x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 even?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/even? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/even? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 spearmans-correlation
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/spearmans-correlation x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x y options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/spearmans-correlation options x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 sqrt
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/sqrt x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/sqrt x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 reduce-*
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/reduce-* x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 next-down
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/next-down x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/next-down x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 -
 ""
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/- x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/- x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/- x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 or
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/or x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 distance-squared
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/distance-squared x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 pow
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/pow x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/pow x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 next-up
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/next-up x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/next-up x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 skew
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/skew x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/skew x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 exp
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/exp x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/exp x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 mean-fast
 "Take the mean of the data.  This operation doesn't know anything about nan hence it is\n  a bit faster than the base [[mean]] fn."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/mean-fast x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 zero?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/zero? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/zero? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 rem
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/rem x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/rem x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 cosh
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/cosh x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/cosh x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 variance
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/variance x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/variance x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 reduce-+
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/reduce-+ x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 get-significand
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/get-significand x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/get-significand x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 bit-and
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-and x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/bit-and x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 kendalls-correlation
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/kendalls-correlation x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x y options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/kendalls-correlation options x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 not
 ""
 ([x]
  (let
   [original-result__29872__auto__ (tech.v3.datatype.functional/not x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/not x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 standard-deviation
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/standard-deviation x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/standard-deviation x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 cummin
 "Cumulative running min; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/cummin x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 magnitude
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/magnitude x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/magnitude x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 cummax
 "Cumulative running max; returns result in double space.\n\n  Options:\n\n  * `:nan-strategy` - one of `:keep`, `:remove`, `:exception`.  Defaults to `:remove`."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/cummax x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 /
 ""
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional// x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional// x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional// x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 bit-or
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-or x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/bit-or x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 equals
 ""
 ([x y & args]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/equals x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 >=
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/>= x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x y z]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/>= x y z)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 bit-flip
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-flip x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/bit-flip x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 log1p
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/log1p x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/log1p x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 asin
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/asin x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/asin x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 quartiles
 "return [min, 25 50 75 max] of item"
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/quartiles x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/quartiles options x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 quartile-3
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/quartile-3 x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/quartile-3 x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 infinite?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/infinite? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/infinite? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 round
 "Vectorized implementation of Math/round.  Operates in double space\n  but returns a long or long reader."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/round x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/round x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 quartile-1
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/quartile-1 x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/quartile-1 x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 odd?
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/odd? x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/odd? x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 bit-clear
 ""
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-clear x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply
     tech.v3.datatype.functional/bit-clear
     x
     y
     args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 +
 ""
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/+ x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/+ x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/+ x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 abs
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/abs x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/abs x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 median
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/median x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/median x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 pearsons-correlation
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/pearsons-correlation x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x y options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/pearsons-correlation options x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 sinh
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/sinh x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/sinh x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 rint
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/rint x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/rint x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 bit-not
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-not x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/bit-not x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 max
 ""
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/max x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/max x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x y & args]
  (let
   [original-result__29873__auto__
    (clojure.core/apply tech.v3.datatype.functional/max x y args)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 ulp
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/ulp x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/ulp x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 percentiles
 "Create a reader of percentile values, one for each percentage passed in.\n  Estimation types are in the set of #{:r1,r2...legacy} and are described\n  here: https://commons.apache.org/proper/commons-math/javadocs/api-3.3/index.html.\n\n  nan-strategy can be one of [:keep :remove :exception] and defaults to :exception."
 ([x percentiles]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/percentiles percentiles x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__)))
 ([x percentiles options]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/percentiles percentiles options x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 sin
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/sin x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__ (tech.v3.datatype.functional/sin x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 sum-fast
 "Find the sum of the data.  This operation is neither nan-aware nor does it implement\n  kahans compensation although via parallelization it implements pairwise summation\n  compensation.  For a more but slightly slower but far more correct sum operator,\n  use [[sum]]."
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/sum-fast x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 signum
 ""
 ([x options]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/signum x options)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__)))
 ([x]
  (let
   [original-result__29873__auto__
    (tech.v3.datatype.functional/signum x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29873__auto__))))

(defn
 magnitude-squared
 ""
 ([x]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/magnitude-squared x)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

(defn
 and
 ""
 ([x y]
  (let
   [original-result__29872__auto__
    (tech.v3.datatype.functional/and x y)]
   (tablecloth.column.api.lift-operators/return-scalar-or-column
    original-result__29872__auto__))))

