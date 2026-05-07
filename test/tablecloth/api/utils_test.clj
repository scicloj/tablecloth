(ns tablecloth.api.utils-test
  (:require [tablecloth.api :as api]
            [tablecloth.api.utils :as sut]
            [clojure.test :refer [deftest is testing]]
            [tech.v3.dataset :as ds]
            [tech.v3.datatype :as dtype]
            [clojure.string :as str]
            [clojure.test.check :as tch]
            [tablecloth.utils.strings :as sut-strings]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [midje.sweet :refer [tabular fact =>]]))

(def ds (api/dataset "data/who.csv.gz"))
(def dsg (api/group-by ds "country"))

(def ds-sn (api/dataset {[1 2 3]     [1 2 3]
                         false       ["false" "false" "false"]
                         true        ["true" "true" "true"]
                         {:a 1 :b 2} [9 9 9]}))

(fact "iterable-sequence"
      (tabular (fact (sut/iterable-sequence? ?x)
                     => true)
               [?x]
               [] '() #{}
               (ds "country")
               (java.util.ArrayList.)
               (dtype/const-reader 3 5))
      (tabular (fact (sut/iterable-sequence? ?x)
                     => false)
               [?x]
               nil {}))

(fact "->str"
      (tabular (fact (sut/->str ?y)
                     => ?x)
               [?x ?y]
               "a" "a"
               "b" :a/b
               "c" 'a/c
               "b" :b
               "c" 'c))

(fact "column-names-regular"
      (fact (api/column-names ds)
            => (ds/column-names ds))
      (fact (api/column-names ds :all)
            => (ds/column-names ds))
      (fact (nil? (api/column-names ds nil))
            => true)
      (tabular (fact (api/column-names ds ?y)
                     => ?x)
               [?x ?y]
               ["country" "iso2" "iso3"] :type/string
               ["country" "iso2" "iso3"] :!type/numerical
               ["year"] "year"
               ["year"] ["year"]
               ["iso2" "year"] #{"year" "iso2"}
               ["new_sp_m014" "country"] ["new_sp_m014" "country"]
               ["iso2" "iso3"] #(str/starts-with? % "iso")
               ["country"] {"country" nil} ;; only keys are used
               ["iso2" "iso3"] #"^i.*")
      (fact (every? #(str/starts-with? % "new") (api/column-names ds (complement #{"country" "iso2" "iso3" "year"})))
            => true)
      (tabular (fact (api/column-names ds ?y ?f)
                     => ?x)
               [?x ?y ?f]
               ["iso2" "iso3"] #{"iso2" "iso3"} :name
               ["country" "iso2" "iso3"] #{:string} :datatype
               ["new_sp_m014"] (fn [{:keys [name datatype]}]
                                 (and (= datatype :int16)
                                      (str/starts-with? name "new_sp_m"))) :all)
      (tabular (fact (api/column-names ds-sn ?y)
                     => ?x)
               [?x ?y]
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

(fact "column-names-grouped"
      (fact (api/column-names dsg)
            => (ds/column-names ds))
      (fact (api/column-names dsg :all)
            => (ds/column-names ds))
      (fact (nil? (api/column-names dsg nil))
            => true)
      (tabular (fact (api/column-names dsg ?y)
                     => ?x)
               [?x ?y]
               ["country" "iso2" "iso3"] :type/string
               ["year"] "year"
               ["year"] ["year"]
               ["iso2" "year"] #{"year" "iso2"}
               ["new_sp_m014" "country"] ["new_sp_m014" "country"]
               ["iso2" "iso3"] #(str/starts-with? % "iso")
               ["country"] {"country" nil} ;; only keys are used
               ["iso2" "iso3"] #"^i.*")
      (fact (every? #(str/starts-with? % "new") (api/column-names dsg (complement #{"country" "iso2" "iso3" "year"})))
            => true)
      (tabular (fact (api/column-names dsg ?y ?f)
                     => ?x)
               [?x ?y ?f]
               ["iso2" "iso3"] #{"iso2" "iso3"} :name
               ["country" "iso2" "iso3"] #{:string} :datatype
               ["new_sp_m014"] (fn [{:keys [name datatype]}]
                                 (and (= datatype :int16)
                                      (str/starts-with? name "new_sp_m"))) :all))

(fact "rank-tests"
      (let [x2 [3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5]]
        (tabular (fact (sut/rank ?y)
                       => ?x)
                 [?x ?y]
                 '(1.0 0.0 2.0 3.0 4.0) [3 1 4 15 92]
                 '(1.0 0.0 2.0 3.0 4.0) '(1.0 0.0 2.0 3.0 4.0)
                 '(3.5 0.5 5.0 0.5 7.0 10.0 2.0 9.0 7.0 3.5 7.0) x2
                 '(3.5 0.5 5.0 0.5 7.0 10.0 2.0 9.0 7.0 3.5 7.0) '(3.5 0.5 5.0 0.5 7.0 10.0 2.0 9.0 7.0 3.5 7.0)
                 ;; as in data.table, `frankv(?x, na.last=FALSE)`
                 '(5.0 2.5 5.0 0.5 2.5 0.5 5.0) [4 1 4 nil 1 nil 4])
        (tabular (fact (sut/rank ?y ?ties)
                       => ?x)
                 [?x ?y ?ties]
                 [3 0 5 1 6 10 2 9 7 4 8] x2 :first
                 [4 1 5 0 8 10 2 9 7 3 6] x2 :last
                 '(4 1 5 1 8 10 2 9 8 4 8) x2 :max
                 '(3 0 5 0 6 10 2 9 6 3 6) x2 :min
                 '(2 0 3 0 4 6 1 5 4 2 4) x2 :dense)
        (fact (sut/rank x2 :average true)
              => '(6.5 9.5 5.0 9.5 3.0 0.0 8.0 1.0 3.0 6.5 3.0))
        (fact (sut/rank x2 :dense true)
              => '(4 6 3 6 2 0 5 1 2 4 2))))


(fact "->general-types describes the set of general types for a concrete datatype"
      (sut/->general-types :int32) => #{:integer :numerical}
      (sut/->general-types :float32) => #{:float :numerical}
      (sut/->general-types :string) => #{:textual})



(def stringable
  (prop/for-all [v gen/any]
                (try
                  (let [result (sut-strings/to-clean-keyword v)]
                    (and (keyword? result)
                         (= 0 (count (re-seq #"[\p{P}&&[^-]]|[^\p{ASCII}]" (name result))))))
                  (catch Throwable ex
                    (if (str/includes? (ex-message ex) "Calling `to-clean-keyword`")
                      :pass
                      (throw ex))))))

;; Use test check to see if I missed any possibilities
(tch/quick-check 10000 stringable)

;; Still test some explicitly to make sure outputs are reasonable and document expected results
(deftest to-clean-keyword
  (testing "handles non-string inputs"
    (is (= :1 (sut-strings/to-clean-keyword 1)))
    (is (= :1-0 (sut-strings/to-clean-keyword 1.0)))

    (is (= :a-1-b-2 (sut-strings/to-clean-keyword {:a 1 :b 2})))
    (is (= :a-1-b-2-3 (sut-strings/to-clean-keyword {:a 1 :b [2 3]})))
    (is (= :1-2-3 (sut-strings/to-clean-keyword [1 2 3])))

    (is (= :set-1-3-2 (sut-strings/to-clean-keyword #{1 2 3})))
    (is (= :set-a-b-c (sut-strings/to-clean-keyword #{"a" "b" "c"})))

    (is (= :keyword (sut-strings/to-clean-keyword :keyword)))
    (is (= :keyword (sut-strings/to-clean-keyword :keyword)))
    (is (= :keyword--test (sut-strings/to-clean-keyword :keyword--test)))
    (is (= :keyword-test (sut-strings/to-clean-keyword :KeywordTest))))

  (testing "removes punctuation"
    (is (= :test123 (sut-strings/to-clean-keyword "test'123")))
    (is (= :test123 (sut-strings/to-clean-keyword "test\"123")))
    (is (= :test123 (sut-strings/to-clean-keyword "test/123")))
    (is (= :test-percent-123 (sut-strings/to-clean-keyword "test%123")))
    (is (= :test-and-123 (sut-strings/to-clean-keyword "test&123")))
    (is (= :test-number-123 (sut-strings/to-clean-keyword "test#123")))
    (is (= :test-123 (sut-strings/to-clean-keyword "test 123")))
    (is (= :test-123 (sut-strings/to-clean-keyword "test_123")))
    (is (= :test-123 (sut-strings/to-clean-keyword ".test.123")))
    (is (= :test-123 (sut-strings/to-clean-keyword " test 123")))
    (is (= :test-123 (sut-strings/to-clean-keyword "test.123.")))
    (is (= :test--123 (sut-strings/to-clean-keyword "test..123")))
    (is (= :test-123 (sut-strings/to-clean-keyword "test-123-")))
    (is (= :test-123 (sut-strings/to-clean-keyword "test-123--")))
    (is (= :test-123 (sut-strings/to-clean-keyword "test_123__")))
    (is (= :test-123 (sut-strings/to-clean-keyword "test_123_"))))

  (testing "removes non-ascii characters"
    (is (= :studio-cafe (sut-strings/to-clean-keyword "Studio Café")))
    (is (= :company (sut-strings/to-clean-keyword "Company ©")))
    (is (= :gbp (sut-strings/to-clean-keyword "£ (GBP)")))
    (is (= :s (sut-strings/to-clean-keyword "µs"))))

  (testing "kebab-cases and keywordizes strings"
    (is (= :testing--this (sut-strings/to-clean-keyword "Testing  This")))
    (is (= :testing-this (sut-strings/to-clean-keyword "Testing This")))
    (is (= :testing-this (sut-strings/to-clean-keyword "TestingThis")))
    (is (= :testingthis (sut-strings/to-clean-keyword "TESTINGTHIS")))
    (is (= :testing-this (sut-strings/to-clean-keyword "TESTING THIS")))
    (is (= :testing-this (sut-strings/to-clean-keyword "TESTING_THIS")))
    (is (= :testing-this (sut-strings/to-clean-keyword "Testing-This")))
    (is (= :testing--this (sut-strings/to-clean-keyword "Testing--This")))
    (is (= :testing-this (sut-strings/to-clean-keyword "TestingThis")))
    (is (= :testing-this (sut-strings/to-clean-keyword "testingThis")))
    (is (= :testing-this (sut-strings/to-clean-keyword "testing This")))
    (is (= :testing--this (sut-strings/to-clean-keyword "testing__this")))
    (is (= :testing-this (sut-strings/to-clean-keyword "testing_this")))
    (is (= :testing-this (sut-strings/to-clean-keyword "testing this")))
    (is (= :testing-this (sut-strings/to-clean-keyword " testing this "))))

  (testing "throws an error when result would be an empty string"
    (is (thrown? clojure.lang.ExceptionInfo (sut-strings/to-clean-keyword "[]")))
    (is (thrown? clojure.lang.ExceptionInfo (sut-strings/to-clean-keyword "こんにちは")))
    (is (thrown? clojure.lang.ExceptionInfo (sut-strings/to-clean-keyword "漢")))))