(ns readme-generation
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]))

(set! *print-length* 1000)

(defn chunk-end? [line]
  (= line "```"))

(defn clojure-chunk-beginning? [line]
  (re-matches #"```\{clojure.*\}" line))

(defn clojure-chunk-options [line]
  (-> line
      (str/replace #"^```\{clojure" "")
      (str/replace #"\}$" "")))

(defn markdown->markdown-with-extracted-clojure-chunks [markdown]
  (loop [lines                 (str/split markdown #"\n")
         current-clojure-chunk-code    nil
         current-clojure-chunk-options nil
         result                []]
    (if-let [current-line (first lines)]
      (if current-clojure-chunk-code
        (if (chunk-end? current-line)
          (recur (rest lines)
                 nil
                 nil
                 (conj result {:clojure-chunk? true
                               :code current-clojure-chunk-code
                               :options current-clojure-chunk-options}))
          (recur (rest lines)
                 (conj current-clojure-chunk-code current-line)
                 current-clojure-chunk-options
                 result))
        (if (clojure-chunk-beginning? current-line)
          (recur (rest lines)
                 []
                 (clojure-chunk-options current-line)
                 result)
          (recur (rest lines)
                 nil
                 nil
                 (conj result current-line))))
      result)))

(defn markdown-with-extracted-clojure-chunks->markdown-evaluated [mwecc]
  (->> mwecc
       (map (fn [element]
              (if (string? element)
                element
                (let [{:keys [code options]} element
                      options-map (-> options
                                      str/trim
                                      (str/split #"=")
                                      (->> (map read-string)
                                           (apply hash-map)))]
                  (str (format "```{clojure}\n%s\n```\n"
                               (->> code
                                    (str/join "\n")))
                       (if (-> options-map
                               (get 'eval)
                               (= 'FALSE))
                         ""
                         (let [result (->> code
                                           (str/join "\n")
                                           load-string)]
                           (case (-> options-map
                                     (get 'results))
                             "hide" ""
                             "asis" (-> result
                                        pp/pprint
                                        with-out-str)))))))))
       (str/join "\n")))


(defn generate! []
  (-> "README-source.md"
      slurp
      markdown->markdown-with-extracted-clojure-chunks
      markdown-with-extracted-clojure-chunks->markdown-evaluated
      (spit "README.md")))
