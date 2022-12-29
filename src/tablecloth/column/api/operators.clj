(ns
 tablecloth.column.api.operators
 (:require [tech.v3.datatype.functional] [tablecloth.column.api.utils])
 (:refer-clojure
  :exclude
  [+ - / < <= > >= * neg? pos? odd? even? zero? not odd? or and]))

(defn
 kurtosis
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/kurtosis col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/kurtosis col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 finite?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/finite? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/finite? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 distance
 ""
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/distance lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 <
 ""
 ([lhs mid rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/< lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/< lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 sum
 "Double sum of data using\n  [Kahan compensated summation](https://en.wikipedia.org/wiki/Kahan_summation_algorithm)."
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/sum col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/sum col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 pos?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/pos? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/pos? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 neg?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/neg? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/neg? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 <=
 ""
 ([lhs mid rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/<= lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/<= lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 *
 ""
 ([x y]
  (let
   [original-result__26257__auto__ (tech.v3.datatype.functional/* x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([x y & args]
  (let
   [original-result__26257__auto__
    (clojure.core/apply tech.v3.datatype.functional/* x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 mathematical-integer?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/mathematical-integer? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/mathematical-integer? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 nan?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/nan? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/nan? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 dot-product
 ""
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/dot-product lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 eq
 ""
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/eq lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 mean
 "double mean of data"
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/mean col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/mean col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 >
 ""
 ([lhs mid rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/> lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/> lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 not-eq
 ""
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/not-eq lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 even?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/even? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/even? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 -
 ""
 ([x]
  (let
   [original-result__26257__auto__ (tech.v3.datatype.functional/- x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([x y]
  (let
   [original-result__26257__auto__ (tech.v3.datatype.functional/- x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([x y & args]
  (let
   [original-result__26257__auto__
    (clojure.core/apply tech.v3.datatype.functional/- x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 or
 ""
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/or lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 distance-squared
 ""
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/distance-squared lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 skew
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/skew col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/skew col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 zero?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/zero? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/zero? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 variance
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/variance col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/variance col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 not
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/not col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/not col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 standard-deviation
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/standard-deviation col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/standard-deviation col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 /
 ""
 ([x]
  (let
   [original-result__26257__auto__ (tech.v3.datatype.functional// x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([x y]
  (let
   [original-result__26257__auto__ (tech.v3.datatype.functional// x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([x y & args]
  (let
   [original-result__26257__auto__
    (clojure.core/apply tech.v3.datatype.functional// x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 equals
 ""
 ([lhs rhs & args]
  (let
   [original-result__26257__auto__
    (clojure.core/apply
     tech.v3.datatype.functional/equals
     lhs
     rhs
     args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 >=
 ""
 ([lhs mid rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/>= lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/>= lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 quartile-3
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/quartile-3 col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/quartile-3 col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 infinite?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/infinite? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/infinite? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 round
 "Vectorized implementation of Math/round.  Operates in double space\n  but returns a long or long reader."
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/round col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/round col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 quartile-1
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/quartile-1 col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/quartile-1 col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 odd?
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/odd? col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/odd? col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 +
 ""
 ([x]
  (let
   [original-result__26257__auto__ (tech.v3.datatype.functional/+ x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([x y]
  (let
   [original-result__26257__auto__ (tech.v3.datatype.functional/+ x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__)))
 ([x y & args]
  (let
   [original-result__26257__auto__
    (clojure.core/apply tech.v3.datatype.functional/+ x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

(defn
 median
 ""
 ([col]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/median col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/median col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 percentiles
 "Create a reader of percentile values, one for each percentage passed in.\n  Estimation types are in the set of #{:r1,r2...legacy} and are described\n  here: https://commons.apache.org/proper/commons-math/javadocs/api-3.3/index.html.\n\n  nan-strategy can be one of [:keep :remove :exception] and defaults to :exception."
 ([col percentiles]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/percentiles percentiles col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__)))
 ([col percentiles options]
  (let
   [original-result__26256__auto__
    (tech.v3.datatype.functional/percentiles percentiles options col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26256__auto__))))

(defn
 and
 ""
 ([lhs rhs]
  (let
   [original-result__26257__auto__
    (tech.v3.datatype.functional/and lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__26257__auto__))))

