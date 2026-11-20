^:kindly/hide-code
(ns ode-to-joy
  (:require [melody-helpers :as mh]
            [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]
            [tablecloth.column.api :as tcc]))

^:kindly/hide-code
(def md
  (comp kindly/hide-code kind/md))

(md "
# Ode to Joy, one column at a time

A melody is a few short sequences that line up: which note, in what order, for how long.
That makes it a small dataset for learning the Column API.

The notes are bars 1–8 of the \"Ode to Joy\" theme from Beethoven's Ninth Symphony
(1824), from a public-domain encoding. Source, license and checks are in
`notebooks/melody/SOURCE.md`.

## The data

Each row is a note, in the order it is played. `bar` is the bar it falls in, `pitch`
its name and octave (C4 is middle C), `duration` its written length.
")

(def melody
  (tc/dataset "data/ode-to-joy.csv" {:key-fn keyword}))

melody

(md "
## Helpers

`melody-helpers` draws and plays notes. Every function takes a dataset and the names of
numeric columns: `piano-roll` and `audio` need pitch, start and length as numbers;
`line-chart` plots any column against another. `audio` reads pitch as a MIDI note number
(60 is middle C) unless given `:pitch->hz`, and length in beats.

They are shown here on three made-up notes, not the melody.
")

(def example
  (tc/dataset {:start [0 1 2]
               :length [1 1 2]
               :pitch [60 62 64]}))

(mh/piano-roll example {:start :start :length :length :pitch :pitch})

(mh/line-chart example {:x :start :y :pitch})

(mh/audio example {:pitch :pitch :length :length} :bpm 100)

(md "
## Representing the melody

To be decided before the analysis begins.

### Pitch

### Order and onset

### Duration
")
