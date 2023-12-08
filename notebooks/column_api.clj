(ns column-api
  (:require [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]
            [scicloj.kindly.v4.kind :as kind :refer [md]]))

(tc/dataset {:a [1 2 3]
             :b [4 5 6]})

(md "## title")


;; # this is a comment

(tcc/column [1 2 3 4])

;; look at quarto docs for things like TOC
