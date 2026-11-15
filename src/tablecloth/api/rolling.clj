(ns tablecloth.api.rolling
  (:require [tech.v3.dataset.rolling :as rolling]))

;; TODO is this the same as the "cumsum" from operators ?
(defn cumsum-col
  "Compute the cumulative sum of a column

  ## Usage

  `(cumsum ds column-name)`

  `(cumsum ds new-column-name column-name)`

  ## Arguments

  - `ds` - A `tech.ml.dataset` (i.e a `tablecloth` dataset)
  - `new-column-name` - __optional__ Name for the column where newly computed values will go.
    When ommitted new column name defaults to the keyword `<old-column-name>-cumulative-sum`
  - `column-name` - Name of the column to use to compute the cumulative sum

  ## Returns

  A dataset with the additional column containing the cumulative sum."
  ([ds column-name]
   (let [new-column-name (-> column-name
                             name
                             (str "-cumulative-sum")
                             keyword)]
     (cumsum-col ds new-column-name column-name)))
  ([ds new-column-name column-name]
   (rolling/expanding ds {new-column-name (rolling/sum column-name)})))
