# Benchmark Lab

Arbitrary Java performance benchmarks, implemented with [JMH](http://openjdk.java.net/projects/code-tools/jmh/).
You can run them with:

    mvn clean install
    java -jar target/benchmarks.jar <BENCHMARK_CLASS_NAME>

Benchmarks:

* [removing from `ArrayList`](#arraylistremoveat)


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
