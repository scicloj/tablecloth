(ns tablecloth.api.missing
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.math :as dm]
            
            [tablecloth.api.utils :refer [column-names grouped? process-group-data]]
            [tablecloth.api.columns :refer [select-columns]]))

(defn- select-or-drop-missing
  "Select rows with missing values"
  ([f ds] (select-or-drop-missing f ds nil))
  ([f ds columns-selector]
   (if (grouped? ds)
     (process-group-data ds #(select-or-drop-missing f % columns-selector))

     (let [ds- (if columns-selector
                 (select-columns ds columns-selector)
                 ds)]
       (f ds (ds/missing ds-))))))

(defn- select-or-drop-missing-docstring
  [op]
  (str op " rows with missing values

 `columns-selector` selects columns to look at missing values"))

(def ^{:doc (select-or-drop-missing-docstring "Select")
       :arglists '([ds] [ds columns-selector])}
  select-missing (partial select-or-drop-missing ds/select-rows))

(def ^{:doc (select-or-drop-missing-docstring "Drop")
       :arglists '([ds] [ds columns-selector])}
  drop-missing (partial select-or-drop-missing ds/drop-rows))

(defn replace-missing
  "Replaces missing values. Accepts

  * dataset
  * column selector, default: :all
  * strategy, default: :nearest
  * value (optional)
  * single value
  * sequence of values (cycled)
  * function, applied on column(s) with stripped missings

  Strategies are:

:value - replace with given value
:up - copy values up
:down - copy values down
:updown - copy values up and then down for missing values at the end
:downup - copy values down and then up for missing values at the beginning
:mid or :nearest - copy values around known values
:midpoint - use average value from previous and next non-missing
:lerp - trying to lineary approximate values, works for numbers and datetime, otherwise applies :nearest. For numbers always results in float datatype.
  "
  ([ds] (replace-missing ds :mid))
  ([ds strategy] (replace-missing ds :all strategy))
  ([ds columns-selector strategy] (replace-missing ds columns-selector strategy nil))
  ([ds columns-selector strategy value]

   (if (grouped? ds)

     (process-group-data ds #(replace-missing % columns-selector strategy value))
     
     (let [cols (column-names ds columns-selector)]
       (ds/replace-missing ds cols strategy value)))))

(defn fill-range-replace
  "Fill missing up with lacking values. Accepts
  * dataset
  * column name
  * expected step (max-span, milliseconds in case of datetime column)
  * (optional) missing-strategy - how to replace missing, default :down (set to nil if none)
  * (optional) missing-value - optional value for replace missing
"
  ([ds colname max-span] (fill-range-replace ds colname max-span :down))
  ([ds colname max-span missing-strategy] (fill-range-replace ds colname max-span missing-strategy nil))
  ([ds colname max-span missing-strategy missing-value]
   (if (grouped? ds)
     (process-group-data ds #(fill-range-replace % colname max-span missing-strategy missing-value))
     (dm/fill-range-replace ds colname max-span missing-strategy missing-value))))

