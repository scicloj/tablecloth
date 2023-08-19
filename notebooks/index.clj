(ns index
  (:require [scicloj.kindly.v3.kind :as kind]
            [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly-default.v1.api :refer [md]]
            [tablecloth.api :as tc]
            [scicloj.note-to-test.v1.api :as note-to-test]))


^:note-to-test/skip
(let [represent-a-little (fn [values]
                           (into []
                                 (comp (take 20)
                                       (map note-to-test/represent-value))
                                 values))]
  (note-to-test/define-value-representations!
    [{:predicate (fn [v]
                   (-> v
                       meta
                       :kindly/kind
                       (= :kind/md)))
      :representation (constantly :note-to-test/skip)}
     {:predicate (comp #{:local-date
                         :local-date-time
                         :packed-local-date
                         :packed-local-date-time}
                       tech.v3.datatype/datatype)
      :representation (juxt class str)}
     {:predicate (fn [v]
                   (or (sequential? v)
                       ;; https://stackoverflow.com/a/9090730
                       (some-> v
                               class
                               (.isArray))
                       (instance? tech.v3.dataset.impl.column.Column v)))
      :representation represent-a-little}
     {:predicate (partial instance? java.util.Map)
      :representation (fn [m]
                        (-> m
                            (update-keys note-to-test/represent-value)
                            (update-vals note-to-test/represent-value)))}
     {:predicate (fn [v]
                   (-> v
                       class
                       (= java.lang.Object)))
      :representation class}
     {:predicate symbol?
      :representation (fn [s]
                        [:symbol (name s)])}
     {:predicate fn?
      :representation (constantly :fn)}
     {:predicate (fn [v]
                   (or (var? v)
                       (nil? v)))
      ;; handling vars and nils the same way
      ;; so that defonce will be represented consistently
      :representation (constantly :var-or-nil)}
     {:predicate (partial instance? org.roaringbitmap.RoaringBitmap)
      :representation (constantly
                       :org.roaringbitmap.RoaringBitmap)}
     {:predicate (fn [x]
                   (and (double? x)
                        (Double/isNaN x)))
      :representation (constantly :NaN)}]))




(set! *warn-on-reflection* true)



(comment
  (time
   (note-to-test/gentest! "notebooks/index.clj"
                          {:accept true
                           :verbose true}))
  ,)


(def tech-ml-version (get-in (read-string (slurp "deps.edn")) [:deps 'techascent/tech.ml.dataset :mvn/version]))


tech-ml-version


(md "
## Introduction

[tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) is a great and fast library which brings columnar dataset to the Clojure. Chris Nuernberger has been working on this library for last year as a part of bigger `tech.ml` stack.

I've started to test the library and help to fix uncovered bugs. My main goal was to compare functionalities with the other standards from other platforms. I focused on R solutions: [dplyr](https://dplyr.tidyverse.org/), [tidyr](https://tidyr.tidyverse.org/) and [data.table](https://rdatatable.gitlab.io/data.table/).

During conversions of the examples I've come up how to reorganized existing `tech.ml.dataset` functions into simple to use API. The main goals were:

* Focus on dataset manipulation functionality, leaving other parts of `tech.ml` like pipelines, datatypes, readers, ML, etc.
* Single entry point for common operations - one function dispatching on given arguments.
* `group-by` results with special kind of dataset - a dataset containing subsets created after grouping as a column.
* Most operations recognize regular dataset and grouped dataset and process data accordingly.
* One function form to enable thread-first on dataset.

If you want to know more about `tech.ml.dataset` and `dtype-next` please refer their documentation:

* [tech.ml.dataset walkthrough](https://techascent.github.io/tech.ml.dataset/walkthrough.html)
* [dtype-next overview](https://cnuernber.github.io/dtype-next/overview.html)
* [dtype-next cheatsheet](https://cnuernber.github.io/dtype-next/cheatsheet.html)


[SOURCE CODE](https://github.com/scicloj/tablecloth)

Join the discussion on [Zulip](https://clojurians.zulipchat.com/#narrow/stream/236259-tech.2Eml.2Edataset.2Edev/topic/api)

Let's require main namespace and define dataset used in most examples:
")


(require '[tablecloth.api :as api]
         '[tech.v3.datatype.functional :as dfn])
(def DS (api/dataset {:V1 (take 9 (cycle [1 2]))
                      :V2 (range 1 10)
                      :V3 (take 9 (cycle [0.5 1.0 1.5]))
                      :V4 (take 9 (cycle ["A" "B" "C"]))}))





DS


(md "
## Functionality

### Dataset

Dataset is a special type which can be considered as a map of columns implemented around `tech.ml.datatype` library. Each column can be considered as named sequence of typed data. Supported types include integers, floats, string, boolean, date/time, objects etc.

#### Dataset creation

Dataset can be created from various of types of Clojure structures and files:

* single values
* sequence of maps
* map of sequences or values
* sequence of columns (taken from other dataset or created manually)
* sequence of pairs: `[string column-data]` or `[keyword column-data]`
* array of native arrays
* file types: raw/gzipped csv/tsv, json, xls(x) taken from local file system or URL
* input stream

`api/dataset` accepts:

* data
* options (see documentation of `tech.ml.dataset/->dataset` function for full list):
    - `:dataset-name` - name of the dataset
    - `:num-rows` - number of rows to read from file
    - `:header-row?` - indication if first row in file is a header
    - `:key-fn` - function applied to column names (eg. `keyword`, to convert column names to keywords)
    - `:separator` - column separator
    - `:single-value-column-name` - name of the column when single value is provided
    - `:column-names` - in case you want to name columns - only works for sequential input (arrays)
    - `:layout` - for numerical, native array of arrays - treat entries `:as-rows` or `:as-columns` (default)

`api/let-dataset` accepts bindings `symbol`-`column-data` to simulate R's `tibble` function. Each binding is converted into a column. You can refer previous columns to in further bindings (as in `let`).

---

Empty dataset.
")


(api/dataset)


(md "
---

Dataset from single value.
")


(api/dataset 999)


(md "
---

Set column name for single value. Also set the dataset name.
")


(api/dataset 999 {:single-value-column-name "my-single-value"})
(api/dataset 999 {:single-value-column-name ""
                  :dataset-name "Single value"})


(md "
---

Sequence of pairs (first = column name, second = value(s)).
")


(api/dataset [[:A 33] [:B 5] [:C :a]])


(md "
---

Not sequential values are repeated row-count number of times.
")


(api/dataset [[:A [1 2 3 4 5 6]] [:B "X"] [:C :a]])


(md "
---

Dataset created from map (keys = column names, vals = value(s)). Works the same as sequence of pairs.
")


(api/dataset {:A 33})
(api/dataset {:A [1 2 3]})
(api/dataset {:A [3 4 5] :B "X"})


(md "
---

You can put any value inside a column
")


(api/dataset {:A [[3 4 5] [:a :b]] :B "X"})


(md "
---

Sequence of maps
")


(api/dataset [{:a 1 :b 3} {:b 2 :a 99}])
(api/dataset [{:a 1 :b [1 2 3]} {:a 2 :b [3 4]}])


(md "
---

Missing values are marked by `nil`
")


(api/dataset [{:a nil :b 1} {:a 3 :b 4} {:a 11}])


(md "
---

Reading from arrays, by default `:as-columns`
")


(-> (map int-array [[1 2] [3 4] [5 6]])
    (into-array)
    (api/dataset))


(md "
`:as-rows`
")


(-> (map int-array [[1 2] [3 4] [5 6]])
    (into-array)
    (api/dataset {:layout :as-rows}))


(md "
`:as-rows` with names
")


(-> (map int-array [[1 2] [3 4] [5 6]])
    (into-array)
    (api/dataset {:layout :as-rows
                  :column-names [:a :b]}))


(md "
---

Create dataset using macro `let-dataset` to simulate R `tibble` function. Each binding is converted into a column.
")


(api/let-dataset [x (range 1 6)
                  y 1
                  z (dfn/+ x y)])


(md "
---

Import CSV file
")


(api/dataset "data/family.csv")


(md "
---

Import from URL
")


(defonce ds (api/dataset "https://vega.github.io/vega-lite/examples/data/seattle-weather.csv"))





ds


(md "
#### Saving

Export dataset to a file or output stream can be done by calling `api/write!`. Function accepts:

* dataset
* file name with one of the extensions: `.csv`, `.tsv`, `.csv.gz` and `.tsv.gz` or output stream
* options:
    - `:separator` - string or separator char.
")


(api/write! ds "output.tsv.gz")
(.exists (clojure.java.io/file "output.tsv.gz"))


(md "
##### Nippy
")


(api/write! DS "output.nippy.gz")





(api/dataset "output.nippy.gz")


(md "
#### Dataset related functions

Summary functions about the dataset like number of rows, columns and basic stats.

---

Number of rows
")


(api/row-count ds)


(md "
---

Number of columns
")


(api/column-count ds)


(md "
---

Shape of the dataset, [row count, column count]
")


(api/shape ds)


(md "
---

General info about dataset. There are three variants:

* default - containing information about columns with basic statistics
    - `:basic` - just name, row and column count and information if dataset is a result of `group-by` operation
    - `:columns` - columns' metadata
")


(api/info ds)
(api/info ds :basic)
(api/info ds :columns)


(md "
---

Getting a dataset name
")


(api/dataset-name ds)


(md "
---

Setting a dataset name (operation is immutable).
")


(->> "seattle-weather"
     (api/set-dataset-name ds)
     (api/dataset-name))


(md "
#### Columns and rows

Get columns and rows as sequences. `column`, `columns` and `rows` treat grouped dataset as regular one. See `Groups` to read more about grouped datasets.

Possible result types:

- `:as-seq` or `:as-seqs` - sequence of seqences (default)
- `:as-maps` - sequence of maps (rows)
- `:as-map` - map of sequences (columns)
- `:as-double-arrays` - array of double arrays

---

Select column.
")


(ds "wind")
(api/column ds "date")


(md "
---

Columns as sequence
")


(take 2 (api/columns ds))


(md "
---

Columns as map
")


(keys (api/columns ds :as-map))


(md "
---

Rows as sequence of sequences
")


(take 2 (api/rows ds))


(md "
---

Select rows/columns as double-double-array
")


(-> ds
    (api/select-columns :type/numerical)
    (api/head)
    (api/rows :as-double-arrays))





(-> ds
    (api/select-columns :type/numerical)
    (api/head)
    (api/columns :as-double-arrays))


(md "
---

Rows as sequence of maps
")


(clojure.pprint/pprint (take 2 (api/rows ds :as-maps)))


(md "
#### Printing

Dataset is printed using `dataset->str` or `print-dataset` functions. Options are the same as in `tech.ml.dataset/dataset-data->str`. Most important is `:print-line-policy` which can be one of the: `:single`, `:repl` or `:markdown`.
")


(api/print-dataset (api/group-by DS :V1) {:print-line-policy :markdown})





(api/print-dataset (api/group-by DS :V1) {:print-line-policy :repl})





(api/print-dataset (api/group-by DS :V1) {:print-line-policy :single})


(md "
### Group-by

Grouping by is an operation which splits dataset into subdatasets and pack it into new special type of... dataset. I distinguish two types of dataset: regular dataset and grouped dataset. The latter is the result of grouping.

Grouped dataset is annotated in by `:grouped?` meta tag and consist following columns:

* `:name` - group name or structure
* `:group-id` - integer assigned to the group
* `:data` - groups as datasets

Almost all functions recognize type of the dataset (grouped or not) and operate accordingly.

You can't apply reshaping or join/concat functions on grouped datasets.

#### Grouping

Grouping is done by calling `group-by` function with arguments:

* `ds` - dataset
* `grouping-selector` - what to use for grouping
* options:
    - `:result-type` - what to return:
        * `:as-dataset` (default) - return grouped dataset
        * `:as-indexes` - return rows ids (row number from original dataset)
        * `:as-map` - return map with group names as keys and subdataset as values
        * `:as-seq` - return sequens of subdatasets
    - `:select-keys` - list of the columns passed to a grouping selector function

All subdatasets (groups) have set name as the group name, additionally `group-id` is in meta.

Grouping can be done by:

* single column name
* seq of column names
* map of keys (group names) and row indexes
* value returned by function taking row as map (limited to `:select-keys`)

Note: currently dataset inside dataset is printed recursively so it renders poorly from markdown. So I will use `:as-seq` result type to show just group names and groups.

---

List of columns in grouped dataset
")


(-> DS
    (api/group-by :V1)
    (api/column-names))


(md "
---

List of columns in grouped dataset treated as regular dataset
")


(-> DS
    (api/group-by :V1)
    (api/as-regular-dataset)
    (api/column-names))


(md "

---

Content of the grouped dataset
")


(api/columns (api/group-by DS :V1) :as-map)


(md "
---

Grouped dataset as map
")


(keys (api/group-by DS :V1 {:result-type :as-map}))





(vals (api/group-by DS :V1 {:result-type :as-map}))


(md "
---

Group dataset as map of indexes (row ids)
")


(api/group-by DS :V1 {:result-type :as-indexes})


(md "
---

Grouped datasets are printed as follows by default.
")


(api/group-by DS :V1)


(md "
---

To get groups as sequence or a map can be done from grouped dataset using `groups->seq` and `groups->map` functions.

Groups as seq can be obtained by just accessing `:data` column.

I will use temporary dataset here.
")


(let [ds (-> {"a" [1 1 2 2]
              "b" ["a" "b" "c" "d"]}
             (api/dataset)
             (api/group-by "a"))]
  (seq (ds :data))) ;; seq is not necessary but Markdown treats `:data` as command here





(-> {"a" [1 1 2 2]
     "b" ["a" "b" "c" "d"]}
    (api/dataset)
    (api/group-by "a")
    (api/groups->seq))


(md "
---

Groups as map
")


(-> {"a" [1 1 2 2]
     "b" ["a" "b" "c" "d"]}
    (api/dataset)
    (api/group-by "a")
    (api/groups->map))


(md "
---

Grouping by more than one column. You can see that group names are maps. When ungrouping is done these maps are used to restore column names.
")


(api/group-by DS [:V1 :V3] {:result-type :as-seq})


(md "
---

Grouping can be done by providing just row indexes. This way you can assign the same row to more than one group.
")


(api/group-by DS {"group-a" [1 2 1 2]
                  "group-b" [5 5 5 1]} {:result-type :as-seq})


(md "
---

You can group by a result of grouping function which gets row as map and should return group name. When map is used as a group name, ungrouping restore original column names.
")


(api/group-by DS (fn [row] (* (:V1 row)
                              (:V3 row))) {:result-type :as-seq})


(md "
---

You can use any predicate on column to split dataset into two groups.
")


(api/group-by DS (comp #(< % 1.0) :V3) {:result-type :as-seq})


(md "
---

`juxt` is also helpful
")


(api/group-by DS (juxt :V1 :V3) {:result-type :as-seq})


(md "
---

`tech.ml.dataset` provides an option to limit columns which are passed to grouping functions. It's done for performance purposes.
")


(api/group-by DS identity {:result-type :as-seq
                           :select-keys [:V1]})


(md "
#### Ungrouping

Ungrouping simply concats all the groups into the dataset. Following options are possible

* `:order?` - order groups according to the group name ascending order. Default: `false`
* `:add-group-as-column` - should group name become a column? If yes column is created with provided name (or `:$group-name` if argument is `true`). Default: `nil`.
* `:add-group-id-as-column` - should group id become a column? If yes column is created with provided name (or `:$group-id` if argument is `true`). Default: `nil`.
* `:dataset-name` - to name resulting dataset. Default: `nil` (_unnamed)

If group name is a map, it will be splitted into separate columns. Be sure that groups (subdatasets) doesn't contain the same columns already.

If group name is a vector, it will be splitted into separate columns. If you want to name them, set vector of target column names as `:add-group-as-column` argument.

After ungrouping, order of the rows is kept within the groups but groups are ordered according to the internal storage.

---

Grouping and ungrouping.
")


(-> DS
    (api/group-by :V3)
    (api/ungroup))


(md "
---

Groups sorted by group name and named.
")


(-> DS
    (api/group-by :V3)
    (api/ungroup {:order? true
                  :dataset-name "Ordered by V3"}))


(md "
---

Groups sorted descending by group name and named.
")


(-> DS
    (api/group-by :V3)
    (api/ungroup {:order? :desc
                  :dataset-name "Ordered by V3 descending"}))


(md "
---

Let's add group name and id as additional columns
")


(-> DS
    (api/group-by (comp #(< % 4) :V2))
    (api/ungroup {:add-group-as-column true
                  :add-group-id-as-column true}))


(md "
---

Let's assign different column names
")


(-> DS
    (api/group-by (comp #(< % 4) :V2))
    (api/ungroup {:add-group-as-column "Is V2 less than 4?"
                  :add-group-id-as-column "group id"}))


(md "
---

If we group by map, we can automatically create new columns out of group names.
")


(-> DS
    (api/group-by (fn [row] {"V1 and V3 multiplied" (* (:V1 row)
                                                       (:V3 row))
                             "V4 as lowercase" (clojure.string/lower-case (:V4 row))}))
    (api/ungroup {:add-group-as-column true}))


(md "
---

We can add group names without separation
")


(-> DS
    (api/group-by (fn [row] {"V1 and V3 multiplied" (* (:V1 row)
                                                       (:V3 row))
                             "V4 as lowercase" (clojure.string/lower-case (:V4 row))}))
    (api/ungroup {:add-group-as-column "just map"
                  :separate? false}))


(md "
---

The same applies to group names as sequences
")


(-> DS
    (api/group-by (juxt :V1 :V3))
    (api/ungroup {:add-group-as-column "abc"}))


(md "
---

Let's provide column names
")


(-> DS
    (api/group-by (juxt :V1 :V3))
    (api/ungroup {:add-group-as-column ["v1" "v3"]}))


(md "
---

Also we can supress separation
")


(-> DS
    (api/group-by (juxt :V1 :V3))
    (api/ungroup {:separate? false
                  :add-group-as-column true}))
;; => _unnamed [9 5]:




(md "
#### Other functions

To check if dataset is grouped or not just use `grouped?` function.
")


(api/grouped? DS)





(api/grouped? (api/group-by DS :V1))


(md "
---

If you want to remove grouping annotation (to make all the functions work as with regular dataset) you can use `unmark-group` or `as-regular-dataset` (alias) functions.

It can be important when you want to remove some groups (rows) from grouped dataset using `drop-rows` or something like that.
")


(-> DS
    (api/group-by :V1)
    (api/as-regular-dataset)
    (api/grouped?))


(md "
You can also operate on grouped dataset as a regular one in case you want to access its columns using `without-grouping->` threading macro.
")


(-> DS
    (api/group-by [:V4 :V1])
    (api/without-grouping->
     (api/order-by (comp (juxt :V4 :V1) :name))))


(md "
---

This is considered internal.

If you want to implement your own mapping function on grouped dataset you can call `process-group-data` and pass function operating on datasets. Result should be a dataset to have ungrouping working.
")


(-> DS
    (api/group-by :V1)
    (api/process-group-data #(str "Shape: " (vector (api/row-count %) (api/column-count %))))
    (api/as-regular-dataset))


(md "
### Columns

Column is a special `tech.ml.dataset` structure based on `tech.ml.datatype` library. For our purposes we cat treat columns as typed and named sequence bound to particular dataset.

Type of the data is inferred from a sequence during column creation.

#### Names

To select dataset columns or column names `columns-selector` is used. `columns-selector` can be one of the following:

* `:all` keyword - selects all columns
* column name - for single column
* sequence of column names - for collection of columns
* regex - to apply pattern on column names or datatype
* filter predicate - to filter column names or datatype
* `type` namespaced keyword for specific datatype or group of datatypes

Column name can be anything.

`column-names` function returns names according to `columns-selector` and optional `meta-field`. `meta-field` is one of the following:

* `:name` (default) - to operate on column names
* `:datatype` - to operated on column types
* `:all` - if you want to process all metadata

Datatype groups are:

* `:type/numerical` - any numerical type
* `:type/float` - floating point number (`:float32` and `:float64`)
* `:type/integer` - any integer
* `:type/datetime` - any datetime type

If qualified keyword starts with `:!type`, complement set is used.

---

To select all column names you can use `column-names` function.
")


(api/column-names DS)


(md "
or
")


(api/column-names DS :all)


(md "
In case you want to select column which has name `:all` (or is sequence or map), put it into a vector. Below code returns empty sequence since there is no such column in the dataset.
")


(api/column-names DS [:all])


(md "
---

Obviously selecting single name returns it's name if available
")


(api/column-names DS :V1)
(api/column-names DS "no such column")


(md "
---

Select sequence of column names.
")


(api/column-names DS [:V1 "V2" :V3 :V4 :V5])


(md "
---

Select names based on regex, columns ends with `1` or `4`
")


(api/column-names DS #".*[14]")


(md "
---

Select names based on regex operating on type of the column (to check what are the column types, call `(api/info DS :columns)`. Here we want to get integer columns only.
")


(api/column-names DS #"^:int.*" :datatype)


(md "
or

")


(api/column-names DS :type/integer)


(md "
---

And finally we can use predicate to select names. Let's select double precision columns.
")


(api/column-names DS #{:float64} :datatype)


(md "
or
")


(api/column-names DS :type/float64)


(md "
---

If you want to select all columns but given, use `complement` function. Works only on a predicate.
")


(api/column-names DS (complement #{:V1}))
(api/column-names DS (complement #{:float64}) :datatype)
(api/column-names DS :!type/float64)


(md "
---

You can select column names based on all column metadata at once by using `:all` metadata selector. Below we want to select column names ending with `1` which have `long` datatype.
")


(api/column-names DS (fn [meta]
                       (and (= :int64 (:datatype meta))
                            (clojure.string/ends-with? (:name meta) "1"))) :all)


(md "
#### Select

`select-columns` creates dataset with columns selected by `columns-selector` as described above. Function works on regular and grouped dataset.

---

Select only float64 columns
")


(api/select-columns DS #(= :float64 %) :datatype)


(md "or
")


(api/select-columns DS :type/float64)


(md "

---

Select all but `:V1` columns
")


(api/select-columns DS (complement #{:V1}))


(md "
---

If we have grouped data set, column selection is applied to every group separately.
")


(-> DS
    (api/group-by :V1)
    (api/select-columns [:V2 :V3])
    (api/groups->map))


(md "
#### Drop

`drop-columns` creates dataset with removed columns.

---

Drop float64 columns
")


(api/drop-columns DS #(= :float64 %) :datatype)


(md "
or
")


(api/drop-columns DS :type/float64)


(md "
---

Drop all columns but `:V1` and `:V2`
")


(api/drop-columns DS (complement #{:V1 :V2}))


(md "
---

If we have grouped data set, column selection is applied to every group separately. Selected columns are dropped.
")


(-> DS
    (api/group-by :V1)
    (api/drop-columns [:V2 :V3])
    (api/groups->map))


(md "
#### Rename

If you want to rename colums use `rename-columns` and pass map where keys are old names, values new ones.

You can also pass mapping function with optional columns-selector
")


(api/rename-columns DS {:V1 "v1"
                        :V2 "v2"
                        :V3 [1 2 3]
                        :V4 (Object.)})


(md "
---

Map all names with function
")


(api/rename-columns DS (comp str second name))


(md "
---

Map selected names with function
")


(api/rename-columns DS [:V1 :V3] (comp str second name))


(md "
---

Function works on grouped dataset
")


(-> DS
    (api/group-by :V1)
    (api/rename-columns {:V1 "v1"
                         :V2 "v2"
                         :V3 [1 2 3]
                         :V4 (Object.)})
    (api/groups->map))


(md "

#### Add or update

To add (or replace existing) column call `add-column` function. Function accepts:

* `ds` - a dataset
* `column-name` - if it's existing column name, column will be replaced
* `column` - can be column (from other dataset), sequence, single value or function. Too big columns are always trimmed. Too small are cycled or extended with missing values (according to `size-strategy` argument)
* `size-strategy` (optional) - when new column is shorter than dataset row count, following strategies are applied:
- `:cycle` (default) - repeat data
- `:na` - append missing values
- `:strict` - throws an exception when sizes mismatch

Function works on grouped dataset.

---

Add single value as column
")


(api/add-column DS :V5 "X")


(md "
---

Replace one column (column is trimmed)
")

^:note-to-test/skip
(api/add-column DS :V1 (repeatedly rand))


(md "
---

Copy column
")


(api/add-column DS :V5 (DS :V1))


(md "
---

When function is used, argument is whole dataset and the result should be column, sequence or single value
")


(api/add-column DS :row-count api/row-count)


(md "
---

Above example run on grouped dataset, applies function on each group separately.
")


(-> DS
    (api/group-by :V1)
    (api/add-column :row-count api/row-count)
    (api/ungroup))


(md "
---

When column which is added is longer than row count in dataset, column is trimmed. When column is shorter, it's cycled or missing values are appended.
")


(api/add-column DS :V5 [:r :b])





(api/add-column DS :V5 [:r :b] :na)


(md "
Exception is thrown when `:strict` strategy is used and column size is not equal row count
")


(try
  (api/add-column DS :V5 [:r :b] :strict)
  (catch Exception e (str "Exception caught: "(ex-message e))))


(md "
---

Tha same applies for grouped dataset
")


(-> DS
    (api/group-by :V3)
    (api/add-column :V5 [:r :b] :na)
    (api/ungroup))


(md "
---

Let's use other column to fill groups
")


(-> DS
    (api/group-by :V3)
    (api/add-column :V5 (DS :V2))
    (api/ungroup))


(md "
---

In case you want to add or update several columns you can call `add-columns` and provide map where keys are column names, vals are columns.
")


(api/add-columns DS {:V1 #(map inc (% :V1))
                     :V5 #(map (comp keyword str) (% :V4))
                     :V6 11})


(md "
#### Update

If you want to modify specific column(s) you can call `update-columns`. Arguments:

* dataset
* one of:
    - `columns-selector` and function (or sequence of functions)
    - map where keys are column names and vals are function

Functions accept column and have to return column or sequence

---

Reverse of columns
")


(api/update-columns DS :all reverse)


(md "
---

Apply dec/inc on numerical columns
")


(api/update-columns DS :type/numerical [(partial map dec)
                                        (partial map inc)])


(md "
---

You can also assing function to a column by packing operations into the map.
")


^:note-to-test/skip
(api/update-columns DS {:V1 reverse
                        :V2 (comp shuffle seq)})


(md "

#### Map

The other way of creating or updating column is to map rows as regular `map` function. The arity of mapping function should be the same as number of selected columns.

Arguments:

* `ds` - dataset
* `column-name` - target column name
* `columns-selector` - columns selected
* `map-fn` - mapping function

---

Let's add numerical columns together
")


(api/map-columns DS
                 :sum-of-numbers
                 (api/column-names DS  #{:int64 :float64} :datatype)
                 (fn [& rows]
                   (reduce + rows)))


(md "
The same works on grouped dataset
")


(-> DS
    (api/group-by :V4)
    (api/map-columns :sum-of-numbers
                     (api/column-names DS  #{:int64 :float64} :datatype)
                     (fn [& rows]
                       (reduce + rows)))
    (api/ungroup))


(md "
#### Reorder

To reorder columns use columns selectors to choose what columns go first. The unseleted columns are appended to the end.
")


(api/reorder-columns DS :V4 [:V3 :V2])


(md "
---

This function doesn't let you select meta field, so you have to call `column-names` in such case. Below we want to add integer columns at the end.
")


(api/reorder-columns DS (api/column-names DS (complement #{:int64}) :datatype))


(md "
#### Type conversion

To convert column into given datatype can be done using `convert-types` function. Not all the types can be converted automatically also some types require slow parsing (every conversion from string). In case where conversion is not possible you can pass conversion function.

Arguments:

* `ds` - dataset
* Two options:
    - `coltype-map` in case when you want to convert several columns, keys are column names, vals are new types
    - `column-selector` and `new-types` - column name and new datatype (or datatypes as sequence)

`new-types` can be:

* a type like `:int64` or `:string` or sequence of types
* or sequence of pair of datetype and conversion function

After conversion additional infomation is given on problematic values.

The other conversion is casting column into java array (`->array`) of the type column or provided as argument. Grouped dataset returns sequence of arrays.

---

Basic conversion
")


(-> DS
    (api/convert-types :V1 :float64)
    (api/info :columns))


(md "
---

Using custom converter. Let's treat `:V4` as haxadecimal values. See that this way we can map column to any value.
")


(-> DS
    (api/convert-types :V4 [[:int16 #(Integer/parseInt % 16)]]))


(md "
---

You can process several columns at once
")


(-> DS
    (api/convert-types {:V1 :float64
                        :V2 :object
                        :V3 [:boolean #(< % 1.0)]
                        :V4 :object})
    (api/info :columns))


(md "
---

Convert one type into another
")


(-> DS
    (api/convert-types :type/numerical :int16)
    (api/info :columns))


(md "
---

Function works on the grouped dataset
")


(-> DS
    (api/group-by :V1)
    (api/convert-types :V1 :float32)
    (api/ungroup)
    (api/info :columns))


(md "
---

Double array conversion.
")


(api/->array DS :V1)


(md "
---

Function also works on grouped dataset
")


(-> DS
    (api/group-by :V3)
    (api/->array :V2))


(md "
---

You can also cast the type to the other one (if casting is possible):
")


(api/->array DS :V4 :string)
(api/->array DS :V1 :float32)


(md "
### Rows

Rows can be selected or dropped using various selectors:

* row id(s) - row index as number or seqence of numbers (first row has index `0`, second `1` and so on)
* sequence of true/false values
* filter by predicate (argument is row as a map)

When predicate is used you may want to limit columns passed to the function (`select-keys` option).

Additionally you may want to precalculate some values which will be visible for predicate as additional columns. It's done internally by calling `add-columns` on a dataset. `:pre` is used as a column definitions.

#### Select

Select fifth row
")


(api/select-rows DS 4)


(md "
---

Select 3 rows
")


(api/select-rows DS [1 4 5])


(md "
---

Select rows using sequence of true/false values
")


(api/select-rows DS [true nil nil true])


(md "
---

Select rows using predicate
")


(api/select-rows DS (comp #(< % 1) :V3))


(md "
---

The same works on grouped dataset, let's select first row from every group.
")


(-> DS
    (api/group-by :V1)
    (api/select-rows 0)
    (api/ungroup))


(md "
---

If you want to select `:V2` values which are lower than or equal mean in grouped dataset you have to precalculate it using `:pre`.
")


(-> DS
    (api/group-by :V4)
    (api/select-rows (fn [row] (<= (:V2 row) (:mean row)))
                     {:pre {:mean #(tech.v3.datatype.functional/mean (% :V2))}})
    (api/ungroup))


(md "
#### Drop

`drop-rows` removes rows, and accepts exactly the same parameters as `select-rows`

---

Drop values lower than or equal `:V2` column mean in grouped dataset.
")


(-> DS
    (api/group-by :V4)
    (api/drop-rows (fn [row] (<= (:V2 row) (:mean row)))
                   {:pre {:mean #(tech.v3.datatype.functional/mean (% :V2))}})
    (api/ungroup))


(md "
#### Other

There are several function to select first, last, random rows, or display head, tail of the dataset. All functions work on grouped dataset.

All random functions accept `:seed` as an option if you want to fix returned result.

---

First row
")


(api/first DS)


(md "
---

Last row
")


(api/last DS)


(md "
---

Random row (single)
")

^:note-to-test/skip
(api/rand-nth DS)


(md "
---

Random row (single) with seed
")

(api/rand-nth DS {:seed 42})


(md "
---

Random `n` (default: row count) rows with repetition.
")


^:note-to-test/skip
(api/random DS)


(md "
---

Five random rows with repetition
")


^:note-to-test/skip
(api/random DS 5)


(md "
---

Five random, non-repeating rows
")


^:note-to-test/skip
(api/random DS 5 {:repeat? false})


(md "
---

Five random, with seed
")


(api/random DS 5 {:seed 42})


(md "

---

Shuffle dataset
")

^:note-to-test/skip
(api/shuffle DS)


(md "
---

Shuffle with seed
")


(api/shuffle DS {:seed 42})


(md "

---

First `n` rows (default 5)
")


(api/head DS)


(md "
---

Last `n` rows (default 5)
")


(api/tail DS)


(md "
---

`by-rank` calculates rank on column(s). It's base on [R rank()](https://www.rdocumentation.org/packages/base/versions/3.6.1/topics/rank) with addition of `:dense` (default) tie strategy which give consecutive rank numbering.

`:desc?` options (default: `true`) sorts input with descending order, giving top values under `0` value.

`rank` is zero based and is defined at `tablecloth.api.utils` namespace.

---
")


(api/by-rank DS :V3 zero?) ;; most V3 values





(api/by-rank DS :V3 zero? {:desc? false}) ;; least V3 values


(md "
---

Rank also works on multiple columns
")


(api/by-rank DS [:V1 :V3] zero? {:desc? false})


(md "
---

Select 5 random rows from each group
")


^:note-to-test/skip
(-> DS
    (api/group-by :V4)
    (api/random 5)
    (api/ungroup))


(md "
### Aggregate

Aggregating is a function which produces single row out of dataset.

Aggregator is a function or sequence or map of functions which accept dataset as an argument and result single value, sequence of values or map.

Where map is given as an input or result, keys are treated as column names.

Grouped dataset is ungrouped after aggreation. This can be turned off by setting `:ungroup` to false. In case you want to pass additional ungrouping parameters add them to the options.

By default resulting column names are prefixed with `summary` prefix (set it with `:default-column-name-prefix` option).

---

Let's calculate mean of some columns
")


(api/aggregate DS #(reduce + (% :V2)))


(md "
---

Let's give resulting column a name.
")


(api/aggregate DS {:sum-of-V2 #(reduce + (% :V2))})


(md "
---

Sequential result is spread into separate columns
")


(api/aggregate DS #(take 5(% :V2)))


(md "
---

You can combine all variants and rename default prefix
")


(api/aggregate DS [#(take 3 (% :V2))
                   (fn [ds] {:sum-v1 (reduce + (ds :V1))
                             :prod-v3 (reduce * (ds :V3))})] {:default-column-name-prefix "V2-value"})


(md "
---

Processing grouped dataset
")


(-> DS
    (api/group-by [:V4])
    (api/aggregate [#(take 3 (% :V2))
                    (fn [ds] {:sum-v1 (reduce + (ds :V1))
                              :prod-v3 (reduce * (ds :V3))})] {:default-column-name-prefix "V2-value"}))


(md "
Result of aggregating is automatically ungrouped, you can skip this step by stetting `:ungroup` option to `false`.
")


(-> DS
    (api/group-by [:V3])
    (api/aggregate [#(take 3 (% :V2))
                    (fn [ds] {:sum-v1 (reduce + (ds :V1))
                              :prod-v3 (reduce * (ds :V3))})] {:default-column-name-prefix "V2-value"
                                                               :ungroup? false}))


(md "
#### Column

You can perform columnar aggreagation also. `aggregate-columns` selects columns and apply aggregating function (or sequence of functions) for each column separately.
")


(api/aggregate-columns DS [:V1 :V2 :V3] #(reduce + %))


(md "
---
")


(api/aggregate-columns DS [:V1 :V2 :V3] [#(reduce + %)
                                         #(reduce max %)
                                         #(reduce * %)])


(md "
---
")


(-> DS
    (api/group-by [:V4])
    (api/aggregate-columns [:V1 :V2 :V3] #(reduce + %)))


(md "

### Order

Ordering can be done by column(s) or any function operating on row. Possible order can be:

* `:asc` for ascending order (default)
* `:desc` for descending order
* custom comparator

`:select-keys` limits row map provided to ordering functions.

---

Order by single column, ascending
")


(api/order-by DS :V1)


(md "
---

Descending order
")


(api/order-by DS :V1 :desc)


(md "
---

Order by two columns
")


(api/order-by DS [:V1 :V2])


(md "
---

Use different orders for columns
")


(api/order-by DS [:V1 :V2] [:asc :desc])





(api/order-by DS [:V1 :V2] [:desc :desc])





(api/order-by DS [:V1 :V3] [:desc :asc])


(md "
---

Custom function can be used to provided ordering key. Here order by `:V4` descending, then by product of other columns ascending.
")


(api/order-by DS [:V4 (fn [row] (* (:V1 row)
                                   (:V2 row)
                                   (:V3 row)))] [:desc :asc])


(md "
---

Custom comparator also can be used in case objects are not comparable by default. Let's define artificial one: if Euclidean distance is lower than 2, compare along `z` else along `x` and `y`. We use first three columns for that.
")


(defn dist
  [v1 v2]
  (->> v2
       (map - v1)
       (map #(* % %))
       (reduce +)
       (Math/sqrt)))





(api/order-by DS [:V1 :V2 :V3] (fn [[x1 y1 z1 :as v1] [x2 y2 z2 :as v2]]
                                 (let [d (dist v1 v2)]
                                   (if (< d 2.0)
                                     (compare z1 z2)
                                     (compare [x1 y1] [x2 y2])))))


(md "
### Unique

Remove rows which contains the same data. By default `unique-by` removes duplicates from whole dataset. You can also pass list of columns or functions (similar as in `group-by`) to remove duplicates limited by them. Default strategy is to keep the first row. More strategies below.

`unique-by` works on groups

---

Remove duplicates from whole dataset
")


(api/unique-by DS)


(md "
---

Remove duplicates from each group selected by column.
")


(api/unique-by DS :V1)


(md "
---

Pair of columns
")


(api/unique-by DS [:V1 :V3])


(md "
---

Also function can be used, split dataset by modulo 3 on columns `:V2`
")


(api/unique-by DS (fn [m] (mod (:V2 m) 3)))


(md "
---

The same can be achived with `group-by`
")


(-> DS
    (api/group-by (fn [m] (mod (:V2 m) 3)))
    (api/first)
    (api/ungroup))


(md "
---

Grouped dataset
")


(-> DS
    (api/group-by :V4)
    (api/unique-by :V1)
    (api/ungroup))


(md "
#### Strategies

There are 4 strategies defined:

* `:first` - select first row (default)
* `:last` - select last row
* `:random` - select random row
* any function - apply function to a columns which are subject of uniqueness

---

Last
")


(api/unique-by DS :V1 {:strategy :last})


(md "
---

Random
")


^:note-to-test/skip
(api/unique-by DS :V1 {:strategy :random})


(md "
---

Pack columns into vector
")


(api/unique-by DS :V4 {:strategy vec})


(md "
---

Sum columns
")


(api/unique-by DS :V4 {:strategy (partial reduce +)})


(md "
---

Group by function and apply functions
")


(api/unique-by DS (fn [m] (mod (:V2 m) 3)) {:strategy vec})


(md "
---

Grouped dataset
")


(-> DS
    (api/group-by :V1)
    (api/unique-by (fn [m] (mod (:V2 m) 3)) {:strategy vec})
    (api/ungroup {:add-group-as-column :from-V1}))


(md "
### Missing

When dataset contains missing values you can select or drop rows with missing values or replace them using some strategy.

`column-selector` can be used to limit considered columns

Let's define dataset which contains missing values
")


(def DSm (api/dataset {:V1 (take 9 (cycle [1 2 nil]))
                       :V2 (range 1 10)
                       :V3 (take 9 (cycle [0.5 1.0 nil 1.5]))
                       :V4 (take 9 (cycle ["A" "B" "C"]))}))





DSm


(md "
#### Select

Select rows with missing values
")


(api/select-missing DSm)


(md "
---

Select rows with missing values in `:V1`
")


(api/select-missing DSm :V1)


(md "
---

The same with grouped dataset
")


(-> DSm
    (api/group-by :V4)
    (api/select-missing :V3)
    (api/ungroup))


(md "
#### Drop

Drop rows with missing values
")


(api/drop-missing DSm)


(md "
---

Drop rows with missing values in `:V1`
")


(api/drop-missing DSm :V1)


(md "
---

The same with grouped dataset
")


(-> DSm
    (api/group-by :V4)
    (api/drop-missing :V1)
    (api/ungroup))


(md "
#### Replace

Missing values can be replaced using several strategies. `replace-missing` accepts:

* dataset
* column selector, default: `:all`
* strategy, default: `:nearest`
* value (optional)
    - single value
    - sequence of values (cycled)
    - function, applied on column(s) with stripped missings

Strategies are:

* `:value` - replace with given value
* `:up` - copy values up and then down for missing values at the end
* `:down` - copy values down and then up for missing values at the beginning
* `:mid` or `:nearest` - copy values around known values
* `:midpoint` - use average value from previous and next non-missing
* `:lerp` - trying to lineary approximate values, works for numbers and datetime, otherwise applies `:nearest`. For numbers always results in `float` datatype.

Let's define special dataset here:
")


(def DSm2 (api/dataset {:a [nil nil nil 1.0 2  nil nil nil nil  nil 4   nil  11 nil nil]
                        :b [2   2   2 nil nil nil nil nil nil 13   nil   3  4  5 5]}))





DSm2


(md "
---

Replace missing with default strategy for all columns
")


(api/replace-missing DSm2)


(md "
---

Replace missing with single value in whole dataset
")


(api/replace-missing DSm2 :all :value 999)


(md "
---

Replace missing with single value in `:a` column
")


(api/replace-missing DSm2 :a :value 999)


(md "
---

Replace missing with sequence in `:a` column
")


(api/replace-missing DSm2 :a :value [-999 -998 -997])


(md "
---

Replace missing with a function (mean)
")


(api/replace-missing DSm2 :a :value tech.v3.datatype.functional/mean)


(md "
---

Using `:down` strategy, fills gaps with values from above. You can see that if missings are at the beginning, the are filled with first value
")


(api/replace-missing DSm2 [:a :b] :down)


(md "
---

To fix above issue you can provide value
")


(api/replace-missing DSm2 [:a :b] :down 999)


(md "
---

The same applies for `:up` strategy which is opposite direction.
")


(api/replace-missing DSm2 [:a :b] :up)


(md "
---

The same applies for `:up` strategy which is opposite direction.
")


(api/replace-missing DSm2 [:a :b] :midpoint)


(md "
---

We can use a function which is applied after applying `:up` or `:down`
")


(api/replace-missing DSm2 [:a :b] :down tech.v3.datatype.functional/mean)


(md "
---

Lerp tries to apply linear interpolation of the values
")


(api/replace-missing DSm2 [:a :b] :lerp)


(md "
---

Lerp works also on dates
")


(-> (api/dataset {:dt [(java.time.LocalDateTime/of 2020 1 1 11 22 33)
                       nil nil nil nil nil nil nil
                       (java.time.LocalDateTime/of 2020 10 1 1 1 1)]})
    (api/replace-missing :lerp))


(md "
#### Inject

When your column contains not continuous data range you can fill up with lacking values. Arguments:

* dataset
* column name
* expected step (`max-span`, milliseconds in case of datetime column)
* (optional) `missing-strategy` - how to replace missing, default `:down` (set to `nil` if none)
* (optional) `missing-value` - optional value for replace missing

---
")


(-> (api/dataset {:a [1 2 9]
                  :b [:a :b :c]})
    (api/fill-range-replace :a 1))


(md "
### Join/Separate Columns

Joining or separating columns are operations which can help to tidy messy dataset.

* `join-columns` joins content of the columns (as string concatenation or other structure) and stores it in new column
* `separate-column` splits content of the columns into set of new columns

#### Join

`join-columns` accepts:

* dataset
* column selector (as in `select-columns`)
* options
    - `:separator` (default `\"-\"`)
    - `:drop-columns?` - whether to drop source columns or not (default `true`)
    - `:result-type`
        * `:map` - packs data into map
        * `:seq` - packs data into sequence
        * `:string` - join strings with separator (default)
        * or custom function which gets row as a vector
    - `:missing-subst` - substitution for missing value

---

Default usage. Create `:joined` column out of other columns.
")


(api/join-columns DSm :joined [:V1 :V2 :V4])


(md "
---

Without dropping source columns.
")


(api/join-columns DSm :joined [:V1 :V2 :V4] {:drop-columns? false})


(md "
---

Let's replace missing value with \"NA\" string.
")


(api/join-columns DSm :joined [:V1 :V2 :V4] {:missing-subst "NA"})


(md "
---

We can use custom separator.
")


(api/join-columns DSm :joined [:V1 :V2 :V4] {:separator "/"
                                             :missing-subst "."})


(md "
---

Or even sequence of separators.
")


(api/join-columns DSm :joined [:V1 :V2 :V4] {:separator ["-" "/"]
                                             :missing-subst "."})


(md "
---

The other types of results, map:
")


(api/join-columns DSm :joined [:V1 :V2 :V4] {:result-type :map})


(md "
---

Sequence
")


(api/join-columns DSm :joined [:V1 :V2 :V4] {:result-type :seq})


(md "
---

Custom function, calculate hash
")


(api/join-columns DSm :joined [:V1 :V2 :V4] {:result-type hash})


(md "
---

Grouped dataset
")


(-> DSm
    (api/group-by :V4)
    (api/join-columns :joined [:V1 :V2 :V4])
    (api/ungroup))


(md "
---

##### Tidyr examples

[source](https://tidyr.tidyverse.org/reference/unite.html)
")


(def df (api/dataset {:x ["a" "a" nil nil]
                      :y ["b" nil "b" nil]}))





df


(md "
---
")


(api/join-columns df "z" [:x :y] {:drop-columns? false
                                  :missing-subst "NA"
                                  :separator "_"})


(md "
---
")


(api/join-columns df "z" [:x :y] {:drop-columns? false
                                  :separator "_"})


(md "

#### Separate

Column can be also separated into several other columns using string as separator, regex or custom function. Arguments:

* dataset
* source column
* target columns - can be `nil` or `:infer` if `separator` returns map
* separator as:
    - string - it's converted to regular expression and passed to `clojure.string/split` function
    - regex
    - or custom function (default: identity)
* options
    - `:drop-columns?` - whether drop source column(s) or not (default: `true` or `:all` in case of empty `target-columns`). When set to `:all` keeps only separation result.
    - `:missing-subst` - values which should be treated as missing, can be set, sequence, value or function (default: `\"\"`)

Custom function (as separator) should return seqence of values for given value.

---

Separate float into integer and factional values
")


(api/separate-column DS :V3 [:int-part :frac-part] (fn [^double v]
                                                     [(int (quot v 1.0))
                                                      (mod v 1.0)]))


(md "
---

Source column can be kept
")


(api/separate-column DS :V3 [:int-part :frac-part] (fn [^double v]
                                                     [(int (quot v 1.0))
                                                      (mod v 1.0)]) {:drop-column? false})


(md "
---

We can treat `0` or `0.0` as missing value
")


(api/separate-column DS :V3 [:int-part :frac-part] (fn [^double v]
                                                     [(int (quot v 1.0))
                                                      (mod v 1.0)]) {:missing-subst [0 0.0]})


(md "
---

Works on grouped dataset
")


(-> DS
    (api/group-by :V4)
    (api/separate-column :V3 [:int-part :fract-part] (fn [^double v]
                                                       [(int (quot v 1.0))
                                                        (mod v 1.0)]))
    (api/ungroup))


(md "
---

Separate using separator returning sequence of maps, in this case we drop all other columns.
")


(api/separate-column DS :V3 (fn [^double v]
                              {:int-part (int (quot v 1.0))
                               :fract-part (mod v 1.0)}))


(md "
Keeping all columns
")


(api/separate-column DS :V3 nil (fn [^double v]
                                  {:int-part (int (quot v 1.0))
                                   :fract-part (mod v 1.0)}) {:drop-column? false})


(md "

---

Join and separate together.
")


(-> DSm
    (api/join-columns :joined [:V1 :V2 :V4] {:result-type :map})
    (api/separate-column :joined [:v1 :v2 :v4] (juxt :V1 :V2 :V4)))





(-> DSm
    (api/join-columns :joined [:V1 :V2 :V4] {:result-type :seq})
    (api/separate-column :joined [:v1 :v2 :v4] identity))


(md "
##### Tidyr examples

[separate source](https://tidyr.tidyverse.org/reference/separate.html)
[extract source](https://tidyr.tidyverse.org/reference/extract.html)
")


(def df-separate (api/dataset {:x [nil "a.b" "a.d" "b.c"]}))
(def df-separate2 (api/dataset {:x ["a" "a b" nil "a b c"]}))
(def df-separate3 (api/dataset {:x ["a?b" nil "a.b" "b:c"]}))
(def df-extract (api/dataset {:x [nil "a-b" "a-d" "b-c" "d-e"]}))





df-separate





df-separate2





df-separate3





df-extract


(md "
---
")


(api/separate-column df-separate :x [:A :B] "\\.")


(md "
---

You can drop columns after separation by setting `nil` as a name. We need second value here.
")


(api/separate-column df-separate :x [nil :B] "\\.")


(md "
---

Extra data is dropped
")


(api/separate-column df-separate2 :x ["a" "b"] " ")


(md "
---

Split with regular expression
")


(api/separate-column df-separate3 :x ["a" "b"] "[?\\.:]")


(md "
---

Or just regular expression to extract values
")


(api/separate-column df-separate3 :x ["a" "b"] #"(.).(.)")


(md "
---

Extract first value only
")


(api/separate-column df-extract :x ["A"] "-")


(md "
---

Split with regex
")


(api/separate-column df-extract :x ["A" "B"] #"(\p{Alnum})-(\p{Alnum})")


(md "
---

Only `a,b,c,d` strings
")


(api/separate-column df-extract :x ["A" "B"] #"([a-d]+)-([a-d]+)")


(md "
### Fold/Unroll Rows

To pack or unpack the data into single value you can use `fold-by` and `unroll` functions.

`fold-by` groups dataset and packs columns data from each group separately into desired datastructure (like vector or sequence). `unroll` does the opposite.

#### Fold-by

Group-by and pack columns into vector
")


(api/fold-by DS [:V3 :V4 :V1])


(md "
---

You can pack several columns at once.
")


(api/fold-by DS [:V4])


(md "
---

You can use custom packing function
")


(api/fold-by DS [:V4] seq)


(md "
or
")


(api/fold-by DS [:V4] set)


(md "
---

This works also on grouped dataset
")


(-> DS
    (api/group-by :V1)
    (api/fold-by :V4)
    (api/ungroup))


(md "
#### Unroll

`unroll` unfolds sequences stored in data, multiplying other ones when necessary. You can unroll more than one column at once (folded data should have the same size!).

Options:

* `:indexes?` if true (or column name), information about index of unrolled sequence is added.
* `:datatypes` list of datatypes which should be applied to restored columns, a map

---

Unroll one column
")


(api/unroll (api/fold-by DS [:V4]) [:V1])


(md "
---

Unroll all folded columns
")


(api/unroll (api/fold-by DS [:V4]) [:V1 :V2 :V3])


(md "
---

Unroll one by one leads to cartesian product
")


(-> DS
    (api/fold-by [:V4 :V1])
    (api/unroll [:V2])
    (api/unroll [:V3]))


(md "
---

You can add indexes
")


(api/unroll (api/fold-by DS [:V1]) [:V4 :V2 :V3] {:indexes? true})





(api/unroll (api/fold-by DS [:V1]) [:V4 :V2 :V3] {:indexes? "vector idx"})


(md "
---

You can also force datatypes
")


(-> DS
    (api/fold-by [:V1])
    (api/unroll [:V4 :V2 :V3] {:datatypes {:V4 :string
                                           :V2 :int16
                                           :V3 :float32}})
    (api/info :columns))


(md "
---

This works also on grouped dataset
")


(-> DS
    (api/group-by :V1)
    (api/fold-by [:V1 :V4])
    (api/unroll :V3 {:indexes? true})
    (api/ungroup))


(md "
### Reshape

Reshaping data provides two types of operations:

* `pivot->longer` - converting columns to rows
* `pivot->wider` - converting rows to columns

Both functions are inspired on [tidyr](https://tidyr.tidyverse.org/articles/pivot.html) R package and provide almost the same functionality.

All examples are taken from mentioned above documentation.

Both functions work only on regular dataset.

#### Longer

`pivot->longer` converts columns to rows. Column names are treated as data.

Arguments:

* dataset
* columns selector
* options:
    - `:target-columns` - names of the columns created or columns pattern (see below) (default: `:$column`)
    - `:value-column-name` - name of the column for values (default: `:$value`)
    - `:splitter` - string, regular expression or function which splits source column names into data
    - `:drop-missing?` - remove rows with missing? (default: `:true`)
    - `:datatypes` - map of target columns data types

`:target-columns` - can be:

* column name - source columns names are put there as a data
* column names as seqence - source columns names after split are put separately into `:target-columns` as data
* pattern - is a sequence of names, where some of the names are `nil`. `nil` is replaced by a name taken from splitter and such column is used for values.

---

Create rows from all columns but `\"religion\"`.
")


(def relig-income (api/dataset "data/relig_income.csv"))





relig-income





(api/pivot->longer relig-income (complement #{"religion"}))


(md "
---

Convert only columns starting with `\"wk\"` and pack them into `:week` column, values go to `:rank` column
")


(def bilboard (-> (api/dataset "data/billboard.csv.gz")
                  (api/drop-columns :type/boolean))) ;; drop some boolean columns, tidyr just skips them





(->> bilboard
     (api/column-names)
     (take 13)
     (api/select-columns bilboard))





(api/pivot->longer bilboard #(clojure.string/starts-with? % "wk") {:target-columns :week
                                                                   :value-column-name :rank})


(md "
---

We can create numerical column out of column names
")


(api/pivot->longer bilboard #(clojure.string/starts-with? % "wk") {:target-columns :week
                                                                   :value-column-name :rank
                                                                   :splitter #"wk(.*)"
                                                                   :datatypes {:week :int16}})



(md "
---

When column names contain observation data, such column names can be splitted and data can be restored into separate columns.
")


(def who (api/dataset "data/who.csv.gz"))





(->> who
     (api/column-names)
     (take 10)
     (api/select-columns who))





(api/pivot->longer who #(clojure.string/starts-with? % "new") {:target-columns [:diagnosis :gender :age]
                                                               :splitter #"new_?(.*)_(.)(.*)"
                                                               :value-column-name :count})


(md "
---

When data contains multiple observations per row, we can use splitter and pattern for target columns to create new columns and put values there. In following dataset we have two obseravations `dob` and `gender` for two childs. We want to put child infomation into the column and leave dob and gender for values.
")


(def family (api/dataset "data/family.csv"))





family





(api/pivot->longer family (complement #{"family"}) {:target-columns [nil :child]
                                                    :splitter "_"
                                                    :datatypes {"gender" :int16}})


(md "
---

Similar here, we have two observations: `x` and `y` in four groups.
")


(def anscombe (api/dataset "data/anscombe.csv"))





anscombe





(api/pivot->longer anscombe :all {:splitter #"(.)(.)"
                                  :target-columns [nil :set]})


(md "
---
")


^:note-to-test/skip
(def pnl (api/dataset {:x [1 2 3 4]
                       :a [1 1 0 0]
                       :b [0 1 1 1]
                       :y1 (repeatedly 4 rand)
                       :y2 (repeatedly 4 rand)
                       :z1 [3 3 3 3]
                       :z2 [-2 -2 -2 -2]}))





^:note-to-test/skip
pnl





^:note-to-test/skip
(api/pivot->longer pnl [:y1 :y2 :z1 :z2] {:target-columns [nil :times]
                                          :splitter #":(.)(.)"})


(md "
#### Wider

`pivot->wider` converts rows to columns.

Arguments:

* dataset
* `columns-selector` - values from selected columns are converted to new columns
* `value-columns` - what are values

When multiple columns are used as columns selector, names are joined using `:concat-columns-with` option.
`:concat-columns-with` can be a string or function (default: \"_\"). Function accepts sequence of names.

When `columns-selector` creates non unique set of values, they are folded using `:fold-fn` (default: `vec`) option.

When `value-columns` is a sequence, multiple observations as columns are created appending value column names into new columns. Column names are joined using `:concat-value-with` option.
`:concat-value-with` can be a string or function (default: \"-\"). Function accepts current column name and value.

---

Use `station` as a name source for columns and `seen` for values
")


(def fish (api/dataset "data/fish_encounters.csv"))





fish





(api/pivot->wider fish "station" "seen" {:drop-missing? false})


(md "
---

If selected columns contain multiple values, such values should be folded.
")


(def warpbreaks (api/dataset "data/warpbreaks.csv"))





warpbreaks


(md "
Let's see how many values are for each type of `wool` and `tension` groups
")


(-> warpbreaks
    (api/group-by ["wool" "tension"])
    (api/aggregate {:n api/row-count}))





(-> warpbreaks
    (api/reorder-columns ["wool" "tension" "breaks"])
    (api/pivot->wider "wool" "breaks" {:fold-fn vec}))


(md "
We can also calculate mean (aggreate values)
")


(-> warpbreaks
    (api/reorder-columns ["wool" "tension" "breaks"])
    (api/pivot->wider "wool" "breaks" {:fold-fn tech.v3.datatype.functional/mean}))


(md "
---

Multiple source columns, joined with default separator.
")


(def production (api/dataset "data/production.csv"))





production





(api/pivot->wider production ["product" "country"] "production")


(md "
Joined with custom function
")


(api/pivot->wider production ["product" "country"] "production" {:concat-columns-with vec})


(md "
---

Multiple value columns
")


(def income (api/dataset "data/us_rent_income.csv"))





income





(api/pivot->wider income "variable" ["estimate" "moe"] {:drop-missing? false})


(md "
Value concatenated by custom function
")


(api/pivot->wider income "variable" ["estimate" "moe"] {:concat-columns-with vec
                                                        :concat-value-with vector
                                                        :drop-missing? false})


(md "
---

Reshape contact data
")


(def contacts (api/dataset "data/contacts.csv"))





contacts





(api/pivot->wider contacts "field" "value" {:drop-missing? false})



(md "
#### Reshaping

A couple of `tidyr` examples of more complex reshaping.

---

[World bank](https://tidyr.tidyverse.org/articles/pivot.html#world-bank)
")


(def world-bank-pop (api/dataset "data/world_bank_pop.csv.gz"))





(->> world-bank-pop
     (api/column-names)
     (take 8)
     (api/select-columns world-bank-pop))


(md "
Step 1 - convert years column into values
")


(def pop2 (api/pivot->longer world-bank-pop (map str (range 2000 2018)) {:drop-missing? false
                                                                         :target-columns ["year"]
                                                                         :value-column-name "value"}))





pop2


(md "
Step 2 - separate `\"indicate\"` column
")


(def pop3 (api/separate-column pop2
                               "indicator" ["area" "variable"]
                               #(rest (clojure.string/split % #"\."))))





pop3


(md "
Step 3 - Make columns based on `\"variable\"` values.
")


(api/pivot->wider pop3 "variable" "value" {:drop-missing? false})


(md "
---

---

[Multi-choice](https://tidyr.tidyverse.org/articles/pivot.html#multi-choice)
")


(def multi (api/dataset {:id [1 2 3 4]
                         :choice1 ["A" "C" "D" "B"]
                         :choice2 ["B" "B" nil "D"]
                         :choice3 ["C" nil nil nil]}))





multi


(md "
Step 1 - convert all choices into rows and add artificial column to all values which are not missing.
")


(def multi2 (-> multi
                (api/pivot->longer (complement #{:id}))
                (api/add-column :checked true)))





multi2


(md "
Step 2 - Convert back to wide form with actual choices as columns
")


(-> multi2
    (api/drop-columns :$column)
    (api/pivot->wider :$value :checked {:drop-missing? false})
    (api/order-by :id))


(md "
---

---

[Construction](https://tidyr.tidyverse.org/articles/pivot.html#by-hand)
")


(def construction (api/dataset "data/construction.csv"))
(def construction-unit-map {"1 unit" "1"
                            "2 to 4 units" "2-4"
                            "5 units or more" "5+"})






construction


(md "
Conversion 1 - Group two column types
")


(-> construction
    (api/pivot->longer #"^[125NWS].*|Midwest" {:target-columns [:units :region]
                                               :splitter (fn [col-name]
                                                           (if (re-matches #"^[125].*" col-name)
                                                             [(construction-unit-map col-name) nil]
                                                             [nil col-name]))
                                               :value-column-name :n
                                               :drop-missing? false}))


(md "
Conversion 2 - Convert to longer form and back and rename columns
")


(-> construction
    (api/pivot->longer #"^[125NWS].*|Midwest" {:target-columns [:units :region]
                                               :splitter (fn [col-name]
                                                           (if (re-matches #"^[125].*" col-name)
                                                             [(construction-unit-map col-name) nil]
                                                             [nil col-name]))
                                               :value-column-name :n
                                               :drop-missing? false})
    (api/pivot->wider [:units :region] :n {:drop-missing? false})
    (api/rename-columns (zipmap (vals construction-unit-map)
                                (keys construction-unit-map))))


(md "
---

Various operations on stocks, examples taken from [gather](https://tidyr.tidyverse.org/reference/gather.html) and [spread](https://tidyr.tidyverse.org/reference/spread.html) manuals.
")


(def stocks-tidyr (api/dataset "data/stockstidyr.csv"))





stocks-tidyr


(md "
Convert to longer form
")


(def stocks-long (api/pivot->longer stocks-tidyr ["X" "Y" "Z"] {:value-column-name :price
                                                                :target-columns :stocks}))





stocks-long


(md "
Convert back to wide form
")


(api/pivot->wider stocks-long :stocks :price)


(md "
Convert to wide form on time column (let's limit values to a couple of rows)
")


(-> stocks-long
    (api/select-rows (range 0 30 4))
    (api/pivot->wider "time" :price {:drop-missing? false}))


(md "
### Join/Concat Datasets

Dataset join and concatenation functions.

Joins accept left-side and right-side datasets and columns selector. Options are the same as in `tech.ml.dataset` functions.

The difference between `tech.ml.dataset` join functions are: arguments order (first datasets) and possibility to join on multiple columns.

Additionally set operations are defined: `intersect` and `difference`.

To concat two datasets rowwise you can choose:

* `concat` - concats rows for matching columns, the number of columns should be equal.
* `union` - like concat but returns unique values
* `bind` - concats rows add missing, empty columns

To add two datasets columnwise use `bind`. The number of rows should be equal.

Datasets used in examples:
")


(def ds1 (api/dataset {:a [1 2 1 2 3 4 nil nil 4]
                       :b (range 101 110)
                       :c (map str "abs tract")}))
(def ds2 (api/dataset {:a [nil 1 2 5 4 3 2 1 nil]
                       :b (range 110 101 -1)
                       :c (map str "datatable")
                       :d (symbol "X")}))





ds1
ds2


(md "
#### Left
")


(api/left-join ds1 ds2 :b)


(md "
---
")


(api/left-join ds2 ds1 :b)


(md "
---
")


(api/left-join ds1 ds2 [:a :b])


(md "
---
")


(api/left-join ds2 ds1 [:a :b])


(md "
#### Right
")


(api/right-join ds1 ds2 :b)


(md "
---
")


(api/right-join ds2 ds1 :b)


(md "
---
")


(api/right-join ds1 ds2 [:a :b])


(md "
---
")


(api/right-join ds2 ds1 [:a :b])


(md "
#### Inner
")


(api/inner-join ds1 ds2 :b)


(md "
---
")


(api/inner-join ds2 ds1 :b)


(md "
---
")


(api/inner-join ds1 ds2 [:a :b])


(md "
---
")


(api/inner-join ds2 ds1 [:a :b])


(md "
#### Full

Join keeping all rows
")


(api/full-join ds1 ds2 :b)


(md "
---
")


(api/full-join ds2 ds1 :b)


(md "
---
")


(api/full-join ds1 ds2 [:a :b])


(md "
---
")


(api/full-join ds2 ds1 [:a :b])


(md "
#### Semi

Return rows from ds1 matching ds2
")


(api/semi-join ds1 ds2 :b)


(md "
---
")


(api/semi-join ds2 ds1 :b)


(md "
---
")


(api/semi-join ds1 ds2 [:a :b])


(md "
---
")


(api/semi-join ds2 ds1 [:a :b])


(md "
#### Anti

Return rows from ds1 not matching ds2
")


(api/anti-join ds1 ds2 :b)


(md "
---
")


(api/anti-join ds2 ds1 :b)


(md "
---
")


(api/anti-join ds1 ds2 [:a :b])


(md "
---
")


(api/anti-join ds2 ds1 [:a :b])


(md "#### asof
")


(def left-ds (api/dataset {:a [1 5 10]
                           :left-val ["a" "b" "c"]}))
(def right-ds (api/dataset {:a [1 2 3 6 7]
                            :right-val [:a :b :c :d :e]}))





left-ds
right-ds





(api/asof-join left-ds right-ds :a)





(api/asof-join left-ds right-ds :a {:asof-op :nearest})





(api/asof-join left-ds right-ds :a {:asof-op :>=})


(md "
#### Concat

`contact` joins rows from other datasets
")


(api/concat ds1)


(md "
---

`concat-copying` ensures all readers are evaluated.
")


(api/concat-copying ds1)


(md "
---
")


(api/concat ds1 (api/drop-columns ds2 :d))


(md "
---
")


^:note-to-test/skip
(apply api/concat (repeatedly 3 #(api/random DS)))


(md "
##### Concat grouped dataset

Concatenation of grouped datasets results also in grouped dataset.
")


(api/concat (api/group-by DS [:V3])
            (api/group-by DS [:V4]))


(md "

#### Union

The same as `concat` but returns unique rows
")


(apply api/union (api/drop-columns ds2 :d) (repeat 10 ds1))


(md "
---
")


^:note-to-test/skip
(apply api/union (repeatedly 10 #(api/random DS)))


(md "
#### Bind

`bind` adds empty columns during concat
")


(api/bind ds1 ds2)


(md "
---
")


(api/bind ds2 ds1)


(md "
#### Append

`append` concats columns
")


(api/append ds1 ds2)


(md "
#### Intersection
")


(api/intersect (api/select-columns ds1 :b)
               (api/select-columns ds2 :b))


(md "
#### Difference
")


(api/difference (api/select-columns ds1 :b)
                (api/select-columns ds2 :b))


(md "
---
")


(api/difference (api/select-columns ds2 :b)
                (api/select-columns ds1 :b))


(md "
### Split into train/test

In ML world very often you need to test given model and prepare collection of train and test datasets. `split` creates new dataset with two additional columns:

* `:$split-name` - with `:train`, `:test`, `:split-2`, ... values
* `:$split-id` - id of splitted group (for k-fold and repeating)

`split-type` can be one of the following:

* `:kfold` (default) - k-fold strategy, `:k` defines number of folds (defaults to `5`), produces `k` splits
* `:bootstrap` - `:ratio` defines ratio of observations put into result (defaults to `1.0`), produces `1` split
* `:holdout` - split into two or more parts with given ratio(s) (defaults to `2/3`), produces `1` split
* `:holdouts` - splits into two parts for ascending ratio. Range of rations is given by `steps` option
* `:loo` - leave one out, produces the same number of splits as number of observations

`:holdout` can accept also probabilites or ratios and can split to more than 2 subdatasets

Additionally you can provide:

* `:seed` - for random number generator
* `:shuffle?` - turn on/off shuffle of the rows (default: `true`)
* `:repeats` - repeat procedure `:repeats` times
* `:partition-selector` - same as in `group-by` for stratified splitting to reflect dataset structure in splits.
* `:split-names` names of subdatasets different than default, ie. `[:train :test :split-2 ...]`
* `:split-col-name` - a column where name of split is stored, either `:train` or `:test` values (default: `:$split-name`)
* `:split-id-col-name` - a column where id of the train/test pair is stored (default: `:$split-id`)

In case of grouped dataset each group is processed separately.

See [more](https://www.mitpressjournals.org/doi/pdf/10.1162/EVCO_a_00069)
")

^:note-to-test/skip
(def for-splitting (api/dataset (map-indexed (fn [id v] {:id id
                                                         :partition v
                                                         :group (rand-nth [:g1 :g2 :g3])})
                                             (concat (repeat 20 :a) (repeat 5 :b)))))





^:note-to-test/skip
for-splitting


(md "
#### k-Fold

Returns `k=5` maps
")


^:note-to-test/skip
(-> for-splitting
    (api/split)
    (api/head 30))


(md "
Partition according to `:k` column to reflect it's distribution
")


^:note-to-test/skip
(-> for-splitting
    (api/split :kfold {:partition-selector :partition})
    (api/head 30))


(md "
#### Bootstrap
")


^:note-to-test/skip
(api/split for-splitting :bootstrap)


(md "
with repeats, to get 100 splits
")


^:note-to-test/skip
(-> for-splitting
    (api/split :bootstrap {:repeats 100})
    (:$split-id)
    (distinct)
    (count))


(md "
#### Holdout

with small ratio
")


^:note-to-test/skip
(api/split for-splitting :holdout {:ratio 0.2})


(md "
you can split to more than two subdatasets with holdout
")


^:note-to-test/skip
(api/split for-splitting :holdout {:ratio [0.1 0.2 0.3 0.15 0.25]})


(md "
you can use also proportions with custom names
")


^:note-to-test/skip
(api/split for-splitting :holdout {:ratio [5 3 11 2]
                                   :split-names ["small" "smaller" "big" "the rest"]})


(md "
#### Holdouts

With ratios from 5% to 95% of the dataset with step 1.5 generates 15 splits with ascending rows in train dataset.
")


^:note-to-test/skip
(-> (api/split for-splitting :holdouts {:steps [0.05 0.95 1.5]
                                        :shuffle? false})
    (api/group-by [:$split-id :$split-name]))


(md "

#### Leave One Out
")


^:note-to-test/skip
(-> for-splitting
    (api/split :loo)
    (api/head 30))





^:note-to-test/skip
(-> for-splitting
    (api/split :loo)
    (api/row-count))


(md "
#### Grouped dataset with partitioning
")


^:note-to-test/skip
(-> for-splitting
    (api/group-by :group)
    (api/split :bootstrap {:partition-selector :partition :seed 11 :ratio 0.8}))


(md "
#### Split as a sequence

To get a sequence of pairs, use `split->seq` function
")


^:note-to-test/skip
(-> for-splitting
    (api/split->seq :kfold {:partition-selector :partition})
    (first))





^:note-to-test/skip
(-> for-splitting
    (api/group-by :group)
    (api/split->seq :bootstrap {:partition-selector :partition :seed 11 :ratio 0.8 :repeats 2})
    (first))


(md "
## Pipeline

`tablecloth.pipeline` exports special versions of API which create functions operating only on dataset. This creates the possibility to chain operations and compose them easily.

There are two ways to create pipelines:

* functional, as a composition of functions
* declarative, separating task declarations and concrete parametrization.

Pipeline operations are prepared to work with [metamorph](https://github.com/scicloj/metamorph) library. That means that result of the pipeline is wrapped into a map and dataset is stored under `:metamorph/data` key.
")


(require '[tablecloth.pipeline :as pip])


(md "
### Functional

To create composable function, call API function but defined in `tablecloth.pipeline` namespace and without `ds` argument.
")


(pip/select-columns :type/numerical)


(md "
Calling such function on a dataset gives a requested result.
")


((pip/select-columns :type/numerical) DS)


(md "
Pipeline functions can be composed using a `comp` function or `pipeline`. The latter is just reversed `comp` to create the order from first operation to last.
")


(let [p (pip/pipeline (pip/group-by :V1)
                      (pip/fold-by :V4)
                      (pip/ungroup))]
  (p DS))


(md "
### Declarative

To create a pipeline declarative way you can use `->pipeline` function and apply sequence of definition. Definition is simple a sequence where on the first position name of the operation is passed as a keyword, a symbol or var. The rest are operation parameters which are mostly the same as when calling a function.

To help keeping pipeline declaration as a pure data structure you can use namespaced keywords to create reference to defined symbols. The special `ctx` namespace is used to refer to a values stored in an optional map passed as an argument to `->pipeline`.

You can't used `type` and `!type` namespaces for a symbol reference, because they are reserved for column type selectors.
")


(def pipeline-declaration [[:pip/group-by :V1]
                           [:pip/unique-by ::unique-by-operation {:strategy :ctx/strategy}]
                           [:pip/ungroup {:add-group-as-column :from-V1}]])

(def unique-by-operation (fn [m] (mod (:V2 m) 3)))

(def pipeline-1 (pip/->pipeline {:strategy vec} pipeline-declaration))
(def pipeline-2 (pip/->pipeline {:strategy set} pipeline-declaration))





(pipeline-1 DS)





(pipeline-2 DS)


(md "
### Custom operator

Custom pipeline operator is just function which returns another function operating on a dataset. If you want it to work with `metamorph` you have to use `metamorph/lift` function.
")


(defn duplicate-columns
  [column-selector]
  (fn [ds]
    (let [column-names (api/column-names ds column-selector)]
      (reduce (fn [d n]
                (api/add-column d (str n "-copy") (d n))) ds column-names))))

(def pipeline (pip/->pipeline [[::duplicate-columns :type/numerical]]))





(pipeline DS)


(md "
## Functions

This API doesn't provide any statistical, numerical or date/time functions. Use below namespaces:

| Namespace | functions |
|-----------|-----------|
| `tech.v3.datatype.functional` | primitive oprations, reducers, statistics |
| `tech.v3.datatype.datetime` | date/time converters and operations|

## Other examples

### Stocks
")


(defonce stocks (api/dataset "https://raw.githubusercontent.com/techascent/tech.ml.dataset/master/test/data/stocks.csv" {:key-fn keyword}))





stocks





(-> stocks
    (api/group-by (fn [row]
                    {:symbol (:symbol row)
                     :year (tech.v3.datatype.datetime/long-temporal-field :years (:date row))}))
    (api/aggregate #(tech.v3.datatype.functional/mean (% :price)))
    (api/order-by [:symbol :year]))





(-> stocks
    (api/group-by (juxt :symbol #(tech.v3.datatype.datetime/long-temporal-field :years (% :date))))
    (api/aggregate #(tech.v3.datatype.functional/mean (% :price)))
    (api/rename-columns {:$group-name-0 :symbol
                         :$group-name-1 :year}))


(md "
### data.table

Below you can find comparizon between functionality of `data.table` and Clojure dataset API. I leave it without comments, please refer original document explaining details:

[Introduction to `data.table`](https://rdatatable.gitlab.io/data.table/articles/datatable-intro.html)

R

```{r}
library(data.table)
library(knitr)

flights <- fread(\"https://raw.githubusercontent.com/Rdatatable/data.table/master/vignettes/flights14.csv\")

kable(head(flights))
```

---

Clojure
")


(require '[tech.v3.datatype.functional :as dfn]
         '[tech.v3.datatype.argops :as aops]
         '[tech.v3.datatype :as dtype])

(defonce flights (api/dataset "https://raw.githubusercontent.com/Rdatatable/data.table/master/vignettes/flights14.csv"))





(api/head flights 6)


(md "
#### Basics

##### Shape of loaded data

R

```{r}
dim(flights)
```

---

Clojure
")


(api/shape flights)


(md "
##### What is `data.table`?

R

```{r}
DT = data.table(
  ID = c(\"b\",\"b\",\"b\",\"a\",\"a\",\"c\"),
  a = 1:6,
  b = 7:12,
  c = 13:18
)

kable(DT)

class(DT$ID)
```

---

Clojure
")


(def DT (api/dataset {:ID ["b" "b" "b" "a" "a" "c"]
                      :a (range 1 7)
                      :b (range 7 13)
                      :c (range 13 19)}))





DT





(-> :ID DT meta :datatype)


(md "
##### Get all the flights with “JFK” as the origin airport in the month of June.

R

```{r}
ans <- flights[origin == \"JFK\" & month == 6L]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/select-rows (fn [row] (and (= (get row "origin") "JFK")
                                    (= (get row "month") 6))))
    (api/head 6))


(md "
##### Get the first two rows from `flights`.

R

```{r}
ans <- flights[1:2]
kable(ans)
```

---

Clojure
")


(api/select-rows flights (range 2))


(md "
##### Sort `flights` first by column `origin` in ascending order, and then by `dest` in descending order

R

```{r}
ans <- flights[order(origin, -dest)]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/order-by ["origin" "dest"] [:asc :desc])
    (api/head 6))


(md "
##### Select `arr_delay` column, but return it as a vector

R

```{r}
ans <- flights[, arr_delay]
head(ans)
```

---

Clojure
")


(take 6 (flights "arr_delay"))


(md "
##### Select `arr_delay` column, but return as a data.table instead

R

```{r}
ans <- flights[, list(arr_delay)]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/select-columns "arr_delay")
    (api/head 6))


(md "
##### Select both `arr_delay` and `dep_delay` columns

R

```{r}
ans <- flights[, .(arr_delay, dep_delay)]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/select-columns ["arr_delay" "dep_delay"])
    (api/head 6))


(md "
##### Select both `arr_delay` and `dep_delay` columns and rename them to `delay_arr` and `delay_dep`

R

```{r}
ans <- flights[, .(delay_arr = arr_delay, delay_dep = dep_delay)]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/select-columns {"arr_delay" "delay_arr"
                         "dep_delay" "delay_arr"})
    (api/head 6))


(md "
##### How many trips have had total delay < 0?

R

```{r}
ans <- flights[, sum( (arr_delay + dep_delay) < 0 )]
ans
```

---

Clojure
")


(->> (dfn/+ (flights "arr_delay") (flights "dep_delay"))
     (aops/argfilter #(< % 0.0))
     (dtype/ecount))


(md "
or pure Clojure functions (much, much slower)
")


(->> (map + (flights "arr_delay") (flights "dep_delay"))
     (filter neg?)
     (count))


(md "
##### Calculate the average arrival and departure delay for all flights with “JFK” as the origin airport in the month of June

R

```{r}
ans <- flights[origin == \"JFK\" & month == 6L,
               .(m_arr = mean(arr_delay), m_dep = mean(dep_delay))]
kable(ans)
```

---

Clojure
")


(-> flights
    (api/select-rows (fn [row] (and (= (get row "origin") "JFK")
                                    (= (get row "month") 6))))
    (api/aggregate {:m_arr #(dfn/mean (% "arr_delay"))
                    :m_dep #(dfn/mean (% "dep_delay"))}))


(md "
##### How many trips have been made in 2014 from “JFK” airport in the month of June?

R

```{r}
ans <- flights[origin == \"JFK\" & month == 6L, length(dest)]
ans
```

or

```{r}
ans <- flights[origin == \"JFK\" & month == 6L, .N]
ans
```

---

Clojure
")


(-> flights
    (api/select-rows (fn [row] (and (= (get row "origin") "JFK")
                                    (= (get row "month") 6))))
    (api/row-count))


(md "
##### deselect columns using - or !

R

```{r}
ans <- flights[, !c(\"arr_delay\", \"dep_delay\")]
kable(head(ans))
```

or

```{r}
ans <- flights[, -c(\"arr_delay\", \"dep_delay\")]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/select-columns (complement #{"arr_delay" "dep_delay"}))
    (api/head 6))


(md "
#### Aggregations

##### How can we get the number of trips corresponding to each origin airport?

R

```{r}
ans <- flights[, .(.N), by = .(origin)]
kable(ans)
```

---

Clojure
")


(-> flights
    (api/group-by ["origin"])
    (api/aggregate {:N api/row-count}))


(md "
##### How can we calculate the number of trips for each origin airport for carrier code \"AA\"?

R

```{r}
ans <- flights[carrier == \"AA\", .N, by = origin]
kable(ans)
```

---

Clojure
")


(-> flights
    (api/select-rows #(= (get % "carrier") "AA"))
    (api/group-by ["origin"])
    (api/aggregate {:N api/row-count}))


(md "
##### How can we get the total number of trips for each `origin`, `dest` pair for carrier code \"AA\"?

R

```{r}
ans <- flights[carrier == \"AA\", .N, by = .(origin, dest)]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/select-rows #(= (get % "carrier") "AA"))
    (api/group-by ["origin" "dest"])
    (api/aggregate {:N api/row-count})
    (api/head 6))


(md "
##### How can we get the average arrival and departure delay for each `orig`,`dest` pair for each month for carrier code \"AA\"?

R

```{r}
ans <- flights[carrier == \"AA\",
        .(mean(arr_delay), mean(dep_delay)),
        by = .(origin, dest, month)]
kable(head(ans,10))
```

---

Clojure
")


(-> flights
    (api/select-rows #(= (get % "carrier") "AA"))
    (api/group-by ["origin" "dest" "month"])
    (api/aggregate [#(dfn/mean (% "arr_delay"))
                    #(dfn/mean (% "dep_delay"))])
    (api/head 10))


(md "
##### So how can we directly order by all the grouping variables?

R

```{r}
ans <- flights[carrier == \"AA\",
        .(mean(arr_delay), mean(dep_delay)),
        keyby = .(origin, dest, month)]
kable(head(ans,10))
```

---

Clojure
")


(-> flights
    (api/select-rows #(= (get % "carrier") "AA"))
    (api/group-by ["origin" "dest" "month"])
    (api/aggregate [#(dfn/mean (% "arr_delay"))
                    #(dfn/mean (% "dep_delay"))])
    (api/order-by ["origin" "dest" "month"])
    (api/head 10))


(md "
##### Can `by` accept expressions as well or does it just take columns?

R

```{r}
ans <- flights[, .N, .(dep_delay>0, arr_delay>0)]
kable(ans)
```

---

Clojure
")


(-> flights
    (api/group-by (fn [row]
                    {:dep_delay (pos? (get row "dep_delay"))
                     :arr_delay (pos? (get row "arr_delay"))}))
    (api/aggregate {:N api/row-count}))


(md "
##### Do we have to compute `mean()` for each column individually?

R

```{r}
kable(DT)

DT[, print(.SD), by = ID]
```

```{r}
kable(DT[, lapply(.SD, mean), by = ID])
```

---

Clojure
")


DT

(api/group-by DT :ID {:result-type :as-map})





(-> DT
    (api/group-by [:ID])
    (api/aggregate-columns (complement #{:ID}) dfn/mean))


(md "
##### How can we specify just the columns we would like to compute the `mean()` on?

R

```{r}
kable(head(flights[carrier == \"AA\",                         ## Only on trips with carrier \"AA\"
                   lapply(.SD, mean),                       ## compute the mean
                   by = .(origin, dest, month),             ## for every 'origin,dest,month'
                   .SDcols = c(\"arr_delay\", \"dep_delay\")])) ## for just those specified in .SDcols
```

---

Clojure
")


(-> flights
    (api/select-rows #(= (get % "carrier") "AA"))
    (api/group-by ["origin" "dest" "month"])
    (api/aggregate-columns ["arr_delay" "dep_delay"] dfn/mean)
    (api/head 6))


(md "
##### How can we return the first two rows for each month?

R

```{r}
ans <- flights[, head(.SD, 2), by = month]
kable(head(ans))
```

---

Clojure
")


(-> flights
    (api/group-by ["month"])
    (api/head 2) ;; head applied on each group
    (api/ungroup)
    (api/head 6))


(md "
##### How can we concatenate columns a and b for each group in ID?

R

```{r}
kable(DT[, .(val = c(a,b)), by = ID])
```

---

Clojure
")


(-> DT
    (api/pivot->longer [:a :b] {:value-column-name :val})
    (api/drop-columns [:$column :c]))


(md "
##### What if we would like to have all the values of column `a` and `b` concatenated, but returned as a list column?

R

```{r}
kable(DT[, .(val = list(c(a,b))), by = ID])
```

---

Clojure
")


(-> DT
    (api/pivot->longer [:a :b] {:value-column-name :val})
    (api/drop-columns [:$column :c])
    (api/fold-by :ID))


(md "
### API tour

Below snippets are taken from [A data.table and dplyr tour](https://atrebas.github.io/post/2019-03-03-datatable-dplyr/) written by Atrebas (permission granted).

I keep structure and subtitles but I skip `data.table` and `dplyr` examples.

Example data
")


(def DS (api/dataset {:V1 (take 9 (cycle [1 2]))
                      :V2 (range 1 10)
                      :V3 (take 9 (cycle [0.5 1.0 1.5]))
                      :V4 (take 9 (cycle ["A" "B" "C"]))}))





(api/dataset? DS)
(class DS)





DS


(md "
#### Basic Operations

##### Filter rows

Filter rows using indices
")


(api/select-rows DS [2 3])


(md "
---

Discard rows using negative indices

In Clojure API we have separate function for that: `drop-rows`.
")


(api/drop-rows DS (range 2 7))


(md "
---

Filter rows using a logical expression
")


(api/select-rows DS (comp #(> % 5) :V2))





(api/select-rows DS (comp #{"A" "C"} :V4))


(md "
---

Filter rows using multiple conditions
")


(api/select-rows DS #(and (= (:V1 %) 1)
                          (= (:V4 %) "A")))


(md "
---

Filter unique rows
")


(api/unique-by DS)





(api/unique-by DS [:V1 :V4])


(md "
---

Discard rows with missing values
")


(api/drop-missing DS)


(md "
---

Other filters
")


^:note-to-test/skip
(api/random DS 3) ;; 3 random rows





^:note-to-test/skip
(api/random DS (/ (api/row-count DS) 2)) ;; fraction of random rows





(api/by-rank DS :V1 zero?) ;; take top n entries


(md "
---

Convenience functions
")


(api/select-rows DS (comp (partial re-matches #"^B") str :V4))





(api/select-rows DS (comp #(<= 3 % 5) :V2))





(api/select-rows DS (comp #(< 3 % 5) :V2))





(api/select-rows DS (comp #(<= 3 % 5) :V2))


(md "
Last example skipped.

##### Sort rows

Sort rows by column
")


(api/order-by DS :V3)


(md "
---

Sort rows in decreasing order
")


(api/order-by DS :V3 :desc)


(md "
---

Sort rows based on several columns
")


(api/order-by DS [:V1 :V2] [:asc :desc])


(md "
##### Select columns

Select one column using an index (not recommended)
")


(nth (api/columns DS :as-seq) 2) ;; as column (iterable)





(api/dataset [(nth (api/columns DS :as-seq) 2)])


(md "
---

Select one column using column name
")


(api/select-columns DS :V2) ;; as dataset





(api/select-columns DS [:V2]) ;; as dataset





(DS :V2) ;; as column (iterable)


(md "
---

Select several columns
")


(api/select-columns DS [:V2 :V3 :V4])


(md "
---

Exclude columns
")


(api/select-columns DS (complement #{:V2 :V3 :V4}))





(api/drop-columns DS [:V2 :V3 :V4])


(md "
---

Other seletions
")


(->> (range 1 3)
     (map (comp keyword (partial format "V%d")))
     (api/select-columns DS))





(api/reorder-columns DS :V4)





(api/select-columns DS #(clojure.string/starts-with? (name %) "V"))





(api/select-columns DS #(clojure.string/ends-with? (name %) "3"))





(api/select-columns DS #"..2") ;; regex converts to string using `str` function





(api/select-columns DS #{:V1 "X"})





(api/select-columns DS #(not (clojure.string/starts-with? (name %) "V2")))


(md "
##### Summarise data

Summarise one column
")


(reduce + (DS :V1)) ;; using pure Clojure, as value





(api/aggregate-columns DS :V1 dfn/sum) ;; as dataset





(api/aggregate DS {:sumV1 #(dfn/sum (% :V1))})


(md "
---

Summarize several columns
")


(api/aggregate DS [#(dfn/sum (% :V1))
                   #(dfn/standard-deviation (% :V3))])





(api/aggregate-columns DS [:V1 :V3] [dfn/sum
                                     dfn/standard-deviation])


(md "

---

Summarise several columns and assign column names
")


(api/aggregate DS {:sumv1 #(dfn/sum (% :V1))
                   :sdv3 #(dfn/standard-deviation (% :V3))})


(md "
---

Summarise a subset of rows
")


(-> DS
    (api/select-rows (range 4))
    (api/aggregate-columns :V1 dfn/sum))


(md "
##### Additional helpers

")


(-> DS
    (api/first)
    (api/select-columns :V3)) ;; select first row from `:V3` column





(-> DS
    (api/last)
    (api/select-columns :V3)) ;; select last row from `:V3` column





(-> DS
    (api/select-rows 4)
    (api/select-columns :V3)) ;; select forth row from `:V3` column





(-> DS
    (api/select :V3 4)) ;; select forth row from `:V3` column





(-> DS
    (api/unique-by :V4)
    (api/aggregate api/row-count)) ;; number of unique rows in `:V4` column, as dataset


(md "
")


(-> DS
    (api/unique-by :V4)
    (api/row-count)) ;; number of unique rows in `:V4` column, as value





(-> DS
    (api/unique-by)
    (api/row-count)) ;; number of unique rows in dataset, as value


(md "
##### Add/update/delete columns

Modify a column
")


(api/map-columns DS :V1 [:V1] #(dfn/pow % 2))





(def DS (api/add-column DS :V1 (dfn/pow (DS :V1) 2)))





DS


(md "
---

Add one column
")


(api/map-columns DS :v5 [:V1] dfn/log)





(def DS (api/add-column DS :v5 (dfn/log (DS :V1))))





DS


(md "
---

Add several columns
")


(def DS (api/add-columns DS {:v6 (dfn/sqrt (DS :V1))
                             :v7 "X"}))





DS


(md "
---

Create one column and remove the others
")


(api/dataset {:v8 (dfn/+ (DS :V3) 1)})


(md "
---

Remove one column
")


(def DS (api/drop-columns DS :v5))





DS


(md "
---

Remove several columns
")


(def DS (api/drop-columns DS [:v6 :v7]))





DS


(md "
---

Remove columns using a vector of colnames

We use set here.
")


(def DS (api/select-columns DS (complement #{:V3})))





DS


(md "
---

Replace values for rows matching a condition
")


(def DS (api/map-columns DS :V2 [:V2] #(if (< % 4.0) 0.0 %)))





DS


(md "
##### by

By group
")


(-> DS
    (api/group-by [:V4])
    (api/aggregate {:sumV2 #(dfn/sum (% :V2))}))


(md "
---

By several groups
")


(-> DS
    (api/group-by [:V4 :V1])
    (api/aggregate {:sumV2 #(dfn/sum (% :V2))}))


(md "
---

Calling function in by
")


(-> DS
    (api/group-by (fn [row]
                    (clojure.string/lower-case (:V4 row))))
    (api/aggregate {:sumV1 #(dfn/sum (% :V1))}))


(md "
---

Assigning column name in by
")


(-> DS
    (api/group-by (fn [row]
                    {:abc (clojure.string/lower-case (:V4 row))}))
    (api/aggregate {:sumV1 #(dfn/sum (% :V1))}))





(-> DS
    (api/group-by (fn [row]
                    (clojure.string/lower-case (:V4 row))))
    (api/aggregate {:sumV1 #(dfn/sum (% :V1))} {:add-group-as-column :abc}))


(md "
---

Using a condition in by
")


(-> DS
    (api/group-by #(= (:V4 %) "A"))
    (api/aggregate #(dfn/sum (% :V1))))


(md "
---

By on a subset of rows
")


(-> DS
    (api/select-rows (range 5))
    (api/group-by :V4)
    (api/aggregate {:sumV1 #(dfn/sum (% :V1))}))


(md "
---

Count number of observations for each group
")


(-> DS
    (api/group-by :V4)
    (api/aggregate api/row-count))


(md "
---

Add a column with number of observations for each group
")


(-> DS
    (api/group-by [:V1])
    (api/add-column :n api/row-count)
    (api/ungroup))


(md "
---

Retrieve the first/last/nth observation for each group
")


(-> DS
    (api/group-by [:V4])
    (api/aggregate-columns :V2 first))





(-> DS
    (api/group-by [:V4])
    (api/aggregate-columns :V2 last))





(-> DS
    (api/group-by [:V4])
    (api/aggregate-columns :V2 #(nth % 1)))


(md "
#### Going further

##### Advanced columns manipulation

Summarise all the columns
")


;; custom max function which works on every type
(api/aggregate-columns DS :all (fn [col] (first (sort #(compare %2 %1) col))))


(md "
---

Summarise several columns
")


(api/aggregate-columns DS [:V1 :V2] dfn/mean)


(md "
---

Summarise several columns by group
")


(-> DS
    (api/group-by [:V4])
    (api/aggregate-columns [:V1 :V2] dfn/mean))


(md "
---

Summarise with more than one function by group
")


(-> DS
    (api/group-by [:V4])
    (api/aggregate-columns [:V1 :V2] (fn [col]
                                       {:sum (dfn/sum col)
                                        :mean (dfn/mean col)})))


(md "
Summarise using a condition
")


(-> DS
    (api/select-columns :type/numerical)
    (api/aggregate-columns :all dfn/mean))


(md "
---

Modify all the columns
")


(api/update-columns DS :all reverse)


(md "
---

Modify several columns (dropping the others)
")


(-> DS
    (api/select-columns [:V1 :V2])
    (api/update-columns :all dfn/sqrt))





(-> DS
    (api/select-columns (complement #{:V4}))
    (api/update-columns :all dfn/exp))


(md "
---

Modify several columns (keeping the others)
")


(def DS (api/update-columns DS [:V1 :V2] dfn/sqrt))





DS





(def DS (api/update-columns DS (complement #{:V4}) #(dfn/pow % 2)))





DS


(md "
---

Modify columns using a condition (dropping the others)
")


(-> DS
    (api/select-columns :type/numerical)
    (api/update-columns :all #(dfn/- % 1)))


(md "
---

Modify columns using a condition (keeping the others)
")


(def DS (api/convert-types DS :type/numerical :int32))





DS


(md "
---

Use a complex expression
")


(-> DS
    (api/group-by [:V4])
    (api/head 2)
    (api/add-column :V2 "X")
    (api/ungroup))


(md "
---

Use multiple expressions
")


(api/dataset (let [x (dfn/+ (DS :V1) (dfn/sum (DS :V2)))]
               (println (seq (DS :V1)))
               (println (api/info (api/select-columns DS :V1)))
               {:A (range 1 (inc (api/row-count DS)))
                :B x}))


(md "
##### Chain expressions

Expression chaining using >
")


(-> DS
    (api/group-by [:V4])
    (api/aggregate {:V1sum #(dfn/sum (% :V1))})
    (api/select-rows #(>= (:V1sum %) 5)))





(-> DS
    (api/group-by [:V4])
    (api/aggregate {:V1sum #(dfn/sum (% :V1))})
    (api/order-by :V1sum :desc))


(md "
##### Indexing and Keys

Set the key/index (order)
")


(def DS (api/order-by DS :V4))





DS


(md "
Select the matching rows
")


(api/select-rows DS #(= (:V4 %) "A"))





(api/select-rows DS (comp #{"A" "C"} :V4))


(md "
---

Select the first matching row
")


(-> DS
    (api/select-rows #(= (:V4 %) "B"))
    (api/first))





(-> DS
    (api/unique-by :V4)
    (api/select-rows (comp #{"B" "C"} :V4)))


(md "
---

Select the last matching row
")


(-> DS
    (api/select-rows #(= (:V4 %) "A"))
    (api/last))


(md "
---

Nomatch argument
")


(api/select-rows DS (comp #{"A" "D"} :V4))


(md "
---

Apply a function on the matching rows
")


(-> DS
    (api/select-rows (comp #{"A" "C"} :V4))
    (api/aggregate-columns :V1 (fn [col]
                                 {:sum (dfn/sum col)})))


(md "
---

Modify values for matching rows
")


(def DS (-> DS
            (api/map-columns :V1 [:V1 :V4] #(if (= %2 "A") 0 %1))
            (api/order-by :V4)))





DS


(md "
---

Use keys in by
")


(-> DS
    (api/select-rows (comp (complement #{"B"}) :V4))
    (api/group-by [:V4])
    (api/aggregate-columns :V1 dfn/sum))


(md "
---

Set keys/indices for multiple columns (ordered)
")


(api/order-by DS [:V4 :V1])


(md "
---

Subset using multiple keys/indices
")


(-> DS
    (api/select-rows #(and (= (:V1 %) 1)
                           (= (:V4 %) "C"))))





(-> DS
    (api/select-rows #(and (= (:V1 %) 1)
                           (#{"B" "C"} (:V4 %)))))





(-> DS
    (api/select-rows #(and (= (:V1 %) 1)
                           (#{"B" "C"} (:V4 %))) {:result-type :as-indexes}))


(md "
##### set*() modifications

Replace values

There is no mutating operations `tech.ml.dataset` or easy way to set value.
")


(def DS (api/update-columns DS :V2 #(map-indexed (fn [idx v]
                                                   (if (zero? idx) 3 v)) %)))





DS


(md "
---

Reorder rows
")


(def DS (api/order-by DS [:V4 :V1] [:asc :desc]))





DS


(md "
---

Modify colnames
")


(def DS (api/rename-columns DS {:V2 "v2"}))





DS





(def DS (api/rename-columns DS {"v2" :V2})) ;; revert back


(md "
---

Reorder columns
")


(def DS (api/reorder-columns DS :V4 :V1 :V2))





DS


(md "
##### Advanced use of by

Select first/last/… row by group
")


(-> DS
    (api/group-by :V4)
    (api/first)
    (api/ungroup))





(-> DS
    (api/group-by :V4)
    (api/select-rows [0 2])
    (api/ungroup))





(-> DS
    (api/group-by :V4)
    (api/tail 2)
    (api/ungroup))


(md "
---

Select rows using a nested query
")


(-> DS
    (api/group-by :V4)
    (api/order-by :V2)
    (api/first)
    (api/ungroup))


(md "
Add a group counter column
")


(-> DS
    (api/group-by [:V4 :V1])
    (api/ungroup {:add-group-id-as-column :Grp}))


(md "
---

Get row number of first (and last) observation by group
")


(-> DS
    (api/add-column :row-id (range))
    (api/select-columns [:V4 :row-id])
    (api/group-by :V4)
    (api/ungroup))





(-> DS
    (api/add-column :row-id (range))
    (api/select-columns [:V4 :row-id])
    (api/group-by :V4)
    (api/first)
    (api/ungroup))





(-> DS
    (api/add-column :row-id (range))
    (api/select-columns [:V4 :row-id])
    (api/group-by :V4)
    (api/select-rows [0 2])
    (api/ungroup))


(md "
---

Handle list-columns by group
")


(-> DS
    (api/select-columns [:V1 :V4])
    (api/fold-by :V4))





(-> DS
    (api/group-by :V4)
    (api/unmark-group))


(md "
---

Grouping sets (multiple by at once)

Not available.

#### Miscellaneous

##### Read / Write data

Write data to a csv file
")


(api/write! DS "DF.csv")


(md "
---

Write data to a tab-delimited file
")


(api/write! DS "DF.txt" {:separator \tab})


(md "
or
")


(api/write! DS "DF.tsv")


(md "
---

Read a csv / tab-delimited file
")


(api/dataset "DF.csv" {:key-fn keyword})





(api/dataset "DF.txt" {:key-fn keyword})





(api/dataset "DF.tsv" {:key-fn keyword})


(md "
---

Read a csv file selecting / droping columns
")


(api/dataset "DF.csv" {:key-fn keyword
                       :column-whitelist ["V1" "V4"]})





(api/dataset "DF.csv" {:key-fn keyword
                       :column-blacklist ["V4"]})


(md "
---

Read and rbind several files
")


(apply api/concat (map api/dataset ["DF.csv" "DF.csv"]))


(md "
##### Reshape data

Melt data (from wide to long)
")


(def mDS (api/pivot->longer DS [:V1 :V2] {:target-columns :variable
                                          :value-column-name :value}))





mDS


(md "
---

Cast data (from long to wide)
")


(-> mDS
    (api/pivot->wider :variable :value {:fold-fn vec})
    (api/update-columns ["V1" "V2"] (partial map count)))





(-> mDS
    (api/pivot->wider :variable :value {:fold-fn vec})
    (api/update-columns ["V1" "V2"] (partial map dfn/sum)))





(-> mDS
    (api/map-columns :value #(> % 5))
    (api/pivot->wider :value :variable {:fold-fn vec})
    (api/update-columns ["true" "false"] (partial map #(if (sequential? %) (count %) 1))))


(md "
---

Split
")


(api/group-by DS :V4 {:result-type :as-map})


(md "
---

Split and transpose a vector/column
")


(-> {:a ["A:a" "B:b" "C:c"]}
    (api/dataset)
    (api/separate-column :a [:V1 :V2] ":"))


(md "
##### Other

Skipped

#### Join/Bind data sets
")


(def x (api/dataset {"Id" ["A" "B" "C" "C"]
                     "X1" [1 3 5 7]
                     "XY" ["x2" "x4" "x6" "x8"]}))
(def y (api/dataset {"Id" ["A" "B" "B" "D"]
                     "Y1" [1 3 5 7]
                     "XY" ["y1" "y3" "y5" "y7"]}))





x y


(md "
##### Join

Join matching rows from y to x
")


(api/left-join x y "Id")


(md "
---

Join matching rows from x to y
")


(api/right-join x y "Id")


(md "
---

Join matching rows from both x and y
")


(api/inner-join x y "Id")


(md "
---

Join keeping all the rows
")


(api/full-join x y "Id")


(md "
---

Return rows from x matching y
")


(api/semi-join x y "Id")


(md "
---

Return rows from x not matching y
")


(api/anti-join x y "Id")


(md "
##### More joins

Select columns while joining
")


(api/right-join (api/select-columns x ["Id" "X1"])
                (api/select-columns y ["Id" "XY"])
                "Id")





(api/right-join (api/select-columns x ["Id" "XY"])
                (api/select-columns y ["Id" "XY"])
                "Id")


(md "
Aggregate columns while joining
")


(-> y
    (api/group-by ["Id"])
    (api/aggregate {"sumY1" #(dfn/sum (% "Y1"))})
    (api/right-join x "Id")
    (api/add-column "X1Y1" (fn [ds] (dfn/* (ds "sumY1")
                                           (ds "X1"))))
    (api/select-columns ["right.Id" "X1Y1"]))


(md "
Update columns while joining
")


(-> x
    (api/select-columns ["Id" "X1"])
    (api/map-columns "SqX1" "X1" (fn [x] (* x x)))
    (api/right-join y "Id")
    (api/drop-columns ["X1" "Id"]))


(md "
---

Adds a list column with rows from y matching x (nest-join)

")


(-> (api/left-join x y "Id")
    (api/drop-columns ["right.Id"])
    (api/fold-by (api/column-names x)))


(md "
---

Some joins are skipped

---

Cross join
")


(def cjds (api/dataset {:V1 [[2 1 1]]
                        :V2 [[3 2]]}))





cjds





(reduce #(api/unroll %1 %2) cjds (api/column-names cjds))





(-> (reduce #(api/unroll %1 %2) cjds (api/column-names cjds))
    (api/unique-by))


(md "
##### Bind
")


(def x (api/dataset {:V1 [1 2 3]}))
(def y (api/dataset {:V1 [4 5 6]}))
(def z (api/dataset {:V1 [7 8 9]
                     :V2 [0 0 0]}))





x y z


(md "
---

Bind rows
")


(api/bind x y)





(api/bind x z)


(md "
---

Bind rows using a list
")


(->> [x y]
     (map-indexed #(api/add-column %2 :id (repeat %1)))
     (apply api/bind))


(md "
---

Bind columns
")


(api/append x y)


(md "
##### Set operations
")


(def x (api/dataset {:V1 [1 2 2 3 3]}))
(def y (api/dataset {:V1 [2 2 3 4 4]}))





x y


(md "
---

Intersection
")


(api/intersect x y)


(md "
---

Difference
")


(api/difference x y)


(md "
---

Union
")


(api/union x y)





(api/concat x y)


(md "
---

Equality not implemented")
