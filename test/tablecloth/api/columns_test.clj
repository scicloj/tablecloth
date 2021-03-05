(ns tablecloth.api.columns-test
  (:require [tablecloth.api :as api]
            [tech.v3.datatype :as dtype]
            [midje.sweet :refer [tabular fact => =not=>]]
            [tablecloth.common-test :refer [cloned-reader-ds non-cloned-reader-ds]]
            [tech.v3.datatype.functional :refer [pow]]))


;; https://github.com/scicloj/tablecloth/issues/9
(def dss (api/dataset {:idx [1 1 1 2 2 2 3 3 3]
                       :a ["a" "b" "c" "a" "b" "c" "a" "b" "c"]
                       "z" 1
                       :b [1 2 3 2 3 4 3 2 1]
                       :c [3 1 2 4 2 1 3 2 4]}))

(fact "reorder-columns"
      (tabular (fact (-> dss
                         (api/reorder-columns ?order)
                         (api/column-names))
                     =>
                     ?expected)
               ?order        ?expected
               [:ids :b :a :c]       [:b :a :c :idx "z"]
               [:idx :b :a "z" :c]   [:idx :b :a "z" :c]
               [:idx :b :a :C]       [:idx :b :a "z" :c]
               [:c :A :b :e :z :idx] [:c :b :idx :a "z"]
               string?               ["z" :idx :a :b :c]
               #".*[az]$"            [:a "z" :idx :b :c])
      (fact [:b :a :c :idx "z"]
            =>
            (-> dss
                (api/reorder-columns :b :a [:c :ids])
                (api/column-names))))

(fact "add-or-replace"
      (tabular (fact (-> {:x [1 2]}
                         (api/dataset)
                         (api/add-or-replace-column :y ?v)
                         :y
                         (dtype/get-datatype))
                     =>
                     ?expected)
               ?expected ?v
               :int64   1
               :float64 1.0
               :string  "abc"))

(fact "add"
      (tabular (fact (-> {:x [1 2]}
                         (api/dataset)
                         (api/add-column :y ?v)
                         :y
                         (dtype/get-datatype))
                     =>
                     ?expected)
               ?expected ?v
               :int64   1
               :float64 1.0
               :string  "abc"))

(fact "add-column" (as-> cloned-reader-ds $
                         (api/add-column $ :z (dtype/make-reader :float32 5 (rand)))
                         (fact "column values must match by default"
                               (first ($ :z)) => (first ($ :z)))))

(fact "add-column" (as-> cloned-reader-ds $
                         (api/add-column $ :z (dtype/make-reader :float32 5 (rand))
                                     {:prevent-clone? true})
                         (fact "column values must NOT match when :prevent-clone? is true"
                               (first ($ :z)) =not=> (first ($ :z)))))

(fact "add-columns" (as-> cloned-reader-ds $
                          (api/add-columns $ {:c (dtype/make-reader :float32 5 (rand))
                                          :d (dtype/make-reader :float32 5 (rand-int 10))})
                          (fact "column values must match by default"
                                (first ($ :c)) => (first ($ :c))
                                (first ($ :d)) => (first ($ :d)))))

(fact "add-columns" (as-> cloned-reader-ds $
                          (api/add-columns $ {:c (dtype/make-reader :float32 5 (rand))
                                          :d (dtype/make-reader :float32 5 (rand-int 10))}
                                       {:prevent-clone? true})
                          (fact "column values must NOT match when :prevent-clone? is true"
                                (first ($ :c)) =not=> (first ($ :c))
                                (first ($ :d)) =not=> (first ($ :d)))))

(fact "map-columns" (as-> cloned-reader-ds $
                          (api/map-columns $ :z
                                       (api/column-names $ #{:float32} :datatype)
                                       (fn [& rows]
                                         (rand)))
                          (fact "column values must match by default"
                                (first ($ :z)) => (first ($ :z)))))

(fact "map-columns" (as-> cloned-reader-ds $
                          (api/map-columns $ :z
                                       (api/column-names $ #{:float32} :datatype)
                                       (fn [& rows]
                                         (rand))
                                       {:prevent-clone? true})
                          (fact "column values must NOT match when :prevent-clone? is true"
                                (first ($ :z)) =not=> (first ($ :z)))))

(fact "update-columns with columns-map" (as-> non-cloned-reader-ds $
                                              (api/update-columns $ {:y #(pow % 2)})
                                              (fact "column values must match by default"
                                                    (first ($ :y)) => (first ($ :y)))))

(fact "update-columns with columns-map" (as-> non-cloned-reader-ds $
                                              (api/update-columns $ {:y #(pow % 2)}
                                                              {:prevent-clone? true})
                                              (fact "column values must NOT match when :prevent-clone? is true"
                                                    (first ($ :y)) =not=> (first ($ :y)))))

(fact "update-columns with columns-selector" (as-> non-cloned-reader-ds $
                                                   (api/update-columns $ :all #(pow % 2))
                                                   (fact "column values must match by default"
                                                         (first ($ :x)) => (first ($ :x))
                                                         (first ($ :y)) => (first ($ :y)))))

(fact "update-columns with columns-selector" (as-> non-cloned-reader-ds $
                                                   (api/update-columns $ :all #(pow % 2)
                                                                   {:prevent-clone? true})
                                                   (fact "column values must NOT match when :prevent-clone? is true"
                                                         (first ($ :x)) =not=> (first ($ :x))
                                                         (first ($ :y)) =not=> (first ($ :y)))))
