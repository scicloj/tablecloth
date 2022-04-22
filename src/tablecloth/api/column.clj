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
   (col/new-column data)))


