(ns tablecloth.playground
  (:require [tablecloth.api :as tc]
            [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as c]
            [tech.v3.dataset.join :as j]))

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
