(ns tablecloth.api.join-separate
  (:refer-clojure :exclude [pmap])
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            [clojure.string :as str]
            [tech.v3.parallel.for :refer [pmap]]
            
            [tablecloth.api.utils :refer [iterable-sequence? column-names grouped? process-group-data]]
            [tablecloth.api.columns :refer [select-columns drop-columns add-column]]))

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
  `:separator` (default -)
  `:drop-columns?` - whether to drop source columns or not (default true)
  `:result-type`
     `:map` - packs data into map
     `:seq` - packs data into sequence
     `:string` - join strings with separator (default)
     or custom function which gets row as a vector
  `:missing-subst` - substitution for missing value
  "
  ([ds target-column columns-selector] (join-columns ds target-column columns-selector nil))
  ([ds target-column columns-selector {:keys [separator missing-subst drop-columns? result-type parallel?]
                                       :or {separator "-" drop-columns? true result-type :string}
                                       :as _conf}]
   
   (let [missing-subst-fn #(map (fn [v] (or v missing-subst)) %)
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

;;

(defn- infer-target-columns
  [col res]
  (let [colname (col/column-name col)]
    (map #(str colname "-" %) (range (count (first res))))))

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
