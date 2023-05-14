(ns tablecloth.api.lift-operators
  (:require [tablecloth.api :refer [select-columns]]))

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
