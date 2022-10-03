(ns tablecloth.column.api.column-test
  (:require [tablecloth.column.api.column :refer [column zeros ones typeof? typeof]] 
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

(fact "`typeof` returns the concrete type of the elements"
      (typeof (column [1 2 3])) => :int64 
      (typeof (column ["a" "b" "c"])) => :string 
      (typeof (column [true false])) => :boolean)

(fact "we can check the type of a column's elements with `typeof?`"
      (typeof? (column [1 2 3]) :integer) => true
      (typeof? (column [1 2 3]) :textual) => false
      (typeof? (column [1.0 2.0 3.0]) :numerical) => true
      (typeof? (column [1.0 2.0 3.0]) :logical) => false
      (typeof? (column ["a" "b" "c"]) :textual) => true
      (typeof? (column ["a" "b" "c"]) :numerical) => false
      (typeof? (column [true false true]) :logical) => true)

(fact "we can check the concrete type of a column's elements with `typeof?`"
      (typeof? (column [1 2 3]) :int64) => true
      (typeof? (column [1 2 3]) :int32) => false)

(fact "`zeros` returns a column filled with zeros"
      (zeros 3) => [0 0 0])

(fact "`ones` returns a column filled with ones"
      (ones 3) => [1 1 1])
