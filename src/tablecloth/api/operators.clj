(ns
 tablecloth.api.operators
 (:require
  [tablecloth.column.api.operators]
  [tablecloth.api.lift_operators])
 (:refer-clojure
  :exclude
  [*
   +
   -
   /
   <
   <=
   >
   >=
   abs
   and
   bit-and
   bit-and-not
   bit-clear
   bit-flip
   bit-not
   bit-or
   bit-set
   bit-shift-left
   bit-shift-right
   bit-test
   bit-xor
   even?
   identity
   infinite?
   max
   min
   neg?
   not
   odd?
   odd?
   or
   pos?
   quot
   rem
   unsigned-bit-shift-right
   zero?]))

(defn
 kurtosis
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/kurtosis
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/kurtosis
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-set
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-set
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 finite?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/finite?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/finite?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 distance
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/distance
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-min
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/reduce-min
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 to-radians
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-radians
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-radians
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-shift-right
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-shift-right
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ieee-remainder
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ieee-remainder
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-shift-left
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-shift-left
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 acos
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/acos
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/acos
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 to-degrees
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-degrees
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-degrees
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 <
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/<
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 floor
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/floor
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/floor
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 atan2
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/atan2
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 normalize
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/normalize
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 hypot
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/hypot
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 tanh
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tanh
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tanh
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sq
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sq
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sq
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 fill-range
 ([ds target-col columns-selector max-span]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [max-span])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/fill-range
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sum
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sum
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sum
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 pos?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pos?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pos?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 shift
 ([ds target-col columns-selector n]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [n])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/shift
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ceil
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ceil
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ceil
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-xor
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-xor
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 unsigned-bit-shift-right
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/unsigned-bit-shift-right
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 neg?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/neg?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/neg?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 <=
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/<=
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 *
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/*
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 min
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/min
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 atan
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/atan
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/atan
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 mathematical-integer?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/mathematical-integer?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/mathematical-integer?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cumprod
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cumprod
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 expm1
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/expm1
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/expm1
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 identity
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/identity
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/identity
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-max
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/reduce-max
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cumsum
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cumsum
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 descriptive-statistics
 ([ds target-col columns-selector options stats-names stats-data]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat
     selected-cols__34262__auto__
     [options stats-names stats-data])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/descriptive-statistics
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector stats-names]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [stats-names])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/descriptive-statistics
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector options stats-names]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat
     selected-cols__34262__auto__
     [options stats-names])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/descriptive-statistics
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/descriptive-statistics
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 nan?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/nan?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/nan?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-and-not
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-and-not
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 logistic
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/logistic
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/logistic
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cos
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cos
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cos
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log10
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log10
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log10
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quot
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quot
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 dot-product
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/dot-product
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 tan
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tan
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tan
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cbrt
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cbrt
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cbrt
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 eq
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/eq
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 mean
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/mean
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/mean
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 >
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/>
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 not-eq
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/not-eq
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 even?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/even?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/even?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 spearmans-correlation
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/spearmans-correlation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/spearmans-correlation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sqrt
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sqrt
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sqrt
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-*
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/reduce-*
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 next-down
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-down
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-down
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 -
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/-
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 or
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/or
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 distance-squared
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/distance-squared
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 pow
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pow
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 next-up
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-up
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-up
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 skew
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/skew
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/skew
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 exp
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/exp
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/exp
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 mean-fast
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/mean-fast
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 zero?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/zero?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/zero?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 rem
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/rem
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cosh
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cosh
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cosh
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 variance
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/variance
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/variance
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-+
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/reduce-+
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 get-significand
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/get-significand
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/get-significand
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-and
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-and
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 kendalls-correlation
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/kendalls-correlation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/kendalls-correlation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 not
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/not
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/not
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 standard-deviation
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/standard-deviation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/standard-deviation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cummin
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cummin
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 magnitude
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/magnitude
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/magnitude
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cummax
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cummax
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 /
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators//
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-or
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-or
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 equals
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/equals
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 >=
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/>=
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-flip
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-flip
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log1p
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log1p
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log1p
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 asin
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/asin
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/asin
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quartiles
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quartiles
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quartiles
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quartile-3
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quartile-3
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quartile-3
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 infinite?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/infinite?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/infinite?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 round
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/round
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/round
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quartile-1
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quartile-1
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quartile-1
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 odd?
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/odd?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/odd?
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-clear
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-clear
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 +
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/+
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 abs
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/abs
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/abs
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 median
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/median
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/median
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 pearsons-correlation
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pearsons-correlation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pearsons-correlation
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sinh
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sinh
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sinh
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 rint
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/rint
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/rint
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-not
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-not
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-not
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 max
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/max
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ulp
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ulp
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ulp
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 percentiles
 ([ds target-col columns-selector options percentiles]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat
     selected-cols__34262__auto__
     [options percentiles])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/percentiles
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector percentiles]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [percentiles])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/percentiles
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sin
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sin
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sin
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sum-fast
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sum-fast
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 signum
 ([ds target-col columns-selector options]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/signum
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/signum
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 magnitude-squared
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/magnitude-squared
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 and
 ([ds target-col columns-selector]
  (let
   [selected-cols__34262__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__34263__auto__
    (clojure.core/concat selected-cols__34262__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__34262__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/and
      args-to-pass__34263__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

