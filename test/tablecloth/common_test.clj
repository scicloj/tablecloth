(ns tablecloth.common-test
  (:require [tablecloth.api :as api]))

(def DS (api/dataset {:V1 (take 9 (cycle [1 2]))
                      :V2 (range 1 10)
                      :V3 (take 9 (cycle [0.5 1.0 1.5]))
                      :V4 (take 9 (cycle ["A" "B" "C"]))}
                     {:dataset-name "DS"}))

(defn approx
  ^double [^double v]
  (-> (Double/toString v)
      (BigDecimal.)
      (.setScale 4 BigDecimal/ROUND_HALF_UP)
      (.doubleValue)))

