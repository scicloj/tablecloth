(ns tablecloth.api.columns-test
  (:require [tablecloth.api :as api]            
            [clojure.test :refer [deftest is are]]
            [tech.v3.datatype :as dtype]))


;; https://github.com/scicloj/tablecloth/issues/9
(def dss (api/dataset {:idx [1 1 1 2 2 2 3 3 3]
                       :a ["a" "b" "c" "a" "b" "c" "a" "b" "c"]
                       "z" 1
                       :b [1 2 3 2 3 4 3 2 1]
                       :c [3 1 2 4 2 1 3 2 4]}))

(deftest reorder-columns
  (are [order expected] (= expected (-> dss
                                        (api/reorder-columns order)
                                        (api/column-names)))
    [:ids :b :a :c] [:b :a :c :idx "z"]
    [:idx :b :a "z" :c] [:idx :b :a "z" :c]
    [:idx :b :a :C] [:idx :b :a "z" :c]
    [:c :A :b :e :z :idx] [:c :b :idx :a "z"]
    string? ["z" :idx :a :b :c]
    #".*[az]$" [:a "z" :idx :b :c])
  (is (= [:b :a :c :idx "z"] (-> dss
                                 (api/reorder-columns :b :a [:c :ids])
                                 (api/column-names)))))

;; This test uses the add-or-replace-column function which is deprecated
(deftest add-or-replace
  (are [expected v] (= expected (-> {:x [1 2]}
                                    (api/dataset)
                                    (api/add-or-replace-column :y v)
                                    :y
                                    (dtype/get-datatype)))
    :int64 1
    :float64 1.0
    :string "abc"))

(deftest add-column
  (are [expected v] (= expected (-> {:x [1 2]}
                                    (api/dataset)
                                    (api/add-column :y v)
                                    :y
                                    (dtype/get-datatype)))
                    :int64 1
                    :float64 1.0
                    :string "abc"))
