# Change Log

## [1.0.0-pre-alpha6]

`tech.ml.dataset` version 2.06

### Added

* `asof-join` added

## [1.0.0-pre-alpha4]

### Added

* `reshape` tests
* `pivot->wider` accepts `:drop-missing?` option (default: `true`)

### Changed

* `pivot->wider` drops missing rows by default
* `pivto->wider` order of concatenated column names is reversed (first: colnames, last: value), was opposite.
* `pivot->longer` `:splitter` accepts string used for splitting column name

