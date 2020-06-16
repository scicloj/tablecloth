(ns tablecloth.api.reshape-test
  (:require [tablecloth.api :as api]
            [tablecloth.api.reshape :as sut]
            [clojure.test :refer [deftest is are]]
            [tech.ml.dataset :as ds]
            [tech.v2.datatype.functional :as dfn]))

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
    (is (= [:id :n "z" "y" "x"] (ds/column-names ds)))
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
                                                  :concat-value-with (fn [col-name value]
                                                                       (str col-name "_" (name value)))}))]
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

