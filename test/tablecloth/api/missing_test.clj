(ns tablecloth.api.missing-test
  (:require [tablecloth.common-test :refer [approx]]
            [tablecloth.api :as api]
            [tech.v3.datatype.functional :as dfn]
            [midje.sweet :refer [tabular fact =>]]))

(fact "empty-missing"
      (let [empty-col (api/dataset {:a [nil nil]})]
        (-> empty-col api/select-missing api/row-count)
         =>
         2
        (-> empty-col api/drop-missing api/row-count)
         =>
         0
        (-> empty-col (api/replace-missing :a :value 0) api/select-missing api/row-count)
         =>
         0
        (-> empty-col (api/replace-missing :a :value dfn/mean) api/select-missing api/row-count)
         =>
         2
        (-> empty-col api/replace-missing api/select-missing api/row-count)
         =>
         2))

(def ds (api/dataset {:a [nil nil nil 1.0 2  nil nil nil nil  nil 4   nil  11 nil nil]
                      :b [2   2   2 nil nil nil nil nil nil 13   nil   3  4  5 5]}))

(fact "selection-and-dropping"
      (tabular (fact (-> ds ?s api/rows count) => ?cnt)
               ?cnt ?s
               14 api/select-missing
               1 api/drop-missing
               11 (api/select-missing :a)
               4 (api/drop-missing :a)
               7 (api/select-missing :b)
               8 (api/drop-missing :b)))

(fact "strategy-mid"
      (fact (-> ds api/replace-missing api/select-missing api/row-count)
            =>
            0)
      (fact (-> ds (api/replace-missing :a :mid) api/select-missing api/row-count)
            =>
            7)
      (tabular (fact (-> ds ?cmd (api/column ?col) seq)
                     => ?xs)
               ?xs ?cmd ?col
               [1.0 1.0 1.0 1.0 2.0 2.0 2.0 2.0 4.0 4.0 4.0 4.0 11.0 11.0 11.0]
               (api/replace-missing :a :mid)
               :a
               [2 2 2 2 2 2 13 13 13 13 13 3 4 5 5]
               (api/replace-missing :b :mid)
               :b))

(fact "strategy-down"
      (tabular (fact (-> ds ?cmd (api/column ?col) seq)
                     => ?xs)
               ?xs ?cmd ?col
               [1.0 1.0 1.0 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0]
               (api/replace-missing :a :down)
               :a
               [2 2 2 2 2 2 2 2 2 13 13 3 4 5 5]
               (api/replace-missing :b :down)
               :b
               [999.0 999.0 999.0 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0]
               (api/replace-missing :a :down 999.0)
               :a
               [4.5 4.5 4.5 1.0 2.0 2.0 2.0 2.0 2.0 2.0 4.0 4.0 11.0 11.0 11.0]
               (api/replace-missing :a :down dfn/mean)
               :a))

(fact "strategy-up"
      (tabular (fact (-> ds ?cmd (api/column ?col) seq)
                     => ?xs)
               ?xs ?cmd ?col
               [1.0 1.0 1.0 1.0 2.0 4.0 4.0 4.0 4.0 4.0 4.0 11.0 11.0 11.0 11.0]
               (api/replace-missing :a :up)
               :a
               [2 2 2 13 13 13 13 13 13 13 3 3 4 5 5]
               (api/replace-missing :b :up)
               :b
               [1.0 1.0 1.0 1.0 2.0 4.0 4.0 4.0 4.0 4.0 4.0 11.0 11.0 999.0 999.0]
               (api/replace-missing :a :up 999.0)
               :a
               [1.0 1.0 1.0 1.0 2.0 4.0 4.0 4.0 4.0 4.0 4.0 11.0 11.0 4.5 4.5]
               (api/replace-missing :a :up dfn/mean)
               :a))

(fact "strategy-lerp"
      (tabular  (fact (-> ds ?cmd (api/column ?col) (->> (map approx)))
                      => ?xs)
                ?xs ?cmd ?col
                (map approx
                     [1.0 1.0 1.0 1.0 2
                      (/ 7 3.0) (/ 8 3.0) 3.0 (/ 10 3.0) (/ 11 3.0)
                      4 7.5 11.0 11.0 11.0])
                (api/replace-missing :a :lerp)
                :a
                (map approx
                     [2.0 2.0 2.0
                      3.5714 5.1429 6.7143 8.2857 9.8571 11.4286
                      13.0 8.0 3.0 4.0 5.0 5.0])
                (api/replace-missing :b :lerp)
                :b))

(fact "strategy-lerp-time"
      (let [dtds (api/dataset {:dt [(java.time.LocalDateTime/of 2020 1 1 1 1 1)
                                    nil nil nil
                                    (java.time.LocalDateTime/of 2020 10 1 1 1 1)]})]
        [(java.time.LocalDateTime/of 2020 1 1 1 1 1)
         (java.time.LocalDateTime/of 2020 3 9 13 1 1)
         (java.time.LocalDateTime/of 2020 5 17 1 1 1)
         (java.time.LocalDateTime/of 2020 7 24 13 1 1)
         (java.time.LocalDateTime/of 2020 10 1 1 1 1)]
        =>
        (seq ((api/replace-missing dtds :lerp) :dt))))

(fact "strategy-value"
      (tabular (fact (-> ds ?cmd (api/column ?col) seq)
                     => ?xs)
               ?xs ?cmd ?col
               [0.0 0.0 0.0 1.0 2.0 0.0 0.0 0.0 0.0 0.0 4.0 0.0 11.0 0.0 0.0]
               (api/replace-missing :a :value 0.0)
               :a
               [4.5 4.5 4.5 1.0 2.0 4.5 4.5 4.5 4.5 4.5 4.0 4.5 11.0 4.5 4.5]
               (api/replace-missing :a :value dfn/mean)
               :a
               [-10.0 -20.0 -10.0 1.0 2.0
                -20.0 -10.0 -20.0 -10.0 -20.0 4.0 -10.0 11.0 -20.0 -10.0]
               (api/replace-missing :a :value [-10.0 -20.0])
               :a
               [nil 100.0 nil 1.0 2.0
                nil nil nil nil
                -100.0 4.0 nil 11.0 nil nil]
               (api/replace-missing :a :value {1 100.0 9 -100.0})
               :a))

;; otfrom case: https://clojurians.zulipchat.com/#narrow/stream/151924-data-science/topic/Simple.20Tablecloth.20beginnings/near/249217023

(fact "replace missing values in grouped dataset"
      (fact (-> (api/dataset [{:calendar-year 2022 :age 49 :location "Barry Buddon" :hours-cycled 1.0}
                              {:calendar-year 2024 :age 49 :location "Barry Buddon" :hours-cycled 2.4}
                              {:calendar-year 2022 :age 49 :location "Tentsmuir Woods" :hours-cycled 3.2}
                              {:calendar-year 2024 :age 49 :location "Tentsmuir Woods" :hours-cycled 1.4}])
                (api/group-by [:age :location])
                (api/fill-range-replace :calendar-year 1 nil nil)
                (api/replace-missing :location :down)
                (api/replace-missing :age :down)
                (api/replace-missing :hours-cycled :value 0.0)
                (api/ungroup)
                (api/rows))
            => [[1.0 2022.0 "Barry Buddon" 49] [0.0 2023.0 "Barry Buddon" 49] [2.4 2024.0 "Barry Buddon" 49] [3.2 2022.0 "Tentsmuir Woods" 49] [0.0 2023.0 "Tentsmuir Woods" 49] [1.4 2024.0 "Tentsmuir Woods" 49]]))

