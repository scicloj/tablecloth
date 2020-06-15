Introduction
------------

[tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) is a great and fast library which brings columnar dataset to the Clojure. Chris Nuernberger has been working on this library for last year as a part of bigger `tech.ml` stack.

I've started to test the library and help to fix uncovered bugs. My main goal was to compare functionalities with the other standards from other platforms. I focused on R solutions: [dplyr](https://dplyr.tidyverse.org/), [tidyr](https://tidyr.tidyverse.org/) and [data.table](https://rdatatable.gitlab.io/data.table/).

During conversions of the examples I've come up how to reorganized existing `tech.ml.dataset` functions into simple to use API. The main goals were:

-   Focus on dataset manipulation functionality, leaving other parts of `tech.ml` like pipelines, datatypes, readers, ML, etc.
-   Single entry point for common operations - one function dispatching on given arguments.
-   `group-by` results with special kind of dataset - a dataset containing subsets created after grouping as a column.
-   Most operations recognize regular dataset and grouped dataset and process data accordingly.
-   One function form to enable thread-first on dataset.

Important! This library is not the replacement of `tech.ml.dataset` nor a separate library. It should be considered as a addition on the top of `tech.ml.dataset`.

If you want to know more about `tech.ml.dataset` and `tech.ml.datatype` please refer their documentation:

-   [Datatype](https://github.com/techascent/tech.datatype/blob/master/docs/cheatsheet.md)
-   [Date/time](https://github.com/techascent/tech.datatype/blob/master/docs/datetime.md)
-   [Dataset](https://github.com/techascent/tech.ml.dataset/blob/master/docs/walkthrough.md)

Join the discussion on [Zulip](https://clojurians.zulipchat.com/#narrow/stream/236259-tech.2Eml.2Edataset.2Edev/topic/api)

Current version
---------------

![](https://img.shields.io/clojars/v/scicloj/tablecloth)

Documentation
-------------

Please refer [detailed documentation with examples](https://scicloj.github.io/tablecloth/index.html)

Usage example
-------------

``` clojure
(require '[tablecloth.api :as api])
```

``` clojure
(-> "https://raw.githubusercontent.com/techascent/tech.ml.dataset/master/test/data/stocks.csv"
    (api/dataset {:key-fn keyword})
    (api/group-by (fn [row]
                    {:symbol (:symbol row)
                     :year (tech.v2.datatype.datetime.operations/get-years (:date row))}))
    (api/aggregate #(tech.v2.datatype.functional/mean (% :price)))
    (api/order-by [:symbol :year])
    (api/head 10))
```

\_unnamed \[10 3\]:

| :symbol | :year | :summary |
|---------|-------|----------|
| AAPL    | 2000  | 21.75    |
| AAPL    | 2001  | 10.18    |
| AAPL    | 2002  | 9.408    |
| AAPL    | 2003  | 9.347    |
| AAPL    | 2004  | 18.72    |
| AAPL    | 2005  | 48.17    |
| AAPL    | 2006  | 72.04    |
| AAPL    | 2007  | 133.4    |
| AAPL    | 2008  | 138.5    |
| AAPL    | 2009  | 150.4    |

TODO
----

-   tests
-   more information about data manipulation

Licence
-------

Copyright (c) 2020 Scicloj

The MIT Licence
