(ns tablecloth.pipeline
  "Linear pipeline operations."
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])
  (:require [tablecloth.api :as api]
            [scicloj.metamorph.core :as mm]))

(defmacro build-pipelined-function
  [f m]
  (let [args (map (comp vec rest) (:arglists m))]
    `(defn ~(symbol (name f))
       ~@(for [arg args
               :let [narg (mapv #(if (map? %) 'options %) arg)
                     [a & r] (split-with (partial not= '&) narg)]]
           (list narg `(fn [ds#]
                         (let [ctx# (if (api/dataset? ds#)
                                      {:metamorph/data ds#} ds#)]
                           (assoc ctx# :metamorph/data (apply ~f (ctx# :metamorph/data) ~@a ~(rest r))))))))))

(def ^:private excludes '#{dataset write-csv! let-dataset row-count column-count})

(defmacro ^:private process-all-api-symbols
  []
  (let [ps (ns-publics 'tablecloth.api)]
    `(do ~@(for [[f v] ps
                 :when (not (excludes f))
                 :let [m (meta v)
                       f (symbol "tablecloth.api" (name f))]]
             `(build-pipelined-function ~f ~m)))))

(process-all-api-symbols)

(def pipeline mm/pipeline)
(def ->pipeline mm/->pipeline)

;; TODO: remove before releasing
;; example


(def DS (api/dataset {:V1 (take 9 (cycle [1 2]))
                      :V2 (range 1 10)
                      :V3 (take 9 (cycle [0.5 1.0 1.5]))
                      :V4 (take 9 (cycle ["A" "B" "C"]))}))

(select-columns :type/numerical)
;; => #function[tablecloth.pipeline/select-columns/fn--43815]

((select-columns :type/numerical) DS)
;; => #:metamorph{:data _unnamed [9 3]:
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

(let [p (mm/pipeline (group-by :V1)
                     (fold-by :V4)
                     (ungroup))]
  (p DS))
;; => #:metamorph{:data _unnamed [6 5]:
;;    | :V4 |   :V1 |   :V2 |       :V3 | :metamorph/id |
;;    |-----|-------|-------|-----------|---------------|
;;    |   B |   [1] |   [5] |     [1.0] |           [0] |
;;    |   C | [1 1] | [3 9] | [1.5 1.5] |         [0 0] |
;;    |   A | [1 1] | [1 7] | [0.5 0.5] |         [0 0] |
;;    |   B | [2 2] | [2 8] | [1.0 1.0] |         [0 0] |
;;    |   C |   [2] |   [6] |     [1.5] |           [0] |
;;    |   A |   [2] |   [4] |     [0.5] |           [0] |
;;    }

(def pipeline-declaration [[:group-by :V1]
                           [:unique-by ::unique-by-operation {:strategy :ctx/strategy}]
                           [:ungroup {:add-group-as-column :from-V1}]])

(def unique-by-operation (fn [m] (mod (:V2 m) 3)))

(def pipeline-1 (mm/->pipeline {:strategy vec} pipeline-declaration))
(def pipeline-2 (mm/->pipeline {:strategy set} pipeline-declaration))


(pipeline-1 DS)
;; => #:metamorph{:data _unnamed [6 6]:
;;    | :from-V1 |   :V1 |   :V2 |       :V3 |       :V4 | :metamorph/id |
;;    |----------|-------|-------|-----------|-----------|---------------|
;;    |        1 | [1 1] | [3 9] | [1.5 1.5] | ["C" "C"] |         [0 0] |
;;    |        1 | [1 1] | [1 7] | [0.5 0.5] | ["A" "A"] |         [0 0] |
;;    |        1 |   [1] |   [5] |     [1.0] |     ["B"] |           [0] |
;;    |        2 |   [2] |   [6] |     [1.5] |     ["C"] |           [0] |
;;    |        2 |   [2] |   [4] |     [0.5] |     ["A"] |           [0] |
;;    |        2 | [2 2] | [2 8] | [1.0 1.0] | ["B" "B"] |         [0 0] |
;;    }

(pipeline-2 {:metamorph/data DS})
;; => #:metamorph{:data _unnamed [6 5]:
;;    | :from-V1 |  :V1 |    :V2 |    :V3 |    :V4 |
;;    |----------|------|--------|--------|--------|
;;    |        1 | #{1} | #{3 9} | #{1.5} | #{"C"} |
;;    |        1 | #{1} | #{7 1} | #{0.5} | #{"A"} |
;;    |        1 | #{1} |   #{5} | #{1.0} | #{"B"} |
;;    |        2 | #{2} |   #{6} | #{1.5} | #{"C"} |
;;    |        2 | #{2} |   #{4} | #{0.5} | #{"A"} |
;;    |        2 | #{2} | #{2 8} | #{1.0} | #{"B"} |
;;    }

;; custom pipeline operation

(defn transformation-returning-context []
  (fn [ctx]
    (let [id (ctx :metamorph/id)]
      (if-let [my-data (ctx id)]
        (do
          (println "My stored data is: " my-data)
          (update ctx id inc))
        (assoc ctx id 0)))))

(def do-something
  (mm/pipeline (transformation-returning-context)
               (transformation-returning-context)))

(take 3 (rest (iterate do-something {})))
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
