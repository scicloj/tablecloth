(ns tablecloth.api.lag-lead-test
  "Dataset-level `lag` and `lead` only. Shifting, datatypes and missing
  values are covered in `tablecloth.column.api.lag-lead-test`."
  (:require [tablecloth.api :as tc]
            [tablecloth.api.lag-lead :as sut]
            [tablecloth.column.api :as tcc]
            [midje.sweet :refer [facts fact => throws]]))

(def ds (tc/dataset {:a [1 2 3 4 5]}))

(facts "about dataset lag"
       (fact "adds the lagged column and leaves the source column alone"
             (let [out (sut/lag ds :a-prev :a 1)]
               (vec (:a out)) => [1 2 3 4 5]
               (vec (:a-prev out)) => [nil 1 2 3 4]
               (tc/column-names out) => [:a :a-prev]
               (tc/row-count out) => 5))

       (fact "boundary rows are missing, not 0"
             (let [out (sut/lag ds :a-prev :a 2)]
               (vec (tcc/missing (:a-prev out))) => [0 1]
               (vec (:a-prev out)) => [nil nil 1 2 3]))

       (fact "numeric columns keep their type (no float promotion)"
             (tcc/typeof (:a-prev (sut/lag ds :a-prev :a 1))) => :int64)

       (facts "about column names"
              (fact "3-arity defaults the name to <col>-lag-<k>"
                    (tc/column-names (sut/lag ds :a 2)) => [:a :a-lag-2])
              (fact "4-arity uses the given name"
                    (tc/column-names (sut/lag ds :x :a 2)) => [:a :x])
              (fact "string column names are supported"
                    (let [out (sut/lag (tc/dataset {"a" [1 2 3]}) "x" "a" 1)]
                      (vec (get out "x")) => [nil 1 2])))

       (facts "about options"
              (fact "5-arity passes :fill-with through"
                    (let [out (sut/lag ds :a-prev :a 2 {:fill-with 0})]
                      (vec (:a-prev out)) => [0 0 1 2 3]
                      (vec (tcc/missing (:a-prev out))) => []))
              (fact "empty options behave like the 4-arity"
                    (vec (:x (sut/lag ds :x :a 1 {}))) => [nil 1 2 3 4])))

(facts "about dataset lead"
       (fact "adds the lead column and leaves the source column alone"
             (let [out (sut/lead ds :a-next :a 1)]
               (vec (:a out)) => [1 2 3 4 5]
               (vec (:a-next out)) => [2 3 4 5 nil]
               (tc/column-names out) => [:a :a-next]
               (tc/row-count out) => 5))

       (fact "boundary rows are missing, not 0"
             (let [out (sut/lead ds :a-next :a 2)]
               (vec (tcc/missing (:a-next out))) => [3 4]
               (vec (:a-next out)) => [3 4 5 nil nil]))

       (fact "numeric columns keep their type (no float promotion)"
             (tcc/typeof (:a-next (sut/lead ds :a-next :a 1))) => :int64)

       (facts "about column names"
              (fact "3-arity defaults the name to <col>-lead-<k>"
                    (tc/column-names (sut/lead ds :a 2)) => [:a :a-lead-2])
              (fact "4-arity uses the given name"
                    (tc/column-names (sut/lead ds :x :a 2)) => [:a :x])
              (fact "string column names are supported"
                    (let [out (sut/lead (tc/dataset {"a" [1 2 3]}) "x" "a" 1)]
                      (vec (get out "x")) => [2 3 nil])))

       (facts "about options"
              (fact "5-arity passes :fill-with through"
                    (let [out (sut/lead ds :a-next :a 2 {:fill-with 0})]
                      (vec (:a-next out)) => [3 4 5 0 0]
                      (vec (tcc/missing (:a-next out))) => []))
              (fact "empty options behave like the 4-arity"
                    (vec (:x (sut/lead ds :x :a 1 {}))) => [2 3 4 5 nil])))

(facts "about invalid sizes"
       (fact "errors from the column functions pass through"
             (sut/lag ds :x :a -1) => (throws clojure.lang.ExceptionInfo #"Use `lead`")
             (sut/lead ds :x :a -1) => (throws clojure.lang.ExceptionInfo #"Use `lag`")
             (sut/lag ds :a nil) => (throws clojure.lang.ExceptionInfo)))
