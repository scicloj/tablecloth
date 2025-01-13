(ns tablecloth.column.api.column-test
  (:require [tablecloth.column.api.column :refer [column zeros ones typeof?
                                                  typeof slice sort-column]] 
            [tech.v3.dataset.column :refer [is-column?]]
            [midje.sweet :refer [fact facts =>]]))

(fact "`column` returns a column"
      (is-column? (column)) => true)

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

(fact "`typeof?` can check the concerete type of column elements"
      (typeof? (column [1 2 3]) :int64) => true
      (typeof? (column [1 2 3]) :int32) => false
      (typeof? (column ["a" "b" "c"]) :string) => true)

(fact "`typeof?` can check the general type of column elements"
      (typeof? (column [1 2 3]) :integer) => true
      (typeof? (column [1 2 3]) :textual) => false
      (typeof? (column [1.0 2.0 3.0]) :numerical) => true
      (typeof? (column [1.0 2.0 3.0]) :logical) => false
      (typeof? (column ["a" "b" "c"]) :textual) => true
      (typeof? (column ["a" "b" "c"]) :numerical) => false
      (typeof? (column [true false true]) :logical) => true)

(fact "`zeros` returns a column filled with zeros"
      (zeros 3) => [0 0 0])

(fact "`ones` returns a column filled with ones"
      (ones 3) => [1 1 1])

(facts "about `slice`"
       (let [c (column [1 2 3 4 5])]
        (fact "it return a subset of a column inclusively"
              (slice c 0 0) => [1]
              (slice c 0 4) => [1 2 3 4 5])
        (fact "it supports negative indexing inclusively"
              (slice c 0 -1)
              (slice c -1 -1) => [5]
              (slice c -3 -1) => [3 4 5])
        (fact "it supports 0 within negative indexing"
              (slice c 0 -2) => [1 2 3 4])
        (fact "it supports stepped slicing"
              (slice c 0 4 2) => [1 3 5])
        (fact "it supports using nil to indicate slice from start or end"
              (slice c 2) => [3 4 5]
              (slice c -2) => [4 5]
              (slice c nil 2) => [1 2 3]
              (slice c nil -2) => [1 2 3 4])
        (fact "it supports special keywords for selecting from start or end"
              (slice c :start 2) => [1 2 3]
              (slice c 1 :end) => [2 3 4 5]
              (slice c -4 :end) => [2 3 4 5]
              (slice c :start -3) => [1 2 3])))

(facts "about `sort-column`"
       (let [c-ints (column [3 100 9 0 -10 43])
             c-strings (column ["a" "z" "baz" "fo" "bar" "foo"])]
         (fact "it returns a column"
               (sort-column c-ints) => is-column?)
         (fact "it sorts in ascending order by default"
               (sort-column c-ints) => [-10 0 3 9 43 100]
               (sort-column c-strings) => ["a" "bar" "baz" "fo" "foo" "z"])
         (fact "it accepts a comparator-fn"
               (sort-column c-strings
                            #(> (count %1) (count %2))) => ["baz" "bar" "foo" "fo" "z" "a"])
         (fact "it moves missing values to the end"
               (sort-column (column [nil 100 nil 3 -10])) => [-10 3 100 nil nil])))

