;; # Tablecloth Column Exploration

^{:kind/hidden true}
(ns intro
  (:require [tablecloth.api :as tc]
            [scicloj.clay.v2.api :as clay]
            [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]
            ))

^{:kind/hidden true}
(clay/start!)

^{:kind/hidden true}
(comment
  (do (clay/show-doc! "docs/column_exploration.clj" {:hide-doc? true}))
  ,)

;; ## What is this exploration?
;; 
;; We want to add a `column` entity to tablecloth that parallels `dataset`. It will make
;; the column a first-class entity within tablecloth.

;; ## Usage

(require '[tablecloth.column.api :refer [column] :as col])

;; ### Column creation

;; We can create an empty column like this:

(column)

;; We can check if it it's a column.

(col/column? (column))

;; We can create a columns with data in a number of ways

(column [1 2 3 4])

(column (range 10))

;; When you do this the types of the resulting array is determined
;; automatically from the items provided.

(let [int-column (column (range 10))]
  (col/typeof int-column))

(let [string-column (column ["foo" "bar"])]
  (col/typeof string-column))

;; ### Basic Operations

;; Right now we need to use the functional name space from the
;; underlying computation library tech.v3.datatype to operate on
;; columns.

(require '[tech.v3.datatype.functional :as fun])

;; With that imported we can perform a large number of operations:

(def a (column [20 30 40 50]))
(def b (column (range 4)))

(fun/- a b)

(fun/pow a 2)

(fun/* 10 (fun/sin a))

(fun/< a 35)
