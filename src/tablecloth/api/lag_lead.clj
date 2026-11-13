(ns tablecloth.api.lag-lead
  (:require [tablecloth.api.columns :as columns]
            [tablecloth.column.api.lag-lead :as lag-lead]))

(defn lag
  "Compute previous (lagged) values from one column in a new column, can be used e.g. to compare values behind the current value.

  ## Usage

  `(lag ds column-name lag-size)`

  `(lag ds new-column-name column-name lag-size)`

  `(lag ds new-column-name column-name lag-size options)`

  ## Arguments

  - `ds` - A `tech.ml.dataset` (i.e a `tablecloth` dataset)
  - `new-column-name` - __optional__ Name for the column where newly computed values will go.
    When ommitted new column name defaults to the keyword `<old-column-name>-lag-<lag-size>`
  - `column-name` - Name of the column to use to compute the lagged values
  - `lag-size` - non-negative integer, the number of rows to shift forward. To shift the
    other way, use `lead`; negative sizes throw.
  - `options` - __optional__ map; `:fill-with` puts the given value in the vacated rows
    instead of leaving them missing. Use `{:fill-with 0}` for the zero padding of earlier
    releases. The value is converted to the column datatype: integer columns take any
    number that converts exactly and fits the range (2.0 works, 2.5 does not), float
    columns any number they can represent, and other columns only a value of the same
    type (`:object` columns take anything). Otherwise this throws.

  ## Returns

  A dataset with the new column added. Row `i` holds the source value from row `i - lag-size`.
  The first `lag-size` rows are missing unless `:fill-with` is given. The column keeps the
  source datatype, with or without a fill, and source missing values stay missing.
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
  - `lead-size` - non-negative integer, the number of rows to shift backward. To shift the
    other way, use `lag`; negative sizes throw.
  - `options` - __optional__ map; `:fill-with` puts the given value in the vacated rows
    instead of leaving them missing. Use `{:fill-with 0}` for the zero padding of earlier
    releases. The value is converted to the column datatype: integer columns take any
    number that converts exactly and fits the range (2.0 works, 2.5 does not), float
    columns any number they can represent, and other columns only a value of the same
    type (`:object` columns take anything). Otherwise this throws.

  ## Returns

  A dataset with the new column added. Row `i` holds the source value from row `i + lead-size`.
  The last `lead-size` rows are missing unless `:fill-with` is given. The column keeps the
  source datatype, with or without a fill, and source missing values stay missing.
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
