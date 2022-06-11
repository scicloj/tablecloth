(ns tablecloth.api.column
  (:refer-clojure :exclude [group-by])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]))

(defn column
  ([] (col/new-column nil []))
  ([data]
   (column data {:name nil}))
  ([data {:keys [name]}]
   (col/new-column name data)))


(defn column? [item]
  (= (class item) tech.v3.dataset.impl.column.Column))

(defn zeros [n-zeros]
  (column (repeatedly n-zeros (constantly 0))))

(defn ones [n-ones]
  (column (repeatedly n-ones (constantly 1))))

(defn typeof [col]
  (dtype/elemwise-datatype col))

