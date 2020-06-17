(defproject scicloj/tablecloth "1.0.0-pre-alpha5"
  :description "Dataset manipulation library built on the top of tech.ml.dataset."
  :url "https://github.com/scicloj/tablecloth"
  :license {:name "The MIT Licence"
            :url "https://opensource.org/licenses/MIT"}
  :plugins [[lein-tools-deps "0.4.5"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]})
