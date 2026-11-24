## Ode to Joy: source and reuse

`data/ode-to-joy.csv` holds bars 1–8 of the "Ode to Joy" melody: 30 notes, 4/4, G major.

### Work

Ludwig van Beethoven, Symphony No. 9 in D minor, Op. 125, finale. Premiered 1824; first
edition Mainz: Schott, 1826, plate 2322. Beethoven died in 1827, so the work is in the
public domain everywhere.

### Encoding the data was taken from

Mutopia Project piece 528, "Ode to Joy", hymn setting entered by Peter Chubb.

- File: `ftp/BeethovenLv/ode/ode.ly` in https://github.com/MutopiaProject/MutopiaProject
- Revision: commit `11dda3cc7f0727847aedd21d3e8e55b217a54592` (2009-08-05)
- Permalink: https://github.com/MutopiaProject/MutopiaProject/blob/11dda3cc7f0727847aedd21d3e8e55b217a54592/ftp/BeethovenLv/ode/ode.ly
- Header: `copyright = "Public Domain"`, `footer = "Mutopia-2009/08/05-528"`; tagline:
  "This sheet music has been placed in the public domain by the typesetter".
- Key `g \major`, time `4/4`. The CSV is the soprano voice, first two lines:

```
sop =  \transpose c c'{
	b4 b c' d' | d' c' b a | g g a b | b4. a8 a2 \bar "||"
	b4 b c' d' | d' c' b a | g g a b | a4. g8 g2 \bar "||"
```

`\transpose c c'` raises everything an octave, so `b` is B4 and `c'` is C5 (middle C is C4).
A LilyPond duration carries forward until changed: `4` quarter, `4.` dotted quarter,
`8` eighth, `2` half.

### Cross-check

An independent Humdrum transcription agrees note for note and in every duration for bars
1–8: `corpus/Monody/Common/comrep096.krn` in https://github.com/shanahdt/mus7921
(`!!!YOR: Fake Book of the World's Favorite Songs`, 1990; encoded by Brian Luke, 2001).
That repository states no license, so it is used only to check, not copied. Both encodings are of the theme as commonly
sung, not of a specific edition of the score.

### Not verified

- The 1826 Schott score itself. IMSLP and Wikimedia Commons were unreachable from the
  build machine, so the excerpt was not compared to the original scan.
- Key. Beethoven states the theme in D major; this setting is in G major. Bars 1–8 keep
  the same intervals and rhythm, transposed.
- Bars 9–16 are excluded: the two encodings disagree in bar 12 (the Humdrum one ties the
  last note over the barline; the hymn setting does not).
