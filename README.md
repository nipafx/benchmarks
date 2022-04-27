# Benchmark Lab

Arbitrary Java performance benchmarks, implemented with [JMH](http://openjdk.java.net/projects/code-tools/jmh/).
You can run them with:

	mvn package
	java --enable-preview --add-modules=jdk.incubator.vector -jar target/benchmarks.jar ${BENCHMARK}

Replace `${BENCHMARK}` with either a package or class name to run just the specified tests.

Benchmarks:

* [`Stream`](#stream)
* [vectorization in parallel streams](#parallel-stream-vectorization)
* [`Stream::mapMulti`](#stream-mapmulti)
* [wrapping in `Collections::unmodifiableList`](#unmodifiable-list)
* [removing from `ArrayList`](#arraylistremoveat)
* [helpful NPE messages](#helpful-npe-messages)
* [vector API](#vector-api)


## Stream

The benchmarks for the posts [_Stream Performance_](https://nipafx.dev/java-stream-performance) and [_Stream Performance - Your Ideas_](https://nipafx.dev/java-stream-performance-your-ideas).
Read them for details on the setup.

### Code

* **Package**: [`dev.nipafx.lab.benchmarks.stream`](src/main/java/dev/nipafx/lab/benchmarks/stream)
* **Classes**:
	* [`SimpleOperationsBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/SimpleOperationsBenchmark.java): integer comparison and addition (first post)
	* [`MediumOperationsBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/MediumOperationsBenchmark.java): a handful of multiplications (first post)
	* [`ComplexOperationsBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/ComplexOperationsBenchmark.java): object creation and string manipulation (first post)
	* [`CommentOperationsBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/CommentOperationsBenchmark.java): benchmarks according to your ideas (second post)
	* [`ControlStructuresBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/ControlStructuresBenchmark.java): benchmarks for various control structures (unpublished experiment)

To tweak the benchmarks, take a look into their superclass [`AbstractIterationBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/AbstractIterationBenchmark.java).

### Results

The results are collected in [this Google Spreadsheet](https://docs.google.com/spreadsheets/d/1K-y44zFrBWpZXkdaBI80-g_MqJiuphmuZAP6gg6zz_4/edit#gid=1205798000).


## Parallel Stream Vectorization

Benchmarks for [Heinz Kabutz' tweet](https://twitter.com/heinzkabutz/status/1286603068990160898) on computing factorials with a parallel stream, where parallelization increased performance by more than 1x per core.

### Code

* **Package**: [`dev.nipafx.lab.benchmarks.stream.vectorization`](src/main/java/dev/nipafx/lab/benchmarks/stream/vectorization)
* **Classes**: [`VectorizationBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/vectorization/VectorizationBenchmark.java)

### Results

| Benchmark | (number) | (parallel) |     Score Units |
| --------- | -------- | ---------- | ---------------:|
| factorial |   200000 |      false | 12267.158 ms/op |
| factorial |   200000 |       true |   166.953 ms/op |

This was done on a Ryzen 9 3900X, which has 24 logical cores, so you would expect a speedup of at most 24x, not the measured ~70x.


## Stream mapMulti

Benchmarks for [the exploration of `Stream::mapMulti`](https://nipafx.dev/java-16-stream-mapmulti).

### Code

* **Package**: [`dev.nipafx.lab.benchmarks.stream.map_multi`](src/main/java/dev/nipafx/lab/benchmarks/stream/map_multi)
* **Classes**: [`MapMulitBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/stream/map_multi/MapMulitBenchmark.java)

### Results

| Benchmark      |  %0s |  Size     | Score         Error  Units |
| -------------- | ----:| ---------:|---------------,-----------:|
| flatMap_count  | 0.01 |    10_000 |    90.743 ±   5.377  us/op |
| flatMap_count  | 0.01 |   100_000 |   932.125 ±  44.921  us/op |
| flatMap_count  | 0.01 | 1_000_000 |  9509.989 ± 140.587  us/op |
| flatMap_count  |  0.1 |    10_000 |    87.546 ±   1.867  us/op |
| flatMap_count  |  0.1 |   100_000 |   953.468 ±  13.979  us/op |
| flatMap_count  |  0.1 | 1_000_000 | 10392.573 ± 432.308  us/op |
| flatMap_count  |  0.5 |    10_000 |   101.504 ±   3.363  us/op |
| flatMap_count  |  0.5 |   100_000 |  1150.309 ±  17.065  us/op |
| flatMap_count  |  0.5 | 1_000_000 |  1561.187 ± 324.065  us/op |
| flatMap_count  |  0.8 |    10_000 |    76.758 ±   7.455  us/op |
| flatMap_count  |  0.8 |   100_000 |   875.666 ±  22.733  us/op |
| flatMap_count  |  0.8 | 1_000_000 |  8861.991 ± 207.893  us/op |
| flatMap_sum    | 0.01 |    10_000 |   113.009 ±   6.977  us/op |
| flatMap_sum    | 0.01 |   100_000 |  1158.694 ±  74.973  us/op |
| flatMap_sum    | 0.01 | 1_000_000 |  1622.151 ± 533.694  us/op |
| flatMap_sum    |  0.1 |    10_000 |   108.073 ±   1.227  us/op |
| flatMap_sum    |  0.1 |   100_000 |  1155.964 ±  54.148  us/op |
| flatMap_sum    |  0.1 | 1_000_000 |  1777.393 ± 453.216  us/op |
| flatMap_sum    |  0.5 |    10_000 |   113.230 ±   5.485  us/op |
| flatMap_sum    |  0.5 |   100_000 |  1284.879 ±  63.869  us/op |
| flatMap_sum    |  0.5 | 1_000_000 |  2906.395 ± 259.311  us/op |
| flatMap_sum    |  0.8 |    10_000 |    79.432 ±   5.875  us/op |
| flatMap_sum    |  0.8 |   100_000 |   936.235 ±  16.178  us/op |
| flatMap_sum    |  0.8 | 1_000_000 |  9300.164 ± 222.237  us/op |
| mapMulti_count | 0.01 |    10_000 |    11.021 ±   3.722  us/op |
| mapMulti_count | 0.01 |   100_000 |   147.080 ±  35.460  us/op |
| mapMulti_count | 0.01 | 1_000_000 |  1936.868 ±  54.057  us/op |
| mapMulti_count |  0.1 |    10_000 |     8.703 ±   1.680  us/op |
| mapMulti_count |  0.1 |   100_000 |   148.165 ±   4.892  us/op |
| mapMulti_count |  0.1 | 1_000_000 |  1876.445 ±  76.954  us/op |
| mapMulti_count |  0.5 |    10_000 |    14.665 ±   6.894  us/op |
| mapMulti_count |  0.5 |   100_000 |   374.459 ±  20.920  us/op |
| mapMulti_count |  0.5 | 1_000_000 |  4005.131 ±  62.601  us/op |
| mapMulti_count |  0.8 |    10_000 |     5.489 ±   0.437  us/op |
| mapMulti_count |  0.8 |   100_000 |   186.849 ±   4.387  us/op |
| mapMulti_count |  0.8 | 1_000_000 |  1842.542 ±  80.950  us/op |
| mapMulti_sum   | 0.01 |    10_000 |    18.161 ±   0.632  us/op |
| mapMulti_sum   | 0.01 |   100_000 |   183.985 ±   3.982  us/op |
| mapMulti_sum   | 0.01 | 1_000_000 |  2309.631 ±  60.015  us/op |
| mapMulti_sum   |  0.1 |    10_000 |    16.771 ±   0.379  us/op |
| mapMulti_sum   |  0.1 |   100_000 |   187.113 ±   9.438  us/op |
| mapMulti_sum   |  0.1 | 1_000_000 |  2333.962 ±  72.094  us/op |
| mapMulti_sum   |  0.5 |    10_000 |     9.876 ±   0.206  us/op |
| mapMulti_sum   |  0.5 |   100_000 |   393.450 ±  19.738  us/op |
| mapMulti_sum   |  0.5 | 1_000_000 |  4327.512 ± 394.266  us/op |
| mapMulti_sum   |  0.8 |    10_000 |     7.022 ±   0.160  us/op |
| mapMulti_sum   |  0.8 |   100_000 |   218.717 ±  24.815  us/op |
| mapMulti_sum   |  0.8 | 1_000_000 |  2024.602 ±  62.695  us/op |


## Unmodifiable List

The benchmarks for the post [_Can `instanceof` make Unmodifiable Collections faster?_](https://www.opsian.com/blog/can-instanceof-make-unmodifiable-collections-faster/) on [Opsian's blog](https://www.opsian.com/blog/), which follows up on [a Twitter conversation](https://twitter.com/gunnarmorling/status/1081228251094237185) and measures how wrapping lists into `Collections::unmodifiableList` impacts performance.

### Code

* **Package**: [`dev.nipafx.lab.benchmarks.nested_unmodifiable`](src/main/java/dev/nipafx/lab/benchmarks/nested_unmodifiable)
* **Classes**:
	* [`NestedUnmodifiableSetBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/nested_unmodifiable/NestedUnmodifiableSetBenchmark.java): benchmarks a few operations on nested sets
	* [`NestedUnmodifiableListBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/nested_unmodifiable/NestedUnmodifiableListBenchmark.java): benchmarks a few more operations on nested lists 
	* [`WrappingBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/nested_unmodifiable/WrappingBenchmark.java): benchmarks impact of adding `instanceof` to `Collections::unmodifiable...`.

To tweak the benchmarks, take a look into their superclass [`AbstractIterationBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/nested_unmodifiable/NestedBenchmark.java).

### Results

Find all results in [this Google Spreadsheet](https://docs.google.com/spreadsheets/d/1RTpAuRQr5G9otSxAvMFjNBvzBnklo6qwyZGi_CuwmUQ/edit?usp=sharing).
Here's the section for `ArrayList` with 1 million elements, which is representative for the overall results:

| Benchmark    | Depth |       Score      Error  Units |
| ------------ | -----:| -----------------------------:|
| forEach      |     0 |    6499.524 ±   116.113 μs/op |
| forEach      |     1 |    6466.625 ±    79.003 μs/op |
| forEach      |    10 |    6572.141 ±    49.209 μs/op |
| forEach      |   100 |    6429.473 ±    35.505 μs/op |
| forEach      |  1000 |    6756.348 ±   103.719 μs/op |
| isEmpty      |     0 |       0.003 ±     0.001 μs/op |
| isEmpty      |     1 |       0.004 ±     0.001 μs/op |
| isEmpty      |    10 |       0.015 ±     0.001 μs/op |
| isEmpty      |   100 |       0.205 ±     0.004 μs/op |
| isEmpty      |  1000 |       2.420 ±     0.020 μs/op |
| iterator     |     0 |    5729.565 ±   429.593 μs/op |
| iterator     |     1 |    7343.901 ±   334.612 μs/op |
| iterator     |    10 |   35199.093 ±   285.387 μs/op |
| iterator     |   100 |  417297.905 ±  9552.693 μs/op |
| iterator     |  1000 | 4778937.048 ± 46199.758 μs/op |
| linearAccess |     0 |    3774.128 ±    39.618 μs/op |
| linearAccess |     1 |    4714.176 ±    22.733 μs/op |
| linearAccess |    10 |   30394.111 ±   218.719 μs/op |
| linearAccess |   100 |  417612.502 ±  4279.777 μs/op |
| linearAccess |  1000 | 4661914.890 ± 30868.705 μs/op |
| randomAccess |     0 |    7231.825 ±   587.417 μs/op |
| randomAccess |     1 |    7406.953 ±   692.301 μs/op |
| randomAccess |    10 |   26135.629 ±   688.995 μs/op |
| randomAccess |   100 |  243328.907 ±  6699.941 μs/op |
| randomAccess |  1000 | 2457828.925 ± 17852.538 μs/op |

And here are the numbers for `WrappingBenchmark`:

| Benchmark            | Depth  |    Score      Error  Units |
| -------------------- | ------:| --------------------------:|
| originalUnmodifiable |      1 |     8.508 ±    0.770 ns/op |
| originalUnmodifiable |     10 |    50.997 ±    2.696 ns/op |
| originalUnmodifiable |    100 |   413.526 ±   74.531 ns/op |
| originalUnmodifiable |  1_000 |  3405.815 ±  382.755 ns/op |
| originalUnmodifiable | 10_000 | 32218.869 ± 3729.403 ns/op |
| withInstanceOfCheck  |      1 |     6.236 ±    0.514 ns/op |
| withInstanceOfCheck  |     10 |    15.241 ±    0.686 ns/op |
| withInstanceOfCheck  |    100 |   108.349 ±    1.621 ns/op |
| withInstanceOfCheck  |  1_000 |   964.785 ±   19.586 ns/op |
| withInstanceOfCheck  | 10_000 |  9575.831 ±  197.473 ns/op |


## ArrayList::removeAt

Given a large `ArrayList` and a bunch of indices - what's a good way to get a list without the elements at those indices?

### Code

* **Package**: [`dev.nipafx.lab.benchmarks.arraylist_removeat`](src/main/java/dev/nipafx/lab/benchmarks/arraylist_removeat)
* **Classes**: [`RemoveBenchmark`](src/main/java/dev/nipafx/lab/benchmarks/arraylist_removeat/RemoveBenchmark.java)

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


## Helpful NPE Messages

In Java 14, [JEP 358](https://openjdk.java.net/jeps/358) introduced more detailed `NullPointerException` messages.
They're deactiveated by default for various reasons, one of them performance:

> The algorithm adds some overhead to the production of a stack trace.
> However, this is comparable to the stack walking done when raising the exception.
> If an application frequently throws and prints messages so that the printing affects performance, already throwing the exception imposes an overhead that definitely should be avoided.

How much overhead do the messages add?

### Code

* **Package**: [`dev.nipafx.lab.benchmarks.npe`](src/main/java/dev/nipafx/lab/benchmarks/npe)
* **Classes**:
	* [`NpeBenchmarks`](src/main/java/dev/nipafx/lab/benchmarks/npe/NpeBenchmarks.java)

To compare numbers, run the benchmark with (`-XX:+ShowCodeDetailsInExceptionMessages`) and without (`-XX:-ShowCodeDetailsInExceptionMessages`) detailed messages.

### Preliminary Results

With detailed message:

```
Benchmark                                (checkMessage)  Mode  Cnt        Score        Error  Units
NpeBenchmarks.throwNpeFromNullArrayEntry          false  avgt    3        3,924 ±      0,416  ns/op
NpeBenchmarks.throwNpeFromNullArrayEntry           true  avgt    3       26,551 ±      5,265  ns/op
NpeBenchmarks.throwNpeFromNullInstance            false  avgt    3        3,943 ±      0,764  ns/op
NpeBenchmarks.throwNpeFromNullInstance             true  avgt    3       28,233 ±     15,923  ns/op
NpeBenchmarks.throwNpeFromNullReturn              false  avgt    3        4,020 ±      0,269  ns/op
NpeBenchmarks.throwNpeFromNullReturn               true  avgt    3       27,173 ±      8,789  ns/op
NpeBenchmarks.throwNpeManually                    false  avgt    3      919,585 ±     92,235  ns/op
NpeBenchmarks.throwNpeManually                     true  avgt    3     1429,069 ±    103,055  ns/op
```

Without detailed message:

```
Benchmark                                (checkMessage)  Mode  Cnt        Score        Error  Units
NpeBenchmarks.throwNpeFromNullArrayEntry          false  avgt    3        3,907 ±      0,991  ns/op
NpeBenchmarks.throwNpeFromNullArrayEntry           true  avgt    3       21,764 ±      4,887  ns/op
NpeBenchmarks.throwNpeFromNullInstance            false  avgt    3        3,910 ±      0,434  ns/op
NpeBenchmarks.throwNpeFromNullInstance             true  avgt    3       21,098 ±      0,948  ns/op
NpeBenchmarks.throwNpeFromNullReturn              false  avgt    3        3,875 ±      0,192  ns/op
NpeBenchmarks.throwNpeFromNullReturn               true  avgt    3       21,593 ±      0,409  ns/op
NpeBenchmarks.throwNpeManually                    false  avgt    3      924,070 ±    194,407  ns/op
NpeBenchmarks.throwNpeManually                     true  avgt    3      941,316 ±    172,077  ns/op
```


## Vector API

The vector API promises reliable vectorization of numerical and logical computations, particularly in loops.
The comparison is between "regular" loops (that may be auto-vectorized by the JIT) and vectorized loops.

### Code

* **Package**: [`dev.nipafx.lab.benchmarks.vector`](src/main/java/dev/nipafx/lab/benchmarks/vector)
* **Classes**:
	* [`ImageColors`](src/main/java/dev/nipafx/lab/benchmarks/vector/ImageColors.java): does some simple pixel-by-pixel color manipulation - benchmark for [this vector demo](https://github.com/nipafx/demo-java-x/blob/main/src/main/java/dev/nipafx/demo/java_next/api/vector/ImageColors.java)

### Preliminary Results

```
Benchmark                            Mode  Cnt       Score       Error  Units
ImageColors.invertColors             avgt    9   52158.239 ± 19513.007  us/op
ImageColors.invertColors_vectorized  avgt    9   15036.490 ±  4313.103  us/op
ImageColors.purpleShift              avgt    9  217830.220 ± 93890.988  us/op
ImageColors.purpleShift_vectorized   avgt    9   24536.969 ±  9002.955  us/op
ImageColors.rotateColors             avgt    9   49739.629 ± 19339.702  us/op
ImageColors.rotateColors_vectorized  avgt    9   14960.650 ±  4031.095  us/op
```

This was taken on my laptop without shutting down other programs, so they're not very reliable.
