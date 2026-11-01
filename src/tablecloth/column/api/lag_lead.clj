(ns tablecloth.column.api.lag-lead
  (:require [tech.v3.datatype :as dtype]
            [tech.v3.datatype.bitmap :as bitmap]
            [tech.v3.datatype.packing :as packing]
            [tech.v3.dataset.column :as col]))

(defn- repack-if-needed
  "`prepend-column-with-empty` unpacks packed temporals and `packing/pack`
  returns a plain buffer. Rebuild a real column with the original name,
  packed data and the missing set."
  [orig shifted]
  (if (packing/packed-datatype? (dtype/get-datatype orig))
    (col/new-column (:name (meta orig))
                    (packing/pack shifted)
                    {}
                    (col/missing shifted))
    shifted))

(defn- shifted-missing
  "Original missing indexes moved by `k`, with out-of-range ones dropped."
  [col direction k]
  (let [n    (count col)
        move (if (= direction :forward) #(+ % k) #(- % k))]
    (bitmap/->bitmap (filter #(< -1 % n) (map move (col/missing col))))))

(defn- fill-shift
  "Shift `col` by `k` and put `fill-value` in the vacated slots. The vacated
  slots are not missing; slots that were already missing stay missing."
  [col direction k fill-value]
  (let [n        (count col)
        k        (min k n)
        unpacked (packing/unpack col)
        dt       (dtype/elemwise-datatype unpacked)
        fill     (dtype/const-reader fill-value k)
        data     (condp = direction
                   :forward  [fill (dtype/sub-buffer unpacked 0 (- n k))]
                   :backward [(dtype/sub-buffer unpacked k (- n k)) fill])]
    (col/new-column (:name (meta col))
                    (dtype/make-container dt (dtype/concat-buffers data))
                    {}
                    (shifted-missing col direction k))))

(defn forward-or-backward? [k]
  (cond
    (> k 0) :forward
    (< k 0) :backward
    :else   :neither))

(defn shift-with-missing
  "Shift `col` by `k` rows. Positive `k` moves values forward (lag), negative
  moves them backward (lead). Vacated slots are missing unless `:fill-with` is
  given in `options`."
  ([col k]
   (shift-with-missing col k {}))
  ([col k {:keys [fill-with]}]
   (let [direction (forward-or-backward? k)
         ;; `some?` rather than truthiness: `false` is a valid fill for booleans
         fill?     (some? fill-with)]
     (condp = direction
       :forward
       (if fill?
         (repack-if-needed col (fill-shift col :forward k fill-with))
         (repack-if-needed
          col
          (dtype/sub-buffer (col/prepend-column-with-empty col (abs k)) 0 (count col))))
       :backward
       (if fill?
         (repack-if-needed col (fill-shift col :backward (abs k) fill-with))
         (repack-if-needed
          col
          (dtype/sub-buffer (col/extend-column-with-empty col (abs k)) (abs k) (count col))))
       :neither
       col))))

(defn lag
  ([col lag-size]
   (shift-with-missing col lag-size {}))
  ([col lag-size options]
   (shift-with-missing col lag-size options)))

(defn lead [col lead-size options]
  ;; add a lead with shift-with-missing
  )
