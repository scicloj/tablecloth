(ns tablecloth.api.lift-operators
  (:require [tablecloth.api.columns :refer [select-columns add-or-replace-column]]
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


(defn convert-arglists [arglists target-column?]
  (let [convert-arglist
        (fn [arglist]
          (apply conj
                 (if target-column?
                   '[ds target-col columns-selector]
                   '[ds columns-selector])
                 (apply vector (clojure.set/difference (set arglist) #{'x 'y 'z '& 'args}))))]
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
  [fn-sym {:keys [return-ds? make-aggregator?]
           :or {return-ds? false
                make-aggregator? false}}]
  (let [defn (symbol "defn")
        let (symbol "let")
        arglists (get-arglists fn-sym)
        max-cols (max-cols-allowed arglists)
        lifted-arglists (convert-arglists arglists return-ds?)
        new-fn-sym (symbol (name fn-sym))
        new-docstring (build-docstring fn-sym)]
    `(~defn ~new-fn-sym
      ~new-docstring
      ~@(for [args lifted-arglists]
          (if make-aggregator?
            ;; build an aggregator fn
            `(~args
              (~let [aggregator#
                     (fn [ds#]
                       (~let [ds-with-selected-cols#
                              (select-columns ds# ~'columns-selector)
                              cols-count#
                              (-> ds-with-selected-cols#
                                  tablecloth.api/column-names
                                  count)
                              selected-cols# (tablecloth.api/columns ds-with-selected-cols#)]
                        (if (>= ~max-cols cols-count#)
                          (apply ~fn-sym (apply vector selected-cols#))
                          (throw (Exception. (str "Exceeded maximum number of columns allowed for operation."))))))]
               (tablecloth.api/aggregate ~'ds aggregator#)))
            ;; build either a fn that returns a dataset or the result of the operation
            `(~args
              (~let [selected-cols# (apply vector (tablecloth.api.dataset/columns
                                                   (select-columns ~'ds ~'columns-selector)))
                     args-to-pass# (concat selected-cols# [~@(drop 3 args)])]
               (if (>= ~max-cols (count selected-cols#))
                 (->> args-to-pass#
                      (apply ~fn-sym)
                      ~(if return-ds? `(add-or-replace-column ~'ds ~'target-col) `(identity)))
                 (throw (Exception. (str "Exceeded maximum number of columns allowed for operation.")))))))))))

(lift-op 'tablecloth.column.api.operators/bit-set {})

(lift-op 'tablecloth.column.api.operators/mean {:make-aggregator? true})

(def serialized-lift-fn-lookup
  {'[distance
     distance-squared
     dot-product
     kurtosis
     magnitude
     magnitude-squared
     mean
     mean-fast
     median
     quartile-1
     quartile-3
     #_quartiles ;; this returns a vector of quartiles not sure it fits here
     reduce-*
     reduce-+
     reduce-max
     reduce-min
     skew
     sum
     sum-fast
     variance]
    {:lift-fn lift-op :optional-args {:make-aggregator? true}}
   '[*
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
     ;; equals ;; leaving this one out. not clear its signature is right.
     even?
     exp
     expm1
     ;; fill-range ;; this one has an odd return value, not a column
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
     zero?] {:lift-fn lift-op :optional-args {:return-ds? true}}})

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
