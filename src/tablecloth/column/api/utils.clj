(ns tablecloth.column.api.utils
  (:import [java.io Writer])
  (:require [tech.v3.datatype.export-symbols :as exporter]
            [tech.v3.datatype.argtypes :refer [arg-type]]
            [tablecloth.column.api :refer [column]]
            [tech.v3.datatype.functional :as fun]
            [clojure.java.io :as io]))

(defn return-scalar-or-column [item]
  (let [item-type (arg-type item)]
    (if (= item-type :reader)
      (column item)
      item)))

(defn lift-op
  ([fn-sym fn-meta]
   (lift-op fn-sym fn-meta nil))
  ([fn-sym fn-meta {:keys [new-args]}]
   (let [defn (symbol "defn")
         let  (symbol "let")
         docstring (:doc fn-meta)
         original-args (:arglists fn-meta)
         sort-by-arg-count (fn [argslist]
                             (sort #(< (count %1) (count %2)) argslist))]
     (if new-args
      `(~defn ~(symbol (name fn-sym))
        ~(or docstring "")
        ~@(for [[new-arg new-arg-lookup original-arg]
                (map vector (sort-by-arg-count (keys new-args))
                            (sort-by-arg-count (vals new-args))
                            (sort-by-arg-count original-args))
                :let [filtered-original-arg (filter (partial not= '&) original-arg)]]
            (list
             (if new-arg new-arg original-arg)
            `(~let [original-result# (~fn-sym
                                      ~@(for [oldarg filtered-original-arg]
                                          (if (nil? (get new-arg-lookup oldarg))
                                            oldarg
                                            (get new-arg-lookup oldarg))))]
              (return-scalar-or-column original-result#)))))
      `(~defn ~(symbol (name fn-sym)) 
        ~(or docstring "") 
        ~@(for [arg original-args
                :let [[explicit-args rest-arg-expr] (split-with (partial not= '&) arg)]]
            (list
            arg
            `(~let [original-result# ~(if (empty? rest-arg-expr)
                                        `(~fn-sym ~@explicit-args)
                                        `(apply ~fn-sym ~@explicit-args ~(second rest-arg-expr)))]
              (return-scalar-or-column original-result#)))))))))


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

(defn get-lifted [lift-fn-lookup source-ns]
  (let [fun-mappings (ns-publics source-ns)]
    (map (fn [[fnsym lift-fn]]
           (lift-fn (symbol (name source-ns) (name fnsym))
                    (meta (get fun-mappings fnsym))))
        lift-fn-lookup)))

(defn get-ns-header [target-ns source-ns ns-exclusions]
  (let [ns (symbol "ns")]
    `(~ns ~target-ns
      (:require [~source-ns]
                [tablecloth.column.api.utils])
      (:refer-clojure :exclude ~ns-exclusions))))

(defn do-lift [lift-plan target-ns source-ns ns-exclusions filename]
  (with-open [writer (io/writer filename :append false)]
    (write-pp writer (get-ns-header target-ns source-ns ns-exclusions))
    (write-empty-ln! writer)
    (doseq [f (get-lifted lift-plan source-ns)]
      (-> writer
           (write-pp f)
           (write-empty-ln!)))))
