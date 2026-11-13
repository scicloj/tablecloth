(ns gendocs
  "Builds docs/index.html from notebooks/index.clj.
  Run from the repository root: `clojure -M:docs dev/gendocs.clj`"
  (:require [clojure.java.io :as io]
            [scicloj.clay.v2.api :as clay]
            [tablecloth.api :as tc]))

(defn- fail-on-load-errors!
  "A file or URL tablecloth cannot load becomes a one-row `:$error` dataset instead
  of an exception, so the notebook would render it silently. Make such loads throw;
  other inputs, like the notebook's deliberate `(tc/dataset 999)`, are left alone."
  []
  (alter-var-root
   #'tc/dataset
   (fn [dataset]
     (fn [& args]
       (let [ds (apply dataset args)]
         (when (and (some #(instance? % (first args)) [String java.io.File java.net.URL])
                    (tc/dataset? ds)
                    (some #{:$error} (tc/column-names ds)))
           (throw (ex-info (str "Cannot load " (pr-str (first args)) ": " (first (ds :$error))
                                " (missing file, or org.scicloj/dataset-io not on the classpath?)")
                           {:args args})))
         ds)))))

(fail-on-load-errors!)

;; `:show false` skips Clay's preview server, which would otherwise write a
;; loading page over docs/index.html and keep the JVM running.
;; Evaluation errors and Quarto failures throw, so the command exits nonzero.
(let [target (io/file "docs/index.html")
      before (slurp target)]
  (try
    (clay/make! {:format [:quarto :html]
                 :base-source-path "notebooks"
                 :source-path "index.clj"
                 :base-target-path "docs"
                 :show false
                 :browse false})
    (catch Throwable e
      ;; on a Quarto failure Clay writes an error page over the target
      (spit target before)
      (throw e))))

(shutdown-agents)
