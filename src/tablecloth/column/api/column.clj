(ns tablecloth.column.api.column
  (:require [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]
            [tablecloth.api.utils :refer [->general-types concrete-type? type?]]))

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

(defn typeof
  "Returns the concrete type of the elements within the column `col`."
  [col]
  (dtype/elemwise-datatype col))

(defn typeof?
  "True|false the column's elements are of the provided type `datatype`.
   Works with concrete types (e.g. :int32) or general types (e.g. :numerical)."
  [col datatype]
  (let [concrete-type-of-els (dtype/elemwise-datatype col)]
    (if (concrete-type? datatype) 
      (= datatype concrete-type-of-els)
      (not (nil? (type? datatype concrete-type-of-els))))))

(defn zeros
  "Create a new column filled wth `n-zeros`."
  [n-zeros]
  (column (dtype/const-reader 0 n-zeros)))

(defn ones
  "Creates a new column filled with `n-ones`"
  [n-ones]
  (column (dtype/const-reader 1 n-ones)))


