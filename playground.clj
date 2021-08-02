(ns playground
  (:require [tablecloth.api :as api]
            [tech.v3.datatype :as dtype]
            [clojure.set :as set]
            [tablecloth.api.operators :as op]

            [tech.v3.datatype.functional :as fun]
            [tech.v3.datatype.datetime :as datetime]
            [tech.v3.datatype.argops :as argops]

            [tablecloth.api.utils :refer [grouped? process-group-data]]
            [tablecloth.api.columns :refer [add-column select-columns update-columns]]
            [tablecloth.api.dataset :refer [columns]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; ops
;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ds (api/dataset "data/iris.csv"))
(def gds (api/group-by ds "Species"))

;; column -> R
(api/aggregate-columns ds :type/numerical op/sum)
;; => _unnamed [1 4]:
;;    | Sepal.Length | Sepal.Width | Petal.Length | Petal.Width |
;;    |-------------:|------------:|-------------:|------------:|
;;    |        876.5 |       458.6 |        563.7 |       179.9 |

(api/aggregate-columns gds :type/numerical op/sum)
;; => _unnamed [3 5]:
;;    | Petal.Width | Sepal.Length | Sepal.Width | Petal.Length | :$group-name |
;;    |------------:|-------------:|------------:|-------------:|--------------|
;;    |        12.3 |        250.3 |       171.4 |         73.1 |       setosa |
;;    |        66.3 |        296.8 |       138.5 |        213.0 |   versicolor |
;;    |       101.3 |        329.4 |       148.7 |        277.6 |    virginica |

;; column -> column
;; we need something easier like map-columns here where all columns are passed to the operator:
(api/add-column ds :result (op/cumsum (ds "Sepal.Length")))
;; (api/function-name ds :target-column "Sepal.Length" op/cumsum)
(api/add-column ds :result (apply op/+ (-> (api/select-columns ds :type/numerical) api/columns)))
;; (api/function-name ds :target-column :type/numerical op/+) ;; <--- store sum of the numerical columns in a column

;; doesn't work!!!
(api/add-column gds :result (op/cumsum (ds "Sepal.Length")))


;; columns -> R
(api/aggregate ds {:length-correlation #(op/pearsons-correlation (% "Sepal.Length")
                                                                 (% "Petal.Length"))
                   :width-correlation #(op/pearsons-correlation (% "Sepal.Width")
                                                                (% "Petal.Width"))})
;; => _unnamed [1 2]:
;;    | :length-correlation | :width-correlation |
;;    |--------------------:|-------------------:|
;;    |          0.87175378 |        -0.36612593 |

(api/aggregate gds {:length-correlation #(op/pearsons-correlation (% "Sepal.Length")
                                                                  (% "Petal.Length"))
                    :width-correlation #(op/pearsons-correlation (% "Sepal.Width")
                                                                 (% "Petal.Width"))})
;; => _unnamed [3 3]:
;;    | :length-correlation | :width-correlation | :$group-name |
;;    |--------------------:|-------------------:|--------------|
;;    |          0.26717576 |         0.23275201 |       setosa |
;;    |          0.75404896 |         0.66399872 |   versicolor |
;;    |          0.86422473 |         0.53772803 |    virginica |

(defmacro make-columnar-ops
  [& ops]
  `(do ~@(for [op ops]
           (let [s (symbol (name op))]
             `(defn ~s
                ([~'column] (~op ~'column))
                ([~'ds ~'target-column ~'column-selector] (~s ~'ds ~'target-column ~'column-selector {}))
                ([~'ds ~'target-column ~'column-selector ~'options]
                 (if (grouped? ~'ds)
                   (process-group-data ~'ds #(~s % ~'target-column ~'column-selector ~'options) (:parallel? ~'options))
                   (add-column ~'ds ~'target-column (apply ~op (-> ~'ds (select-columns ~'column-selector) columns))))))))))



(make-columnar-ops op/= op/not= op/* op/tan op/quartiles)

(op/quartiles (ds "Sepal.Length"))

(-> (tan ds :result "Sepal.Length") api/head)
;; => data/iris.csv [5 6]:
;;    | Sepal.Length | Sepal.Width | Petal.Length | Petal.Width | Species |     :result |
;;    |-------------:|------------:|-------------:|------------:|---------|------------:|
;;    |          5.1 |         3.5 |          1.4 |         0.2 |  setosa | -2.44938942 |
;;    |          4.9 |         3.0 |          1.4 |         0.2 |  setosa | -5.26749307 |
;;    |          4.7 |         3.2 |          1.3 |         0.2 |  setosa | 80.71276297 |
;;    |          4.6 |         3.1 |          1.5 |         0.2 |  setosa |  8.86017490 |
;;    |          5.0 |         3.6 |          1.4 |         0.2 |  setosa | -3.38051501 |


;; how to multiply by 2?????????????????????

(-> (* ds :sum-of-numerical-columns :type/numerical) api/head)
;; => data/iris.csv [5 6]:
;;    | Sepal.Length | Sepal.Width | Petal.Length | Petal.Width | Species | :sum-of-numerical-columns |
;;    |-------------:|------------:|-------------:|------------:|---------|--------------------------:|
;;    |          5.1 |         3.5 |          1.4 |         0.2 |  setosa |                      10.2 |
;;    |          4.9 |         3.0 |          1.4 |         0.2 |  setosa |                       9.5 |
;;    |          4.7 |         3.2 |          1.3 |         0.2 |  setosa |                       9.4 |
;;    |          4.6 |         3.1 |          1.5 |         0.2 |  setosa |                       9.4 |
;;    |          5.0 |         3.6 |          1.4 |         0.2 |  setosa |                      10.2 |

(op/* (ds "Sepal.Length") 2)

(-> (api/update-columns ds :type/numerical (partial op/* 2)) api/head)
;; => data/iris.csv [5 5]:
;;    | Sepal.Length | Sepal.Width | Petal.Length | Petal.Width | Species |
;;    |-------------:|------------:|-------------:|------------:|---------|
;;    |          5.1 |         3.5 |          1.4 |         0.2 |  setosa |
;;    |         10.0 |         6.5 |          2.8 |         0.4 |  setosa |
;;    |         14.7 |         9.7 |          4.1 |         0.6 |  setosa |
;;    |         19.3 |        12.8 |          5.6 |         0.8 |  setosa |
;;    |         24.3 |        16.4 |          7.0 |         1.0 |  setosa |





















;;

(-> "https://www.travishinkelman.com/data/gapminder.csv"
    (api/dataset {:key-fn keyword})
    (api/select-rows
     (fn [row]
       (and
        (= (:year row) 2007)
        (= (:country row) "United States"))))
    api/head)
;; => https://www.travishinkelman.com/data/gapminder.csv [1 6]:
;;    |      :country | :continent | :year | :lifeExp |      :pop |  :gdpPercap |
;;    |---------------|------------|------:|---------:|----------:|------------:|
;;    | United States |   Americas |  2007 |   78.242 | 301139947 | 42951.65309 |

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(import [java.util TreeMap])
(import java.util.LinkedHashMap)

(defn random-datetime []
  (datetime/milliseconds->datetime :local-date-time
                                   (* 1000 (rand-int 999999999))))

(def n 10000)

(random-datetime)


(def ds
  (-> {:datetime (repeatedly n random-datetime)
       :x        (repeatedly n rand)}
      (tablecloth/dataset)
      (tablecloth/order-by :datetime)
      (tablecloth/add-or-replace-column :i (range))))


(tablecloth/head ds)
;; => _unnamed [5 3]:
;;    |           :datetime |         :x | :i |
;;    |---------------------|------------|----|
;;    | 1970-01-02T17:23:41 | 0.58541705 |  0 |
;;    | 1970-01-02T21:33:29 | 0.94620430 |  1 |
;;    | 1970-01-04T14:32:10 | 0.63106419 |  2 |
;;    | 1970-01-05T20:45:05 | 0.23300722 |  3 |
;;    | 1970-01-06T01:28:25 | 0.61134539 |  4 |



(def milliseconds-in-an-day (* 24 60 60 1000))



(defn row-numbers-of-last-few-days-by-argfilter [reference-datetime num-days]
  (let [end (datetime/local-date-time->milliseconds-since-epoch reference-datetime)
        start (- end (* num-days milliseconds-in-an-day))]
    (->> ds
         :datetime
         (argops/argfilter (fn [datetime]
                             (<= start
                                 (datetime/local-date-time->milliseconds-since-epoch
                                  datetime)
                                 end))))))


(def datetime20
  (-> ds
      :datetime
      (nth 20)))

(row-numbers-of-last-few-days-by-argfilter
 datetime20
 5)

(def index
  (LinkedHashMap. ^java.util.Map
                  (zipmap (dtype/emap datetime/local-date-time->milliseconds-since-epoch
                                      :int64
                                      (:datetime ds))
                          (range))))


(defn compute-with-time-measurement [f]
  (let [start-time (datetime/instant)
        result (f)
        end-time (datetime/instant)]
    {:result result
     :duration (datetime/between start-time end-time :milliseconds)}))

(compute-with-time-measurement
 (fn []
   (->> ds
        :datetime
        (dtype/emap (fn [datetime]
                      (let [row-numbers (row-numbers-of-last-few-days-by-argfilter
                                         datetime
                                         5)]
                        (-> ds
                            (tablecloth/select-rows row-numbers)
                            :x
                            fun/mean)))
                    :float64)
        fun/standard-deviation)))

(defn row-numbers-of-last-few-days-by-index [reference-datetime num-days]
  (let [end   (datetime/local-date-time->milliseconds-since-epoch
               reference-datetime)
        start (- end
                 (* num-days milliseconds-in-an-day))]
    (-> (.subMap ^LinkedHashMap
                 index
                 start
                 true
                 end
                 true)
        (.values))))

#_(compute-with-time-measurement
   (fn []
     (->> ds
          :datetime
          (dtype/emap (fn [datetime]
                        (let [row-numbers (row-numbers-of-last-few-days-by-index
                                           datetime
                                           5)]
                          (-> ds
                              (tablecloth/select-rows row-numbers)
                              :x
                              fun/mean)))
                      :float64)
          fun/standard-deviation)))

#_(time (let [z ( datetime/local-date-time->milliseconds-since-epoch
                 (:datetime ds))]
          (reduce + z)))


;;

(def n 99)

(def ds
  (-> {:i (range n)
       :g (repeatedly n #([:A :B :C :D :E :F :G :H :I] (rand-int 9)))
       :x (repeatedly n rand)}
      tablecloth/dataset))

ds

(tablecloth/group-by ds [:g])

(let [m (LinkedHashMap.)]
  (doseq [[grp row-numbers] (->> (tablecloth/rows ds :as-maps)
                                 (group-by :g)
                                 (sort-by key)
                                 (map (fn [[g rows]]
                                        [{:g g} (->> rows
                                                     (mapv :i))])))]
    (.put ^java.util.Map m grp row-numbers))
  (-> ds
      (tablecloth/group-by m)
      (tablecloth/aggregate {:x-mean #(-> %
                                          :x
                                          tech.v3.datatype.functional/mean)})))

(-> ds
    (tablecloth/group-by :g)
    (tablecloth/without-grouping->
     (tablecloth/order-by (comp :name))))
;; => _unnamed [9 3]:
;;    | :name | :group-id |             :data |
;;    |-------|-----------|-------------------|
;;    |    :A |         8 | Group: :A [12 3]: |
;;    |    :B |         6 | Group: :B [14 3]: |
;;    |    :C |         7 | Group: :C [12 3]: |
;;    |    :D |         3 | Group: :D [14 3]: |
;;    |    :E |         2 | Group: :E [11 3]: |
;;    |    :F |         5 |  Group: :F [6 3]: |
;;    |    :G |         4 |  Group: :G [7 3]: |
;;    |    :H |         0 |  Group: :H [8 3]: |
;;    |    :I |         1 | Group: :I [15 3]: |

;; => _unnamed [9 3]:
;;    |   :name | :group-id |                  :data |
;;    |---------|-----------|------------------------|
;;    | {:g :A} |         8 | Group: {:g :A} [12 3]: |
;;    | {:g :B} |         5 | Group: {:g :B} [14 3]: |
;;    | {:g :C} |         7 | Group: {:g :C} [12 3]: |
;;    | {:g :D} |         4 | Group: {:g :D} [14 3]: |
;;    | {:g :E} |         2 | Group: {:g :E} [11 3]: |
;;    | {:g :F} |         6 |  Group: {:g :F} [6 3]: |
;;    | {:g :G} |         3 |  Group: {:g :G} [7 3]: |
;;    | {:g :H} |         0 |  Group: {:g :H} [8 3]: |
;;    | {:g :I} |         1 | Group: {:g :I} [15 3]: |

;; => (2 12 28 31 32 52 55 61 63 66 70 79 87 88)

;; => _unnamed [9 4]:
;;    | :name | :group-id |         :indexes |             :data |
;;    |-------|-----------|------------------|-------------------|
;;    |    :A |         8 | #list<int32>[14] | Group: :A [14 3]: |
;;    |    :B |         6 |  #list<int32>[9] |  Group: :B [9 3]: |
;;    |    :C |         7 | #list<int32>[14] | Group: :C [14 3]: |
;;    |    :D |         3 |  #list<int32>[9] |  Group: :D [9 3]: |
;;    |    :E |         2 |  #list<int32>[7] |  Group: :E [7 3]: |
;;    |    :F |         5 | #list<int32>[13] | Group: :F [13 3]: |
;;    |    :G |         4 |  #list<int32>[9] |  Group: :G [9 3]: |
;;    |    :H |         0 | #list<int32>[13] | Group: :H [13 3]: |
;;    |    :I |         1 | #list<int32>[11] | Group: :I [11 3]: |

;; => _unnamed [9 3]:
;;    | :name | :group-id |             :data |
;;    |-------|-----------|-------------------|
;;    |    :A |         8 | Group: :A [14 3]: |
;;    |    :B |         6 |  Group: :B [9 3]: |
;;    |    :C |         7 | Group: :C [14 3]: |
;;    |    :D |         3 |  Group: :D [9 3]: |
;;    |    :E |         2 |  Group: :E [7 3]: |
;;    |    :F |         5 | Group: :F [13 3]: |
;;    |    :G |         4 |  Group: :G [9 3]: |
;;    |    :H |         0 | Group: :H [13 3]: |
;;    |    :I |         1 | Group: :I [11 3]: |
;; => _unnamed [9 3]:
;;    |   :name | :group-id |                  :data |
;;    |---------|-----------|------------------------|
;;    | {:g :A} |         8 | Group: {:g :A} [14 3]: |
;;    | {:g :B} |         5 |  Group: {:g :B} [9 3]: |
;;    | {:g :C} |         7 | Group: {:g :C} [14 3]: |
;;    | {:g :D} |         4 |  Group: {:g :D} [9 3]: |
;;    | {:g :E} |         2 |  Group: {:g :E} [7 3]: |
;;    | {:g :F} |         6 | Group: {:g :F} [13 3]: |
;;    | {:g :G} |         3 |  Group: {:g :G} [9 3]: |
;;    | {:g :H} |         0 | Group: {:g :H} [13 3]: |
;;    | {:g :I} |         1 | Group: {:g :I} [11 3]: |


;;


(def ds-with-missing (api/dataset {:ints [1 2 nil 3 4 nil 6 nil nil nil nil nil]
                                   :doubles [1.0 0.0 nil -1 nil 3 4 nil nil nil nil nil]}))

ds-with-missing
;; => _unnamed [12 2]:
;;    |  :ints | :doubles |
;;    |--------|----------|
;;    |      1 |      1.0 |
;;    |      2 |      0.0 |
;;    |        |          |
;;    |      3 |     -1.0 |
;;    |      4 |          |
;;    |        |      3.0 |
;;    |      6 |      4.0 |
;;    |        |          |
;;    |        |          |
;;    |        |          |
;;    |        |          |
;;    |        |          |

(dtype/elemwise-datatype (ds-with-missing :ints))
;; => :int64

(.readLong (dtype/->reader (ds-with-missing :ints)) 2)
;; => -9223372036854775808

;; but...

(fun/* (ds-with-missing :ints)
       (ds-with-missing :ints))
;; => [1 4 0 9 16 0 36]

(fun/reduce-min (ds-with-missing :ints))
;; => -9223372036854775808

;; it's always read as doubles, so missing is `##NaN` and is skipped
(fun/descriptive-statistics (ds-with-missing :ints))
;; => {:min 1.0, :mean 3.2, :standard-deviation 1.9235384061671346, :max 6.0, :n-values 5}

;; also read as doubles
(fun/sum (ds-with-missing :ints))
;; => 16.0


;;

(defmacro timem
  [m & r]
  `(do (print ~m " - ")
       (time ~@r)))

(def ds (api/dataset {:a (range 10)}))

(defn heavy-duplication
  [v]
  (Thread/sleep 300)
  (+ v v))

(def ds2 (timem "creation" (api/map-columns ds :b :a heavy-duplication)))

(timem "evaluation1" (double-array (ds2 :b)))
(timem "evaluation2" (double-array (ds2 :b)))

(def ds3 (timem "another column" (api/map-columns ds2 :c :b heavy-duplication)))

(timem "evaluation3" (double-array (ds3 :c)))
(timem "evaluation4" (double-array (ds3 :c)))

(timem "adding two columns" (double-array (fun/+ (ds3 :b) (ds3 :c))))


;; pure clojure

(def a (range 10))

(def b (timem "creation" (map heavy-duplication a)))

(timem "evaluation1" (double-array b))
(timem "evaluation2" (double-array b))

(def c (timem "creation" (map heavy-duplication b)))

(timem "evaluation1" (double-array c))
(timem "evaluation2" (double-array c))

(timem "adding two columns" (double-array (map + b c)))


(clojure.lang.MapEntry. :a 1)

(class (first {:a 1}))


(def ds (api/dataset {:x (range 1 7)
                      :g1 [:A :A :A :B :B :B]
                      :g2 (take 6 (cycle [:C :D]))}))

ds
;; => _unnamed [6 3]:
;;    |     :x |      :g1 |      :g2 |
;;    |--------|----------|----------|
;;    |      1 |       :A |       :C |
;;    |      2 |       :A |       :D |
;;    |      3 |       :A |       :C |
;;    |      4 |       :B |       :D |
;;    |      5 |       :B |       :C |
;;    |      6 |       :B |       :D |

(def g1 (api/group-by ds [:g1]))
(def g2 (api/group-by ds [:g1 :g2]))

(-> (api/concat g1 g2)
    (api/mark-as-group)
    (api/aggregate-columns :x (partial reduce +)))
;; => null [6 3]:
;;    |      :g1 |     :x |      :g2 |
;;    |----------|--------|----------|
;;    |       :B |     15 |          |
;;    |       :A |      6 |          |
;;    |       :B |      5 |       :C |
;;    |       :A |      2 |       :D |
;;    |       :B |     10 |       :D |
;;    |       :A |      4 |       :C |


(-> (api/concat g1 g1)
    (api/mark-as-group)
    (api/ungroup))
;; => null [12 3]:
;;    |     :x |      :g1 |      :g2 |
;;    |--------|----------|----------|
;;    |      4 |       :B |       :D |
;;    |      5 |       :B |       :C |
;;    |      6 |       :B |       :D |
;;    |      1 |       :A |       :C |
;;    |      2 |       :A |       :D |
;;    |      3 |       :A |       :C |
;;    |      4 |       :B |       :D |
;;    |      5 |       :B |       :C |
;;    |      6 |       :B |       :D |
;;    |      1 |       :A |       :C |
;;    |      2 |       :A |       :D |
;;    |      3 |       :A |       :C |



(-> (api/concat g1 g1)
    (api/mark-as-group))
;; => null [4 3]:
;;    |           :name | :group-id |                  :data |
;;    |-----------------|-----------|------------------------|
;;    |        {:g1 :B} |         0 | Group: {:g1 :B} [3 3]: |
;;    |        {:g1 :A} |         1 | Group: {:g1 :A} [3 3]: |
;;    |        {:g1 :B} |         0 | Group: {:g1 :B} [3 3]: |
;;    |        {:g1 :A} |         1 | Group: {:g1 :A} [3 3]: |

(-> (api/concat g1 g2)
    (api/mark-as-group))
;; => null [6 3]:
;;    |            :name | :group-id |                          :data |
;;    |------------------|-----------|--------------------------------|
;;    |         {:g1 :B} |         0 |         Group: {:g1 :B} [3 3]: |
;;    |         {:g1 :A} |         1 |         Group: {:g1 :A} [3 3]: |
;;    | {:g1 :B, :g2 :C} |         0 | Group: {:g1 :B, :g2 :C} [1 3]: |
;;    | {:g1 :A, :g2 :D} |         1 | Group: {:g1 :A, :g2 :D} [1 3]: |
;;    | {:g1 :B, :g2 :D} |         2 | Group: {:g1 :B, :g2 :D} [2 3]: |
;;    | {:g1 :A, :g2 :C} |         3 | Group: {:g1 :A, :g2 :C} [2 3]: |
;;


(def ds (api/dataset {:a (repeatedly 8 #(format "%.2f" (- (* 10 (rand)) 5)))}))

ds
;; => _unnamed [8 1]:
;;    |      :a |
;;    |---------|
;;    |    2.55 |
;;    |   -3.85 |
;;    |    3.42 |
;;    |   -2.42 |
;;    |   -0.57 |
;;    |    4.81 |
;;    |    0.62 |
;;    |   -2.92 |

(api/separate-column ds :a [:credit :debit] (fn [v]
                                              (let [ve (Double/parseDouble v)]
                                                (if (pos? ve)
                                                  [ve 0,0]
                                                  [0.0 (- ve)]))))
;; => _unnamed [8 2]:
;;    |  :credit |   :debit |
;;    |----------|----------|
;;    |     2.55 |     0.00 |
;;    |     0.00 |     3.85 |
;;    |     3.42 |     0.00 |
;;    |     0.00 |     2.42 |
;;    |     0.00 |     0.57 |
;;    |     4.81 |     0.00 |
;;    |     0.62 |     0.00 |
;;    |     0.00 |     2.92 |

(-> ds
    (api/convert-types :a :float64)
    (api/separate-column :a [:credit :debit] (fn [v]
                                               (if (pos? v)
                                                 [v 0.0]
                                                 [0.0 (- v)])) {:drop-column? false}))
;; => _unnamed [8 3]:
;;    |       :a |  :credit |   :debit |
;;    |----------|----------|----------|
;;    |     2.55 |     2.55 |     0.00 |
;;    |    -3.85 |     0.00 |     3.85 |
;;    |     3.42 |     3.42 |     0.00 |
;;    |    -2.42 |     0.00 |     2.42 |
;;    |    -0.57 |     0.00 |     0.57 |
;;    |     4.81 |     4.81 |     0.00 |
;;    |     0.62 |     0.62 |     0.00 |
;;    |    -2.92 |     0.00 |     2.92 |

(api/update-columns ds :a (partial map (fn [v] (Double/parseDouble v))))
;; => _unnamed [8 1]:
;;    |      :a |
;;    |---------|
;;    |   2.550 |
;;    |  -3.850 |
;;    |   3.420 |
;;    |  -2.420 |
;;    | -0.5700 |
;;    |   4.810 |
;;    |  0.6200 |
;;    |  -2.920 |


;;;;

(require '[clojure.set])

(defprotocol IndexProto
  (slice-idx [idx ks] [idx from to] [idx from from-inclusive? to to-inclusive?]
    "Slice by keys or range")
  (select-by-idx [idx ks] [idx from to]
    "Select by keys or range"))

;; TreeMap as an index
(extend-type TreeMap
  IndexProto
  (slice-idx
    ([idx ks]
     (let [^TreeMap nidx (.clone ^TreeMap idx)
           s (clojure.set/difference (set (.keySet nidx)) (set ks))]
       (doseq [k s]
         (.remove nidx k))
       nidx))
    ([idx from to]
     (slice-idx idx from true to true))
    ([idx from from-inclusive? to to-inclusive?]
     (.subMap ^TreeMap idx from from-inclusive? to to-inclusive?)))
  (select-by-idx
    ([idx from to]
     (select-by-idx idx (keys (slice-idx idx from to))))
    ([idx ks]
     (mapcat identity (map #(.get ^TreeMap idx %) ks)))))

;; build index using group-by path and Clojure comparator to allow `nil` and vector based keys
(defn build-index
  ([ds grouping] (build-index ds grouping compare))
  ([ds grouping comparator]
   (let [g (api/group-by ds grouping {:result-type :as-indexes})
         ^TreeMap tm (TreeMap. comparator)]
     (.putAll tm g)
     tm)))

;; test dataset with missing values
(def ds (api/dataset {:id (repeatedly 30 #(when (< (rand) 0.8)
                                            (rand-int 5)))
                      :v (repeatedly 30 #(rand-nth [:a :b :c :d :e]))}))


ds;; => _unnamed [30 2]:
;;    |    :id |       :v |
;;    |--------|----------|
;;    |        |       :c |
;;    |      3 |       :b |
;;    |      2 |       :e |
;;    |      3 |       :b |
;;    |      0 |       :c |
;;    |      0 |       :b |
;;    |        |       :b |
;;    |      4 |       :d |
;;    |      0 |       :d |
;;    |      1 |       :e |
;;    |      2 |       :a |
;;    |      0 |       :b |
;;    |      4 |       :c |
;;    |      1 |       :d |
;;    |      3 |       :b |
;;    |      2 |       :c |
;;    |      1 |       :a |
;;    |      4 |       :e |
;;    |      1 |       :b |
;;    |        |       :a |
;;    |      2 |       :b |
;;    |      1 |       :d |
;;    |      4 |       :d |
;;    |      4 |       :b |
;;    |      0 |       :b |


;; build index
(def idx (build-index ds :id))

idx
;; => {nil #list<int32>[4]
;;    [0, 6, 19, 29], 0 #list<int32>[5]
;;    [4, 5, 8, 11, 24], 1 #list<int32>[5]
;;    [9, 13, 16, 18, 21], 2 #list<int32>[5]
;;    [2, 10, 15, 20, 26], 3 #list<int32>[4]
;;    [1, 3, 14, 28], 4 #list<int32>[7]
;;    [7, 12, 17, 22, 23, 25, 27]}

(slice-idx idx 0 1)
;; => {0 #list<int32>[5]
;;    [4, 5, 8, 11, 24], 1 #list<int32>[5]
;;    [9, 13, 16, 18, 21]}

(slice-idx idx [2 4 0])
;; => {0 #list<int32>[5]
;;    [4, 5, 8, 11, 24], 2 #list<int32>[5]
;;    [2, 10, 15, 20, 26], 4 #list<int32>[7]
;;    [7, 12, 17, 22, 23, 25, 27]}

(select-by-idx idx 0 1)
;; => (4 5 8 11 24 9 13 16 18 21)

(select-by-idx idx [2 4 0])
;; => (2 10 15 20 26 7 12 17 22 23 25 27 4 5 8 11 24)

;; custom function to select rows using an index
(defn select-by
  ([ds idx from to]
   (api/select-rows ds (mapcat identity (vals (slice-idx idx from to)))))
  ([ds idx ks]
   (api/select-rows ds (select-by-idx idx ks))))

(select-by ds idx 2 3)
;; => _unnamed [9 2]:
;;    |    :id |       :v |
;;    |--------|----------|
;;    |      2 |       :e |
;;    |      2 |       :a |
;;    |      2 |       :c |
;;    |      2 |       :b |
;;    |      2 |       :a |
;;    |      3 |       :b |
;;    |      3 |       :b |
;;    |      3 |       :b |
;;    |      3 |       :e |

(select-by ds idx [0 3])
;; => _unnamed [9 2]:
;;    |    :id |       :v |
;;    |--------|----------|
;;    |      0 |       :c |
;;    |      0 |       :b |
;;    |      0 |       :d |
;;    |      0 |       :b |
;;    |      0 |       :b |
;;    |      3 |       :b |
;;    |      3 |       :b |
;;    |      3 |       :b |
;;    |      3 |       :e |

;; grouping works without any change
(api/group-by ds idx)
;; => _unnamed [6 3]:
;;    |  :name | :group-id |           :data |
;;    |--------|-----------|-----------------|
;;    |        |         0 |  Group:  [4 2]: |
;;    |      0 |         1 | Group: 0 [5 2]: |
;;    |      1 |         2 | Group: 1 [5 2]: |
;;    |      2 |         3 | Group: 2 [5 2]: |
;;    |      3 |         4 | Group: 3 [4 2]: |
;;    |      4 |         5 | Group: 4 [7 2]: |

(api/group-by ds (slice-idx idx [2 0]))
;; => _unnamed [2 3]:
;;    |  :name | :group-id |           :data |
;;    |--------|-----------|-----------------|
;;    |      0 |         0 | Group: 0 [5 2]: |
;;    |      2 |         1 | Group: 2 [5 2]: |

;;


;; index from both fields
(def idx2 (build-index ds (juxt :id :v)))

(select-by ds idx2 [nil :c] [2 :b])
;; => _unnamed [15 2]:
;;    |    :id |       :v |
;;    |--------|----------|
;;    |        |       :c |
;;    |        |       :e |
;;    |      0 |       :b |
;;    |      0 |       :b |
;;    |      0 |       :b |
;;    |      0 |       :c |
;;    |      0 |       :d |
;;    |      1 |       :a |
;;    |      1 |       :b |
;;    |      1 |       :d |
;;    |      1 |       :d |
;;    |      1 |       :e |
;;    |      2 |       :a |
;;    |      2 |       :a |
;;    |      2 |       :b |

(select-by ds idx2 [[1 :a] [2 :c] [nil :a]])
;; => _unnamed [3 2]:
;;    |    :id |       :v |
;;    |--------|----------|
;;    |      1 |       :a |
;;    |      2 |       :c |
;;    |        |       :a |


(api/group-by ds idx2)
;; => _unnamed [21 3]:
;;    |              :name | :group-id |                  :data |
;;    |--------------------|-----------|------------------------|
;;    |           [nil :a] |         0 | Group: [nil :a] [1 2]: |
;;    |           [nil :b] |         1 | Group: [nil :b] [1 2]: |
;;    |           [nil :c] |         2 | Group: [nil :c] [1 2]: |
;;    |           [nil :e] |         3 | Group: [nil :e] [1 2]: |
;;    |             [0 :b] |         4 |   Group: [0 :b] [3 2]: |
;;    |             [0 :c] |         5 |   Group: [0 :c] [1 2]: |
;;    |             [0 :d] |         6 |   Group: [0 :d] [1 2]: |
;;    |             [1 :a] |         7 |   Group: [1 :a] [1 2]: |
;;    |             [1 :b] |         8 |   Group: [1 :b] [1 2]: |
;;    |             [1 :d] |         9 |   Group: [1 :d] [2 2]: |
;;    |             [1 :e] |        10 |   Group: [1 :e] [1 2]: |
;;    |             [2 :a] |        11 |   Group: [2 :a] [2 2]: |
;;    |             [2 :b] |        12 |   Group: [2 :b] [1 2]: |
;;    |             [2 :c] |        13 |   Group: [2 :c] [1 2]: |
;;    |             [2 :e] |        14 |   Group: [2 :e] [1 2]: |
;;    |             [3 :b] |        15 |   Group: [3 :b] [3 2]: |
;;    |             [3 :e] |        16 |   Group: [3 :e] [1 2]: |
;;    |             [4 :b] |        17 |   Group: [4 :b] [2 2]: |
;;    |             [4 :c] |        18 |   Group: [4 :c] [1 2]: |
;;    |             [4 :d] |        19 |   Group: [4 :d] [2 2]: |
;;    |             [4 :e] |        20 |   Group: [4 :e] [2 2]: |

;;

;; build index for even/odd split
(def idx3 (build-index ds #(when-let [id (:id %)]
                             (even? id))))

;; even rows
(select-by ds idx3 [true])
;; => _unnamed [17 2]:
;;    |    :id |       :v |
;;    |--------|----------|
;;    |      2 |       :e |
;;    |      0 |       :c |
;;    |      0 |       :b |
;;    |      4 |       :d |
;;    |      0 |       :d |
;;    |      2 |       :a |
;;    |      0 |       :b |
;;    |      4 |       :c |
;;    |      2 |       :c |
;;    |      4 |       :e |
;;    |      2 |       :b |
;;    |      4 |       :d |
;;    |      4 |       :b |
;;    |      0 |       :b |
;;    |      4 |       :e |
;;    |      2 |       :a |
;;    |      4 |       :b |

;; odd rows
(select-by ds idx3 [false])
;; => _unnamed [9 2]:
;;    |    :id |       :v |
;;    |--------|----------|
;;    |      3 |       :b |
;;    |      3 |       :b |
;;    |      1 |       :e |
;;    |      1 |       :d |
;;    |      3 |       :b |
;;    |      1 |       :a |
;;    |      1 |       :b |
;;    |      1 |       :d |
;;    |      3 |       :e |
