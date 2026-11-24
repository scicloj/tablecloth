(ns melody-helpers
  "Plots and audio for the melody notebook. Each takes a dataset and the names of
  numeric columns, so it works with whatever representation the notebook settles on."
  (:require [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc])
  (:import [java.nio ByteBuffer ByteOrder]
           [java.util Base64]))

(defn- rows [ds cols]
  (tc/rows (tc/select-columns ds cols) :as-maps))

(defn piano-roll
  "A bar per note, from `start` to `start` + `length` across, at height `pitch`."
  [ds {:keys [start length pitch]}]
  (kind/vega-lite
   {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
    :width 600
    :height 200
    :data {:values (map (fn [row]
                          {:start (row start)
                           :end (+ (row start) (row length))
                           :low (- (row pitch) 0.4)
                           :high (+ (row pitch) 0.4)
                           :pitch (row pitch)})
                        (rows ds [start length pitch]))}
    :mark "rect"
    :encoding {:x {:field "start" :type "quantitative" :title (name start)}
               :x2 {:field "end"}
               :y {:field "low" :type "quantitative" :title (name pitch)
                   :scale {:zero false}}
               :y2 {:field "high"}
               :tooltip [{:field "pitch"} {:field "start"} {:field "end"}]}}))

(defn line-chart
  "`y` against `x`, points joined in row order."
  [ds {:keys [x y]}]
  (kind/vega-lite
   {:$schema "https://vega.github.io/schema/vega-lite/v5.json"
    :width 600
    :height 200
    :data {:values (rows ds [x y])}
    :mark {:type "line" :point true}
    :encoding {:x {:field (name x) :type "quantitative"}
               :y {:field (name y) :type "quantitative" :scale {:zero false}}}}))

(defn midi->hz
  "Frequency of a MIDI note number; 69 is A4 = 440 Hz."
  [midi]
  (* 440.0 (Math/pow 2.0 (/ (- midi 69) 12.0))))

(def ^:private sample-rate 11025)

(defn- tone [hz seconds]
  (let [n (long (* seconds sample-rate))
        fade (min (quot n 2) (long (* 0.01 sample-rate)))]
    (map (fn [i]
           (* 0.3
              (min 1.0 (/ i (double fade)) (/ (- n i) (double fade)))
              (Math/sin (/ (* 2 Math/PI hz i) sample-rate))))
         (range n))))

(defn- wav
  "16-bit mono PCM WAV bytes for samples in [-1, 1]."
  [samples]
  (let [n (count samples)
        buf (doto (ByteBuffer/allocate (+ 44 (* 2 n)))
              (.order ByteOrder/LITTLE_ENDIAN))]
    (.put buf (.getBytes "RIFF" "US-ASCII"))
    (.putInt buf (+ 36 (* 2 n)))
    (.put buf (.getBytes "WAVEfmt " "US-ASCII"))
    (.putInt buf 16)
    (.putShort buf (short 1))
    (.putShort buf (short 1))
    (.putInt buf sample-rate)
    (.putInt buf (* 2 sample-rate))
    (.putShort buf (short 2))
    (.putShort buf (short 16))
    (.put buf (.getBytes "data" "US-ASCII"))
    (.putInt buf (* 2 n))
    (doseq [s samples]
      (.putShort buf (short (Math/round (* 32767.0 (double s))))))
    (.array buf)))

(defn audio
  "An audio player for the notes in row order, one sine tone each.
  `length` is in beats. `pitch` is a MIDI note number unless `:pitch->hz` says otherwise."
  [ds {:keys [pitch length]} & {:keys [bpm pitch->hz] :or {bpm 100 pitch->hz midi->hz}}]
  (let [seconds-per-beat (/ 60.0 bpm)
        samples (mapcat (fn [row]
                          (tone (pitch->hz (row pitch))
                                (* seconds-per-beat (row length))))
                        (rows ds [pitch length]))]
    (kind/hiccup
     [:audio {:controls true
              :src (str "data:audio/wav;base64,"
                        (.encodeToString (Base64/getEncoder) (wav samples)))}])))
