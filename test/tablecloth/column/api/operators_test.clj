(ns tablecloth.column.api.operators-test
  (:refer-clojure :exclude [+ - / < <= > >= neg? pos? odd? even? zero? not odd?])
  (:require [midje.sweet :refer [fact facts =>]]
            [clojure.test :refer [deftest is]]
            [tablecloth.column.api :refer [column column? typeof]])
  (:use [tablecloth.column.api.operators]))


(defn sample-column [n]
  (column (repeatedly n #(rand-int 100))))

(defn scalar? [item]
  (= (tech.v3.datatype.argtypes/arg-type item)
     :scalar))

(facts
 "about [/ - +] ops"
 (let [ops [/ - +]
       a (sample-column 5)
       b (sample-column 5)
       c (sample-column 5)
       d (sample-column 5)]
   (doseq [op ops]
     (op a b) => column?
     (op a b c) => column?
     (op a b c d) => column?
     (op 1) => scalar?
     (op 1 2) => scalar?
     (op 1 2 3) => scalar?)))

(facts
 "about [> >= < <=]"
 (let [ops [> >= < <=]
       a (sample-column 5)
       b (sample-column 5)
       c (sample-column 5)]
   (doseq [op ops]
     (op a b) => column?
     (op a b c) => column?
     (op 1 2) => boolean?)))

(facts
 "about ops that take a single column or scalar and return a scalar"
 (let [ops [kurtosis
            sum
            mean
            skew
            variance
            standard-deviation
            quartile-3
            quartile-1
            median]
       a (sample-column 5)]
   (doseq [op ops]
     (op a) => scalar?)))

(facts
 "about ops that take a single column and return scalar or column of booleans"
 (let [ops [finite?
            pos?
            neg?
            mathematical-integer?
            nan?
            even?
            zero?
            not
            infinite?
            odd?]
       a (sample-column 5)]
   (doseq [op ops]
     (op a) => column? 
     (typeof (op a)) => :boolean
     (op 1) => boolean?)))




