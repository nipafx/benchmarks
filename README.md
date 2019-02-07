# Benchmark Lab

Arbitrary Java performance benchmarks, implemented with [JMH](http://openjdk.java.net/projects/code-tools/jmh/).
You can run them with:

    mvn clean install
    java -jar target/benchmarks.jar <BENCHMARK_CLASS_NAME>

Benchmarks:

* [`Stream`](#stream)
* [wrapping in `Collections::unmodifiableList`](#unmodifiablelist)
* [removing from `ArrayList`](#arraylistremoveat)


## Stream

The benchmark code for the posts [_Stream Performance_](http://blog.codefx.org/java/stream-performance/) and [_Stream Performance - Your Ideas_](http://blog.codefx.org/java/stream-performance-your-ideas/).
Read them for details on the setup.

### Code

Package: [`org.codefx.lab.benchmarks.stream`](src/main/java/org/codefx/lab/benchmarks/stream)

For the first post, benchmarks are split into three classes that cover operations of different complexity:

* [`SimpleOperationsBenchmark`](src/main/java/org/codefx/lab/benchmarks/stream/SimpleOperationsBenchmark.java): integer comparison and addition
* [`MediumOperationsBenchmark`](src/main/java/org/codefx/lab/benchmarks/stream/MediumOperationsBenchmark.java): a handful of multiplications
* [`ComplexOperationsBenchmark`](src/main/java/org/codefx/lab/benchmarks/stream/ComplexOperationsBenchmark.java): object creation and string manipulation

And for the second post:

* [`CommentOperationsBenchmark`](src/main/java/org/codefx/lab/benchmarks/stream/CommentOperationsBenchmark.java): benchmarks according to your ideas

There's also an unpublished experiment:
* [`ControlStructuresBenchmark`](src/main/java/org/codefx/lab/benchmarks/stream/ControlStructuresBenchmark.java): benchmarks for various control structures

To tweak the benchmarks, take a look into their superclass [`AbstractIterationBenchmark`](src/main/java/org/codefx/lab/benchmarks/stream/AbstractIterationBenchmark.java).

### Results

The results are collected in [this Google Spreadsheet](https://docs.google.com/spreadsheets/d/1K-y44zFrBWpZXkdaBI80-g_MqJiuphmuZAP6gg6zz_4/edit#gid=1205798000).


## Unmodifiable List

Following up [on this conversation](https://twitter.com/gunnarmorling/status/1081228251094237185), I benchmarked how wrapping lists into `Collections::unmodifiableList` impacts performance.

### Code

* Package: [`org.codefx.lab.benchmarks.nested_unmodifiable`](src/main/java/org/codefx/lab/benchmarks/nested_unmodifiable)
* Benchmark class name: [`NestedUnmodifiableBenchmark`](src/main/java/org/codefx/lab/benchmarks/nested_unmodifiable/NestedUnmodifiableBenchmark.java)

### Results

| Benchmark    |  Depth  |     Score      Error  Units |
| ------------ | -------:| ---------------------------:|
| forEach      |      0  |     6.482 ±    0.163  ms/op |
| forEach      |      1  |     6.551 ±    0.092  ms/op |
| forEach      |     10  |     6.659 ±    0.270  ms/op |
| forEach      |    100  |     6.666 ±    0.066  ms/op |
| forEach      |  1_000  |     6.703 ±    0.180  ms/op |
| iterator     |      0  |     5.555 ±    0.038  ms/op |
| iterator     |      1  |     7.576 ±    0.232  ms/op |
| iterator     |     10  |    35.480 ±    0.435  ms/op |
| iterator     |    100  |   415.162 ±    2.583  ms/op |
| iterator     |  1_000  |  4813.140 ±   29.506  ms/op |
| linearAccess |      0  |     4.057 ±    0.020  ms/op |
| linearAccess |      1  |     5.222 ±    0.131  ms/op |
| linearAccess |     10  |    33.361 ±    0.287  ms/op |
| linearAccess |    100  |   422.536 ±    1.455  ms/op |
| linearAccess |  1_000  |  4691.470 ±   47.073  ms/op |
| linearAccess | 10_000  | 57178.027 ± 1238.238  ms/op |
| randomAccess |      0  |     8.126 ±    0.390  ms/op |
| randomAccess |      1  |     8.347 ±    0.322  ms/op |
| randomAccess |     10  |    30.222 ±    4.118  ms/op |
| randomAccess |    100  |   258.086 ±    7.145  ms/op |
| randomAccess |  1_000  |  2508.090 ±   32.911  ms/op |
| randomAccess | 10_000  | 29827.984 ± 1306.416  ms/op |


## ArrayList::removeAt

Given a large `ArrayList` and a bunch of indices - what's a good way to get a list without the elements at those indices?

### Code

* Package: [`org.codefx.lab.benchmarks.arraylist_removeat`](src/main/java/org/codefx/lab/benchmarks/arraylist_removeat)
* Benchmark class name: [`RemoveBenchmark`](src/main/java/org/codefx/lab/benchmarks/arraylist_removeat/RemoveBenchmark.java)

### Results

Some explaining needs to go into these numbers but I'm not going to do that now (too late). 😛

```
Benchmark                       (arrayLength)  (removals)    Mode     Cnt       Score      Error  Units
RemoveBenchmark.baseline              10_000           0  sample  424380      63.505 ±    0.222  us/op
RemoveBenchmark.baseline              10_000          10  sample  435078      61.964 ±    0.216  us/op
RemoveBenchmark.baseline              10_000         100  sample  430968      62.539 ±    0.202  us/op
RemoveBenchmark.baseline              10_000       1_000  sample  432169      62.364 ±    0.298  us/op
RemoveBenchmark.baseline             100_000           0  sample   41510     649.561 ±    2.447  us/op
RemoveBenchmark.baseline             100_000          10  sample   42676     631.752 ±    2.078  us/op
RemoveBenchmark.baseline             100_000         100  sample   41851     644.353 ±    2.262  us/op
RemoveBenchmark.baseline             100_000       1_000  sample   41461     650.318 ±    2.550  us/op
RemoveBenchmark.baseline           1_000_000           0  sample    6250    4318.081 ±   55.947  us/op
RemoveBenchmark.baseline           1_000_000          10  sample    6253    4315.854 ±   48.320  us/op
RemoveBenchmark.baseline           1_000_000         100  sample    6306    4279.591 ±   47.078  us/op
RemoveBenchmark.baseline           1_000_000       1_000  sample    6289    4290.965 ±   52.761  us/op
RemoveBenchmark.copyArrayRemove       10_000           0  sample  422433      63.808 ±    0.212  us/op
RemoveBenchmark.copyArrayRemove       10_000          10  sample  363302      74.184 ±    0.235  us/op
RemoveBenchmark.copyArrayRemove       10_000         100  sample   79192      75.628 ±    0.343  us/op
RemoveBenchmark.copyArrayRemove      100_000           0  sample   41897     643.563 ±    2.697  us/op
RemoveBenchmark.copyArrayRemove      100_000          10  sample   36363     741.439 ±    3.704  us/op
RemoveBenchmark.copyArrayRemove      100_000         100  sample   24082     746.417 ±    3.579  us/op
RemoveBenchmark.copyArrayRemove    1_000_000           0  sample    5934    4548.275 ±   61.058  us/op
RemoveBenchmark.copyArrayRemove    1_000_000          10  sample    4058    6654.678 ±  102.694  us/op
RemoveBenchmark.copyArrayRemove    1_000_000         100  sample    4135    6529.373 ±   94.918  us/op
RemoveBenchmark.iterativeAt           10_000           0  sample  424924      63.429 ±    0.149  us/op
RemoveBenchmark.iterativeAt           10_000          10  sample  409336      65.847 ±    0.171  us/op
RemoveBenchmark.iterativeAt           10_000         100  sample  259490     103.919 ±    0.315  us/op
RemoveBenchmark.iterativeAt           10_000       1_000  sample   65966     408.920 ±    1.295  us/op
RemoveBenchmark.iterativeAt          100_000           0  sample   39867     676.439 ±    2.997  us/op
RemoveBenchmark.iterativeAt          100_000          10  sample   36813     732.621 ±    2.605  us/op
RemoveBenchmark.iterativeAt          100_000         100  sample   19385    1391.798 ±    4.587  us/op
RemoveBenchmark.iterativeAt          100_000       1_000  sample    3730    7242.558 ±   31.338  us/op
RemoveBenchmark.iterativeAt        1_000_000           0  sample    6145    4394.814 ±   55.932  us/op
RemoveBenchmark.iterativeAt        1_000_000          10  sample    4289    6297.320 ±   93.764  us/op
RemoveBenchmark.iterativeAt        1_000_000         100  sample    2089   12943.901 ±  123.726  us/op
RemoveBenchmark.iterativeAt        1_000_000       1_000  sample     315   86580.338 ± 2239.426  us/op
RemoveBenchmark.listIterator          10_000           0  sample  338713      79.565 ±    0.262  us/op
RemoveBenchmark.listIterator          10_000          10  sample  291777      92.398 ±    0.445  us/op
RemoveBenchmark.listIterator         100_000           0  sample   28322     951.927 ±   10.195  us/op
RemoveBenchmark.listIterator         100_000          10  sample   23533    1145.802 ±   11.786  us/op
RemoveBenchmark.listIterator       1_000_000           0  sample    2812    9677.106 ± 1049.759  us/op
RemoveBenchmark.listIterator       1_000_000          10  sample    2010   13512.276 ± 1252.236  us/op
RemoveBenchmark.listIterator       1_000_000         100  sample    1319   13733.767 ± 1505.709  us/op
RemoveBenchmark.listIterator       1_000_000       1_000  sample    1117   13468.484 ± 1633.372  us/op
RemoveBenchmark.nullifyRemove         10_000           0  sample  359501      74.971 ±    0.174  us/op
RemoveBenchmark.nullifyRemove         10_000          10  sample  215591     125.113 ±    0.355  us/op
RemoveBenchmark.nullifyRemove         10_000         100  sample  225774     119.462 ±    0.201  us/op
RemoveBenchmark.nullifyRemove         10_000       1_000  sample  228752     117.899 ±    0.396  us/op
RemoveBenchmark.nullifyRemove        100_000           0  sample   36508     738.993 ±    3.087  us/op
RemoveBenchmark.nullifyRemove        100_000          10  sample   20568    1311.620 ±    5.625  us/op
RemoveBenchmark.nullifyRemove        100_000         100  sample   20882    1291.816 ±    5.561  us/op
RemoveBenchmark.nullifyRemove        100_000       1_000  sample   21246    1269.576 ±    3.442  us/op
RemoveBenchmark.nullifyRemove      1_000_000           0  sample    5680    4751.827 ±   55.961  us/op
RemoveBenchmark.nullifyRemove      1_000_000          10  sample    2585   10454.266 ±  103.232  us/op
RemoveBenchmark.nullifyRemove      1_000_000         100  sample    2698   10016.958 ±  114.831  us/op
RemoveBenchmark.nullifyRemove      1_000_000       1_000  sample    2725    9911.803 ±   82.370  us/op
```
