(ns lag-exercise.matrix
  "Test matrix for lag (exercises 1 and 8). Runs any 4-arity lag fn
  `(f ds new-col col k)` over datatype x source-missing x k and records
  values, datatype, and missing set per case.

  Usage:
    (def old (run lag-exercise.old/lag))
    (print-table old)
    (diff old (run my-new-lag))   ; => only the cases that differ"
  (:require [lag-exercise.old]
            [tablecloth.api :as tc]
            [tech.v3.dataset.column :as col]
            [tech.v3.datatype :as dtype])
  (:import [java.time LocalDate]))

(def n 5)

(def sources
  "Source values per datatype; `:missing` variant has a nil at index 2."
  (let [d #(LocalDate/of 2020 1 %)]
    {:float64 {:plain   [1.0 2.0 3.0 4.0 5.0]
               :missing [1.0 2.0 nil 4.0 5.0]}
     :int64   {:plain   [1 2 3 4 5]
               :missing [1 2 nil 4 5]}
     :boolean {:plain   [true false true false true]
               :missing [true false nil false true]}
     :string  {:plain   ["a" "b" "c" "d" "e"]
               :missing ["a" "b" nil "d" "e"]}
     :date    {:plain   (mapv d [1 2 3 4 5])
               :missing [(d 1) (d 2) nil (d 4) (d 5)]}}))

(def ks
  "0, 1, n-1, n, n+1, and a negative."
  [0 1 (dec n) n (inc n) -1])

(defn run-case
  "Run `lag-fn` on one column. Returns {:data :dtype :missing} or {:error msg}."
  [lag-fn values k]
  (try
    (let [ds  (tc/dataset {:a values})
          out (lag-fn ds :out :a k)
          c   (:out out)]
      {:src-dtype (dtype/get-datatype (:a ds))
       :n         (count c)
       :data      (vec c)
       :dtype     (dtype/get-datatype c)
       :missing   (into [] (map long) (col/missing c))})
    (catch Throwable t
      {:error (str (class t) ": " (ex-message t))})))

(defn run
  "Run the full matrix. Returns a map keyed by [datatype variant k]."
  [lag-fn]
  (into (sorted-map)
        (for [[dt variants] sources
              [variant values] variants
              k ks]
          [[dt variant k] (run-case lag-fn values k)])))

(defn print-table
  "One line per case: key, datatype, data, missing."
  [results]
  (doseq [[[dt variant k] r] results]
    (println (format "%-8s %-8s k=%-3d" (name dt) (name variant) k)
             (if (:error r)
               (str "ERROR " (:error r))
               (str (:dtype r) " " (:data r) " missing=" (:missing r)))))
  :done)

(defn diff
  "Cases where `a` and `b` (results from `run`) differ, with both results."
  [a b]
  (into (sorted-map)
        (for [k (keys a)
              :when (not= (get a k) (get b k))]
          [k {:a (get a k) :b (get b k)}])))

(defn invariant-failures
  "Cases that break the section 4 invariants for `results` (from `run`):
   length equals n, datatype equals source, and source-missing rows stay
   missing at their shifted position (checked where index 2+k is in range)."
  [results]
  (into (sorted-map)
        (for [[[dt variant k :as key] r] results
              :let [problems
                    (cond-> []
                      (:error r)
                      (conj :error)

                      (and (not (:error r)) (not= n (:n r)))
                      (conj :length)

                      (and (not (:error r)) (not= (:src-dtype r) (:dtype r)))
                      (conj :datatype)

                      (and (not (:error r))
                           (= variant :missing)
                           (pos? k)
                           (< (+ 2 k) n) ; source nil at index 2 stays in range
                           (not (some #{(+ 2 k)} (:missing r))))
                      (conj :source-missing-lost))]
              :when (seq problems)]
          [key problems])))
