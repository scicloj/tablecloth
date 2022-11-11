(ns tablecloth.column.api.operators
  (:require [tech.v3.datatype.export-symbols :as exporter]
            [tech.v3.datatype.argtypes :refer [arg-type]]
            [tablecloth.column.api :refer [column]]
            [tech.v3.datatype.functional :as fun]))

(defn get-lifted [fn-sym fn-meta]
  (let [fn (symbol "fn")
        let (symbol "let")
        defn (symbol "defn")
        args (:arglists fn-meta)
        docstring (:doc fn-meta)]
    `(~defn ~(symbol (name fn-sym)) 
      ~@(for [arg args
              :let [a (filter (partial not= '&) arg)]]
          (list
           arg
           `(~let [original-result# ~(if (> (count a) 2)
                                      `(apply ~fn-sym ~@a)
                                      `(~fn-sym ~@a))]
             (if (-> original-result# arg-type (= :reader))
               (column original-result#)
               original-result#)))))))



(get-lifted (symbol "tech.v3.datatype.functional" "+") (meta (get fun-mappings '+)))


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

