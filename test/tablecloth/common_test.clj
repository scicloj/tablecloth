(ns tablecloth.common-test)

(defn approx
  ^double [^double v]
  (-> (Double/toString v)
      (BigDecimal.)
      (.setScale 4 BigDecimal/ROUND_HALF_UP)
      (.doubleValue)))

