(ns
 tablecloth.column.api.operators
 (:require [tech.v3.datatype.functional])
 (:refer-clojure :exclude [+ - / > >= < <=]))

(defn
 +
 ""
 ([x]
  (let
   [original-result__43655__auto__ (tech.v3.datatype.functional/+ x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([x y]
  (let
   [original-result__43655__auto__ (tech.v3.datatype.functional/+ x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([x y & args]
  (let
   [original-result__43655__auto__
    (clojure.core/apply tech.v3.datatype.functional/+ x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__))))

(defn
 -
 ""
 ([x]
  (let
   [original-result__43655__auto__ (tech.v3.datatype.functional/- x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([x y]
  (let
   [original-result__43655__auto__ (tech.v3.datatype.functional/- x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([x y & args]
  (let
   [original-result__43655__auto__
    (clojure.core/apply tech.v3.datatype.functional/- x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__))))

(defn
 /
 ""
 ([x]
  (let
   [original-result__43655__auto__ (tech.v3.datatype.functional// x)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([x y]
  (let
   [original-result__43655__auto__ (tech.v3.datatype.functional// x y)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([x y & args]
  (let
   [original-result__43655__auto__
    (clojure.core/apply tech.v3.datatype.functional// x y args)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__))))

(defn
 >
 ""
 ([lhs mid rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/> lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([lhs rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/> lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__))))

(defn
 >=
 ""
 ([lhs mid rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/>= lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([lhs rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/>= lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__))))

(defn
 <
 ""
 ([lhs mid rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/< lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([lhs rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/< lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__))))

(defn
 <=
 ""
 ([lhs mid rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/<= lhs mid rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__)))
 ([lhs rhs]
  (let
   [original-result__43655__auto__
    (tech.v3.datatype.functional/<= lhs rhs)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43655__auto__))))

(defn
 percentiles
 "Create a reader of percentile values, one for each percentage passed in.\n  Estimation types are in the set of #{:r1,r2...legacy} and are described\n  here: https://commons.apache.org/proper/commons-math/javadocs/api-3.3/index.html.\n\n  nan-strategy can be one of [:keep :remove :exception] and defaults to :exception."
 ([col percentiles]
  (let
   [original-result__43654__auto__
    (tech.v3.datatype.functional/percentiles percentiles col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43654__auto__)))
 ([col percentiles options]
  (let
   [original-result__43654__auto__
    (tech.v3.datatype.functional/percentiles percentiles options col)]
   (tablecloth.column.api.utils/return-scalar-or-column
    original-result__43654__auto__))))

