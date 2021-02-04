(ns tablecloth.transfrom.linear
  "Various linear transformations"
  (:require [tablecloth.api.dataset :refer [rows dataset columns]]
            [tablecloth.api.columns :refer [->array]]
            [tech.v3.dataset.column :as col]
            [tech.v3.dataset :as ds]))

(dataset "data/iris.csv")
