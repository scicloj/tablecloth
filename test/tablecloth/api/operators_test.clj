(ns tablecloth.api.operators-test
  (:refer-clojure :exclude [* + - / < <= > >= abs and bit-and bit-and-not bit-clear bit-flip
                              bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor
                              even? identity infinite? max min neg? not odd? odd? or pos? quot rem
                              unsigned-bit-shift-right zero?])
  (:require [tablecloth.api :refer [dataset]]
            [tech.v3.datatype :refer [elemwise-datatype]]
            [midje.sweet :refer [fact facts =>]])
  (:use [tablecloth.api.operators]))



(defn scalar? [item]
  (= (tech.v3.datatype.argtypes/arg-type item)
     :scalar))

(defn has-col? 
  ([colname]
   (partial has-col? colname))
  ([colname ds]
   (contains? ds colname)))

(facts
 "about ops that return a dataset with a new column"

 (facts
  "about ops that take a maximum of two columns"
  (let [ops [and #_distance #_distance-squared #_dot-product eq not-eq or]
        ds (dataset {:a [1 2 3]
                     :b [4 5 6]})]
      (doseq [op ops]
        (let [result (op ds :c [:a :b :c])]
          (contains? result :c) => true))))

 (facts
  "about ops that take a maximum of three columns"
  (let [ops [< > <= >=]
        ds (dataset {:a [1 2 3]
                     :b [4 5 6]
                     :c [7 8 9]})]
    (doseq [op ops]
      (let [result (op ds :d [:a :b :c])]
        (contains? result :d) => true))))

 (facts
  "about ops that can take an unlimited number of columns"
  (let [ops [/
             -
             +
             *
             atan2
             ;; equals  // this function doesn't seem to handle more than two cols
             hypot
             bit-and
             bit-and-not
             bit-clear
             bit-flip
             bit-or
             bit-set
             bit-shift-right
             bit-shift-left
             bit-xor
             ieee-remainder
             max
             min
             pow
             quot
             rem
             unsigned-bit-shift-right]
        ds (dataset {:a [1 2 3]
                     :b [4 5 6]
                     :c [7 8 9]
                     :d [10 11 12]})]
    (doseq [op ops]
      (let [result (op ds :e [:a :b :c :d])]
        (contains? result :e) => true)))))

;; (bit-clear (dataset {:a [1 2 3]
;;                      :b [4 5 6]
;;                      :c [7 8 9]
;;                :d [10 11 12]})
;;      :e
;;      [:a :b :c])

(defn longest-vector [lst]
  (reduce #(max-key count %1 %2) lst))

(->> (ns-publics 'tablecloth.column.api.operators)
     (map (fn [[sym var]] [sym (-> var meta :arglists)]))
     (map (fn [[sym arglist]] [sym (longest-vector arglist)]))
     (reduce (fn [memo [sym longest-arglist]]
               (if (contains? memo longest-arglist)
                 (update memo longest-arglist conj sym)
                 (assoc memo longest-arglist [sym])))
             {})
     (reduce (fn [m [k v]] (update m k sort v)) {})
     )
;; => {[x]
;;     (cummax
;;      cummin
;;      cumprod
;;      cumsum
;;      magnitude-squared
;;      mean-fast
;;      normalize
;;      reduce-*
;;      reduce-+
;;      reduce-max
;;      reduce-min
;;      sum-fast),
;;     [x max-span] (fill-range),
;;     [x stats-names stats-data options] (descriptive-statistics),
;;     [x n] (shift),
;;     [x options]
;;     (abs
;;      acos
;;      asin
;;      atan
;;      bit-not
;;      cbrt
;;      ceil
;;      cos
;;      cosh
;;      even?
;;      exp
;;      expm1
;;      finite?
;;      floor
;;      get-significand
;;      identity
;;      infinite?
;;      kurtosis
;;      log
;;      log10
;;      log1p
;;      logistic
;;      magnitude
;;      mathematical-integer?
;;      mean
;;      median
;;      nan?
;;      neg?
;;      next-down
;;      next-up
;;      not
;;      odd?
;;      pos?
;;      quartile-1
;;      quartile-3
;;      quartiles
;;      rint
;;      round
;;      signum
;;      sin
;;      sinh
;;      skew
;;      sq
;;      sqrt
;;      standard-deviation
;;      sum
;;      tan
;;      tanh
;;      to-degrees
;;      to-radians
;;      ulp
;;      variance
;;      zero?),
;;     [x y] (and distance distance-squared dot-product eq not-eq or),
;;     [x y & args]
;;     (*
;;      +
;;      -
;;      /
;;      atan2
;;      bit-and
;;      bit-and-not
;;      bit-clear
;;      bit-flip
;;      bit-or
;;      bit-set
;;      bit-shift-left
;;      bit-shift-right
;;      bit-xor
;;      equals
;;      hypot
;;      ieee-remainder
;;      max
;;      min
;;      pow
;;      quot
;;      rem
;;      unsigned-bit-shift-right),
;;     [x y options]
;;     (kendalls-correlation pearsons-correlation spearmans-correlation),
;;     [x y z] (< <= > >=),
;;     [x percentiles options] (percentiles)}




;; (facts
;;  "about ops that take a single column and return a scalar"
;;  (let [ops [magnitude
;;             reduce-max
;;             reduce-min
;;             reduce-*
;;             reduce-+
;;             mean-fast
;;             sum-fast
;;             magnitude-squared]
;;        a (sample-column 5)]
;;    (doseq [op ops]
;;      (op a) => scalar?)))

;; (facts
;;  "about ops that take one or more columns or scalars and return either a scalar or a column"
;;  (let [ops [/ - +]
;;        a (sample-column 5)
;;        b (sample-column 5)
;;        c (sample-column 5)
;;        d (sample-column 5)]
;;    (doseq [op ops]
;;      (op a) => column?
;;      (op a b) => column?
;;      (op a b c) => column?
;;      (op a b c d) => column?
;;      (op 1) => scalar?
;;      (op 1 2) => scalar?
;;      (op 1 2 3) => scalar?)))

;; (facts
;;  "about comparison ops that take two or more columns and return a boolean"
;;  (let [ops [> >= < <=]
;;        a (sample-column 5)
;;        b (sample-column 5)
;;        c (sample-column 5)]
;;    (doseq [op ops]
;;      (op a b) => column?
;;      (op a b c) => column?
;;      (op 1 2) => boolean?)))

;; (facts
;;  "about comparison ops that take two columns and return a boolean"
;;  (let [ops [equals]
;;        a (sample-column 5)
;;        b (sample-column 5)]
;;    (doseq [op ops]
;;      (op a b) => boolean?)))

;; (facts
;;  "about comparison ops that take two columns or scalars and return a boolean or column of booleans"
;;  (let [ops [or and eq not-eq]
;;        a (sample-column 5)
;;        b (sample-column 5)]
;;    (doseq [op ops]
;;      (op a b) => column?
;;      (typeof (op a b)) => :boolean
;;      (op 1 2) => boolean?)))

;; (facts
;;  "about ops that take a single column or scalar and return a scalar"
;;  (let [ops [kurtosis
;;             sum
;;             mean
;;             skew
;;             variance
;;             standard-deviation
;;             quartile-3
;;             quartile-1
;;             median]
;;        a (sample-column 5)]
;;    (doseq [op ops]
;;      (op a) => scalar?)))

;; (facts
;;  "about ops that take two or more scalars or columns and return a column or scalar"
;;  (let [ops [*
;;             bit-and
;;             bit-and-not
;;             bit-clear
;;             bit-flip
;;             bit-or
;;             bit-set
;;             bit-shift-right
;;             bit-shift-left
;;             ;; bit-test
;;             bit-xor
;;             hypot
;;             ieee-remainder
;;             max
;;             min
;;             pow
;;             quot
;;             rem
;;             unsigned-bit-shift-right
;;             ]
;;        a (sample-column 5)
;;        b (sample-column 5)
;;        c (sample-column 5)]
;;    (doseq [op ops]
;;      (op a b) => column?
;;      (op a b c) => column?
;;      (op 5 5)
;;      (op 5 5 5))))

;; (facts
;;  "about ops that take left-hand / right-hand columns and return a scalar"
;;  (let [ops [distance
;;             dot-product
;;             distance-squared
;;             kendalls-correlation
;;             pearsons-correlation
;;             spearmans-correlation]
;;        a (sample-column 5)
;;        b (sample-column 5)]
;;    (doseq [op ops]
;;      (op a b) => scalar?)))

;; (facts
;;  "about ops that take a single column or scalar and return boolean or column of booleans"
;;  (let [ops [finite?
;;             pos?
;;             neg?
;;             mathematical-integer?
;;             nan?
;;             even?
;;             zero?
;;             not
;;             infinite?
;;             odd?]
;;        a (sample-column 5)]
;;    (doseq [op ops]
;;      (op a) => column? 
;;      (typeof (op a)) => :boolean
;;      (op 1) => boolean?)))
