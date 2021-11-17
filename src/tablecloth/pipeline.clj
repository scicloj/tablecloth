(ns tablecloth.pipeline
  "Linear pipeline operations."
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])
  (:require [tablecloth.api :as api]))

(defmacro build-pipelined-function
  [f m]
  (let [args (map (comp vec rest) (:arglists m))]
    `(defn ~(symbol (name f)) {:doc ~(:doc m) :orig (symbol (var ~f))}
       ~@(for [arg args
               :let [narg (mapv #(if (map? %) 'options %) arg)
                     [a & r] (split-with (partial not= '&) narg)]]
           (list narg `(fn [ds#]
                         (let [ctx# (if (api/dataset? ds#)
                                      {:metamorph/data ds#} ds#)]
                           (assoc ctx# :metamorph/data (apply ~f (ctx# :metamorph/data) ~@a ~(rest r))))))))))

(def ^:private excludes '#{dataset write-csv! let-dataset without-grouping->})

(defmacro process-all-api-symbols
  []
  (let [ps (ns-publics 'tablecloth.api)]
    `(do ~@(for [[f v] ps
                 :when (not (excludes f))
                 :let [m (meta v)
                       f (symbol "tablecloth.api" (name f))]]
             `(build-pipelined-function ~f ~m)))))

(process-all-api-symbols)

