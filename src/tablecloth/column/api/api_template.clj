(ns tablecloth.column.api.api-template
  "Tablecloth Column API"
  (:require [tech.v3.datatype.export-symbols :as exporter]))

(exporter/export-symbols tablecloth.column.api.column
                         column
                         column?
                         typeof
                         zeros
                         ones
                         )

(comment
  (exporter/write-api! 'tablecloth.column.api.api-template
                       'tablecloth.column.api
                       "src/tablecloth/column/api.clj"
                       '[]
                       )

  )
