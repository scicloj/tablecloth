(ns tablecloth.api.rows-test
  (:require [clojure.test :refer [deftest testing is]]
            [tablecloth.api :as tc]
            [tablecloth.api.rows :as sut]))

(deftest between
  (testing "returns the correct rows"
    (is (= {:a [6 7] :b [4 5]}
           (-> {:a [1 2 5 6 7 10 11] :b [1 2 3 4 5 6 7]}
               tc/dataset
               (sut/between :a 5 10)
               (tc/columns :as-map)))))

  (testing "works with missing values in the given column only when given a default"
    (is (= {:a [6 7] :b [4 5]}
           (-> {:a [1 nil 5 6 7 10 nil] :b [1 2 3 4 5 6 7]}
               tc/dataset
               (sut/between :a 5 10 {:missing-default 0})
               (tc/columns :as-map))))))

(deftest duplicate-rows
  (testing "returns only duplicate rows"
    (is (= {:a [1 1 5 5] :b [2 2 9 9]}
           (-> {:a [1 2 3 1 5 5] :b [2 4 5 2 9 9]}
               tc/dataset
               sut/duplicate-rows
               (tc/columns :as-map))))))