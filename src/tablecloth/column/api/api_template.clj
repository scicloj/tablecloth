(ns tablecloth.column.api.api-template
  "Tablecloth Column API"
  (:require [tech.v3.datatype.export-symbols :as exporter]))

(exporter/export-symbols tablecloth.column.api.column
                         column
                         column?
                         column-map
                         typeof
                         typeof?
                         slice
                         zeros
                         ones
                         count-missing
                         sort-column)

(exporter/export-symbols tech.v3.dataset.column
                         is-missing?
                         missing
                         select)

(comment
  ;; Use this to generate the column api
  (exporter/write-api! 'tablecloth.column.api.api-template
                       'tablecloth.column.api
                       "src/tablecloth/column/api.clj"
                       '[])
  ,)
