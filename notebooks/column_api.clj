^:kindly/hide-code?
(ns column-api
  (:require [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]
            [scicloj.kindly.v4.kind :as kind :refer [md]]))

;; ## Column API

;; A `column` in tablecloth is a named sequence of typed data. This special type is defined in the `tech.ml.dataset`. It is roughly comparable to a R vector.

;; ### Column Creation

;; Empty column

(tcc/column)

;; Column from a vector or a sequence

(tcc/column [1 2 3 4 5])

(tcc/column `(1 2 3 4 5))

;; You can also quickly create columns of ones or zeros:

(tcc/ones 10)

(tcc/zeros 10)

;; Finally, you can use the `column?` function to check if an item is a column:

(tcc/column? [1 2 3 4 5])
(tcc/column? (tcc/column))

;; ### Types and Type detection

;; The default set of types for a column are defined in the underlying "tech ml" system. We can see the set here:

(tech.v3.datatype.casting/all-datatypes)

;; When you create a column, the underlying system will try to autodetect its type. We can see that here using the `tcc/typeof` function to check the type of a column:

(-> (tcc/column [1 2 3 4 5])
    (tcc/typeof))

(-> (tcc/column [:a :b :c :d :e])
    (tcc/typeof))

;; Columns containing heterogenous data will receive type `:object`:

(-> (tcc/column [1 :b 3 :c 5])
    (tcc/typeof))

;; You can also use the `tcc/typeof?` function to check the value of a function as an asssertion:

(-> (tcc/column [1 2 3 4 6])
    (tcc/typeof? :boolean))

(-> (tcc/column [1 2 3 4 6])
    (tcc/typeof? :int64))

;; Tablecloth has a concept of "concrete" and "general" types. A general type is the broad category of type and the concrete type is the actual type in memory. For example, a concrete type is a 64-bit integer `:int64`, which is also of the general type `:integer`. The `typeof?` function supports checking both.

(-> (tcc/column [1 2 3 4 6])
    (tcc/typeof? :int64))

(-> (tcc/column [1 2 3 4 6])
    (tcc/typeof? :integer))

;; ### Column access & manipulation

;; The method for accessing a particular index position in a column is the same as for Clojure vectors:

(-> (tcc/column [1 2 3 4 5])
    (get 3))

(-> (tcc/column [1 2 3 4 5])
    (nth 3))

;; You can also slice a column

(-> (tcc/column (range 10))
    (tcc/slice 5))

(-> (tcc/column (range 10))
    (tcc/slice 1 4))

(-> (tcc/column (range 10))
    (tcc/slice 0 9 2))

;; For clarity, the `slice` method supports the `:end` and `:start` keywords:

(-> (tcc/column (range 10))
    (tcc/slice :start :end 2))

;; If you need to create a discontinuous subset of the column, you can use the `select` function. This method accepts an array of index positions or an array of booleans. When using boolean select, a true value will select the value at the index positions containing true values:

;; Select the values at index positions 1 and 9:

(-> (tcc/column (range 10))
    (tcc/select [1 9]))


;; Select the values at index positions 0 and 2 using booelan select:

(-> (tcc/column (range 10))
    (tcc/select (tcc/column [true false true])))

;; ### Column Operations

;; The Column API contains the vast majority of column opeartions that have typically been available in the `tech.v3.datatype.functional` namespace.

(def a (tcc/column [20 30 40 50]))
(def b (tcc/column (range 4)))

(tcc/- a b)

(tcc/pow a 2)

(tcc/* 10 (tcc/sin a))

(tcc/< a 35)

;; All these operations take a column as their first argument and
;; return a column, so they can be chained easily.

(-> a
    (tcc/* b)
    (tcc/< 70))
