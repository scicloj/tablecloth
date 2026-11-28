(ns tablecloth.runtime-isolation-test
  (:require [clojure.java.io :as io]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str]
            [midje.sweet :refer [fact =>]]))

(def generator-namespaces
  '[tablecloth.utils.codegen
    tablecloth.column.api.lift-operators
    tablecloth.api.lift-operators])

(defn- ns->resource [ns-sym]
  (-> (name ns-sym) (str/replace "-" "_") (str/replace "." "/") (str ".clj")))

(defn- generator-namespaces-loaded-in-fresh-jvm
  "Requires both operator namespaces in a new JVM on this classpath; returns the
  generator namespaces that got loaded along with them."
  []
  (let [java (str (io/file (System/getProperty "java.home") "bin" "java"))
        form (pr-str `(do (require 'tablecloth.column.api.operators 'tablecloth.api.operators)
                          (prn (filterv find-ns '~generator-namespaces))
                          (shutdown-agents)))
        {:keys [exit out err]} (sh java "-cp" (System/getProperty "java.class.path")
                                   "clojure.main" "-e" form)]
    (if (zero? exit)
      (read-string (last (str/split-lines out)))
      {:exit exit :err err})))

(fact "the generator namespaces this test watches still exist"
      (remove #(io/resource (ns->resource %)) generator-namespaces) => [])

(fact "loading the generated operators does not load the code generator"
      (generator-namespaces-loaded-in-fresh-jvm) => [])
