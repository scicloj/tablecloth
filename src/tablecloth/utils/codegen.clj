(ns tablecloth.utils.codegen
  (:import [java.io Writer])
  (:require [tech.v3.datatype.export-symbols :as exporter]
            [clojure.java.io :as io]))

(defn- writeln!
  ^Writer [^Writer writer strdata & strdatas]
  (.append writer (str strdata))
  (doseq [data strdatas]
    (when data
      (.append writer (str data))))
  (.append writer "\n")
  writer)

(defn- write-empty-ln! ^Writer [^Writer writer]
  (writeln! writer "")
  writer)

(defn- write-pp ^Writer [^Writer writer item]
  (clojure.pprint/pprint item writer)
  writer)

(defn deserialize-lift-fn-lookup [serialized-lift-fn-lookup]
  (reduce (fn [m [symlist liftfn]]
            (loop [syms symlist
                   result m]
              (if (empty? syms)
                result
                (recur (rest syms) (assoc result (first syms) liftfn)))))
          {}
          serialized-lift-fn-lookup))

(defn get-lifted [lift-fn-lookup source-ns]
  (let [fun-mappings (ns-publics source-ns)]
    (map (fn [[fnsym lift-fn]]
           (lift-fn (symbol (name source-ns) (name fnsym))))
         (deserialize-lift-fn-lookup lift-fn-lookup))))

(defn namespace-to-path [ns-str]
  (-> ns-str
       (name)
       (clojure.string/replace "." "/")
       (clojure.string/replace "-" "_")
       (->> (str "./src/"))
       (str ".clj")))

(defn build-ns-header
  "Generates a namespace header with the specified target-ns and
  source-ns, along with optional additional dependencies and
  exclusions. If exclusions are provided, they will be used to exclude
  the specified symbol(s) from the :refer-clojure directive."
  ([target-ns source-ns]
   (build-ns-header target-ns source-ns nil nil))
  ([target-ns source-ns additional-deps]
   (build-ns-header target-ns source-ns additional-deps nil))
  ([target-ns source-ns additional-deps exclusions]
   (let [ns (symbol "ns")]
     `(~ns ~target-ns
       (:require [~source-ns]
                 ~@(for [dep additional-deps]
                     [dep]))
       ~@(when exclusions `((:refer-clojure :exclude ~exclusions)))))))

(defn do-lift
  "Writes the lifted functions to target namespace.

   Example:
     {:target-ns 'tablecloth.api.operators
     :source-ns 'tablecloth.column.api.operators
     :lift-fn-lookup {['+ '- '*] lift-fn}
     :deps ['tablecloth.api.lift_operators]
     :exclusions
     '[* + -]}"
[{:keys [target-ns source-ns lift-fn-lookup deps exclusions]}]
  (with-open [writer (io/writer (namespace-to-path target-ns) :append false)]
    (write-pp writer (build-ns-header target-ns source-ns deps exclusions))
    (write-empty-ln! writer)
    (doseq [f (get-lifted lift-fn-lookup source-ns)]
      (-> writer
           (write-pp f)
           (write-empty-ln!)))))
