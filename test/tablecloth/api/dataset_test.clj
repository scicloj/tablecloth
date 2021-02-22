(ns tablecloth.api.dataset-test
  (:require [tablecloth.api :as api]
            [tablecloth.common-test :refer [DS]]
            [clojure.java.io :as io]
            [midje.sweet :refer [tabular fact =>]]))

(fact "dataset?"
      (fact (api/dataset? (api/dataset)) => true)
      (fact (api/dataset? DS) => true)
      (fact (api/dataset? {}) => false))

(fact "empty-ds"
      (fact (api/empty-ds? (api/dataset)) => true)
      (fact (api/empty-ds? (api/dataset {:a []})) => true)
      (fact (api/empty-ds? DS) => false))

(fact "dataset-creation"
      (fact (and (zero? (api/row-count (api/dataset)))
                 (zero? (api/column-count (api/dataset)))) => true)
      (fact (:$value (api/dataset 999))
            => [999])
      (fact (get (api/dataset 999 {:single-value-column-name "my-single-value"}) "my-single-value")
            => [999])
      (fact ((juxt #(get % 0) api/dataset-name)
             (api/dataset 999 {:single-value-column-name ""
                               :dataset-name             "Single value"}))
            => [[999] "Single value"])
      (tabular (fact (-> (api/dataset ?input)
                         (api/columns :as-map))
                     => ?res)
               ?res ?input
               {:A [33] :B [5] :C [:a]}    [[:A 33] [:B 5] [:C :a]]
               {:A [1 2 3 4 5 6]
                :B (repeat 6 "X")
                :C (repeat 6 :a)}       [[:A [1 2 3 4 5 6]] [:B "X"] [:C :a]]
               {:A [33]}           {:A 33}
               {:A [1 2 3]}          {:A [1 2 3]}
               {:A [3 4 5] :B (repeat 3 "X")} {:A [3 4 5] :B "X"}
               {:a [1 99] :b [3 2]}      [{:a 1 :b 3} {:b 2 :a 99}]
               {:a [1 2] :b [[1 2 3] [3 4]]} [{:a 1 :b [1 2 3]} {:a 2 :b [3 4]}]
               {:a [nil 3 11] :b [1 4 nil]}  [{:a nil :b 1} {:a 3 :b 4} {:a 11}])
      (fact (-> (api/dataset "data/family.csv")
                (api/shape))
            => [5 5])
      (fact (-> (api/dataset "https://vega.github.io/vega-lite/examples/data/seattle-weather.csv")
                (api/shape))
            => [1461 6]))

(fact "saving"
      (fact (do (api/write! DS "DS.tsv.gz")
                (.exists (io/file "DS.tsv.gz")))
            => true)
      (fact (-> (api/dataset "DS.tsv.gz")
                (api/dataset?))
            => true)
      (fact (do (api/write! DS "DS.csv.gz")
                (.exists (io/file "DS.csv.gz")))
            => true)
      (fact (-> (api/dataset "DS.csv.gz")
                (api/dataset?))
            => true)
      (fact (do (api/write-nippy! DS "DS.nippy.gz")
                (.exists (io/file "DS.nippy.gz")))
            => true)
      (fact (-> (api/read-nippy "DS.nippy.gz")
                (api/dataset?))
            => true))

(fact "dataset-shape"
      (fact (api/shape DS)
            => [9 4])
      (fact (api/row-count DS)
            => 9)
      (fact (api/column-count DS)
            => 4))

(fact "dataset-name"
      (fact (api/dataset-name DS)
            => "DS"))

(fact "dataset-info"
      (fact
       (-> (api/info DS)
           (select-keys [:col-name :datatype]))
       => {:col-name [:V1 :V2 :V3 :V4]
           :datatype [:int64 :int64 :float64 :string]})
      (fact
       (-> (api/info DS)
           (api/column-names))
       => '(:col-name :datatype :n-valid :n-missing :min :mean :mode :max :standard-deviation :skew :first :last))
      (fact
       (-> (api/info DS :basic)
           (api/rows :as-maps))
       => [{:name "DS", :columns 4, :rows 9, :grouped? false}])
      (fact
       (-> (api/info DS :columns)
           (api/rows :as-maps))
       => [{:name :V1, :n-elems 9, :categorical? nil, :datatype :int64}
           {:name :V2, :n-elems 9, :categorical? nil, :datatype :int64}
           {:name :V3, :n-elems 9, :categorical? nil, :datatype :float64}
           {:name :V4, :n-elems 9, :categorical? true, :datatype :string}]))

(fact "as-double-arrays"
      (tabular (fact (-> (api/dataset {:a [1 2 3]
                                       :b [5 6 7]})
                         (?f :as-double-arrays)
                         (->> (map seq)))
                     = ?v)
               ?f ?v
               api/rows  '((1.0 5.0) (2.0 6.0) (3.0 7.0))
               api/columns '((1.0 2.0 3.0) (5.0 6.0 7.0))))

