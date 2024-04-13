;; # Tablecloth Column Exploration

^{:kind/hidden true}
(ns intro
  (:require [tablecloth.api :as tc]
            [scicloj.clay.v2.api :as clay]
            [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]))

^{:kind/hidden true}
(clay/start!)

^{:kind/hidden true}
(comment
  (clay/show-doc! "docs/column_exploration.clj" {:hide-doc? true})
  (clay/write-html! "docs/column_exploration.html")
  ,)

;; ## What is this exploration?
;; 
;; We want to add a `column` entity to tablecloth that parallels `dataset`. It will make
;; the column a first-class entity within tablecloth.

;; ## Usage

(require '[tablecloth.column.api :refer [column] :as col])

;; ### Column creation

;; We can create an empty column like this:

(column)

;; We can check if it it's a column.

(col/column? (column))

;; We can create a columns with data in a number of ways

(column [1 2 3 4])

(column (range 10))

;; When you do this the types of the resulting array is determined
;; automatically from the items provided.

(let [int-column (column (range 10))]
  (col/typeof int-column))

(let [string-column (column ["foo" "bar"])]
  (col/typeof string-column))


;; ### Basic Operations

;; Operations are right now in their own namespace
(require '[tablecloth.column.api.operators :as ops])

;; With that imported we can perform a large number of operations:

(def a (column [20 30 40 50]))
(def b (column (range 4)))

(ops/- a b)

(ops/pow a 2)

(ops/* 10 (ops/sin a))

(ops/< a 35)

;; All these operations take a column as their first argument and
;; return a column, so they can be chained easily.

(-> a
    (ops/* b)
    (ops/< 70))

;; ### Subsetting and accesssing

;; You can access an element in a column in exactly the same ways you
;; would in Clojure.

(def myclm (column (range 5)))

myclm

(myclm 2)

(nth myclm 2)

(get myclm 2)

;; #### Selecting multiple elements

;; There are two ways to select multiple elements from a column:
;;   * If you need to select a continuous subset, you can use `slice`;
;;   * if you may need to select diverse elements, use `select`.
;;

;; **Slice**

;; The `slice` method allows you to use indexes to specify a portion
;; of the column to extract.

(def myclm
  (column (repeatedly 10 #(rand-int 10))))

myclm

(col/slice myclm 3 5)


;; It also supports negative indexing, making it possible to slice
;; from the end of the column:

(col/slice myclm -7 -5)

;; It's also possible to slice from one direction to the beginning or
;; end:

(col/slice myclm 7 :end)

(col/slice myclm -3 :end)

(col/slice myclm :start 7)

(col/slice myclm :start -3)

;; **Select**
;;
;; The `select` fn works by taking a list of index positions:

(col/select myclm [1 3 5 8])

;; We can combine this type of selection with the operations just
;; demonstrated to select certain values.


myclm

;; Let's see which positions are greter than 5.
(ops/> myclm 5)


;; We can use a column of boolean values like the one above with the `select` function as well. `select` will choose all the positions that are true. It's like supplying select a list of the index positions that hold true values.
(col/select myclm (ops/> myclm 5))


;; ### Iterating over a column

;; Many operations that you might want to perform on a column are
;; available in the `tablecloth.column.api.operators` namespace.
;; However, when there is a need to do something custom, you can also
;; interate over the column.

(defn calc-percent [x]
  (/ x 100.0))

(col/column-map myclm calc-percent)

;; It's also possible to iterate over multiple columns by supplying a
;; vector of columns:

(-> [(column [5 6 7 8 9])
     (column [1 2 3 4 5])]
    (col/column-map (partial *)))


(comment 
  (-> (column [1 nil 2 3 nil 0])
      (ops/* 10))

  (-> (column [1 nil 2 3 nil 0])
      (ops/max [10 10 10 10 10 10]))


  (tech.v3.dataset.column/missing))

;; ### Sorting a column

;; You can use `sort-column` to sort a colum

(def myclm (column (repeatedly 10 #(rand-int 100))))

myclm

(col/sort-column myclm)

;; As you can see, sort-columns sorts in ascending order by default,
;; but you can also specify a different order using ordering keywords
;; `:asc` and `:desc`:


(col/sort-column myclm :desc)

;; Finally, sort can also accept a `comparator-fn`:

(let [c (column ["1" "100" "4" "-10"])]
  (col/sort-column c (fn [a b]
                       (let [a (parse-long a)
                             b (parse-long b)]
                         (< a b)))))


;; ### Missing values

;; The column has built-in support for basic awareness and handling of
;; missing values. Columns will be scanned for missing values when
;; created.

(def myclm (column [10 nil -4 20 1000 nil -233]))

;; You can identify the set of index positions of missing values:

(col/missing myclm)

(col/count-missing myclm)

;; You can remove missing values:

(col/drop-missing myclm)

;; Or you can replace them:

(col/replace-missing myclm)

;; There are a range of built-in strategies:

(col/replace-missing myclm :midpoint)


;; And you can provide your own value using a specific value or fn:

(col/replace-missing myclm :value 555)

(col/replace-missing myclm :value (fn [col-without-missing]
                                    (ops/mean col-without-missing)))
