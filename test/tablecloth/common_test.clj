(ns tablecloth.common-test
  (:require [tablecloth.api :as api]
            [tech.v3.datatype :as dtype]))

(def DS (api/dataset {:V1 (take 9 (cycle [1 2]))
                      :V2 (range 1 10)
                      :V3 (take 9 (cycle [0.5 1.0 1.5]))
                      :V4 (take 9 (cycle ["A" "B" "C"]))}
                     {:dataset-name "DS"}))

(def cloned-reader-ds (-> {:x (dtype/make-reader :float32 5 (rand))
                           :y (dtype/make-reader :float32 5 (rand))}
                          (api/dataset)))

(def non-cloned-reader-ds (-> {:x (dtype/make-reader :float32 5 (rand))
                               :y (dtype/make-reader :float32 5 (rand))}
                              (api/dataset {:prevent-clone? true})))

(defn approx
  ^double [^double v]
  (-> (Double/toString v)
      (BigDecimal.)
      (.setScale 4 BigDecimal/ROUND_HALF_UP)
      (.doubleValue)))

