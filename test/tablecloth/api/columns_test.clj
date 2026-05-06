(ns tablecloth.api.columns-test
  (:require [tablecloth.api :as api]
            [tech.v3.datatype :as dtype]
            [clojure.test :refer [deftest testing is]]
            [midje.sweet :refer [tabular fact =>]]))


;; https://github.com/scicloj/tablecloth/issues/9
(def dss (api/dataset {:idx [1 1 1 2 2 2 3 3 3]
                       :a ["a" "b" "c" "a" "b" "c" "a" "b" "c"]
                       "z" 1
                       :b [1 2 3 2 3 4 3 2 1]
                       :c [3 1 2 4 2 1 3 2 4]}))

(fact "reorder-columns"
      (tabular (fact (-> dss
                         (api/reorder-columns ?order)
                         (api/column-names))
                     =>
                     ?expected)
               ?order        ?expected
               [:ids :b :a :c]       [:b :a :c :idx "z"]
               [:idx :b :a "z" :c]   [:idx :b :a "z" :c]
               [:idx :b :a :C]       [:idx :b :a "z" :c]
               [:c :A :b :e :z :idx] [:c :b :idx :a "z"]
               string?               ["z" :idx :a :b :c]
               #".*[az]$"            [:a "z" :idx :b :c])
      (fact [:b :a :c :idx "z"]
            =>
            (-> dss
                (api/reorder-columns :b :a [:c :ids])
                (api/column-names))))

(fact "add-or-replace"
      (tabular (fact (-> {:x [1 2]}
                         (api/dataset)
                         (api/add-or-replace-column :y ?v)
                         :y
                         (dtype/get-datatype))
                     =>
                     ?expected)
               ?expected ?v
               :int64   1
               :float64 1.0
               :string  "abc"))

(fact "add"
      (tabular (fact (-> {:x [1 2]}
                         (api/dataset)
                         (api/add-column :y ?v)
                         :y
                         (dtype/get-datatype))
                     =>
                     ?expected)
               ?expected ?v
               :int64   1
               :float64 1.0
               :string  "abc"))

(fact "update-columns"
      (->
       (api/update-columns dss
                           {:a (fn [row] (map count row))
                            :b (fn [row] (map inc row))})
       (api/select-columns [:a :b])
       (api/rows :as-maps)) =>
      [{:a 1, :b 2} {:a 1, :b 3} {:a 1, :b 4} {:a 1, :b 3} {:a 1, :b 4} {:a 1, :b 5} {:a 1, :b 4} {:a 1, :b 3} {:a 1, :b 2}]

      (->
       (api/update-columns dss [:a]
                           [(fn [row] (map count row))])
       :a
       seq) => [1 1 1 1 1 1 1 1 1])

(deftest clean-column-names
  (testing "normalizes column names to kebab-cased keywords"
    ;; See `to-clean-keyword` test for in-depth examples
    (is (= [:a-test :b-test :c-test]
           (-> {"A Test" []
                "bTest" []
                :C_TEST []}
               api/dataset
               api/clean-column-names
               api/column-names)))))
