(ns index-generated-test
  (:require
   [clojure.test :refer [deftest is]]
   [scicloj.kindly.v3.kind :as kind]
   [scicloj.kindly-default.v1.api :refer [md]]
   [tablecloth.api :as tc]
   [scicloj.note-to-test.v1.api :as note-to-test]
   [scicloj.clay.v2.api :as clay]
   [tablecloth.api :as tc]
   [tech.v3.datatype.functional :as dfn]
   [tech.v3.datatype.functional :as dfn]
   [tech.v3.datatype.argops :as aops]
   [tech.v3.datatype :as dtype]))

(deftest test-everything

  (is (= (note-to-test/represent-value
          ^{:kindly/hide-code? true
            :kind/void true}
          (clay/swap-options!
           assoc
           :quarto {:format {:html {:toc true
                                    :theme :spacelab
                                    :embed-resources true}}
                    :highlight-style :solarized
                    :code-block-background true
                    :embed-resources true}))
       :ok))

  (is (= (note-to-test/represent-value
          ^{:kindly/hide-code? true
            :kind/void true}
          (note-to-test/define-value-representations!
            [{:predicate (fn [v]
                           (-> v
                               meta
                               :kindly/kind
                               (= :kind/md)))
              :representation (constantly :note-to-test/skip)}
             {:predicate (comp #{:local-date
                                 :local-date-time
                                 :packed-local-date
                                 :packed-local-date-time}
                               tech.v3.datatype/datatype)
              :representation (juxt class str)}
             {:predicate (fn [v]
                           (or (sequential? v)
                               ;; https://stackoverflow.com/a/9090730
                               (some-> v
                                       class
                                       (.isArray))
                               (instance? tech.v3.dataset.impl.column.Column v)))
              :representation (fn [values]
                                (->> values
                                     (take 20)
                                     (map note-to-test/represent-value)
                                     (into [])))}
             {:predicate (partial instance? java.util.Map)
              :representation (fn [m]
                                (-> m
                                    (update-keys note-to-test/represent-value)
                                    (update-vals note-to-test/represent-value)))}
             {:predicate (fn [v]
                           (-> v
                               class
                               (= java.lang.Object)))
              :representation class}
             {:predicate symbol?
              :representation (fn [s]
                                [:symbol (name s)])}
             {:predicate fn?
              :representation (constantly :fn)}
             {:predicate (fn [v]
                           (or (var? v)
                               (nil? v)))
              ;; handling vars and nils the same way
              ;; so that defonce will be represented consistently
              :representation (constantly :var-or-nil)}
             {:predicate (partial instance? org.roaringbitmap.RoaringBitmap)
              :representation (constantly
                               :org.roaringbitmap.RoaringBitmap)}
             {:predicate (fn [x]
                           (and (double? x)
                                (Double/isNaN x)))
              :representation (constantly :NaN)}]))
       [:ok]))


  (is (= (note-to-test/represent-value
          (def tech-ml-version (get-in (read-string (slurp "deps.edn")) [:deps 'techascent/tech.ml.dataset :mvn/version])))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def tablecloth-version (nth (read-string (slurp "project.clj")) 2)))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          tech-ml-version)
       "7.000-beta-51"))

  (is (= (note-to-test/represent-value
          tablecloth-version)
       "7.000-beta-51"))


  (is (= (note-to-test/represent-value
          (require '[tablecloth.api :as tc]
                   '[tech.v3.datatype.functional :as dfn]))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def DS (tc/dataset {:V1 (take 9 (cycle [1 2]))
                               :V2 (range 1 10)
                               :V3 (take 9 (cycle [0.5 1.0 1.5]))
                               :V4 (take 9 (cycle ["A" "B" "C"]))})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/dataset))
       {}))


  (is (= (note-to-test/represent-value
          (tc/dataset nil {:column-names [:a :b]}))
       {:a [], :b []}))


  (is (= (note-to-test/represent-value
          (tc/dataset [[:A 33] [:B 5] [:C :a]]))
       {:A [33], :B [5], :C [:a]}))


  (is (= (note-to-test/represent-value
          (tc/dataset [[:A [1 2 3 4 5 6]] [:B "X"] [:C :a]]))
       {:A [1 2 3 4 5 6], :B ["X" "X" "X" "X" "X" "X"], :C [:a :a :a :a :a :a]}))


  (is (= (note-to-test/represent-value
          (tc/dataset {:A 33}))
       {:A [33]}))

  (is (= (note-to-test/represent-value
          (tc/dataset {:A [1 2 3]}))
       {:A [1 2 3]}))

  (is (= (note-to-test/represent-value
          (tc/dataset {:A [3 4 5] :B "X"}))
       {:A [3 4 5], :B ["X" "X" "X"]}))


  (is (= (note-to-test/represent-value
          (tc/dataset {:A [[3 4 5] [:a :b]] :B "X"}))
       {:A [[3 4 5] [:a :b]], :B ["X" "X"]}))


  (is (= (note-to-test/represent-value
          (tc/dataset [{:a 1 :b 3} {:b 2 :a 99}]))
       {:a [1 99], :b [3 2]}))

  (is (= (note-to-test/represent-value
          (tc/dataset [{:a 1 :b [1 2 3]} {:a 2 :b [3 4]}]))
       {:a [1 2], :b [[1 2 3] [3 4]]}))


  (is (= (note-to-test/represent-value
          (tc/dataset [{:a nil :b 1} {:a 3 :b 4} {:a 11}]))
       {:a [:var-or-nil 3 11], :b [1 4 :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (-> (map int-array [[1 2] [3 4] [5 6]])
              (into-array)
              (tc/dataset)))
       {0 [1 3 5], 1 [2 4 6]}))


  (is (= (note-to-test/represent-value
          (-> (map int-array [[1 2] [3 4] [5 6]])
              (into-array)
              (tc/dataset {:layout :as-columns})))
       {0 [1 2], 1 [3 4], 2 [5 6]}))


  (is (= (note-to-test/represent-value
          (-> (map int-array [[1 2] [3 4] [5 6]])
              (into-array)
              (tc/dataset {:layout :as-rows
                           :column-names [:a :b]})))
       {:a [1 3 5], :b [2 4 6]}))


  (is (= (note-to-test/represent-value
          (-> (map to-array [[:a :z] ["ee" "ww"] [9 10]])
              (into-array)
              (tc/dataset {:column-names [:a :b :c]
                           :layout :as-columns})))
       {:a [:a :z], :b ["ee" "ww"], :c [9 10]}))


  (is (= (note-to-test/represent-value
          (tc/let-dataset [x (range 1 6)
                            y 1
                            z (dfn/+ x y)]))
       {:x [1 2 3 4 5], :y [1 1 1 1 1], :z [2 3 4 5 6]}))


  (is (= (note-to-test/represent-value
          (tc/dataset "data/family.csv"))
       {"family" [1 2 3 4 5],
        "dob_child1"
        [[java.time.LocalDate "1998-11-26"]
         [java.time.LocalDate "1996-06-22"]
         [java.time.LocalDate "2002-07-11"]
         [java.time.LocalDate "2004-10-10"]
         [java.time.LocalDate "2000-12-05"]],
        "dob_child2"
        [[java.time.LocalDate "2000-01-29"]
         :var-or-nil
         [java.time.LocalDate "2004-04-05"]
         [java.time.LocalDate "2009-08-27"]
         [java.time.LocalDate "2005-02-28"]],
        "gender_child1" [1 2 2 1 2],
        "gender_child2" [2 :var-or-nil 2 1 1]}))


  (is (= (note-to-test/represent-value
          (defonce ds (tc/dataset "https://vega.github.io/vega-lite/examples/data/seattle-weather.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          ds)
       {"date"
        [[java.time.LocalDate "2012-01-01"]
         [java.time.LocalDate "2012-01-02"]
         [java.time.LocalDate "2012-01-03"]
         [java.time.LocalDate "2012-01-04"]
         [java.time.LocalDate "2012-01-05"]
         [java.time.LocalDate "2012-01-06"]
         [java.time.LocalDate "2012-01-07"]
         [java.time.LocalDate "2012-01-08"]
         [java.time.LocalDate "2012-01-09"]
         [java.time.LocalDate "2012-01-10"]
         [java.time.LocalDate "2012-01-11"]
         [java.time.LocalDate "2012-01-12"]
         [java.time.LocalDate "2012-01-13"]
         [java.time.LocalDate "2012-01-14"]
         [java.time.LocalDate "2012-01-15"]
         [java.time.LocalDate "2012-01-16"]
         [java.time.LocalDate "2012-01-17"]
         [java.time.LocalDate "2012-01-18"]
         [java.time.LocalDate "2012-01-19"]
         [java.time.LocalDate "2012-01-20"]],
        "precipitation"
        [0.0
         10.9
         0.8
         20.3
         1.3
         2.5
         0.0
         0.0
         4.3
         1.0
         0.0
         0.0
         0.0
         4.1
         5.3
         2.5
         8.1
         19.8
         15.2
         13.5],
        "temp_max"
        [12.8
         10.6
         11.7
         12.2
         8.9
         4.4
         7.2
         10.0
         9.4
         6.1
         6.1
         6.1
         5.0
         4.4
         1.1
         1.7
         3.3
         0.0
         -1.1
         7.2],
        "temp_min"
        [5.0
         2.8
         7.2
         5.6
         2.8
         2.2
         2.8
         2.8
         5.0
         0.6
         -1.1
         -1.7
         -2.8
         0.6
         -3.3
         -2.8
         0.0
         -2.8
         -2.8
         -1.1],
        "wind"
        [4.7
         4.5
         2.3
         4.7
         6.1
         2.2
         2.3
         2.0
         3.4
         3.4
         5.1
         1.9
         1.3
         5.3
         3.2
         5.0
         5.6
         5.0
         1.6
         2.3],
        "weather"
        ["drizzle"
         "rain"
         "rain"
         "rain"
         "rain"
         "rain"
         "rain"
         "sun"
         "rain"
         "rain"
         "sun"
         "sun"
         "sun"
         "snow"
         "snow"
         "snow"
         "snow"
         "snow"
         "snow"
         "snow"]}))


  (is (= (note-to-test/represent-value
          (tc/dataset 999))
       {:$value [999],
        :$error ["Don't know how to create ISeq from: java.lang.Long"]}))


  (is (= (note-to-test/represent-value
          (tc/dataset 999 {:single-value-column-name "my-single-value"
                           :error-column? false}))
       {"my-single-value" [999]}))

  (is (= (note-to-test/represent-value
          (tc/dataset 999 {:single-value-column-name ""
                           :dataset-name "Single value"
                           :error-column? false}))
       {0 [999]}))


  (is (= (note-to-test/represent-value
          (tc/write! ds "output.tsv.gz"))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (.exists (clojure.java.io/file "output.tsv.gz")))
       true))


  (is (= (note-to-test/represent-value
          (tc/write! DS "output.nippy.gz"))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (tc/dataset "output.nippy.gz"))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/row-count ds))
       1461))


  (is (= (note-to-test/represent-value
          (tc/column-count ds))
       6))


  (is (= (note-to-test/represent-value
          (tc/shape ds))
       [1461 6]))


  (is (= (note-to-test/represent-value
          (tc/info ds))
       {:min
        [[java.time.LocalDate "2012-01-01"] 0.0 -1.6 -7.1 0.4 :var-or-nil],
        :n-missing [0 0 0 0 0 0],
        :col-name
        ["date" "precipitation" "temp_max" "temp_min" "wind" "weather"],
        :mean
        [[java.time.LocalDate "2013-12-31"]
         3.02943189596167
         16.43908281998631
         8.234770704996578
         3.24113620807666
         :var-or-nil],
        :datatype
        [:packed-local-date :float64 :float64 :float64 :float64 :string],
        :skew
        [1.989714182664766E-17
         3.5056437169988737
         0.2809299923916157
         -0.24945855161317937
         0.8916675191285054
         :var-or-nil],
        :mode
        [:var-or-nil :var-or-nil :var-or-nil :var-or-nil :var-or-nil "rain"],
        :standard-deviation
        [3.645204634255807E10
         6.6801943223147315
         7.34975809736018
         5.02300417996126
         1.4378250588746238
         :var-or-nil],
        :n-valid [1461 1461 1461 1461 1461 1461],
        :max
        [[java.time.LocalDate "2015-12-31"] 55.9 35.6 18.3 9.5 :var-or-nil],
        :first
        [[java.time.LocalDate "2012-01-01"] 0.0 12.8 5.0 4.7 "drizzle"],
        :last [[java.time.LocalDate "2015-12-31"] 0.0 5.6 -2.1 3.5 "sun"]}))

  (is (= (note-to-test/represent-value
          (tc/info ds :basic))
       {:name
        ["https://vega.github.io/vega-lite/examples/data/seattle-weather.csv"],
        :grouped? [false],
        :rows [1461],
        :columns [6]}))

  (is (= (note-to-test/represent-value
          (tc/info ds :columns))
       {:name ["date" "precipitation" "temp_max" "temp_min" "wind" "weather"],
        :datatype
        [:packed-local-date :float64 :float64 :float64 :float64 :string],
        :n-elems [1461 1461 1461 1461 1461 1461],
        :categorical?
        [:var-or-nil :var-or-nil :var-or-nil :var-or-nil :var-or-nil true]}))


  (is (= (note-to-test/represent-value
          (tc/dataset-name ds))
       "https://vega.github.io/vega-lite/examples/data/seattle-weather.csv"))


  (is (= (note-to-test/represent-value
          (->> "seattle-weather"
               (tc/set-dataset-name ds)
               (tc/dataset-name)))
       "seattle-weather"))


  (is (= (note-to-test/represent-value
          (ds "wind"))
       [4.7
        4.5
        2.3
        4.7
        6.1
        2.2
        2.3
        2.0
        3.4
        3.4
        5.1
        1.9
        1.3
        5.3
        3.2
        5.0
        5.6
        5.0
        1.6
        2.3]))

  (is (= (note-to-test/represent-value
          (tc/column ds "date"))
       [[java.time.LocalDate "2012-01-01"]
        [java.time.LocalDate "2012-01-02"]
        [java.time.LocalDate "2012-01-03"]
        [java.time.LocalDate "2012-01-04"]
        [java.time.LocalDate "2012-01-05"]
        [java.time.LocalDate "2012-01-06"]
        [java.time.LocalDate "2012-01-07"]
        [java.time.LocalDate "2012-01-08"]
        [java.time.LocalDate "2012-01-09"]
        [java.time.LocalDate "2012-01-10"]
        [java.time.LocalDate "2012-01-11"]
        [java.time.LocalDate "2012-01-12"]
        [java.time.LocalDate "2012-01-13"]
        [java.time.LocalDate "2012-01-14"]
        [java.time.LocalDate "2012-01-15"]
        [java.time.LocalDate "2012-01-16"]
        [java.time.LocalDate "2012-01-17"]
        [java.time.LocalDate "2012-01-18"]
        [java.time.LocalDate "2012-01-19"]
        [java.time.LocalDate "2012-01-20"]]))


  (is (= (note-to-test/represent-value
          (take 2 (tc/columns ds)))
       [[[java.time.LocalDate "2012-01-01"]
         [java.time.LocalDate "2012-01-02"]
         [java.time.LocalDate "2012-01-03"]
         [java.time.LocalDate "2012-01-04"]
         [java.time.LocalDate "2012-01-05"]
         [java.time.LocalDate "2012-01-06"]
         [java.time.LocalDate "2012-01-07"]
         [java.time.LocalDate "2012-01-08"]
         [java.time.LocalDate "2012-01-09"]
         [java.time.LocalDate "2012-01-10"]
         [java.time.LocalDate "2012-01-11"]
         [java.time.LocalDate "2012-01-12"]
         [java.time.LocalDate "2012-01-13"]
         [java.time.LocalDate "2012-01-14"]
         [java.time.LocalDate "2012-01-15"]
         [java.time.LocalDate "2012-01-16"]
         [java.time.LocalDate "2012-01-17"]
         [java.time.LocalDate "2012-01-18"]
         [java.time.LocalDate "2012-01-19"]
         [java.time.LocalDate "2012-01-20"]]
        [0.0
         10.9
         0.8
         20.3
         1.3
         2.5
         0.0
         0.0
         4.3
         1.0
         0.0
         0.0
         0.0
         4.1
         5.3
         2.5
         8.1
         19.8
         15.2
         13.5]]))


  (is (= (note-to-test/represent-value
          (keys (tc/columns ds :as-map)))
       ["date" "precipitation" "temp_max" "temp_min" "wind" "weather"]))


  (is (= (note-to-test/represent-value
          (take 2 (tc/rows ds)))
       [[[java.time.LocalDate "2012-01-01"] 0.0 12.8 5.0 4.7 "drizzle"]
        [[java.time.LocalDate "2012-01-02"] 10.9 10.6 2.8 4.5 "rain"]]))


  (is (= (note-to-test/represent-value
          (-> ds
              (tc/select-columns :type/numerical)
              (tc/head)
              (tc/rows :as-double-arrays)))
       [[0.0 12.8 5.0 4.7]
        [10.9 10.6 2.8 4.5]
        [0.8 11.7 7.2 2.3]
        [20.3 12.2 5.6 4.7]
        [1.3 8.9 2.8 6.1]]))

  (is (= (note-to-test/represent-value
          (-> ds
              (tc/select-columns :type/numerical)
              (tc/head)
              (tc/columns :as-double-arrays)))
       [[0.0 10.9 0.8 20.3 1.3]
        [12.8 10.6 11.7 12.2 8.9]
        [5.0 2.8 7.2 5.6 2.8]
        [4.7 4.5 2.3 4.7 6.1]]))


  (is (= (note-to-test/represent-value
          (clojure.pprint/pprint (take 2 (tc/rows ds :as-maps))))
       :var-or-nil))


  (is (= (note-to-test/represent-value
          (get-in ds ["wind" 2]))
       2.3))

  (is (= (note-to-test/represent-value
          (tc/get-entry ds "wind" 2))
       2.3))


  (is (= (note-to-test/represent-value
          (tc/print-dataset (tc/group-by DS :V1) {:print-line-policy :markdown}))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (tc/print-dataset (tc/group-by DS :V1) {:print-line-policy :repl}))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (tc/print-dataset (tc/group-by DS :V1) {:print-line-policy :single}))
       :var-or-nil))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/column-names)))
       [:V1 :V2 :V3 :V4]))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/as-regular-dataset)
              (tc/column-names)))
       [:name :group-id :data]))


  (is (= (note-to-test/represent-value
          (tc/columns (tc/group-by DS :V1) :as-map))
       {:name [1 2],
        :group-id [0 1],
        :data
        [{:V1 [1 1 1 1 1],
          :V2 [1 3 5 7 9],
          :V3 [0.5 1.5 1.0 0.5 1.5],
          :V4 ["A" "C" "B" "A" "C"]}
         {:V1 [2 2 2 2],
          :V2 [2 4 6 8],
          :V3 [1.0 0.5 1.5 1.0],
          :V4 ["B" "A" "C" "B"]}]}))


  (is (= (note-to-test/represent-value
          (keys (tc/group-by DS :V1 {:result-type :as-map})))
       [1 2]))

  (is (= (note-to-test/represent-value
          (vals (tc/group-by DS :V1 {:result-type :as-map})))
       [{:V1 [1 1 1 1 1],
         :V2 [1 3 5 7 9],
         :V3 [0.5 1.5 1.0 0.5 1.5],
         :V4 ["A" "C" "B" "A" "C"]}
        {:V1 [2 2 2 2],
         :V2 [2 4 6 8],
         :V3 [1.0 0.5 1.5 1.0],
         :V4 ["B" "A" "C" "B"]}]))


  (is (= (note-to-test/represent-value
          (tc/group-by DS :V1 {:result-type :as-indexes}))
       {1 [0 2 4 6 8], 2 [1 3 5 7]}))


  (is (= (note-to-test/represent-value
          (tc/group-by DS :V1))
       {:name [1 2],
        :group-id [0 1],
        :data
        [{:V1 [1 1 1 1 1],
          :V2 [1 3 5 7 9],
          :V3 [0.5 1.5 1.0 0.5 1.5],
          :V4 ["A" "C" "B" "A" "C"]}
         {:V1 [2 2 2 2],
          :V2 [2 4 6 8],
          :V3 [1.0 0.5 1.5 1.0],
          :V4 ["B" "A" "C" "B"]}]}))


  (is (= (note-to-test/represent-value
          (let [ds (-> {"a" [1 1 2 2]
                        "b" ["a" "b" "c" "d"]}
                       (tc/dataset)
                       (tc/group-by "a"))]
            (seq (ds :data))))
       [{"a" [1 1], "b" ["a" "b"]} {"a" [2 2], "b" ["c" "d"]}]))

  (is (= (note-to-test/represent-value
          (-> {"a" [1 1 2 2]
               "b" ["a" "b" "c" "d"]}
              (tc/dataset)
              (tc/group-by "a")
              (tc/groups->seq)))
       [{"a" [1 1], "b" ["a" "b"]} {"a" [2 2], "b" ["c" "d"]}]))


  (is (= (note-to-test/represent-value
          (-> {"a" [1 1 2 2]
               "b" ["a" "b" "c" "d"]}
              (tc/dataset)
              (tc/group-by "a")
              (tc/groups->map)))
       {1 {"a" [1 1], "b" ["a" "b"]}, 2 {"a" [2 2], "b" ["c" "d"]}}))


  (is (= (note-to-test/represent-value
          (tc/group-by DS [:V1 :V3] {:result-type :as-seq}))
       [{:V1 [1 1], :V2 [1 7], :V3 [0.5 0.5], :V4 ["A" "A"]}
        {:V1 [2 2], :V2 [2 8], :V3 [1.0 1.0], :V4 ["B" "B"]}
        {:V1 [1 1], :V2 [3 9], :V3 [1.5 1.5], :V4 ["C" "C"]}
        {:V1 [2], :V2 [4], :V3 [0.5], :V4 ["A"]}
        {:V1 [1], :V2 [5], :V3 [1.0], :V4 ["B"]}
        {:V1 [2], :V2 [6], :V3 [1.5], :V4 ["C"]}]))


  (is (= (note-to-test/represent-value
          (tc/group-by DS {"group-a" [1 2 1 2]
                            "group-b" [5 5 5 1]} {:result-type :as-seq}))
       [{:V1 [2 1 2 1],
         :V2 [2 3 2 3],
         :V3 [1.0 1.5 1.0 1.5],
         :V4 ["B" "C" "B" "C"]}
        {:V1 [2 2 2 2],
         :V2 [6 6 6 2],
         :V3 [1.5 1.5 1.5 1.0],
         :V4 ["C" "C" "C" "B"]}]))


  (is (= (note-to-test/represent-value
          (tc/group-by DS (fn [row] (* (:V1 row)
                                       (:V3 row))) {:result-type :as-seq}))
       [{:V1 [1 1], :V2 [1 7], :V3 [0.5 0.5], :V4 ["A" "A"]}
        {:V1 [2 2], :V2 [2 8], :V3 [1.0 1.0], :V4 ["B" "B"]}
        {:V1 [1 1], :V2 [3 9], :V3 [1.5 1.5], :V4 ["C" "C"]}
        {:V1 [2 1], :V2 [4 5], :V3 [0.5 1.0], :V4 ["A" "B"]}
        {:V1 [2], :V2 [6], :V3 [1.5], :V4 ["C"]}]))


  (is (= (note-to-test/represent-value
          (tc/group-by DS (comp #(< % 1.0) :V3) {:result-type :as-seq}))
       [{:V1 [1 2 1], :V2 [1 4 7], :V3 [0.5 0.5 0.5], :V4 ["A" "A" "A"]}
        {:V1 [2 1 1 2 2 1],
         :V2 [2 3 5 6 8 9],
         :V3 [1.0 1.5 1.0 1.5 1.0 1.5],
         :V4 ["B" "C" "B" "C" "B" "C"]}]))


  (is (= (note-to-test/represent-value
          (tc/group-by DS (juxt :V1 :V3) {:result-type :as-seq}))
       [{:V1 [1 1], :V2 [1 7], :V3 [0.5 0.5], :V4 ["A" "A"]}
        {:V1 [2 2], :V2 [2 8], :V3 [1.0 1.0], :V4 ["B" "B"]}
        {:V1 [1 1], :V2 [3 9], :V3 [1.5 1.5], :V4 ["C" "C"]}
        {:V1 [2], :V2 [4], :V3 [0.5], :V4 ["A"]}
        {:V1 [1], :V2 [5], :V3 [1.0], :V4 ["B"]}
        {:V1 [2], :V2 [6], :V3 [1.5], :V4 ["C"]}]))


  (is (= (note-to-test/represent-value
          (tc/group-by DS identity {:result-type :as-seq
                                     :select-keys [:V1]}))
       [{:V1 [1 1 1 1 1],
         :V2 [1 3 5 7 9],
         :V3 [0.5 1.5 1.0 0.5 1.5],
         :V4 ["A" "C" "B" "A" "C"]}
        {:V1 [2 2 2 2],
         :V2 [2 4 6 8],
         :V3 [1.0 0.5 1.5 1.0],
         :V4 ["B" "A" "C" "B"]}]))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V3)
              (tc/ungroup)))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :V3 [0.5 0.5 0.5 1.0 1.0 1.0 1.5 1.5 1.5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V3)
              (tc/ungroup {:order? true
                            :dataset-name "Ordered by V3"})))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :V3 [0.5 0.5 0.5 1.0 1.0 1.0 1.5 1.5 1.5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V3)
              (tc/ungroup {:order? :desc
                            :dataset-name "Ordered by V3 descending"})))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [3 6 9 2 5 8 1 4 7],
        :V3 [1.5 1.5 1.5 1.0 1.0 1.0 0.5 0.5 0.5],
        :V4 ["C" "C" "C" "B" "B" "B" "A" "A" "A"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (comp #(< % 4) :V2))
              (tc/ungroup {:add-group-as-column true
                            :add-group-id-as-column true})))
       {:$group-name [true true true false false false false false false],
        :$group-id [0 0 0 1 1 1 1 1 1],
        :V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (comp #(< % 4) :V2))
              (tc/ungroup {:add-group-as-column "Is V2 less than 4?"
                            :add-group-id-as-column "group id"})))
       {"Is V2 less than 4?"
        [true true true false false false false false false],
        "group id" [0 0 0 1 1 1 1 1 1],
        :V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (fn [row] {"V1 and V3 multiplied" (* (:V1 row)
                                                                (:V3 row))
                                      "V4 as lowercase" (clojure.string/lower-case (:V4 row))}))
              (tc/ungroup {:add-group-as-column true})))
       {"V1 and V3 multiplied" [0.5 0.5 2.0 2.0 1.5 1.5 1.0 1.0 3.0],
        "V4 as lowercase" ["a" "a" "b" "b" "c" "c" "a" "b" "c"],
        :V1 [1 1 2 2 1 1 2 1 2],
        :V2 [1 7 2 8 3 9 4 5 6],
        :V3 [0.5 0.5 1.0 1.0 1.5 1.5 0.5 1.0 1.5],
        :V4 ["A" "A" "B" "B" "C" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (fn [row] {"V1 and V3 multiplied" (* (:V1 row)
                                                                (:V3 row))
                                      "V4 as lowercase" (clojure.string/lower-case (:V4 row))}))
              (tc/ungroup {:add-group-as-column "just map"
                            :separate? false})))
       {"just map"
        [{"V1 and V3 multiplied" 0.5, "V4 as lowercase" "a"}
         {"V1 and V3 multiplied" 0.5, "V4 as lowercase" "a"}
         {"V1 and V3 multiplied" 2.0, "V4 as lowercase" "b"}
         {"V1 and V3 multiplied" 2.0, "V4 as lowercase" "b"}
         {"V1 and V3 multiplied" 1.5, "V4 as lowercase" "c"}
         {"V1 and V3 multiplied" 1.5, "V4 as lowercase" "c"}
         {"V1 and V3 multiplied" 1.0, "V4 as lowercase" "a"}
         {"V1 and V3 multiplied" 1.0, "V4 as lowercase" "b"}
         {"V1 and V3 multiplied" 3.0, "V4 as lowercase" "c"}],
        :V1 [1 1 2 2 1 1 2 1 2],
        :V2 [1 7 2 8 3 9 4 5 6],
        :V3 [0.5 0.5 1.0 1.0 1.5 1.5 0.5 1.0 1.5],
        :V4 ["A" "A" "B" "B" "C" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (juxt :V1 :V3))
              (tc/ungroup {:add-group-as-column "abc"})))
       {:abc-0 [1 1 2 2 1 1 2 1 2],
        :abc-1 [0.5 0.5 1.0 1.0 1.5 1.5 0.5 1.0 1.5],
        :V1 [1 1 2 2 1 1 2 1 2],
        :V2 [1 7 2 8 3 9 4 5 6],
        :V3 [0.5 0.5 1.0 1.0 1.5 1.5 0.5 1.0 1.5],
        :V4 ["A" "A" "B" "B" "C" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (juxt :V1 :V3))
              (tc/ungroup {:add-group-as-column ["v1" "v3"]})))
       {"v1" [1 1 2 2 1 1 2 1 2],
        "v3" [0.5 0.5 1.0 1.0 1.5 1.5 0.5 1.0 1.5],
        :V1 [1 1 2 2 1 1 2 1 2],
        :V2 [1 7 2 8 3 9 4 5 6],
        :V3 [0.5 0.5 1.0 1.0 1.5 1.5 0.5 1.0 1.5],
        :V4 ["A" "A" "B" "B" "C" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (juxt :V1 :V3))
              (tc/ungroup {:separate? false
                            :add-group-as-column true})))
       {:$group-name
        [[1 0.5]
         [1 0.5]
         [2 1.0]
         [2 1.0]
         [1 1.5]
         [1 1.5]
         [2 0.5]
         [1 1.0]
         [2 1.5]],
        :V1 [1 1 2 2 1 1 2 1 2],
        :V2 [1 7 2 8 3 9 4 5 6],
        :V3 [0.5 0.5 1.0 1.0 1.5 1.5 0.5 1.0 1.5],
        :V4 ["A" "A" "B" "B" "C" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/grouped? DS))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (tc/grouped? (tc/group-by DS :V1)))
       true))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/as-regular-dataset)
              (tc/grouped?)))
       :var-or-nil))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4 :V1])
              (tc/without-grouping->
               (tc/order-by (comp (juxt :V4 :V1) :name)))))
       {:name
        [{:V4 "A", :V1 1}
         {:V4 "A", :V1 2}
         {:V4 "B", :V1 1}
         {:V4 "B", :V1 2}
         {:V4 "C", :V1 1}
         {:V4 "C", :V1 2}],
        :group-id [0 3 4 1 2 5],
        :data
        [{:V1 [1 1], :V2 [1 7], :V3 [0.5 0.5], :V4 ["A" "A"]}
         {:V1 [2], :V2 [4], :V3 [0.5], :V4 ["A"]}
         {:V1 [1], :V2 [5], :V3 [1.0], :V4 ["B"]}
         {:V1 [2 2], :V2 [2 8], :V3 [1.0 1.0], :V4 ["B" "B"]}
         {:V1 [1 1], :V2 [3 9], :V3 [1.5 1.5], :V4 ["C" "C"]}
         {:V1 [2], :V2 [6], :V3 [1.5], :V4 ["C"]}]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/process-group-data #(str "Shape: " (vector (tc/row-count %) (tc/column-count %))))
              (tc/as-regular-dataset)))
       {:name [1 2], :group-id [0 1], :data ["Shape: [5 4]" "Shape: [4 4]"]}))


  (is (= (note-to-test/represent-value
          (tc/column-names DS))
       [:V1 :V2 :V3 :V4]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS :all))
       [:V1 :V2 :V3 :V4]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS [:all]))
       []))


  (is (= (note-to-test/represent-value
          (tc/column-names DS :V1))
       [:V1]))

  (is (= (note-to-test/represent-value
          (tc/column-names DS "no such column"))
       []))


  (is (= (note-to-test/represent-value
          (tc/column-names DS [:V1 "V2" :V3 :V4 :V5]))
       [:V1 :V3 :V4]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS #".*[14]"))
       [:V1 :V4]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS #"^:int.*" :datatype))
       [:V1 :V2]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS :type/integer))
       [:V1 :V2]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS #{:float64} :datatype))
       [:V3]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS :type/float64))
       [:V3]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS (complement #{:V1})))
       [:V2 :V3 :V4]))

  (is (= (note-to-test/represent-value
          (tc/column-names DS (complement #{:float64}) :datatype))
       [:V1 :V2 :V4]))

  (is (= (note-to-test/represent-value
          (tc/column-names DS :!type/float64))
       [:V1 :V2 :V4]))


  (is (= (note-to-test/represent-value
          (tc/column-names DS (fn [meta]
                                 (and (= :int64 (:datatype meta))
                                      (clojure.string/ends-with? (:name meta) "1"))) :all))
       [:V1]))


  (is (= (note-to-test/represent-value
          (tc/select-columns DS #(= :float64 %) :datatype))
       {:V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5]}))


  (is (= (note-to-test/represent-value
          (tc/select-columns DS :type/float64))
       {:V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5]}))


  (is (= (note-to-test/represent-value
          (tc/select-columns DS (complement #{:V1})))
       {:V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/select-columns [:V2 :V3])
              (tc/groups->map)))
       {1 {:V2 [1 3 5 7 9], :V3 [0.5 1.5 1.0 0.5 1.5]},
        2 {:V2 [2 4 6 8], :V3 [1.0 0.5 1.5 1.0]}}))


  (is (= (note-to-test/represent-value
          (tc/drop-columns DS #(= :float64 %) :datatype))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/drop-columns DS :type/float64))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/drop-columns DS (complement #{:V1 :V2})))
       {:V1 [1 2 1 2 1 2 1 2 1], :V2 [1 2 3 4 5 6 7 8 9]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/drop-columns [:V2 :V3])
              (tc/groups->map)))
       {1 {:V1 [1 1 1 1 1], :V4 ["A" "C" "B" "A" "C"]},
        2 {:V1 [2 2 2 2], :V4 ["B" "A" "C" "B"]}}))


  (is (= (note-to-test/represent-value
          (tc/rename-columns DS {:V1 "v1"
                                  :V2 "v2"
                                  :V3 [1 2 3]
                                  :V4 (Object.)}))
       {"v1" [1 2 1 2 1 2 1 2 1],
        "v2" [1 2 3 4 5 6 7 8 9],
        [1 2 3] [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        java.lang.Object ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/rename-columns DS (comp str second name)))
       {"1" [1 2 1 2 1 2 1 2 1],
        "2" [1 2 3 4 5 6 7 8 9],
        "3" [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        "4" ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/rename-columns DS [:V1 :V3] (comp str second name)))
       {"1" [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        "3" [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/rename-columns {:V1 "v1"
                                   :V2 "v2"
                                   :V3 [1 2 3]
                                   :V4 (Object.)})
              (tc/groups->map)))
       {1
        {"v1" [1 1 1 1 1],
         "v2" [1 3 5 7 9],
         [1 2 3] [0.5 1.5 1.0 0.5 1.5],
         java.lang.Object ["A" "C" "B" "A" "C"]},
        2
        {"v1" [2 2 2 2],
         "v2" [2 4 6 8],
         [1 2 3] [1.0 0.5 1.5 1.0],
         java.lang.Object ["B" "A" "C" "B"]}}))


  (is (= (note-to-test/represent-value
          (tc/add-column DS :V5 "X"))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V5 ["X" "X" "X" "X" "X" "X" "X" "X" "X"]}))




  (is (= (note-to-test/represent-value
          (tc/add-column DS :V5 (DS :V1)))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V5 [1 2 1 2 1 2 1 2 1]}))


  (is (= (note-to-test/represent-value
          (tc/add-column DS :row-count tc/row-count))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :row-count [9 9 9 9 9 9 9 9 9]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/add-column :row-count tc/row-count)
              (tc/ungroup)))
       {:V1 [1 1 1 1 1 2 2 2 2],
        :V2 [1 3 5 7 9 2 4 6 8],
        :V3 [0.5 1.5 1.0 0.5 1.5 1.0 0.5 1.5 1.0],
        :V4 ["A" "C" "B" "A" "C" "B" "A" "C" "B"],
        :row-count [5 5 5 5 5 4 4 4 4]}))


  (is (= (note-to-test/represent-value
          (tc/add-column DS :V5 [:r :b] :cycle))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V5 [:r :b :r :b :r :b :r :b :r]}))

  (is (= (note-to-test/represent-value
          (tc/add-column DS :V5 [:r :b] :na))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V5
        [:r
         :b
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (try
            (tc/add-column DS :V5 [:r :b])
            (catch Exception e (str "Exception caught: "(ex-message e)))))
       "Exception caught: Column size (2) should be exactly the same as dataset row count (9). Consider `:cycle` or `:na` strategy."))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V3)
              (tc/add-column :V5 [:r :b] :na)
              (tc/ungroup)))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :V3 [0.5 0.5 0.5 1.0 1.0 1.0 1.5 1.5 1.5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V5 [:r :b :var-or-nil :r :b :var-or-nil :r :b :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V3)
              (tc/add-column :V5 (DS :V2) :cycle)
              (tc/ungroup)))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :V3 [0.5 0.5 0.5 1.0 1.0 1.0 1.5 1.5 1.5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V5 [1 2 3 1 2 3 1 2 3]}))


  (is (= (note-to-test/represent-value
          (tc/add-columns DS {:V1 #(map inc (% :V1))
                                         :V5 #(map (comp keyword str) (% :V4))
                                         :V6 11}))
       {:V1 [2 3 2 3 2 3 2 3 2],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V5 [:A :B :C :A :B :C :A :B :C],
        :V6 [11 11 11 11 11 11 11 11 11]}))


  (is (= (note-to-test/represent-value
          (tc/update-columns DS :all reverse))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [9 8 7 6 5 4 3 2 1],
        :V3 [1.5 1.0 0.5 1.5 1.0 0.5 1.5 1.0 0.5],
        :V4 ["C" "B" "A" "C" "B" "A" "C" "B" "A"]}))


  (is (= (note-to-test/represent-value
          (tc/update-columns DS :type/numerical [(partial map dec)
                                                  (partial map inc)]))
       {:V1 [0 1 0 1 0 1 0 1 0],
        :V2 [2 3 4 5 6 7 8 9 10],
        :V3 [-0.5 0.0 0.5 -0.5 0.0 0.5 -0.5 0.0 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))




  (is (= (note-to-test/represent-value
          (tc/map-columns DS
                           :sum-of-numbers
                           (tc/column-names DS  #{:int64 :float64} :datatype)
                           (fn [& rows]
                             (reduce + rows))))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :sum-of-numbers [2.5 5.0 5.5 6.5 7.0 9.5 8.5 11.0 11.5]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/map-columns :sum-of-numbers
                               (tc/column-names DS  #{:int64 :float64} :datatype)
                               (fn [& rows]
                                 (reduce + rows)))
              (tc/ungroup)))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :V3 [0.5 0.5 0.5 1.0 1.0 1.0 1.5 1.5 1.5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :sum-of-numbers [2.5 6.5 8.5 5.0 7.0 11.0 5.5 9.5 11.5]}))


  (is (= (note-to-test/represent-value
          (tc/reorder-columns DS :V4 [:V3 :V2]))
       {:V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V1 [1 2 1 2 1 2 1 2 1]}))


  (is (= (note-to-test/represent-value
          (tc/reorder-columns DS (tc/column-names DS (complement #{:int64}) :datatype)))
       {:V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/convert-types :V1 :float64)
              (tc/info :columns)))
       {:name [:V1 :V2 :V3 :V4],
        :datatype [:float64 :int64 :float64 :string],
        :n-elems [9 9 9 9],
        :unparsed-indexes
        [:org.roaringbitmap.RoaringBitmap
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :unparsed-data [[] :var-or-nil :var-or-nil :var-or-nil],
        :categorical? [:var-or-nil :var-or-nil :var-or-nil true]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/convert-types :V4 [[:int16 #(Integer/parseInt % 16)]])))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 [10 11 12 10 11 12 10 11 12]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/convert-types {:V1 :float64
                                  :V2 :object
                                  :V3 [:boolean #(< % 1.0)]
                                  :V4 :object})
              (tc/info :columns)))
       {:name [:V1 :V2 :V3 :V4],
        :datatype [:float64 :object :boolean :object],
        :n-elems [9 9 9 9],
        :unparsed-indexes
        [:org.roaringbitmap.RoaringBitmap
         :org.roaringbitmap.RoaringBitmap
         :org.roaringbitmap.RoaringBitmap
         :var-or-nil],
        :unparsed-data [[] [] [] :var-or-nil],
        :categorical? [:var-or-nil true :var-or-nil true]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/convert-types :type/numerical :int16)
              (tc/info :columns)))
       {:name [:V1 :V2 :V3 :V4],
        :datatype [:int16 :int16 :int16 :string],
        :n-elems [9 9 9 9],
        :unparsed-indexes
        [:org.roaringbitmap.RoaringBitmap
         :org.roaringbitmap.RoaringBitmap
         :org.roaringbitmap.RoaringBitmap
         :var-or-nil],
        :unparsed-data [[] [] [] :var-or-nil],
        :categorical? [:var-or-nil :var-or-nil :var-or-nil true]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/convert-types :V1 :float32)
              (tc/ungroup)
              (tc/info :columns)))
       {:name [:V1 :V2 :V3 :V4],
        :datatype [:float32 :int64 :float64 :string],
        :n-elems [9 9 9 9],
        :unparsed-indexes
        [:org.roaringbitmap.RoaringBitmap
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :unparsed-data [[] :var-or-nil :var-or-nil :var-or-nil],
        :categorical? [:var-or-nil :var-or-nil :var-or-nil true]}))


  (is (= (note-to-test/represent-value
          (tc/->array DS :V1))
       [1 2 1 2 1 2 1 2 1]))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V3)
              (tc/->array :V2)))
       [[1 4 7] [2 5 8] [3 6 9]]))


  (is (= (note-to-test/represent-value
          (tc/->array DS :V4 :string))
       ["A" "B" "C" "A" "B" "C" "A" "B" "C"]))

  (is (= (note-to-test/represent-value
          (tc/->array DS :V1 :float32))
       [1.0 2.0 1.0 2.0 1.0 2.0 1.0 2.0 1.0]))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS 4))
       {:V1 [1], :V2 [5], :V3 [1.0], :V4 ["B"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS [1 4 5]))
       {:V1 [2 1 2], :V2 [2 5 6], :V3 [1.0 1.0 1.5], :V4 ["B" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS [true nil nil true]))
       {:V1 [1 2], :V2 [1 4], :V3 [0.5 0.5], :V4 ["A" "A"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #(< % 1) :V3)))
       {:V1 [1 2 1], :V2 [1 4 7], :V3 [0.5 0.5 0.5], :V4 ["A" "A" "A"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/select-rows 0)
              (tc/ungroup)))
       {:V1 [1 2], :V2 [1 2], :V3 [0.5 1.0], :V4 ["A" "B"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/select-rows (fn [row] (<= (:V2 row) (:mean row)))
                               {:pre {:mean #(tech.v3.datatype.functional/mean (% :V2))}})
              (tc/ungroup)))
       {:V1 [1 2 2 1 1 2],
        :V2 [1 4 2 5 3 6],
        :V3 [0.5 0.5 1.0 1.0 1.5 1.5],
        :V4 ["A" "A" "B" "B" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/drop-rows (fn [row] (<= (:V2 row) (:mean row)))
                             {:pre {:mean #(tech.v3.datatype.functional/mean (% :V2))}})
              (tc/ungroup)))
       {:V1 [1 2 1], :V2 [7 8 9], :V3 [0.5 1.0 1.5], :V4 ["A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/map-rows DS (fn [{:keys [V1 V2]}] {:V1 0
                                                 :V5 (/ (+ V1 V2) (double V2))})))
       {:V1 [0 0 0 0 0 0 0 0 0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V5
        [2.0
         2.0
         1.3333333333333333
         1.5
         1.2
         1.3333333333333333
         1.1428571428571428
         1.25
         1.1111111111111112]}))


  (is (= (note-to-test/represent-value
          (tc/first DS))
       {:V1 [1], :V2 [1], :V3 [0.5], :V4 ["A"]}))


  (is (= (note-to-test/represent-value
          (tc/last DS))
       {:V1 [1], :V2 [9], :V3 [1.5], :V4 ["C"]}))




  (is (= (note-to-test/represent-value
          (tc/rand-nth DS {:seed 42}))
       {:V1 [2], :V2 [6], :V3 [1.5], :V4 ["C"]}))








  (is (= (note-to-test/represent-value
          (tc/random DS 5 {:seed 42}))
       {:V1 [2 1 1 1 1],
        :V2 [6 5 3 1 9],
        :V3 [1.5 1.0 1.5 0.5 1.5],
        :V4 ["C" "B" "C" "A" "C"]}))




  (is (= (note-to-test/represent-value
          (tc/shuffle DS {:seed 42}))
       {:V1 [1 2 2 2 2 1 1 1 1],
        :V2 [5 2 6 4 8 3 7 1 9],
        :V3 [1.0 1.0 1.5 0.5 1.0 1.5 0.5 0.5 1.5],
        :V4 ["B" "B" "C" "A" "B" "C" "A" "A" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/head DS))
       {:V1 [1 2 1 2 1],
        :V2 [1 2 3 4 5],
        :V3 [0.5 1.0 1.5 0.5 1.0],
        :V4 ["A" "B" "C" "A" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/tail DS))
       {:V1 [1 2 1 2 1],
        :V2 [5 6 7 8 9],
        :V3 [1.0 1.5 0.5 1.0 1.5],
        :V4 ["B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/by-rank DS :V3 zero?))
       {:V1 [1 2 1], :V2 [3 6 9], :V3 [1.5 1.5 1.5], :V4 ["C" "C" "C"]}))

  (is (= (note-to-test/represent-value
          (tc/by-rank DS :V3 zero? {:desc? false}))
       {:V1 [1 2 1], :V2 [1 4 7], :V3 [0.5 0.5 0.5], :V4 ["A" "A" "A"]}))


  (is (= (note-to-test/represent-value
          (tc/by-rank DS [:V1 :V3] zero? {:desc? false}))
       {:V1 [1 1], :V2 [1 7], :V3 [0.5 0.5], :V4 ["A" "A"]}))




  (is (= (note-to-test/represent-value
          (tc/aggregate DS #(reduce + (% :V2))))
       {"summary" [45]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate DS {:sum-of-V2 #(reduce + (% :V2))}))
       {:sum-of-V2 [45]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate DS #(take 5(% :V2))))
       {:summary-0 [1],
        :summary-1 [2],
        :summary-2 [3],
        :summary-3 [4],
        :summary-4 [5]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate DS [#(take 3 (% :V2))
                             (fn [ds] {:sum-v1 (reduce + (ds :V1))
                                      :prod-v3 (reduce * (ds :V3))})] {:default-column-name-prefix "V2-value"}))
       {:V2-value-0-0 [1],
        :V2-value-0-1 [2],
        :V2-value-0-2 [3],
        :V2-value-1-sum-v1 [13],
        :V2-value-1-prod-v3 [0.421875]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate [#(take 3 (% :V2))
                              (fn [ds] {:sum-v1 (reduce + (ds :V1))
                                       :prod-v3 (reduce * (ds :V3))})] {:default-column-name-prefix "V2-value"})))
       {:V4 ["A" "B" "C"],
        :V2-value-0-0 [1 2 3],
        :V2-value-0-1 [4 5 6],
        :V2-value-0-2 [7 8 9],
        :V2-value-1-sum-v1 [4 5 4],
        :V2-value-1-prod-v3 [0.125 1.0 3.375]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V3])
              (tc/aggregate [#(take 3 (% :V2))
                              (fn [ds] {:sum-v1 (reduce + (ds :V1))
                                       :prod-v3 (reduce * (ds :V3))})] {:default-column-name-prefix "V2-value"
                                                                        :ungroup? false})))
       {:name [{:V3 0.5} {:V3 1.0} {:V3 1.5}],
        :group-id [0 1 2],
        :data
        [{:V2-value-0-0 [1],
          :V2-value-0-1 [4],
          :V2-value-0-2 [7],
          :V2-value-1-sum-v1 [4],
          :V2-value-1-prod-v3 [0.125]}
         {:V2-value-0-0 [2],
          :V2-value-0-1 [5],
          :V2-value-0-2 [8],
          :V2-value-1-sum-v1 [5],
          :V2-value-1-prod-v3 [1.0]}
         {:V2-value-0-0 [3],
          :V2-value-0-1 [6],
          :V2-value-0-2 [9],
          :V2-value-1-sum-v1 [4],
          :V2-value-1-prod-v3 [3.375]}]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate-columns DS [:V1 :V2 :V3] #(reduce + %)))
       {:V1 [13], :V2 [45], :V3 [9.0]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate-columns DS [:V1 :V2 :V3] [#(reduce + %)
                                                   #(reduce max %)
                                                   #(reduce * %)]))
       {:V1 [13], :V2 [9], :V3 [0.421875]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate-columns [:V1 :V2 :V3] #(reduce + %))))
       {:V4 ["A" "B" "C"], :V1 [4 5 4], :V2 [12 15 18], :V3 [1.5 3.0 4.5]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/drop-columns :V4)
              (tc/aggregate-columns #(reduce + %))))
       {:V1 [13], :V2 [45], :V3 [9.0]}))


  (is (= (note-to-test/represent-value
          (def ctds (tc/dataset {:a [:foo :foo :bar :bar :foo :foo]
                                 :b [:one :one :two :one :two :one]
                                 :c [:dull :dull :shiny :dull :dull :shiny]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          ctds)
       {:a [:foo :foo :bar :bar :foo :foo],
        :b [:one :one :two :one :two :one],
        :c [:dull :dull :shiny :dull :dull :shiny]}))


  (is (= (note-to-test/represent-value
          (tc/crosstab ctds :a [:b :c]))
       {"rows/cols" [:foo :bar],
        [:one :dull] [2 1],
        [:two :shiny] [0 1],
        [:two :dull] [1 0],
        [:one :shiny] [1 0]}))


  (is (= (note-to-test/represent-value
          (tc/crosstab ctds :a [:b :c] {:marginal-rows true :marginal-cols true}))
       {"rows/cols" [:foo :bar :summary],
        [:one :dull] [2 1 3],
        [:two :shiny] [0 1 1],
        [:two :dull] [1 0 1],
        [:one :shiny] [1 0 1],
        :summary [4 2 6]}))


  (is (= (note-to-test/represent-value
          (tc/crosstab ctds :a [:b :c] {:missing-value -1}))
       {"rows/cols" [:foo :bar],
        [:one :dull] [2 1],
        [:two :shiny] [-1 1],
        [:two :dull] [1 -1],
        [:one :shiny] [1 -1]}))


  (is (= (note-to-test/represent-value
          (tc/crosstab ctds :a [:b :c] {:pivot? false}))
       {:rows [:foo :bar :bar :foo :foo],
        :cols
        [[:one :dull] [:two :shiny] [:one :dull] [:two :dull] [:one :shiny]],
        "summary" [2 1 1 1 1]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS :V1))
       {:V1 [1 1 1 1 1 2 2 2 2],
        :V2 [1 3 5 7 9 6 4 8 2],
        :V3 [0.5 1.5 1.0 0.5 1.5 1.5 0.5 1.0 1.0],
        :V4 ["A" "C" "B" "A" "C" "C" "A" "B" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS :V1 :desc))
       {:V1 [2 2 2 2 1 1 1 1 1],
        :V2 [2 4 6 8 5 3 7 1 9],
        :V3 [1.0 0.5 1.5 1.0 1.0 1.5 0.5 0.5 1.5],
        :V4 ["B" "A" "C" "B" "B" "C" "A" "A" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V1 :V2]))
       {:V1 [1 1 1 1 1 2 2 2 2],
        :V2 [1 3 5 7 9 2 4 6 8],
        :V3 [0.5 1.5 1.0 0.5 1.5 1.0 0.5 1.5 1.0],
        :V4 ["A" "C" "B" "A" "C" "B" "A" "C" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V1 :V2] [:asc :desc]))
       {:V1 [1 1 1 1 1 2 2 2 2],
        :V2 [9 7 5 3 1 8 6 4 2],
        :V3 [1.5 0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0],
        :V4 ["C" "A" "B" "C" "A" "B" "C" "A" "B"]}))

  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V1 :V2] [:desc :desc]))
       {:V1 [2 2 2 2 1 1 1 1 1],
        :V2 [8 6 4 2 9 7 5 3 1],
        :V3 [1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5 0.5],
        :V4 ["B" "C" "A" "B" "C" "A" "B" "C" "A"]}))

  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V1 :V3] [:desc :asc]))
       {:V1 [2 2 2 2 1 1 1 1 1],
        :V2 [4 2 8 6 1 7 5 3 9],
        :V3 [0.5 1.0 1.0 1.5 0.5 0.5 1.0 1.5 1.5],
        :V4 ["A" "B" "B" "C" "A" "A" "B" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V4 (fn [row] (* (:V1 row)
                                            (:V2 row)
                                            (:V3 row)))] [:desc :asc]))
       {:V1 [1 1 2 2 1 2 1 1 2],
        :V2 [3 9 6 2 5 8 1 7 4],
        :V3 [1.5 1.5 1.5 1.0 1.0 1.0 0.5 0.5 0.5],
        :V4 ["C" "C" "C" "B" "B" "B" "A" "A" "A"]}))


  (is (= (note-to-test/represent-value
          (defn dist
            [v1 v2]
            (->> v2
                 (map - v1)
                 (map #(* % %))
                 (reduce +)
                 (Math/sqrt))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V1 :V2 :V3] (fn [[x1 y1 z1 :as v1] [x2 y2 z2 :as v2]]
                                          (let [d (dist v1 v2)]
                                            (if (< d 2.0)
                                              (compare z1 z2)
                                              (compare [x1 y1] [x2 y2]))))))
       {:V1 [1 1 1 1 2 2 1 2 2],
        :V2 [1 5 7 9 2 4 3 6 8],
        :V3 [0.5 1.0 0.5 1.5 1.0 0.5 1.5 1.5 1.0],
        :V4 ["A" "B" "A" "C" "B" "A" "C" "C" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS :V1))
       {:V1 [1 2], :V2 [1 2], :V3 [0.5 1.0], :V4 ["A" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS [:V1 :V3]))
       {:V1 [1 2 1 2 1 2],
        :V2 [1 2 3 4 5 6],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS (fn [m] (mod (:V2 m) 3))))
       {:V1 [1 2 1], :V2 [1 2 3], :V3 [0.5 1.0 1.5], :V4 ["A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (fn [m] (mod (:V2 m) 3)))
              (tc/first)
              (tc/ungroup)))
       {:V1 [1 2 1], :V2 [1 2 3], :V3 [0.5 1.0 1.5], :V4 ["A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/unique-by :V1)
              (tc/ungroup)))
       {:V1 [1 2 2 1 1 2],
        :V2 [1 4 2 5 3 6],
        :V3 [0.5 0.5 1.0 1.0 1.5 1.5],
        :V4 ["A" "A" "B" "B" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS :V1 {:strategy :last}))
       {:V1 [2 1], :V2 [8 9], :V3 [1.0 1.5], :V4 ["B" "C"]}))




  (is (= (note-to-test/represent-value
          (tc/unique-by DS :V4 {:strategy vec}))
       {:V1 [[1 2 1] [2 1 2] [1 2 1]],
        :V2 [[1 4 7] [2 5 8] [3 6 9]],
        :V3 [[0.5 0.5 0.5] [1.0 1.0 1.0] [1.5 1.5 1.5]]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS :V4 {:strategy (partial reduce +)}))
       {:V1 [4 5 4], :V2 [12 15 18], :V3 [1.5 3.0 4.5]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS (fn [m] (mod (:V2 m) 3)) {:strategy vec}))
       {:V1 [[1 2 1] [2 1 2] [1 2 1]],
        :V2 [[1 4 7] [2 5 8] [3 6 9]],
        :V3 [[0.5 0.5 0.5] [1.0 1.0 1.0] [1.5 1.5 1.5]],
        :V4 [["A" "A" "A"] ["B" "B" "B"] ["C" "C" "C"]]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/unique-by (fn [m] (mod (:V2 m) 3)) {:strategy vec})
              (tc/ungroup {:add-group-as-column :from-V1})))
       {:from-V1 [1 1 1 2 2 2],
        :V1 [[1 1] [1 1] [1] [2 2] [2] [2]],
        :V2 [[1 7] [3 9] [5] [2 8] [4] [6]],
        :V3 [[0.5 0.5] [1.5 1.5] [1.0] [1.0 1.0] [0.5] [1.5]],
        :V4 [["A" "A"] ["C" "C"] ["B"] ["B" "B"] ["A"] ["C"]]}))


  (is (= (note-to-test/represent-value
          (def DSm (tc/dataset {:V1 (take 9 (cycle [1 2 nil]))
                                :V2 (range 1 10)
                                :V3 (take 9 (cycle [0.5 1.0 nil 1.5]))
                                :V4 (take 9 (cycle ["A" "B" "C"]))})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DSm)
       {:V1 [1 2 :var-or-nil 1 2 :var-or-nil 1 2 :var-or-nil],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-missing DSm))
       {:V1 [:var-or-nil :var-or-nil 1 :var-or-nil],
        :V2 [3 6 7 9],
        :V3 [:var-or-nil 1.0 :var-or-nil 0.5],
        :V4 ["C" "C" "A" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-missing DSm :V1))
       {:V1 [:var-or-nil :var-or-nil :var-or-nil],
        :V2 [3 6 9],
        :V3 [:var-or-nil 1.0 0.5],
        :V4 ["C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DSm
              (tc/group-by :V4)
              (tc/select-missing :V3)
              (tc/ungroup)))
       {:V1 [1 :var-or-nil],
        :V2 [7 3],
        :V3 [:var-or-nil :var-or-nil],
        :V4 ["A" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/drop-missing DSm))
       {:V1 [1 2 1 2 2],
        :V2 [1 2 4 5 8],
        :V3 [0.5 1.0 1.5 0.5 1.5],
        :V4 ["A" "B" "A" "B" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/drop-missing DSm :V1))
       {:V1 [1 2 1 2 1 2],
        :V2 [1 2 4 5 7 8],
        :V3 [0.5 1.0 1.5 0.5 :var-or-nil 1.5],
        :V4 ["A" "B" "A" "B" "A" "B"]}))


  (is (= (note-to-test/represent-value
          (-> DSm
              (tc/group-by :V4)
              (tc/drop-missing :V1)
              (tc/ungroup)))
       {:V1 [1 1 1 2 2 2],
        :V2 [1 4 7 2 5 8],
        :V3 [0.5 1.5 :var-or-nil 1.0 0.5 1.5],
        :V4 ["A" "A" "A" "B" "B" "B"]}))


  (is (= (note-to-test/represent-value
          (def DSm2 (tc/dataset {:a [nil nil nil 1.0 2  nil nil nil nil  nil 4   nil  11 nil nil]
                                 :b [2   2   2 nil nil nil nil nil nil 13   nil   3  4  5 5]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DSm2)
       {:a
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         1.0
         2.0
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         4.0
         :var-or-nil
         11.0
         :var-or-nil
         :var-or-nil],
        :b
        [2
         2
         2
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         13
         :var-or-nil
         3
         4
         5
         5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2))
       {:a [1.0 1.0 1.0 1.0 2.0 2.0 2.0 2.0 4.0 4.0 4.0 4.0 11.0 11.0 11.0],
        :b [2 2 2 2 2 2 13 13 13 13 13 3 4 5 5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 :all :value 999))
       {:a
        [999.0
         999.0
         999.0
         1.0
         2.0
         999.0
         999.0
         999.0
         999.0
         999.0
         4.0
         999.0
         11.0
         999.0
         999.0],
        :b [2 2 2 999 999 999 999 999 999 13 999 3 4 5 5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 :a :value 999))
       {:a
        [999.0
         999.0
         999.0
         1.0
         2.0
         999.0
         999.0
         999.0
         999.0
         999.0
         4.0
         999.0
         11.0
         999.0
         999.0],
        :b
        [2
         2
         2
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         13
         :var-or-nil
         3
         4
         5
         5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 :a :value [-999 -998 -997]))
       {:a
        [-999
         -998
         -997
         1.0
         2.0
         -999
         -998
         -997
         -999
         -998
         4.0
         -997
         11.0
         -999
         -998],
        :b
        [2
         2
         2
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         13
         :var-or-nil
         3
         4
         5
         5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 :a :value tech.v3.datatype.functional/mean))
       {:a [4.5 4.5 4.5 1.0 2.0 4.5 4.5 4.5 4.5 4.5 4.0 4.5 11.0 4.5 4.5],
        :b
        [2
         2
         2
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         13
         :var-or-nil
         3
         4
         5
         5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 :a :value {0 100 1 -100 14 -1000}))
       {:a
        [100
         -100
         :var-or-nil
         1.0
         2.0
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         4.0
         :var-or-nil
         11.0
         :var-or-nil
         -1000],
        :b
        [2
         2
         2
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         13
         :var-or-nil
         3
         4
         5
         5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 [:a :b] :downup))
       {:a [1.0 1.0 1.0 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0],
        :b [2 2 2 2 2 2 2 2 2 13 13 3 4 5 5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 [:a :b] :down 999))
       {:a
        [999.0
         999.0
         999.0
         1.0
         2.0
         2.0
         2.0
         2.0
         2.0
         2.0
         4.0
         4.0
         11.0
         11.0
         11.0],
        :b [2 2 2 2 2 2 2 2 2 13 13 3 4 5 5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 [:a :b] :up))
       {:a
        [1.0
         1.0
         1.0
         1.0
         2.0
         4.0
         4.0
         4.0
         4.0
         4.0
         4.0
         11.0
         11.0
         :var-or-nil
         :var-or-nil],
        :b [2 2 2 13 13 13 13 13 13 13 3 3 4 5 5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 [:a :b] :updown))
       {:a [1.0 1.0 1.0 1.0 2.0 4.0 4.0 4.0 4.0 4.0 4.0 11.0 11.0 11.0 11.0],
        :b [2 2 2 13 13 13 13 13 13 13 3 3 4 5 5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 [:a :b] :midpoint))
       {:a [1.0 1.0 1.0 1.0 2.0 3.0 3.0 3.0 3.0 3.0 4.0 7.5 11.0 11.0 11.0],
        :b [2.0 2.0 2.0 7.5 7.5 7.5 7.5 7.5 7.5 13.0 8.0 3.0 4.0 5.0 5.0]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 [:a :b] :down tech.v3.datatype.functional/mean))
       {:a [4.5 4.5 4.5 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0],
        :b [2 2 2 2 2 2 2 2 2 13 13 3 4 5 5]}))


  (is (= (note-to-test/represent-value
          (tc/replace-missing DSm2 [:a :b] :lerp))
       {:a
        [1.0
         1.0
         1.0
         1.0
         2.0
         2.3333333333333335
         2.6666666666666665
         3.0
         3.333333333333333
         3.6666666666666665
         4.0
         7.5
         11.0
         11.0
         11.0],
        :b
        [2.0
         2.0
         2.0
         3.571428571428571
         5.142857142857142
         6.714285714285714
         8.285714285714285
         9.857142857142858
         11.428571428571429
         13.0
         8.0
         3.0
         4.0
         5.0
         5.0]}))


  (is (= (note-to-test/represent-value
          (-> (tc/dataset {:dt [(java.time.LocalDateTime/of 2020 1 1 11 22 33)
                                nil nil nil nil nil nil nil
                                (java.time.LocalDateTime/of 2020 10 1 1 1 1)]})
              (tc/replace-missing :lerp)))
       {:dt
        [[java.time.LocalDateTime "2020-01-01T11:22:33"]
         [java.time.LocalDateTime "2020-02-04T16:04:51.500"]
         [java.time.LocalDateTime "2020-03-09T20:47:10"]
         [java.time.LocalDateTime "2020-04-13T01:29:28.500"]
         [java.time.LocalDateTime "2020-05-17T06:11:47"]
         [java.time.LocalDateTime "2020-06-20T10:54:05.500"]
         [java.time.LocalDateTime "2020-07-24T15:36:24"]
         [java.time.LocalDateTime "2020-08-27T20:18:42.500"]
         [java.time.LocalDateTime "2020-10-01T01:01:01"]]}))


  (is (= (note-to-test/represent-value
          (-> (tc/dataset {:a [1 2 9]
                           :b [:a :b :c]})
              (tc/fill-range-replace :a 1)))
       {:a [1.0 2.0 3.0 4.0 5.0 6.0 7.0 8.0 9.0],
        :b [:a :b :b :b :b :b :b :b :c]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4]))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :joined
        ["1-1-A" "2-2-B" "3-C" "1-4-A" "2-5-B" "6-C" "1-7-A" "2-8-B" "9-C"]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4] {:drop-columns? false}))
       {:V1 [1 2 :var-or-nil 1 2 :var-or-nil 1 2 :var-or-nil],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :joined
        ["1-1-A" "2-2-B" "3-C" "1-4-A" "2-5-B" "6-C" "1-7-A" "2-8-B" "9-C"]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4] {:missing-subst "NA"}))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :joined
        ["1-1-A"
         "2-2-B"
         "NA-3-C"
         "1-4-A"
         "2-5-B"
         "NA-6-C"
         "1-7-A"
         "2-8-B"
         "NA-9-C"]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4] {:separator "/"
                                                      :missing-subst "."}))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :joined
        ["1/1/A"
         "2/2/B"
         "./3/C"
         "1/4/A"
         "2/5/B"
         "./6/C"
         "1/7/A"
         "2/8/B"
         "./9/C"]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4] {:separator ["-" "/"]
                                                      :missing-subst "."}))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :joined
        ["1-1/A"
         "2-2/B"
         ".-3/C"
         "1-4/A"
         "2-5/B"
         ".-6/C"
         "1-7/A"
         "2-8/B"
         ".-9/C"]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4] {:result-type :map}))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :joined
        [{:V1 1, :V2 1, :V4 "A"}
         {:V1 2, :V2 2, :V4 "B"}
         {:V1 :var-or-nil, :V2 3, :V4 "C"}
         {:V1 1, :V2 4, :V4 "A"}
         {:V1 2, :V2 5, :V4 "B"}
         {:V1 :var-or-nil, :V2 6, :V4 "C"}
         {:V1 1, :V2 7, :V4 "A"}
         {:V1 2, :V2 8, :V4 "B"}
         {:V1 :var-or-nil, :V2 9, :V4 "C"}]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4] {:result-type :seq}))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :joined
        [[1 1 "A"]
         [2 2 "B"]
         [:var-or-nil 3 "C"]
         [1 4 "A"]
         [2 5 "B"]
         [:var-or-nil 6 "C"]
         [1 7 "A"]
         [2 8 "B"]
         [:var-or-nil 9 "C"]]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns DSm :joined [:V1 :V2 :V4] {:result-type hash}))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :joined
        [535226087
         1128801549
         -1842240303
         2022347171
         1884312041
         -1555412370
         1640237355
         -967279152
         1128367958]}))


  (is (= (note-to-test/represent-value
          (-> DSm
              (tc/group-by :V4)
              (tc/join-columns :joined [:V1 :V2 :V4])
              (tc/ungroup)))
       {:V3 [0.5 1.5 :var-or-nil 1.0 0.5 1.5 :var-or-nil 1.0 0.5],
        :joined
        ["1-1-A" "1-4-A" "1-7-A" "2-2-B" "2-5-B" "2-8-B" "3-C" "6-C" "9-C"]}))


  (is (= (note-to-test/represent-value
          (def df (tc/dataset {:x ["a" "a" nil nil]
                                :y ["b" nil "b" nil]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          df)
       {:x ["a" "a" :var-or-nil :var-or-nil],
        :y ["b" :var-or-nil "b" :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns df "z" [:x :y] {:drop-columns? false
                                            :missing-subst "NA"
                                            :separator "_"}))
       {:x ["a" "a" :var-or-nil :var-or-nil],
        :y ["b" :var-or-nil "b" :var-or-nil],
        "z" ["a_b" "a_NA" "NA_b" "NA_NA"]}))


  (is (= (note-to-test/represent-value
          (tc/join-columns df "z" [:x :y] {:drop-columns? false
                                            :separator "_"}))
       {:x ["a" "a" :var-or-nil :var-or-nil],
        :y ["b" :var-or-nil "b" :var-or-nil],
        "z" ["a_b" "a" "b" :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column DS :V3 [:int-part :frac-part] (fn [^double v]
                                                               [(int (quot v 1.0))
                                                                (mod v 1.0)])))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :int-part [0 1 1 0 1 1 0 1 1],
        :frac-part [0.5 0.0 0.5 0.5 0.0 0.5 0.5 0.0 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column DS :V3 [:int-part :frac-part] (fn [^double v]
                                                               [(int (quot v 1.0))
                                                                (mod v 1.0)]) {:drop-column? false}))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :int-part [0 1 1 0 1 1 0 1 1],
        :frac-part [0.5 0.0 0.5 0.5 0.0 0.5 0.5 0.0 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column DS :V3 [:int-part :frac-part] (fn [^double v]
                                                               [(int (quot v 1.0))
                                                                (mod v 1.0)]) {:missing-subst [0 0.0]}))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :int-part [:var-or-nil 1 1 :var-or-nil 1 1 :var-or-nil 1 1],
        :frac-part
        [0.5 :var-or-nil 0.5 0.5 :var-or-nil 0.5 0.5 :var-or-nil 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/separate-column :V3 [:int-part :fract-part] (fn [^double v]
                                                                 [(int (quot v 1.0))
                                                                  (mod v 1.0)]))
              (tc/ungroup)))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :int-part [0 0 0 1 1 1 1 1 1],
        :fract-part [0.5 0.5 0.5 0.0 0.0 0.0 0.5 0.5 0.5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column DS :V3 (fn [^double v]
                                        {:int-part (int (quot v 1.0))
                                         :fract-part (mod v 1.0)})))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :int-part [0 1 1 0 1 1 0 1 1],
        :fract-part [0.5 0.0 0.5 0.5 0.0 0.5 0.5 0.0 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column DS :V3 nil (fn [^double v]
                                            {:int-part (int (quot v 1.0))
                                             :fract-part (mod v 1.0)}) {:drop-column? false}))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :int-part [0 1 1 0 1 1 0 1 1],
        :fract-part [0.5 0.0 0.5 0.5 0.0 0.5 0.5 0.0 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column DS :V3 nil (fn [^double v]
                                           {:int-part (int (quot v 1.0))
                                            :fract-part (mod v 1.0)}) {:drop-column? :all}))
       {:int-part [0 1 1 0 1 1 0 1 1],
        :fract-part [0.5 0.0 0.5 0.5 0.0 0.5 0.5 0.0 0.5]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column DS :V3 (fn [^double v]
                                       [(int (quot v 1.0)) (mod v 1.0)])))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        ":V3-0" [0 1 1 0 1 1 0 1 1],
        ":V3-1" [0.5 0.0 0.5 0.5 0.0 0.5 0.5 0.0 0.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DSm
              (tc/join-columns :joined [:V1 :V2 :V4] {:result-type :map})
              (tc/separate-column :joined [:v1 :v2 :v4] (juxt :V1 :V2 :V4))))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :v1 [1 2 :var-or-nil 1 2 :var-or-nil 1 2 :var-or-nil],
        :v2 [1 2 3 4 5 6 7 8 9],
        :v4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))

  (is (= (note-to-test/represent-value
          (-> DSm
              (tc/join-columns :joined [:V1 :V2 :V4] {:result-type :seq})
              (tc/separate-column :joined [:v1 :v2 :v4] identity)))
       {:V3 [0.5 1.0 :var-or-nil 1.5 0.5 1.0 :var-or-nil 1.5 0.5],
        :v1 [1 2 :var-or-nil 1 2 :var-or-nil 1 2 :var-or-nil],
        :v2 [1 2 3 4 5 6 7 8 9],
        :v4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (def df-separate (tc/dataset {:x [nil "a.b" "a.d" "b.c"]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def df-separate2 (tc/dataset {:x ["a" "a b" nil "a b c"]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def df-separate3 (tc/dataset {:x ["a?b" nil "a.b" "b:c"]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def df-extract (tc/dataset {:x [nil "a-b" "a-d" "b-c" "d-e"]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          df-separate)
       {:x [:var-or-nil "a.b" "a.d" "b.c"]}))

  (is (= (note-to-test/represent-value
          df-separate2)
       {:x ["a" "a b" :var-or-nil "a b c"]}))

  (is (= (note-to-test/represent-value
          df-separate3)
       {:x ["a?b" :var-or-nil "a.b" "b:c"]}))

  (is (= (note-to-test/represent-value
          df-extract)
       {:x [:var-or-nil "a-b" "a-d" "b-c" "d-e"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-separate :x [:A :B] "\\."))
       {:A [:var-or-nil "a" "a" "b"], :B [:var-or-nil "b" "d" "c"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-separate :x [nil :B] "\\."))
       {:B [:var-or-nil "b" "d" "c"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-separate2 :x ["a" "b"] " "))
       {"a" ["a" "a" :var-or-nil "a"], "b" [:var-or-nil "b" :var-or-nil "b"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-separate3 :x ["a" "b"] "[?\\.:]"))
       {"a" ["a" :var-or-nil "a" "b"], "b" ["b" :var-or-nil "b" "c"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-separate3 :x ["a" "b"] #"(.).(.)"))
       {"a" ["a" :var-or-nil "a" "b"], "b" ["b" :var-or-nil "b" "c"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-extract :x ["A"] "-"))
       {"A" [:var-or-nil "a" "a" "b" "d"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-extract :x ["A" "B"] #"(\p{Alnum})-(\p{Alnum})"))
       {"A" [:var-or-nil "a" "a" "b" "d"], "B" [:var-or-nil "b" "d" "c" "e"]}))


  (is (= (note-to-test/represent-value
          (tc/separate-column df-extract :x ["A" "B"] #"([a-d]+)-([a-d]+)"))
       {"A" [:var-or-nil "a" "a" "b" :var-or-nil],
        "B" [:var-or-nil "b" "d" "c" :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (-> (tc/dataset {:x [(double-array [1 2 3])
                               (double-array [4 5 6])]
                           :y [:a :b]})
              (tc/array-column->columns :x)))
       {:y [:a :b], 0 [1.0 4.0], 1 [2.0 5.0], 2 [3.0 6.0]}))


  (is (= (note-to-test/represent-value
          (-> (tc/dataset {0 [0.0 1 2]
                           1 [3.0 4 5]
                           :x [:a :b :c]})
              (tc/columns->array-column [0 1] :y)))
       {:x [:a :b :c], :y [[0.0 3.0] [1.0 4.0] [2.0 5.0]]}))


  (is (= (note-to-test/represent-value
          (tc/fold-by DS [:V3 :V4 :V1]))
       {:V3 [0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C"],
        :V1 [1 2 1 2 1 2],
        :V2 [[1 7] [2 8] [3 9] [4] [5] [6]]}))


  (is (= (note-to-test/represent-value
          (tc/fold-by DS [:V4]))
       {:V4 ["A" "B" "C"],
        :V1 [[1 2 1] [2 1 2] [1 2 1]],
        :V2 [[1 4 7] [2 5 8] [3 6 9]],
        :V3 [[0.5 0.5 0.5] [1.0 1.0 1.0] [1.5 1.5 1.5]]}))


  (is (= (note-to-test/represent-value
          (tc/fold-by DS [:V4] seq))
       {:V4 ["A" "B" "C"],
        :V1 [[1 2 1] [2 1 2] [1 2 1]],
        :V2 [[1 4 7] [2 5 8] [3 6 9]],
        :V3 [[0.5 0.5 0.5] [1.0 1.0 1.0] [1.5 1.5 1.5]]}))


  (is (= (note-to-test/represent-value
          (tc/fold-by DS [:V4] set))
       {:V4 ["A" "B" "C"],
        :V1 [#{1 2} #{1 2} #{1 2}],
        :V2 [#{7 1 4} #{2 5 8} #{6 3 9}],
        :V3 [#{0.5} #{1.0} #{1.5}]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/fold-by :V4)
              (tc/ungroup)))
       {:V4 ["A" "C" "B" "B" "A" "C"],
        :V1 [[1 1] [1 1] [1] [2 2] [2] [2]],
        :V2 [[1 7] [3 9] [5] [2 8] [4] [6]],
        :V3 [[0.5 0.5] [1.5 1.5] [1.0] [1.0 1.0] [0.5] [1.5]]}))


  (is (= (note-to-test/represent-value
          (tc/unroll (tc/fold-by DS [:V4]) [:V1]))
       {:V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V2
        [[1 4 7]
         [1 4 7]
         [1 4 7]
         [2 5 8]
         [2 5 8]
         [2 5 8]
         [3 6 9]
         [3 6 9]
         [3 6 9]],
        :V3
        [[0.5 0.5 0.5]
         [0.5 0.5 0.5]
         [0.5 0.5 0.5]
         [1.0 1.0 1.0]
         [1.0 1.0 1.0]
         [1.0 1.0 1.0]
         [1.5 1.5 1.5]
         [1.5 1.5 1.5]
         [1.5 1.5 1.5]],
        :V1 [1 2 1 2 1 2 1 2 1]}))


  (is (= (note-to-test/represent-value
          (tc/unroll (tc/fold-by DS [:V4]) [:V1 :V2 :V3]))
       {:V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :V3 [0.5 0.5 0.5 1.0 1.0 1.0 1.5 1.5 1.5]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/fold-by [:V4 :V1])
              (tc/unroll [:V2])
              (tc/unroll [:V3])))
       {:V4 ["A" "A" "A" "A" "B" "B" "B" "B" "C" "C" "C" "C" "A" "B" "C"],
        :V1 [1 1 1 1 2 2 2 2 1 1 1 1 2 1 2],
        :V2 [1 1 7 7 2 2 8 8 3 3 9 9 4 5 6],
        :V3 [0.5 0.5 0.5 0.5 1.0 1.0 1.0 1.0 1.5 1.5 1.5 1.5 0.5 1.0 1.5]}))


  (is (= (note-to-test/represent-value
          (tc/unroll (tc/fold-by DS [:V1]) [:V4 :V2 :V3] {:indexes? true}))
       {:V1 [1 1 1 1 1 2 2 2 2],
        :indexes [0 1 2 3 4 0 1 2 3],
        :V4 ["A" "C" "B" "A" "C" "B" "A" "C" "B"],
        :V2 [1 3 5 7 9 2 4 6 8],
        :V3 [0.5 1.5 1.0 0.5 1.5 1.0 0.5 1.5 1.0]}))

  (is (= (note-to-test/represent-value
          (tc/unroll (tc/fold-by DS [:V1]) [:V4 :V2 :V3] {:indexes? "vector idx"}))
       {:V1 [1 1 1 1 1 2 2 2 2],
        "vector idx" [0 1 2 3 4 0 1 2 3],
        :V4 ["A" "C" "B" "A" "C" "B" "A" "C" "B"],
        :V2 [1 3 5 7 9 2 4 6 8],
        :V3 [0.5 1.5 1.0 0.5 1.5 1.0 0.5 1.5 1.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/fold-by [:V1])
              (tc/unroll [:V4 :V2 :V3] {:datatypes {:V4 :string
                                                     :V2 :int16
                                                     :V3 :float32}})
              (tc/info :columns)))
       {:name [:V1 :V4 :V2 :V3],
        :datatype [:int64 :string :int16 :float32],
        :n-elems [9 9 9 9],
        :categorical? [:var-or-nil true :var-or-nil :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V1)
              (tc/fold-by [:V1 :V4])
              (tc/unroll :V3 {:indexes? true})
              (tc/ungroup)))
       {:V1 [1 1 1 1 1 2 2 2 2],
        :V4 ["A" "A" "C" "C" "B" "B" "B" "A" "C"],
        :V2 [[1 7] [1 7] [3 9] [3 9] [5] [2 8] [2 8] [4] [6]],
        :indexes [0 1 0 1 0 0 1 0 0],
        :V3 [0.5 0.5 1.5 1.5 1.0 1.0 1.0 0.5 1.5]}))


  (is (= (note-to-test/represent-value
          (def relig-income (tc/dataset "data/relig_income.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          relig-income)
       {"$10-20k" [34 27 21 617 14 869 9 244 27 19 495 40 7 17 7 33 2 299],
        "$50-75k"
        [137 70 58 1116 35 1486 34 223 30 95 1107 112 23 47 14 63 7 528],
        ">150k" [84 74 53 633 18 414 54 78 6 151 634 42 6 46 12 41 4 258],
        "$100-150k"
        [109 59 39 792 17 723 48 81 11 87 753 49 8 42 14 40 4 321],
        "$75-100k"
        [122 73 62 949 21 949 47 131 15 69 939 85 16 38 18 46 3 407],
        "<$10k" [27 12 27 418 15 575 1 228 20 19 289 29 6 13 9 20 5 217],
        "$20-30k" [60 37 30 732 15 1064 7 236 24 25 619 48 9 23 11 40 3 374],
        "$30-40k" [81 52 34 670 11 982 9 238 24 25 655 51 10 32 13 46 4 365],
        "$40-50k" [76 35 33 638 10 881 11 197 21 30 651 56 9 32 13 49 2 341],
        "Don't know/refused"
        [96 76 54 1489 116 1529 37 339 37 162 1328 69 22 73 18 71 8 597],
        "religion"
        ["Agnostic"
         "Atheist"
         "Buddhist"
         "Catholic"
         "Don’t know/refused"
         "Evangelical Prot"
         "Hindu"
         "Historically Black Prot"
         "Jehovah's Witness"
         "Jewish"
         "Mainline Prot"
         "Mormon"
         "Muslim"
         "Orthodox"
         "Other Christian"
         "Other Faiths"
         "Other World Religions"
         "Unaffiliated"]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->longer relig-income (complement #{"religion"})))
       {"religion"
        ["Agnostic"
         "Atheist"
         "Buddhist"
         "Catholic"
         "Don’t know/refused"
         "Evangelical Prot"
         "Hindu"
         "Historically Black Prot"
         "Jehovah's Witness"
         "Jewish"
         "Mainline Prot"
         "Mormon"
         "Muslim"
         "Orthodox"
         "Other Christian"
         "Other Faiths"
         "Other World Religions"
         "Unaffiliated"
         "Agnostic"
         "Atheist"],
        :$column
        ["<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "<$10k"
         "Don't know/refused"
         "Don't know/refused"],
        :$value [27 12 27 418 15 575 1 228 20 19 289 29 6 13 9 20 5 217 96 76]}))


  (is (= (note-to-test/represent-value
          (def bilboard (-> (tc/dataset "data/billboard.csv.gz")
                            (tc/drop-columns :type/boolean))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (->> bilboard
               (tc/column-names)
               (take 13)
               (tc/select-columns bilboard)))
       {"track"
        ["Baby Don't Cry (Keep..."
         "The Hardest Part Of ..."
         "Kryptonite"
         "Loser"
         "Wobble Wobble"
         "Give Me Just One Nig..."
         "Dancing Queen"
         "I Don't Wanna"
         "Try Again"
         "Open My Heart"
         "More"
         "Come On Over Baby (A..."
         "I Turn To You"
         "What A Girl Wants"
         "Better Off Alone"
         "Smoke Rings In The D..."
         "Sexual"
         "I'm Outta Love"
         "My Baby You"
         "You Sang To Me"],
        "wk2"
        [82
         87
         70
         76
         34
         39
         97
         62
         53
         76
         84
         47
         39
         51
         65
         78
         99
         :var-or-nil
         76
         54],
        "wk3"
        [72
         92
         68
         72
         25
         34
         96
         51
         38
         74
         75
         45
         30
         28
         53
         76
         96
         :var-or-nil
         76
         50],
        "wk4"
        [77
         :var-or-nil
         67
         69
         17
         26
         95
         41
         28
         69
         73
         29
         28
         18
         48
         77
         96
         95
         70
         43],
        "wk6"
        [94
         :var-or-nil
         57
         65
         31
         19
         :var-or-nil
         35
         18
         67
         69
         18
         19
         13
         36
         :var-or-nil
         93
         :var-or-nil
         81
         27],
        "wk10"
        [:var-or-nil
         :var-or-nil
         51
         61
         57
         6
         :var-or-nil
         36
         10
         59
         83
         11
         17
         2
         30
         :var-or-nil
         :var-or-nil
         :var-or-nil
         76
         13],
        "wk1" [87 91 81 76 57 51 97 84 59 76 84 57 50 71 79 80 99 92 82 77],
        "wk7"
        [99
         :var-or-nil
         54
         55
         36
         2
         :var-or-nil
         35
         16
         61
         68
         11
         20
         11
         34
         :var-or-nil
         93
         :var-or-nil
         74
         21],
        "wk8"
        [:var-or-nil
         :var-or-nil
         53
         59
         49
         2
         :var-or-nil
         38
         14
         58
         65
         9
         17
         1
         29
         :var-or-nil
         96
         :var-or-nil
         80
         18],
        "wk5"
        [87
         :var-or-nil
         66
         67
         17
         26
         100
         38
         21
         68
         73
         23
         21
         13
         45
         92
         100
         :var-or-nil
         82
         30],
        "wk9"
        [:var-or-nil
         :var-or-nil
         51
         62
         53
         3
         :var-or-nil
         38
         12
         57
         73
         9
         17
         1
         27
         :var-or-nil
         :var-or-nil
         :var-or-nil
         76
         15],
        "date.entered"
        [[java.time.LocalDate "2000-02-26"]
         [java.time.LocalDate "2000-09-02"]
         [java.time.LocalDate "2000-04-08"]
         [java.time.LocalDate "2000-10-21"]
         [java.time.LocalDate "2000-04-15"]
         [java.time.LocalDate "2000-08-19"]
         [java.time.LocalDate "2000-07-08"]
         [java.time.LocalDate "2000-01-29"]
         [java.time.LocalDate "2000-03-18"]
         [java.time.LocalDate "2000-08-26"]
         [java.time.LocalDate "2000-04-29"]
         [java.time.LocalDate "2000-08-05"]
         [java.time.LocalDate "2000-04-15"]
         [java.time.LocalDate "1999-11-27"]
         [java.time.LocalDate "2000-04-08"]
         [java.time.LocalDate "2000-01-22"]
         [java.time.LocalDate "1999-07-17"]
         [java.time.LocalDate "2000-04-01"]
         [java.time.LocalDate "2000-09-16"]
         [java.time.LocalDate "2000-02-26"]],
        "artist"
        ["2 Pac"
         "2Ge+her"
         "3 Doors Down"
         "3 Doors Down"
         "504 Boyz"
         "98^0"
         "A*Teens"
         "Aaliyah"
         "Aaliyah"
         "Adams, Yolanda"
         "Adkins, Trace"
         "Aguilera, Christina"
         "Aguilera, Christina"
         "Aguilera, Christina"
         "Alice Deejay"
         "Allan, Gary"
         "Amber"
         "Anastacia"
         "Anthony, Marc"
         "Anthony, Marc"]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->longer bilboard #(clojure.string/starts-with? % "wk") {:target-columns :week
                                                                             :value-column-name :rank}))
       {"artist"
        ["3 Doors Down"
         "Braxton, Toni"
         "Creed"
         "Creed"
         "Hill, Faith"
         "Joe"
         "Lonestar"
         "Vertical Horizon"
         "matchbox twenty"
         "Creed"
         "Lonestar"
         "3 Doors Down"
         "3 Doors Down"
         "98^0"
         "Aaliyah"
         "Aaliyah"
         "Adams, Yolanda"
         "Aguilera, Christina"
         "Aguilera, Christina"
         "Aguilera, Christina"],
        "track"
        ["Kryptonite"
         "He Wasn't Man Enough"
         "Higher"
         "With Arms Wide Open"
         "Breathe"
         "I Wanna Know"
         "Amazed"
         "Everything You Want"
         "Bent"
         "Higher"
         "Amazed"
         "Kryptonite"
         "Loser"
         "Give Me Just One Nig..."
         "I Don't Wanna"
         "Try Again"
         "Open My Heart"
         "Come On Over Baby (A..."
         "I Turn To You"
         "What A Girl Wants"],
        "date.entered"
        [[java.time.LocalDate "2000-04-08"]
         [java.time.LocalDate "2000-03-18"]
         [java.time.LocalDate "1999-09-11"]
         [java.time.LocalDate "2000-05-13"]
         [java.time.LocalDate "1999-11-06"]
         [java.time.LocalDate "2000-01-01"]
         [java.time.LocalDate "1999-06-05"]
         [java.time.LocalDate "2000-01-22"]
         [java.time.LocalDate "2000-04-29"]
         [java.time.LocalDate "1999-09-11"]
         [java.time.LocalDate "1999-06-05"]
         [java.time.LocalDate "2000-04-08"]
         [java.time.LocalDate "2000-10-21"]
         [java.time.LocalDate "2000-08-19"]
         [java.time.LocalDate "2000-01-29"]
         [java.time.LocalDate "2000-03-18"]
         [java.time.LocalDate "2000-08-26"]
         [java.time.LocalDate "2000-08-05"]
         [java.time.LocalDate "2000-04-15"]
         [java.time.LocalDate "1999-11-27"]],
        :week
        ["wk35"
         "wk35"
         "wk35"
         "wk35"
         "wk35"
         "wk35"
         "wk35"
         "wk35"
         "wk35"
         "wk55"
         "wk55"
         "wk19"
         "wk19"
         "wk19"
         "wk19"
         "wk19"
         "wk19"
         "wk19"
         "wk19"
         "wk19"],
        :rank [4 34 22 5 8 5 14 27 33 21 22 18 73 93 83 3 79 23 29 18]}))


  (is (= (note-to-test/represent-value
          (tc/pivot->longer bilboard #(clojure.string/starts-with? % "wk") {:target-columns :week
                                                                             :value-column-name :rank
                                                                             :splitter #"wk(.*)"
                                                                             :datatypes {:week :int16}}))
       {"artist"
        ["3 Doors Down"
         "Creed"
         "Creed"
         "Hill, Faith"
         "Lonestar"
         "3 Doors Down"
         "Creed"
         "Hill, Faith"
         "Lonestar"
         "2 Pac"
         "3 Doors Down"
         "3 Doors Down"
         "504 Boyz"
         "98^0"
         "Aaliyah"
         "Aaliyah"
         "Adams, Yolanda"
         "Adkins, Trace"
         "Aguilera, Christina"
         "Aguilera, Christina"],
        "track"
        ["Kryptonite"
         "Higher"
         "With Arms Wide Open"
         "Breathe"
         "Amazed"
         "Kryptonite"
         "Higher"
         "Breathe"
         "Amazed"
         "Baby Don't Cry (Keep..."
         "Kryptonite"
         "Loser"
         "Wobble Wobble"
         "Give Me Just One Nig..."
         "I Don't Wanna"
         "Try Again"
         "Open My Heart"
         "More"
         "Come On Over Baby (A..."
         "I Turn To You"],
        "date.entered"
        [[java.time.LocalDate "2000-04-08"]
         [java.time.LocalDate "1999-09-11"]
         [java.time.LocalDate "2000-05-13"]
         [java.time.LocalDate "1999-11-06"]
         [java.time.LocalDate "1999-06-05"]
         [java.time.LocalDate "2000-04-08"]
         [java.time.LocalDate "1999-09-11"]
         [java.time.LocalDate "1999-11-06"]
         [java.time.LocalDate "1999-06-05"]
         [java.time.LocalDate "2000-02-26"]
         [java.time.LocalDate "2000-04-08"]
         [java.time.LocalDate "2000-10-21"]
         [java.time.LocalDate "2000-04-15"]
         [java.time.LocalDate "2000-08-19"]
         [java.time.LocalDate "2000-01-29"]
         [java.time.LocalDate "2000-03-18"]
         [java.time.LocalDate "2000-08-26"]
         [java.time.LocalDate "2000-04-29"]
         [java.time.LocalDate "2000-08-05"]
         [java.time.LocalDate "2000-04-15"]],
        :week [46 46 46 46 46 51 51 51 51 6 6 6 6 6 6 6 6 6 6 6],
        :rank [21 7 37 31 5 42 14 49 12 94 57 65 31 19 35 18 67 69 18 19]}))


  (is (= (note-to-test/represent-value
          (def who (tc/dataset "data/who.csv.gz")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (->> who
               (tc/column-names)
               (take 10)
               (tc/select-columns who)))
       {"country"
        ["Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"
         "Afghanistan"],
        "iso2"
        ["AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"
         "AF"],
        "new_sp_m2534"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         6
         128
         55],
        "new_sp_m1524"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         10
         129
         55],
        "new_sp_m3544"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         3
         90
         47],
        "iso3"
        ["AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AFG"],
        "new_sp_m014"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         0
         30
         8],
        "year"
        [1980
         1981
         1982
         1983
         1984
         1985
         1986
         1987
         1988
         1989
         1990
         1991
         1992
         1993
         1994
         1995
         1996
         1997
         1998
         1999],
        "new_sp_m4554"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         5
         89
         34],
        "new_sp_m5564"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         2
         64
         21]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->longer who #(clojure.string/starts-with? % "new") {:target-columns [:diagnosis :gender :age]
                                                                         :splitter #"new_?(.*)_(.)(.*)"
                                                                         :value-column-name :count}))
       {"country"
        ["Albania"
         "Algeria"
         "Andorra"
         "Angola"
         "Anguilla"
         "Antigua and Barbuda"
         "Argentina"
         "Armenia"
         "Australia"
         "Austria"
         "Azerbaijan"
         "Bahamas"
         "Bahrain"
         "Bangladesh"
         "Barbados"
         "Belarus"
         "Belgium"
         "Belize"
         "Benin"
         "Bermuda"],
        "iso2"
        ["AL"
         "DZ"
         "AD"
         "AO"
         "AI"
         "AG"
         "AR"
         "AM"
         "AU"
         "AT"
         "AZ"
         "BS"
         "BH"
         "BD"
         "BB"
         "BY"
         "BE"
         "BZ"
         "BJ"
         "BM"],
        "iso3"
        ["ALB"
         "DZA"
         "AND"
         "AGO"
         "AIA"
         "ATG"
         "ARG"
         "ARM"
         "AUS"
         "AUT"
         "AZE"
         "BHS"
         "BHR"
         "BGD"
         "BRB"
         "BLR"
         "BEL"
         "BLZ"
         "BEN"
         "BMU"],
        "year"
        [2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013],
        :diagnosis
        ["rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"
         "rel"],
        :gender
        ["m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"
         "m"],
        :age
        [1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524
         1524],
        :count
        [60 1021 0 2992 0 1 1124 116 105 44 958 2 13 14705 0 162 63 8 301 0]}))


  (is (= (note-to-test/represent-value
          (def family (tc/dataset "data/family.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          family)
       {"family" [1 2 3 4 5],
        "dob_child1"
        [[java.time.LocalDate "1998-11-26"]
         [java.time.LocalDate "1996-06-22"]
         [java.time.LocalDate "2002-07-11"]
         [java.time.LocalDate "2004-10-10"]
         [java.time.LocalDate "2000-12-05"]],
        "dob_child2"
        [[java.time.LocalDate "2000-01-29"]
         :var-or-nil
         [java.time.LocalDate "2004-04-05"]
         [java.time.LocalDate "2009-08-27"]
         [java.time.LocalDate "2005-02-28"]],
        "gender_child1" [1 2 2 1 2],
        "gender_child2" [2 :var-or-nil 2 1 1]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->longer family (complement #{"family"}) {:target-columns [nil :child]
                                                              :splitter "_"
                                                              :datatypes {"gender" :int16}}))
       {"family" [1 2 3 4 5 1 3 4 5],
        :child
        ["child1"
         "child1"
         "child1"
         "child1"
         "child1"
         "child2"
         "child2"
         "child2"
         "child2"],
        "dob"
        [[java.time.LocalDate "1998-11-26"]
         [java.time.LocalDate "1996-06-22"]
         [java.time.LocalDate "2002-07-11"]
         [java.time.LocalDate "2004-10-10"]
         [java.time.LocalDate "2000-12-05"]
         [java.time.LocalDate "2000-01-29"]
         [java.time.LocalDate "2004-04-05"]
         [java.time.LocalDate "2009-08-27"]
         [java.time.LocalDate "2005-02-28"]],
        "gender" [1 2 2 1 2 2 2 1 1]}))


  (is (= (note-to-test/represent-value
          (def anscombe (tc/dataset "data/anscombe.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          anscombe)
       {"x1" [10 8 13 9 11 14 6 4 12 7 5],
        "x2" [10 8 13 9 11 14 6 4 12 7 5],
        "x3" [10 8 13 9 11 14 6 4 12 7 5],
        "x4" [8 8 8 8 8 8 8 19 8 8 8],
        "y1" [8.04 6.95 7.58 8.81 8.33 9.96 7.24 4.26 10.84 4.82 5.68],
        "y2" [9.14 8.14 8.74 8.77 9.26 8.1 6.13 3.1 9.13 7.26 4.74],
        "y3" [7.46 6.77 12.74 7.11 7.81 8.84 6.08 5.39 8.15 6.42 5.73],
        "y4" [6.58 5.76 7.71 8.84 8.47 7.04 5.25 12.5 5.56 7.91 6.89]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->longer anscombe :all {:splitter #"(.)(.)"
                                            :target-columns [nil :set]}))
       {:set [1 1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2],
        "x" [10 8 13 9 11 14 6 4 12 7 5 10 8 13 9 11 14 6 4 12],
        "y"
        [8.04
         6.95
         7.58
         8.81
         8.33
         9.96
         7.24
         4.26
         10.84
         4.82
         5.68
         9.14
         8.14
         8.74
         8.77
         9.26
         8.1
         6.13
         3.1
         9.13]}))






  (is (= (note-to-test/represent-value
          (def fish (tc/dataset "data/fish_encounters.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          fish)
       {"fish"
        [4842
         4842
         4842
         4842
         4842
         4842
         4842
         4842
         4842
         4842
         4842
         4843
         4843
         4843
         4843
         4843
         4843
         4843
         4843
         4843],
        "station"
        ["Release"
         "I80_1"
         "Lisbon"
         "Rstr"
         "Base_TD"
         "BCE"
         "BCW"
         "BCE2"
         "BCW2"
         "MAE"
         "MAW"
         "Release"
         "I80_1"
         "Lisbon"
         "Rstr"
         "Base_TD"
         "BCE"
         "BCW"
         "BCE2"
         "BCW2"],
        "seen" [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->wider fish "station" "seen" {:drop-missing? false}))
       {"Lisbon"
        [1
         1
         1
         1
         1
         1
         1
         :var-or-nil
         1
         1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "Release" [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1],
        "BCE2"
        [1
         1
         1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "MAW"
        [1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "Base_TD"
        [1
         1
         1
         1
         1
         1
         1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "BCW"
        [1
         1
         1
         1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "MAE"
        [1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "Rstr"
        [1
         1
         1
         1
         1
         1
         1
         1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "BCW2"
        [1
         1
         1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "BCE"
        [1
         1
         1
         1
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "I80_1" [1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1],
        "fish"
        [4842
         4843
         4844
         4858
         4861
         4857
         4862
         4850
         4845
         4855
         4859
         4848
         4847
         4865
         4849
         4851
         4854
         4863
         4864]}))


  (is (= (note-to-test/represent-value
          (def warpbreaks (tc/dataset "data/warpbreaks.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          warpbreaks)
       {"breaks"
        [26 30 54 25 70 52 51 26 67 18 21 29 17 12 18 35 30 36 36 21],
        "wool"
        ["A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"],
        "tension"
        ["L"
         "L"
         "L"
         "L"
         "L"
         "L"
         "L"
         "L"
         "L"
         "M"
         "M"
         "M"
         "M"
         "M"
         "M"
         "M"
         "M"
         "M"
         "H"
         "H"]}))


  (is (= (note-to-test/represent-value
          (-> warpbreaks
              (tc/group-by ["wool" "tension"])
              (tc/aggregate {:n tc/row-count})))
       {"wool" ["A" "A" "A" "B" "B" "B"],
        "tension" ["L" "M" "H" "L" "M" "H"],
        :n [9 9 9 9 9 9]}))

  (is (= (note-to-test/represent-value
          (-> warpbreaks
              (tc/reorder-columns ["wool" "tension" "breaks"])
              (tc/pivot->wider "wool" "breaks" {:fold-fn vec})))
       {"tension" ["L" "M" "H"],
        "A"
        [[26 30 54 25 70 52 51 26 67]
         [18 21 29 17 12 18 35 30 36]
         [36 21 24 18 10 43 28 15 26]],
        "B"
        [[27 14 29 19 29 31 41 20 44]
         [42 26 19 16 39 28 21 39 29]
         [20 21 24 17 13 15 15 16 28]]}))


  (is (= (note-to-test/represent-value
          (-> warpbreaks
              (tc/reorder-columns ["wool" "tension" "breaks"])
              (tc/pivot->wider "wool" "breaks" {:fold-fn tech.v3.datatype.functional/mean})))
       {"tension" ["L" "M" "H"],
        "A" [44.55555555555556 24.0 24.555555555555557],
        "B" [28.22222222222222 28.77777777777778 18.77777777777778]}))


  (is (= (note-to-test/represent-value
          (def production (tc/dataset "data/production.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          production)
       {"product"
        ["A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "A"
         "B"
         "B"
         "B"
         "B"
         "B"],
        "country"
        ["AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"
         "AI"],
        "year"
        [2000
         2001
         2002
         2003
         2004
         2005
         2006
         2007
         2008
         2009
         2010
         2011
         2012
         2013
         2014
         2000
         2001
         2002
         2003
         2004],
        "production"
        [1.6372715774122846
         0.15870783986040046
         -1.567977450807575
         -0.44455509350133854
         -0.07133701045735727
         1.6118308960119823
         -0.7043468225867656
         -1.5355054163278994
         0.8390715457273077
         -0.3742411001291045
         -0.7115892568951634
         1.1280563436678663
         1.4571824668730426
         -1.5593410121937417
         -0.11695838002114005
         -0.026176611940051428
         -0.6886357642826394
         0.0624874105489952
         -0.723396863031015
         0.47248951964453134]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->wider production ["product" "country"] "production"))
       {"year"
        [2000
         2001
         2002
         2003
         2004
         2005
         2006
         2007
         2008
         2009
         2010
         2011
         2012
         2013
         2014],
        "A_AI"
        [1.6372715774122846
         0.15870783986040046
         -1.567977450807575
         -0.44455509350133854
         -0.07133701045735727
         1.6118308960119823
         -0.7043468225867656
         -1.5355054163278994
         0.8390715457273077
         -0.3742411001291045
         -0.7115892568951634
         1.1280563436678663
         1.4571824668730426
         -1.5593410121937417
         -0.11695838002114005],
        "B_AI"
        [-0.026176611940051428
         -0.6886357642826394
         0.0624874105489952
         -0.723396863031015
         0.47248951964453134
         -0.9417386106260295
         -0.3478210750446564
         0.5242528380610967
         1.8323093743778909
         0.10706490503775681
         -0.329036635089033
         -1.7831912123478162
         0.6112579805886741
         -0.7852609221892832
         0.9784363513590328],
        "B_EI"
        [1.404708477530438
         -0.5961836879930892
         -0.26568578637154766
         0.6525780779508413
         0.6256499904976253
         -1.3453029907000869
         -0.9718497486278521
         -1.697158206855015
         0.045561282626706424
         1.1931504259618515
         -1.6055750319322417
         -0.7723549690075867
         -2.502627383821918
         -1.6275376866923248
         0.0332964450957834]}))


  (is (= (note-to-test/represent-value
          (tc/pivot->wider production ["product" "country"] "production" {:concat-columns-with vec}))
       {"year"
        [2000
         2001
         2002
         2003
         2004
         2005
         2006
         2007
         2008
         2009
         2010
         2011
         2012
         2013
         2014],
        ["A" "AI"]
        [1.6372715774122846
         0.15870783986040046
         -1.567977450807575
         -0.44455509350133854
         -0.07133701045735727
         1.6118308960119823
         -0.7043468225867656
         -1.5355054163278994
         0.8390715457273077
         -0.3742411001291045
         -0.7115892568951634
         1.1280563436678663
         1.4571824668730426
         -1.5593410121937417
         -0.11695838002114005],
        ["B" "AI"]
        [-0.026176611940051428
         -0.6886357642826394
         0.0624874105489952
         -0.723396863031015
         0.47248951964453134
         -0.9417386106260295
         -0.3478210750446564
         0.5242528380610967
         1.8323093743778909
         0.10706490503775681
         -0.329036635089033
         -1.7831912123478162
         0.6112579805886741
         -0.7852609221892832
         0.9784363513590328],
        ["B" "EI"]
        [1.404708477530438
         -0.5961836879930892
         -0.26568578637154766
         0.6525780779508413
         0.6256499904976253
         -1.3453029907000869
         -0.9718497486278521
         -1.697158206855015
         0.045561282626706424
         1.1931504259618515
         -1.6055750319322417
         -0.7723549690075867
         -2.502627383821918
         -1.6275376866923248
         0.0332964450957834]}))


  (is (= (note-to-test/represent-value
          (def income (tc/dataset "data/us_rent_income.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          income)
       {"GEOID" [1 1 2 2 4 4 5 5 6 6 8 8 9 9 10 10 11 11 12 12],
        "NAME"
        ["Alabama"
         "Alabama"
         "Alaska"
         "Alaska"
         "Arizona"
         "Arizona"
         "Arkansas"
         "Arkansas"
         "California"
         "California"
         "Colorado"
         "Colorado"
         "Connecticut"
         "Connecticut"
         "Delaware"
         "Delaware"
         "District of Columbia"
         "District of Columbia"
         "Florida"
         "Florida"],
        "variable"
        ["income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"
         "income"
         "rent"],
        "estimate"
        [24476
         747
         32940
         1200
         27517
         972
         23789
         709
         29454
         1358
         32401
         1125
         35326
         1123
         31560
         1076
         43198
         1424
         25952
         1077],
        "moe" [136 3 508 13 148 4 165 5 109 3 109 5 195 5 247 10 681 17 70 3]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->wider income "variable" ["estimate" "moe"] {:drop-missing? false}))
       {"GEOID" [1 2 4 5 6 8 9 10 11 12 13 15 16 17 18 19 20 21 22 23],
        "NAME"
        ["Alabama"
         "Alaska"
         "Arizona"
         "Arkansas"
         "California"
         "Colorado"
         "Connecticut"
         "Delaware"
         "District of Columbia"
         "Florida"
         "Georgia"
         "Hawaii"
         "Idaho"
         "Illinois"
         "Indiana"
         "Iowa"
         "Kansas"
         "Kentucky"
         "Louisiana"
         "Maine"],
        "income-estimate"
        [24476
         32940
         27517
         23789
         29454
         32401
         35326
         31560
         43198
         25952
         27024
         32453
         25298
         30684
         27247
         30002
         29126
         24702
         25086
         26841],
        "income-moe"
        [136
         508
         148
         165
         109
         109
         195
         247
         681
         70
         106
         218
         208
         83
         117
         143
         208
         159
         155
         187],
        "rent-estimate"
        [747
         1200
         972
         709
         1358
         1125
         1123
         1076
         1424
         1077
         927
         1507
         792
         952
         782
         740
         801
         713
         825
         808],
        "rent-moe" [3 13 4 5 3 5 5 10 17 3 3 18 7 3 3 4 5 4 4 7]}))


  (is (= (note-to-test/represent-value
          (tc/pivot->wider income "variable" ["estimate" "moe"] {:concat-columns-with vec
                                                                  :concat-value-with vector
                                                                  :drop-missing? false}))
       {"GEOID" [1 2 4 5 6 8 9 10 11 12 13 15 16 17 18 19 20 21 22 23],
        "NAME"
        ["Alabama"
         "Alaska"
         "Arizona"
         "Arkansas"
         "California"
         "Colorado"
         "Connecticut"
         "Delaware"
         "District of Columbia"
         "Florida"
         "Georgia"
         "Hawaii"
         "Idaho"
         "Illinois"
         "Indiana"
         "Iowa"
         "Kansas"
         "Kentucky"
         "Louisiana"
         "Maine"],
        ["income" "estimate"]
        [24476
         32940
         27517
         23789
         29454
         32401
         35326
         31560
         43198
         25952
         27024
         32453
         25298
         30684
         27247
         30002
         29126
         24702
         25086
         26841],
        ["income" "moe"]
        [136
         508
         148
         165
         109
         109
         195
         247
         681
         70
         106
         218
         208
         83
         117
         143
         208
         159
         155
         187],
        ["rent" "estimate"]
        [747
         1200
         972
         709
         1358
         1125
         1123
         1076
         1424
         1077
         927
         1507
         792
         952
         782
         740
         801
         713
         825
         808],
        ["rent" "moe"] [3 13 4 5 3 5 5 10 17 3 3 18 7 3 3 4 5 4 4 7]}))


  (is (= (note-to-test/represent-value
          (def contacts (tc/dataset "data/contacts.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          contacts)
       {"field" ["name" "company" "name" "company" "email" "name"],
        "value"
        ["Jiena McLellan"
         "Toyota"
         "John Smith"
         "google"
         "john@google.com"
         "Huxley Ratcliffe"],
        "person_id" [1 1 2 2 2 3]}))

  (is (= (note-to-test/represent-value
          (tc/pivot->wider contacts "field" "value" {:drop-missing? false}))
       {"person_id" [2 1 3],
        "name" ["John Smith" "Jiena McLellan" "Huxley Ratcliffe"],
        "company" ["google" "Toyota" :var-or-nil],
        "email" ["john@google.com" :var-or-nil :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (def world-bank-pop (tc/dataset "data/world_bank_pop.csv.gz")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (->> world-bank-pop
               (tc/column-names)
               (take 8)
               (tc/select-columns world-bank-pop)))
       {"country"
        ["ABW"
         "ABW"
         "ABW"
         "ABW"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AGO"
         "AGO"
         "AGO"
         "AGO"
         "ALB"
         "ALB"
         "ALB"
         "ALB"
         "AND"
         "AND"
         "AND"
         "AND"],
        "indicator"
        ["SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"],
        "2000"
        [42444.0
         1.18263237130859
         90853.0
         2.0550267831539
         4436299.0
         3.91222845961752
         2.0093756E7
         3.49465873827217
         8234766.0
         5.43749411195487
         1.6440924E7
         3.03294341513484
         1289391.0
         0.742478629285177
         3089027.0
         -0.637356833943492
         60417.0
         1.27931383487801
         65390.0
         1.57216555086599],
        "2001"
        [43048.0
         1.41302121757747
         92898.0
         2.22593013018195
         4648055.0
         4.66283822436107
         2.0966463E7
         4.25150411214678
         8708000.0
         5.58771954469661
         1.6983266E7
         3.24549138933295
         1298584.0
         0.710442617598151
         3060173.0
         -0.93847042771206
         61991.0
         2.57186909250154
         67341.0
         2.93999220643334],
        "2002"
        [43670.0
         1.4345595310351
         94992.0
         2.22905604655647
         4892951.0
         5.13467454246197
         2.1979923E7
         4.72052846249987
         9218787.0
         5.700132371906
         1.7572649E7
         3.41151528682319
         1327220.0
         2.18120890441126
         3051010.0
         -0.299876697084691
         64194.0
         3.49205351666753
         70049.0
         3.94257335261145],
        "2003"
        [44246.0
         1.31036043903253
         97017.0
         2.10935433511798
         5155686.0
         5.23045853493535
         2.3064851E7
         4.81804111999567
         9765197.0
         5.75812711249289
         1.8203369E7
         3.52630276523875
         1354848.0
         2.06027418181457
         3039616.0
         -0.374149169291299
         66747.0
         3.8999604065606
         73182.0
         4.37544919255239],
        "2004"
        [44669.0
         0.951477683657806
         98737.0
         1.75735286660729
         5426770.0
         5.12439301939892
         2.4118979E7
         4.46891839618783
         1.0343506E7
         5.75341449589518
         1.8865716E7
         3.57396197367573
         1381828.0
         1.97179893742068
         3026939.0
         -0.417931377925999
         69192.0
         3.59758965540803
         76244.0
         4.09892347776702],
        "2005"
        [44889.0
         0.491302714501917
         100031.0
         1.30203884024471
         5691823.0
         4.76864699745982
         2.5070798E7
         3.8704701590704
         1.0949424E7
         5.69279689639696
         1.9552542E7
         3.57589969753347
         1407298.0
         1.82642935665107
         3011487.0
         -0.511790116421897
         71205.0
         2.86777916671802
         78867.0
         3.38241655431066]}))


  (is (= (note-to-test/represent-value
          (def pop2 (tc/pivot->longer world-bank-pop (map str (range 2000 2018)) {:drop-missing? false
                                                                                   :target-columns ["year"]
                                                                                   :value-column-name "value"})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          pop2)
       {"country"
        ["ABW"
         "ABW"
         "ABW"
         "ABW"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AGO"
         "AGO"
         "AGO"
         "AGO"
         "ALB"
         "ALB"
         "ALB"
         "ALB"
         "AND"
         "AND"
         "AND"
         "AND"],
        "indicator"
        ["SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"
         "SP.URB.TOTL"
         "SP.URB.GROW"
         "SP.POP.TOTL"
         "SP.POP.GROW"],
        "year"
        [2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013],
        "value"
        [44360.0
         0.66950399359219
         103187.0
         0.592914005394165
         7733964.0
         4.19297967082464
         3.1731688E7
         3.3152241309788
         1.6119491E7
         4.72272270014104
         2.599834E7
         3.53182419107337
         1603505.0
         1.74363937043308
         2895092.0
         -0.183211384606402
         71527.0
         -2.11923330970293
         80788.0
         -2.01331401109279]}))


  (is (= (note-to-test/represent-value
          (def pop3 (tc/separate-column pop2
                                         "indicator" ["area" "variable"]
                                         #(rest (clojure.string/split % #"\.")))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          pop3)
       {"country"
        ["ABW"
         "ABW"
         "ABW"
         "ABW"
         "AFG"
         "AFG"
         "AFG"
         "AFG"
         "AGO"
         "AGO"
         "AGO"
         "AGO"
         "ALB"
         "ALB"
         "ALB"
         "ALB"
         "AND"
         "AND"
         "AND"
         "AND"],
        "area"
        ["URB"
         "URB"
         "POP"
         "POP"
         "URB"
         "URB"
         "POP"
         "POP"
         "URB"
         "URB"
         "POP"
         "POP"
         "URB"
         "URB"
         "POP"
         "POP"
         "URB"
         "URB"
         "POP"
         "POP"],
        "variable"
        ["TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"
         "TOTL"
         "GROW"],
        "year"
        [2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013],
        "value"
        [44360.0
         0.66950399359219
         103187.0
         0.592914005394165
         7733964.0
         4.19297967082464
         3.1731688E7
         3.3152241309788
         1.6119491E7
         4.72272270014104
         2.599834E7
         3.53182419107337
         1603505.0
         1.74363937043308
         2895092.0
         -0.183211384606402
         71527.0
         -2.11923330970293
         80788.0
         -2.01331401109279]}))


  (is (= (note-to-test/represent-value
          (tc/pivot->wider pop3 "variable" "value" {:drop-missing? false}))
       {"country"
        ["ABW"
         "ABW"
         "AFG"
         "AFG"
         "AGO"
         "AGO"
         "ALB"
         "ALB"
         "AND"
         "AND"
         "ARB"
         "ARB"
         "ARE"
         "ARE"
         "ARG"
         "ARG"
         "ARM"
         "ARM"
         "ASM"
         "ASM"],
        "area"
        ["URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"
         "URB"
         "POP"],
        "year"
        [2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013
         2013],
        "TOTL"
        [44360.0
         103187.0
         7733964.0
         3.1731688E7
         1.6119491E7
         2.599834E7
         1603505.0
         2895092.0
         71527.0
         80788.0
         2.18605128E8
         3.81702086E8
         7661268.0
         9006263.0
         3.8817256E7
         4.2539925E7
         1827656.0
         2893509.0
         48310.0
         55307.0],
        "GROW"
        [0.66950399359219
         0.592914005394165
         4.19297967082464
         3.3152241309788
         4.72272270014104
         3.53182419107337
         1.74363937043308
         -0.183211384606402
         -2.11923330970293
         -2.01331401109279
         2.78289394614211
         2.2488442910042
         1.55515586512934
         1.18180498875127
         1.18764912603333
         1.04727674680202
         0.281027190997132
         0.401251977426153
         0.0579758174264564
         0.139319888281354]}))


  (is (= (note-to-test/represent-value
          (def multi (tc/dataset {:id [1 2 3 4]
                                   :choice1 ["A" "C" "D" "B"]
                                   :choice2 ["B" "B" nil "D"]
                                   :choice3 ["C" nil nil nil]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          multi)
       {:id [1 2 3 4],
        :choice1 ["A" "C" "D" "B"],
        :choice2 ["B" "B" :var-or-nil "D"],
        :choice3 ["C" :var-or-nil :var-or-nil :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (def multi2 (-> multi
                          (tc/pivot->longer (complement #{:id}))
                          (tc/add-column :checked true))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          multi2)
       {:id [1 2 3 4 1 2 4 1],
        :$column
        [:choice1
         :choice1
         :choice1
         :choice1
         :choice2
         :choice2
         :choice2
         :choice3],
        :$value ["A" "C" "D" "B" "B" "B" "D" "C"],
        :checked [true true true true true true true true]}))


  (is (= (note-to-test/represent-value
          (-> multi2
              (tc/drop-columns :$column)
              (tc/pivot->wider :$value :checked {:drop-missing? false})
              (tc/order-by :id)))
       {:id [1 2 3 4],
        "A" [true :var-or-nil :var-or-nil :var-or-nil],
        "C" [true true :var-or-nil :var-or-nil],
        "D" [:var-or-nil :var-or-nil true true],
        "B" [true true :var-or-nil true]}))


  (is (= (note-to-test/represent-value
          (def construction (tc/dataset "data/construction.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def construction-unit-map {"1 unit" "1"
                                      "2 to 4 units" "2-4"
                                      "5 units or more" "5+"}))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          construction)
       {"West" [339 336 330 304 319 360 310 286 296],
        "South" [596 655 595 613 673 610 594 649 560],
        "Northeast" [114 138 150 144 90 76 108 90 117],
        "1 unit" [859 882 862 797 875 867 829 939 835],
        "Midwest" [169 160 154 196 169 170 183 205 175],
        "2 to 4 units"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "Year" [2018 2018 2018 2018 2018 2018 2018 2018 2018],
        "5 units or more" [348 400 356 447 364 342 360 286 304],
        "Month"
        ["January"
         "February"
         "March"
         "April"
         "May"
         "June"
         "July"
         "August"
         "September"]}))


  (is (= (note-to-test/represent-value
          (-> construction
              (tc/pivot->longer #"^[125NWS].*|Midwest" {:target-columns [:units :region]
                                                         :splitter (fn [col-name]
                                                                     (if (re-matches #"^[125].*" col-name)
                                                                       [(construction-unit-map col-name) nil]
                                                                       [nil col-name]))
                                                         :value-column-name :n
                                                         :drop-missing? false})))
       {"Year"
        [2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018
         2018],
        "Month"
        ["January"
         "February"
         "March"
         "April"
         "May"
         "June"
         "July"
         "August"
         "September"
         "January"
         "February"
         "March"
         "April"
         "May"
         "June"
         "July"
         "August"
         "September"
         "January"
         "February"],
        :units
        [1
         1
         1
         1
         1
         1
         1
         1
         1
         "2-4"
         "2-4"
         "2-4"
         "2-4"
         "2-4"
         "2-4"
         "2-4"
         "2-4"
         "2-4"
         "5+"
         "5+"],
        :region
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :n
        [859
         882
         862
         797
         875
         867
         829
         939
         835
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         348
         400]}))


  (is (= (note-to-test/represent-value
          (-> construction
              (tc/pivot->longer #"^[125NWS].*|Midwest" {:target-columns [:units :region]
                                                         :splitter (fn [col-name]
                                                                     (if (re-matches #"^[125].*" col-name)
                                                                       [(construction-unit-map col-name) nil]
                                                                       [nil col-name]))
                                                         :value-column-name :n
                                                         :drop-missing? false})
              (tc/pivot->wider [:units :region] :n {:drop-missing? false})
              (tc/rename-columns (zipmap (vals construction-unit-map)
                                          (keys construction-unit-map)))))
       {"West" [339 336 330 304 319 360 310 286 296],
        1 [859 882 862 797 875 867 829 939 835],
        "South" [596 655 595 613 673 610 594 649 560],
        "Northeast" [114 138 150 144 90 76 108 90 117],
        "Midwest" [169 160 154 196 169 170 183 205 175],
        "2 to 4 units"
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        "Year" [2018 2018 2018 2018 2018 2018 2018 2018 2018],
        "5 units or more" [348 400 356 447 364 342 360 286 304],
        "Month"
        ["January"
         "February"
         "March"
         "April"
         "May"
         "June"
         "July"
         "August"
         "September"]}))


  (is (= (note-to-test/represent-value
          (def stocks-tidyr (tc/dataset "data/stockstidyr.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          stocks-tidyr)
       {"time"
        [[java.time.LocalDate "2009-01-01"]
         [java.time.LocalDate "2009-01-02"]
         [java.time.LocalDate "2009-01-03"]
         [java.time.LocalDate "2009-01-04"]
         [java.time.LocalDate "2009-01-05"]
         [java.time.LocalDate "2009-01-06"]
         [java.time.LocalDate "2009-01-07"]
         [java.time.LocalDate "2009-01-08"]
         [java.time.LocalDate "2009-01-09"]
         [java.time.LocalDate "2009-01-10"]],
        "X"
        [1.3098980569694743
         -0.299938042403242
         0.536475012262886
         -1.8839080177420178
         -0.9605236142913735
         -1.1852896576065972
         -0.8520705598024025
         0.2523417204877737
         0.40257136394542437
         -0.6438350002601568],
        "Y"
        [-1.8904019256298135
         -1.8247309017308997
         -1.0360685974515387
         -0.5217838968579932
         -2.2168334921282327
         -2.8935092410204866
         -2.167948176956802
         -0.3285411651231233
         1.964078984798918
         2.6861838168176297],
        "Z"
        [-1.7794688023227476
         2.398925133113737
         -3.98697977071294
         -2.8306548990407485
         1.4371517117119361
         3.3978414042157543
         -1.2010825844501143
         -1.5316047270579687
         -6.808788298292381
         -2.559093207111715]}))


  (is (= (note-to-test/represent-value
          (def stocks-long (tc/pivot->longer stocks-tidyr ["X" "Y" "Z"] {:value-column-name :price
                                                                          :target-columns :stocks})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          stocks-long)
       {"time"
        [[java.time.LocalDate "2009-01-01"]
         [java.time.LocalDate "2009-01-02"]
         [java.time.LocalDate "2009-01-03"]
         [java.time.LocalDate "2009-01-04"]
         [java.time.LocalDate "2009-01-05"]
         [java.time.LocalDate "2009-01-06"]
         [java.time.LocalDate "2009-01-07"]
         [java.time.LocalDate "2009-01-08"]
         [java.time.LocalDate "2009-01-09"]
         [java.time.LocalDate "2009-01-10"]
         [java.time.LocalDate "2009-01-01"]
         [java.time.LocalDate "2009-01-02"]
         [java.time.LocalDate "2009-01-03"]
         [java.time.LocalDate "2009-01-04"]
         [java.time.LocalDate "2009-01-05"]
         [java.time.LocalDate "2009-01-06"]
         [java.time.LocalDate "2009-01-07"]
         [java.time.LocalDate "2009-01-08"]
         [java.time.LocalDate "2009-01-09"]
         [java.time.LocalDate "2009-01-10"]],
        :stocks
        ["X"
         "X"
         "X"
         "X"
         "X"
         "X"
         "X"
         "X"
         "X"
         "X"
         "Y"
         "Y"
         "Y"
         "Y"
         "Y"
         "Y"
         "Y"
         "Y"
         "Y"
         "Y"],
        :price
        [1.3098980569694743
         -0.299938042403242
         0.536475012262886
         -1.8839080177420178
         -0.9605236142913735
         -1.1852896576065972
         -0.8520705598024025
         0.2523417204877737
         0.40257136394542437
         -0.6438350002601568
         -1.8904019256298135
         -1.8247309017308997
         -1.0360685974515387
         -0.5217838968579932
         -2.2168334921282327
         -2.8935092410204866
         -2.167948176956802
         -0.3285411651231233
         1.964078984798918
         2.6861838168176297]}))


  (is (= (note-to-test/represent-value
          (tc/pivot->wider stocks-long :stocks :price))
       {"time"
        [[java.time.LocalDate "2009-01-01"]
         [java.time.LocalDate "2009-01-02"]
         [java.time.LocalDate "2009-01-03"]
         [java.time.LocalDate "2009-01-04"]
         [java.time.LocalDate "2009-01-05"]
         [java.time.LocalDate "2009-01-06"]
         [java.time.LocalDate "2009-01-07"]
         [java.time.LocalDate "2009-01-08"]
         [java.time.LocalDate "2009-01-09"]
         [java.time.LocalDate "2009-01-10"]],
        "X"
        [1.3098980569694743
         -0.299938042403242
         0.536475012262886
         -1.8839080177420178
         -0.9605236142913735
         -1.1852896576065972
         -0.8520705598024025
         0.2523417204877737
         0.40257136394542437
         -0.6438350002601568],
        "Y"
        [-1.8904019256298135
         -1.8247309017308997
         -1.0360685974515387
         -0.5217838968579932
         -2.2168334921282327
         -2.8935092410204866
         -2.167948176956802
         -0.3285411651231233
         1.964078984798918
         2.6861838168176297],
        "Z"
        [-1.7794688023227476
         2.398925133113737
         -3.98697977071294
         -2.8306548990407485
         1.4371517117119361
         3.3978414042157543
         -1.2010825844501143
         -1.5316047270579687
         -6.808788298292381
         -2.559093207111715]}))


  (is (= (note-to-test/represent-value
          (-> stocks-long
              (tc/select-rows (range 0 30 4))
              (tc/pivot->wider "time" :price {:drop-missing? false})))
       {:stocks ["Y" "X" "Z"],
        [java.time.LocalDate "2009-01-01"]
        [:var-or-nil 1.3098980569694743 -1.7794688023227476],
        [java.time.LocalDate "2009-01-05"]
        [:var-or-nil -0.9605236142913735 1.4371517117119361],
        [java.time.LocalDate "2009-01-09"]
        [:var-or-nil 0.40257136394542437 -6.808788298292381],
        [java.time.LocalDate "2009-01-03"]
        [-1.0360685974515387 :var-or-nil :var-or-nil],
        [java.time.LocalDate "2009-01-07"]
        [-2.167948176956802 :var-or-nil :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (def ds1 (tc/dataset {:a [1 2 1 2 3 4 nil nil 4]
                                 :b (range 101 110)
                                 :c (map str "abs tract")})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def ds2 (tc/dataset {:a [nil 1 2 5 4 3 2 1 nil]
                                :b (range 110 101 -1)
                                :c (map str "datatable")
                                :d (symbol "X")
                                :e [3 4 5 6 7 nil 8 1 1]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          ds1)
       {:a [1 2 1 2 3 4 :var-or-nil :var-or-nil 4],
        :b [101 102 103 104 105 106 107 108 109],
        :c ["a" "b" "s" " " "t" "r" "a" "c" "t"]}))

  (is (= (note-to-test/represent-value
          ds2)
       {:a [:var-or-nil 1 2 5 4 3 2 1 :var-or-nil],
        :b [110 109 108 107 106 105 104 103 102],
        :c ["d" "a" "t" "a" "t" "a" "b" "l" "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [3 4 5 6 7 :var-or-nil 8 1 1]}))


  (is (= (note-to-test/represent-value
          (tc/left-join ds1 ds2 :b))
       {:b [109 108 107 106 105 104 103 102 101],
        :a [4 :var-or-nil :var-or-nil 4 3 2 1 2 1],
        :c ["t" "c" "a" "r" "t" " " "s" "b" "a"],
        :right.b [109 108 107 106 105 104 103 102 :var-or-nil],
        :right.a [1 2 5 4 3 2 1 :var-or-nil :var-or-nil],
        :right.c ["a" "t" "a" "t" "a" "b" "l" "e" :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil],
        :e [4 5 6 7 :var-or-nil 8 1 1 :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/left-join ds2 ds1 :b))
       {:b [102 103 104 105 106 107 108 109 110],
        :a [:var-or-nil 1 2 3 4 5 2 1 :var-or-nil],
        :c ["e" "l" "b" "a" "t" "a" "t" "a" "d"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [1 1 8 :var-or-nil 7 6 5 4 3],
        :right.b [102 103 104 105 106 107 108 109 :var-or-nil],
        :right.a [2 1 2 3 4 :var-or-nil :var-or-nil 4 :var-or-nil],
        :right.c ["b" "s" " " "t" "r" "a" "c" "t" :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/left-join ds1 ds2 [:a :b]))
       {:a [4 3 2 1 1 2 :var-or-nil :var-or-nil 4],
        :b [106 105 104 103 101 102 107 108 109],
        :c ["r" "t" " " "s" "a" "b" "a" "c" "t"],
        :right.a
        [4 3 2 1 :var-or-nil :var-or-nil :var-or-nil :var-or-nil :var-or-nil],
        :right.b
        [106
         105
         104
         103
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.c
        ["t"
         "a"
         "b"
         "l"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :e
        [7
         :var-or-nil
         8
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/left-join ds2 ds1 [:a :b]))
       {:a [1 2 3 4 :var-or-nil 1 2 5 :var-or-nil],
        :b [103 104 105 106 110 109 108 107 102],
        :c ["l" "b" "a" "t" "d" "a" "t" "a" "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [1 8 :var-or-nil 7 3 4 5 6 1],
        :right.a
        [1 2 3 4 :var-or-nil :var-or-nil :var-or-nil :var-or-nil :var-or-nil],
        :right.b
        [103
         104
         105
         106
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.c
        ["s"
         " "
         "t"
         "r"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/left-join ds1 ds2 {:left :a :right :e}))
       {:a [3 4 4 1 1 1 1 2 2 :var-or-nil :var-or-nil],
        :b [105 106 109 101 103 101 103 102 104 107 108],
        :c ["t" "r" "t" "a" "s" "a" "s" "b" " " "a" "c"],
        :e [3 4 4 1 1 1 1 :var-or-nil :var-or-nil :var-or-nil :var-or-nil],
        :right.a
        [:var-or-nil
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.b
        [110
         109
         109
         103
         103
         102
         102
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.c
        ["d"
         "a"
         "a"
         "l"
         "l"
         "e"
         "e"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/left-join ds2 ds1 {:left :e :right :a}))
       {:e [1 1 1 1 3 4 4 5 6 7 :var-or-nil 8],
        :a [1 :var-or-nil 1 :var-or-nil :var-or-nil 1 1 2 5 4 3 2],
        :b [103 102 103 102 110 109 109 108 107 106 105 104],
        :c ["l" "e" "l" "e" "d" "a" "a" "t" "a" "t" "a" "b"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :right.a
        [1
         1
         1
         1
         3
         4
         4
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.b
        [101
         101
         103
         103
         105
         106
         109
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.c
        ["a"
         "a"
         "s"
         "s"
         "t"
         "r"
         "t"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/right-join ds1 ds2 :b))
       {:b [109 108 107 106 105 104 103 102 :var-or-nil],
        :a [4 :var-or-nil :var-or-nil 4 3 2 1 2 :var-or-nil],
        :c ["t" "c" "a" "r" "t" " " "s" "b" :var-or-nil],
        :right.b [109 108 107 106 105 104 103 102 110],
        :right.a [1 2 5 4 3 2 1 :var-or-nil :var-or-nil],
        :right.c ["a" "t" "a" "t" "a" "b" "l" "e" "d"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [4 5 6 7 :var-or-nil 8 1 1 3]}))


  (is (= (note-to-test/represent-value
          (tc/right-join ds2 ds1 :b))
       {:b [102 103 104 105 106 107 108 109 :var-or-nil],
        :a [:var-or-nil 1 2 3 4 5 2 1 :var-or-nil],
        :c ["e" "l" "b" "a" "t" "a" "t" "a" :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil],
        :e [1 1 8 :var-or-nil 7 6 5 4 :var-or-nil],
        :right.b [102 103 104 105 106 107 108 109 101],
        :right.a [2 1 2 3 4 :var-or-nil :var-or-nil 4 1],
        :right.c ["b" "s" " " "t" "r" "a" "c" "t" "a"]}))


  (is (= (note-to-test/represent-value
          (tc/right-join ds1 ds2 [:a :b]))
       {:a
        [4 3 2 1 :var-or-nil :var-or-nil :var-or-nil :var-or-nil :var-or-nil],
        :b
        [106
         105
         104
         103
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["r"
         "t"
         " "
         "s"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.a [4 3 2 1 :var-or-nil 1 2 5 :var-or-nil],
        :right.b [106 105 104 103 110 109 108 107 102],
        :right.c ["t" "a" "b" "l" "d" "a" "t" "a" "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [7 :var-or-nil 8 1 3 4 5 6 1]}))


  (is (= (note-to-test/represent-value
          (tc/right-join ds2 ds1 [:a :b]))
       {:a
        [1 2 3 4 :var-or-nil :var-or-nil :var-or-nil :var-or-nil :var-or-nil],
        :b
        [103
         104
         105
         106
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["l"
         "b"
         "a"
         "t"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :e
        [1
         8
         :var-or-nil
         7
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.a [1 2 3 4 1 2 :var-or-nil :var-or-nil 4],
        :right.b [103 104 105 106 101 102 107 108 109],
        :right.c ["s" " " "t" "r" "a" "b" "a" "c" "t"]}))


  (is (= (note-to-test/represent-value
          (tc/right-join ds1 ds2 {:left :a :right :e}))
       {:a
        [3
         4
         4
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :b
        [105
         106
         109
         101
         103
         101
         103
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["t"
         "r"
         "t"
         "a"
         "s"
         "a"
         "s"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :e [3 4 4 1 1 1 1 5 6 7 :var-or-nil 8],
        :right.a [:var-or-nil 1 1 1 1 :var-or-nil :var-or-nil 2 5 4 3 2],
        :right.b [110 109 109 103 103 102 102 108 107 106 105 104],
        :right.c ["d" "a" "a" "l" "l" "e" "e" "t" "a" "t" "a" "b"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]]}))


  (is (= (note-to-test/represent-value
          (tc/right-join ds2 ds1 {:left :e :right :a}))
       {:e [1 1 1 1 3 4 4 :var-or-nil :var-or-nil :var-or-nil :var-or-nil],
        :a
        [1
         :var-or-nil
         1
         :var-or-nil
         :var-or-nil
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :b
        [103
         102
         103
         102
         110
         109
         109
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["l"
         "e"
         "l"
         "e"
         "d"
         "a"
         "a"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.a [1 1 1 1 3 4 4 2 2 :var-or-nil :var-or-nil],
        :right.b [101 101 103 103 105 106 109 102 104 107 108],
        :right.c ["a" "a" "s" "s" "t" "r" "t" "b" " " "a" "c"]}))


  (is (= (note-to-test/represent-value
          (tc/inner-join ds1 ds2 :b))
       {:b [109 108 107 106 105 104 103 102],
        :a [4 :var-or-nil :var-or-nil 4 3 2 1 2],
        :c ["t" "c" "a" "r" "t" " " "s" "b"],
        :right.a [1 2 5 4 3 2 1 :var-or-nil],
        :right.c ["a" "t" "a" "t" "a" "b" "l" "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [4 5 6 7 :var-or-nil 8 1 1]}))


  (is (= (note-to-test/represent-value
          (tc/inner-join ds2 ds1 :b))
       {:b [102 103 104 105 106 107 108 109],
        :a [:var-or-nil 1 2 3 4 5 2 1],
        :c ["e" "l" "b" "a" "t" "a" "t" "a"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [1 1 8 :var-or-nil 7 6 5 4],
        :right.a [2 1 2 3 4 :var-or-nil :var-or-nil 4],
        :right.c ["b" "s" " " "t" "r" "a" "c" "t"]}))


  (is (= (note-to-test/represent-value
          (tc/inner-join ds1 ds2 [:a :b]))
       {:a [4 3 2 1],
        :b [106 105 104 103],
        :c ["r" "t" " " "s"],
        :right.a [4 3 2 1],
        :right.b [106 105 104 103],
        :right.c ["t" "a" "b" "l"],
        :d [[:symbol "X"] [:symbol "X"] [:symbol "X"] [:symbol "X"]],
        :e [7 :var-or-nil 8 1]}))


  (is (= (note-to-test/represent-value
          (tc/inner-join ds2 ds1 [:a :b]))
       {:a [1 2 3 4],
        :b [103 104 105 106],
        :c ["l" "b" "a" "t"],
        :d [[:symbol "X"] [:symbol "X"] [:symbol "X"] [:symbol "X"]],
        :e [1 8 :var-or-nil 7],
        :right.a [1 2 3 4],
        :right.b [103 104 105 106],
        :right.c ["s" " " "t" "r"]}))


  (is (= (note-to-test/represent-value
          (tc/inner-join ds1 ds2 {:left :a :right :e}))
       {:a [3 4 4 1 1 1 1],
        :b [105 106 109 101 103 101 103],
        :c ["t" "r" "t" "a" "s" "a" "s"],
        :right.a [:var-or-nil 1 1 1 1 :var-or-nil :var-or-nil],
        :right.b [110 109 109 103 103 102 102],
        :right.c ["d" "a" "a" "l" "l" "e" "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]]}))


  (is (= (note-to-test/represent-value
          (tc/inner-join ds2 ds1 {:left :e :right :a}))
       {:e [1 1 1 1 3 4 4],
        :a [1 :var-or-nil 1 :var-or-nil :var-or-nil 1 1],
        :b [103 102 103 102 110 109 109],
        :c ["l" "e" "l" "e" "d" "a" "a"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :right.b [101 101 103 103 105 106 109],
        :right.c ["a" "a" "s" "s" "t" "r" "t"]}))


  (is (= (note-to-test/represent-value
          (tc/full-join ds1 ds2 :b))
       {:b [109 108 107 106 105 104 103 102 101 :var-or-nil],
        :a [4 :var-or-nil :var-or-nil 4 3 2 1 2 1 :var-or-nil],
        :c ["t" "c" "a" "r" "t" " " "s" "b" "a" :var-or-nil],
        :right.b [109 108 107 106 105 104 103 102 :var-or-nil 110],
        :right.a [1 2 5 4 3 2 1 :var-or-nil :var-or-nil :var-or-nil],
        :right.c ["a" "t" "a" "t" "a" "b" "l" "e" :var-or-nil "d"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         [:symbol "X"]],
        :e [4 5 6 7 :var-or-nil 8 1 1 :var-or-nil 3]}))


  (is (= (note-to-test/represent-value
          (tc/full-join ds2 ds1 :b))
       {:b [102 103 104 105 106 107 108 109 110 :var-or-nil],
        :a [:var-or-nil 1 2 3 4 5 2 1 :var-or-nil :var-or-nil],
        :c ["e" "l" "b" "a" "t" "a" "t" "a" "d" :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil],
        :e [1 1 8 :var-or-nil 7 6 5 4 3 :var-or-nil],
        :right.b [102 103 104 105 106 107 108 109 :var-or-nil 101],
        :right.a [2 1 2 3 4 :var-or-nil :var-or-nil 4 :var-or-nil 1],
        :right.c ["b" "s" " " "t" "r" "a" "c" "t" :var-or-nil "a"]}))


  (is (= (note-to-test/represent-value
          (tc/full-join ds1 ds2 [:a :b]))
       {:a
        [4
         3
         2
         1
         1
         2
         :var-or-nil
         :var-or-nil
         4
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :b
        [106
         105
         104
         103
         101
         102
         107
         108
         109
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["r"
         "t"
         " "
         "s"
         "a"
         "b"
         "a"
         "c"
         "t"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.a
        [4
         3
         2
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         1
         2
         5
         :var-or-nil],
        :right.b
        [106
         105
         104
         103
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         110
         109
         108
         107
         102],
        :right.c
        ["t"
         "a"
         "b"
         "l"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         "d"
         "a"
         "t"
         "a"
         "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e
        [7
         :var-or-nil
         8
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         3
         4
         5
         6
         1]}))


  (is (= (note-to-test/represent-value
          (tc/full-join ds2 ds1 [:a :b]))
       {:a
        [1
         2
         3
         4
         :var-or-nil
         1
         2
         5
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :b
        [103
         104
         105
         106
         110
         109
         108
         107
         102
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["l"
         "b"
         "a"
         "t"
         "d"
         "a"
         "t"
         "a"
         "e"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :e
        [1
         8
         :var-or-nil
         7
         3
         4
         5
         6
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.a
        [1
         2
         3
         4
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         1
         2
         :var-or-nil
         :var-or-nil
         4],
        :right.b
        [103
         104
         105
         106
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         101
         102
         107
         108
         109],
        :right.c
        ["s"
         " "
         "t"
         "r"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         "a"
         "b"
         "a"
         "c"
         "t"]}))


  (is (= (note-to-test/represent-value
          (tc/full-join ds1 ds2 {:left :a :right :e}))
       {:a
        [3
         4
         4
         1
         1
         1
         1
         2
         2
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :b
        [105
         106
         109
         101
         103
         101
         103
         102
         104
         107
         108
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["t"
         "r"
         "t"
         "a"
         "s"
         "a"
         "s"
         "b"
         " "
         "a"
         "c"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :e
        [3
         4
         4
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         5
         6
         7
         :var-or-nil
         8],
        :right.a
        [:var-or-nil
         1
         1
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         2
         5
         4
         3
         2],
        :right.b
        [110
         109
         109
         103
         103
         102
         102
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         108
         107
         106
         105
         104],
        :right.c
        ["d"
         "a"
         "a"
         "l"
         "l"
         "e"
         "e"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         "t"
         "a"
         "t"
         "a"
         "b"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]]}))


  (is (= (note-to-test/represent-value
          (tc/full-join ds2 ds1 {:left :e :right :a}))
       {:e
        [1
         1
         1
         1
         3
         4
         4
         5
         6
         7
         :var-or-nil
         8
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :a
        [1
         :var-or-nil
         1
         :var-or-nil
         :var-or-nil
         1
         1
         2
         5
         4
         3
         2
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :b
        [103
         102
         103
         102
         110
         109
         109
         108
         107
         106
         105
         104
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :c
        ["l"
         "e"
         "l"
         "e"
         "d"
         "a"
         "a"
         "t"
         "a"
         "t"
         "a"
         "b"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :right.a
        [1
         1
         1
         1
         3
         4
         4
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         2
         2
         :var-or-nil
         :var-or-nil],
        :right.b
        [101
         101
         103
         103
         105
         106
         109
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         102
         104
         107
         108],
        :right.c
        ["a"
         "a"
         "s"
         "s"
         "t"
         "r"
         "t"
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         "b"
         " "
         "a"
         "c"]}))


  (is (= (note-to-test/represent-value
          (tc/semi-join ds1 ds2 :b))
       {:b [109 108 107 106 105 104 103 102],
        :a [4 :var-or-nil :var-or-nil 4 3 2 1 2],
        :c ["t" "c" "a" "r" "t" " " "s" "b"]}))


  (is (= (note-to-test/represent-value
          (tc/semi-join ds2 ds1 :b))
       {:b [102 103 104 105 106 107 108 109],
        :a [:var-or-nil 1 2 3 4 5 2 1],
        :c ["e" "l" "b" "a" "t" "a" "t" "a"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [1 1 8 :var-or-nil 7 6 5 4]}))


  (is (= (note-to-test/represent-value
          (tc/semi-join ds1 ds2 [:a :b]))
       {:a [4 3 2 1], :b [106 105 104 103], :c ["r" "t" " " "s"]}))


  (is (= (note-to-test/represent-value
          (tc/semi-join ds2 ds1 [:a :b]))
       {:a [1 2 3 4],
        :b [103 104 105 106],
        :c ["l" "b" "a" "t"],
        :d [[:symbol "X"] [:symbol "X"] [:symbol "X"] [:symbol "X"]],
        :e [1 8 :var-or-nil 7]}))


  (is (= (note-to-test/represent-value
          (tc/semi-join ds1 ds2 {:left :a :right :e}))
       {:a [3 4 4 1 1 :var-or-nil :var-or-nil],
        :b [105 106 109 101 103 107 108],
        :c ["t" "r" "t" "a" "s" "a" "c"]}))


  (is (= (note-to-test/represent-value
          (tc/semi-join ds2 ds1 {:left :e :right :a}))
       {:e [1 1 3 4 :var-or-nil],
        :a [1 :var-or-nil :var-or-nil 1 3],
        :b [103 102 110 109 105],
        :c ["l" "e" "d" "a" "a"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]]}))


  (is (= (note-to-test/represent-value
          (tc/anti-join ds1 ds2 :b))
       {:b [101], :a [1], :c ["a"]}))


  (is (= (note-to-test/represent-value
          (tc/anti-join ds2 ds1 :b))
       {:b [110], :a [:var-or-nil], :c ["d"], :d [[:symbol "X"]], :e [3]}))


  (is (= (note-to-test/represent-value
          (tc/anti-join ds1 ds2 [:a :b]))
       {:a [1 2 :var-or-nil :var-or-nil 4],
        :b [101 102 107 108 109],
        :c ["a" "b" "a" "c" "t"]}))


  (is (= (note-to-test/represent-value
          (tc/anti-join ds1 ds2 {:left :a :right :e}))
       {:a [2 2], :b [102 104], :c ["b" " "]}))


  (is (= (note-to-test/represent-value
          (tc/anti-join ds2 ds1 {:left :e :right :a}))
       {:e [5 6 7 8],
        :a [2 5 4 2],
        :b [108 107 106 104],
        :c ["t" "a" "t" "b"],
        :d [[:symbol "X"] [:symbol "X"] [:symbol "X"] [:symbol "X"]]}))


  (is (= (note-to-test/represent-value
          (tc/left-join ds1 ds2 :b {:hashing (fn [[v]] (mod v 5))}))
       {:a
        [3
         2
         4
         1
         :var-or-nil
         2
         :var-or-nil
         1
         4
         3
         2
         4
         1
         :var-or-nil
         2
         :var-or-nil],
        :b [105 104 109 103 108 102 107 101 106 105 104 109 103 108 102 107],
        :c ["t" " " "t" "s" "c" "b" "a" "a" "r" "t" " " "t" "s" "c" "b" "a"],
        :right.a
        [:var-or-nil 1 1 2 2 5 5 4 4 3 2 2 1 1 :var-or-nil :var-or-nil],
        :right.b
        [110 109 109 108 108 107 107 106 106 105 104 104 103 103 102 102],
        :right.c
        ["d" "a" "a" "t" "t" "a" "a" "t" "t" "a" "b" "b" "l" "l" "e" "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [3 4 4 5 5 6 6 7 7 :var-or-nil 8 8 1 1 1 1]}))


  (is (= (note-to-test/represent-value
          (tc/cross-join ds1 ds2 [:a :b]))
       {:a [1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 1 1],
        :b
        [101
         101
         101
         101
         101
         101
         101
         101
         101
         102
         102
         102
         102
         102
         102
         102
         102
         102
         103
         103],
        :right.a
        [:var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil
         :var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil
         :var-or-nil
         1],
        :right.b
        [110
         109
         108
         107
         106
         105
         104
         103
         102
         110
         109
         108
         107
         106
         105
         104
         103
         102
         110
         109]}))


  (is (= (note-to-test/represent-value
          (tc/cross-join ds1 ds2 {:left [:a :b] :right :e}))
       {:a [1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 1 1],
        :b
        [101
         101
         101
         101
         101
         101
         101
         101
         101
         102
         102
         102
         102
         102
         102
         102
         102
         102
         103
         103],
        :e [3 4 5 6 7 :var-or-nil 8 1 1 3 4 5 6 7 :var-or-nil 8 1 1 3 4]}))


  (is (= (note-to-test/represent-value
          (tc/expand ds2 :a :c :d))
       {:a
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         1
         1
         1
         1
         1
         1
         2
         2
         2
         2
         2
         2
         5
         5],
        :c
        ["d"
         "a"
         "t"
         "b"
         "l"
         "e"
         "d"
         "a"
         "t"
         "b"
         "l"
         "e"
         "d"
         "a"
         "t"
         "b"
         "l"
         "e"
         "d"
         "a"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]]}))


  (is (= (note-to-test/represent-value
          (tc/expand ds2 [:a :c] [:e :b]))
       {:a
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         1
         1
         1
         1
         1
         1
         1
         1
         1
         2
         2],
        :c
        ["d"
         "d"
         "d"
         "d"
         "d"
         "d"
         "d"
         "d"
         "d"
         "a"
         "a"
         "a"
         "a"
         "a"
         "a"
         "a"
         "a"
         "a"
         "t"
         "t"],
        :e [3 4 5 6 7 :var-or-nil 8 1 1 3 4 5 6 7 :var-or-nil 8 1 1 3 4],
        :b
        [110
         109
         108
         107
         106
         105
         104
         103
         102
         110
         109
         108
         107
         106
         105
         104
         103
         102
         110
         109]}))


  (is (= (note-to-test/represent-value
          (tc/complete ds2 :a :c :d))
       {:a
        [:var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         1
         1
         1
         1
         2
         2
         2],
        :c
        ["d"
         "a"
         "t"
         "a"
         "t"
         "a"
         "b"
         "l"
         "e"
         "a"
         "t"
         "b"
         "l"
         "d"
         "t"
         "b"
         "e"
         "d"
         "a"
         "l"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :b
        [110
         109
         108
         107
         106
         105
         104
         103
         102
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :e
        [3
         4
         5
         6
         7
         :var-or-nil
         8
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/complete ds2 [:a :c] [:e :b]))
       {:a
        [:var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         1
         1
         1],
        :c
        ["d"
         "a"
         "t"
         "a"
         "t"
         "a"
         "b"
         "l"
         "e"
         "d"
         "d"
         "d"
         "d"
         "d"
         "d"
         "d"
         "d"
         "a"
         "a"
         "a"],
        :e [3 4 5 6 7 :var-or-nil 8 1 1 4 5 6 7 :var-or-nil 8 1 1 3 5 6],
        :b
        [110
         109
         108
         107
         106
         105
         104
         103
         102
         109
         108
         107
         106
         105
         104
         103
         102
         110
         108
         107],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (def left-ds (tc/dataset {:a [1 5 10]
                                    :left-val ["a" "b" "c"]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def right-ds (tc/dataset {:a [1 2 3 6 7]
                                     :right-val [:a :b :c :d :e]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          left-ds)
       {:a [1 5 10], :left-val ["a" "b" "c"]}))

  (is (= (note-to-test/represent-value
          right-ds)
       {:a [1 2 3 6 7], :right-val [:a :b :c :d :e]}))

  (is (= (note-to-test/represent-value
          (tc/asof-join left-ds right-ds :a))
       {:a [1 5 10],
        :left-val ["a" "b" "c"],
        :right.a [1 6 :var-or-nil],
        :right-val [:a :d :var-or-nil]}))

  (is (= (note-to-test/represent-value
          (tc/asof-join left-ds right-ds :a {:asof-op :nearest}))
       {:a [1 5 10],
        :left-val ["a" "b" "c"],
        :right.a [1 6 7],
        :right-val [:a :d :e]}))

  (is (= (note-to-test/represent-value
          (tc/asof-join left-ds right-ds :a {:asof-op :>=}))
       {:a [1 5 10],
        :left-val ["a" "b" "c"],
        :right.a [1 3 7],
        :right-val [:a :c :e]}))


  (is (= (note-to-test/represent-value
          (tc/concat ds1))
       {:a [1 2 1 2 3 4 :var-or-nil :var-or-nil 4],
        :b [101 102 103 104 105 106 107 108 109],
        :c ["a" "b" "s" " " "t" "r" "a" "c" "t"]}))


  (is (= (note-to-test/represent-value
          (tc/concat-copying ds1))
       {:a [1 2 1 2 3 4 :var-or-nil :var-or-nil 4],
        :b [101 102 103 104 105 106 107 108 109],
        :c ["a" "b" "s" " " "t" "r" "a" "c" "t"]}))


  (is (= (note-to-test/represent-value
          (tc/concat ds1 (tc/drop-columns ds2 :d)))
       {:a
        [1
         2
         1
         2
         3
         4
         :var-or-nil
         :var-or-nil
         4
         :var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil],
        :b
        [101
         102
         103
         104
         105
         106
         107
         108
         109
         110
         109
         108
         107
         106
         105
         104
         103
         102],
        :c
        ["a"
         "b"
         "s"
         " "
         "t"
         "r"
         "a"
         "c"
         "t"
         "d"
         "a"
         "t"
         "a"
         "t"
         "a"
         "b"
         "l"
         "e"],
        :e
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         3
         4
         5
         6
         7
         :var-or-nil
         8
         1
         1]}))




  (is (= (note-to-test/represent-value
          (tc/concat (tc/group-by DS [:V3])
                     (tc/group-by DS [:V4])))
       {:name [{:V3 0.5} {:V3 1.0} {:V3 1.5} {:V4 "A"} {:V4 "B"} {:V4 "C"}],
        :group-id [0 1 2 3 4 5],
        :data
        [{:V1 [1 2 1], :V2 [1 4 7], :V3 [0.5 0.5 0.5], :V4 ["A" "A" "A"]}
         {:V1 [2 1 2], :V2 [2 5 8], :V3 [1.0 1.0 1.0], :V4 ["B" "B" "B"]}
         {:V1 [1 2 1], :V2 [3 6 9], :V3 [1.5 1.5 1.5], :V4 ["C" "C" "C"]}
         {:V1 [1 2 1], :V2 [1 4 7], :V3 [0.5 0.5 0.5], :V4 ["A" "A" "A"]}
         {:V1 [2 1 2], :V2 [2 5 8], :V3 [1.0 1.0 1.0], :V4 ["B" "B" "B"]}
         {:V1 [1 2 1], :V2 [3 6 9], :V3 [1.5 1.5 1.5], :V4 ["C" "C" "C"]}]}))


  (is (= (note-to-test/represent-value
          (apply tc/union (tc/drop-columns ds2 :d) (repeat 10 ds1)))
       {:a
        [:var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil
         1
         2
         1
         2
         3
         4
         :var-or-nil
         :var-or-nil
         4],
        :b
        [110
         109
         108
         107
         106
         105
         104
         103
         102
         101
         102
         103
         104
         105
         106
         107
         108
         109],
        :c
        ["d"
         "a"
         "t"
         "a"
         "t"
         "a"
         "b"
         "l"
         "e"
         "a"
         "b"
         "s"
         " "
         "t"
         "r"
         "a"
         "c"
         "t"],
        :e
        [3
         4
         5
         6
         7
         :var-or-nil
         8
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))




  (is (= (note-to-test/represent-value
          (tc/bind ds1 ds2))
       {:a
        [1
         2
         1
         2
         3
         4
         :var-or-nil
         :var-or-nil
         4
         :var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil],
        :b
        [101
         102
         103
         104
         105
         106
         107
         108
         109
         110
         109
         108
         107
         106
         105
         104
         103
         102],
        :c
        ["a"
         "b"
         "s"
         " "
         "t"
         "r"
         "a"
         "c"
         "t"
         "d"
         "a"
         "t"
         "a"
         "t"
         "a"
         "b"
         "l"
         "e"],
        :e
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         3
         4
         5
         6
         7
         :var-or-nil
         8
         1
         1],
        :d
        [:var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]]}))


  (is (= (note-to-test/represent-value
          (tc/bind ds2 ds1))
       {:a
        [:var-or-nil
         1
         2
         5
         4
         3
         2
         1
         :var-or-nil
         1
         2
         1
         2
         3
         4
         :var-or-nil
         :var-or-nil
         4],
        :b
        [110
         109
         108
         107
         106
         105
         104
         103
         102
         101
         102
         103
         104
         105
         106
         107
         108
         109],
        :c
        ["d"
         "a"
         "t"
         "a"
         "t"
         "a"
         "b"
         "l"
         "e"
         "a"
         "b"
         "s"
         " "
         "t"
         "r"
         "a"
         "c"
         "t"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil],
        :e
        [3
         4
         5
         6
         7
         :var-or-nil
         8
         1
         1
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil
         :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/append ds1 ds2))
       {:a [:var-or-nil 1 2 5 4 3 2 1 :var-or-nil],
        :b [110 109 108 107 106 105 104 103 102],
        :c ["d" "a" "t" "a" "t" "a" "b" "l" "e"],
        :d
        [[:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]
         [:symbol "X"]],
        :e [3 4 5 6 7 :var-or-nil 8 1 1]}))


  (is (= (note-to-test/represent-value
          (tc/intersect (tc/select-columns ds1 :b)
                        (tc/select-columns ds2 :b)))
       {:b [109 108 107 106 105 104 103 102]}))


  (is (= (note-to-test/represent-value
          (tc/difference (tc/select-columns ds1 :b)
                         (tc/select-columns ds2 :b)))
       {:b [101]}))


  (is (= (note-to-test/represent-value
          (tc/difference (tc/select-columns ds2 :b)
                         (tc/select-columns ds1 :b)))
       {:b [110]}))





























  (is (= (note-to-test/represent-value
          (defonce stocks (tc/dataset "https://raw.githubusercontent.com/techascent/tech.ml.dataset/master/test/data/stocks.csv" {:key-fn keyword})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          stocks)
       {:symbol
        ["MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"],
        :date
        [[java.time.LocalDate "2000-01-01"]
         [java.time.LocalDate "2000-02-01"]
         [java.time.LocalDate "2000-03-01"]
         [java.time.LocalDate "2000-04-01"]
         [java.time.LocalDate "2000-05-01"]
         [java.time.LocalDate "2000-06-01"]
         [java.time.LocalDate "2000-07-01"]
         [java.time.LocalDate "2000-08-01"]
         [java.time.LocalDate "2000-09-01"]
         [java.time.LocalDate "2000-10-01"]
         [java.time.LocalDate "2000-11-01"]
         [java.time.LocalDate "2000-12-01"]
         [java.time.LocalDate "2001-01-01"]
         [java.time.LocalDate "2001-02-01"]
         [java.time.LocalDate "2001-03-01"]
         [java.time.LocalDate "2001-04-01"]
         [java.time.LocalDate "2001-05-01"]
         [java.time.LocalDate "2001-06-01"]
         [java.time.LocalDate "2001-07-01"]
         [java.time.LocalDate "2001-08-01"]],
        :price
        [39.81
         36.35
         43.22
         28.37
         25.45
         32.54
         28.4
         28.4
         24.53
         28.02
         23.34
         17.65
         24.84
         24.0
         22.25
         27.56
         28.14
         29.7
         26.93
         23.21]}))

  (is (= (note-to-test/represent-value
          (-> stocks
              (tc/group-by (fn [row]
                              {:symbol (:symbol row)
                               :year (tech.v3.datatype.datetime/long-temporal-field :years (:date row))}))
              (tc/aggregate #(tech.v3.datatype.functional/mean (% :price)))
              (tc/order-by [:symbol :year])))
       {:symbol
        ["AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AAPL"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"],
        :year
        [2000
         2001
         2002
         2003
         2004
         2005
         2006
         2007
         2008
         2009
         2010
         2000
         2001
         2002
         2003
         2004
         2005
         2006
         2007
         2008],
        "summary"
        [21.748333333333335
         10.175833333333333
         9.408333333333333
         9.3475
         18.723333333333333
         48.17166666666666
         72.04333333333334
         133.35333333333332
         138.48083333333332
         150.39333333333335
         206.5666666666667
         43.93083333333333
         11.739166666666668
         16.723333333333333
         39.016666666666666
         43.267500000000005
         40.1875
         36.251666666666665
         69.9525
         69.015]}))

  (is (= (note-to-test/represent-value
          (-> stocks
              (tc/group-by (juxt :symbol #(tech.v3.datatype.datetime/long-temporal-field :years (% :date))))
              (tc/aggregate #(tech.v3.datatype.functional/mean (% :price)))
              (tc/rename-columns {:$group-name-0 :symbol
                                   :$group-name-1 :year})))
       {:symbol
        ["MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "MSFT"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"
         "AMZN"],
        :year
        [2000
         2001
         2002
         2003
         2004
         2005
         2006
         2007
         2008
         2009
         2010
         2000
         2001
         2002
         2003
         2004
         2005
         2006
         2007
         2008],
        "summary"
        [29.673333333333332
         25.3475
         21.826666666666668
         20.934166666666666
         22.674166666666668
         23.84583333333333
         24.758333333333336
         29.284166666666668
         25.208333333333332
         22.872500000000002
         28.50666666666667
         43.93083333333333
         11.739166666666668
         16.723333333333333
         39.016666666666666
         43.267500000000005
         40.1875
         36.251666666666665
         69.9525
         69.015]}))


  (is (= (note-to-test/represent-value
          (require '[tech.v3.datatype.functional :as dfn]
                   '[tech.v3.datatype.argops :as aops]
                   '[tech.v3.datatype :as dtype]))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (defonce flights (tc/dataset "https://raw.githubusercontent.com/Rdatatable/data.table/master/vignettes/flights14.csv")))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (tc/head flights 6))
       {"dep_delay" [14 -3 2 -8 2 4],
        "origin" ["JFK" "JFK" "JFK" "LGA" "JFK" "EWR"],
        "air_time" [359 363 351 157 350 339],
        "hour" [9 11 19 7 13 18],
        "arr_delay" [13 13 9 -26 1 0],
        "dest" ["LAX" "LAX" "LAX" "PBI" "LAX" "LAX"],
        "distance" [2475 2475 2475 1035 2475 2454],
        "year" [2014 2014 2014 2014 2014 2014],
        "month" [1 1 1 1 1 1],
        "day" [1 1 1 1 1 1],
        "carrier" ["AA" "AA" "AA" "AA" "AA" "AA"]}))


  (is (= (note-to-test/represent-value
          (tc/shape flights))
       [253316 11]))


  (is (= (note-to-test/represent-value
          (def DT (tc/dataset {:ID ["b" "b" "b" "a" "a" "c"]
                                :a (range 1 7)
                                :b (range 7 13)
                                :c (range 13 19)})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DT)
       {:ID ["b" "b" "b" "a" "a" "c"],
        :a [1 2 3 4 5 6],
        :b [7 8 9 10 11 12],
        :c [13 14 15 16 17 18]}))

  (is (= (note-to-test/represent-value
          (-> :ID DT meta :datatype))
       :string))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows (fn [row] (and (= (get row "origin") "JFK")
                                             (= (get row "month") 6))))
              (tc/head 6)))
       {"dep_delay" [-9 -10 18 -6 -4 -6],
        "origin" ["JFK" "JFK" "JFK" "JFK" "JFK" "JFK"],
        "air_time" [324 329 326 320 326 329],
        "hour" [8 12 7 10 18 14],
        "arr_delay" [-5 -13 -1 -16 -45 -23],
        "dest" ["LAX" "LAX" "LAX" "LAX" "LAX" "LAX"],
        "distance" [2475 2475 2475 2475 2475 2475],
        "year" [2014 2014 2014 2014 2014 2014],
        "month" [6 6 6 6 6 6],
        "day" [1 1 1 1 1 1],
        "carrier" ["AA" "AA" "AA" "AA" "AA" "AA"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows flights (range 2)))
       {"dep_delay" [14 -3],
        "origin" ["JFK" "JFK"],
        "air_time" [359 363],
        "hour" [9 11],
        "arr_delay" [13 13],
        "dest" ["LAX" "LAX"],
        "distance" [2475 2475],
        "year" [2014 2014],
        "month" [1 1],
        "day" [1 1],
        "carrier" ["AA" "AA"]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/order-by ["origin" "dest"] [:asc :desc])
              (tc/head 6)))
       {"dep_delay" [-6 -9 -6 231 -8 21],
        "origin" ["EWR" "EWR" "EWR" "EWR" "EWR" "EWR"],
        "air_time" [154 177 201 184 159 176],
        "hour" [6 8 6 12 6 8],
        "arr_delay" [-38 -17 10 268 -32 10],
        "dest" ["XNA" "XNA" "XNA" "XNA" "XNA" "XNA"],
        "distance" [1131 1131 1131 1131 1131 1131],
        "year" [2014 2014 2014 2014 2014 2014],
        "month" [6 1 3 2 4 2],
        "day" [3 20 19 3 25 19],
        "carrier" ["EV" "EV" "EV" "EV" "EV" "EV"]}))


  (is (= (note-to-test/represent-value
          (take 6 (flights "arr_delay")))
       [13 13 9 -26 1 0]))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-columns "arr_delay")
              (tc/head 6)))
       {"arr_delay" [13 13 9 -26 1 0]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-columns ["arr_delay" "dep_delay"])
              (tc/head 6)))
       {"arr_delay" [13 13 9 -26 1 0], "dep_delay" [14 -3 2 -8 2 4]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-columns {"arr_delay" "delay_arr"
                                   "dep_delay" "delay_arr"})
              (tc/head 6)))
       {"delay_arr" [14 -3 2 -8 2 4]}))


  (is (= (note-to-test/represent-value
          (->> (dfn/+ (flights "arr_delay") (flights "dep_delay"))
               (aops/argfilter #(< % 0.0))
               (dtype/ecount)))
       141814))


  (is (= (note-to-test/represent-value
          (->> (map + (flights "arr_delay") (flights "dep_delay"))
               (filter neg?)
               (count)))
       141814))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows (fn [row] (and (= (get row "origin") "JFK")
                                             (= (get row "month") 6))))
              (tc/aggregate {:m_arr #(dfn/mean (% "arr_delay"))
                              :m_dep #(dfn/mean (% "dep_delay"))})))
       {:m_arr [5.83934932320114], :m_dep [9.807884113037284]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows (fn [row] (and (= (get row "origin") "JFK")
                                             (= (get row "month") 6))))
              (tc/row-count)))
       8422))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-columns (complement #{"arr_delay" "dep_delay"}))
              (tc/head 6)))
       {"origin" ["JFK" "JFK" "JFK" "LGA" "JFK" "EWR"],
        "air_time" [359 363 351 157 350 339],
        "hour" [9 11 19 7 13 18],
        "dest" ["LAX" "LAX" "LAX" "PBI" "LAX" "LAX"],
        "distance" [2475 2475 2475 1035 2475 2454],
        "year" [2014 2014 2014 2014 2014 2014],
        "month" [1 1 1 1 1 1],
        "day" [1 1 1 1 1 1],
        "carrier" ["AA" "AA" "AA" "AA" "AA" "AA"]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/group-by ["origin"])
              (tc/aggregate {:N tc/row-count})))
       {"origin" ["JFK" "LGA" "EWR"], :N [81483 84433 87400]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows #(= (get % "carrier") "AA"))
              (tc/group-by ["origin"])
              (tc/aggregate {:N tc/row-count})))
       {"origin" ["JFK" "LGA" "EWR"], :N [11923 11730 2649]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows #(= (get % "carrier") "AA"))
              (tc/group-by ["origin" "dest"])
              (tc/aggregate {:N tc/row-count})
              (tc/head 6)))
       {"origin" ["JFK" "LGA" "EWR" "JFK" "JFK" "EWR"],
        "dest" ["LAX" "PBI" "LAX" "MIA" "SEA" "MIA"],
        :N [3387 245 62 1876 298 848]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows #(= (get % "carrier") "AA"))
              (tc/group-by ["origin" "dest" "month"])
              (tc/aggregate [#(dfn/mean (% "arr_delay"))
                              #(dfn/mean (% "dep_delay"))])
              (tc/head 10)))
       {"origin"
        ["LGA" "JFK" "EWR" "JFK" "JFK" "LGA" "JFK" "LGA" "EWR" "LGA"],
        "dest" ["ORD" "SEA" "DFW" "STT" "SJU" "MIA" "MIA" "DFW" "MIA" "PBI"],
        "month" [1 1 1 1 1 1 1 1 1 1],
        :summary-0
        [8.440233236151604
         14.357142857142858
         6.427672955974843
         18.93103448275862
         18.920454545454547
         6.13
         15.720670391061452
         0.5295698924731183
         11.01123595505618
         -7.758620689655173],
        :summary-1
        [10.119533527696793
         30.75
         10.0125786163522
         21.379310344827587
         20.227272727272727
         5.4175
         18.743016759776538
         7.758064516129032
         12.123595505617978
         0.3103448275862069]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows #(= (get % "carrier") "AA"))
              (tc/group-by ["origin" "dest" "month"])
              (tc/aggregate [#(dfn/mean (% "arr_delay"))
                              #(dfn/mean (% "dep_delay"))])
              (tc/order-by ["origin" "dest" "month"])
              (tc/head 10)))
       {"origin"
        ["EWR" "EWR" "EWR" "EWR" "EWR" "EWR" "EWR" "EWR" "EWR" "EWR"],
        "dest" ["DFW" "DFW" "DFW" "DFW" "DFW" "DFW" "DFW" "DFW" "DFW" "DFW"],
        "month" [1 2 3 4 5 6 7 8 9 10],
        :summary-0
        [6.427672955974843
         10.536764705882353
         12.865030674846626
         17.79268292682927
         18.48780487804878
         37.00595238095238
         20.25
         16.936046511627907
         5.865030674846626
         18.81366459627329],
        :summary-1
        [10.0125786163522
         11.345588235294118
         8.079754601226995
         12.920731707317072
         18.682926829268293
         38.74404761904762
         21.154761904761905
         22.069767441860463
         13.05521472392638
         18.8944099378882]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/group-by (fn [row]
                              {:dep_delay (pos? (get row "dep_delay"))
                               :arr_delay (pos? (get row "arr_delay"))}))
              (tc/aggregate {:N tc/row-count})))
       {:dep_delay [true false false true],
        :arr_delay [true true false false],
        :N [72836 34583 119304 26593]}))


  (is (= (note-to-test/represent-value
          DT)
       {:ID ["b" "b" "b" "a" "a" "c"],
        :a [1 2 3 4 5 6],
        :b [7 8 9 10 11 12],
        :c [13 14 15 16 17 18]}))

  (is (= (note-to-test/represent-value
          (tc/group-by DT :ID {:result-type :as-map}))
       {"b" {:ID ["b" "b" "b"], :a [1 2 3], :b [7 8 9], :c [13 14 15]},
        "a" {:ID ["a" "a"], :a [4 5], :b [10 11], :c [16 17]},
        "c" {:ID ["c"], :a [6], :b [12], :c [18]}}))

  (is (= (note-to-test/represent-value
          (-> DT
              (tc/group-by [:ID])
              (tc/aggregate-columns (complement #{:ID}) dfn/mean)))
       {:ID ["b" "a" "c"],
        :a [2.0 4.5 6.0],
        :b [8.0 10.5 12.0],
        :c [14.0 16.5 18.0]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/select-rows #(= (get % "carrier") "AA"))
              (tc/group-by ["origin" "dest" "month"])
              (tc/aggregate-columns ["arr_delay" "dep_delay"] dfn/mean)
              (tc/head 6)))
       {"origin" ["LGA" "JFK" "EWR" "JFK" "JFK" "LGA"],
        "dest" ["ORD" "SEA" "DFW" "STT" "SJU" "MIA"],
        "month" [1 1 1 1 1 1],
        "arr_delay"
        [8.440233236151604
         14.357142857142858
         6.427672955974843
         18.93103448275862
         18.920454545454547
         6.13],
        "dep_delay"
        [10.119533527696793
         30.75
         10.0125786163522
         21.379310344827587
         20.227272727272727
         5.4175]}))


  (is (= (note-to-test/represent-value
          (-> flights
              (tc/group-by ["month"])
              (tc/head 2) ;; head applied on each group
              (tc/ungroup)
              (tc/head 6)))
       {"dep_delay" [39 -12 -1 -5 -11 -3],
        "origin" ["EWR" "EWR" "JFK" "JFK" "JFK" "JFK"],
        "air_time" [117 33 358 358 375 368],
        "hour" [19 7 8 11 8 11],
        "arr_delay" [42 -11 1 3 36 14],
        "dest" ["BNA" "BWI" "LAX" "LAX" "LAX" "LAX"],
        "distance" [748 169 2475 2475 2475 2475],
        "year" [2014 2014 2014 2014 2014 2014],
        "month" [1 1 2 2 3 3],
        "day" [24 24 1 1 1 1],
        "carrier" ["EV" "EV" "AA" "AA" "AA" "AA"]}))


  (is (= (note-to-test/represent-value
          (-> DT
              (tc/pivot->longer [:a :b] {:value-column-name :val})
              (tc/drop-columns [:$column :c])))
       {:ID ["b" "b" "b" "a" "a" "c" "b" "b" "b" "a" "a" "c"],
        :val [1 2 3 4 5 6 7 8 9 10 11 12]}))


  (is (= (note-to-test/represent-value
          (-> DT
              (tc/pivot->longer [:a :b] {:value-column-name :val})
              (tc/drop-columns [:$column :c])
              (tc/fold-by :ID)))
       {:ID ["b" "a" "c"], :val [[1 2 3 7 8 9] [4 5 10 11] [6 12]]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/dataset {:V1 (take 9 (cycle [1 2]))
                                :V2 (range 1 10)
                                :V3 (take 9 (cycle [0.5 1.0 1.5]))
                                :V4 (take 9 (cycle ["A" "B" "C"]))})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (tc/dataset? DS))
       true))

  (is (= (note-to-test/represent-value
          (class DS))
       tech.v3.dataset.impl.dataset.Dataset))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS [2 3]))
       {:V1 [1 2], :V2 [3 4], :V3 [1.5 0.5], :V4 ["C" "A"]}))


  (is (= (note-to-test/represent-value
          (tc/drop-rows DS (range 2 7)))
       {:V1 [1 2 2 1],
        :V2 [1 2 8 9],
        :V3 [0.5 1.0 1.0 1.5],
        :V4 ["A" "B" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #(> % 5) :V2)))
       {:V1 [2 1 2 1],
        :V2 [6 7 8 9],
        :V3 [1.5 0.5 1.0 1.5],
        :V4 ["C" "A" "B" "C"]}))

  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #{"A" "C"} :V4)))
       {:V1 [1 1 2 2 1 1],
        :V2 [1 3 4 6 7 9],
        :V3 [0.5 1.5 0.5 1.5 0.5 1.5],
        :V4 ["A" "C" "A" "C" "A" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS #(and (= (:V1 %) 1)
                                    (= (:V4 %) "A"))))
       {:V1 [1 1], :V2 [1 7], :V3 [0.5 0.5], :V4 ["A" "A"]}))


  (is (= (note-to-test/represent-value
          (tc/unique-by DS))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))

  (is (= (note-to-test/represent-value
          (tc/unique-by DS [:V1 :V4]))
       {:V1 [1 2 1 2 1 2],
        :V2 [1 2 3 4 5 6],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/drop-missing DS))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))




  (is (= (note-to-test/represent-value
          (tc/by-rank DS :V1 zero?))
       {:V1 [2 2 2 2],
        :V2 [2 4 6 8],
        :V3 [1.0 0.5 1.5 1.0],
        :V4 ["B" "A" "C" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp (partial re-matches #"^B") str :V4)))
       {:V1 [2 1 2], :V2 [2 5 8], :V3 [1.0 1.0 1.0], :V4 ["B" "B" "B"]}))

  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #(<= 3 % 5) :V2)))
       {:V1 [1 2 1], :V2 [3 4 5], :V3 [1.5 0.5 1.0], :V4 ["C" "A" "B"]}))

  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #(< 3 % 5) :V2)))
       {:V1 [2], :V2 [4], :V3 [0.5], :V4 ["A"]}))

  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #(<= 3 % 5) :V2)))
       {:V1 [1 2 1], :V2 [3 4 5], :V3 [1.5 0.5 1.0], :V4 ["C" "A" "B"]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS :V3))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 4 7 2 5 8 3 6 9],
        :V3 [0.5 0.5 0.5 1.0 1.0 1.0 1.5 1.5 1.5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS :V3 :desc))
       {:V1 [1 2 1 1 2 2 1 2 1],
        :V2 [3 6 9 5 2 8 7 4 1],
        :V3 [1.5 1.5 1.5 1.0 1.0 1.0 0.5 0.5 0.5],
        :V4 ["C" "C" "C" "B" "B" "B" "A" "A" "A"]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V1 :V2] [:asc :desc]))
       {:V1 [1 1 1 1 1 2 2 2 2],
        :V2 [9 7 5 3 1 8 6 4 2],
        :V3 [1.5 0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0],
        :V4 ["C" "A" "B" "C" "A" "B" "C" "A" "B"]}))


  (is (= (note-to-test/represent-value
          (nth (tc/columns DS :as-seq) 2))
       [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5]))

  (is (= (note-to-test/represent-value
          (tc/dataset [(nth (tc/columns DS :as-seq) 2)]))
       {:V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5]}))


  (is (= (note-to-test/represent-value
          (tc/select-columns DS :V2))
       {:V2 [1 2 3 4 5 6 7 8 9]}))

  (is (= (note-to-test/represent-value
          (tc/select-columns DS [:V2]))
       {:V2 [1 2 3 4 5 6 7 8 9]}))

  (is (= (note-to-test/represent-value
          (DS :V2))
       [1 2 3 4 5 6 7 8 9]))


  (is (= (note-to-test/represent-value
          (tc/select-columns DS [:V2 :V3 :V4]))
       {:V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-columns DS (complement #{:V2 :V3 :V4})))
       {:V1 [1 2 1 2 1 2 1 2 1]}))

  (is (= (note-to-test/represent-value
          (tc/drop-columns DS [:V2 :V3 :V4]))
       {:V1 [1 2 1 2 1 2 1 2 1]}))


  (is (= (note-to-test/represent-value
          (->> (range 1 3)
               (map (comp keyword (partial format "V%d")))
               (tc/select-columns DS)))
       {:V1 [1 2 1 2 1 2 1 2 1], :V2 [1 2 3 4 5 6 7 8 9]}))

  (is (= (note-to-test/represent-value
          (tc/reorder-columns DS :V4))
       {:V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5]}))

  (is (= (note-to-test/represent-value
          (tc/select-columns DS #(clojure.string/starts-with? (name %) "V")))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))

  (is (= (note-to-test/represent-value
          (tc/select-columns DS #(clojure.string/ends-with? (name %) "3")))
       {:V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5]}))

  (is (= (note-to-test/represent-value
          (tc/select-columns DS #"..2"))
       {:V2 [1 2 3 4 5 6 7 8 9]}))

  (is (= (note-to-test/represent-value
          (tc/select-columns DS #{:V1 "X"}))
       {:V1 [1 2 1 2 1 2 1 2 1]}))

  (is (= (note-to-test/represent-value
          (tc/select-columns DS #(not (clojure.string/starts-with? (name %) "V2"))))
       {:V1 [1 2 1 2 1 2 1 2 1],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (reduce + (DS :V1)))
       13))

  (is (= (note-to-test/represent-value
          (tc/aggregate-columns DS :V1 dfn/sum))
       {:V1 [13.0]}))

  (is (= (note-to-test/represent-value
          (tc/aggregate DS {:sumV1 #(dfn/sum (% :V1))}))
       {:sumV1 [13.0]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate DS [#(dfn/sum (% :V1))
                             #(dfn/standard-deviation (% :V3))]))
       {:summary-0 [13.0], :summary-1 [0.4330127018922193]}))

  (is (= (note-to-test/represent-value
          (tc/aggregate-columns DS [:V1 :V3] [dfn/sum
                                               dfn/standard-deviation]))
       {:V1 [13.0], :V3 [0.4330127018922193]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate DS {:sumv1 #(dfn/sum (% :V1))
                             :sdv3 #(dfn/standard-deviation (% :V3))}))
       {:sumv1 [13.0], :sdv3 [0.4330127018922193]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows (range 4))
              (tc/aggregate-columns :V1 dfn/sum)))
       {:V1 [6.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/first)
              (tc/select-columns :V3)))
       {:V3 [0.5]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/last)
              (tc/select-columns :V3)))
       {:V3 [1.5]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows 4)
              (tc/select-columns :V3)))
       {:V3 [1.0]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select :V3 4)))
       {:V3 [1.0]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/unique-by :V4)
              (tc/aggregate tc/row-count)))
       {"summary" [3]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/unique-by :V4)
              (tc/row-count)))
       3))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/unique-by)
              (tc/row-count)))
       9))


  (is (= (note-to-test/represent-value
          (tc/map-columns DS :V1 [:V1] #(dfn/pow % 2)))
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))

  (is (= (note-to-test/represent-value
          (def DS (tc/add-column DS :V1 (dfn/pow (DS :V1) 2))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/map-columns DS :v5 [:V1] dfn/log))
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :v5
        [0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0]}))

  (is (= (note-to-test/represent-value
          (def DS (tc/add-column DS :v5 (dfn/log (DS :V1)))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :v5
        [0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/add-columns DS {:v6 (dfn/sqrt (DS :V1))
                                                 :v7 "X"})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :v5
        [0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0
         1.3862943611198906
         0.0],
        :v6 [1.0 2.0 1.0 2.0 1.0 2.0 1.0 2.0 1.0],
        :v7 ["X" "X" "X" "X" "X" "X" "X" "X" "X"]}))


  (is (= (note-to-test/represent-value
          (tc/dataset {:v8 (dfn/+ (DS :V3) 1)}))
       {:v8 [1.5 2.0 2.5 1.5 2.0 2.5 1.5 2.0 2.5]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/drop-columns DS :v5)))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"],
        :v6 [1.0 2.0 1.0 2.0 1.0 2.0 1.0 2.0 1.0],
        :v7 ["X" "X" "X" "X" "X" "X" "X" "X" "X"]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/drop-columns DS [:v6 :v7])))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V3 [0.5 1.0 1.5 0.5 1.0 1.5 0.5 1.0 1.5],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/select-columns DS (complement #{:V3}))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [1 2 3 4 5 6 7 8 9],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/map-columns DS :V2 [:V2] #(if (< % 4.0) 0.0 %))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [0.0 0.0 0.0 4.0 5.0 6.0 7.0 8.0 9.0],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate {:sumV2 #(dfn/sum (% :V2))})))
       {:V4 ["A" "B" "C"], :sumV2 [11.0 13.0 15.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4 :V1])
              (tc/aggregate {:sumV2 #(dfn/sum (% :V2))})))
       {:V4 ["A" "B" "C" "A" "B" "C"],
        :V1 [1.0 4.0 1.0 4.0 1.0 4.0],
        :sumV2 [7.0 8.0 9.0 4.0 5.0 6.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (fn [row]
                              (clojure.string/lower-case (:V4 row))))
              (tc/aggregate {:sumV1 #(dfn/sum (% :V1))})))
       {:$group-name ["a" "b" "c"], :sumV1 [6.0 9.0 6.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (fn [row]
                              {:abc (clojure.string/lower-case (:V4 row))}))
              (tc/aggregate {:sumV1 #(dfn/sum (% :V1))})))
       {:abc ["a" "b" "c"], :sumV1 [6.0 9.0 6.0]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by (fn [row]
                              (clojure.string/lower-case (:V4 row))))
              (tc/aggregate {:sumV1 #(dfn/sum (% :V1))} {:add-group-as-column :abc})))
       {:$group-name ["a" "b" "c"], :sumV1 [6.0 9.0 6.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by #(= (:V4 %) "A"))
              (tc/aggregate #(dfn/sum (% :V1)))))
       {:$group-name [true false], "summary" [6.0 15.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows (range 5))
              (tc/group-by :V4)
              (tc/aggregate {:sumV1 #(dfn/sum (% :V1))})))
       {:$group-name ["A" "B" "C"], :sumV1 [5.0 5.0 1.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/aggregate tc/row-count)))
       {:$group-name ["A" "B" "C"], "summary" [3 3 3]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V1])
              (tc/add-column :n tc/row-count)
              (tc/ungroup)))
       {:V1 [1.0 1.0 1.0 1.0 1.0 4.0 4.0 4.0 4.0],
        :V2 [0.0 0.0 5.0 7.0 9.0 0.0 4.0 6.0 8.0],
        :V4 ["A" "C" "B" "A" "C" "B" "A" "C" "B"],
        :n [5 5 5 5 5 4 4 4 4]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate-columns :V2 first)))
       {:V4 ["A" "B" "C"], :V2 [0.0 0.0 0.0]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate-columns :V2 last)))
       {:V4 ["A" "B" "C"], :V2 [7.0 8.0 9.0]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate-columns :V2 #(nth % 1))))
       {:V4 ["A" "B" "C"], :V2 [4.0 5.0 6.0]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate-columns DS :all (fn [col] (first (sort #(compare %2 %1) col)))))
       {:V1 [4.0], :V2 [9.0], :V4 ["C"]}))


  (is (= (note-to-test/represent-value
          (tc/aggregate-columns DS [:V1 :V2] dfn/mean))
       {:V1 [2.3333333333333335], :V2 [4.333333333333333]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate-columns [:V1 :V2] dfn/mean)))
       {:V4 ["A" "B" "C"],
        :V1 [2.0 3.0 2.0],
        :V2 [3.6666666666666665 4.333333333333333 5.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate-columns [:V1 :V2] (fn [col]
                                                 {:sum (dfn/sum col)
                                                  :mean (dfn/mean col)}))))
       {:V4 ["A" "B" "C"],
        :V1-sum [6.0 9.0 6.0],
        :V1-mean [2.0 3.0 2.0],
        :V2-sum [11.0 13.0 15.0],
        :V2-mean [3.6666666666666665 4.333333333333333 5.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-columns :type/numerical)
              (tc/aggregate-columns :all dfn/mean)))
       {:V1 [2.3333333333333335], :V2 [4.333333333333333]}))


  (is (= (note-to-test/represent-value
          (tc/update-columns DS :all reverse))
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2 [9.0 8.0 7.0 6.0 5.0 4.0 0.0 0.0 0.0],
        :V4 ["C" "B" "A" "C" "B" "A" "C" "B" "A"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-columns [:V1 :V2])
              (tc/update-columns :all dfn/sqrt)))
       {:V1 [1.0 2.0 1.0 2.0 1.0 2.0 1.0 2.0 1.0],
        :V2
        [0.0
         0.0
         0.0
         2.0
         2.23606797749979
         2.449489742783178
         2.6457513110645907
         2.8284271247461903
         3.0]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-columns (complement #{:V4}))
              (tc/update-columns :all dfn/exp)))
       {:V1
        [2.718281828459045
         54.598150033144236
         2.718281828459045
         54.598150033144236
         2.718281828459045
         54.598150033144236
         2.718281828459045
         54.598150033144236
         2.718281828459045],
        :V2
        [1.0
         1.0
         1.0
         54.598150033144236
         148.4131591025766
         403.4287934927351
         1096.6331584284585
         2980.9579870417283
         8103.083927575384]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/update-columns DS [:V1 :V2] dfn/sqrt)))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 2.0 1.0 2.0 1.0 2.0 1.0 2.0 1.0],
        :V2
        [0.0
         0.0
         0.0
         2.0
         2.23606797749979
         2.449489742783178
         2.6457513110645907
         2.8284271247461903
         3.0],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))

  (is (= (note-to-test/represent-value
          (def DS (tc/update-columns DS (complement #{:V4}) #(dfn/pow % 2))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1.0 4.0 1.0 4.0 1.0 4.0 1.0 4.0 1.0],
        :V2
        [0.0
         0.0
         0.0
         4.0
         5.000000000000001
         5.999999999999999
         7.000000000000001
         8.000000000000002
         9.0],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-columns :type/numerical)
              (tc/update-columns :all #(dfn/- % 1))))
       {:V1 [0.0 3.0 0.0 3.0 0.0 3.0 0.0 3.0 0.0],
        :V2
        [-1.0
         -1.0
         -1.0
         3.0
         4.000000000000001
         4.999999999999999
         6.000000000000001
         7.000000000000002
         8.0]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/convert-types DS :type/numerical :int32)))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1 4 1 4 1 4 1 4 1],
        :V2 [0 0 0 4 5 5 7 8 9],
        :V4 ["A" "B" "C" "A" "B" "C" "A" "B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/head 2)
              (tc/add-column :V2 "X")
              (tc/ungroup)))
       {:V1 [1 4 4 1 1 4],
        :V2 ["X" "X" "X" "X" "X" "X"],
        :V4 ["A" "A" "B" "B" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/dataset (let [x (dfn/+ (DS :V1) (dfn/sum (DS :V2)))]
                         (println (seq (DS :V1)))
                         (println (tc/info (tc/select-columns DS :V1)))
                         {:A (range 1 (inc (tc/row-count DS)))
                          :B x})))
       {:A [1 2 3 4 5 6 7 8 9],
        :B [39.0 42.0 39.0 42.0 39.0 42.0 39.0 42.0 39.0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate {:V1sum #(dfn/sum (% :V1))})
              (tc/select-rows #(>= (:V1sum %) 5))))
       {:V4 ["A" "B" "C"], :V1sum [6.0 9.0 6.0]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4])
              (tc/aggregate {:V1sum #(dfn/sum (% :V1))})
              (tc/order-by :V1sum :desc)))
       {:V4 ["B" "A" "C"], :V1sum [9.0 6.0 6.0]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/order-by DS :V4)))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [1 4 1 4 1 4 1 4 1],
        :V2 [0 4 7 0 5 8 0 5 9],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS #(= (:V4 %) "A")))
       {:V1 [1 4 1], :V2 [0 4 7], :V4 ["A" "A" "A"]}))

  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #{"A" "C"} :V4)))
       {:V1 [1 4 1 1 4 1], :V2 [0 4 7 0 5 9], :V4 ["A" "A" "A" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows #(= (:V4 %) "B"))
              (tc/first)))
       {:V1 [4], :V2 [0], :V4 ["B"]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/unique-by :V4)
              (tc/select-rows (comp #{"B" "C"} :V4))))
       {:V1 [4 1], :V2 [0 0], :V4 ["B" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows #(= (:V4 %) "A"))
              (tc/last)))
       {:V1 [1], :V2 [7], :V4 ["A"]}))


  (is (= (note-to-test/represent-value
          (tc/select-rows DS (comp #{"A" "D"} :V4)))
       {:V1 [1 4 1], :V2 [0 4 7], :V4 ["A" "A" "A"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows (comp #{"A" "C"} :V4))
              (tc/aggregate-columns :V1 (fn [col]
                                           {:sum (dfn/sum col)}))))
       {:V1-sum [12.0]}))


  (is (= (note-to-test/represent-value
          (def DS (-> DS
                      (tc/map-columns :V1 [:V1 :V4] #(if (= %2 "A") 0 %1))
                      (tc/order-by :V4))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [0 0 0 4 1 4 1 4 1],
        :V2 [0 4 7 0 5 8 0 5 9],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows (comp (complement #{"B"}) :V4))
              (tc/group-by [:V4])
              (tc/aggregate-columns :V1 dfn/sum)))
       {:V4 ["A" "C"], :V1 [0.0 6.0]}))


  (is (= (note-to-test/represent-value
          (tc/order-by DS [:V4 :V1]))
       {:V1 [0 0 0 1 4 4 1 1 4],
        :V2 [0 4 7 5 0 8 0 9 5],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows #(and (= (:V1 %) 1)
                                     (= (:V4 %) "C")))))
       {:V1 [1 1], :V2 [0 9], :V4 ["C" "C"]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows #(and (= (:V1 %) 1)
                                     (#{"B" "C"} (:V4 %))))))
       {:V1 [1 1 1], :V2 [5 0 9], :V4 ["B" "C" "C"]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-rows #(and (= (:V1 %) 1)
                                     (#{"B" "C"} (:V4 %))) {:result-type :as-indexes})))
       [4 6 8]))


  (is (= (note-to-test/represent-value
          (def DS (tc/update-columns DS :V2 #(map-indexed (fn [idx v]
                                                             (if (zero? idx) 3 v)) %))))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [0 0 0 4 1 4 1 4 1],
        :V2 [3 4 7 0 5 8 0 5 9],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/order-by DS [:V4 :V1] [:asc :desc])))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [0 0 0 4 4 1 4 1 1],
        :V2 [3 4 7 0 8 5 5 0 9],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))


  (is (= (note-to-test/represent-value
          (def DS (tc/rename-columns DS {:V2 "v2"})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V1 [0 0 0 4 4 1 4 1 1],
        "v2" [3 4 7 0 8 5 5 0 9],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"]}))

  (is (= (note-to-test/represent-value
          (def DS (tc/rename-columns DS {"v2" :V2})))
       :var-or-nil))


  (is (= (note-to-test/represent-value
          (def DS (tc/reorder-columns DS :V4 :V1 :V2)))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          DS)
       {:V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V1 [0 0 0 4 4 1 4 1 1],
        :V2 [3 4 7 0 8 5 5 0 9]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/first)
              (tc/ungroup)))
       {:V4 ["A" "B" "C"], :V1 [0 4 4], :V2 [3 0 5]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/select-rows [0 2])
              (tc/ungroup)))
       {:V4 ["A" "A" "B" "B" "C" "C"], :V1 [0 0 4 1 4 1], :V2 [3 7 0 5 5 9]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/tail 2)
              (tc/ungroup)))
       {:V4 ["A" "A" "B" "B" "C" "C"], :V1 [0 0 4 1 1 1], :V2 [4 7 8 5 0 9]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/order-by :V2)
              (tc/first)
              (tc/ungroup)))
       {:V4 ["A" "B" "C"], :V1 [0 4 1], :V2 [3 0 0]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by [:V4 :V1])
              (tc/ungroup {:add-group-id-as-column :Grp})))
       {:Grp [0 0 0 1 1 2 3 4 4],
        :V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V1 [0 0 0 4 4 1 4 1 1],
        :V2 [3 4 7 0 8 5 5 0 9]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/add-column :row-id (range))
              (tc/select-columns [:V4 :row-id])
              (tc/group-by :V4)
              (tc/ungroup)))
       {:V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"], :row-id [0 1 2 3 4 5 6 7 8]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/add-column :row-id (range))
              (tc/select-columns [:V4 :row-id])
              (tc/group-by :V4)
              (tc/first)
              (tc/ungroup)))
       {:V4 ["A" "B" "C"], :row-id [0 3 6]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/add-column :row-id (range))
              (tc/select-columns [:V4 :row-id])
              (tc/group-by :V4)
              (tc/select-rows [0 2])
              (tc/ungroup)))
       {:V4 ["A" "A" "B" "B" "C" "C"], :row-id [0 2 3 5 6 8]}))


  (is (= (note-to-test/represent-value
          (-> DS
              (tc/select-columns [:V1 :V4])
              (tc/fold-by :V4)))
       {:V4 ["A" "B" "C"], :V1 [[0 0 0] [4 4 1] [4 1 1]]}))

  (is (= (note-to-test/represent-value
          (-> DS
              (tc/group-by :V4)
              (tc/unmark-group)))
       {:name ["A" "B" "C"],
        :group-id [0 1 2],
        :data
        [{:V4 ["A" "A" "A"], :V1 [0 0 0], :V2 [3 4 7]}
         {:V4 ["B" "B" "B"], :V1 [4 4 1], :V2 [0 8 5]}
         {:V4 ["C" "C" "C"], :V1 [4 1 1], :V2 [5 0 9]}]}))


  (is (= (note-to-test/represent-value
          (tc/write! DS "DF.csv"))
       :var-or-nil))


  (is (= (note-to-test/represent-value
          (tc/write! DS "DF.txt" {:separator \tab}))
       :var-or-nil))


  (is (= (note-to-test/represent-value
          (tc/write! DS "DF.tsv"))
       :var-or-nil))


  (is (= (note-to-test/represent-value
          (tc/dataset "DF.csv" {:key-fn keyword}))
       {:V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V1 [0 0 0 4 4 1 4 1 1],
        :V2 [3 4 7 0 8 5 5 0 9]}))


  (is (= (note-to-test/represent-value
          (tc/dataset "DF.tsv" {:key-fn keyword}))
       {:V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"],
        :V1 [0 0 0 4 4 1 4 1 1],
        :V2 [3 4 7 0 8 5 5 0 9]}))


  (is (= (note-to-test/represent-value
          (tc/dataset "DF.csv" {:key-fn keyword
                                 :column-whitelist ["V1" "V4"]}))
       {:V4 ["A" "A" "A" "B" "B" "B" "C" "C" "C"], :V1 [0 0 0 4 4 1 4 1 1]}))

  (is (= (note-to-test/represent-value
          (tc/dataset "DF.csv" {:key-fn keyword
                                 :column-blacklist ["V4"]}))
       {:V1 [0 0 0 4 4 1 4 1 1], :V2 [3 4 7 0 8 5 5 0 9]}))


  (is (= (note-to-test/represent-value
          (apply tc/concat (map tc/dataset ["DF.csv" "DF.csv"])))
       {"V4"
        ["A"
         "A"
         "A"
         "B"
         "B"
         "B"
         "C"
         "C"
         "C"
         "A"
         "A"
         "A"
         "B"
         "B"
         "B"
         "C"
         "C"
         "C"],
        "V1" [0 0 0 4 4 1 4 1 1 0 0 0 4 4 1 4 1 1],
        "V2" [3 4 7 0 8 5 5 0 9 3 4 7 0 8 5 5 0 9]}))


  (is (= (note-to-test/represent-value
          (def mDS (tc/pivot->longer DS [:V1 :V2] {:target-columns :variable
                                                    :value-column-name :value})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          mDS)
       {:V4
        ["A"
         "A"
         "A"
         "B"
         "B"
         "B"
         "C"
         "C"
         "C"
         "A"
         "A"
         "A"
         "B"
         "B"
         "B"
         "C"
         "C"
         "C"],
        :variable
        [:V1
         :V1
         :V1
         :V1
         :V1
         :V1
         :V1
         :V1
         :V1
         :V2
         :V2
         :V2
         :V2
         :V2
         :V2
         :V2
         :V2
         :V2],
        :value [0 0 0 4 4 1 4 1 1 3 4 7 0 8 5 5 0 9]}))


  (is (= (note-to-test/represent-value
          (-> mDS
              (tc/pivot->wider :variable :value {:fold-fn vec})
              (tc/update-columns ["V1" "V2"] (partial map count))))
       {:V4 ["A" "B" "C"],
        :V1 [[0 0 0] [4 4 1] [4 1 1]],
        :V2 [[3 4 7] [0 8 5] [5 0 9]]}))

  (is (= (note-to-test/represent-value
          (-> mDS
              (tc/pivot->wider :variable :value {:fold-fn vec})
              (tc/update-columns ["V1" "V2"] (partial map dfn/sum))))
       {:V4 ["A" "B" "C"],
        :V1 [[0 0 0] [4 4 1] [4 1 1]],
        :V2 [[3 4 7] [0 8 5] [5 0 9]]}))

  (is (= (note-to-test/represent-value
          (-> mDS
              (tc/map-columns :value #(str (> % 5))) ;; coerce to strings
              (tc/pivot->wider :value :variable {:fold-fn vec})
              (tc/update-columns ["true" "false"] (partial map #(if (sequential? %) (count %) 1)))))
       {:V4 ["A" "B" "C"], "false" [5 5 5], "true" [1 1 1]}))


  (is (= (note-to-test/represent-value
          (tc/group-by DS :V4 {:result-type :as-map}))
       {"A" {:V4 ["A" "A" "A"], :V1 [0 0 0], :V2 [3 4 7]},
        "B" {:V4 ["B" "B" "B"], :V1 [4 4 1], :V2 [0 8 5]},
        "C" {:V4 ["C" "C" "C"], :V1 [4 1 1], :V2 [5 0 9]}}))


  (is (= (note-to-test/represent-value
          (-> {:a ["A:a" "B:b" "C:c"]}
              (tc/dataset)
              (tc/separate-column :a [:V1 :V2] ":")))
       {:V1 ["A" "B" "C"], :V2 ["a" "b" "c"]}))


  (is (= (note-to-test/represent-value
          (def x (tc/dataset {"Id" ["A" "B" "C" "C"]
                               "X1" [1 3 5 7]
                               "XY" ["x2" "x4" "x6" "x8"]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def y (tc/dataset {"Id" ["A" "B" "B" "D"]
                               "Y1" [1 3 5 7]
                               "XY" ["y1" "y3" "y5" "y7"]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          x)
       {"Id" ["A" "B" "C" "C"], "X1" [1 3 5 7], "XY" ["x2" "x4" "x6" "x8"]}))

  (is (= (note-to-test/represent-value
          y)
       {"Id" ["A" "B" "B" "D"], "Y1" [1 3 5 7], "XY" ["y1" "y3" "y5" "y7"]}))


  (is (= (note-to-test/represent-value
          (tc/left-join x y "Id"))
       {"Id" ["A" "B" "B" "C" "C"],
        "X1" [1 3 3 5 7],
        "XY" ["x2" "x4" "x4" "x6" "x8"],
        "right.Id" ["A" "B" "B" :var-or-nil :var-or-nil],
        "Y1" [1 3 5 :var-or-nil :var-or-nil],
        "right.XY" ["y1" "y3" "y5" :var-or-nil :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (tc/right-join x y "Id"))
       {"Id" ["A" "B" "B" :var-or-nil],
        "X1" [1 3 3 :var-or-nil],
        "XY" ["x2" "x4" "x4" :var-or-nil],
        "right.Id" ["A" "B" "B" "D"],
        "Y1" [1 3 5 7],
        "right.XY" ["y1" "y3" "y5" "y7"]}))


  (is (= (note-to-test/represent-value
          (tc/inner-join x y "Id"))
       {"Id" ["A" "B" "B"],
        "X1" [1 3 3],
        "XY" ["x2" "x4" "x4"],
        "Y1" [1 3 5],
        "right.XY" ["y1" "y3" "y5"]}))


  (is (= (note-to-test/represent-value
          (tc/full-join x y "Id"))
       {"Id" ["A" "B" "B" "C" "C" :var-or-nil],
        "X1" [1 3 3 5 7 :var-or-nil],
        "XY" ["x2" "x4" "x4" "x6" "x8" :var-or-nil],
        "right.Id" ["A" "B" "B" :var-or-nil :var-or-nil "D"],
        "Y1" [1 3 5 :var-or-nil :var-or-nil 7],
        "right.XY" ["y1" "y3" "y5" :var-or-nil :var-or-nil "y7"]}))


  (is (= (note-to-test/represent-value
          (tc/semi-join x y "Id"))
       {"Id" ["A" "B"], "X1" [1 3], "XY" ["x2" "x4"]}))


  (is (= (note-to-test/represent-value
          (tc/anti-join x y "Id"))
       {"Id" ["C" "C"], "X1" [5 7], "XY" ["x6" "x8"]}))


  (is (= (note-to-test/represent-value
          (tc/right-join (tc/select-columns x ["Id" "X1"])
                          (tc/select-columns y ["Id" "XY"])
                          "Id"))
       {"Id" ["A" "B" "B" :var-or-nil],
        "X1" [1 3 3 :var-or-nil],
        "right.Id" ["A" "B" "B" "D"],
        "XY" ["y1" "y3" "y5" "y7"]}))

  (is (= (note-to-test/represent-value
          (tc/right-join (tc/select-columns x ["Id" "XY"])
                          (tc/select-columns y ["Id" "XY"])
                          "Id"))
       {"Id" ["A" "B" "B" :var-or-nil],
        "XY" ["x2" "x4" "x4" :var-or-nil],
        "right.Id" ["A" "B" "B" "D"],
        "right.XY" ["y1" "y3" "y5" "y7"]}))


  (is (= (note-to-test/represent-value
          (-> y
              (tc/group-by ["Id"])
              (tc/aggregate {"sumY1" #(dfn/sum (% "Y1"))})
              (tc/right-join x "Id")
              (tc/add-column "X1Y1" (fn [ds] (dfn/* (ds "sumY1")
                                                              (ds "X1"))))
              (tc/select-columns ["right.Id" "X1Y1"])))
       {"right.Id" ["A" "B" "C" "C"],
        "X1Y1" [1.0 24.0 :var-or-nil :var-or-nil]}))


  (is (= (note-to-test/represent-value
          (-> x
              (tc/select-columns ["Id" "X1"])
              (tc/map-columns "SqX1" "X1" (fn [x] (* x x)))
              (tc/right-join y "Id")
              (tc/drop-columns ["X1" "Id"])))
       {"SqX1" [1 9 9 :var-or-nil],
        "right.Id" ["A" "B" "B" "D"],
        "Y1" [1 3 5 7],
        "XY" ["y1" "y3" "y5" "y7"]}))


  (is (= (note-to-test/represent-value
          (-> (tc/left-join x y "Id")
              (tc/drop-columns ["right.Id"])
              (tc/fold-by (tc/column-names x))))
       {"Id" ["A" "B" "C" "C"],
        "X1" [1 3 5 7],
        "XY" ["x2" "x4" "x6" "x8"],
        "Y1" [[1] [3 5] [] []],
        "right.XY" [["y1"] ["y3" "y5"] [] []]}))


  (is (= (note-to-test/represent-value
          (def cjds (tc/dataset {:V1 [[2 1 1]]
                                  :V2 [[3 2]]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          cjds)
       {:V1 [[2 1 1]], :V2 [[3 2]]}))

  (is (= (note-to-test/represent-value
          (reduce #(tc/unroll %1 %2) cjds (tc/column-names cjds)))
       {:V1 [2 2 1 1 1 1], :V2 [3 2 3 2 3 2]}))

  (is (= (note-to-test/represent-value
          (-> (reduce #(tc/unroll %1 %2) cjds (tc/column-names cjds))
              (tc/unique-by)))
       {:V1 [2 2 1 1], :V2 [3 2 3 2]}))


  (is (= (note-to-test/represent-value
          (def x (tc/dataset {:V1 [1 2 3]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def y (tc/dataset {:V1 [4 5 6]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def z (tc/dataset {:V1 [7 8 9]
                              :V2 [0 0 0]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          x)
       {:V1 [1 2 3]}))

  (is (= (note-to-test/represent-value
          y)
       {:V1 [4 5 6]}))

  (is (= (note-to-test/represent-value
          z)
       {:V1 [7 8 9], :V2 [0 0 0]}))


  (is (= (note-to-test/represent-value
          (tc/bind x y))
       {:V1 [1 2 3 4 5 6]}))

  (is (= (note-to-test/represent-value
          (tc/bind x z))
       {:V1 [1 2 3 7 8 9], :V2 [:var-or-nil :var-or-nil :var-or-nil 0 0 0]}))


  (is (= (note-to-test/represent-value
          (->> [x y]
               (map-indexed #(tc/add-column %2 :id (repeat %1)))
               (apply tc/bind)))
       {:V1 [1 2 3 4 5 6], :id [0 0 0 1 1 1]}))


  (is (= (note-to-test/represent-value
          (tc/append x y))
       {:V1 [4 5 6]}))


  (is (= (note-to-test/represent-value
          (def x (tc/dataset {:V1 [1 2 2 3 3]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          (def y (tc/dataset {:V1 [2 2 3 4 4]})))
       :var-or-nil))

  (is (= (note-to-test/represent-value
          x)
       {:V1 [1 2 2 3 3]}))

  (is (= (note-to-test/represent-value
          y)
       {:V1 [2 2 3 4 4]}))


  (is (= (note-to-test/represent-value
          (tc/intersect x y))
       {:V1 [2 3]}))


  (is (= (note-to-test/represent-value
          (tc/difference x y))
       {:V1 [1]}))


  (is (= (note-to-test/represent-value
          (tc/union x y))
       {:V1 [1 2 3 4]}))

  (is (= (note-to-test/represent-value
          (tc/concat x y))
       {:V1 [1 2 2 3 3 2 2 3 4 4]}))
)
