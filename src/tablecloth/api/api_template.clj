(ns tablecloth.api.api-template
  "Tablecloth API"
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])  
  (:require [tech.v3.datatype.export-symbols :as exporter]))

(exporter/export-symbols tech.v3.datatype
                         clone)

(exporter/export-symbols tech.v3.dataset
                         column-count
                         row-count
                         set-dataset-name
                         dataset-name
                         column
                         has-column?
                         write!)

(def ^{:deprecated "Use `write!` instead."} write-csv! write!)

(exporter/export-symbols tech.v3.dataset.print
                         dataset->str)

(exporter/export-symbols tablecloth.api.utils
                         column-names
                         write-nippy!
                         read-nippy
                         grouped?
                         unmark-group
                         mark-as-group
                         as-regular-dataset
                         process-group-data)

(exporter/export-symbols tablecloth.api.dataset
                         dataset?
                         empty-ds?
                         dataset
                         shape
                         info
                         columns
                         rows
                         get-entry
                         print-dataset
                         concat
                         concat-copying)

(exporter/export-symbols tablecloth.api.group-by
                         group-by
                         ungroup
                         groups->seq
                         groups->map)

(exporter/export-symbols tablecloth.api.columns
                         select-columns
                         drop-columns
                         rename-columns
                         add-column
                         add-columns
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
                         map-rows
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
                         aggregate-columns
                         crosstab)

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
                         separate-column
                         array-column->columns
                         columns->array-column)

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
                         cross-join
                         expand
                         complete
                         intersect
                         difference
                         union
                         bind
                         append)

(exporter/export-symbols tablecloth.api.split
                         split
                         split->seq)

;; (exporter/export-symbols tablecloth.api.operators
;;   * + - / < <= > >= abs acos and asin atan atan2 bit-and bit-and-not bit-clear bit-flip bit-not bit-or bit-set bit-shift-left bit-shift-right bit-xor cbrt ceil cos cosh cummax cummin cumprod cumsum distance distance-squared dot-product eq even? exp expm1 finite? floor get-significand hypot identity ieee-remainder infinite? kurtosis log log10 log1p logistic magnitude magnitude-squared mathematical-integer? max mean mean-fast median min nan? neg? next-down next-up normalize not not-eq odd? or percentiles pos? pow quartile-1 quartile-3 quot reduce-* reduce-+ reduce-max reduce-min rem rint round shift signum sin sinh skew sq sqrt sum sum-fast tan tanh to-degrees to-radians ulp unsigned-bit-shift-right variance zero?)

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

;; tibble macro

(defmacro let-dataset
  ([bindings] `(let-dataset ~bindings nil))
  ([bindings options]
   (let [cols (take-nth 2 bindings)
         col-defs (mapv vector (map keyword cols) cols)]
     `(let [~@bindings]
        (dataset ~col-defs ~options)))))

;; ungroup/group wrapper
(defmacro without-grouping->
  [ds & r]
  `(-> ~ds
       (unmark-group)
       ~@r
       (mark-as-group)))

(comment
  (exporter/write-api! 'tablecloth.api.api-template
                       'tablecloth.api
                       "src/tablecloth/api.clj"
                       '[group-by drop concat rand-nth first last shuffle])

  )
