package dev.nipafx.lab.benchmarks.stream.map_multi;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toUnmodifiableList;

@Fork(3)
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Thread)
public class MapMulitBenchmark {

	@Param({ "10_000", "100_000", "1_000_000" })
	private String size = "10_000";

	@Param({ "0.01", "0.1", "0.5", "0.8" })
	private String shareOfZeroes = "0.01";

	private List<Optional<Integer>> numbers;

	@Setup(Level.Trial)
	public void createWrappedCollection() {
		numbers = createRandomIntList(size(), shareOfZeroes());
	}

	private int size() {
		return Integer.valueOf(size.replace("_", ""));
	}

	private double shareOfZeroes() {
		return Double.valueOf(shareOfZeroes);
	}

	private static List<Optional<Integer>> createRandomIntList(int numberOfElements, double shareOfZeroes) {
		return new Random()
				.ints(numberOfElements, 0, 100)
				.mapToObj(number -> number < shareOfZeroes * 100 ? Optional.<Integer>empty() : Optional.of(number))
				.collect(toUnmodifiableList());
	}

	@Benchmark
	public long flatMap_count() {
		return numbers.stream()
				.flatMap(Optional::stream)
				.count();
	}

	@Benchmark
	public long mapMulti_count() {
		return numbers.stream()
				.mapMulti(Optional::ifPresent)
				.count();
	}

	@Benchmark
	public int flatMap_sum() {
		return numbers.stream()
				.flatMap(Optional::stream)
				.mapToInt(i -> i)
				.sum();
	}

	@Benchmark
	public int mapMulti_sum() {
		return numbers.stream()
				.<Integer> mapMulti(Optional::ifPresent)
				.mapToInt(i -> i)
				.sum();
	}

}
