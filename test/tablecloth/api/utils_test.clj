(ns tablecloth.api.utils-test
  (:require [tablecloth.api :as api]
            [tablecloth.api.utils :as sut]
            [tech.v3.dataset :as ds]
            [tech.v3.datatype :as dtype]
            [clojure.string :as str]
            [clojure.test :refer [deftest is are]]))

(def ds (api/dataset "data/who.csv.gz"))
(def dsg (api/group-by ds "country"))

(def ds-sn (api/dataset {[1 2 3] [1 2 3]
                         false ["false" "false" "false"]
                         true ["true" "true" "true"]
                         {:a 1 :b 2} [9 9 9]}))

(deftest iterable-sequence
  (are [x] (sut/iterable-sequence? x)
    [] '() #{}
    (ds "country")
    (java.util.ArrayList.)
    (dtype/const-reader 3 5))
  (are [x] (not (sut/iterable-sequence? x))
    nil {}))

(deftest ->str
  (are [x y] (= x (sut/->str y))
    "a" "a"
    "b" :a/b
    "c" 'a/c
    "b" :b
    "c" 'c))

(deftest column-names-regular
  (is (= (ds/column-names ds) (api/column-names ds)))
  (is (= (ds/column-names ds) (api/column-names ds :all)))
  (is (nil? (api/column-names ds nil)))
  (are [x y] (= x (api/column-names ds y))
    ["country" "iso2" "iso3"] :type/string
    ["country" "iso2" "iso3"] :!type/numerical
    ["year"] "year"
    ["year"] ["year"]
    ["iso2" "year"] #{"year" "iso2"}
    ["new_sp_m014" "country"] ["new_sp_m014" "country"]
    ["iso2" "iso3"] #(str/starts-with? % "iso")
    ["country"] {"country" nil} ;; only keys are used
    ["iso2" "iso3"] #"^i.*")
  (is (every? #(str/starts-with? % "new") (api/column-names ds (complement #{"country" "iso2" "iso3" "year"}))))
  (are [x y f] (= x (api/column-names ds y f))
    ["iso2" "iso3"] #{"iso2" "iso3"} :name
    ["country" "iso2" "iso3"] #{:string} :datatype
    ["new_sp_m014"] (fn [{:keys [name datatype]}]
                      (and (= datatype :int16)
                           (str/starts-with? name "new_sp_m"))) :all)
  (are [x y] (= x (api/column-names ds-sn y))
    [false] false?
    [true] true?
    [false] #{false}
    [true] #{true}
    [[1 2 3]] vector?
    [{:a 1 :b 2}] map?
    [[1 2 3] {:a 1 :b 2}] seqable?
    [false true] boolean?
    [[1 2 3] {:a 1 :b 2}] :type/numerical
    [false true] :!type/numerical))

(deftest column-names-grouped
  (is (= (ds/column-names ds) (api/column-names dsg)))
  (is (= (ds/column-names ds) (api/column-names dsg :all)))
  (is (nil? (api/column-names dsg nil)))
  (are [x y] (= x (api/column-names dsg y))
    ["country" "iso2" "iso3"] :type/string
    ["year"] "year"
    ["year"] ["year"]
    ["iso2" "year"] #{"year" "iso2"}
    ["new_sp_m014" "country"] ["new_sp_m014" "country"]
    ["iso2" "iso3"] #(str/starts-with? % "iso")
    ["country"] {"country" nil} ;; only keys are used
    ["iso2" "iso3"] #"^i.*")
  (is (every? #(str/starts-with? % "new") (api/column-names dsg (complement #{"country" "iso2" "iso3" "year"}))))
  (are [x y f] (= x (api/column-names dsg y f))
    ["iso2" "iso3"] #{"iso2" "iso3"} :name
    ["country" "iso2" "iso3"] #{:string} :datatype
    ["new_sp_m014"] (fn [{:keys [name datatype]}]
                      (and (= datatype :int16)
                           (str/starts-with? name "new_sp_m"))) :all))

(deftest rank-tests
  (let [x2 [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5]]
    (are [x y] (= x (sut/rank y))
      '(1.0 0.0 2.0 3.0 4.0) [3 1 4 15 92]
      '(1.0 0.0 2.0 3.0 4.0) '(1.0 0.0 2.0 3.0 4.0)
      '(3.5 0.5 5.0 0.5 7.0 10.0 2.0 9.0 7.0 3.5 7.0) x2
      '(3.5 0.5 5.0 0.5 7.0 10.0 2.0 9.0 7.0 3.5 7.0) '(3.5 0.5 5.0 0.5 7.0 10.0 2.0 9.0 7.0 3.5 7.0)
      ;; as in data.table, `frankv(x, na.last=FALSE)`
      '(5.0 2.5 5.0 0.5 2.5 0.5 5.0) [4 1 4 nil 1 nil 4])
    (are [x y ties] (= x (sut/rank y ties))
      [3 0 5 1 6 10 2 9 7 4 8] x2 :first
      [4 1 5 0 8 10 2 9 7 3 6] x2 :last
      '(4 1 5 1 8 10 2 9 8 4 8) x2 :max
      '(3 0 5 0 6 10 2 9 6 3 6) x2 :min
      '(2 0 3 0 4 6 1 5 4 2 4) x2 :dense)
    (is (= '(6.5 9.5 5.0 9.5 3.0 0.0 8.0 1.0 3.0 6.5 3.0) (sut/rank x2 :average true)))
    (is (= '(4 6 3 6 2 0 5 1 2 4 2) (sut/rank x2 :dense true)))))
