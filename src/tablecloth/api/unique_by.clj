(ns tablecloth.api.unique-by
  (:require [tech.v3.dataset :as ds]
            [tech.v3.datatype.protocols :as dtype-proto]
            [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.bitmap :as bitmap]
            
            [tablecloth.api.utils :refer [iterable-sequence? column-names grouped? process-group-data]]
            [tablecloth.api.dataset :refer [dataset empty-ds?]]
            [tablecloth.api.columns :refer [select-columns]]
            [tablecloth.api.group-by :refer [ungroup]]))

(defn- strategy-first [_ idxs] (clojure.core/first idxs))
(defn- strategy-last [_ idxs] (clojure.core/last idxs))
(defn- strategy-random [_ idxs] (clojure.core/rand-nth idxs))

(def ^:private strategies
  {:first strategy-first
   :last strategy-last
   :random strategy-random})

(defn- remove-missing-from-column
  "The same as remove rows"
  [col]
  (let [cnt (dtype/ecount col)
        m (col/missing col)]
    (if (and (pos? cnt)
             (seq m))
      (col/select col (-> cnt
                          (range)
                          (bitmap/->bitmap)
                          (dtype-proto/set-and-not m)))
      col)))

(defn strategy-fold
  ([ds columns-selector] (strategy-fold ds columns-selector nil))
  ([ds columns-selector fold-fn] (strategy-fold ds columns-selector fold-fn nil))
  ([ds columns-selector fold-fn ungroup-options]
   (let [[group-by-selector target-names] (if (fn? columns-selector)
                                            [columns-selector (ds/column-names ds)]
                                            (let [group-by-names (column-names ds columns-selector)]
                                              [group-by-names (->> group-by-names
                                                                   (set)
                                                                   (partial contains?)
                                                                   (complement)
                                                                   (column-names ds))]))
         fold-fn (or fold-fn vec)]
     (-> (tablecloth.api.group-by/group-by ds group-by-selector)
         (process-group-data (fn [ds]
                               (as-> ds ds
                                 (select-columns ds target-names)
                                 (dataset [(zipmap target-names
                                                   (map (comp fold-fn remove-missing-from-column) (ds/columns ds)))]))))
         (ungroup ungroup-options)))))

(defn- unique-by-fn
  [strategy columns-selector selected-keys options]
  (if (fn? strategy)

    (fn [ds] (strategy-fold ds columns-selector strategy options))
    
    (let [local-options {:keep-fn (get strategies strategy strategy-first)}]
      (cond
        (iterable-sequence? columns-selector) (let [local-options (assoc local-options :column-name-seq columns-selector)]
                                                (fn [ds]
                                                  (if (= (count columns-selector) 1)
                                                    (ds/unique-by-column ds local-options (clojure.core/first columns-selector))
                                                    (ds/unique-by ds local-options #(select-keys % columns-selector)))))
        (fn? columns-selector) (let [local-options (if selected-keys
                                                     (assoc local-options :column-name-seq selected-keys)
                                                     local-options)]
                                 (fn [ds] (ds/unique-by ds local-options columns-selector)))
        :else (fn [ds] (ds/unique-by-column ds local-options columns-selector ))))))

(defn- maybe-skip-unique
  [ufn ds]
  (if (= 1 (ds/row-count ds)) ds (ufn ds)))

(defn- maybe-empty
  [ufn ds]
  (if (empty-ds? ds) ds (ufn ds)))

(defn unique-by
  ([ds] (unique-by ds (ds/column-names ds)))
  ([ds columns-selector] (unique-by ds columns-selector nil))
  ([ds columns-selector {:keys [strategy select-keys parallel?]
                         :or {strategy :first}
                         :as options}]
   (let [selected-keys (column-names ds select-keys)
         ufn (unique-by-fn strategy columns-selector selected-keys options)
         ufn (partial maybe-empty (if (fn? strategy) ufn (partial maybe-skip-unique ufn)))]

     (if (grouped? ds)
       (process-group-data ds ufn parallel?)
       (ufn ds)))))

