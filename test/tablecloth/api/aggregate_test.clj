(ns tablecloth.api.aggregate-test
  (:require [tablecloth.api :as api]
            [tech.v3.datatype.functional :refer [sum]]
            [midje.sweet :refer [facts fact =>]]))


(fact "aggregate keeps an order of columns"
      (-> (array-map
           :a [1 1]
           :b 2
           :c 3
           :d 4
           :e 5
           :f 6
           :g 7
           :h 8
           :i 9)
          (api/dataset)
          (api/aggregate-columns [:a :b :c :d :e :f :g :h :i] sum)
          (api/column-names))  => '(:a :b :c :d :e :f :g :h :i))
