(ns tablecloth.playground
  (:require [tablecloth.api :as api]))

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
      (api/dataset)))

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
