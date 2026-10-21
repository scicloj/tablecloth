(ns tablecloth.column.api.missing-test
  (:require [tablecloth.column.api.missing :refer [count-missing replace-missing drop-missing]]
            [tablecloth.column.api.column :refer [column]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [midje.sweet :refer [facts fact =>]])
  (:import [java.io PushbackReader]))

(facts "about `count-missing`"
       (count-missing (column [1 2 3])) => 0
       (count-missing (column [1 nil 3 nil])) => 2)

(facts "about `replace-missing`"
       (replace-missing (column [1 nil 3])) => [1 1 3]
       (replace-missing (column [1 nil 3]) :up) => [1 3 3]
       (map double (replace-missing (column [1 nil 3]) :value 0)) => [1.0 0.0 3.0])

(facts "about `drop-missing`"
       (drop-missing (column [1 nil 3])) => [1 3])

(defn- ns-requires
  "Namespaces required by `ns-sym`'s source file, read without loading it."
  [ns-sym]
  (let [path (-> (name ns-sym) (str/replace "-" "_") (str/replace "." "/") (str ".clj"))]
    (with-open [r (PushbackReader. (io/reader (io/resource path)))]
      (->> (read r)
           (filter #(and (seq? %) (= :require (first %))))
           (mapcat rest)
           (map #(if (sequential? %) (first %) %))
           set))))

(defn- tablecloth-deps
  "Transitive tablecloth namespaces required by `ns-sym`."
  [ns-sym]
  (loop [seen #{} todo [ns-sym]]
    (if-let [[n & more] (seq todo)]
      (if (seen n)
        (recur seen more)
        (recur (conj seen n)
               (into (vec more) (filter #(str/starts-with? (name %) "tablecloth")) (ns-requires n))))
      (disj seen ns-sym))))

(fact "missing-value code does not reach the generated APIs"
      (let [deps (tablecloth-deps 'tablecloth.column.api.missing)]
        (contains? deps 'tablecloth.api) => false
        (contains? deps 'tablecloth.api.operators) => false
        (contains? deps 'tablecloth.column.api.operators) => false))

