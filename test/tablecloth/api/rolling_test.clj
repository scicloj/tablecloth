(ns tablecloth.api.rolling-test
  (:require [tablecloth.api :as tc]
            [midje.sweet :refer [fact =>]]
            [tablecloth.api.rolling :as sut]))

(fact "lag-test"
      (fact "with valid arguments"
            (fact "fills in 0 for lagging numeric values"
                  (-> (tc/dataset {:C1 [1 2 3 4 5]})
                      (sut/lag :lag-1 :C1 1)
                      (tc/columns :as-map)) =>
                  {:C1 [1 2 3 4 5]
                   :lag-1 [0 1 2 3 4]}))

      (fact "lags by given number of rows"
            (-> (tc/dataset {:C1 [1 2 3 4 5]})
                (sut/lag :lag-1 :C1 3)
                (tc/columns :as-map))
            =>
            {:C1 [1 2 3 4 5]
             :lag-1 [0 0 0 1 2]})

      (fact "generates new column name if not given one"
            (-> (tc/dataset {:C1 [1 2 3 4 5]})
                (sut/lag :C1 2)
                (tc/columns :as-map)) =>
            {:C1 [1 2 3 4 5]
             :C1-lag-2 [0 0 1 2 3]})

      (fact "fills in `nil` for lagging non-numeric values"
            (-> (tc/dataset {:C1 ["a" "b" "c" "d"]})
                (sut/lag :lag-1 :C1 1)
                (tc/columns :as-map)) =>
            {:C1 ["a" "b" "c" "d"]
             :lag-1 [nil "a" "b" "c"]}))

(fact "lead-test"
      (fact "with valid-input"
            (fact "fills in 0 for leading numeric values"
                  (-> (tc/dataset {:C1 [1 2 3 4 5]})
                      (sut/lead :lead-1 :C1 1)
                      (tc/columns :as-map)) =>
                  {:C1 [1 2 3 4 5]
                   :lead-1 [2 3 4 5 0]})


            (fact "generates new column name if not given one"

                  (-> (tc/dataset {:C1 [1 2 3 4 5]})
                      (sut/lead :C1 2)
                      (tc/columns :as-map)) =>
                  {:C1 [1 2 3 4 5]
                   :C1-lead-2 [3 4 5 0 0]})

            (fact "fills in `nil` for leading non-numeric values"
                  (-> (tc/dataset {:C1 ["a" "b" "c" "d" "e"]})
                      (sut/lead :lead-3 :C1 3)
                      (tc/columns :as-map)) =>
                  {:C1 ["a" "b" "c" "d" "e"]
                   :lead-3 ["d" "e" nil nil nil]})))

