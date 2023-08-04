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
 "Applies the operation tablecloth.column.api.operators/kurtosis to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/kurtosis)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/kurtosis)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-set
 "Applies the operation tablecloth.column.api.operators/bit-set to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-set)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 finite?
 "Applies the operation tablecloth.column.api.operators/finite? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/finite?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/finite?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 distance
 "Applies the operation tablecloth.column.api.operators/distance to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/distance)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-min
 "Applies the operation tablecloth.column.api.operators/reduce-min to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/reduce-min)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 to-radians
 "Applies the operation tablecloth.column.api.operators/to-radians to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/to-radians)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/to-radians)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-shift-right
 "Applies the operation tablecloth.column.api.operators/bit-shift-right to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/bit-shift-right)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ieee-remainder
 "Applies the operation tablecloth.column.api.operators/ieee-remainder to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/ieee-remainder)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log
 "Applies the operation tablecloth.column.api.operators/log to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/log)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/log)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-shift-left
 "Applies the operation tablecloth.column.api.operators/bit-shift-left to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/bit-shift-left)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 acos
 "Applies the operation tablecloth.column.api.operators/acos to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/acos)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/acos)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 to-degrees
 "Applies the operation tablecloth.column.api.operators/to-degrees to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/to-degrees)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/to-degrees)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 <
 "Applies the operation tablecloth.column.api.operators/< to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/<)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 floor
 "Applies the operation tablecloth.column.api.operators/floor to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/floor)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/floor)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 atan2
 "Applies the operation tablecloth.column.api.operators/atan2 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/atan2)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 normalize
 "Applies the operation tablecloth.column.api.operators/normalize to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/normalize)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 hypot
 "Applies the operation tablecloth.column.api.operators/hypot to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/hypot)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 tanh
 "Applies the operation tablecloth.column.api.operators/tanh to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/tanh)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/tanh)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sq
 "Applies the operation tablecloth.column.api.operators/sq to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sq)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sq)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sum
 "Applies the operation tablecloth.column.api.operators/sum to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sum)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sum)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 pos?
 "Applies the operation tablecloth.column.api.operators/pos? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/pos?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/pos?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 shift
 "Applies the operation tablecloth.column.api.operators/shift to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector n]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [n])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/shift)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ceil
 "Applies the operation tablecloth.column.api.operators/ceil to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/ceil)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/ceil)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-xor
 "Applies the operation tablecloth.column.api.operators/bit-xor to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-xor)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 unsigned-bit-shift-right
 "Applies the operation tablecloth.column.api.operators/unsigned-bit-shift-right to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/unsigned-bit-shift-right)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 neg?
 "Applies the operation tablecloth.column.api.operators/neg? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/neg?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/neg?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 <=
 "Applies the operation tablecloth.column.api.operators/<= to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/<=)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 *
 "Applies the operation tablecloth.column.api.operators/* to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/*)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 min
 "Applies the operation tablecloth.column.api.operators/min to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/min)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 atan
 "Applies the operation tablecloth.column.api.operators/atan to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/atan)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/atan)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 mathematical-integer?
 "Applies the operation tablecloth.column.api.operators/mathematical-integer? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/mathematical-integer?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/mathematical-integer?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cumprod
 "Applies the operation tablecloth.column.api.operators/cumprod to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cumprod)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 expm1
 "Applies the operation tablecloth.column.api.operators/expm1 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/expm1)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/expm1)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 identity
 "Applies the operation tablecloth.column.api.operators/identity to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/identity)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/identity)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-max
 "Applies the operation tablecloth.column.api.operators/reduce-max to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/reduce-max)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cumsum
 "Applies the operation tablecloth.column.api.operators/cumsum to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cumsum)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 nan?
 "Applies the operation tablecloth.column.api.operators/nan? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/nan?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/nan?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-and-not
 "Applies the operation tablecloth.column.api.operators/bit-and-not to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-and-not)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 logistic
 "Applies the operation tablecloth.column.api.operators/logistic to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/logistic)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/logistic)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cos
 "Applies the operation tablecloth.column.api.operators/cos to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cos)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cos)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log10
 "Applies the operation tablecloth.column.api.operators/log10 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/log10)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/log10)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quot
 "Applies the operation tablecloth.column.api.operators/quot to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/quot)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 dot-product
 "Applies the operation tablecloth.column.api.operators/dot-product to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/dot-product)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 tan
 "Applies the operation tablecloth.column.api.operators/tan to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/tan)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/tan)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cbrt
 "Applies the operation tablecloth.column.api.operators/cbrt to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cbrt)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cbrt)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 eq
 "Applies the operation tablecloth.column.api.operators/eq to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/eq)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 mean
 "Applies the operation tablecloth.column.api.operators/mean to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/mean)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/mean)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 >
 "Applies the operation tablecloth.column.api.operators/> to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/>)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 not-eq
 "Applies the operation tablecloth.column.api.operators/not-eq to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/not-eq)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 even?
 "Applies the operation tablecloth.column.api.operators/even? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/even?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/even?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sqrt
 "Applies the operation tablecloth.column.api.operators/sqrt to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sqrt)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sqrt)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-*
 "Applies the operation tablecloth.column.api.operators/reduce-* to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/reduce-*)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 next-down
 "Applies the operation tablecloth.column.api.operators/next-down to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/next-down)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/next-down)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 -
 "Applies the operation tablecloth.column.api.operators/- to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/-)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 or
 "Applies the operation tablecloth.column.api.operators/or to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/or)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 distance-squared
 "Applies the operation tablecloth.column.api.operators/distance-squared to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/distance-squared)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 pow
 "Applies the operation tablecloth.column.api.operators/pow to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/pow)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 next-up
 "Applies the operation tablecloth.column.api.operators/next-up to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/next-up)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/next-up)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 skew
 "Applies the operation tablecloth.column.api.operators/skew to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/skew)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/skew)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 exp
 "Applies the operation tablecloth.column.api.operators/exp to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/exp)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/exp)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 mean-fast
 "Applies the operation tablecloth.column.api.operators/mean-fast to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/mean-fast)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 zero?
 "Applies the operation tablecloth.column.api.operators/zero? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/zero?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/zero?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 rem
 "Applies the operation tablecloth.column.api.operators/rem to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/rem)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cosh
 "Applies the operation tablecloth.column.api.operators/cosh to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cosh)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cosh)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 variance
 "Applies the operation tablecloth.column.api.operators/variance to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/variance)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/variance)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 reduce-+
 "Applies the operation tablecloth.column.api.operators/reduce-+ to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/reduce-+)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 get-significand
 "Applies the operation tablecloth.column.api.operators/get-significand to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/get-significand)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/get-significand)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-and
 "Applies the operation tablecloth.column.api.operators/bit-and to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-and)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 not
 "Applies the operation tablecloth.column.api.operators/not to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/not)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/not)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cummin
 "Applies the operation tablecloth.column.api.operators/cummin to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cummin)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 magnitude
 "Applies the operation tablecloth.column.api.operators/magnitude to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/magnitude)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/magnitude)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 cummax
 "Applies the operation tablecloth.column.api.operators/cummax to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/cummax)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 /
 "Applies the operation tablecloth.column.api.operators// to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators//)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-or
 "Applies the operation tablecloth.column.api.operators/bit-or to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-or)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 >=
 "Applies the operation tablecloth.column.api.operators/>= to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 3 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     3
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/>=)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-flip
 "Applies the operation tablecloth.column.api.operators/bit-flip to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-flip)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 log1p
 "Applies the operation tablecloth.column.api.operators/log1p to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/log1p)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/log1p)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 asin
 "Applies the operation tablecloth.column.api.operators/asin to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/asin)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/asin)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quartile-3
 "Applies the operation tablecloth.column.api.operators/quartile-3 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/quartile-3)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/quartile-3)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 infinite?
 "Applies the operation tablecloth.column.api.operators/infinite? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/infinite?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/infinite?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 round
 "Applies the operation tablecloth.column.api.operators/round to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/round)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/round)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 quartile-1
 "Applies the operation tablecloth.column.api.operators/quartile-1 to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/quartile-1)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/quartile-1)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 odd?
 "Applies the operation tablecloth.column.api.operators/odd? to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/odd?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/odd?)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-clear
 "Applies the operation tablecloth.column.api.operators/bit-clear to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-clear)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 +
 "Applies the operation tablecloth.column.api.operators/+ to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/+)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 abs
 "Applies the operation tablecloth.column.api.operators/abs to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/abs)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/abs)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 median
 "Applies the operation tablecloth.column.api.operators/median to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/median)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/median)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sinh
 "Applies the operation tablecloth.column.api.operators/sinh to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sinh)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sinh)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 rint
 "Applies the operation tablecloth.column.api.operators/rint to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/rint)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/rint)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 bit-not
 "Applies the operation tablecloth.column.api.operators/bit-not to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-not)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/bit-not)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 max
 "Applies the operation tablecloth.column.api.operators/max to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. null\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     ##Inf
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/max)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 ulp
 "Applies the operation tablecloth.column.api.operators/ulp to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/ulp)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/ulp)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 percentiles
 "Applies the operation tablecloth.column.api.operators/percentiles to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options percentiles]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat
     selected-cols__49993__auto__
     [options percentiles])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/percentiles)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector percentiles]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [percentiles])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/percentiles)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sin
 "Applies the operation tablecloth.column.api.operators/sin to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sin)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sin)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 sum-fast
 "Applies the operation tablecloth.column.api.operators/sum-fast to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/sum-fast)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 signum
 "Applies the operation tablecloth.column.api.operators/signum to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector options]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [options])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/signum)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation."))))))
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/signum)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 magnitude-squared
 "Applies the operation tablecloth.column.api.operators/magnitude-squared to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 1 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     1
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply
      tablecloth.column.api.operators/magnitude-squared)
     (clojure.core/identity))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

(defn
 and
 "Applies the operation tablecloth.column.api.operators/and to the columns selected by\n      `columns-selector` and returns a new ds with the the result in\n      `target-col`. This operation takes a maximum of 2 columns, so\n         `columns-selector` can yield no more than that many columns.\n      \n      `columns-selector can be:\n      - name\n      - sequence of names\n      - map of names with new names (rename)\n      - function which filter names (via column metadata)"
 ([ds target-col columns-selector]
  (let
   [selected-cols__49993__auto__
    (clojure.core/apply
     clojure.core/vector
     (tablecloth.api.dataset/columns
      (tablecloth.api/select-columns ds columns-selector)))
    args-to-pass__49994__auto__
    (clojure.core/concat selected-cols__49993__auto__ [])]
   (if
    (clojure.core/>=
     2
     (clojure.core/count selected-cols__49993__auto__))
    (clojure.core/->>
     args-to-pass__49994__auto__
     (clojure.core/apply tablecloth.column.api.operators/and)
     (tablecloth.api/add-or-replace-column ds target-col))
    (throw
     (java.lang.Exception.
      (clojure.core/str
       "Exceeded maximum number of columns allowed for operation.")))))))

