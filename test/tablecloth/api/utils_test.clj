(ns tablecloth.api.utils-test
  (:require [tablecloth.api :as api]
            [tech.ml.dataset :as ds]
            [tech.v2.datatype.readers.const :as const-rdr]
            [tablecloth.api.utils :as sut]
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
    (const-rdr/make-const-reader 3 :int64 5))
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
  (is (= (ds/column-names ds) (sut/column-names ds)))
  (is (= (ds/column-names ds) (sut/column-names ds :all)))
  (is (nil? (sut/column-names ds nil)))
  (are [x y] (= x (sut/column-names ds y))
    ["country" "iso2" "iso3"] :type/string
    ["country" "iso2" "iso3"] :!type/numerical
    ["year"] "year"
    ["year"] ["year"]
    ["iso2" "year"] #{"year" "iso2"}
    ["country" "new_sp_m014"] ["new_sp_m014" "country"] ;; order of columns is kept
    ["iso2" "iso3"] #(str/starts-with? % "iso")
    ["country"] {"country" nil} ;; only keys are used
    ["iso2" "iso3"] #"^i.*")
  (is (every? #(str/starts-with? % "new") (sut/column-names ds (complement #{"country" "iso2" "iso3" "year"}))))
  (are [x y f] (= x (sut/column-names ds y f))
    ["iso2" "iso3"] #{"iso2" "iso3"} :name
    ["country" "iso2" "iso3"] #{:string} :datatype
    ["new_sp_m014"] (fn [{:keys [name datatype]}]
                      (and (= datatype :int16)
                           (str/starts-with? name "new_sp_m"))) :all)
  (are [x y] (= x (sut/column-names ds-sn y))
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
  (is (= (ds/column-names ds) (sut/column-names dsg)))
  (is (= (ds/column-names ds) (sut/column-names dsg :all)))
  (is (nil? (sut/column-names dsg nil)))
  (are [x y] (= x (sut/column-names dsg y))
    ["country" "iso2" "iso3"] :type/string
    ["year"] "year"
    ["year"] ["year"]
    ["iso2" "year"] #{"year" "iso2"}
    ["country" "new_sp_m014"] ["new_sp_m014" "country"] ;; order of columns is kept
    ["iso2" "iso3"] #(str/starts-with? % "iso")
    ["country"] {"country" nil} ;; only keys are used
    ["iso2" "iso3"] #"^i.*")
  (is (every? #(str/starts-with? % "new") (sut/column-names dsg (complement #{"country" "iso2" "iso3" "year"}))))
  (are [x y f] (= x (sut/column-names dsg y f))
    ["iso2" "iso3"] #{"iso2" "iso3"} :name
    ["country" "iso2" "iso3"] #{:string} :datatype
    ["new_sp_m014"] (fn [{:keys [name datatype]}]
                      (and (= datatype :int16)
                           (str/starts-with? name "new_sp_m"))) :all))

(deftest rank-tests)
