(ns tablecloth.api.missing
  (:require [tech.ml.dataset :as ds]
            [tech.ml.dataset.column :as col]
            [tech.v2.datatype.readers.update :as update-rdr]
            [tech.v2.datatype.bitmap :as bitmap]
            [tech.v2.datatype :as dtype]
            [tech.v2.datatype.datetime.operations :as dto]
            
            [tablecloth.api.utils :refer [iterable-sequence? column-names type?]]
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
  [[primary-f secondary-f] col missing value]
  (let [cnt (dtype/ecount col)
        missing-map-pre-fn (fn [f m v]
                             (let [vv (f missing v)] 
                               (if (< -1 vv cnt)
                                 (assoc m v (dtype/get-value col vv))
                                 m)))
        missing-map-fn #(reduce (partial missing-map-pre-fn %1) {} %2) 
        step1 (replace-missing-with-value col missing (missing-map-fn primary-f missing))
        missing2 (col/missing step1)]
    (if (empty? missing2)
      step1
      (if (nil? value)
        (replace-missing-with-value step1 missing2 (missing-map-fn secondary-f missing2))
        (replace-missing-with-value step1 missing2 value)))))

;; mid and range

(defn- find-missing-ranges
  [^RoaringBitmap missing]
  (when-not (empty? missing)
    (loop [current (.first missing)
           buff []]
      (if (neg? current)
        buff
        (let [p (.previousAbsentValue missing current)
              n (.nextAbsentValue missing current)]
          (recur (.nextValue missing n) (conj buff [p n])))))))

(defn- lerp
  [coerce-type-fn mn mx steps]
  (let [steps+ (inc steps)]
    (map #(+ mn (coerce-type-fn (* % (/ (- mx mn) steps+)))) (range 1 steps+))))

(def ^:private lerp-double (partial lerp double))
(def ^:private lerp-float (partial lerp float))
(def ^:private lerp-long (partial lerp #(long (Math/round ^double %))))
(def ^:private lerp-int (partial lerp #(int (Math/round ^double %))))
(def ^:private lerp-short (partial lerp #(short (Math/round ^double %))))
(def ^:private lerp-byte (partial lerp #(byte (Math/round ^double %))))

(defn- lerp-time
  [datatype mn mx steps]
  (let [vs (lerp-long (dto/->milliseconds mn) (dto/->milliseconds mx) steps)]
    (seq (dto/milliseconds->datetime datatype vs))))

(defn- lerp-object
  [f l steps]
  (let [h (/ steps 2.0)]
    (map #(if (< % h) f l)(range steps))))

(defn- find-lerp
  [v datatype]
  (or (condp = datatype
        :int8 lerp-byte
        :int16 lerp-short
        :int32 lerp-int
        :int64 lerp-long
        :float32 lerp-float
        :float64 lerp-double
        :object (cond
                  (integer? v) lerp-long
                  (number? v) lerp-double
                  :else lerp-object)
        nil)
      (cond
        (type? :datetime datatype) (partial lerp-time datatype)
        :else lerp-object)))

(defn- find-lerp-values
  ([cnt col lerp curr [start end]]
   (let [no-left? (neg? start)
         no-right? (>= end cnt)]
     (when-not (and no-left? no-right?) ;; all values are missing?
       (let [s (dtype/get-value col (if no-left? end start))
             e (dtype/get-value col (if no-right? start end))
             start (inc start)
             end (min cnt end)
             size (- end start)
             lerp (or lerp (find-lerp s (dtype/get-datatype col)))]
         (merge curr (zipmap (range start end) (lerp s e size))))))))

(defn- replace-missing-with-lerp
  ([col missing] (replace-missing-with-lerp nil col missing))
  ([lerp col missing]
   (let [ranges (find-missing-ranges missing)
         cnt (dtype/ecount col)
         missing-replace (reduce (partial find-lerp-values cnt col lerp) {} ranges)]
     (replace-missing-with-value col missing missing-replace))))

(defn- replace-missing-with-strategy
  [col missing strategy value]
  (let [value (if (fn? value)
                (value (dtype/->reader col (dtype/get-datatype col) {:missing-policy :elide}))
                value)]
    (condp = strategy
      :down (replace-missing-with-direction [missing-direction-prev missing-direction-next] col missing value)
      :up (replace-missing-with-direction [missing-direction-next missing-direction-prev] col missing value)
      :lerp (replace-missing-with-lerp col missing)
      :mid (replace-missing-with-lerp lerp-object col missing)
      (replace-missing-with-value col missing value))))

;; TODO change API to reflect strategies without values

(defn replace-missing
  ([ds] (replace-missing ds :mid))
  ([ds strategy] (replace-missing ds :all strategy))
  ([ds columns-selector strategy] (replace-missing ds columns-selector strategy nil))
  ([ds columns-selector strategy value]

   (let [strategy (or strategy :mid)]
     
     (if (grouped? ds)

       (process-group-data ds #(replace-missing % columns-selector value strategy))
       
       (let [cols (column-names ds columns-selector)
             row-cnt (ds/row-count ds)]
         (reduce (fn [ds colname]
                   (let [col (ds colname)
                         ^RoaringBitmap missing (col/missing col)]
                     (if-not (or (empty? missing)
                                 (and (= row-cnt (.getCardinality missing))
                                      (not (and (= :value strategy)
                                                (not (fn? value))))))
                       (ds/add-or-update-column ds (replace-missing-with-strategy col missing strategy value))
                       ds))) ds cols))))))
