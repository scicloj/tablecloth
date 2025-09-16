(ns profile
  (:require [clj-async-profiler.core :as prof]))

(defn -main []
  (println "Profiling...")
  (prof/profile (require '[tablecloth.api]))
  (println "Done. See /tmp/clj-async-profiler/results/"))
