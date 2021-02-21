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
  ([ds] (replace-missing ds :mid))
  ([ds strategy] (replace-missing ds :all strategy))
  ([ds columns-selector strategy] (replace-missing ds columns-selector strategy nil))
  ([ds columns-selector strategy value]

   (if (grouped? ds)

     (process-group-data ds #(replace-missing % columns-selector value strategy))
     
     (let [cols (column-names ds columns-selector)]
       (ds/replace-missing ds cols strategy value)))))

(defn fill-range-replace
  ([ds colname max-span] (fill-range-replace ds colname max-span :down))
  ([ds colname max-span missing-strategy] (fill-range-replace ds colname max-span missing-strategy nil))
  ([ds colname max-span missing-strategy missing-value]
   (if (grouped? ds)
     (process-group-data ds #(fill-range-replace % colname max-span missing-strategy missing-value))
     (dm/fill-range-replace ds colname max-span missing-strategy missing-value))))


