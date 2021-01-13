(ns tablecloth.api
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])  
  (:require [tech.v3.datatype.export-symbols :as exporter]
            [tech.v3.tensor :as tensor]
            [tech.v3.dataset.tensor :as ds-tensor]))

(exporter/export-symbols tech.v3.datatype
                         clone)

(exporter/export-symbols tech.v3.dataset
                         column-count
                         row-count
                         set-dataset-name
                         dataset-name
                         column
                         has-column?
                         write!
                         concat
                         concat-copying)

(def ^{:deprecated "Use `write!` instead."} write-csv! write!)

(exporter/export-symbols tech.v3.dataset.print
                         dataset->str)

(exporter/export-symbols tablecloth.api.utils
                         column-names
                         write-nippy!
                         read-nippy)

(exporter/export-symbols tablecloth.api.dataset
                         dataset?
                         empty-ds?
                         dataset
                         shape
                         info
                         columns
                         rows
                         print-dataset)

(exporter/export-symbols tablecloth.api.group-by
                         group-by
                         ungroup
                         grouped?
                         unmark-group
                         as-regular-dataset
                         process-group-data
                         groups->seq
                         groups->map)

(exporter/export-symbols tablecloth.api.columns
                         select-columns
                         drop-columns
                         rename-columns
                         add-or-replace-column
                         add-or-replace-columns
                         map-columns
                         update-columns
                         reorder-columns
                         convert-types
                         ->array)

(exporter/export-symbols tablecloth.api.rows
                         select-rows
                         drop-rows
                         head
                         tail
                         shuffle
                         random
                         rand-nth
                         first
                         last
                         by-rank)

(exporter/export-symbols tablecloth.api.aggregate
                         aggregate
                         aggregate-columns)

(exporter/export-symbols tablecloth.api.order-by
                         order-by)

(exporter/export-symbols tablecloth.api.unique-by
                         unique-by)

(exporter/export-symbols tablecloth.api.missing
                         select-missing
                         drop-missing
                         replace-missing
                         fill-range-replace)

(exporter/export-symbols tablecloth.api.join-separate
                         join-columns
                         separate-column)

(exporter/export-symbols tablecloth.api.fold-unroll
                         fold-by
                         unroll)

(exporter/export-symbols tablecloth.api.reshape
                         pivot->longer
                         pivot->wider)

(exporter/export-symbols tablecloth.api.join-concat-ds                         
                         left-join
                         right-join
                         inner-join
                         asof-join
                         full-join
                         semi-join
                         anti-join
                         intersect
                         difference
                         union
                         bind
                         append)

(exporter/export-symbols tablecloth.api.split
                         split)
;;

(defn- select-or-drop
  "Select columns and rows"
  [fc fs ds columns-selector rows-selector]
  (let [ds (if (and columns-selector
                    (not= :all columns-selector))
             (fc ds columns-selector)
             ds)]
    (if (and rows-selector
             (not= :all rows-selector))
      (fs ds rows-selector)
      ds)))

(defn- select-or-drop-docstring
  [op]
  (str op " columns and rows."))

(def ^{:doc (select-or-drop-docstring "Select")
       :arglists '([ds columns-selector rows-selector])}
  select (partial select-or-drop select-columns select-rows))
(def ^{:doc (select-or-drop-docstring "Drop")
       :arglists '([ds columns-selector rows-selector])}
  drop (partial select-or-drop drop-columns drop-rows))

