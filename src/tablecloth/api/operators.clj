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
 bit-set
 "Applies the operation tablecloth.column.api.operators/bit-set to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-set
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 finite?
 "Applies the operation tablecloth.column.api.operators/finite? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/finite?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/finite?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 to-radians
 "Applies the operation tablecloth.column.api.operators/to-radians to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-radians
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-radians
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-shift-right
 "Applies the operation tablecloth.column.api.operators/bit-shift-right to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-shift-right
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ieee-remainder
 "Applies the operation tablecloth.column.api.operators/ieee-remainder to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ieee-remainder
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log
 "Applies the operation tablecloth.column.api.operators/log to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-shift-left
 "Applies the operation tablecloth.column.api.operators/bit-shift-left to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-shift-left
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 acos
 "Applies the operation tablecloth.column.api.operators/acos to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/acos
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/acos
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 to-degrees
 "Applies the operation tablecloth.column.api.operators/to-degrees to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-degrees
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/to-degrees
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 <
 "Applies the operation tablecloth.column.api.operators/< to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/<
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 floor
 "Applies the operation tablecloth.column.api.operators/floor to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/floor
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/floor
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 atan2
 "Applies the operation tablecloth.column.api.operators/atan2 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/atan2
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 normalize
 "Applies the operation tablecloth.column.api.operators/normalize to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/normalize
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 hypot
 "Applies the operation tablecloth.column.api.operators/hypot to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/hypot
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 tanh
 "Applies the operation tablecloth.column.api.operators/tanh to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tanh
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tanh
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sq
 "Applies the operation tablecloth.column.api.operators/sq to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sq
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sq
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 fill-range
 "Applies the operation tablecloth.column.api.operators/fill-range to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector max-span]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [max-span])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/fill-range
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 pos?
 "Applies the operation tablecloth.column.api.operators/pos? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pos?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pos?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 shift
 "Applies the operation tablecloth.column.api.operators/shift to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector n]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [n])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/shift
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ceil
 "Applies the operation tablecloth.column.api.operators/ceil to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ceil
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ceil
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-xor
 "Applies the operation tablecloth.column.api.operators/bit-xor to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-xor
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 unsigned-bit-shift-right
 "Applies the operation tablecloth.column.api.operators/unsigned-bit-shift-right to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/unsigned-bit-shift-right
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 neg?
 "Applies the operation tablecloth.column.api.operators/neg? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/neg?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/neg?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 <=
 "Applies the operation tablecloth.column.api.operators/<= to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/<=
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 *
 "Applies the operation tablecloth.column.api.operators/* to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/*
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 min
 "Applies the operation tablecloth.column.api.operators/min to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/min
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 atan
 "Applies the operation tablecloth.column.api.operators/atan to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/atan
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/atan
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 mathematical-integer?
 "Applies the operation tablecloth.column.api.operators/mathematical-integer? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/mathematical-integer?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/mathematical-integer?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cumprod
 "Applies the operation tablecloth.column.api.operators/cumprod to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cumprod
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 expm1
 "Applies the operation tablecloth.column.api.operators/expm1 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/expm1
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/expm1
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 identity
 "Applies the operation tablecloth.column.api.operators/identity to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/identity
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/identity
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cumsum
 "Applies the operation tablecloth.column.api.operators/cumsum to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cumsum
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 nan?
 "Applies the operation tablecloth.column.api.operators/nan? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/nan?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/nan?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-and-not
 "Applies the operation tablecloth.column.api.operators/bit-and-not to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-and-not
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 logistic
 "Applies the operation tablecloth.column.api.operators/logistic to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/logistic
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/logistic
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cos
 "Applies the operation tablecloth.column.api.operators/cos to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cos
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cos
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log10
 "Applies the operation tablecloth.column.api.operators/log10 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log10
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log10
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quot
 "Applies the operation tablecloth.column.api.operators/quot to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/quot
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 tan
 "Applies the operation tablecloth.column.api.operators/tan to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tan
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/tan
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cbrt
 "Applies the operation tablecloth.column.api.operators/cbrt to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cbrt
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cbrt
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 eq
 "Applies the operation tablecloth.column.api.operators/eq to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/eq
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 >
 "Applies the operation tablecloth.column.api.operators/> to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/>
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 not-eq
 "Applies the operation tablecloth.column.api.operators/not-eq to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/not-eq
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 even?
 "Applies the operation tablecloth.column.api.operators/even? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/even?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/even?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sqrt
 "Applies the operation tablecloth.column.api.operators/sqrt to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sqrt
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sqrt
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 next-down
 "Applies the operation tablecloth.column.api.operators/next-down to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-down
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-down
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 -
 "Applies the operation tablecloth.column.api.operators/- to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/-
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 or
 "Applies the operation tablecloth.column.api.operators/or to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/or
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 pow
 "Applies the operation tablecloth.column.api.operators/pow to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/pow
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 next-up
 "Applies the operation tablecloth.column.api.operators/next-up to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-up
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/next-up
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 exp
 "Applies the operation tablecloth.column.api.operators/exp to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/exp
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/exp
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 zero?
 "Applies the operation tablecloth.column.api.operators/zero? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/zero?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/zero?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 rem
 "Applies the operation tablecloth.column.api.operators/rem to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/rem
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cosh
 "Applies the operation tablecloth.column.api.operators/cosh to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cosh
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cosh
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 get-significand
 "Applies the operation tablecloth.column.api.operators/get-significand to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/get-significand
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/get-significand
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-and
 "Applies the operation tablecloth.column.api.operators/bit-and to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-and
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 not
 "Applies the operation tablecloth.column.api.operators/not to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/not
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/not
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cummin
 "Applies the operation tablecloth.column.api.operators/cummin to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cummin
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cummax
 "Applies the operation tablecloth.column.api.operators/cummax to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/cummax
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 /
 "Applies the operation tablecloth.column.api.operators// to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators//
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-or
 "Applies the operation tablecloth.column.api.operators/bit-or to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-or
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 >=
 "Applies the operation tablecloth.column.api.operators/>= to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/>=
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-flip
 "Applies the operation tablecloth.column.api.operators/bit-flip to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-flip
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log1p
 "Applies the operation tablecloth.column.api.operators/log1p to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log1p
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/log1p
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 asin
 "Applies the operation tablecloth.column.api.operators/asin to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/asin
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/asin
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 infinite?
 "Applies the operation tablecloth.column.api.operators/infinite? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/infinite?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/infinite?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 round
 "Applies the operation tablecloth.column.api.operators/round to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/round
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/round
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 odd?
 "Applies the operation tablecloth.column.api.operators/odd? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/odd?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/odd?
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-clear
 "Applies the operation tablecloth.column.api.operators/bit-clear to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-clear
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 +
 "Applies the operation tablecloth.column.api.operators/+ to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/+
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 abs
 "Applies the operation tablecloth.column.api.operators/abs to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/abs
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/abs
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sinh
 "Applies the operation tablecloth.column.api.operators/sinh to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sinh
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sinh
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 rint
 "Applies the operation tablecloth.column.api.operators/rint to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/rint
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/rint
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-not
 "Applies the operation tablecloth.column.api.operators/bit-not to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-not
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/bit-not
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 max
 "Applies the operation tablecloth.column.api.operators/max to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/max
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ulp
 "Applies the operation tablecloth.column.api.operators/ulp to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ulp
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/ulp
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 percentiles
 "Applies the operation tablecloth.column.api.operators/percentiles to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options percentiles]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat
     selected-cols__33066__auto__
     [options percentiles])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/percentiles
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector percentiles]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [percentiles])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/percentiles
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sin
 "Applies the operation tablecloth.column.api.operators/sin to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sin
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/sin
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 signum
 "Applies the operation tablecloth.column.api.operators/signum to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/signum
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/signum
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 and
 "Applies the operation tablecloth.column.api.operators/and to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__33066__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__33067__auto__
    (clojure.core/concat selected-cols__33066__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__33066__auto__))
    (tablecloth.api.columns/add-or-replace-column
     ds
     target-col
     (clojure.core/apply
      tablecloth.column.api.operators/and
      args-to-pass__33067__auto__))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

