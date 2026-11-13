(ns tablecloth.column.api.lag-lead
  (:require [tech.v3.datatype :as dtype]
            [tech.v3.datatype.bitmap :as bitmap]
            [tech.v3.datatype.casting :as casting]
            [tech.v3.datatype.packing :as packing]
            [tech.v3.dataset.column :as col]
            [tech.v3.dataset.protocols :as ds-proto]))

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
        ;; the raw buffer, not the column reader, which widens ints with missing values to doubles
        unpacked (packing/unpack (ds-proto/column-buffer col))
        dt       (dtype/elemwise-datatype unpacked)
        fill     (dtype/const-reader fill-value k)
        data     (condp = direction
                   :forward  [fill (dtype/sub-buffer unpacked 0 (- n k))]
                   :backward [(dtype/sub-buffer unpacked k (- n k)) fill])]
    (col/new-column (:name (meta col))
                    (dtype/make-container dt (dtype/concat-buffers data))
                    {}
                    (shifted-missing col direction k))))

(defn- forward-or-backward? [k]
  (cond
    (> k 0) :forward
    (< k 0) :backward
    :else   :neither))

(defn- shift-with-missing
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

(defn- check-size!
  "Throws unless `n` is a non-negative integer."
  [fn-name n]
  (when-not (and (integer? n) (not (neg? n)))
    (throw (ex-info (str fn-name " size must be a non-negative integer, got "
                         (pr-str n)
                         (when (and (number? n) (neg? n))
                           (str ". Use `" (if (= fn-name "lag") "lead" "lag")
                                "` to shift the other way.")))
                    {:type :tablecloth/invalid-argument
                     :fn   fn-name
                     :size n}))))

(defn- invalid-fill
  [fn-name value dt reason cause]
  (ex-info (str fn-name " :fill-with " (pr-str value) " cannot fill a " dt
                " column (" (name reason) ")")
           {:type     :tablecloth/invalid-argument
            :fn       fn-name
            :argument :fill-with
            :value    value
            :datatype dt
            :reason   reason}
           cause))

(defn- non-finite? [v]
  (and (or (instance? Double v) (instance? Float v))
       (not (Double/isFinite (double v)))))

(defn- cast-fill
  "Cast `value` once to the unpacked datatype of `col` and return it, or throw
  a Tablecloth error when it does not fit that datatype exactly."
  [fn-name col value]
  (let [dt    (packing/unpack-datatype (dtype/elemwise-datatype col))
        fail  (fn [reason cause] (throw (invalid-fill fn-name value dt reason cause)))
        cast! (fn [] (try (casting/cast value dt)
                          (catch Exception e (fail :not-representable e))))]
    (cond
      (casting/integer-type? dt)
      (cond (not (number? value)) (fail :incompatible-type nil)
            (non-finite? value)   (fail :not-representable nil)
            :else (let [v (cast!)]
                    (if (== value v) v (fail :lossy-conversion nil))))

      (casting/float-type? dt)
      (if-not (number? value)
        (fail :incompatible-type nil)
        (let [v (cast!)]
          ;; a finite input that casts to infinity (e.g. a huge BigDecimal) overflowed
          (if (and (not (non-finite? value)) (non-finite? (double v)))
            (fail :not-representable nil)
            v)))

      (= :object dt)        (cast!)
      (= :boolean dt)       (if (boolean? value) (cast!) (fail :incompatible-type nil))
      (#{:string :text} dt) (if (string? value) (cast!) (fail :incompatible-type nil))
      :else (if (= dt (dtype/elemwise-datatype value))
              (cast!)
              (fail :incompatible-type nil)))))

(defn- cast-fill-option
  [fn-name col options]
  (cond-> options
    (some? (:fill-with options)) (update :fill-with #(cast-fill fn-name col %))))

(defn lag
  "Shift `col` forward by `lag-size` rows, filling vacated slots with missing
  values unless `:fill-with` is given in `options`. `lag-size` must be a
  non-negative integer; use `lead` to shift the other way.

  The result keeps the datatype of `col`, and its missing values stay missing.
  A `:fill-with` value is converted to that datatype. Integer columns take any
  number that converts exactly and fits the range (2.0 works, 2.5 does not);
  float columns take any number they can represent; other columns, including
  strings, booleans and dates, take only a value of the same type; `:object`
  columns take anything. Otherwise this throws."
  ([col lag-size]
   (lag col lag-size {}))
  ([col lag-size options]
   (check-size! "lag" lag-size)
   (shift-with-missing col lag-size (cast-fill-option "lag" col options))))

(defn lead
  "Shift `col` backward by `lead-size` rows, filling vacated slots with missing
  values unless `:fill-with` is given in `options`. `lead-size` must be a
  non-negative integer; use `lag` to shift the other way.

  The result keeps the datatype of `col`, and its missing values stay missing.
  A `:fill-with` value is converted to that datatype. Integer columns take any
  number that converts exactly and fits the range (2.0 works, 2.5 does not);
  float columns take any number they can represent; other columns, including
  strings, booleans and dates, take only a value of the same type; `:object`
  columns take anything. Otherwise this throws."
  ([col lead-size]
   (lead col lead-size {}))
  ([col lead-size options]
   (check-size! "lead" lead-size)
   (shift-with-missing col (- lead-size) (cast-fill-option "lead" col options))))
