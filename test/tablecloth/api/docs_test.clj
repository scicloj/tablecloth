(ns tablecloth.api.docs-test
  (:require [tablecloth.api]
            [tablecloth.api.api-template]
            [tablecloth.column.api]
            [tablecloth.column.api.api-template]
            [clojure.string :as str]
            [midje.sweet :refer [fact =>]]))

(defn- source-var [template-ns var-sym]
  (let [v (ns-resolve template-ns var-sym)
        {:keys [src-ns src-sym]} (:export-info (meta v))]
    (if src-ns
      (do (require src-ns) (ns-resolve src-ns src-sym))
      v)))

(defn- doc-of [v]
  (let [d (:doc (meta v))]
    (when-not (str/blank? d) d)))

(defn- doc-mismatches
  "Exported vars of `target-ns` whose doc differs from their source var's doc."
  [target-ns template-ns]
  (for [[var-sym v] (ns-publics target-ns)
        :let [expected (doc-of (source-var template-ns var-sym))
              actual (doc-of v)]
        :when (not= expected actual)]
    var-sym))

(fact "every exported tablecloth.api doc matches its source"
      (count (ns-publics 'tablecloth.api)) => #(> % 150)
      (doc-mismatches 'tablecloth.api 'tablecloth.api.api-template) => empty?)

(fact "every exported tablecloth.column.api doc matches its source"
      (count (ns-publics 'tablecloth.column.api)) => #(> % 100)
      (doc-mismatches 'tablecloth.column.api 'tablecloth.column.api.api-template) => empty?)
