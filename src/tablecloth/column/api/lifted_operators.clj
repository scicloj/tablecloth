(ns
 tablecloth.column.api.lifted-operators
 (:require [tech.v3.datatype.functional])
 (:refer-clojure :exclude [+ - / > >= < <=]))

(defn
 +
 ([x]
  (let
   [original-result__46675__auto__ (tech.v3.datatype.functional/+ x)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([x y]
  (let
   [original-result__46675__auto__ (tech.v3.datatype.functional/+ x y)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([x y & args]
  (let
   [original-result__46675__auto__
    (clojure.core/apply tech.v3.datatype.functional/+ x y args)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__))))

(defn
 -
 ([x]
  (let
   [original-result__46675__auto__ (tech.v3.datatype.functional/- x)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([x y]
  (let
   [original-result__46675__auto__ (tech.v3.datatype.functional/- x y)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([x y & args]
  (let
   [original-result__46675__auto__
    (clojure.core/apply tech.v3.datatype.functional/- x y args)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__))))

(defn
 /
 ([x]
  (let
   [original-result__46675__auto__ (tech.v3.datatype.functional// x)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([x y]
  (let
   [original-result__46675__auto__ (tech.v3.datatype.functional// x y)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([x y & args]
  (let
   [original-result__46675__auto__
    (clojure.core/apply tech.v3.datatype.functional// x y args)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__))))

(defn
 >
 ([lhs mid rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/> lhs mid rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([lhs rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/> lhs rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__))))

(defn
 >=
 ([lhs mid rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/>= lhs mid rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([lhs rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/>= lhs rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__))))

(defn
 <
 ([lhs mid rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/< lhs mid rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([lhs rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/< lhs rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__))))

(defn
 <=
 ([lhs mid rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/<= lhs mid rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__)))
 ([lhs rhs]
  (let
   [original-result__46675__auto__
    (tech.v3.datatype.functional/<= lhs rhs)]
   (if
    (clojure.core/->
     original-result__46675__auto__
     tech.v3.datatype.argtypes/arg-type
     (clojure.core/= :reader))
    (tablecloth.column.api/column original-result__46675__auto__)
    original-result__46675__auto__))))

(defn
 percentiles
 ([col percentiles]
  (let
   [original-result__47036__auto__
    (tech.v3.datatype.functional/percentiles percentiles col)]
   (tablecloth.column.api.operators/return-scalar-or-column
    original-result__47036__auto__)))
 ([col percentiles options]
  (let
   [original-result__47036__auto__
    (tech.v3.datatype.functional/percentiles percentiles options col)]
   (tablecloth.column.api.operators/return-scalar-or-column
    original-result__47036__auto__))))

