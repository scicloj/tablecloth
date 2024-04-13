(ns tablecloth.api.utils
  (:refer-clojure :exclude [pmap])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.io :as tio]
            [tech.v3.parallel.for :refer [pmap]])
  (:import [java.util Map]))

;;;;;;;;;;;;
;; HELPERS
;;;;;;;;;;;;

(defn iterable-sequence?
  "Check if object is sequential, is column or maybe a reader (iterable)?"
  [xs]
  (or (sequential? xs)      
      (and (not (map? xs))
           (instance? Iterable xs))))

(defn map-inst? [m] (instance? Map m))

(defn ->str
  [v]
  (if (instance? clojure.lang.Named v) (name v) (str v)))

(defn rank
  "Sample ranks. See [R docs](https://www.rdocumentation.org/packages/base/versions/3.6.1/topics/rank).
  Rank uses 0 based indexing.
  
  Possible tie strategies: `:average`, `:first`, `:last`, `:random`, `:min`, `:max`, `:dense`.
  `:dense` is the same as in `data.table::frank` from R"
  ([vs] (rank vs :average))
  ([vs ties] (rank vs ties false))
  ([vs ties desc?]
   (let [cmp (if desc? #(compare %2 %1) compare)
         indexed-sorted-map (group-by second (map-indexed vector (sort cmp vs)))]
     (if (#{:first :last :random} ties)
       (let [tie-sort (case ties
                        :first (partial sort-by first clojure.core/<)
                        :last (partial sort-by first clojure.core/>)
                        :random shuffle)
             sorted2-map (into {} (map (fn [[k v]] [k (tie-sort v)]) indexed-sorted-map))]
         (first (reduce (fn [[res curr] v]
                          (let [lst (curr v)]
                            [(conj res (ffirst lst))
                             (assoc curr v (rest lst))])) [[] sorted2-map] vs)))
       (let [tie-fn (case ties
                      :min ffirst
                      :dense ffirst
                      :max (comp first last)
                      (fn ^double [v] (/ ^double (reduce #(+ ^double %1 ^double %2) (map first v)) (count v))))
             m (map (fn [[k v]] [k (tie-fn v)]) indexed-sorted-map)
             m (if (= ties :dense)
                 (map-indexed (fn [id [k _]]
                                [k id]) (sort-by second m))
                 m)]
         (map (into {} m) vs))))))

;;
(def ^:private type-sets
  {:datetime #{:zoned-date-time :local-date :local-time :local-date-time :instant :duration
               :packed-zoned-date-time :packed-local-date :packed-local-time
               :packed-local-date-time :packed-instant :packed-duration}
   :integer #{:int8 :int16 :int32 :int64 :uint8 :uint16 :uint32 :uint64
              :long :int :short :byte}
   :float #{:float32 :float64 :double :float}
   :numerical #{:int8 :int16 :int32 :int64 :uint8 :uint16 :uint32 :uint64
                :long :int :short :byte
                :float32 :float64 :double :float}
   :textual #{:text :string}
   :logical #{:boolean}})

;; This lookup is hardcoded as an optimization. Downside: this
;; lookup must be kept up to date. However, so long as `type-sets`
;; is up-to-date it can be generated from that set.
(def ^:private general-types-lookup
  {:int32 #{:integer :numerical},
    :int16 #{:integer :numerical},
    :float32 #{:float :numerical},
    :packed-local-time #{:datetime},
    :local-date-time #{:datetime},
    :packed-zoned-date-time #{:datetime},
    :float64 #{:float :numerical},
    :long #{:integer :numerical},
    :double #{:float :numerical},
    :short #{:integer :numerical},
    :packed-local-date-time #{:datetime},
    :zoned-date-time #{:datetime},
    :instant #{:datetime},
    :packed-local-date #{:datetime},
    :int #{:integer :numerical},
    :int64 #{:integer :numerical},
    :local-time #{:datetime},
    :packed-duration #{:datetime},
    :uint64 #{:integer :numerical},
    :float #{:float :numerical},
    :duration #{:datetime},
    :string #{:textual},
    :uint16 #{:integer :numerical},
    :int8 #{:integer :numerical},
    :uint32 #{:integer :numerical},
    :byte #{:integer :numerical},
    :local-date #{:datetime},
    :boolean #{:logical},
    :packed-instant #{:datetime},
    :text #{:textual},
    :uint8 #{:integer :numerical}})

(defn type?
  ([general-type]
   (fn [datatype]
     (type? general-type datatype)))
  ([general-type datatype]
   ((type-sets general-type) datatype)))

(defn ->general-types
  "Given a concrete `datatype` (e.g. `:int32`), returns the general
  set of general types (e.g. `#{:integer numerical}`)."
  [datatype]
  (general-types-lookup datatype))

(defn types
  "Returns the set of concrete types e.g. (:int32, :float32, etc)"
  []
  (apply clojure.set/union (vals type-sets)))

(defn general-types
  "Returns the set of general types e.g. (:integer, :logical, etc)"
  []
  (vals type-sets))

(defn concrete-type?
  "Returns true if `datatype` is a concrete datatype (e.g. :int32)."
  [datatype]
  (not (nil? ((types) datatype))))

(defn- prepare-datatype-set
  [datatype-columns-selector]
  (let [k (-> datatype-columns-selector name keyword)]
    (get type-sets k #{k})))


(defn- filter-column-names
  "Filter column names"
  [ds columns-selector meta-field]
  (let [field-fn (if (= :all meta-field)
                   identity
                   (or meta-field :name))]
    (->> ds
         (ds/columns)
         (map meta)
         (filter (comp columns-selector field-fn))
         (map :name))))

(defn- filter-column-names-with-order
  [ds column-names meta-field]
  (let [existing-names (set (filter-column-names ds (partial contains? (set column-names)) meta-field))]
    (filter existing-names column-names)))

(defn column-names
  "Returns column names, given a selector.
  Columns-selector can be one of the following:

  * :all keyword - selects all columns
  * column name - for single column
  * sequence of column names - for collection of columns
  * regex - to apply pattern on column names or datatype
  * filter predicate - to filter column names or datatype
  * type namespaced keyword for specific datatype or group of datatypes

  Column name can be anything.

column-names function returns names according to columns-selector
  and optional meta-field. meta-field is one of the following:

  * `:name` (default) - to operate on column names
  * `:datatype` - to operated on column types
  * `:all` - if you want to process all metadata

  Datatype groups are:

  * `:type/numerical` - any numerical type
  * `:type/float` - floating point number (:float32 and :float64)
  * `:type/integer` - any integer
  * `:type/datetime` - any datetime type

  If qualified keyword starts with :!type, complement set is used.


  "
  ([ds] (column-names ds :all))
  ([ds columns-selector] (column-names ds columns-selector :name))
  ([ds columns-selector meta-field]
   (when-not (nil? columns-selector)
     (let [ds (if (:grouped? (meta ds)) (first (ds :data)) ds)]
       (cond (= :all columns-selector) (ds/column-names ds)
             (and (keyword? columns-selector)
                  (= "type" (namespace columns-selector))) (column-names ds (prepare-datatype-set columns-selector) :datatype)
             (and (keyword? columns-selector)
                  (= "!type" (namespace columns-selector))) (column-names ds (complement (prepare-datatype-set columns-selector)) :datatype)
             (and (not (set? columns-selector)) ;; huh
                  (iterable-sequence? columns-selector)) (filter-column-names-with-order ds columns-selector meta-field)
             (map? columns-selector) (filter-column-names-with-order ds (keys columns-selector) meta-field)
             :else (let [csel-fn (cond
                                   (set? columns-selector) #(contains? columns-selector %)
                                   (instance? java.util.regex.Pattern columns-selector) #(re-matches columns-selector (str %))
                                   (fn? columns-selector) columns-selector
                                   :else #(= % columns-selector))]
                     (filter-column-names ds csel-fn meta-field)))))))

;; nippy

(defn- gzipped?
  [filename]
  (re-matches #".+\.gz$" filename))

(defn ^:deprecated write-nippy!
  [ds filename]
  (let [f (if (gzipped? filename)
            (tio/gzip-output-stream! filename)
            filename)]
    (tio/put-nippy! f ds)))

(defn ^:deprecated read-nippy
  [filename]
  (let [f (if (gzipped? filename)
            (tio/gzip-input-stream filename)
            filename)]
    (tio/get-nippy f)))

;; parallel concat

(defn parallel-concat
  [& dss]
  (let [cnt (max 10 (int (Math/sqrt (count dss))))        
        subdss (pmap (partial apply ds/concat) (partition-all cnt dss))]
    (apply ds/concat subdss)))

;; grouping

(defn grouped?
  "Is `dataset` represents grouped dataset (result of `group-by`)?"
  [ds]
  (:grouped? (meta ds)))

(defn unmark-group
  "Remove grouping tag"
  [ds]
  (vary-meta ds dissoc :grouped?))

(let [m (meta #'unmark-group)]
  (def ^{:doc (:doc m)
         :arglists (:arglists m)}
    as-regular-dataset unmark-group))

(defn mark-as-group
  "Add grouping tag"
  [ds]
  (vary-meta ds assoc
             :grouped? true
             :print-line-policy :single))

(defn process-group-data
  "Internal: The passed-in function is applied on all groups"
  ([ds f] (process-group-data ds f false))
  ([ds f parallel?]
   (ds/add-or-update-column ds :data ((if parallel? pmap map) f (ds :data)))))

