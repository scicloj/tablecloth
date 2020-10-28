(ns tablecloth.api.fold-unroll-test
  (:require [tablecloth.api :as api]
            [clojure.test :refer [deftest is]]))

(deftest one-row-ds
  (is (= [[1 #{2}]] (-> (api/dataset {:a [1] :b [2]})
                        (api/fold-by :a set)
                        (api/rows)))))

(deftest empty-ds
  (is (-> (api/dataset {:a [1]})
          (api/drop-rows 0)
          (api/fold-by :a)
          (api/empty-ds?))))
