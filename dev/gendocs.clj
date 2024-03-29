(ns gendocs
  (:require [scicloj.clay.v2.api :as clay]))

(clay/make! {:format [:quarto :html]
             :source-path "notebooks/index.clj"})
