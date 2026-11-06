(ns tablecloth.api.lag-lead
  (:require [tablecloth.api.columns :as columns]
            [tablecloth.column.api.lag-lead :as lag-lead]))

(defn lag
  "Compute previous (lagged) values from one column in a new column, can be used e.g. to compare values behind the current value.

  ## Usage

  `(lag ds column-name lag-size)`

  `(lag ds new-column-name column-name lag-size)`

  ## Arguments

  - `ds` - A `tech.ml.dataset` (i.e a `tablecloth` dataset)
  - `new-column-name` - __optional__ Name for the column where newly computed values will go.
    When ommitted new column name defaults to the keyword `<old-column-name>-lag-<lag-size>`
  - `column-name` - Name of the column to use to compute the lagged values
  - `lag-size` - positive integer indicating how many rows to skip over to compute the lag

  ## Returns

  A dataset with the new column populated with the nil missing values.
  Optionally, specify fill values.
  "
  ([ds column-name lag-size]
   (let [new-column-name (-> column-name
                             name
                             (str "-lag-" lag-size)
                             keyword)]
     (lag ds new-column-name column-name lag-size {})))
  ([ds new-column-name column-name lag-size]
   (lag ds new-column-name column-name lag-size {}))
  ([ds new-column-name column-name lag-size options]
   (let [lagged (lag-lead/lag (ds column-name) lag-size options)]
     (columns/add-column ds new-column-name lagged))))

(defn lead
  "Compute next (lead) values from one column in a new column, can be used e.g. to compare values ahead of the current value.

  ## Usage

  `(lead ds column-name lead-size)`

  `(lead ds new-column-name column-name lead-size)`

  `(lead ds new-column-name column-name lead-size options)`

  ## Arguments

  - `ds` - A `tech.ml.dataset` (i.e a `tablecloth` dataset)
  - `new-column-name` - __optional__ Name for the column where newly computed values will go.
    When ommitted new column name defaults to the keyword `<old-column-name>-lead-<lead-size>`
  - `column-name` - Name of the column to use to compute the lead values
  - `lead-size` - positive integer indicating how many rows to skip over to compute the lead
  - `options` - __optional__ map; `:fill-with` fills the vacated rows instead of leaving them missing

  ## Returns

  A dataset with the new column populated with the nil missing values.
  Optionally, specify fill values.
  "
  ([ds column-name lead-size]
   (let [new-column-name (-> column-name
                             name
                             (str "-lead-" lead-size)
                             keyword)]
     (lead ds new-column-name column-name lead-size {})))
  ([ds new-column-name column-name lead-size]
   (lead ds new-column-name column-name lead-size {}))
  ([ds new-column-name column-name lead-size options]
   (let [led (lag-lead/lead (ds column-name) lead-size options)]
     (columns/add-column ds new-column-name led))))
