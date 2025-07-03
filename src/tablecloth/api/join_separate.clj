(ns tablecloth.api.join-separate
  (:refer-clojure :exclude [pmap])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            
            [tech.v3.tensor :as tens]
            [tech.v3.datatype :as dtt]
            [clojure.string :as str]
            [tech.v3.parallel.for :refer [pmap]]
            [tech.v3.dataset.tensor]
            [tablecloth.api.dataset :as tc-dataset]
            [tablecloth.api.utils :refer [iterable-sequence? column-names grouped? process-group-data ->str ]]
            [tablecloth.api.columns :refer [select-columns drop-columns add-column rename-columns]]))

(defn- process-join-columns
  [ds target-column join-function col-names drop-columns?]
  (let [cols (select-columns ds col-names)
        result (add-column ds target-column (when (seq cols) (->> (ds/value-reader cols)
                                                                  (map join-function))))]
    (if drop-columns? (drop-columns result col-names) result)))

(defn join-columns
  "Join clumns of dataset. Accepts:
  dataset
  column selector (as in select-columns)
  options
  `:separator` (default \"-\")
  `:drop-columns?` - whether to drop source columns or not (default true)
  `:result-type`
     `:map` - packs data into map
     `:seq` - packs data into sequence
     `:string` - join strings with separator (default)
     or custom function which gets row as a vector
  `:missing-subst` - substitution for missing value"
  ([ds target-column columns-selector] (join-columns ds target-column columns-selector nil))
  ([ds target-column columns-selector {:keys [separator missing-subst drop-columns? result-type parallel?]
                                       :or {separator "-" drop-columns? true result-type :string}
                                       :as _conf}]
   
   (let [missing-subst-fn #(map (fn [v] (if (nil? v) missing-subst v)) %)
         col-names (column-names ds columns-selector)
         join-function (comp (cond
                               (= :map result-type) #(zipmap col-names %)
                               (= :seq result-type) seq
                               (fn? result-type) result-type
                               :else (if (iterable-sequence? separator)
                                       (let [sep (concat
                                                  (conj (seq separator) :empty)
                                                  (cycle separator))]
                                         (fn [row] (->> row
                                                       (remove nil?)
                                                       (interleave sep)
                                                       (rest)
                                                       (apply str))))
                                       (fn [row] (->> row
                                                     (remove nil?)
                                                     (str/join separator)))))
                             missing-subst-fn)]

     (if (grouped? ds)
       (process-group-data ds #(process-join-columns % target-column join-function col-names drop-columns?) parallel?)
       (process-join-columns ds target-column join-function col-names drop-columns?)))))

(defn- prefix [prefix-name value]
  (let [with-prefix (str (->str prefix-name) "-" value)]
    (if (keyword? prefix-name)
      (keyword with-prefix)
      with-prefix)))

(defn- infer-target-columns
  [col res]
  (let [colname (col/column-name col)]
    (map #(prefix colname %) (range (count (first res))))))

(defn- separate-column->columns
  [col target-columns replace-missing separator-fn]
  (let [res (pmap separator-fn col)]
    (if (map? (first res))
      (ds/->dataset res) ;; ds/column->dataset functionality
      (->> (if (or (= :infer target-columns)
                   (not target-columns)) (infer-target-columns col res)
               target-columns)
           (map-indexed vector)
           (reduce (fn [curr [idx colname]]
                     (if-not colname
                       curr
                       (conj curr colname (pmap #(replace-missing (nth % idx)) res)))) [])
           (apply array-map)
           (ds/->dataset)))))

(defn- prepare-missing-subst-fn
  [missing-subst]
  (let [missing-subst-fn (cond
                           (or (set? missing-subst)
                               (fn? missing-subst)) missing-subst
                           (iterable-sequence? missing-subst) (set missing-subst)
                           :else (partial = missing-subst))]
    (fn [v] (if (missing-subst-fn v) nil v))))

(defn- process-separate-columns
  [ds column target-columns replace-missing separator-fn drop-column?]
  (let [result (separate-column->columns (ds column) target-columns replace-missing separator-fn)]
    (if (= drop-column? :all)
      result
      (let [[dataset-before dataset-after] (map (partial ds/select-columns ds)
                                                (split-with #(not= % column)
                                                            (ds/column-names ds)))]
        (cond-> (ds/->dataset dataset-before)
          (not drop-column?) (ds/add-column (ds column))
          result (ds/append-columns (seq (ds/columns result)))
          :else (ds/append-columns (ds/columns (ds/drop-columns dataset-after [column]))))))))

(defn separate-column
  ([ds column] (separate-column ds column identity))
  ([ds column separator] (separate-column ds column nil separator))
  ([ds column target-columns separator] (separate-column ds column target-columns separator nil))
  ([ds column target-columns separator {:keys [missing-subst drop-column? parallel?]
                                        :or {missing-subst ""}
                                        :as _conf}]
   (let [separator-fn (cond
                        (string? separator) (let [pat (re-pattern separator)]
                                              #(-> (str %)
                                                   (str/split pat)
                                                   (concat (repeat ""))))
                        (instance? java.util.regex.Pattern separator) #(-> separator
                                                                           (re-matches (str %))
                                                                           (rest)
                                                                           (concat (repeat "")))
                        :else separator)
         replace-missing (if missing-subst
                           (prepare-missing-subst-fn missing-subst)
                           identity)
         drop-column? (if (not (nil? drop-column?)) drop-column? true)]
     
     (if (grouped? ds)       
       (process-group-data ds #(process-separate-columns % column target-columns replace-missing separator-fn drop-column?) parallel?)
       (process-separate-columns ds column target-columns replace-missing separator-fn drop-column?)))))

(defn- prefix [prefix-name value]
  (let [with-prefix (str (->str prefix-name) "-" value)]
    (if (keyword? prefix-name)
      (keyword with-prefix)
      with-prefix)))


(defn- combine-with-dash [arg1 arg2]
  (let [to-string (fn [x]
                    (cond
                      (string? x) x
                      (keyword? x) (name x)
                      (symbol? x) (name x)
                      :else (str x)))
        combined-str (str (to-string arg1) "-" (to-string arg2))]
    (cond
      (keyword? arg1) (keyword combined-str)
      (symbol? arg1) (symbol combined-str)
      (string? arg1) combined-str
      :else combined-str)))

(defn map-column->columns 
  "
   The map-column->columns function transforms a dataset by expanding a column containing map values into 
   multiple new columns. Specifically, it takes a source dataset ds and a source column src-col within that dataset (which contains map values), and performs the following operations:

- Extracts the map data from src-col.
- Creates a new dataset from this map data, where each key in the maps becomes a column.
- Generates new column names by combining the name of src-col with each of the original map keys, using a dash (-) as a separator. The type (keyword, symbol, or string) of the new column names matches the type of src-col.
- Appends these new columns to the original dataset ds.
- Removes the original src-col from ds.

The result is a new dataset that includes all original columns (except src-col) and the newly expanded columns derived from the maps in src-col.
Parameters
 

'ds': The input dataset, expected to be a Tablecloth dataset or any dataset compatible with the functions used.
'src-col': The name (keyword, symbol, or string) of the source column in ds that contains map values.

   Return Value
 
A new dataset with the following characteristics:

Contains all columns from the original dataset ds, except the src-col.
Includes new columns derived from the keys of the maps in src-col, with names formed by combining src-col and the map keys.
The new columns are appropriately named and typed, maintaining the type consistency with src-col.
   "
  [ds src-col]
  (let [columns-ds
        (tc-dataset/dataset (get ds src-col))

        new-col-names
        (map #(combine-with-dash src-col %)
             (column-names columns-ds))

        renamed-columns-ds
        (rename-columns columns-ds
                           (zipmap
                            (column-names columns-ds)
                            new-col-names))]
    (->
     (ds/append-columns ds (tc-dataset/columns renamed-columns-ds))
     (ds/remove-column src-col))))


(defn array-column->columns
  "Converts a column of type java array into several columns,
  one for each element of the array of all rows. The source column is dropped afterwards.
  The function assumes that arrays in all rows have same type and length and are numeric.

  `ds` Datset to operate on.
  `src-column` The (array) column to convert
  `opts` can contain:
    `prefix` newly created column will get prefix before column number
  "
  ([ds src-column opts]
   (assert (not (grouped? ds)) "Not supported on grouped datasets")
   (let [len-arrays (-> ds (get src-column) first count)
         new-ds
         (->
          (dtt/concat-buffers (get ds src-column))
          (tens/reshape [(ds/row-count ds) len-arrays])
          (tech.v3.dataset.tensor/tensor->dataset))

         new-ds-renamed (if (:prefix opts)
                          (ds/rename-columns new-ds
                                             (zipmap (range len-arrays)
                                                     (map #(prefix (:prefix opts) %) (range len-arrays))))

                          new-ds)
         ]
     (-> ds
         (ds/append-columns (ds/columns new-ds-renamed))
         (ds/drop-columns [src-column]))))
  ([ds src-column]
   (array-column->columns ds src-column {})))

(defn columns->array-column
  "Converts several columns to a single column of type array.
   The src columns are dropped afterwards.

  `ds` Dataset to operate on.
  `column-selector` anything supported by [[select-columns]]
  `new-column` new column to create
  "
  [ds column-selector new-column]
  (assert (not (grouped? ds)) "Not supported on grouped datasets")
  (let [ds-to-convert (select-columns ds column-selector)
        rows
        (->
         (dtt/concat-buffers (ds/columns ds-to-convert))
         (tens/reshape [(ds/column-count ds-to-convert)
                        (ds/row-count ds-to-convert)])
         (tens/transpose [1 0])
         (tens/rows))]
    (-> ds
        (drop-columns (column-names ds-to-convert))
        (ds/add-column (ds/new-column new-column (map tech.v3.datatype/->array rows))))))
