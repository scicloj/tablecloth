(ns tablecloth.api.operators-test
  (:refer-clojure :exclude [* + - / < <= > >= abs and bit-and bit-and-not bit-clear bit-flip
                              bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor
                              even? identity infinite? max min neg? not odd? odd? or pos? quot rem
                              unsigned-bit-shift-right zero?])
  (:require [tablecloth.api :refer [dataset]]
            [tech.v3.datatype :refer [elemwise-datatype]]
            [midje.sweet :refer [fact facts =>]])
  (:use [tablecloth.api.operators]))


(facts
 "about ops that return the op result"

 (facts
  "about ops that take a maximum of one column and return a scalar"
  (let [ds (dataset {:label [:a :b :a :b]
                     :value [1 2 3 4]})]
    (let [ops [kurtosis
               magnitude
               magnitude-squared
               mean
               mean-fast
               median
               quartile-1
               quartile-3
               reduce-*
               reduce-+
               reduce-max
               reduce-min
               skew
               sum
               sum-fast
               variance]]
      (doseq [op ops]
        (fact "ops can aggregate a regular dataset"
              (let [result (op ds [:value])]
                result => tablecloth.api/dataset?
                (contains? result "summary") => true))
        (fact "ops can aggregate a grouped ds"
              (let [result (-> ds
                         (tablecloth.api/group-by :label)
                         (op [:value]))]
                result => tablecloth.api/dataset?
                (contains? result "summary") => true))))))

 (facts
  "about ops that take a maximum of two columns and return a scalar"
  (let [ds (dataset {:label [:a :b :a :b]
                     :values1 [1 2 3 4]
                     :values2 [5 6 7 8]})]
    (let [ops [distance
               distance-squared
               dot-product]]
      (doseq [op ops]
        (fact "ops can aggregate a regular dataset"
              (let [result (op ds [:values1 :values2])]
                result => tablecloth.api/dataset?
                (contains? result "summary")))
        (fact "ops can aggregate a grouped dataset"
              (let [result (-> ds
                               (tablecloth.api/group-by :label)
                               (op [:values1 :values2]))]
                result => tablecloth.api/dataset?
                (contains? result "summary"))))))))

(facts
 "about ops that return a dataset with a new column"

 (facts
  "about ops that take a maximum of one column"
  (let [ds (dataset {:a [1 2 3]})]
    (let [result (shift ds :b [:a] 1)]
      (:b result) => [1 1 2])

    (let [ops [cummax
               cummin
               cumprod
               cumsum
               abs
               acos
               asin
               atan
               bit-not
               cbrt
               ceil
               cos
               cosh
               even?
               exp
               expm1
               finite?
               floor
               get-significand
               identity
               infinite?
               log
               log10
               log1p
               logistic
               mathematical-integer?
               nan?
               neg?
               next-down
               next-up
               normalize
               not
               odd?
               pos?
               rint
               round
               signum
               sin
               sinh
               sq
               sqrt
               tan
               tanh
               to-degrees
               to-radians
               ulp
               zero?]]
        (doseq [op ops]
          (let [result (op ds :b [:a])]
            (contains? result :b) => true)))))

 (facts
  "about ops that take a maximum of two columns"
  (let [ops [and eq not-eq or]
        ds (dataset {:a [1 2 3]
                     :b [4 5 6]})]
      (doseq [op ops]
        (let [result (op ds :c [:a :b :c])]
          (contains? result :c) => true))))

 (facts
  "about ops that take a maximum of three columns"
  (let [ops [< > <= >=]
        ds (dataset {:a [1 2 3]
                     :b [4 5 6]
                     :c [7 8 9]})]
    (doseq [op ops]
      (let [result (op ds :d [:a :b :c])]
        (contains? result :d) => true))))

 (facts
  "about ops that can take an unlimited number of columns"
  (let [ops [/
             -
             +
             *
             atan2
             ;; equals  // this function doesn't seem to handle more than two cols
             hypot
             bit-and
             bit-and-not
             bit-clear
             bit-flip
             bit-or
             bit-set
             bit-shift-right
             bit-shift-left
             bit-xor
             ieee-remainder
             max
             min
             pow
             quot
             rem
             unsigned-bit-shift-right]
        ds (dataset {:a [1 2 3]
                     :b [4 5 6]
                     :c [7 8 9]
                     :d [10 11 12]})]
    (doseq [op ops]
      (let [result (op ds :e [:a :b :c :d])]
        (contains? result :e) => true)))))
