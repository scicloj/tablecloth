(defproject scicloj/tablecloth "8.024"
  :description "Dataset manipulation library built on the top of tech.ml.dataset."
  :url "https://github.com/scicloj/tablecloth"
  :license {:name "The MIT Licence"
            :url "https://opensource.org/licenses/MIT"}
  ;; :plugins [[lein-tools-deps "0.4.5"]]
  ;; :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  ;; :lein-tools-deps/config {:config-files [:install :user :project]}
  :dependencies [[techascent/tech.ml.dataset "8.024"]
                 [org.clojure/clojure "1.12.5"]]
  :profiles {:dev {:cloverage    {:runner :midje}
                   :dependencies [[org.clojure/clojure "1.13.0-alpha6"]
                                  [midje "1.10.10"]
                                  [org.scicloj/clay "2.0.20"]
                                  [org.scicloj/dataset-io "1.0.0"]
                                  #_[babashka/fs "0.5.25"]] ;; need this since TMD grabs old version...
                   :plugins      [[lein-midje "3.2.1"]
                                  [lein-cloverage "1.2.4"]]}})
