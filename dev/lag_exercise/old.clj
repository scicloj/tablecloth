(ns lag-exercise.old
  "Frozen copy of the original rolling-based `lag` from
  `tablecloth.api.rolling`. Does NOT call the live function, so it keeps
  returning the original behavior after `rolling.clj` is edited.
  Delete with the rest of the lag exercise scratch files."
  (:require [tech.v3.dataset.rolling :as rolling]))

(defn lag
  ([ds column-name lag-size]
   (let [new-column-name (-> column-name
                             name
                             (str "-lag-" lag-size)
                             keyword)]
     (lag ds new-column-name column-name lag-size)))
  ([ds new-column-name column-name lag-size]
   (rolling/rolling ds
                    {:window-type :fixed
                     :window-size (inc lag-size)
                     :relative-window-position :left
                     :edge-mode :zero}
                    {new-column-name (rolling/first column-name)})))

(comment
  ;; needs (require '[lag-exercise.matrix]) in the REPL first; kept out of the ns so
  ;; lag-exercise.old and lag-exercise.matrix don't require each other.
  (def result (lag-exercise.matrix/run lag))

  (filterv #(contains? (nth % 2) :error) result)

  result)

