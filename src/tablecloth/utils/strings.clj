(ns tablecloth.utils.strings
  (:require
   [clojure.string :as str])
  (:import [java.text Normalizer Normalizer$Form]))

(defn to-kebab-case [string]
  ;; TODO there is a librray for this : https://github.com/clj-commons/camel-snake-kebab
  (-> string
      (str/replace #"([a-z0-9])([A-Z])" "$1-$2") ;; insert a hyphen before uppercase characters
      (str/replace #"([A-Z])([A-Z][a-z])" "$1-$2")
      str/lower-case))

(defn clean-up-punctuation [string]
  (-> string
      (str/replace #"[.|_|\s]" "-") ;; replace periods, underscores, and spaces with hyphens
      (str/replace "#{" "set-") ;; replace certain punctuation with text
      (str/replace "%" "-percent-")
      (str/replace "&" "-and-")
      (str/replace "#" "-number-")
      (str/replace #"[\p{P}&&[^-]]+" "") ;; remove the rest of the punctuation except hyphens
      (str/replace #"^[-]+|[-]+$" "") ;; remove leading and trailing hyphens
      ))

(defn to-ascii
  "Removes diacritic marks and any non-ascii characters from the given string."
  [string]
  (-> string
      (Normalizer/normalize Normalizer$Form/NFD)
      (str/replace #"\p{InCombiningDiacriticalMarks}+" "")
      (str/replace #"[^\p{ASCII}]" "")))

(defn to-clean-keyword
  "Takes any value and converts it to a clean, kebab-cased keyword. The meaning of \"clean\" is
  borrowed from the [implementation in R's `janitor` package here](https://github.com/sfirke/janitor/blob/80cd1eb4d5abf360ad68780654204e94a2303046/R/make_clean_names.R#L254-L263)"
  [val]
  (let [result (-> val
                   str
                   to-kebab-case
                   to-ascii
                   clean-up-punctuation)]
    (if (seq result)
      (keyword result)
      (throw (ex-info "Calling `to-clean-keyword` on this value would result in an empty keyword"
                      {:value val})))))