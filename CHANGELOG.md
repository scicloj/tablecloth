# Change Log

## [5.00-beta-21]

* `concat-copying` exposed.
* `split` function for splitting into train-test pairs with `:kfold`, `:bootstrap`, `:loo` and `holdout` strategies + stratified versions
* `replace-missing` with new strategy `:midpoint`

## [5.00-beta-5a]

### Fixed

* column names should keep order for provided names (#9)

## [5.00-beta-5]

t.m.d update

## [5.00-beta-3]

t.m.d update

### Changed

* contribution guide in readme

## [5.00-beta-2]

t.m.d update

### Changed

* `write-nippy!` and `read-nippy` are deprecated, replaced by `write!` and `dataset`

## [5.0-SNAPSHOT]

`tech.ml.dataset` version 5.0-alpha*

### Added

* `map-columns` accepts optional target datatype
* `ds/column->dataset` functionality introduced in `separate-column`
* more datatypes included for conversion (`:text` among others)

### Changed

* `write-csv!` replaced by `write!` (`write-csv!` is marked as deprecated)
* `info` field `:size` is replaced by `:n-elems`
* [breaking] `separate-column` 3-arity version accepts `separator` instead `target-columns` now

### Fixed

* do not skip 1-row DS when folding
* do not attempt to fold empty dataset

## [4.04]

`tech.ml.dataset` version 4.04

### Added

* tests: dataset

### Changed

* version number to match t.m.dataset version
* documentation:
    - gfm renderer for markdown
    
### Fixed

* code block language alignment fix in css

## [1.0.0-pre-alpha9]

`tech.ml.dataset` version 4.03

### Added

* some operations on grouped dataset can be parallel (`parallel?` option set to `true`). These are: `aggregate`, `unique-by`, `order-by`, `join-columns`, `separate-columns`, `ungroup`

### Fixed

* #2 - docs typo
* #3 - recover datatypes after ungrouping

### Changed

* `aggregation` uses now in-place ungrouping which is much faster

## [1.0.0-pre-alpha8]

`tech.ml.dataset` version 3.06

### Added

* `fill-range-replace` to inject data to make continuous seqence in column
* `write-nippy!` and `read-nippy`

## [1.0.0-pre-alpha7]

`tech.ml.dataset` version 2.13

### Added

* `replace-missing` new strategies: `:mid` and `:lerp`, working also for dates.

### Changed

* [breaking] `replace-missing` has different conctract and default strategy `:mid`. `value` argument is the last argument now.
* [breaking] `replace-missing` `:up` and `:down` strategies, when `value` is `nil` fills border missing values with nearest value.

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

