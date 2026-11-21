(ns tablecloth.api.docs-test
  (:require [tablecloth.api]
            [tablecloth.api.api-template]
            [tablecloth.column.api]
            [tablecloth.column.api.api-template]
            [clojure.string :as str]
            [midje.sweet :refer [fact =>]]))

(defn- source-var [template-var]
  (let [{:keys [src-ns src-sym]} (:export-info (meta template-var))]
    (if src-ns
      (do (require src-ns) (ns-resolve src-ns src-sym))
      template-var)))

(defn- doc-of [v]
  (let [d (:doc (meta v))]
    (when-not (str/blank? d) d)))

(defn- doc-mismatches
  "Exports of `template-ns` that are missing from `target-ns`, or whose exported doc
  differs from their source var's doc."
  [target-ns template-ns]
  (let [exported (ns-publics target-ns)]
    (for [[var-sym template-var] (ns-publics template-ns)
          :let [v (get exported var-sym)]
          :when (or (nil? v)
                    (not= (doc-of (source-var template-var)) (doc-of v)))]
      var-sym)))

(fact "every tablecloth.api export exists and its doc matches its source"
      (doc-mismatches 'tablecloth.api 'tablecloth.api.api-template) => empty?)

(fact "every tablecloth.column.api export exists and its doc matches its source"
      (doc-mismatches 'tablecloth.column.api 'tablecloth.column.api.api-template) => empty?)
