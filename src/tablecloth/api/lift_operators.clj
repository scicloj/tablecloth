(ns tablecloth.api.lift-operators
  (:require [tablecloth.api :refer [select-columns]]))

(require '[tablecloth.column.api.operators])

(def ops-mappings (ns-publics 'tablecloth.column.api.operators))

(defn lift-op [fn-sym]
  (let [defn (symbol "defn")
        let  (symbol "let")]
    `(~defn ~(symbol (name fn-sym))
      ~'[ds & columns-selector]
      (~let [just-selected-ds# (select-columns ~'ds ~'columns-selector)]
       #_cols#
       (apply ~fn-sym (tablecloth.api.dataset/columns just-selected-ds#))))))

(lift-op 'tablecloth.column.api.operators/+)

(eval (lift-op 'tablecloth.column.api.operators/+))

(def ds (tablecloth.api/dataset {:a [1 2 3 4 5]
                                 :b [6 7 8 9 10]
                                 :c [11 12 13 14 16]}))


(+ ds :a :c)



