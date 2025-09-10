(ns gendocs
  (:require [scicloj.clay.v2.api :as clay]))

(clay/make! {:format [:quarto :html]
             :source-path "notebooks/index.clj"
             :base-target-path "docs"})

;; beta54
(clay/make! {:format [:quarto :html]
             :base-source-path "notebooks"
             :source-path "index.clj"
             :base-target-path "docs"})
