(ns tablecloth.api.missing
  (:require [tech.ml.dataset :as ds]
            [tech.ml.dataset.column :as col]
            [tech.v2.datatype.readers.update :as update-rdr]
            [tech.v2.datatype.bitmap :as bitmap]
            [tech.v2.datatype :as dtype]
            
            [tablecloth.api.utils :refer [iterable-sequence? column-names]]
            [tablecloth.api.columns :refer [select-columns]]
            [tablecloth.api.group-by :refer [grouped? process-group-data]])
  (:import [org.roaringbitmap RoaringBitmap]))

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

(def ^{:doc (select-or-drop-missing-docstring "Select")}
  select-missing (partial select-or-drop-missing ds/select-rows))

(def ^{:doc (select-or-drop-missing-docstring "Drop")}
  drop-missing (partial select-or-drop-missing ds/drop-rows))

(defn- remove-from-rbitmap
  ^RoaringBitmap [^RoaringBitmap rb ks]
  (let [rb (.clone rb)]
    (reduce (fn [^RoaringBitmap rb ^long k]
              (.remove rb k)
              rb) rb ks)))

(defn- replace-missing-with-value
  [col missing value]
  (col/new-column (col/column-name col)
                  (update-rdr/update-reader col (cond
                                                  (map? value) value
                                                  (iterable-sequence? value) (zipmap missing (cycle value))
                                                  :else (bitmap/bitmap-value->bitmap-map missing value)))
                  {} (if (map? value)
                       (remove-from-rbitmap missing (keys value))
                       (RoaringBitmap.))))

(defn- missing-direction-prev
  ^long [^RoaringBitmap rb ^long idx]
  (.previousAbsentValue rb idx))

(defn- missing-direction-next
  ^long [^RoaringBitmap rb ^long idx]
  (.nextAbsentValue rb idx))

(defn- replace-missing-with-direction
  [f col missing value]
  (let [cnt (dtype/ecount col)
        step1 (replace-missing-with-value col missing (reduce (fn [m v]
                                                                (let [vv (f missing v)]
                                                                  (if (< -1 vv cnt)
                                                                    (assoc m v (dtype/get-value col vv))
                                                                    m))) {} missing))]
    (if (or (nil? value)
            (empty? (col/missing step1)))
      step1
      (replace-missing-with-value step1 (col/missing step1) value))))

(defn- replace-missing-with-strategy
  [col missing value strategy]
  (let [value (if (fn? value)
                (value (dtype/->reader col (dtype/get-datatype col) {:missing-policy :elide}))
                value)]
    (condp = strategy
      :down (replace-missing-with-direction missing-direction-prev col missing value)
      :up (replace-missing-with-direction missing-direction-next col missing value)
      (replace-missing-with-value col missing value))))

(defn replace-missing
  ([ds value] (replace-missing ds :all value))
  ([ds columns-selector value] (replace-missing ds columns-selector value nil))
  ([ds columns-selector value strategy]

   (let [strategy (or strategy :value)]
     
     (if (grouped? ds)

       (process-group-data ds #(replace-missing % columns-selector value strategy))
       
       (let [cols (column-names ds columns-selector)]
         (reduce (fn [ds colname]
                   (let [col (ds colname)
                         missing (col/missing col)]
                     (if-not (empty? missing)
                       (ds/add-or-update-column ds (replace-missing-with-strategy col missing value strategy))
                       ds))) ds cols))))))

