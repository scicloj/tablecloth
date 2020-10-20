(ns tablecloth.api.rows
  (:refer-clojure :exclude [shuffle rand-nth first last])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.datatype.argops :as aop]

            [tablecloth.api.utils :refer [iterable-sequence? rank column-names]]
            [tablecloth.api.dataset :refer [rows]]
            [tablecloth.api.columns :refer [add-or-replace-columns select-columns]]
            [tablecloth.api.group-by :refer [grouped? process-group-data]]))

(defn- find-indexes-from-seq
  "Find row indexes based on true/false values or indexes"
  [ds rows-selector]
  (if (number? (clojure.core/first rows-selector))
    rows-selector
    (->> rows-selector
         (take (ds/row-count ds))
         (aop/argfilter identity))))

(defn- find-indexes-from-fn
  "Filter rows"
  [ds rows-selector selected-keys]
  (->> (or selected-keys :all)
       (ds/select-columns ds)
       (ds/mapseq-reader)
       (aop/argfilter rows-selector)))

(defn- find-indexes
  ([ds rows-selector] (find-indexes ds rows-selector nil))
  ([ds rows-selector selected-keys]
   (cond
     (number? rows-selector) [(long rows-selector)]
     (iterable-sequence? rows-selector) (find-indexes-from-seq ds rows-selector)
     (fn? rows-selector) (find-indexes-from-fn ds rows-selector selected-keys))))

(defn- select-or-drop-rows
  "Select or drop rows."
  ([f ds rows-selector] (select-or-drop-rows f ds rows-selector nil))
  ([f ds rows-selector {:keys [select-keys pre result-type parallel?]}]
   (let [selected-keys (column-names ds select-keys)]
     (if (grouped? ds)
       (let [mapper (if parallel? pmap map)
             pre-ds (mapper #(add-or-replace-columns % pre) (ds :data))
             indices (mapper #(find-indexes % rows-selector selected-keys) pre-ds)]
         (if (= result-type :as-indexes)
           (mapper seq indices)
           (ds/add-or-update-column ds :data (mapper #(select-or-drop-rows f %1 %2) (ds :data) indices))))
       (let [indices (find-indexes (add-or-replace-columns ds pre) rows-selector selected-keys)]
         (if (= result-type :as-indexes)
           (seq indices)
           (f ds indices)))))))

(defn- select-or-drop-rows-docstring
  [op]
  (str op " rows using:

  - row id
  - seq of row ids
  - seq of true/false
  - fn with predicate"))

(def ^{:doc (select-or-drop-rows-docstring "Select")}
  select-rows (partial select-or-drop-rows ds/select-rows))

(def ^{:doc (select-or-drop-rows-docstring "Drop")}
  drop-rows (partial select-or-drop-rows ds/drop-rows))

;;

(defn head
  ([ds] (head ds 5))
  ([ds n]
   (if (grouped? ds)
     (process-group-data ds #(ds/head % n))
     (ds/head ds n))))

(defn tail
  ([ds] (tail ds 5))
  ([ds n]
   (if (grouped? ds)
     (process-group-data ds #(ds/tail % n))
     (ds/tail ds n))))

(defn- shuffle-seq
  [seq rng]
  (let [^java.util.Collection s seq
        ar (java.util.ArrayList. s)]
    (if rng
      (java.util.Collections/shuffle ar rng)
      (java.util.Collections/shuffle ar))
    ar))

(defn- shuffle-with-rng
  [ds rng]
  (ds/select-rows ds (shuffle-seq (range (ds/row-count ds)) rng)))

(defn shuffle
  ([ds] (shuffle ds nil))
  ([ds {:keys [seed]}]
   (let [rng (when seed (java.util.Random. seed))]
     (if (grouped? ds)
       (process-group-data ds #(shuffle-with-rng % rng))
       (shuffle-with-rng ds rng)))))

(defn- get-random-long
  ^long [^long cnt ^java.util.Random rng]
  (if rng
    (mod (.nextLong rng) cnt)
    (long (rand cnt))))

(defn- process-random
  [ds repeat? n rng]
  (let [cnt (ds/row-count ds)
        n (if (number? n) n cnt)
        idxs (if repeat?
               (repeatedly n #(get-random-long cnt rng))
               (take (min cnt n) (shuffle-seq (range cnt) rng)))]
    (ds/select-rows ds idxs)))

(defn random
  ([ds] (random ds (ds/row-count ds)))
  ([ds n] (random ds n nil))
  ([ds n {:keys [repeat? seed]
          :or {repeat? true}}]
   (let [rng (when seed (java.util.Random. seed))]
     (if (grouped? ds)
       (process-group-data ds #(process-random % repeat? n rng))
       (process-random ds repeat? n rng)))))

(defn- process-rand-nth
  [ds rng]
  (ds/select-rows ds (get-random-long (ds/row-count ds) rng)))

(defn rand-nth
  ([ds] (rand-nth ds nil))
  ([ds {:keys [seed]}]
   (let [rng (when seed (java.util.Random. seed))]
     (if (grouped? ds)
       (process-group-data ds #(process-rand-nth % rng))
       (process-rand-nth ds rng)))))

(defn first
  [ds]
  (if (grouped? ds)
    (process-group-data ds #(ds/select-rows % [0]))
    (ds/select-rows ds [0])))

(defn last
  [ds]
  (if (grouped? ds)
    (process-group-data ds last)
    (let [idx (dec (ds/row-count ds))]
      (ds/select-rows ds [idx]))))


(defn- rank-by-process
  [ds rank-fn rank-predicate]
  (->> ds
       (rank-fn)
       (map rank-predicate)
       (select-rows ds)))

(defn by-rank
  "Select rows using `rank` on a column, ties are resolved using `:dense` method.

  See [R docs](https://www.rdocumentation.org/packages/base/versions/3.6.1/topics/rank).
  Rank uses 0 based indexing.
  
  Possible `:ties` strategies: `:average`, `:first`, `:last`, `:random`, `:min`, `:max`, `:dense`.
  `:dense` is the same as in `data.table::frank` from R

  `:desc?` set to true (default) order descending before calculating rank"
  ([ds columns-selector rank-predicate] (by-rank ds columns-selector rank-predicate nil))
  ([ds columns-selector rank-predicate {:keys [desc? ties]
                                        :or {desc? true ties :dense}}]
   (let [col-names (column-names (if (grouped? ds)
                                   (clojure.core/first (ds :data))
                                   ds) columns-selector)
         rank-fn (if (= 1 (count col-names))
                   (let [n (clojure.core/first col-names)]
                     #(rank (% n) ties desc?))
                   #(rank (->> (-> (select-columns % col-names)
                                   (rows :as-seq))
                               (map vec)) ties desc?))]
     (if (grouped? ds)
       (process-group-data ds #(rank-by-process % rank-fn rank-predicate))
       (rank-by-process ds rank-fn rank-predicate)))))
