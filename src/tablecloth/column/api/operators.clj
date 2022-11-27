(ns
 tablecloth.column.api.operators
 (:require [tech.v3.datatype.functional])
 (:refer-clojure :exclude [+ - / > >= < <=]))

(defn
 kurtosis
 ""
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/kurtosis col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/kurtosis col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 <
 ""
 ([lhs mid rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/< lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([lhs rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/< lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__))))

(defn
 sum
 "Double sum of data using\n  [Kahan compensated summation](https://en.wikipedia.org/wiki/Kahan_summation_algorithm)."
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/sum col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/sum col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 <=
 ""
 ([lhs mid rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/<= lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([lhs rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/<= lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__))))

(defn
 mean
 "double mean of data"
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/mean col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/mean col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 >
 ""
 ([lhs mid rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/> lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([lhs rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/> lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__))))

(defn
 -
 ""
 ([x]
  (let
   [original-result__42777__auto__ (tech.v3.datatype.functional/- x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([x y]
  (let
   [original-result__42777__auto__ (tech.v3.datatype.functional/- x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([x y & args]
  (let
   [original-result__42777__auto__
    (clojure.core/apply tech.v3.datatype.functional/- x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__))))

(defn
 skew
 ""
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/skew col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/skew col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 variance
 ""
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/variance col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/variance col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 standard-deviation
 ""
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/standard-deviation col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/standard-deviation col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 /
 ""
 ([x]
  (let
   [original-result__42777__auto__ (tech.v3.datatype.functional// x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([x y]
  (let
   [original-result__42777__auto__ (tech.v3.datatype.functional// x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([x y & args]
  (let
   [original-result__42777__auto__
    (clojure.core/apply tech.v3.datatype.functional// x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__))))

(defn
 >=
 ""
 ([lhs mid rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/>= lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([lhs rhs]
  (let
   [original-result__42777__auto__
    (tech.v3.datatype.functional/>= lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__))))

(defn
 quartile-3
 ""
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/quartile-3 col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/quartile-3 col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 quartile-1
 ""
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/quartile-1 col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/quartile-1 col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 +
 ""
 ([x]
  (let
   [original-result__42777__auto__ (tech.v3.datatype.functional/+ x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([x y]
  (let
   [original-result__42777__auto__ (tech.v3.datatype.functional/+ x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__)))
 ([x y & args]
  (let
   [original-result__42777__auto__
    (clojure.core/apply tech.v3.datatype.functional/+ x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42777__auto__))))

(defn
 median
 ""
 ([col]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/median col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/median col options)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

(defn
 percentiles
 "Create a reader of percentile values, one for each percentage passed in.\n  Estimation types are in the set of #{:r1,r2...legacy} and are described\n  here: https://commons.apache.org/proper/commons-math/javadocs/api-3.3/index.html.\n\n  nan-strategy can be one of [:keep :remove :exception] and defaults to :exception."
 ([col percentiles]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/percentiles percentiles col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__)))
 ([col percentiles options]
  (let
   [original-result__42776__auto__
    (tech.v3.datatype.functional/percentiles percentiles options col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__42776__auto__))))

