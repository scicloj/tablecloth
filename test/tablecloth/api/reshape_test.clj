(ns tablecloth.api.reshape-test
  (:require [tablecloth.api :as api]
            [tablecloth.api.reshape :as sut]
            [clojure.test :refer [deftest is are]]
            [tech.v2.datatype.functional :as dfn]
            [clojure.string :refer [starts-with?]]))

;; tidyr tests

(deftest pivot-longer-basic
  (let [ds (-> {:x [1 2] :y [3 4]}
               (api/dataset)
               (sut/pivot->longer))]
    (is (= '(:$column :$value) (api/column-names ds)))
    (are [v n] (= v (seq (ds n)))
      [:x :x :y :y] :$column
      [1 2 3 4] :$value)))

(deftest pivot-longer-column-names
  (let [ds (-> {:x [1 2] :y [3 4]}
               (api/dataset)
               (sut/pivot->longer :all {:target-columns :colname
                                        :value-column-name :v}))]
    (is (= '(:colname :v) (api/column-names ds)))
    (are [v n] (= v (seq (ds n)))
      [:x :x :y :y] :colname
      [1 2 3 4] :v)))

(deftest pivot-longer-datatypes
  (let [ds (-> {:x [1 2] :y [3 4]}
               (api/dataset)
               (sut/pivot->longer :all {:datatypes {:$column [:object (comp symbol name)]
                                                    :$value [:float32 double]}}))]
    (is (= '(:$column :$value) (api/column-names ds)))
    (are [v n] (= v (seq (ds n)))
      '(x x y y) :$column
      [1.0 2.0 3.0 4.0] :$value)))

(deftest pivot-longer-keep-column
  (let [ds (-> {:x [1 2] :y 2 :z [1 2]}
               (api/dataset)
               (sut/pivot->longer [:y :z]))]
    (is (= '(:x :$column :$value) (api/column-names ds)))
    (are [v n] (= v (seq (ds n)))
      [1 2 1 2] :x
      [:y :y :z :z] :$column
      [2 2 1 2] :$value)))

(deftest pivot-longer-missing
  (let [ds (-> {:x [1 nil] :y [nil 2]}
               (api/dataset))
        ds-drop (sut/pivot->longer ds)
        ds-keep (sut/pivot->longer ds :all {:drop-missing? false})]
    (are [v d n] (= v (seq (d n)))
      [:x :y] ds-drop :$column
      [1 2] ds-drop :$value
      [:x :x :y :y] ds-keep :$column
      [1 nil nil 2] ds-keep :$value)))

(deftest pivot-longer-separate-names-with-missing-combinations
  (let [ds (-> {:id ["A" "B"]
                "x_1" [1 3]
                "x_2" [2 4]
                "y_2" ["a" "b"]}
               (api/dataset))
        ds-drop (sut/pivot->longer ds string? {:target-columns [nil "n"]
                                               :splitter "_"})
        ds-keep (sut/pivot->longer ds string? {:target-columns [nil "n"]
                                               :splitter "_"
                                               :drop-missing? false})]
    (are [v d n] (= v (seq (d n)))
      ["A" "B" "A" "B"] ds-keep :id
      [1 1 2 2] ds-keep "n"
      [1 3 2 4] ds-keep "x"
      [nil nil "a" "b"] ds-keep "y"
      ["A" "B"] ds-drop :id
      [2 2] ds-drop "n"
      [2 4] ds-drop "x"
      ["a" "b"] ds-drop "y")))

(deftest pivot-longer-separate-names
  (let [ds (-> {:id ["A" "B"]
                "z_1" [1 7]
                "y_1" [2 8]
                "x_1" [3 9]
                "z_2" [4 10]
                "y_2" [5 11]
                "x_2" [6 12]}
               (api/dataset)
               (sut/pivot->longer string? {:target-columns [nil :n]
                                           :splitter "_"}))]
    (is (= [:id :n "z" "y" "x"] (api/column-names ds)))
    (are [v n] (= v (seq (ds n)))
      ["A" "B" "A" "B"] :id
      [1 1 2 2] :n
      [1 7 4 10] "z"
      [2 8 5 11] "y"
      [3 9 6 12] "x")))

(deftest pivot-longer-separate-names-regex
  (let [ds (-> {:id ["A" "B"]
                "z_1" [1 7]
                "y_1" [2 8]
                "x_1" [3 9]
                "z_2" [4 10]
                "y_2" [5 11]
                "x_2" [6 12]}
               (api/dataset)
               (sut/pivot->longer string? {:target-columns [nil :n]
                                           :splitter #"(.)_(.)"}))]
    (is (= [:id :n "z" "y" "x"] (api/column-names ds)))
    (are [v n] (= v (seq (ds n)))
      ["A" "B" "A" "B"] :id
      [1 1 2 2] :n
      [1 7 4 10] "z"
      [2 8 5 11] "y"
      [3 9 6 12] "x")))

(deftest pivot-longer-separate-names_vector
  (let [ds (-> {:id ["A" "B"]
                [:x 1] [1 3]
                [:x 2] [2 4]
                [:y 2] ["a" "b"]}
               (api/dataset)
               (sut/pivot->longer vector? {:target-columns [nil :n]
                                           :splitter identity}))]
    (are [v n] (= v (seq (ds n)))
      ["A" "B"] :id
      [2 2] :n
      [2 4] :x
      ["a" "b"] :y)))

(deftest pivot-longer-separate-names-custom
  (let [ds (-> {:id ["A" "B"]
                {:a :x :b 1} [1 3]
                {:a :x :b 2} [2 4]
                {:a :y :b 2} ["a" "b"]}
               (api/dataset)
               (sut/pivot->longer map? {:target-columns [nil :n]
                                        :splitter (juxt :a :b)}))]
    (are [v n] (= v (seq (ds n)))
      ["A" "B"] :id
      [2 2] :n
      [2 4] :x
      ["a" "b"] :y)))

;;

(deftest pivot-wider-basic
  (let [ds (-> {:key ["x" "y" "z"]
                :val [1 2 3]}
               (api/dataset)
               (sut/pivot->wider :key :val))]
    (are [v n] (= v (first (ds n)))
      1 "x"
      2 "y"
      3 "z")))

(deftest pivot-wider-not-pivoted-cols
  (let [ds (-> {:a 1
                :key ["x" "y"]
                :val [1 2]}
               (api/dataset)
               (sut/pivot->wider :key :val))]
    (is (= [:a "x" "y"] (api/column-names ds)))
    (are [v n] (= v (first (ds n)))
      1 :a
      1 "x"
      2 "y")))

(deftest pivot-wider-missings
  (let [ds (-> {:a [1 2]
                :key ["x" "y"]
                :val [1 2]}
               (api/dataset)
               (sut/pivot->wider :key :val {:drop-missing? false}))]
    (is (= [:a "x" "y"] (api/column-names ds)))
    (are [v n] (= v (seq (ds n)))
      [2 1] :a
      [nil 1] "x"
      [2 nil] "y")))

(deftest pivot-wider-concat-columns
  (let [ds (-> {:x ["X" "Y"]
                :y [1 2]
                :a [1 2]
                :b [1 2]}
               (api/dataset)
               (sut/pivot->wider [:x :y] [:a :b] {:concat-columns-with ""
                                                  :concat-value-with "_"}))]
    (is (= ["Y2_a" "Y2_b" "X1_a" "X1_b"] (api/column-names ds)))
    (is (= [[2 2 1 1]] (api/rows ds)))))

(deftest pivot-wider-multiple
  (let [ds (-> {:a [1 1 2]
                :key ["x" "x" "x"]
                :val [1 2 3]}
               (api/dataset)
               (sut/pivot->wider :key :val {:fold-fn vec}))]
    (is (= [[3] [1 2]] (seq (ds "x"))))))

(deftest pivot-wider-multiple-summarize
  (let [ds (-> {:a [1 1 2]
                :key ["x" "x" "x"]
                :val [1 10 100]}
               (api/dataset)
               (sut/pivot->wider :key :val {:fold-fn dfn/sum}))]
    (is (= [100.0 11.0] (seq (ds "x"))))))

(deftest pivot-wider-multiple-values
  (let [ds (-> {:row 1
                :var [:x :y]
                :a [1 2]
                :b [3 4]}
               (api/dataset)
               (sut/pivot->wider :var [:a :b]))]
    (is (= [{:row 1, "x-b" 3, "y-a" 2, "x-a" 1, "y-b" 4}] (api/rows ds :as-maps)))))

(deftest pivot-wider-multiple-values-no-row
  (let [ds (-> {:var [:x :y]
                :a [1 2]
                :b [3 4]}
               (api/dataset)
               (sut/pivot->wider :var [:a :b]))]
    (is (= [{"x-b" 3, "y-a" 2, "x-a" 1, "y-b" 4}] (api/rows ds :as-maps)))))

;; construction case

(defonce construction (api/dataset "data/construction.csv"))
(defonce construction-unit-map {"1 unit" "1"
                                "2 to 4 units" "2-4"
                                "5 units or more" "5+"})

(deftest pivot-revert
  (let [ds (-> construction
               (sut/pivot->longer #"^[125NWS].*|Midwest" {:target-columns [:units :region]
                                                          :splitter (fn [col-name]
                                                                      (if (re-matches #"^[125].*" col-name)
                                                                        [(construction-unit-map col-name) nil]
                                                                        [nil col-name]))
                                                          :value-column-name :n
                                                          :drop-missing? false})
               (sut/pivot->wider [:units :region] :n {:drop-missing? false})
               (api/rename-columns (zipmap (vals construction-unit-map)
                                           (keys construction-unit-map))))]
    (is (= (api/row-count ds) (api/row-count construction)))
    (is (= (api/column-count ds) (api/column-count construction)))
    (is (= (set (api/column-names ds)) (set (api/column-names construction))))
    (are [c] (= (seq (ds c)) (seq (construction c)))
      "Year" "Month" "1 unit" "2 to 4 units" "5 units or more" "Northeast" "Midwest" "South" "West")))

(deftest pivot-sizes
  (are [s d f] (= s (api/shape (-> d (api/dataset) f)))
    
    [180 3] "data/relig_income.csv"
    (api/pivot->longer (complement #{"religion"}))
    
    [5307 5] (-> (api/dataset "data/billboard.csv.gz")
                 (api/drop-columns :type/boolean))
    (api/pivot->longer #(starts-with? % "wk") {:target-columns :week
                                               :value-column-name :rank})

    [76046 8] "data/who.csv.gz"
    (api/pivot->longer #(starts-with? % "new") {:target-columns [:diagnosis :gender :age]
                                                :splitter #"new_?(.*)_(.)(.*)"
                                                :value-column-name :count})

    [405440 8] "data/who.csv.gz"
    (api/pivot->longer #(starts-with? % "new") {:target-columns [:diagnosis :gender :age]
                                                :splitter #"new_?(.*)_(.)(.*)"
                                                :value-column-name :count
                                                :drop-missing? false})

    [9 4] "data/family.csv"
    (api/pivot->longer (complement #{"family"}) {:target-columns [nil :child]
                                                 :splitter "_"
                                                 :datatypes {"gender" :int16}})

    [44 3] "data/anscombe.csv"
    (api/pivot->longer :all {:splitter #"(.)(.)"
                             :target-columns [nil :set]})

    [8 6] (api/dataset {:x [1 2 3 4]
                        :a [1 1 0 0]
                        :b [0 1 1 1]
                        :y1 (repeatedly 4 rand)
                        :y2 (repeatedly 4 rand)
                        :z1 [3 3 3 3]
                        :z2 [-2 -2 -2 -2]})
    (api/pivot->longer [:y1 :y2 :z1 :z2] {:target-columns [nil :times]
                                          :splitter #":(.)(.)"})

    [19 12] "data/fish_encounters.csv"
    (api/pivot->wider "station" "seen" {:drop-missing? false})

    [6 3] "data/warpbreaks.csv"
    (-> (api/group-by ["wool" "tension"])
        (api/aggregate {:n api/row-count}))

    [3 3] "data/warpbreaks.csv"
    (-> (api/reorder-columns ["wool" "tension" "breaks"])
        (api/pivot->wider "wool" "breaks" {:fold-fn vec}))

    [15 4] "data/production.csv"
    (api/pivot->wider ["product" "country"] "production")

    [52 6] "data/us_rent_income.csv"
    (api/pivot->wider "variable" ["estimate" "moe"] {:drop-missing? false})

    [3 4] "data/contacts.csv"
    (api/pivot->wider "field" "value" {:drop-missing? false})))
