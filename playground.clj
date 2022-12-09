(ns tablecloth.playground
  (:require [tablecloth.api :as tc]
            [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as c]
            [tech.v3.dataset.join :as j]
            [tech.v3.datatype.functional :as dfn]
            [clojure.string :as str]
            [clojure.test :as t]))

(ds/concat
 (ds/new-dataset [(c/new-column :a [])])
 (ds/new-dataset [(c/new-column :a [1 2 3])]))


(-> (tc/dataset {} {:column-names [:a :b]}))

(defn cartesian-product
  [xxs]
  (if (seq xxs)
    (for [n (cartesian-product (rest xxs))
          x (first xxs)]
      (conj n x))
    '(nil)))

(defn expand-grid
  [in]
  (-> (map (partial zipmap (keys in))
           (cartesian-product (vals in)))
      (tc/dataset)))

(def input {:height (range 60 81 5)
            :weight (range 100 301 50)
            :sex [:Male :Female]})

(expand-grid input)
;; => _unnamed [50 3]:
;;    |  :sex | :height | :weight |
;;    |-------|--------:|--------:|
;;    | :Male |      60 |     100 |
;;    | :Male |      65 |     100 |
;;    | :Male |      70 |     100 |
;;    | :Male |      75 |     100 |
;;    | :Male |      80 |     100 |
;;    | :Male |      60 |     150 |
;;    | :Male |      65 |     150 |
;;    | :Male |      70 |     150 |
;;    | :Male |      75 |     150 |
;;    | :Male |      80 |     150 |
;;    | :Male |      60 |     200 |
;;    | :Male |      65 |     200 |
;;    | :Male |      70 |     200 |
;;    | :Male |      75 |     200 |
;;    | :Male |      80 |     200 |
;;    | :Male |      60 |     250 |
;;    | :Male |      65 |     250 |
;;    | :Male |      70 |     250 |
;;    | :Male |      75 |     250 |
;;    | :Male |      80 |     250 |
;;    | :Male |      60 |     300 |
;;    | :Male |      65 |     300 |
;;    | :Male |      70 |     300 |
;;    | :Male |      75 |     300 |
;;    | :Male |      80 |     300 |

(def ds (-> {:tags [["app" "mobile" "travel"] ["business"] ["tool" "automation" "macos"]
                  ["mobile" "macos"] ["travel" "app" "macos"]]}
          (tc/dataset)
          (tc/add-column :id (range)))) ;; we need to add artificial column

ds
;; => _unnamed [5 2]:
;;    |                         :tags | :id |
;;    |-------------------------------|----:|
;;    |     ["app" "mobile" "travel"] |   0 |
;;    |                  ["business"] |   1 |
;;    | ["tool" "automation" "macos"] |   2 |
;;    |            ["mobile" "macos"] |   3 |
;;    |      ["travel" "app" "macos"] |   4 |


(def unrolled-ds (-> (tc/unroll ds [:tags])
                   (tc/add-column :exists? true)))

unrolled-ds
;; => _unnamed [12 3]:
;;    | :id |      :tags | :exists? |
;;    |----:|------------|----------|
;;    |   0 |        app |     true |
;;    |   0 |     mobile |     true |
;;    |   0 |     travel |     true |
;;    |   1 |   business |     true |
;;    |   2 |       tool |     true |
;;    |   2 | automation |     true |
;;    |   2 |      macos |     true |
;;    |   3 |     mobile |     true |
;;    |   3 |      macos |     true |
;;    |   4 |     travel |     true |
;;    |   4 |        app |     true |
;;    |   4 |      macos |     true |

(tc/pivot->wider unrolled-ds :tags :exists? {:drop-missing? false})
;; => _unnamed [5 8]:
;;    | :id |  app | mobile | travel | business | tool | automation | macos |
;;    |----:|------|--------|--------|----------|------|------------|-------|
;;    |   2 |      |        |        |          | true |       true |  true |
;;    |   3 |      |   true |        |          |      |            |  true |
;;    |   4 | true |        |   true |          |      |            |  true |
;;    |   1 |      |        |        |     true |      |            |       |
;;    |   0 | true |   true |   true |          |      |            |       |


(-> (tc/pivot->wider unrolled-ds :tags :exists? {:drop-missing? false})
    (tc/replace-missing :all :value false))
;; => _unnamed [5 8]:
;;    | :id |   app | mobile | travel | business |  tool | automation | macos |
;;    |----:|-------|--------|--------|----------|-------|------------|-------|
;;    |   2 | false |  false |  false |    false |  true |       true |  true |
;;    |   3 | false |   true |  false |    false | false |      false |  true |
;;    |   4 |  true |  false |   true |    false | false |      false |  true |
;;    |   1 | false |  false |  false |     true | false |      false | false |
;;    |   0 |  true |   true |   true |    false | false |      false | false |


(-> (tech.v3.dataset/unroll-column ds :tags)
    (tech.v3.dataset/categorical->one-hot [:tags]))

(def data (tech.v3.dataset/->dataset {:a [[1 2 3] [4 5] [6 7 8]]}))

data
;; => _unnamed [3 1]:
;;    |      :a |
;;    |---------|
;;    | [1 2 3] |
;;    |   [4 5] |
;;    | [6 7 8] |

(tech.v3.dataset/unroll-column data :a)
;; exception

(def data2 (tech.v3.dataset/add-or-update-column data :b [-1 -2 -3]))

data2
;; => _unnamed [3 2]:
;;    |      :a | :b |
;;    |---------|---:|
;;    | [1 2 3] | -1 |
;;    |   [4 5] | -2 |
;;    | [6 7 8] | -3 |

(tech.v3.dataset/unroll-column data2 :a)
;; => _unnamed [8 2]:
;;    | :b | :a |
;;    |---:|---:|
;;    | -1 |  1 |
;;    | -1 |  2 |
;;    | -1 |  3 |
;;    | -2 |  4 |
;;    | -2 |  5 |
;;    | -3 |  6 |
;;    | -3 |  7 |
;;    | -3 |  8 |

(require '[tech.v3.dataset.join :as j])
(def a (tc/dataset {:a [1 2 3] :b [4 5 6]}))
(def b (tc/dataset {:c [:a :b :c] :d [:x :y :z]}))

(j/pd-merge a b {:how :cross})
;; => cross-join [9 4]:
;;    | :a | :b | :c | :d |
;;    |---:|---:|----|----|
;;    |  1 |  4 | :a | :x |
;;    |  2 |  5 | :b | :y |
;;    |  3 |  6 | :c | :z |
;;    |  1 |  4 | :a | :x |
;;    |  2 |  5 | :b | :y |
;;    |  3 |  6 | :c | :z |
;;    |  1 |  4 | :a | :x |
;;    |  2 |  5 | :b | :y |
;;    |  3 |  6 | :c | :z |

;;;;;;;

(def df (tc/dataset {:group [1 2 1]
                   :item-id [1 2 2]
                   :item-name [:a :b :b]
                   :value1 [1 2 3]
                   :value2 [4 5 6]}))

df
;; => _unnamed [3 5]:
;;    | :group | :item-id | :item-name | :value1 | :value2 |
;;    |-------:|---------:|------------|--------:|--------:|
;;    |      1 |        1 |         :a |       1 |       4 |
;;    |      2 |        2 |         :b |       2 |       5 |
;;    |      1 |        2 |         :b |       3 |       6 |

;; cross product of regular column and nested columns
(def tmp (j/pd-merge (-> (tc/select-columns df :group)
                       (tc/unique-by))
                   (-> (tc/select-columns df [:item-id :item-name])
                       (tc/unique-by)) {:how :cross}))

tmp
;; => cross-join [4 3]:
;;    | :group | :item-id | :item-name |
;;    |-------:|---------:|------------|
;;    |      1 |        1 |         :a |
;;    |      1 |        2 |         :b |
;;    |      2 |        1 |         :a |
;;    |      2 |        2 |         :b |

(-> (tc/left-join tmp df [:group :item-id :item-name])
    (tc/select-columns (tc/column-names df)))
;; => left-outer-join [4 5]:
;;    | :group | :item-id | :item-name | :value1 | :value2 |
;;    |-------:|---------:|------------|--------:|--------:|
;;    |      1 |        1 |         :a |       1 |       4 |
;;    |      2 |        2 |         :b |       2 |       5 |
;;    |      1 |        2 |         :b |       3 |       6 |
;;    |      2 |        1 |         :a |         |         |

(-> (tc/left-join tmp df [:group :item-id :item-name])
    (tc/select-columns (tc/column-names df))
    (tc/replace-missing :all :value 0))
;; => left-outer-join [4 5]:
;;    | :group | :item-id | :item-name | :value1 | :value2 |
;;    |-------:|---------:|------------|--------:|--------:|
;;    |      1 |        1 |         :a |       1 |       4 |
;;    |      2 |        2 |         :b |       2 |       5 |
;;    |      1 |        2 |         :b |       3 |       6 |
;;    |      2 |        1 |         :a |       0 |       0 |

;;;


(-> (tc/dataset [{:setting "bar", :calendar-year 2020, :ay 0, :people 2, :need "foo"}
                 {:setting "bar", :calendar-year 2021, :ay 0, :people 4, :need "foo"}
                 {:setting "quux", :calendar-year 2020, :ay 1, :people 1, :need "foo"}
                 {:setting "quux", :calendar-year 2021, :ay 2, :people 3, :need "foo"}
                 {:setting "quux", :calendar-year 2022, :ay 3, :people 6, :need "foo"}])
    (tc/select-rows (comp #{0 1 2} :ay))
    (:ay)
    (sequential?))

;; => _unnamed [4 5]:
;;    | :setting | :calendar-year | :people | :ay | :need |
;;    |----------|---------------:|--------:|----:|-------|
;;    |      bar |           2020 |       2 |   0 |   foo |
;;    |      bar |           2021 |       4 |   0 |   foo |
;;    |     quux |           2020 |       1 |   1 |   foo |
;;    |     quux |           2021 |       3 |   2 |   foo |

(def ds (tc/dataset {:a [1 2 3]
                   :b [:A :B :C]}))

(map? ds);; => true
(seqable? (:a ds));; => true
(sequential? (ds :b));; => true

(-> (tc/dataset [{:a 1 :b 1.1 :c 1.2 :e 1.3}
                 {:a 1 :b 1.1 :c 5 :e 1.3}
                 {:a 2 :b 2.2 :c 3.2 :e 5.3}
                 {:a 3 :b 3.3 :c 4.2 :e 6.3}])
    (tc/group-by [:a :b])
    (tc/unmark-group)
    (tc/select-rows #(> (ds/row-count (:data %)) 1))
    (tc/mark-as-group)
    (tc/ungroup))

;; => _unnamed [2 4]:
;;    |  :b |  :c | :a |  :e |
;;    |----:|----:|---:|----:|
;;    | 1.1 | 1.2 |  1 | 1.3 |
;;    | 1.1 | 5.0 |  1 | 1.3 |

(-> (tc/dataset [{:a 1 :b 1.1 :c 1.2 :e 1.3}
                 {:a 1 :b 1.1 :c 5 :e 1.3}
                 {:a 2 :b 2.2 :c 3.2 :e 5.3}
                 {:a 3 :b 3.3 :c 4.2 :e 6.3}])
    (tc/group-by [:a :b]))

;; => _unnamed [3 3]:
;;    | :group-id |          :name |                        :data |
;;    |----------:|----------------|------------------------------|
;;    |         0 | {:b 1.1, :a 1} | Group: {:b 1.1, :a 1} [2 4]: |
;;    |         1 | {:b 2.2, :a 2} | Group: {:b 2.2, :a 2} [1 4]: |
;;    |         2 | {:b 3.3, :a 3} | Group: {:b 3.3, :a 3} [1 4]: |


;;

(-> (tc/dataset [{"id" 1, "name" "bob"} {"id" 2, "name" "bob"}, {"id" 3, "name" "alice"}])
    (tc/fold-by ["name"] (partial str/join ", ")))

;; => _unnamed [2 2]:
;;    |  name |   id |
;;    |-------|------|
;;    |   bob | 1, 2 |
;;    | alice |    3 |



(def ds (tc/dataset {:group (repeatedly 100 #(rand-nth [:u :i :o :p]))
                   :a (repeatedly 100 rand)
                   :b (repeatedly 100 #(rand 5))}))

ds
;; => _unnamed [100 3]:
;;    | :group |         :a |         :b |
;;    |--------|-----------:|-----------:|
;;    |     :i | 0.21923487 | 0.02757318 |
;;    |     :o | 0.47121141 | 1.03039666 |
;;    |     :i | 0.26676569 | 4.16760503 |
;;    |     :u | 0.38287395 | 2.24760673 |
;;    |     :o | 0.96585848 | 0.01097307 |
;;    |     :o | 0.88531448 | 4.46218351 |
;;    |     :p | 0.19173693 | 3.57395669 |
;;    |     :p | 0.53615392 | 2.74081683 |
;;    |     :p | 0.43978083 | 4.67902393 |
;;    |     :p | 0.72916344 | 1.01818710 |
;;    |    ... |        ... |        ... |
;;    |     :o | 0.75565644 | 0.38405762 |
;;    |     :p | 0.06499388 | 2.39519054 |
;;    |     :p | 0.33801667 | 4.25192711 |
;;    |     :u | 0.76001839 | 3.49197309 |
;;    |     :u | 0.71527541 | 1.33661718 |
;;    |     :u | 0.46813365 | 3.31539727 |
;;    |     :o | 0.90347413 | 0.59158482 |
;;    |     :i | 0.40986820 | 1.26976567 |
;;    |     :u | 0.62212172 | 2.57981844 |
;;    |     :u | 0.77436201 | 1.90787845 |
;;    |     :p | 0.95894755 | 4.35116263 |

(def agg (-> ds
           (tc/group-by [:group])
           (tc/aggregate-columns [:a :b] dfn/mean)
           (tc/rename-columns {:a :mean-a :b :mean-b})))

agg
;; => _unnamed [4 3]:
;;    |    :mean-b | :group |    :mean-a |
;;    |-----------:|--------|-----------:|
;;    | 2.20742202 |     :i | 0.53935031 |
;;    | 2.00721651 |     :o | 0.60963595 |
;;    | 2.69337664 |     :u | 0.48111990 |
;;    | 2.69662835 |     :p | 0.50979658 |

(-> (tc/left-join ds agg :group)
    (tc/drop-columns :right.group))

;; => left-outer-join [100 5]:
;;    | :group |         :a |         :b |    :mean-b |    :mean-a |
;;    |--------|-----------:|-----------:|-----------:|-----------:|
;;    |     :i | 0.21923487 | 0.02757318 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.26676569 | 4.16760503 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.97345184 | 3.68318639 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.19880123 | 1.53596251 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.13353903 | 4.57865084 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.49446615 | 0.12514693 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.69925871 | 0.14210092 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.77344282 | 0.86134163 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.56868174 | 2.86614456 | 2.20742202 | 0.53935031 |
;;    |     :i | 0.37321600 | 4.43636290 | 2.20742202 | 0.53935031 |
;;    |    ... |        ... |        ... |        ... |        ... |
;;    |     :p | 0.13506468 | 1.93568665 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.23051436 | 4.22573832 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.22231685 | 3.38299530 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.98586194 | 0.82699630 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.82490726 | 2.79908028 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.35192255 | 1.06928476 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.79226756 | 2.31301546 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.11991132 | 2.11139863 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.06499388 | 2.39519054 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.33801667 | 4.25192711 | 2.69662835 | 0.50979658 |
;;    |     :p | 0.95894755 | 4.35116263 | 2.69662835 | 0.50979658 |


(tc/add-columns ds {:mean-a (dfn/mean (:a ds))
                    :mean-b (dfn/mean (:b ds))})

;; => _unnamed [100 5]:
;;    | :group |         :a |         :b |    :mean-a |    :mean-b |
;;    |--------|-----------:|-----------:|-----------:|-----------:|
;;    |     :i | 0.21923487 | 0.02757318 | 0.52913219 | 2.43060934 |
;;    |     :o | 0.47121141 | 1.03039666 | 0.52913219 | 2.43060934 |
;;    |     :i | 0.26676569 | 4.16760503 | 0.52913219 | 2.43060934 |
;;    |     :u | 0.38287395 | 2.24760673 | 0.52913219 | 2.43060934 |
;;    |     :o | 0.96585848 | 0.01097307 | 0.52913219 | 2.43060934 |
;;    |     :o | 0.88531448 | 4.46218351 | 0.52913219 | 2.43060934 |
;;    |     :p | 0.19173693 | 3.57395669 | 0.52913219 | 2.43060934 |
;;    |     :p | 0.53615392 | 2.74081683 | 0.52913219 | 2.43060934 |
;;    |     :p | 0.43978083 | 4.67902393 | 0.52913219 | 2.43060934 |
;;    |     :p | 0.72916344 | 1.01818710 | 0.52913219 | 2.43060934 |
;;    |    ... |        ... |        ... |        ... |        ... |
;;    |     :o | 0.75565644 | 0.38405762 | 0.52913219 | 2.43060934 |
;;    |     :p | 0.06499388 | 2.39519054 | 0.52913219 | 2.43060934 |
;;    |     :p | 0.33801667 | 4.25192711 | 0.52913219 | 2.43060934 |
;;    |     :u | 0.76001839 | 3.49197309 | 0.52913219 | 2.43060934 |
;;    |     :u | 0.71527541 | 1.33661718 | 0.52913219 | 2.43060934 |
;;    |     :u | 0.46813365 | 3.31539727 | 0.52913219 | 2.43060934 |
;;    |     :o | 0.90347413 | 0.59158482 | 0.52913219 | 2.43060934 |
;;    |     :i | 0.40986820 | 1.26976567 | 0.52913219 | 2.43060934 |
;;    |     :u | 0.62212172 | 2.57981844 | 0.52913219 | 2.43060934 |
;;    |     :u | 0.77436201 | 1.90787845 | 0.52913219 | 2.43060934 |
;;    |     :p | 0.95894755 | 4.35116263 | 0.52913219 | 2.43060934 |

(def ds (tc/dataset {:a [1 2 3]
                   :b [99 98 97]
                   :c [:r :t :y]}))

(tc/rows ds) ;; returns vector of rows as vectors
;; => [[1 99 :r] [2 98 :t] [3 97 :y]]
(tc/rows ds :as-maps) ;; returns vector of rows as columns
;; => [{:a 1, :b 99, :c :r} {:a 2, :b 98, :c :t} {:a 3, :b 97, :c :y}]

(tc/columns ds) ;; returns sequence of columns
;; => [#tech.v3.dataset.column<int64>[3]
;;    :a
;;    [1, 2, 3] #tech.v3.dataset.column<int64>[3]
;;    :b
;;    [99, 98, 97] #tech.v3.dataset.column<keyword>[3]
;;    :c
;;    [:r, :t, :y]]

(tc/column ds :c) ;; returns column
;; => #tech.v3.dataset.column<keyword>[3]
;;    :c
;;    [:r, :t, :y]

;; column is a sequence
(seqable? (tc/column ds :c)) ;; => true
(first (tc/column ds :c)) ;; => :r

;; column is a vector
(sequential? (tc/column ds :c)) ;; => true
((tc/column ds :c) 0) ;; => :r

(def DS (tc/dataset {:V1 (take 9 (cycle [1 2]))
                      :V2 (range 1 10)
                      :V3 (take 9 (cycle [0.5 1.0 1.5]))
                      :V4 (take 9 (cycle ["A" "B" "C"]))}))


(tc/separate-column DS :V3 (fn [^double v]
                                 [(int (quot v 1.0))
                                  (mod v 1.0)]))

(-> (tc/dataset {:x [1] :y [[2 3 9 10 11 22 33]]})
    (tc/separate-column :y))
;; => _unnamed [1 8]:
;;    | :x | :y-0 | :y-1 | :y-2 | :y-3 | :y-4 | :y-5 | :y-6 |
;;    |---:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|
;;    |  1 |    2 |    3 |    9 |   10 |   11 |   22 |   33 |

(-> (tc/dataset {:x [1] :y [[2 3 9 10 11 22 33]]})
    (tc/separate-column :y reverse))
;; => _unnamed [1 8]:
;;    | :x | :y-0 | :y-1 | :y-2 | :y-3 | :y-4 | :y-5 | :y-6 |
;;    |---:|-----:|-----:|-----:|-----:|-----:|-----:|-----:|
;;    |  1 |   33 |   22 |   11 |   10 |    9 |    3 |    2 |

(-> (tc/dataset {:x [1] :y [[2 3 9 10 11 22 33]]})
    (tc/separate-column :y (fn [input]
                             (zipmap "somenames" input))))
;; => _unnamed [1 7]:
;;    | :x |  a | s |  e |  m |  n | o |
;;    |---:|---:|--:|---:|---:|---:|--:|
;;    |  1 | 22 | 2 | 10 | 33 | 11 | 3 |


;;


(let [col-names (map (partial str "row-") (range 10))]
  (-> (tc/dataset {:x (range 10)
                   :y (range 100 110)
                   :z (seq "abcdefghij")})
      (tc/rows)
      (->> (zipmap col-names))
      (tc/dataset)
      (tc/select-columns col-names)))

;; => _unnamed [3 10]:
;;    | row-0 | row-1 | row-2 | row-3 | row-4 | row-5 | row-6 | row-7 | row-8 | row-9 |
;;    |-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|
;;    |     0 |     1 |     2 |     3 |     4 |     5 |     6 |     7 |     8 |     9 |
;;    |   100 |   101 |   102 |   103 |   104 |   105 |   106 |   107 |   108 |   109 |
;;    |     a |     b |     c |     d |     e |     f |     g |     h |     i |     j |

(defonce stocks (tc/dataset "https://raw.githubusercontent.com/techascent/tech.ml.dataset/master/test/data/stocks.csv" {:key-fn keyword}))

(def price-index (-> (->> (stocks :price)
                        (map-indexed vector)
                        (group-by second))
                   (update-vals (partial map first))
                   (java.util.TreeMap.)))

;; selection

(-> stocks
    (tc/select-rows (->> (.subMap price-index 10.0 true 20.0 false) ;; select range <10,20)
                         (.values) ;; get indices
                         (mapcat identity))))

;; => https://raw.githubusercontent.com/techascent/tech.ml.dataset/master/test/data/stocks.csv [61 3]:
;;    | :symbol |      :date | :price |
;;    |---------|------------|-------:|
;;    |    AMZN | 2001-02-01 |  10.19 |
;;    |    AMZN | 2001-03-01 |  10.23 |
;;    |    AAPL | 2003-09-01 |  10.36 |
;;    |    AAPL | 2003-11-01 |  10.45 |
;;    |    AAPL | 2003-07-01 |  10.54 |
;;    |    AAPL | 2001-11-01 |  10.65 |
;;    |    AAPL | 2003-12-01 |  10.69 |
;;    |    AAPL | 2001-01-01 |  10.81 |
;;    |    AMZN | 2001-12-01 |  10.82 |
;;    |    AAPL | 2002-02-01 |  10.85 |
;;    |     ... |        ... |    ... |
;;    |    AMZN | 2002-12-01 |  18.89 |
;;    |    MSFT | 2008-12-01 |  18.91 |
;;    |    MSFT | 2003-01-01 |  19.31 |
;;    |    MSFT | 2003-02-01 |  19.34 |
;;    |    AMZN | 2002-10-01 |  19.36 |
;;    |    AAPL | 2004-09-01 |  19.38 |
;;    |    MSFT | 2002-07-01 |  19.52 |
;;    |    MSFT | 2008-11-01 |  19.66 |
;;    |    MSFT | 2003-03-01 |  19.76 |
;;    |    MSFT | 2009-04-01 |  19.84 |
;;    |    MSFT | 2002-08-01 |  19.97 |

(tc/aggregate-columns stocks count)

(-> DS
    (tc/drop-columns :V4)
    (tc/aggregate-columns #(reduce + %)))
