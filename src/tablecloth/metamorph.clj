(ns tablecloth.metamorph
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])
  (:require [tablecloth.api :as api])

  )

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

(def ^:private excludes '#{dataset write-csv! write! read-nippy write-nippy! let-dataset row-count column-count
                           set-dataset-name dataset-name column has-column?
                           dataset->str column-names dataset? empty-ds? shape
                           info columns rows print-dataset grouped? process-group-data groups->seq groups->map
                           ->array split})

(defmacro ^:private process-all-api-symbols
  []
  (let [ps (ns-publics 'tablecloth.api)]
    `(do ~@(for [[f v] ps
                 :when (not (excludes f))
                 :let [m (meta v)
                       f (symbol "tablecloth.api" (name f))]]
             `(build-pipelined-function ~f ~m)))))

(process-all-api-symbols)


