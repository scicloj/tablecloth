(ns tablecloth.column.api
  (:refer-clojure :exclude [group-by])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]))

(defn column
  "Create a `column` from a vector or sequence. "
  ([] (col/new-column nil []))
  ([data]
   (column data {:name nil}))
  ([data {:keys [name]}]
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

(defn zeros
  "Create a new column filled wth `n-zeros`."
  [n-zeros]
  (column (repeatedly n-zeros (constantly 0))))

(defn ones
  "Creates a new column filled with `n-ones`"
  [n-ones]
  (column (repeatedly n-ones (constantly 1))))
