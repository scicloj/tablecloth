(ns tablecloth.api.join-concat-ds-test
  (:require [tablecloth.api.join-concat-ds :as sut]
            [midje.sweet :as midje :refer [fact tabular =>]]
            [tablecloth.api :as api]))

(def ds1 (api/dataset {:a [1 2 1 2 3 4 nil nil 4]
                     :b (range 101 110)
                     :c (map str "abs tract")}))
(def ds2 (api/dataset {:a [nil 1 2 5 4 3 2 1 nil]
                     :b (range 110 101 -1)
                     :c (map str "datatable")
                     :d (symbol "X")
                     :e [3 4 5 6 7 nil 8 1 1]}))

(fact "anti-join"
      (-> (api/anti-join ds1 ds2 :a)
          (api/rows :as-maps)) => []
      (-> (api/anti-join ds2 ds1 :a)
          (api/rows :as-maps)) => [{:a 5, :b 107, :c "a", :d 'X, :e 6}]
      (-> (api/anti-join ds1 ds2 :b)
          (api/rows :as-maps)) => [{:b 101, :a 1, :c "a"}]
      (-> (api/anti-join ds2 ds1 :b)
          (api/rows :as-maps)) => [{:b 110, :a nil, :c "d", :d 'X, :e 3}]
      (-> (api/anti-join ds1 ds2 :c)
          (api/rows :as-maps)) => [{:c "s", :a 1, :b 103}
                                   {:c " ", :a 2, :b 104}
                                   {:c "r", :a 4, :b 106}
                                   {:c "c", :a nil, :b 108}]
      (-> (api/anti-join ds2 ds1 :c)
          (api/rows :as-maps)) => [{:c "d", :a nil, :b 110, :d 'X, :e 3}
                                   {:c "l", :a 1, :b 103, :d 'X, :e 1}
                                   {:c "e", :a nil, :b 102, :d 'X, :e 1}]
      (-> (api/anti-join ds1 ds2 {:left :a :right :e})
          (api/rows :as-maps)) => [{:a 2, :b 102, :c "b"} {:a 2, :b 104, :c " "}]
      (-> (api/anti-join ds2 ds1 {:left :e :right :a})
          (api/rows :as-maps)) => [{:e 5, :a 2, :b 108, :c "t", :d 'X}
                                   {:e 6, :a 5, :b 107, :c "a", :d 'X}
                                   {:e 7, :a 4, :b 106, :c "t", :d 'X}
                                   {:e 8, :a 2, :b 104, :c "b", :d 'X}]
      (-> (api/anti-join ds1 ds2 [:a :b])
          (api/rows :as-maps)) => [{:a 1, :b 101, :c "a"}
                                   {:a 2, :b 102, :c "b"}
                                   {:a nil, :b 107, :c "a"}
                                   {:a nil, :b 108, :c "c"}
                                   {:a 4, :b 109, :c "t"}]
      (-> (api/anti-join ds2 ds1 [:a :b])
          (api/rows :as-maps)) => [{:a nil, :b 110, :c "d", :d 'X, :e 3}
                                   {:a 1, :b 109, :c "a", :d 'X, :e 4}
                                   {:a 2, :b 108, :c "t", :d 'X, :e 5}
                                   {:a 5, :b 107, :c "a", :d 'X, :e 6}
                                   {:a nil, :b 102, :c "e", :d 'X, :e 1}])

(fact "semi-join"
      (-> (api/semi-join ds1 ds2 :a)
          (api/row-count)) => 9
      (-> (api/semi-join ds2 ds1 :a)
          (api/row-count)) => 8
      (-> (api/semi-join ds1 ds2 :b)
          (api/row-count)) => 8
      (-> (api/semi-join ds2 ds1 :b)
          (api/row-count)) => 8
      (-> (api/semi-join ds1 ds2 :c)
          (api/row-count)) => 5
      (-> (api/semi-join ds2 ds1 :c)
          (api/row-count)) => 6
      (-> (api/semi-join ds1 ds2 {:left :a :right :e})
          (api/row-count)) => 7
      (-> (api/semi-join ds2 ds1 {:left :e :right :a})
          (api/row-count)) => 5
      (-> (api/semi-join ds1 ds2 [:a :b])
          (api/row-count)) => 4
      (-> (api/semi-join ds2 ds1 [:a :b])
          (api/row-count)) => 4)

(fact "eraderna int-string join"
      (-> (api/left-join (-> (api/dataset [{:i "foo" :y 2022}]))
                         (-> (api/dataset [{:i "foo" :y 2022 :s "2022"}
                                           {:i "foo" :y 2023 :s "2023"}]))
                         [:i :y])
          (api/rows :as-maps))  => [{:i "foo", :y 2022, :right.i "foo", :right.y 2022, :s "2022"}]
      (-> (api/left-join (-> (api/dataset [{:i "foo" :y 2022}])
                             (api/convert-types {:y :int16}))
                         (-> (api/dataset [{:i "foo" :y 2022 :s "2022"}
                                           {:i "foo" :y 2023 :s "2023"}]))
                         [:i :y])
          (api/rows :as-maps)) => [{:i "foo", :y 2022, :right.i "foo", :right.y 2022, :s "2022"}])
