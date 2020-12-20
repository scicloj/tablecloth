(ns tablecloth.pipeline
  "Linear pipeline operations."
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])
  (:require [tablecloth.api]))

(defmacro build-pipelined-function
  [f m]
  (let [args (map (comp vec rest) (:arglists m))]
    `(defn ~(symbol (name f))
       ~@(for [arg args
               :let [narg (mapv #(if (map? %) 'options %) arg)
                     [a & r] (split-with (partial not= '&) narg)]]
           (list narg `(fn [ds#] (apply ~f ds# ~@a ~(rest r))))))))

(def ^:private excludes #{'dataset 'write-csv!})

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
  (apply comp (reverse ops)))

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
