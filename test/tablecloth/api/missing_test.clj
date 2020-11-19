(ns tablecloth.api.missing-test
  (:require [tablecloth.common-test :refer [approx]]
            [tablecloth.api :as api]
            [clojure.test :refer [deftest is are]]
            [tech.v3.datatype.functional :as dfn]))

(deftest empty-missing
  (let [empty-col (api/dataset {:a [nil nil]})]
    (is (= 2 (-> empty-col api/select-missing api/row-count)))
    (is (= 0 (-> empty-col api/drop-missing api/row-count)))
    (is (= 0 (-> empty-col (api/replace-missing :a :value 0) api/select-missing api/row-count)))
    (is (= 2 (-> empty-col (api/replace-missing :a :value dfn/mean) api/select-missing api/row-count)))
    (is (= 2 (-> empty-col api/replace-missing api/select-missing api/row-count)))))

(def ds (api/dataset {:a [nil nil nil 1.0 2  nil nil nil nil  nil 4   nil  11 nil nil]
                      :b [2   2   2 nil nil nil nil nil nil 13   nil   3  4  5 5]}))

(deftest selection-and-dropping
  (are [cnt s] (= cnt (-> ds s api/rows count))
    14 api/select-missing
    1 api/drop-missing
    11 (api/select-missing :a)
    4 (api/drop-missing :a)
    7 (api/select-missing :b)
    8 (api/drop-missing :b)))

(deftest strategy-mid
  (is (= 0 (-> ds api/replace-missing api/select-missing api/row-count)))
  (is (= 7 (-> ds (api/replace-missing :a :mid) api/select-missing api/row-count)))
  (are [xs cmd col] (= xs (-> ds cmd (api/column col) seq))
    [1.0 1.0 1.0 1.0 2.0 2.0 2.0 2.0 4.0 4.0 4.0 4.0 11.0 11.0 11.0]
    (api/replace-missing :a :mid) :a
    [2 2 2 2 2 2 13 13 13 13 13 3 4 5 5]
    (api/replace-missing :b :mid) :b))

(deftest strategy-down
  (are [xs cmd col] (= xs (-> ds cmd (api/column col) seq))
    [1.0 1.0 1.0 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0]
    (api/replace-missing :a :down) :a
    [2 2 2 2 2 2 2 2 2 13 13 3 4 5 5]
    (api/replace-missing :b :down) :b
    [999.0 999.0 999.0 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0]
    (api/replace-missing :a :down 999.0) :a
    [4.5 4.5 4.5 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0]
    (api/replace-missing :a :down dfn/mean) :a))

(deftest strategy-up
  (are [xs cmd col] (= xs (-> ds cmd (api/column col) seq))
    [1.0 1.0 1.0 1.0 2.0 4.0 4.0 4.0 4.0 4.0 4.0 11.0 11.0 11.0 11.0]
    (api/replace-missing :a :up) :a
    [2 2 2 13 13 13 13 13 13 13 3 3 4 5 5]
    (api/replace-missing :b :up) :b
    [1.0 1.0 1.0 1.0 2.0 4.0 4.0 4.0 4.0 4.0 4.0 11.0 11.0 999.0 999.0]
    (api/replace-missing :a :up 999.0) :a
    [1.0 1.0 1.0 1.0 2.0 4.0 4.0 4.0 4.0 4.0 4.0 11.0 11.0 4.5 4.5]
    (api/replace-missing :a :up dfn/mean) :a))

(deftest strategy-lerp
  (are [xs cmd col] (= xs (-> ds cmd (api/column col) (->> (map approx))))
    (map approx [1.0 1.0 1.0 1.0 2 (/ 7 3.0) (/ 8 3.0) 3.0 (/ 10 3.0) (/ 11 3.0) 4 7.5 11.0 11.0 11.0])
    (api/replace-missing :a :lerp) :a
    (map approx [2 2 2 4 5 7 8 10 11 13 8 3 4 5 5])
    (api/replace-missing :b :lerp) :b))

(deftest strategy-lerp-time
  (let [dtds (api/dataset {:dt [(java.time.LocalDateTime/of 2020 1 1 1 1 1)
                                nil nil nil
                                (java.time.LocalDateTime/of 2020 10 1 1 1 1)]})]
    (is (= (seq ((api/replace-missing dtds :lerp) :dt))
           [(java.time.LocalDateTime/of 2020 1 1 1 1 1)
            (java.time.LocalDateTime/of 2020 3 9 13 1 1)
            (java.time.LocalDateTime/of 2020 5 17 1 1 1)
            (java.time.LocalDateTime/of 2020 7 24 13 1 1)
            (java.time.LocalDateTime/of 2020 10 1 1 1 1)]))))

(deftest strategy-value
  (are [xs cmd col] (= xs (-> ds cmd (api/column col) seq))
    [0.0 0.0 0.0 1.0 2.0 0.0 0.0 0.0 0.0 0.0 4.0 0.0 11.0 0.0 0.0]
    (api/replace-missing :a :value 0.0) :a
    [4.5 4.5 4.5 1.0 2.0 4.5 4.5 4.5 4.5 4.5 4.0 4.5 11.0 4.5 4.5]
    (api/replace-missing :a :value dfn/mean) :a
    [-10.0 -20.0 -10.0 1.0 2.0 -20.0 -10.0 -20.0 -10.0 -20.0 4.0 -10.0 11.0 -20.0 -10.0]
    (api/replace-missing :a :value [-10.0 -20.0]) :a
    [nil 100.0 nil 1.0 2.0 nil nil nil nil -100.0 4.0 nil 11.0 nil nil]
    (api/replace-missing :a :value {1 100.0 9 -100.0}) :a))
