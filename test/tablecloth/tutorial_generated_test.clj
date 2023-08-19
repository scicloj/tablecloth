(ns
    tutorial-generated-test
  (:require [clojure.test :refer [deftest is]]
            [tablecloth.api :as tc]))


(deftest test__45996__auto__
  (is (=

       (-> (map int-array [[1 2] [3 4] [5 6]])
           (into-array)
           (tc/dataset {:layout :as-rows
                        :column-names [:a :b]}))

       (tablecloth.api/dataset {:a [1 3 5], :b [2 4 6]}))))
