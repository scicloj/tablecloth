(ns tablecloth.api.lift-operators
  (:require [tablecloth.api :refer [select-columns]]))

(require '[tablecloth.column.api.operators])

(def ops-mappings (ns-publics 'tablecloth.column.api.operators))

(defn get-arglists [fn-sym]
  (-> fn-sym resolve meta :arglists))

(defn max-cols-allowed [arglists]
  (let [col-symbol-set #{'x 'y 'z}
        longest-arglist (reduce
                         #(if (> (count %1) (count %2)) %1 %2) arglists)]
     (if (= longest-arglist '[x y & args])
      Double/POSITIVE_INFINITY
      (count
       (clojure.set/intersection
        col-symbol-set
        (set longest-arglist))))))


;; broken if '[x y & args]
(defn count-non-col-args [arglist]
  (let [col-symbol-set #{'x 'y 'z}]
    (count (clojure.set/difference (set arglist) col-symbol-set))))

(defn convert-arglists [arglists]
  (let [convert-arglist
        (fn [arglist]
          (apply conj 
              '[ds target-col columns-selector]
              (apply vector
                     (clojure.set/difference (set arglist) #{'x 'y 'z '& 'args}))))]
    (->> arglists (map convert-arglist) set (apply list))))

(defn lift-op [fn-sym]
  (let [defn (symbol "defn")
        let (symbol "let")
        arglists (get-arglists fn-sym)
        max-cols (max-cols-allowed arglists)
        lifted-arglists (convert-arglists arglists)]
    `(~defn ~(symbol (name fn-sym))
      ;; docstring
      ~@(for [args lifted-arglists]
          `(~args
            (~let [selected-cols# (apply vector (tablecloth.api.dataset/columns
                                                 (select-columns ~'ds ~'columns-selector)))
                   args-to-pass# (concat selected-cols# [~@(drop 3 args)])]
             (if (>= ~max-cols (count selected-cols#))
               (tablecloth.api.columns/add-or-replace-column ~'ds ~'target-col (apply ~fn-sym args-to-pass#))
               (throw (Exception. (str "Exceeded maximum number of columns allowed for operation."))))))))))

(lift-op 'tablecloth.column.api.operators/shift)

(eval (lift-op 'tablecloth.column.api.operators/shift))



(apply tablecloth.column.api.operators/shift [[1 2 3] 1] )

(tablecloth.column.api.operators/shift [1 2 3] 1)



(comment 

  (type #'tablecloth.column.api.operators/+)

  (defn get-mappings []
    (ns-publics 'tablecloth.column.api.operators))

  (eval (lift-op 'tablecloth.column.api.operators/+))

  (def ds (tablecloth.api/dataset {:a [1 2 3 4 5]
                                   :b [6 7 8 9 10]
                                   :c [11 12 13 14 16]}))




  )



