(ns tablecloth.conversion
  (:require [clojure.string :as string]))

(set! *print-length* 1000)

(def example-doc
  (slurp "docs/index.Rmd"
         #_"https://raw.githubusercontent.com/scicloj/tablecloth/master/docs/index.Rmd"))

(defn chunk-end? [line]
  (= line "```"))

(defn clojure-chunk-beginning? [line]
  (re-matches #"```\{clojure.*\}" line))

(defn clojure-chunk-options [line]
  (-> line
      (string/replace #"^```\{clojure" "")
      (string/replace #"\}$" "")))

(defn markdown->markdown-with-extracted-clojure-chunks [markdown]
  (loop [lines                 (string/split markdown #"\n")
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

(defn clojure-chunk->markdown [{:keys [clojure-chunk? code options]}]
  (when clojure-chunk?
    (str "```{clojure"
         options
         "}\n"
         (string/join "\n" code)
         "\n```")))

(defn markdown-with-extracted-clojure-chunks->markdown [mwecc]
  (->> mwecc
       (map #(or (clojure-chunk->markdown %)
                 %))
       (string/join "\n")
       (format "%s\n")))

(comment
  (->> example-doc
       (spit "/tmp/0.Rmd"))

  (->> example-doc
       markdown->markdown-with-extracted-clojure-chunks
       markdown-with-extracted-clojure-chunks->markdown
       (spit "/tmp/a.Rmd"))

  (->> example-doc
       markdown->markdown-with-extracted-clojure-chunks
       markdown-with-extracted-clojure-chunks->markdown
       (= example-doc))

  (->> example-doc
       markdown->markdown-with-extracted-clojure-chunks
       (partition-by string?)))


(defn inner-pr-str [s]
  (let [s1 (pr-str s)
        n (count s1)]
    (subs s1 1 (dec n))))

(defn markdown-with-extracted-clojure-chunks->clojure [mwecc]
  (->> mwecc
       (partition-by string?)
       (mapcat (fn [part]
                 (cond (-> part first string?)         [(->> part
                                                             (map inner-pr-str)
                                                             (string/join "\n")
                                                             ((fn [s]
                                                                (if (seq s)
                                                                  (format "(md \"%s\")" s)
                                                                  ""))))]
                       (-> part first :clojure-chunk?) [(->> part
                                                             (mapcat
                                                              (fn [{:keys [code options]}]
                                                                code
                                                                #_(cons (format "^{:chunk-options \"%s\"}[]\n"
                                                                                options)
                                                                        code)))
                                                             (string/join "\n"))])))
       (string/join "\n\n\n")
       (format "
(ns index
  (:require [scicloj.kindly.v3.kind :as kind]
            [scicloj.kindly-default.v1.api :refer [md]]
            [tablecloth.api :as tc]
            [scicloj.note-to-test.v1.api :as note-to-test]))

%s")))

(->> example-doc
     markdown->markdown-with-extracted-clojure-chunks
     markdown-with-extracted-clojure-chunks->clojure
     (spit "notebooks/index.clj"))
