(ns tablecloth.pipeline
  "Linear pipeline operations."
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])
  (:require [tablecloth.api :as api]))

(defmacro build-pipelined-function
  [f m]
  (let [args (map (comp vec rest) (:arglists m))]
    `(defn ~(symbol (name f))
       ~@(for [arg args
               :let [narg (mapv #(if (map? %) 'options %) arg)
                     [a & r] (split-with (partial not= '&) narg)]]
           (list narg `(fn [ds#]
                         (let [ctx# (if (api/dataset? ds#)
                                      {:dataset ds#} ds#)]
                           (assoc ctx# :dataset (apply ~f (ctx# :dataset) ~@a ~(rest r))))))))))

(def ^:private excludes #{'dataset 'write-csv! 'let-dataset})

(defmacro process-all-api-symbols
  []
  (let [ps (ns-publics 'tablecloth.api)]
    `(do ~@(for [[f v] ps
                 :when (not (excludes f))
                 :let [m (meta v)
                       f (symbol "tablecloth.api" (name f))]]
             `(build-pipelined-function ~f ~m)))))

(process-all-api-symbols)

(defn pipeline
  [& ops]
  (let [ops-with-uuid (map #(vector (java.util.UUID/randomUUID) %) ops)]
    (fn [ctx]
      (let [ctx (if (api/dataset? ctx)
                  {:dataset ctx} ctx)]
        (reduce (fn [curr-ctx [uuid op]]
                  (-> curr-ctx
                      (assoc :uuid uuid)
                      (op)
                      (dissoc :uuid))) ctx ops-with-uuid)))))

(declare process-param)

(defn- process-map
  [ctx params]
  (into {} (map (fn [[k v]]
                  [k (process-param ctx v)]) params)))

(defn- process-seq
  [ctx params]
  (mapv (fn [p] (process-param ctx p)) params))

(defn- process-param
  [ctx p]
  (cond
    (and (keyword? p)
         (namespace p)
         (not (#{"type" "!type"} (namespace p)))) (let [n (namespace p)]
                                                    (if (= n "ctx")
                                                      (ctx (keyword (name p)))
                                                      (var-get (resolve (symbol p)))))
    (map? p) (process-map ctx p)
    (sequential? p) (process-seq ctx p)
    :else p))

(defn ->pipeline
  ([ops] (->pipeline {} ops))
  ([ctx ops]
   (apply pipeline (for [[op & params] ops
                         :let [nparams (process-param ctx params)
                               v (cond
                                   (and (keyword? op)
                                        (not (namespace op))) (var-get (resolve (symbol "tablecloth.pipeline" (name op))))
                                   (keyword? op) (var-get (resolve (symbol op)))
                                   (symbol? op) (resolve op)
                                   :else op)]]
                     (apply v nparams)))))


;; TODO: remove before releasing
;; example


(def DS (api/dataset {:V1 (take 9 (cycle [1 2]))
                      :V2 (range 1 10)
                      :V3 (take 9 (cycle [0.5 1.0 1.5]))
                      :V4 (take 9 (cycle ["A" "B" "C"]))}))

(select-columns :type/numerical)
;; => #function[tablecloth.pipeline/select-columns/fn--43815]

((select-columns :type/numerical) DS)
;; => {:dataset _unnamed [9 3]:
;;    | :V1 | :V2 | :V3 |
;;    |-----|-----|-----|
;;    |   1 |   1 | 0.5 |
;;    |   2 |   2 | 1.0 |
;;    |   1 |   3 | 1.5 |
;;    |   2 |   4 | 0.5 |
;;    |   1 |   5 | 1.0 |
;;    |   2 |   6 | 1.5 |
;;    |   1 |   7 | 0.5 |
;;    |   2 |   8 | 1.0 |
;;    |   1 |   9 | 1.5 |
;;    }

(let [p (pipeline (group-by :V1)
                  (fold-by :V4)
                  (ungroup))]
  (p DS))
;; => {:dataset _unnamed [6 4]:
;;    | :V4 |   :V1 |   :V2 |       :V3 |
;;    |-----|-------|-------|-----------|
;;    |   B |   [1] |   [5] |     [1.0] |
;;    |   C | [1 1] | [3 9] | [1.5 1.5] |
;;    |   A | [1 1] | [1 7] | [0.5 0.5] |
;;    |   B | [2 2] | [2 8] | [1.0 1.0] |
;;    |   C |   [2] |   [6] |     [1.5] |
;;    |   A |   [2] |   [4] |     [0.5] |
;;    }

(def pipeline-declaration [[:group-by :V1]
                           [:unique-by ::unique-by-operation {:strategy :ctx/strategy}]
                           [:ungroup {:add-group-as-column :from-V1}]])

(def unique-by-operation (fn [m] (mod (:V2 m) 3)))

(def pipeline-1 (->pipeline {:strategy vec} pipeline-declaration))
(def pipeline-2 (->pipeline {:strategy set} pipeline-declaration))


(pipeline-1 DS)

(pipeline-2 {:dataset DS})


;; custom pipeline operation

(defn transformation-returning-context []
  (fn [{:keys [uuid] :as ctx}]
    (if-let [my-data (ctx uuid)]
      (do
        (println "My stored data is: " my-data)
        (update ctx uuid inc))
      (assoc ctx uuid 0))))

(def do-something
  (pipeline (transformation-returning-context)
            (transformation-returning-context)))

(take 3 (rest (iterate do-something (api/dataset))))
;; => ({:dataset _unnamed [0 0],
;;      #uuid "b3d63d87-096b-4c17-8290-9f4fdd68d6a6" 0,
;;      #uuid "95a154e8-cdf0-4f7c-80e7-d0d3e00e1463" 0}My stored data is:  0
;;    My stored data is:  0
;;     {:dataset _unnamed [0 0],
;;      #uuid "b3d63d87-096b-4c17-8290-9f4fdd68d6a6" 1,
;;      #uuid "95a154e8-cdf0-4f7c-80e7-d0d3e00e1463" 1}My stored data is:  1
;;    My stored data is:  1
;;     {:dataset _unnamed [0 0],
;;      #uuid "b3d63d87-096b-4c17-8290-9f4fdd68d6a6" 2,
;;      #uuid "95a154e8-cdf0-4f7c-80e7-d0d3e00e1463" 2})
