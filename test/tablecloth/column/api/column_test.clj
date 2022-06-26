(ns tablecloth.column.api.column-test
  (:require [tablecloth.column.api.column :refer [column zeros ones typeof?]] 
            [midje.sweet :refer [fact =>]]))

(fact "`column` returns a column"
      (tech.v3.dataset.column/is-column? (column)) => true)

(fact "`column` provides a few ways to generate a column`"
      (column) => []
      (column [1 2 3]) => [1 2 3]
      (column (list 1 2 3)) =>[1 2 3]
      (column (range 3)) => [0 1 2])

(fact "`column` identifies the type of the elements automatically"
      (-> [1 2 3]
          (column)
          (tech.v3.datatype/elemwise-datatype)) => :int64
      (-> [true false true]
          (column)
          (tech.v3.datatype/elemwise-datatype)) => :boolean
      (-> [1 true false]
          (column)
          (tech.v3.datatype/elemwise-datatype)) => :object)

(fact "we can check the type of a column's elements with `typeof?`"
      (typeof? (column [1 2 3]) :int64) => true
      (typeof? (column [true false]) :boolean) => true)

(fact "`zeros` returns a column filled with zeros"
      (zeros 3) => [0 0 0])

(fact "`ones` returns a column filled with ones"
      (ones 3) => [1 1 1])
