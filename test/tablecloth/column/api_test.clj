(ns tablecloth.column.api-test
  (:require [tablecloth.column.api :as api]
            [midje.sweet :refer [fact =>]]))

(fact "`column` returns a column"
      (tech.v3.dataset.column/is-column? (api/column)) => true)

(fact "`column` provides a few ways to generate a column`"
      (api/column) => []
      (api/column [1 2 3]) => [1 2 3]
      (api/column (list 1 2 3)) =>[1 2 3]
      (api/column (range 3)) => [0 1 2])
