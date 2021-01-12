(ns tablecloth.api.dataset-test
  (:require [tablecloth.api :as api]
            [tablecloth.common-test :refer [DS]]
            [clojure.test :refer [deftest is are]]
            [clojure.java.io :as io]))

(deftest dataset?
  (is (api/dataset? (api/dataset)))
  (is (api/dataset? DS))
  (is (not (api/dataset? {}))))

(deftest empty-ds
  (is (api/empty-ds? (api/dataset)))
  (is (api/empty-ds? (api/dataset {:a []})))
  (is (not (api/empty-ds? DS))))

(deftest dataset-creation
  (is (and (zero? (api/row-count (api/dataset)))
           (zero? (api/column-count (api/dataset)))))
  (is (= [999] (:$value (api/dataset 999))))
  (is (= [999] (get (api/dataset 999 {:single-value-column-name "my-single-value"}) "my-single-value")))
  (is (= [[999] "Single value"] ((juxt #(get % 0) api/dataset-name)
                                 (api/dataset 999 {:single-value-column-name ""
                                                   :dataset-name "Single value"}))))
  (are [res input] (= res (-> (api/dataset input)
                              (api/columns :as-map)))
    {:A [33] :B [5] :C [:a]} [[:A 33] [:B 5] [:C :a]]
    {:A [1 2 3 4 5 6]
     :B (repeat 6 "X")
     :C (repeat 6 :a)} [[:A [1 2 3 4 5 6]] [:B "X"] [:C :a]]
    {:A [33]} {:A 33}
    {:A [1 2 3]} {:A [1 2 3]}
    {:A [3 4 5] :B (repeat 3 "X")} {:A [3 4 5] :B "X"}
    {:a [1 99] :b [3 2]} [{:a 1 :b 3} {:b 2 :a 99}]
    {:a [1 2] :b [[1 2 3] [3 4]]} [{:a 1 :b [1 2 3]} {:a 2 :b [3 4]}]
    {:a [nil 3 11] :b [1 4 nil]} [{:a nil :b 1} {:a 3 :b 4} {:a 11}])
  (is (= [5 5] (-> (api/dataset "data/family.csv")
                   (api/shape))))
  (is (= [1461 6] (-> (api/dataset "https://vega.github.io/vega-lite/examples/data/seattle-weather.csv")
                      (api/shape)))))

(deftest saving
  (is (do (api/write! DS "DS.tsv.gz")
          (.exists (io/file "DS.tsv.gz"))))
  (is (-> (api/dataset "DS.tsv.gz")
          (api/dataset?)))
  (is (do (api/write! DS "DS.csv.gz")
          (.exists (io/file "DS.csv.gz"))))
  (is (-> (api/dataset "DS.csv.gz")
          (api/dataset?)))
  (is (do (api/write-nippy! DS "DS.nippy.gz")
          (.exists (io/file "DS.nippy.gz"))))
  (is (-> (api/read-nippy "DS.nippy.gz")
          (api/dataset?))))

(deftest dataset-shape
  (is (= [9 4] (api/shape DS)))
  (is (= 9 (api/row-count DS)))
  (is (= 4 (api/column-count DS))))

(deftest dataset-name
  (is (= "DS" (api/dataset-name DS))))

(deftest dataset-info
  (is (= {:col-name [:V1 :V2 :V3 :V4]
          :datatype [:int64 :int64 :float64 :string]}
         (-> (api/info DS)
             (select-keys [:col-name :datatype]))))
  (is (= '(:col-name :datatype :n-valid :n-missing :min :mean :mode :max :standard-deviation :skew :first :last)
         (-> (api/info DS)
             (api/column-names))))
  (is (= [{:name "DS", :columns 4, :rows 9, :grouped? false}]
         (-> (api/info DS :basic)
             (api/rows :as-maps))))
  (is (= [{:name :V1, :n-elems 9, :categorical? nil, :datatype :int64}
          {:name :V2, :n-elems 9, :categorical? nil, :datatype :int64}
          {:name :V3, :n-elems 9, :categorical? nil, :datatype :float64}
          {:name :V4, :n-elems 9, :categorical? true, :datatype :string}]
         (-> (api/info DS :columns)
             (api/rows :as-maps)))))

(deftest as-double-arrays
  (are [f v] (= v (-> (api/dataset {:a [1 2 3]
                                    :b [5 6 7]})
                      (f :as-double-arrays)
                      (->> (map seq))))
    api/rows '((1.0 5.0) (2.0 6.0) (3.0 7.0))
    api/columns '((1.0 2.0 3.0) (5.0 6.0 7.0))))

