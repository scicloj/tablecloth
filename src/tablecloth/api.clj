(ns tablecloth.api
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle
                            max min + - * / <= < >= > bit-xor unsigned-bit-shift-right quot bit-test
                            bit-and rem bit-or bit-flip bit-shift-left bit-clear bit-shift-right bit-set
                            bit-and-not bit-not identity])
  (:require [tech.v3.datatype.export-symbols :as exporter]
            [tablecloth.api.operators :as op]))

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
                         split
                         split->seq)
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

;; operators

(exporter/export-symbols tablecloth.api.operators
                         ;; (->> (ns-publics 'tablecloth.api.operators) keys sort)
                         *
                         +
                         -
                         /
                         abs
                         acos
                         asin
                         atan
                         atan2
                         bit-and
                         bit-and-not
                         bit-clear
                         bit-flip
                         bit-not
                         bit-or
                         bit-set
                         bit-shift-left
                         bit-shift-right
                         bit-test
                         bit-xor
                         cbrt
                         ceil
                         cos
                         cosh
                         cummax
                         cummin
                         cumprod
                         cumsum
                         descriptive-statistics
                         emax
                         emin
                         exp
                         expm1
                         floor
                         get-significand
                         hypot
                         identity
                         ieee-remainder
                         kendalls-correlation
                         kurtosis
                         log
                         log10
                         log1p
                         logistic
                         max
                         mean
                         median
                         min
                         next-down
                         next-up
                         pearsons-correlation
                         percentiles
                         pow
                         quartile-1
                         quartile-3
                         quartile-outlier-fn
                         quartiles
                         quot
                         rem
                         rint
                         round
                         signum
                         sin
                         sinh
                         skew
                         spearmans-correlation
                         sq
                         sqrt
                         standard-deviation
                         sum
                         tan
                         tanh
                         to-degrees
                         to-radians
                         ulp
                         unsigned-bit-shift-right
                         variance)
