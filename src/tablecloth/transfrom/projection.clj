(ns tablecloth.transfrom.projection
  (:require [tablecloth.api.utils :refer [column-names]]
            [tablecloth.api.dataset :refer [rows dataset columns]]
            [tablecloth.api.columns :refer [select-columns drop-columns add-or-replace-columns]]
            [tablecloth.api.group-by :refer [grouped? ungroup process-group-data]]            
            [fastmath.kernel :as k])
  (:import [smile.projection PCA ProbabilisticPCA KPCA GHA RandomProjection Projection]
           [smile.math.kernel MercerKernel]))

(set! *warn-on-reflection* true)

(defn- pca
  ([rows target-dims] (pca rows target-dims false))
  ([rows ^long target-dims cor?]
   (let [^PCA model (if cor? (PCA/cor rows) (PCA/fit rows))]
     (.setProjection model target-dims)
     model)))

(defn- pca-prob
  [rows target-dims]
  (ProbabilisticPCA/fit rows target-dims))

(defn- build-smile-kernel
  [kernel kernel-params]
  (cond
    (instance? MercerKernel kernel) kernel
    (fn? kernel) (k/smile-mercer kernel)
    :else (k/smile-mercer (apply k/kernel kernel kernel-params))))

(defn- kpca
  [rows target-dims kernel kernel-params threshold]
  (KPCA/fit rows (build-smile-kernel kernel kernel-params) target-dims threshold))

(defn- gha
  [rows target-dims learning-rate decay]
  (let [^GHA model (GHA. (count (first rows)) target-dims learning-rate)]
    (doseq [row rows]
      (.setLearningRate model (* decay (.getLearningRate model)))
      (.update model row))
    model))

(defn- random
  [rows target-dims]
  (let [cnt (count (first rows))]
    (RandomProjection/of cnt target-dims)))

(defn- build-model
  [rows algorithm target-dims {:keys [kernel kernel-params
                                      threshold learning-rate decay]
                               :or {kernel (k/kernel :gaussian)
                                    threshold 0.0001
                                    learning-rate 0.0001
                                    decay 0.995}}]
  (case algorithm
    :pca-cov (pca rows target-dims)
    :pca-cor (pca rows target-dims true)
    :pca-prob (pca-prob rows target-dims)
    :kpca (kpca rows target-dims kernel kernel-params threshold)
    :gha (gha rows target-dims learning-rate decay)
    :random (random rows target-dims)
    (pca rows target-dims)))

(defn- rows->array
  [ds names]
  (-> ds
      (select-columns names)
      (rows :as-double-arrays)))

(defn- array->ds
  [arr target-columns]
  (->> arr
       (map (partial zipmap target-columns))
       (dataset)))

(defn process-reduction
  [ds algorithm target-dims cnames target-columns {:keys [drop-columns? model]
                                                   :or {drop-columns? true}
                                                   :as opts}]
  (let [rows (rows->array ds cnames)
        ^Projection model (or model (build-model rows algorithm target-dims opts))
        ds-res (array->ds (.project model #^"[[D" rows) target-columns)]
    (-> (if drop-columns? (drop-columns ds cnames) ds)
        (add-or-replace-columns (columns ds-res :as-map)))))


(defn reduce-dimensions
  ([ds target-dims] (reduce-dimensions ds :pca target-dims))
  ([ds algorithm target-dims] (reduce-dimensions ds :type/numerical algorithm target-dims))
  ([ds columns-selector algorithm target-dims] (reduce-dimensions ds columns-selector algorithm target-dims {}))
  ([ds columns-selector algorithm target-dims {:keys [prefix parallel? model common-model?]
                                               :or {common-model? true
                                                    parallel? false}
                                               :as opts}]
   (let [cnames (column-names ds columns-selector)
         target-columns (map #(str (or prefix
                                       (name algorithm)) "-" %) (range))]
     (if (grouped? ds)
       (let [opts (assoc opts :model (or model (if common-model?
                                                 (build-model (-> (ungroup ds)
                                                                  (rows->array cnames)) algorithm target-dims opts)
                                                 model)))]
         (process-group-data ds (fn [ds]
                                  (process-reduction ds algorithm target-dims cnames target-columns opts)) parallel?))
       (process-reduction ds algorithm target-dims cnames target-columns opts)))))


#_(-> "data/iris.csv"
      (dataset)
      (reduce-dimensions :type/numerical :kpca 2 {:drop-columns? false}))

;; => data/iris.csv [150 7]:
;;    | Sepal.Length | Sepal.Width | Petal.Length | Petal.Width | Species |      gha-0 |      gha-1 |
;;    |--------------|-------------|--------------|-------------|---------|------------|------------|
;;    |          5.1 |         3.5 |          1.4 |         0.2 |  setosa | 1.30138692 | 1.15508583 |
;;    |          4.9 |         3.0 |          1.4 |         0.2 |  setosa | 1.23701475 | 1.09056739 |
;;    |          4.7 |         3.2 |          1.3 |         0.2 |  setosa | 1.20036878 | 1.06415293 |
;;    |          4.6 |         3.1 |          1.5 |         0.2 |  setosa | 1.20672764 | 1.06935277 |
;;    |          5.0 |         3.6 |          1.4 |         0.2 |  setosa | 1.29042605 | 1.14849335 |
;;    |          5.4 |         3.9 |          1.7 |         0.4 |  setosa | 1.43560210 | 1.26887314 |
;;    |          4.6 |         3.4 |          1.4 |         0.3 |  setosa | 1.21755121 | 1.07961725 |
;;    |          5.0 |         3.4 |          1.5 |         0.2 |  setosa | 1.29302157 | 1.14705616 |
;;    |          4.4 |         2.9 |          1.4 |         0.2 |  setosa | 1.14582429 | 1.01360489 |
;;    |          4.9 |         3.1 |          1.5 |         0.1 |  setosa | 1.25044857 | 1.10963633 |
;;    |          5.4 |         3.7 |          1.5 |         0.2 |  setosa | 1.37931550 | 1.22475954 |
;;    |          4.8 |         3.4 |          1.6 |         0.2 |  setosa | 1.27369534 | 1.13243401 |
;;    |          4.8 |         3.0 |          1.4 |         0.1 |  setosa | 1.21263479 | 1.07514763 |
;;    |          4.3 |         3.0 |          1.1 |         0.1 |  setosa | 1.08333603 | 0.96582994 |
;;    |          5.8 |         4.0 |          1.2 |         0.2 |  setosa | 1.42143678 | 1.26277438 |
;;    |          5.7 |         4.4 |          1.5 |         0.4 |  setosa | 1.48755107 | 1.32085837 |
;;    |          5.4 |         3.9 |          1.3 |         0.4 |  setosa | 1.37670524 | 1.21595508 |
;;    |          5.1 |         3.5 |          1.4 |         0.3 |  setosa | 1.30874167 | 1.15657975 |
;;    |          5.7 |         3.8 |          1.7 |         0.3 |  setosa | 1.47325868 | 1.30182335 |
;;    |          5.1 |         3.8 |          1.5 |         0.3 |  setosa | 1.34165892 | 1.19180933 |
;;    |          5.4 |         3.4 |          1.7 |         0.2 |  setosa | 1.39057089 | 1.22921851 |
;;    |          5.1 |         3.7 |          1.5 |         0.4 |  setosa | 1.34294931 | 1.18596991 |
;;    |          4.6 |         3.6 |          1.0 |         0.2 |  setosa | 1.16342830 | 1.03987197 |
;;    |          5.1 |         3.3 |          1.7 |         0.5 |  setosa | 1.35549511 | 1.18458945 |
;;    |          4.8 |         3.4 |          1.9 |         0.2 |  setosa | 1.31786798 | 1.17212256 |
