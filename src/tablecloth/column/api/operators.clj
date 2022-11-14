(ns tablecloth.column.api.operators
  (:require [tech.v3.datatype.export-symbols :as exporter]
            [tech.v3.datatype.argtypes :refer [arg-type]]
            [tablecloth.column.api :refer [column]]
            [tech.v3.datatype.functional :as fun]))

;; this is for fns taking [[x] [x y] [x y & args]]
(defn lift-ops-1 [fn-sym fn-meta]
  (let [fn (symbol "fn")
        let (symbol "let")
        defn (symbol "defn")
        args (:arglists fn-meta)
        docstring (:doc fn-meta)]
    `(~defn ~(symbol (name fn-sym)) 
      ~@(for [arg args
              :let [[explicit-args rest-arg-expr] (split-with (partial not= '&) arg)]]
          (list
           arg
           `(~let [original-result# ~(if (empty? rest-arg-expr)
                                      `(~fn-sym ~@explicit-args)
                                      `(apply ~fn-sym ~@explicit-args ~(second rest-arg-expr)))]
             (if (-> original-result# arg-type (= :reader))
               (column original-result#)
               original-result#)))))))



(def serialized-lift-fn-lookup
  {['+ '- '/ '> '>= '< '<=] lift-ops-1})


(lift-ops-1 (symbol "tech.v3.datatype.functional" (name '>))
            (meta (get fun-mappings '>)))


(defn deserialize-lift-fn-lookup []
  (reduce (fn [m [symlist liftfn]]
            (loop [syms symlist
                   result m]
              (if (empty? syms)
                result
                (recur (rest syms) (assoc result (first syms) liftfn)))))
          {}
          serialized-lift-fn-lookup))


(deserialize-lift-fn-lookup)



(defn do-lift []
  (let [lift-fn-lookup (deserialize-lift-fn-lookup)
        fun-mappings (ns-publics `tech.v3.datatype.functional)]
    (map (fn [[fnsym lift-fn]]
           (lift-fn (symbol "tech.v3.datatype.functional" (name fnsym))
                    (meta (get fun-mappings fnsym))))
         lift-fn-lookup)))


(do-lift)


;; (get-lifted (symbol "tech.v3.datatype.functional" "+") (meta (get fun-mappings '+)))


(clojure.pprint/pp)




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

