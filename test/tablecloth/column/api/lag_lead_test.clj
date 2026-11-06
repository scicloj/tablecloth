(ns tablecloth.column.api.lag-lead-test
  (:require [tablecloth.column.api.lag-lead :refer [lag lead]]
            [tablecloth.column.api :refer [column column? missing typeof]]
            [midje.sweet :refer [tabular facts fact => =not=>]]))

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

(defn- thrown-data
  "Returns [message ex-data] if `f` throws an ExceptionInfo, else nil."
  [f]
  (try (f) nil
       (catch clojure.lang.ExceptionInfo e [(ex-message e) (ex-data e)])))

(facts "about invalid sizes"
       (fact "a size of 0 returns the column unchanged"
             (vec (lag (column [1 2 3]) 0)) => [1 2 3]
             (vec (lead (column [1 2 3]) 0)) => [1 2 3])

       (fact "negative sizes throw, in every arity"
             (thrown-data #(lag (column [1 2 3]) -1)) =not=> nil
             (thrown-data #(lag (column [1 2 3]) -1 {})) =not=> nil
             (thrown-data #(lead (column [1 2 3]) -1)) =not=> nil
             (thrown-data #(lead (column [1 2 3]) -1 {})) =not=> nil)

       (fact "the message points to the opposite function"
             (first (thrown-data #(lag (column [1 2 3]) -1))) => #"Use `lead`"
             (first (thrown-data #(lead (column [1 2 3]) -1))) => #"Use `lag`")

       (fact "the error carries data"
             (second (thrown-data #(lag (column [1 2 3]) -2)))
             => {:type :tablecloth/invalid-argument :fn "lag" :size -2}
             (second (thrown-data #(lead (column [1 2 3]) -2)))
             => {:type :tablecloth/invalid-argument :fn "lead" :size -2})

       (fact "nil and non-integer sizes throw"
             (thrown-data #(lag (column [1 2 3]) nil)) =not=> nil
             (thrown-data #(lag (column [1 2 3]) 1.5)) =not=> nil
             (thrown-data #(lead (column [1 2 3]) nil)) =not=> nil
             (thrown-data #(lead (column [1 2 3]) 1.5)) =not=> nil))
