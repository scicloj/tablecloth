(ns tablecloth.api.rows-test
  (:require [midje.sweet :refer [fact facts =>]]
            [tablecloth.api :as tc]
            [tablecloth.api.rows :as sut]))

(facts "between"
       (facts "if returns the correct rows"
              (-> {:a [1 2 5 6 7 10 11] :b [1 2 3 4 5 6 7]}
                  tc/dataset
                  (sut/between :a 5 10)
                  (tc/columns :as-map)) => {:a [6 7] :b [4 5]})
       (facts "if works with missing values in the given column only when given a default"
              (-> {:a [1 nil 5 6 7 10 nil] :b [1 2 3 4 5 6 7]}
                  tc/dataset
                  (sut/between :a 5 10 {:missing-default 0})
                  (tc/columns :as-map)) => {:a [6 7] :b [4 5]}))

(fact "duplicate-rows"
      (-> {:a [1 2 3 1 5 5] :b [2 4 5 2 9 9]}
          tc/dataset
          sut/duplicate-rows
          (tc/columns :as-map)) => {:a [1 1 5 5] :b [2 2 9 9]})
