(ns tablecloth.api.join-separate-test
  (:require [tablecloth.api :as api]
            [midje.sweet :refer [tabular fact =>]]))



(fact "array-column->columns works"
      (-> (api/dataset {:x [(double-array [1 2 3])
                            (double-array [4 5 6])]
                        :y [:a :b]})
          (api/array-column->columns :x)
          (api/rows :as-maps))
      => [{:y :a 0 1.0 1 2.0 2 3.0} {:y :b 0 4.0 1 5.0 2 6.0}])

(fact "array-column->columns works can prefix columns with key-word"
      (-> (api/dataset {:x [(double-array [1 2 3])
                            (double-array [4 5 6])]
                        :y [:a :b]})
          (api/array-column->columns :x {:prefix :col})
          (api/rows :as-maps))
      => [{:y :a
           :col-0 1.0
           :col-1 2.0
           :col-2 3.0}
          {:y :b
           :col-0 4.0
           :col-1 5.0
           :col-2 6.0}])

(fact "array-column->columns works can prefix columns with string"
      (-> (api/dataset {:x [(double-array [1 2 3])
                            (double-array [4 5 6])]
                        :y [:a :b]})
          (api/array-column->columns :x {:prefix "col"})
          (api/rows :as-maps))
      => [{:y :a
           "col-0" 1.0
           "col-1" 2.0
           "col-2" 3.0}
          {:y :b
           "col-0" 4.0
           "col-1" 5.0
           "col-2" 6.0}])

(fact "columns->array columns works"
      (let [ds (api/dataset {0 [0.0 1 2]
                             1 [3.0 4 5]
                             :x [:a :b :c]})
            ds-with-array-column
            (-> ds
                (api/columns->array-column [0 1] :y))]

        (:x ds-with-array-column) => [:a :b :c]
        (->> ds-with-array-column :y (mapv vec))
        => [[0.0 3.0] [1.0 4.0] [2.0 5.0]]))

(fact "false-is-not-missing"
      (-> (api/dataset [{:a "foo" :b true}
                        {:a "bar" :b false}])
          (api/join-columns :join-columns-string [:a :b])

          (api/rows)
          (flatten))
      => ["foo-true" "bar-false"])

(fact "map-column->columns work"
      (->
       (api/dataset {:m [{:a 1 :b 2} {:a 3 :b 4}]
                     "n" [{:a 10 :b 20} {:a 30 :b 40}]})
       (api/map-column->columns :m)
       (api/rows :as-maps))
      => [{"n" {:a 10, :b 20}, :m-a 1, :m-b 2} {"n" {:a 30, :b 40}, :m-a 3, :m-b 4}]



      (->
       (api/dataset {:m [{:a 1 :b 2} {:a 3 :b 4}]
                     "n" [{:a 10 :b 20} {:a 30 :b 40}]})
       (api/map-column->columns "n")
       (api/rows :as-maps))
      => [{:m {:a 1, :b 2}, "n-a" 10, "n-b" 20} {:m {:a 3, :b 4}, "n-a" 30, "n-b" 40}]

      (->
       (api/dataset {:m [{:a 1 :b 2 :d 4} {:a 3 :c "hello"}]})
       (api/map-column->columns :m)
       (api/rows :as-maps))
      => [{:m-a 1, :m-b 2, :m-d 4, :m-c nil} 
          {:m-a 3, :m-b nil, :m-d nil, :m-c "hello"}])
