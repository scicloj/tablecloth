(ns tablecloth.column.api.lag-lead-test
  (:require [tablecloth.column.api.lag-lead :refer [lag lead]]
            [tablecloth.column.api :refer [column column? missing typeof]]
            [midje.sweet :refer [tabular facts fact =>]]))

(facts "about lag"
       (fact "returns a column"
             (column? (lag (column [1 2 3]) 1)) => true)

       (fact "it shifts elements forward by n positions, filling with nil by default"
             (tabular (fact (lag ?coll ?n) => ?result)
                      ?coll ?n ?result
                      (column [1 2 3]) 1 (column [nil 1 2])
                      (column [1 2 3]) 2 (column [nil nil 1])))
       (fact "works for all standard datatypes"
             (tabular (fact (lag ?coll ?n) => ?result)
                      ?coll ?n ?result
                      (column [1.0 2.0 3.0]) 1 (column [nil 1.0 2.0])
                      (column [true false true]) 1 (column [nil true false])
                      (column ["a" "b" "c"]) 1 (column [nil "a" "b"])))

       (facts "about datatypes"
              (fact "maintains datatypes"
                    (tabular (fact (typeof (lag ?coll ?n)) => ?dtype)
                             ?coll ?n ?dtype
                             (column [1 2 3]) 1 :int64
                             (column [1.0 2.0 3.0]) 1 :float64
                             (column [true false true]) 1 :boolean
                             (column ["a" "b" "c"]) 1 :string
                             (column [(java.time.LocalDate/of 2020 1 1)
                                      (java.time.LocalDate/of 2020 1 2)
                                      (java.time.LocalDate/of 2020 1 3)]) 1 :packed-local-date))

              (fact "maintains datatypes when all values are shifted"
                    (typeof (lag (column [1 2 3]) 3)) => :int64))

       (facts "about missing values"
              (fact "prepended values are missing"
                    (set (missing (lag (column [1 2 3]) 1))) => #{0})
              (fact "maintains old missing values"
                    (set (missing (lag (column [1 nil 3]) 1)))
                    => #{0 2})
              (fact "a lag larger than the column gives all missing values"
                    (set (missing (lag (column [1 2 3]) 4))) => #{0 1 2})))

(facts "about lead"
       (fact "returns a column"
             (column? (lead (column [1 2 3]) 1)) => true)

       (fact "it shifts elements backward by n positions, filling with nil by default"
             (tabular (fact (lead ?coll ?n) => ?result)
                      ?coll ?n ?result
                      (column [1 2 3]) 1 (column [2 3 nil])
                      (column [1 2 3]) 2 (column [3 nil nil])))
       (fact "works for all standard datatypes"
             (tabular (fact (lead ?coll ?n) => ?result)
                      ?coll ?n ?result
                      (column [1.0 2.0 3.0]) 1 (column [2.0 3.0 nil])
                      (column [true false true]) 1 (column [false true nil])
                      (column ["a" "b" "c"]) 1 (column ["b" "c" nil])))

       (facts "about datatypes"
              (fact "maintains datatypes"
                    (tabular (fact (typeof (lead ?coll ?n)) => ?dtype)
                             ?coll ?n ?dtype
                             (column [1 2 3]) 1 :int64
                             (column [1.0 2.0 3.0]) 1 :float64
                             (column [true false true]) 1 :boolean
                             (column ["a" "b" "c"]) 1 :string
                             (column [(java.time.LocalDate/of 2020 1 1)
                                      (java.time.LocalDate/of 2020 1 2)
                                      (java.time.LocalDate/of 2020 1 3)]) 1 :packed-local-date))

              (fact "maintains datatypes when all values are shifted"
                    (typeof (lead (column [1 2 3]) 3)) => :int64))

       (facts "about missing values"
              (fact "appended values are missing"
                    (set (missing (lead (column [1 2 3]) 1))) => #{2})
              (fact "maintains old missing values"
                    (set (missing (lead (column [1 nil 3]) 1)))
                    => #{0 2})
              (fact "a lead larger than the column gives all missing values"
                    (set (missing (lead (column [1 2 3]) 4))) => #{0 1 2})))
