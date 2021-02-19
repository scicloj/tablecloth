(ns tablecloth.api.clone)

(defn clone-columns
  ([ds column-names prevent-clone?]
   (if prevent-clone?
     ds
     (tech.v3.dataset/update-columnwise ds column-names tech.v3.datatype/clone))))
