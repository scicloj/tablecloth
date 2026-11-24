(ns genmelody
  "Checks data/ode-to-joy.csv and renders notebooks/melody/ode_to_joy.clj to
  target/melody/ode_to_joy.html. Run from the repository root:
  `clojure -M:melody dev/genmelody.clj`"
  (:require [scicloj.clay.v2.api :as clay]
            [tablecloth.api :as tc]))

(def ^:private beats
  {"eighth" 1/2 "quarter" 1 "dotted-quarter" 3/2 "half" 2})

(let [ds (tc/dataset "data/ode-to-joy.csv" {:key-fn keyword})
      bar-lengths (->> (map vector (ds :bar) (ds :duration))
                       (reduce (fn [acc [bar d]] (update acc bar (fnil + 0) (beats d))) {}))]
  (assert (= [:bar :pitch :duration] (tc/column-names ds)))
  (assert (= 30 (tc/row-count ds)))
  (assert (every? beats (ds :duration)) "unknown duration")
  (assert (every? #(re-matches #"[A-G][#b]?\d" %) (ds :pitch)) "unknown pitch")
  (assert (= (zipmap (range 1 9) (repeat 4)) bar-lengths) "a bar is not 4 beats"))

;; `:show false` skips Clay's preview server, which would keep the JVM running.
(clay/make! {:format [:html]
             :base-source-path "notebooks/melody"
             :source-path "ode_to_joy.clj"
             :base-target-path "target/melody"
             :show false
             :browse false})

(shutdown-agents)
