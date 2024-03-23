(ns tablecloth.column.api.missing-test
  (:require [tablecloth.column.api.missing :refer [count-missing replace-missing drop-missing]]
            [tablecloth.column.api.column :refer [column]]
            [midje.sweet :refer [facts =>]]))

(facts "about `count-missing`"
       (count-missing (column [1 2 3])) => 0)

(facts "about `replace-missing`"
       (replace-missing (column [1 nil 3])) => [1 1 3])

(facts "about `drop-missing`"
       (drop-missing (column [1 nil 3])) => [1 3])
