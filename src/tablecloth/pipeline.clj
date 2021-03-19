(ns tablecloth.pipeline
  "Linear pipeline operations."
  (:refer-clojure :exclude [group-by drop concat rand-nth first last shuffle])
  (:require [tablecloth.api :as api]))

(defmacro build-pipelined-function
  [f m]
  (let [args (map (comp vec rest) (:arglists m))
        doc-string (:doc m)
        ]
    `(defn ~(symbol (name f)) {:doc ~doc-string}
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

(defn pipeline
  [& ops]
  (apply comp (reverse ops)))

(declare process-param)

(defn- process-map
  [config params]
  (into {} (map (fn [[k v]]
                  [k (process-param config v)]) params)))

(defn- process-seq
  [config params]
  (mapv (fn [p] (process-param config p)) params))

(defn- resolve-keyword
  "Interpret keyword as a symbol and try to resolve it."
  [k]
  (-> (if-let [n (namespace k)] ;; namespaced?
        (let [sn (symbol n)
              n (str (get (ns-aliases *ns*) sn sn))] ;; try to find namespace in aliases
          (symbol n (name k))) ;; create proper symbol with fixed namespace
        (symbol (name k))) ;; no namespace case
      (resolve)))

(defn- maybe-var-get
  "If symbol can be resolved, return var, else return original keyword"
  [k]
  (if-let [rk (resolve-keyword k)]
    (var-get rk)
    k))

(defn- process-param
  "Recursively process parameters and try to resolve symbols for namespaced keywords.
  Special case for namespaced keyword is `ctx` namespace. It means that we should look up in `config` map."
  [config p]
  (cond
    (and (keyword? p) ;; 
         (let [n (namespace p)]
           (and n (or (= n "ctx")
                      (let [sn (symbol n)]
                        (find-ns (get (ns-aliases *ns*) sn sn))))))) (let [n (namespace p)]
                                                                       (if (= n "ctx")
                                                                         (config (keyword (name p)))
                                                                         (maybe-var-get p)))
    (map? p) (process-map config p)
    (sequential? p) (process-seq config p)
    :else p))

(defn ->pipeline
  "Create pipeline from declarative description."
  ([ops] (->pipeline {} ops))
  ([config ops]
   (apply pipeline (for [line ops]
                     (cond
                       ;; if it's a sequence, resolve function, process parameters and call it.
                       (sequential? line) (let [[op & params] line
                                                nparams (process-param config params)
                                                f (cond
                                                    (keyword? op) (maybe-var-get op)
                                                    (symbol? op) (var-get (resolve op))
                                                    :else op)]
                                            (apply f nparams))
                       (keyword? line) (maybe-var-get line)
                       :else line))))) ;; leave untouched otherwise
