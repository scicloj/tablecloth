(ns regen
  "Regenerates Tablecloth's four generated API files. Run `clojure -M:regen` from the repo root."
  (:require [clojure.java.io :as io]
            [tablecloth.utils.codegen :as codegen]
            [tech.v3.datatype.export-symbols :as exporter])
  (:import [java.nio.file CopyOption Files StandardCopyOption]
           [java.nio.file.attribute FileAttribute]))

(def ^:private operator-exclusions
  '[* + - / < <= > >= abs and bit-and bit-and-not bit-clear bit-flip
    bit-not bit-or bit-set bit-shift-left bit-shift-right bit-test bit-xor
    even? identity infinite? max min neg? not odd? odd? or pos? quot rem
    unsigned-bit-shift-right zero?])

(def ^:private api-exclusions
  '[* + - / < <= >= >
    abs and bit-and bit-and-not bit-clear bit-flip bit-not bit-or bit-set
    bit-shift-left bit-shift-right bit-xor even? identity infinite? max min
    neg? not odd? or pos? quot rem unsigned-bit-shift-right zero?])

(defn- lift-step [target-ns source-ns lift-ns dep]
  {:ns target-ns
   :file (codegen/namespace-to-path target-ns)
   :generate! #(codegen/do-lift
                {:target-ns target-ns
                 :source-ns source-ns
                 :lift-fn-lookup @(requiring-resolve (symbol (name lift-ns) "serialized-lift-fn-lookup"))
                 :deps [dep]
                 :exclusions operator-exclusions})})

(defn- api-step [template-ns target-ns exclusions]
  (let [file (codegen/namespace-to-path target-ns)]
    {:ns target-ns
     :file file
     :generate! #(exporter/write-api! template-ns target-ns file exclusions)}))

(def steps
  "In order: each step reads the outputs of the steps before it."
  [(lift-step 'tablecloth.column.api.operators 'tech.v3.datatype.functional
              'tablecloth.column.api.lift-operators 'tablecloth.column.api.lift_operators)
   (lift-step 'tablecloth.api.operators 'tablecloth.column.api.operators
              'tablecloth.api.lift-operators 'tablecloth.api.lift_operators)
   (api-step 'tablecloth.column.api.api-template 'tablecloth.column.api api-exclusions)
   (api-step 'tablecloth.api.api-template 'tablecloth.api
             (into '[group-by drop concat rand-nth first last shuffle] api-exclusions))])

(defn- copy! [from to]
  (Files/copy (.toPath (io/file from)) (.toPath (io/file to))
              ^"[Ljava.nio.file.CopyOption;" (into-array CopyOption [StandardCopyOption/REPLACE_EXISTING])))

(defn- backup-file [dir i] (io/file dir (str i ".clj")))

(defn- delete-backups! [dir]
  (run! io/delete-file (reverse (file-seq dir))))

(defn- backup!
  "Copies every file into a fresh temp directory and returns it."
  [files]
  (let [dir (.toFile (Files/createTempDirectory "tablecloth-regen" (make-array FileAttribute 0)))]
    (try
      (doseq [[i file] (map-indexed vector files)]
        (copy! file (backup-file dir i)))
      dir
      (catch Throwable t
        (delete-backups! dir)
        (throw t)))))

(defn- restore! [dir files]
  (doseq [[i file] (map-indexed vector files)]
    (copy! (backup-file dir i) file)))

(defn- run-step! [{:keys [ns file generate!]}]
  (println "Generating" file)
  (generate!)
  (require ns :reload))

(defn regen!
  "Runs every step in order. On any failure, restores all four files and rethrows."
  []
  (let [files (mapv :file steps)
        dir (backup! files)]
    (try
      (run! run-step! steps)
      (catch Throwable t
        (try
          (restore! dir files)
          (delete-backups! dir)
          (println "Regeneration failed; all four files restored.")
          (catch Throwable restore-error
            (.addSuppressed t restore-error)
            (println "Restore failed; original files kept in" (str dir))))
        (throw t)))
    (delete-backups! dir)))

(defn -main [& _]
  (try
    (regen!)
    (shutdown-agents)
    (catch Throwable t
      (.printStackTrace t)
      (System/exit 1))))
