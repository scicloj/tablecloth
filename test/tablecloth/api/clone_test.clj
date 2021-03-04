(ns tablecloth.api.clone-test
  (:require [midje.sweet :refer [fact => =not=>]]
            [tech.v3.datatype :as dtype]
            [tablecloth.api.utils :refer [column-names clone-columns]]
            [tablecloth.api.dataset :refer [dataset]]
            [tablecloth.api.columns :refer [add-column add-columns map-columns update-columns]]
            [tech.v3.datatype.functional :refer [pow]]))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          (dataset {:prevent-clone? true})
          (clone-columns :all false))]
  (fact "" (first (DS :x)) => (first (DS :x)))
  (fact "" (first (DS :y)) => (first (DS :y))))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          dataset)]
  (fact "" (first (DS :x)) => (first (DS :x)))
  (fact "" (first (DS :y)) => (first (DS :y))))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          (dataset {:prevent-clone? true}))]
  (fact "" (first (DS :x)) =not=> (first (DS :x)))
  (fact "" (first (DS :y)) =not=> (first (DS :y))))

(let [DS
        (-> {:x (dtype/make-reader :float32 9 (rand))
             :y (dtype/make-reader :float32 9 (rand))}
            dataset
            (add-column :z (dtype/make-reader :float32 9 (rand))))]
    (fact "" (first (DS :z)) => (first (DS :z))))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          dataset
          (add-column :z (dtype/make-reader :float32 9 (rand)) {:prevent-clone? true}))]
  (fact "" (first (DS :z)) =not=> (first (DS :z))))

(let [DS
      (-> {:a [1 2 3 4]
           :b [5 6 7 8]}
          (dataset)
          (add-columns {:c (dtype/make-reader :float32 4 (rand))
                        :d (dtype/make-reader :float32 4 (rand-int 10))}))]
  (fact "" (first (DS :c)) => (first (DS :c)))
  (fact "" (first (DS :d)) => (first (DS :d))))

(let [DS
      (-> {:a [1 2 3 4]
           :b [5 6 7 8]}
          (dataset)
          (add-columns {:c (dtype/make-reader :float32 4 (rand))
                        :d (dtype/make-reader :float32 4 (rand-int 10))}
                       {:prevent-clone? true}))]
  (fact "" (first (DS :c)) =not=> (first (DS :c)))
  (fact "" (first (DS :d)) =not=> (first (DS :d))))

(let [DS
      (as-> {:V1 (take 9 (cycle [1 2]))
             :V2 (range 1 10)
             :V3 (take 9 (cycle [0.5 1.0 1.5]))
             :V4 (take 9 (cycle ["A" "B" "C"]))} ds
            (dataset ds)
            (map-columns ds :V5
                         (column-names ds  #{:int64 :float64} :datatype)
                         (fn [& rows]
                           (rand))))]
  (fact "" (first (DS :V5)) => (first (DS :V5))))

(let [DS
      (as-> {:V1 (take 9 (cycle [1 2]))
             :V2 (range 1 10)
             :V3 (take 9 (cycle [0.5 1.0 1.5]))
             :V4 (take 9 (cycle ["A" "B" "C"]))} ds
            (dataset ds)
            (map-columns ds :V5
                         nil
                         (column-names ds  #{:int64 :float64} :datatype)
                         (fn [& rows]
                           (rand))
                         {:prevent-clone? true}))]
  (fact "" (first (DS :V5)) =not=> (first (DS :V5))))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          (dataset {:prevent-clone? true})
          (update-columns {:y #(pow % 2)}))]
  (fact "" (first (DS :x)) =not=> (first (DS :x)))
  (fact "" (first (DS :y)) => (first (DS :y))))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          (dataset {:prevent-clone? true})
          (update-columns {:y #(pow % 2)} {:prevent-clone? true}))]
  (fact "" (first (DS :y)) =not=> (first (DS :y))))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          (dataset {:prevent-clone? true})
          (update-columns :all #(pow % 2)))]
  (fact "" (first (DS :y)) => (first (DS :y))))

(let [DS
      (-> {:x (dtype/make-reader :float32 9 (rand))
           :y (dtype/make-reader :float32 9 (rand))}
          (dataset {:prevent-clone? true})
          (update-columns :all #(pow % 2) {:prevent-clone? true}))]
  (fact "" (first (DS :y)) =not=> (first (DS :y))))
