(ns lag-exercise.scratch
  "Temporary scratchpad for the lag/lead exercise (section 3). Delete when done."
  (:require [criterium.core :as crit]
            [lag-exercise.old :as lag-old]
            [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]
            [tech.v3.dataset.column :as col]
            [tech.v3.dataset.rolling :as rolling]
            [tech.v3.datatype :as dtype]
            [tech.v3.datatype.bitmap :as bitmap]
            [tech.v3.datatype.packing :as packing])
  (:import [java.time LocalDate]
           [java.util Random]
           [org.roaringbitmap RoaringBitmap]))

;; ---------------------------------------------------------------------------
;; Candidate implementation (section 3 primitives + datatype restore)

(defn shift-column
  "Positional lag of column `c` by `k` >= 0 rows using TMD primitives.
  Boundary rows are missing. Packed temporals are re-packed afterwards
  (prepend-column-with-empty unpacks them) and the missing set is carried
  over with the 4-arity `new-column`."
  [c k]
  (let [n (dtype/ecount c)
        k (min k n)]
    (if (zero? k)
      c
      (let [dt      (dtype/get-datatype c)
            shifted (dtype/sub-buffer (col/prepend-column-with-empty c k) 0 n)]
        (if (packing/packed-datatype? dt)
          (col/new-column (:name (meta c))
                          (packing/pack shifted)
                          {}
                          (col/missing shifted))
          shifted)))))

(defn new-lag
  "Same 4-arity signature as the old lag: (ds new-column-name column-name k)."
  [ds new-column-name column-name k]
  (when-not (and (integer? k) (not (neg? k)))
    (throw (ex-info (str "lag-size must be a non-negative integer, got "
                         (pr-str k))
                    {:lag-size k})))
  (tc/add-column ds new-column-name (shift-column (ds column-name) k)))

;; ---------------------------------------------------------------------------
;; Benchmark tools (section 7). No extra deps: System/nanoTime based.
;;
;; Recipe: seeded inputs, force results, warm up, gc, 15+ timed runs, report
;; median/min/max, and check old/new parity BEFORE timing.

(defn jvm-info
  "Record this next to any numbers you quote."
  []
  {:java-version (System/getProperty "java.version")
   :java-vm      (System/getProperty "java.vm.name")
   :max-heap-mb  (quot (.maxMemory (Runtime/getRuntime)) (* 1024 1024))
   :cores        (.availableProcessors (Runtime/getRuntime))
   :os           (System/getProperty "os.name")})

(defn make-input
  "Seeded one-column dataset `{:a ...}` of `n` rows. `dt` is one of
  :float64 :int64 :string :date. Input building is never timed."
  [dt n seed]
  (let [rnd (Random. (long seed))]
    (tc/dataset
     {:a (case dt
           :float64 (double-array (repeatedly n #(.nextDouble rnd)))
           :int64   (long-array (repeatedly n #(long (.nextInt rnd 1000000))))
           :string  (vec (repeatedly n #(str "s" (.nextInt rnd 1000))))
           :date    (vec (repeatedly n #(LocalDate/ofEpochDay
                                         (+ 18000 (.nextInt rnd 1000))))))})))

(defn force-last
  "Read the last element so a lazy result can't skip the work."
  [c]
  (let [n (dtype/ecount c)]
    (when (pos? n) (c (dec n)))))

(defn force-all
  "Stricter: read every element. Costs the same for old and new, but is
  noisy for big inputs. Use to check lazy-view read cost."
  [c]
  (dotimes [i (dtype/ecount c)] (c i))
  nil)

(defn- elapsed-ms [f]
  (let [t0 (System/nanoTime)
        r  (f)]
    [(/ (- (System/nanoTime) t0) 1e6) r]))

(defn bench
  "Time `(f)` after warm-up. `f` must do the work AND force its result.
  Returns {:median :min :max :runs} in ms.
  Options: :warmup-iters (default 5), :warmup-ms (default 1000), :runs (15)."
  [f & {:keys [warmup-iters warmup-ms runs]
        :or   {warmup-iters 5 warmup-ms 1000 runs 15}}]
  ;; warm up: at least `warmup-iters` calls AND at least `warmup-ms` elapsed
  (let [start (System/nanoTime)]
    (loop [i 0]
      (f)
      (when (or (< (inc i) warmup-iters)
                (< (/ (- (System/nanoTime) start) 1e6) warmup-ms))
        (recur (inc i)))))
  (System/gc)
  (Thread/sleep 200)
  (let [times  (vec (sort (repeatedly runs #(first (elapsed-ms f)))))
        median (let [m (quot (count times) 2)]
                 (if (odd? (count times))
                   (times m)
                   (/ (+ (times (dec m)) (times m)) 2.0)))]
    {:median median
     :min    (first times)
     :max    (peek times)
     :runs   runs}))

(defn parity
  "Compare old/new result columns for lag size `k`. Boundary rows (< k) are
  EXPECTED to differ (old: 0 for numerics, new: missing), so only rows k..n
  are compared for value and missingness. Datatype is compared everywhere.
  Reports how each side treats the boundary."
  [old-c new-c k]
  (let [n   (dtype/ecount old-c)
        mo  ^RoaringBitmap (col/missing old-c)
        mn  ^RoaringBitmap (col/missing new-c)
        body-ok? (every? (fn [i]
                           (and (= (old-c i) (new-c i))
                                (= (.contains mo (int i))
                                   (.contains mn (int i)))))
                         (range (min k n) n))]
    {:length-match (= n (dtype/ecount new-c))
     :dtype-match  (= (dtype/get-datatype old-c) (dtype/get-datatype new-c))
     :old-dtype    (dtype/get-datatype old-c)
     :new-dtype    (dtype/get-datatype new-c)
     :body-match   body-ok?
     :boundary-old (when (and (pos? k) (pos? n))
                     {:value (old-c 0) :missing? (.contains mo (int 0))})
     :boundary-new (when (and (pos? k) (pos? n))
                     {:value (new-c 0) :missing? (.contains mn (int 0))})}))

(defn run-suite
  "For each datatype and size: check parity, then time old vs new for lag `k`.
  `impls` is {:old (fn [ds new-col col k] ...) :new (fn ...)}.
  Returns a vector of rows; use `print-suite` to display."
  [& {:keys [dtypes sizes k impls force seed runs]
      :or   {dtypes [:float64 :int64 :string :date]
             sizes  [1000 100000 1000000]
             k      1
             impls  {:old lag-old/lag :new new-lag}
             force  force-last
             seed   42
             runs   15}}]
  (vec
   (for [dt dtypes
         n  sizes
         :let [ds      (make-input dt n seed)
               results (update-vals impls #(% ds :out :a k))
               par     (parity (:out (:old results)) (:out (:new results)) k)
               timings (into {}
                             (for [[nm f] impls]
                               [nm (bench #(force (:out (f ds :out :a k)))
                                          :runs runs)]))]]
     {:dtype dt :n n :k k :parity par :timings timings})))

(defn print-suite
  "Parity flags and timings (median [min-max] ms) per case."
  [rows]
  (println "JVM:" (jvm-info))
  (doseq [{:keys [dtype n k parity timings]} rows]
    (println (format "%-8s n=%-8d k=%d  dtype-match=%s body-match=%s"
                     (name dtype) n k
                     (:dtype-match parity) (:body-match parity)))
    (doseq [[nm {:keys [median min max]}] timings]
      (println (format "    %-4s median %9.2f ms   [min %9.2f  max %9.2f]"
                       (name nm) (double median) (double min) (double max))))
    (let [{o :old n* :new} timings]
      (when (and o n* (pos? (:median n*)))
        (println (format "    old/new speedup: %.1fx"
                         (/ (:median o) (:median n*)))))))
  :done)

(comment
  ;; Quick smoke run (small, few runs):
  (print-suite (run-suite :sizes [1000] :runs 5))

  ;; Real run per section 7:
  (print-suite (run-suite))

  ;; Stricter forcing (reads every element) for one case:
  (print-suite (run-suite :dtypes [:date] :sizes [100000] :force force-all))

  ;; Compare against the matrix too:
  ;; (lag-exercise.matrix/diff (lag-exercise.matrix/run lag-old/lag) (lag-exercise.matrix/run new-lag))
  )

(comment
  ;; Sample column with a missing value
  (def c (col/new-column :a [1 2 nil 4 5]))

  (def ds (tc/dataset {:a [1 2 nil 4 5]}))

  ;; select: what happens to the missing set?
  (col/missing (:a ds))

  (tc/select ds :a [0 2 4])
  ;; => _unnamed [3 1]:
  ;;    | :a |
  ;;    |---:|
  ;;    |  1 |
  ;;    |    |
  ;;    |  5 |

  (col/missing (tc/select ds :a [0 2 4]))

  ;; answer the missig set is tracked correctly

  ;; prepend-column-with-empty / extend-column-with-empty:
  ;; datatype? missing set? fill value?

  (vec c)

  (def c2 (col/extend-column-with-empty c 2))

  (vec c2)

  (col/missing c2)

  (def c3 (col/prepend-column-with-empty c 2))

  (dtype/get-datatype c3)

  (vec c3)

  (col/missing c3)

  ;; answer: datatype, missing set are correct. fill value is nil! is that right?

  ;; new-column (4-arity): data, metadata, missing set
  (def c4 (col/new-column :a [1 2 3 4 5] {} (bitmap/->bitmap [2])))

  (vec c4)

  (col/missing c4)

  ;; answer: missing set is honored even though the data at that index is non-nil

;; packing: packed-datatype? pack, unpack-datatype
  (def dates (col/new-column :d [(java.time.LocalDate/of 2020 1 1)
                                 (java.time.LocalDate/of 2020 1 2)]))

  (dtype/get-datatype dates)

  (def packed-dates (packing/pack dates))

  (dtype/get-datatype packed-dates)

  (packing/packed-datatype? (dtype/get-datatype packed-dates))

  (packing/unpack-datatype (dtype/get-datatype packed-dates))

  ;; answer: pack/unpack round-trip the datatype; packed-datatype? tells us
  ;; whether a datatype is already in packed form

  ;; Compose two of the above into a lag
  (def lagged (dtype/sub-buffer (col/prepend-column-with-empty c 2) 0 (count c)))

  (vec c)
  ;; => [1 2 nil 4 5]

  (vec lagged)
  ;; => [nil nil 1 2 nil]

  (col/missing lagged)
  ;; => {0,1,4}

  ;; answer: prepend-column-with-empty shifts values forward and introduces
  ;; missing values at the front; sub-buffer trims it back to the original
  ;; length, giving us a proper lag-by-n column with correct missing tracking

  ;; test type of lagged
  (dtype/get-datatype lagged)

  ;; lets' test it with dates
  (def lagged-dates
    (dtype/sub-buffer (col/prepend-column-with-empty dates 1) 0 (count dates)))

  (vec lagged-dates)

  (dtype/get-datatype lagged-dates)

  ;; when would this column be packed?
  (def lagged-packed-dates
    (dtype/sub-buffer (col/prepend-column-with-empty packed-dates 1) 0 (count packed-dates)))

  (vec lagged-packed-dates)

  (dtype/get-datatype lagged-packed-dates)

  (tcc/column? lagged-dates)
  (tcc/column? lagged-packed-dates)

  (def lagged-dates-ds
    (tc/dataset {:dates lagged-dates
                 :dates-packed lagged-packed-dates}))

  (tcc/typeof (:dates lagged-dates-ds))
  ;; => :local-date

  (tcc/typeof (:dates-packed lagged-dates-ds))
  ;; => :local-date

  ;; answer: types preserved by our lag method 

  ;; check 3: old rolling lag and datatypes. 
  (def tc-lagged-ds
    (tc/lag (tc/dataset {:dates dates})
            :dates
            1))

  tc-lagged-ds
  ;; => _unnamed [2 2]:
  ;;    |     :dates | :dates-lag-1 |
  ;;    |------------|--------------|
  ;;    | 2020-01-01 |              |
  ;;    | 2020-01-02 |   2020-01-01 |

  (tcc/typeof (:dates-lag-1 tc-lagged-ds))
  ;; => :packed-local-date

  ;; **Check 4:** try `:int64`, `:float32`, `:boolean`, and `:string`, with a
  ;; **missing source value and `k >= n`.
  (defn test-lag [c k]
    (let [lagged (dtype/sub-buffer (col/prepend-column-with-empty c k) 0 (count c))]
      {:data    (vec lagged)
       :dtype   (dtype/get-datatype lagged)
       :missing (col/missing lagged)}))

  (def c-int (col/new-column :a [1 2 nil 4]))
  (def c-float (col/new-column :a [1.0 2.0 nil 4.0]))
  (def c-bool (col/new-column :a [true false nil true]))
  (def c-str (col/new-column :a ["a" "b" nil "d"]))

  (test-lag c-int 4)
  ;; => {:data [nil nil nil nil], :dtype :int64, :missing {0,1,2,3}}
  (test-lag c-float 4)
  ;; => {:data [nil nil nil nil], :dtype :float64, :missing {0,1,2,3}}
  (test-lag c-bool 4)
  ;; => {:data [nil nil nil nil], :dtype :boolean, :missing {0,1,2,3}}
  (test-lag c-str 4)
  ;; => {:data [nil nil nil nil], :dtype :string, :missing {0,1,2,3}}

  ;; answer: when k >= n, every value is shifted out of range and the whole
  ;; column becomes missing, but the datatype is still preserved correctly
  ;; regardless of the original missing value or the source type 

;; why did the old rolling lag retrurn :packed-local-date and my test :local-date?
  (dtype/get-datatype (:dates-packed lagged-dates-ds))
  ;; => :local-date

  (tcc/typeof (:dates (tc/dataset {:dates dates})))
  ;; => :packed-local-date

  (dtype/get-datatype lagged-packed-dates)
  ;; => :local-date

  (dtype/get-datatype dates)
  ;; => :packed-local-date

  (dtype/get-datatype (col/prepend-column-with-empty
                       (tcc/column dates)
                       1))
  ;; => :local-date

  (def not-repacked
    (dtype/sub-buffer (col/prepend-column-with-empty dates 1) 0 (count dates)))

  (def repacked
    (packing/pack
     (dtype/sub-buffer (col/prepend-column-with-empty dates 1) 0 (count dates))))

  (dtype/get-datatype not-repacked)
  ;; => :local-date

  (dtype/get-datatype repacked)
  ;; => :packed-local-date

  (tcc/column? not-repacked)
  ;; => true

  (tcc/column? repacked)
  ;; => false

  (col/missing not-repacked)
  ;; => {0}

  (col/missing repacked)
  ;; => {}

  (def rebuilt
    (col/new-column :d repacked {} (col/missing not-repacked)))

  (dtype/get-datatype rebuilt)
  ;; => :packed-local-date

  (tcc/column? rebuilt)
  ;; => true

  (col/missing rebuilt)
  ;; => {0}

  (vec rebuilt)
  ;; => [nil #object[java.time.LocalDate 0x3f78530f "2020-01-01"]]

  ;; looks good. but is there a performance hit. col/new-column does a copy?
  ;; answer: no if all four-variant approach is used it just wraps. 

;; Comparint prior art in tablecloth.time. 

  ;; Performance only. Both take an existing column `c` and shift `k`,
  ;; returning n rows. The source is built ONCE, outside the timing.
  ;; (def src (col/new-column :a (double-array (range 1000000))))
  (def src
    (col/new-column :a (mapv #(java.time.LocalDate/ofEpochDay (+ 18262 %))
                             (range 1000000))))

  (defn tct-lag-method [c k]
    (tcc/column (concat (repeat k nil) (take (- (count c) k) c))))

  (defn new-method [c k]
    (dtype/sub-buffer (col/prepend-column-with-empty c k) 0 (count c)))

  ;; sanity: same length
  (count (tct-lag-method src 1))

  (count (new-method src 1))

  ;; now benchmark both methods (force-last reads the last row so a lazy
  ;; result can't skip work)
  (crit/quick-bench (force-last (tct-lag-method src 1)) :samples 30)

  (crit/quick-bench (force-last (new-method src 1)) :samples 30))

