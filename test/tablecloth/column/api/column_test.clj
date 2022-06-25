(ns tablecloth.column.api.column-test
  (:require [tablecloth.column.api.column :refer [column zeros ones]] 
            [midje.sweet :refer [fact =>]]))

(fact "`column` returns a column"
      (tech.v3.dataset.column/is-column? (column)) => true)

(fact "`column` provides a few ways to generate a column`"
      (column) => []
      (column [1 2 3]) => [1 2 3]
      (column (list 1 2 3)) =>[1 2 3]
      (column (range 3)) => [0 1 2])

(fact "`zeros` returns a column filled with zeros"
      (zeros 3) => [0 0 0])

(fact "`ones` returns a column filled with ones"
      (ones 3) => [1 1 1])
