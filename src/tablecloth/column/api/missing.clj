(ns tablecloth.column.api.missing
  (:require [tech.v3.dataset :as ds]
            [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype]
            [tablecloth.api :as tc]))

(defn- into-ds [col]
  (-> {:col col}
      tc/dataset
      (tc/update-columns :col #(col/set-missing % (col/missing col)))))

(defn- out-of-ds [ds]
  (:col ds))

(defn count-missing 
  "Returns the number of missing values in column `col`. "
  [col]
  (-> col col/missing dtype/ecount))

(defn drop-missing 
  "Remove missing values from column `col`."
  [col]
  (-> col
      (into-ds)
      (ds/drop-missing)
      (out-of-ds)))

(defn replace-missing 
  "Replace missing values in column `col` with give `strategy`.

    Strategies may be:

    - `:down` -	Take the previous value, or use provided value.
    - `:up` - Take the next value, or use provided value.
    - `:downup` - Take the previous value, otherwise take the next value.
    - `:updown` - Take the next value, otherwise take the previous value.
    - `:nearest` - Use the nearest of next or previous values. (Strategy `:mid` is an alias for `:nearest`).
    - `:midpoint` - Use the midpoint of averaged values between previous and next (non-missing) values.
    - `:abb` - Impute missing value with approximate Bayesian bootstrap.
               See [r's ABB](https://search.r-project.org/CRAN/refmans/LaplacesDemon/html/ABB.html).
    - `:lerp` - Linearly interpolate values between previous and next nonmissing rows.
    - `:value` - Provide a value explicitly.  Value may be a function in which
                 case it will be called on the column with missing values elided
                 and the return will be used to as the filler."
  ([col]
   (replace-missing col :nearest))
  ([col strategy]
   (replace-missing col strategy nil))
  ([col strategy value]
   (-> col
       (into-ds)
       (ds/replace-missing [:col] strategy value)
       (out-of-ds))))
