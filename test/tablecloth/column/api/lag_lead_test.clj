(ns tablecloth.column.api.lag-lead-test
  (:require [tablecloth.column.api.lag-lead :refer [lag lead]]
            [tablecloth.column.api :as tcc :refer [column column? missing typeof]]
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

(facts "about :fill-with"
       (fact "an integer column with missing values keeps its datatype and missing positions"
             (let [out (lag (column [10 20 nil 40 50]) 1 {:fill-with 0})]
               (vec out) => [0 10 20 nil 40]
               (typeof out) => :int64
               (set (missing out)) => #{3})
             (let [out (lead (column [10 20 nil 40 50]) 2 {:fill-with 0})]
               (vec out) => [nil 40 50 0 0]
               (typeof out) => :int64
               (set (missing out)) => #{0}))

       (fact "a float column with missing values keeps its datatype"
             (typeof (lag (column [1.5 nil 3.5]) 1 {:fill-with 0})) => :float64
             (typeof (lead (column [1.5 nil 3.5]) 1 {:fill-with 0.0})) => :float64)

       (fact "string columns accept a string fill"
             (let [out (lag (column ["a" nil "c"]) 1 {:fill-with "z"})]
               (vec out) => ["z" "a" nil]
               (typeof out) => :string
               (set (missing out)) => #{2})
             (vec (lead (column ["a" nil "c"]) 1 {:fill-with "z"})) => [nil "c" "z"])

       (fact "packed dates accept a date fill and stay packed"
             (let [d   #(java.time.LocalDate/of 2020 1 %)
                   out (lead (column [(d 1) nil (d 3)]) 1 {:fill-with (d 9)})]
               (vec out) => [nil (d 3) (d 9)]
               (typeof out) => :packed-local-date
               (set (missing out)) => #{0}))

       (fact "a size of 0 returns the column unchanged"
             (vec (lag (column [10 nil 30]) 0 {:fill-with 0})) => [10 nil 30]
             (vec (lead (column [10 nil 30]) 0 {:fill-with 0})) => [10 nil 30])

       (fact "a size at least as large as the column fills every row"
             (tabular (fact (let [out (?f (column [10 nil 30]) ?n {:fill-with 0})]
                              [(vec out) (typeof out) (set (missing out))])
                            => [[0 0 0] :int64 #{}])
                      ?f   ?n
                      lag  3
                      lag  4
                      lead 3
                      lead 4))

       (fact "a fill that does not match the column datatype throws a clear error"
             (first (thrown-data #(lag (column ["a" "b"]) 1 {:fill-with 0})))
             => #"does not match the column datatype :string"
             (first (thrown-data #(lead (column [1 2 3]) 1 {:fill-with 1.5})))
             => #"does not match the column datatype :int64"
             (second (thrown-data #(lag (column [1 2 3]) 1 {:fill-with "x"})))
             => {:type :tablecloth/invalid-argument :fn "lag" :fill-with "x" :datatype :int64})

       (fact "the fill is checked even when the size is 0"
             (thrown-data #(lag (column [1 2 3]) 0 {:fill-with "x"})) =not=> nil))

(facts "about the public Column API exports"
       (fact "tcc/lag and tcc/lead exist"
             (some? (resolve 'tablecloth.column.api/lag)) => true
             (some? (resolve 'tablecloth.column.api/lead)) => true)

       (fact "they match the implementation, with and without options"
             (let [c   (column [10 20 nil 40 50])
                   obs (fn [x] [(vec x) (typeof x) (set (missing x))])]
               (obs (tcc/lag c 1)) => (obs (lag c 1))
               (obs (tcc/lead c 2)) => (obs (lead c 2))
               (obs (tcc/lag c 1 {:fill-with 0})) => [[0 10 20 nil 40] :int64 #{3}]
               (obs (tcc/lead c 2 {:fill-with 0})) => [[nil 40 50 0 0] :int64 #{0}]))

       (fact "they keep packed dates"
             (typeof (tcc/lead (column [(java.time.LocalDate/of 2020 1 1)
                                        (java.time.LocalDate/of 2020 1 2)]) 1))
             => :packed-local-date)

       (fact "they raise the same errors"
             (second (thrown-data #(tcc/lag (column [1 2 3]) -1)))
             => {:type :tablecloth/invalid-argument :fn "lag" :size -1}
             (first (thrown-data #(tcc/lead (column [1 2 3]) -1))) => #"Use `lag`"
             (second (thrown-data #(tcc/lead (column ["a"]) 1 {:fill-with 0})))
             => {:type :tablecloth/invalid-argument :fn "lead" :fill-with 0 :datatype :string}))
