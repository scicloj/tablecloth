# Change Log

## [unreleased]

### Fixed

* predicates should behave as in Clojure ([discussion](https://clojurians.zulipchat.com/#narrow/stream/151924-data-science/topic/tablecloth.2Fselect-rows.20predicate.20requirements.3F))

## [6.090]

TMD version bump

### Changed

[breaking]

`replace-missing` up/down strategies clarified. `:down` is replaced by `:downup` and `:up` is replaced by `:updown`. `:down` and `:up` work only in one direction now.

https://github.com/techascent/tech.ml.dataset/issues/305

## [6.088.1]

### Fixed

* Wrong way of selecting columns for joins (shouldn't be a set). https://clojurians.zulipchat.com/#narrow/stream/151924-data-science/topic/complete.20ala.20R/near/286277344

## [6.088]

### Added

* `data frame` term in the title of docs ([discussion](https://app.slack.com/client/T03RZGPFR/threads/thread/C03RZGPG3-1649857892.946909))
* joins can accept different names for left/right datasets
* `cross-join`, `expand` and `complete` introduced

### Changed

* removed setting `*warn-on-reflection*`
* [breaking] creation of singleton dataset adds an error message as a column by default ([discussion](https://clojurians.zulipchat.com/#narrow/stream/236259-tech.2Eml.2Edataset.2Edev/topic/Empty.20csv.20regression.3F.3F))

## [6.076]

Version bump

### Added

* docstring for `unroll` and `fold-by` by @holyjak (#60 and #61) 

## [6.051]

### Fixed

- [#58] - editor friendly api file

## [6.031]

### Fixed

- #57 - InputStream should be dispatched first (the flow now: tries to create a dataset and it fails packs an objet as a singleton

### Changed

- `select-rows` accepts `IFn` for row selection.
- [breaking] #54, #56 -  `pipeline` namespace is stripped, all functions are moved to [metamorph](https://github.com/scicloj/metamorph) library. This is temporary solution before removing this namespace completely. Pipelined versions of functions will be moved to metamorph as well later.

## [6.025]

### Added

* [#49] added docstring to `add-column`

### Fixed

* [#53] summary prefix ignored for aggregate (when fn[ds] is passed)

## [6.023]

### Added

* Documented columns / rows functions [PR52](https://github.com/scicloj/tablecloth/pull/52)
* Reference to original to lifted functions metadata for pipelines [PR51](https://github.com/scicloj/tablecloth/pull/51)

### Changed

* alias for api functions in reference (was: `api`, is: `tc`)

## [6.012]

### Fixed

* `replace-missing` on grouped dataset has swapped arguments

## [6.006]

### Fixed

* `update-columns` on grouped dataset 

## [6.002]

### Changed

* [#43] Align with TMD for dataset creation from a map of sequences.
* [breaking] creation from tensor is `:as-rows` now

## [6.00-beta-16]

### Changed

* [#42] [breaking] `add-column` default strategy is `:strict` now.

## [6.00-beta-10]

### Fixed

* [#41] dataset name not set on tensor path

## [6.00-beta-7]

TMD upgrade, no changes in TC

## [5.17]

TMD upgrade

### Fixed

* [#36] `reorder-columns` on empty dataset returns nil

## [5.11]

### Fixed

* `aggregate-columns` didn't keep column order (#35)

## [5.05.1]

### Added

* `pipeline` functions have `doc` copied from original ones

## [5.05]

### Added

* `split` can turn off shuffling now (`:shuffle?` option)
* `split :holdouts` - sequence of consecutive holdouts

## [5.04]

tech.ml.dataset version bump, this introduces the change of the order of the groups after `group-by` operation

## [5.02]

### Added

* `split :holdout` supports any number of splits (minimum 2) [#28]
* `split` supports `split-names` to provide custom names for subdatasets 
* `concat` and `concat-copying` are working with grouped datasets

### Fixed

* `kfold` split failed on small number of rows (due to `partition-all` behaviour

## [5.01]

### Added

* `split->seq` to return train/test splits as a sequence or datasets or as map of sequences for grouped datasets

### Changed

* [breaking] `tablecloth.pipeline` returns a map with dataset under `:metamorph/data` key (see [metamorph](https://github.com/scicloj/metamorph))
* [breaking] `split` returns now a dataset or grouped dataset with two new columns indicating train/test and split id. See `split->seq` for previous behaviour.

## [5.00-beta-29.1]

### Added

* `without-grouping->` threading macro which allows operations on grouping dataset treated as a regular one.

### Changed

* `group-by` accepts any java.util.Map for a collection of indexes (use LinkedHashMap to persist an order)
* some `tablecloth.api.group-by` functions moved to `tablecloth.api.utils`, no changes to API

## [5.00-beta-29]

### Changed

* `add-or-replace-column(s)` replaced by `add-column(s)` (`add-or-replace-column(s)` is marked as deprecated) (#16)

### Fixed

* `mark-as-group` wasn't visible in API (#18)
* `map-columns` didn't propagate `new-type` for grouped case (#20)
* broken links (#14) in readme

## [5.00-beta-28]

### Added

* `let-dataset` - to simulate `tibble` from R

### Fixed

* Adding a column to an empty dataset returned empty dataset

## [5.00-beta-27]

### Changed

* re-implementation of numerical arrays path dataset creation

## [5.00-beta-25]

### Added

* `rows` and `columns` new result: `:as-double-arrays` - convert rows to 2d double array
* dataset can be created from numerical arrays [discusson](https://clojurians.zulipchat.com/#narrow/stream/236259-tech.2Eml.2Edataset.2Edev/topic/dataset.20.3C-.3E.20jvm.20arrays)

### Fixed

* column from single value should create valid datatype (#10)

## [5.00-beta-21a]

### Added

* `tablecloth.pipeline` for pipeline operations

## [5.00-beta-21]

### Added

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

