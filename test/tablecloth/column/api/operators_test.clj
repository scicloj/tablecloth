(ns tablecloth.column.api.operators-test
  (:refer-clojure :exclude [+ - / < <= > >= * neg? pos? odd? even? or and zero? not odd?])
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
 "about 'shift"
 (let [a (column [1 2 3 4 5])]
   (shift a 2) => [1 1 1 2 3]))

(facts
 "about ops that take a single column and return a column"
 (let [ops [normalize]
       a (sample-column 5)]
   (doseq [op ops]
     (op a) => column?)))

(facts
 "about ops that take a single column and return a scalar"
 (let [ops [magnitude]
       a (sample-column 5)]
   (doseq [op ops]
     (op a) => scalar?)))

(facts
 "about ops that take one or more columns or scalars
  and return either a scalar or a column"
 (let [ops [/ - +]
       a (sample-column 5)
       b (sample-column 5)
       c (sample-column 5)
       d (sample-column 5)]
   (doseq [op ops]
     (op a) => column?
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
 "about ops that take two or more scalars or columns and return a column or scalar"
 (let [ops [*]
       a (sample-column 5)
       b (sample-column 5)
       c (sample-column 5)]
   (doseq [op ops]
     (op a b) => column?
     (op a b c) => column?
     (op 5 5)
     (op 5 5 5))))

(facts
 "about ops that take left-hand / right-hand columns and return a scalar"
 (let [ops [distance
            dot-product
            distance-squared]
       a (sample-column 5)
       b (sample-column 5)]
   (doseq [op ops]
     (op a b) => scalar?)))

(facts
 "about ops that take a single column or scalar 
  and return boolean or column of booleans"
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

(facts
 "about ops that take left-hand and right-hand columns and return a boolean"
 (let [ops [equals]
       a (sample-column 5)
       b (sample-column 5)]
   (doseq [op ops]
     (op a b) => boolean?)))

(facts
 "about ops that take left-hand / right-hand columns or scalars 
  and returns boolean or column of booleans"
 (let [ops [or and eq not-eq]
       a (sample-column 5)
       b (sample-column 5)]
   (doseq [op ops]
     (op a b) => column?
     (typeof (op a b)) => :boolean
     (op 1 2) => boolean?)))

