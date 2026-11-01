# lag/lead exercise scratch files

> **WIP: remove or clean up before opening a PR.** These files are
> scratch work for the lag/lead exercise (`dev/lag_lead_exercise.md`). They
> are on the branch only so the work can be continued from another
> machine. They are not meant to be merged.

| File           | Namespace             | What it is                                                                                                     |
|----------------|-----------------------|----------------------------------------------------------------------------------------------------------------|
| `old.clj`      | `lag-exercise.old`    | Frozen copy of the original rolling-based `lag`, kept as a behavior baseline.                                  |
| `matrix.clj`   | `lag-exercise.matrix` | Test matrix (datatype x missing x k) that runs any 4-arity lag fn so old and new behavior can be diffed.       |
| `scratch.clj`  | `lag-exercise.scratch`| REPL notes: findings on packed types and missing sets, candidate `shift-column`, and benchmark helpers.        |

These are not on the default classpath. Load them by evaluating the file
(`C-c C-k`) in this order: `old`, `matrix`, `scratch`.

## Before the PR

- [ ] Delete `dev/lag_exercise/`, or keep only what is worth keeping.
- [ ] Revert the `criterium/criterium` line in the `:dev` alias of
      `deps.edn`. It is only used by `scratch.clj` for benchmarking.
- [ ] Move anything reusable (the test-matrix cases) into
      `test/tablecloth/column/api/lag_lead_test.clj`.
- [ ] Remove or fix the `Comparint prior art in tablecloth.time` comment
      in `scratch.clj`.
