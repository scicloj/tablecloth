;; # Tablecloth Column Exploration

^{:kind/hidden true}
(ns intro
  (:require [tablecloth.api :as tc]
            [scicloj.clay.v1.api :as clay]
            [scicloj.clay.v1.tools :as tools]
            [scicloj.clay.v1.tool.scittle :as scittle]
            [scicloj.kindly.v2.kind :as kind]
            [scicloj.clay.v1.view.dataset]
            [nextjournal.clerk :as clerk]))

^{:kind/hidden true}
(clay/start! {:tools [tools/scittle
                      tools/clerk]})

^{:kind/hidden true}
(comment
  (clerk/show!)

  (do (scittle/show-doc! "docs/column_exploration.clj" {:hide-doc? true})
      (scittle/write-html! "docs/column_exploration.html"))
  ,)

;; ## What is this exploration?
;; 
;; We want to add a `column` entity to tablecloth that parallels `dataset`. It will make
;; the column a first-class entity within tablecloth.

;; ## Usage

(require '[tablecloth.api.column :refer [column] :as col])

;; ### Column creation

;; We can create an empty column like this:

(column)

;; We can check if it it's a column.

(col/column? (column))

;; We can create a columns with data in a number of ways

(column [1 2 3 4])

(column (range 10))

