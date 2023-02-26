(ns tablecloth.column.api.column
  (:require [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]
            [tablecloth.api.utils :refer [->general-types concrete-type? type?]]))

(defn column
  "Create a `column` from a vector or sequence. "
  ([]
   (col/new-column nil []))
  ([data]
   (column data {:name nil}))
  ([data {:keys [name]
          :as options}]
   (col/new-column name data)))

;; Alias for tech.v3.dasetset.column.is-column?
(defn column? 
  "Return true or false `item` is a column."
  [item]
  (col/is-column? item))

(defn typeof
  "Returns the concrete type of the elements within the column `col`."
  [col]
  (dtype/elemwise-datatype col))

(defn typeof?
  "True|false the column's elements are of the provided type `datatype`. Can check
   both concrete types (e.g. :int32) or general types (:numerical, :textual, etc)."
  [col datatype]
  (let [concrete-type-of-els (dtype/elemwise-datatype col)]
    (if (concrete-type? datatype) 
      (= datatype concrete-type-of-els)
      (not (nil? (type? datatype concrete-type-of-els))))))

(defn zeros
  "Create a new column filled wth `n-zeros`."
  [n-zeros]
  (column (dtype/const-reader 0 n-zeros)))

(defn ones
  "Creates a new column filled with `n-ones`"
  [n-ones]
  (column (dtype/const-reader 1 n-ones)))

(defn slice
  "Returns a subset of the column defined by the inclusive `from` and
  `to` indexes. If `to` is not provided, slices to the end of the
  column. If `from` is not provided (i.e. is `nil`), slices from the
  beginning of the column. If either `from` or `to` is a negative
  number, it is treated as an index from the end of the column. The
  `:start` and `:end` keywords can be used to represent the start and
  end of the column, respectively.

  Examples:
  (def column [1 2 3 4 5])
  (slice column 1 3)     ;=> [2 3]
  (slice column 2)        ;=> [3 4 5]
  (slice column -3 -1)    ;=> [3 4 5]
  (slice column :start 2) ;=> [1 2 3 4 5]
  (slice column 2 :end)   ;=> [3 4 5]
  (slice column -2 :end)  ;=> [4 5]"
  ([col from]
   (slice col from :end))
  ([col from to]
   (slice col from to 1))
  ([col from to step]
   (let [len (count col)
         from (or (when-not (or (= from :start) (nil? from)) from) 0)
         to (or (when-not (or (= to :end) (nil? :end)) to) (dec len))]
     (col/select col (range (if (neg? from) (+ len from) from)
                            (inc (if (neg? to) (+ len to) to))
                            step)))))

(defn column-map
  "Applies a map function `map-fn` to one or more columns. If `col` is
  a vector of columns, `map-fn` must have an arity equal to the number
  of columns. The datatype of the resulting column will be inferred,
  unless specified in the `options` map. Missing values can be handled
  by providing a `:missing-fn` in the options map.

  options:
  - :datatype   - The desired datatype of the resulting column. The datatype
                  is inferred if not provided
  - :missing-fn - A function that takes a sequence of columns, and returns a
                  set of missing index positions."
  ([col map-fn]
   (column-map col map-fn {}))
  ([col map-fn options-or-datatype]
   (if (vector? col)
     (apply col/column-map map-fn options col)
     (col/column-map map-fn options col))))

(column-map [(column [1 2 nil 4 5])
             (column [nil 2 5 8 0])]
            (partial + 10)
            {:datatype :int64
             :missing-fn tech.v3.dataset.column/union-missing-sets})

(column-map [(column [1 2 nil 4 5])
             (column [nil 2 5 8 0])]
            (partial +)
            {:datatype :int64
             :missing-fn (fn [col-seq] (set [0 2]))})
