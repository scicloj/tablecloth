(ns tablecloth.api.fold-unroll-test
  (:require [tablecloth.api :as api]
            [midje.sweet :refer [tabular fact =>]]))

(fact "one-row-ds"
      (-> (api/dataset {:a [1] :b [2]})
          (api/fold-by :a set)
          (api/rows))
      =>
      [[1 #{2}]])

(fact "empty-ds"
      (-> (api/dataset {:a [1]})
          (api/drop-rows 0)
          (api/fold-by :a)
          (api/empty-ds?))
      =>
      true)
