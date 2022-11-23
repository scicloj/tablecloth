(ns tablecloth.column.api.lifted-operators-test
  (:require [midje.sweet :refer [fact facts =>]]
            [clojure.test :refer [deftest is]]
            [tablecloth.column.api :refer [column column?]]
            [tablecloth.column.api.lifted-operators :refer [+ - / > >= < <= percentiles] :as ops])
  (:refer-clojure :exclude [+ - / > >= < <=]))

(def a (column [1.0 2.0 3.0 4.0]))
(def b (column [5 6 7 8]))
(def scalar-a 10)
(def sclar)

(defn scalar? [item]
  (= (tech.v3.datatype.argtypes/arg-type item)
     :scalar))

(facts
 "about +"
 (let [a [1 2 3 4]
       b [5 6 7 8]]
   (+ a b) => column?
   (+ 3 3) => scalar?
   (+ a b) => [6 8 10 12]))

(facts
 "about -"
 (let [a [1 2 3 4]
       b [5 6 7 8]]
   (- a b) => column?
   (- 3 3) => scalar?
   (- a b) => [-4 -4 -4 -4]))

(facts
 "about /"
 (let [a [1.0 2.0 3.0 4.0]
       b [5 6 7 8]]
   (/ a b) => column?
   (- 3 3) => scalar?
   (/ a b) => [0.2 0.3333333333333333 0.42857142857142855 0.5]))

(facts
 "about >"
 (let [a [1 2 3 4]
       b [5 0 7 8]]
   (> a b) => column?
   (> 3 4) => boolean?
   (> a b) => [false true false false]))


(facts
 "about `percentiles"
 (let [a (range 100)]
   (ops/percentiles a [25 50 75]) => column?
   (ops/percentiles a [25 50 75]) => [24.25 49.50 74.75]))


