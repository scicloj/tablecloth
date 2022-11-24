(ns tablecloth.column.api.operators
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
  ([fn-sym fn-meta {:keys [new-args new-args-lookup]}]
   (let [defn (symbol "defn")
         let  (symbol "let")
         docstring (:doc fn-meta)
         original-args (:arglists fn-meta)
         sort-by-arg-count (fn [argslist]
                             (sort #(< (count %1) (count %2)) argslist))]
     (if new-args
      `(~defn ~(symbol (name fn-sym))
        ~(or docstring "")
        ~@(for [[new-arg original-arg] (zipmap (sort-by-arg-count new-args)
                                                (sort-by-arg-count original-args))
                :let [filtered-original-arg (filter (partial not= '&) original-arg)]]
            (list
            (if new-arg new-arg original-arg)
            `(~let [original-result# (~fn-sym
                                      ~@(if (nil? new-args-lookup)
                                          filtered-original-arg
                                          (for [oldarg filtered-original-arg]
                                            (get new-args-lookup oldarg))))]
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

(meta (get fun-mappings 'percentiles))

;; (def fun-mappings (ns-publics 'tech.v3.datatype.functional))

(lift-op (symbol "tech.v3.datatype.functional" "+")
         (meta (get fun-mappings '+)))


;; (lift-op (symbol "tech.v3.datatype.functional" "percentiles")
;;                 (meta (get fun-mappings 'percentiles))
;;                 {:new-args '([data percentiles] [data percentiles & options])
;;                  :new-args-lookup {'percentages 'percentiles, 'data 'data, 'options 'options}})

;; (lift-op (symbol "tech.v3.datatype.functional/+" )
;;                 (meta (get fun-mappings '+))
;;                 {:new-args '([a] [b a] [b a & args])
;;                  :new-args-lookup {'x 'a, 'y 'b, 'args 'args}})


;; (clojure.pprint/pp)

(def serialized-lift-fn-lookup
  {['+
    '-
    '/
    '>
    '>=
    '<
    '<=
    ] lift-op
   ['percentiles] (fn [fn-sym fn-meta]
                    (lift-op
                     fn-sym fn-meta
                     {:new-args '([col percentiles] [col percentiles options])
                      :new-args-lookup {'data 'col,
                                        'percentages 'percentiles,
                                        'options 'options}}))})


(defn deserialize-lift-fn-lookup []
  (reduce (fn [m [symlist liftfn]]
            (loop [syms symlist
                   result m]
              (if (empty? syms)
                result
                (recur (rest syms) (assoc result (first syms) liftfn)))))
          {}
          serialized-lift-fn-lookup))



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

(defn get-lifted []
(let [lift-fn-lookup (deserialize-lift-fn-lookup)
      fun-mappings (ns-publics `tech.v3.datatype.functional)]
  (map (fn [[fnsym lift-fn]]
         (lift-fn (symbol "tech.v3.datatype.functional" (name fnsym))
                  (meta (get fun-mappings fnsym))))
       lift-fn-lookup)))

(defn get-ns-header [target-ns source-ns]
  (let [ns (symbol "ns")]
    `(~ns ~target-ns
      (:require [~source-ns])
      (:refer-clojure :exclude ~['+ '- '/ '> '>= '< '<=]))))

(defn do-lift [target-ns source-ns filename]
  (with-open [writer (io/writer filename :append false)]
    (write-pp writer (get-ns-header target-ns source-ns))
    (write-empty-ln! writer)
    (doseq [f (get-lifted)]
      (-> writer
           (write-pp f)
           (write-empty-ln!)))))

(comment
  (do-lift 'tablecloth.column.api.lifted-operators
           'tech.v3.datatype.functional
           "src/tablecloth/column/api/lifted_operators.clj")
  ,)


(comment 
  ;; some early attempts at lifting with macro
  (defmacro lift-operator [f m]
    `(defn ~(symbol (name f)) {:doc ~(:doc m) :orig (symbol (var ~f))} [a#]
      (let [result# (apply ~f [a#])]
        (if (= (tech.v3.datatype/datatype result#) :buffer)
          (tablecloth.column.api/column result#)
          result#))))

  (defmacro lift-operator-2 [f m]
    `(defn ~(symbol (name f)) {:doc ~(:doc m) :origin (symbol (var ~f))}
      ~@(for [arg args
              :let [narg (mapv #(if (map? %) 'options %) arg)
                    [a & r] (split-with (partial not= '&) narg)]]
          (list narg `(fn [ds#])))))

  ;; example of pprinting a function as a method for code gen
  (let [fun-mappings (ns-publics 'tech.v3.datatype.functional)
        fn-name (name 'sum)
        op (get fun-mappings (symbol fn-name))
        opsym (symbol (format "dfn/%s" fn-name))
        op-meta (meta op)
        op-sym (vary-meta (symbol fn-name) merge op-meta)
        defn (symbol "defn")
        let  (symbol "let")]
    (clojure.pprint/pprint
     `(~defn ~op-sym ~(:doc op-meta)
       [~'col]
       (~let [original-result# (~opsym ~'col)]
        original-result#))))


  ;; building a map of the functions in tech.v3.datatype.functional
  ;; by arg patterns.
  (def fun-mappings (ns-publics 'tech.v3.datatype.functional))

  (def fns-by-args (reduce (fn [m [fnsym fnvar]]
                            (let [args (-> fnvar meta :arglists str)]
                              (assoc m args (merge (if (get m args)
                                                    (merge (get m args) fnsym)
                                                    (merge [] fnsym))))))
                          {}
                          fun-mappings))

  fns-by-args


  (sort (fn [[_ fns1] [_ fns2]]
          (compare (count fns1) (count fns2)))
        fns-by-args)

  )

