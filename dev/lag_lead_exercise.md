# Exercise: faster `lag` and `lead`

A guided exercise, not a solution: pointers, constraints, and questions. The
answers and the code are yours.

## 1. Where things live

| What | Where |
|---|---|
| Implementation | `src/tablecloth/api/rolling.clj` |
| Export | `src/tablecloth/api/api_template.clj` |
| Generated API | `src/tablecloth/api.clj` — regenerate with `exporter/write-api!` (template's `comment` block); never hand-edit |
| Tests | `test/tablecloth/api/rolling_test.clj` (midje) |
| Column API (no lag yet) | `src/tablecloth/column/api/` |

Both functions call `tech.v3.dataset.rolling/rolling`: fixed window `k+1`,
`:edge-mode :zero`, reducer `first` / `last`.

## 2. The observation

`lag` is a positional shift: output row `i` holds input row `i-k`. One input
per row means no window is needed.

The rolling engine builds one per row anyway. In TMD's
`tech/v3/dataset/rolling.clj`, read `apply-window-ranges` and find:

- the `:datatype` the `first` / `last` reducers declare, and what `dtype/emap`
  then produces;
- where the result's datatype is decided;
- whether the source's missing set is read at all.

## 3. Primitives

TMD 8.024, `tech.v3.dataset.column` (bodies in `tech.v3.dataset.impl.column`):

- `select` — a row range of a column. What happens to its missing set?
- `prepend-column-with-empty` / `extend-column-with-empty` — add `n` missing
  rows at the front / back. Read the bodies:
  - Which datatype do they allocate? (Look for `packing/unpack-datatype`.)
  - How is the source missing set carried over? Why does only one need
    `bitmap/offset`?
  - What fills the new slots, and where does that value come from?
- `new-column`, 4-arity — data, metadata, and missing set, without a rescan.
- `tech.v3.datatype.packing` — `packed-datatype?`, `pack`, `unpack-datatype`.

Clue: two of these, composed, are most of a lag. The real work is around them.

## 4. Invariants

- **Positional.** By row, not timestamp. Say so in the docstring.
- **Length.** Output length equals input length for every `k`.
- **Datatype.** Output matches input, including `:int64`, `:string`,
  `:boolean`, and packed temporals such as `:packed-local-date`.
- **Source missingness.** A missing source value stays missing at its new row.
- **Boundary.** Numeric boundaries are `0`, object boundaries `nil`. Is that
  `0` recorded as missing? Check at the REPL.
- **`k = 0`.** Same column, or a copy? Does it matter?
- **`k >= n`.** All boundary, or an error?
- **Invalid `k`.** Negative, non-integer, `nil`: what happens today?
- **Grouped datasets.** Does `tc/lag` shift per group today? Should it?

## 5. Compatibility

`lag` and `lead` shipped in Tablecloth 8.021 and 8.024 on Clojars (the
CHANGELOG still lists them as unreleased). Callers may already depend on:

- the 3- and 4-arity signatures, with the new column name in second position;
- the default name `<col>-lag-<k>`;
- `0` at numeric boundaries, which `rolling_test.clj` pins;
- whatever result datatype and missing set the rolling path produces today.

Which of these is contract and which is accident is part of exercise 4.

## 6. Benchmark context (indicative)

An exploratory tablecloth.time run, lag 1, one million `:float64` rows, median:

| Implementation | ~ms |
|---|---:|
| Tablecloth rolling `lag` | 46 |
| Former tablecloth.time lazy `concat` | 241 |
| Direct typed-column prototype | 5.3 |

One machine, one JVM, and the rolling row ran `tc/lag`'s body on **TMD 7.062**,
not 8.024. Expectations only; re-measure before quoting.

## 7. A trustworthy before/after

1. Copy the current `lag` body into a scratch file outside `src/` so old and
   new run in one JVM. The `:dev` alias in `deps.edn` gives a REPL.
2. Seeded inputs: `:float64`, `:int64`, `:string`, `:local-date` at 1e3, 1e5,
   1e6 rows.
3. Force each result (read the last element); an unforced lazy reader measures
   nothing.
4. Warm up (iterations *and* at least a second), `System/gc`, then 15+ timed
   runs. Report median, min, max.
5. First assert old/new parity on values, missing set, and datatype. A fast
   wrong answer is the usual failure.
6. Record JVM version, heap, and core count.

## 8. Prior art

`scicloj/tablecloth.time` PR #67, commit `14a92fc`:

- `src/tablecloth/time/column/api.clj` — `shift`, `lag`, `lead`, and private
  fill and packed-datatype helpers. Column in, column out.
- `src/tablecloth/time/api.clj` — `add-lag`, `add-lead`, `add-lags`,
  `add-leads`.
- `bench/lag_lead_bench.clj` — the benchmark behind section 6.

Do section 9 first, then compare. Note the differences: tablecloth.time
defaults boundaries to *missing*; Tablecloth defaults numeric ones to `0`.

## 9. Exercises

End each step in a REPL result or a failing test, not a guess.

1. **Pin current behavior.** For `:float64`, `:int64`, `:string`,
   `:local-date`, with and without a missing source value, and `k` in
   `0, 1, n, n+1, -1`: record datatype, values, missing set. What surprises you?
2. **Explain the cost.** Name the per-row work the rolling path does that a
   shift does not.
3. **Shift without the window.** Build a lag from section 3 and run
   `rolling_test.clj`. What fails, and why? What is newly in the missing set?
4. **Options and compatibility.** TODO: keep `0` as the default, add an option,
   or change the default as a recorded break? How does an option fit beside the
   positional `new-column-name` arity?
5. **Datatype-safe fill.** TODO: which writer method per type for `:int64`,
   `:float32`, `:boolean`? What should a `1.5` fill into `:int64` do? Do filled
   slots leave the missing set?
6. **Packed temporals.** TODO: run step 3 on `:local-date` and `:instant`.
   Compare result and source datatypes. Where did it change, and what restores
   it without a per-row loop?
7. **Helper location.** TODO: a column-level shift in `tablecloth.column.api`,
   private in `tablecloth.api.rolling`, or proposed to TMD beside
   `prepend-column-with-empty`. Which makes the other two simplest?
8. **Test matrix.** TODO: datatypes × `k` (`0`, `1`, `n-1`, `n`, `n+1`,
   negative) × source-missing × fill/no-fill × empty dataset × `lag`/`lead`.
   Which cells are redundant? Which must assert datatype and missing set, not
   just values?
9. **Measure.** Run section 7. Does the gap hold on TMD 8.024, and for
   `:string`?
10. **Scope.** TODO: multi-lag helpers (`add-lags`-style), now or later? What
    would they need from the step-7 helper?
