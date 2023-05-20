(ns tablecloth.api.lift-operators
  (:require [tablecloth.api :refer [select-columns]]
            [tablecloth.utils.codegen :refer [do-lift]]))

(defn get-meta [fn-sym]
  (-> fn-sym resolve meta))

(defn get-arglists [fn-sym]
  (-> fn-sym get-meta :arglists))

(defn get-docstring [fn-sym]
  (-> fn-sym get-meta :docstring))

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

(defn build-docstring [fn-sym]
  (let [original-docstring (get-docstring fn-sym)
        max-allowed (max-cols-allowed (get-arglists fn-sym))]

    (format 
     "Applies the operation %s to the columns selected by
      `columns-selector` and returns a new ds with the the result in
      `target-col`. %s
      
      `columns-selector can be:
      - name
      - sequence of names
      - map of names with new names (rename)
      - function which filter names (via column metadata)"
     fn-sym
     (when (< max-allowed Double/POSITIVE_INFINITY)
       (format
        "This operation takes a maximum of %s columns, so
         `columns-selector` can yield no more than that many columns."
        max-allowed)))))

(defn lift-op
  "Takes a function symbol `fn-sym` and generates a function that
    applies that function to one or more columns of a dataset, placing
    the result in the target column.

    Resulting signature:
    (lift-op [fn-sym]) => (fn [ds columns-selector target-col] ...)"
  [fn-sym]
  (let [defn (symbol "defn")
        let (symbol "let")
        arglists (get-arglists fn-sym)
        max-cols (max-cols-allowed arglists)
        lifted-arglists (convert-arglists arglists)
        new-fn-sym (symbol (name fn-sym))
        new-docstring (build-docstring fn-sym)]
    `(~defn ~new-fn-sym
      ~new-docstring
      ~@(for [args lifted-arglists]
          `(~args
            (~let [selected-cols# (apply vector (tablecloth.api.dataset/columns
                                                 (select-columns ~'ds ~'columns-selector)))
                   args-to-pass# (concat selected-cols# [~@(drop 3 args)])]
             (if (>= ~max-cols (count selected-cols#))
               (tablecloth.api.columns/add-or-replace-column ~'ds ~'target-col (apply ~fn-sym args-to-pass#))
               (throw (Exception. (str "Exceeded maximum number of columns allowed for operation."))))))))))

(def serialized-lift-fn-lookup
  {'[*
     +
     -
     /
     <
     <=
     >
     >=
     abs
     acos
     and
     asin
     atan
     atan2
     bit-and
     bit-and-not
     bit-clear
     bit-flip
     bit-not
     bit-or
     bit-set
     bit-shift-left
     bit-shift-right
     bit-xor
     cbrt
     ceil
     cos
     cosh
     cummax
     cummin
     cumprod
     cumsum
     eq
     even?
     exp
     expm1
     fill-range
     finite?
     floor
     get-significand
     hypot
     identity
     ieee-remainder
     infinite?
     log
     log10
     log1p
     logistic
     mathematical-integer?
     max
     min
     nan?
     neg?
     next-down
     next-up
     normalize
     not
     not-eq
     odd?
     or
     percentiles
     pos?
     pow
     quot
     rem
     rint
     round
     shift
     signum
     sin
     sinh
     sq
     sqrt
     tan
     tanh
     to-degrees
     to-radians
     ulp
     unsigned-bit-shift-right
     zero?] lift-op})

(comment
  (do-lift {:target-ns 'tablecloth.api.operators
            :source-ns 'tablecloth.column.api.operators
            :lift-fn-lookup serialized-lift-fn-lookup
            :deps ['tablecloth.api.lift_operators]
            :exclusions
            '[* + - / < <= > >= abs and bit-and bit-and-not bit-clear bit-flip
             bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor
             even? identity infinite? max min neg? not odd? odd? or pos? quot rem
             unsigned-bit-shift-right zero?]})
  ,)
