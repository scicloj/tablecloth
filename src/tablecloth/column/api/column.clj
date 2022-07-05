(ns tablecloth.column.api.column
  (:require [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]))

(defn column
  "Create a `column` from a vector or sequence. "
  ([]
   (col/new-column nil []))
  ([data]
   (column data {:name nil}))
  ([data {:keys [name]
          :as options}]
   (col/new-column name data)))

;; Alias for tech.v3.dasetset.column.is-column?
(defn column? 
  "Return true or false `item` is a column."
  [item]
  (col/is-column? item))

;; Alias for tech.v3.datatype.elemwise-datatype`
(defn typeof
  "Returns the datatype fo the elements within the column `col`."
  [col]
  (dtype/elemwise-datatype col))

(defn typeof?
  "True|false the column's elements are of type `dtype`"
  [col dtype]
  (= (dtype/elemwise-datatype col) dtype))

(defn zeros
  "Create a new column filled wth `n-zeros`."
  [n-zeros]
  (column (dtype/emap (constantly 0) :int64 (range n-zeros))))

(defn ones
  "Creates a new column filled with `n-ones`"
  [n-ones]
  (column (dtype/emap (constantly 1) :int64 (range n-ones))))
