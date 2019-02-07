package org.codefx.lab.benchmarks.nested_unmodifiable;

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
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

@Fork(value = 2)
@Warmup(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 3, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class NestedUnmodifiableBenchmark {

	// SETUP

	private int size = 1_000_000;

	@Param({ "0", "1", "10", "100", "1_000", "10_000" })
	private String wrappingDepth = "0";

	private List<Integer> unmodifiableWrapper;
	private int[] getAts;

	@Setup(Level.Trial)
	public void createCollections() {
		Random random = new Random();

		List<Integer> underlyingCollection = random
				.ints(size).mapToObj(i -> i)
				.collect(Collectors.toList());

		unmodifiableWrapper = underlyingCollection;
		int depth = Integer.valueOf(wrappingDepth.replace("_", ""));
		for (int i = 0; i < depth; i++)
			unmodifiableWrapper = unmodifiableList(unmodifiableWrapper);

		getAts = random
				.ints(0, size)
				.limit(size)
				.toArray();
	}

	// BENCHMARKS

	@Benchmark
	public void forEach(Blackhole hole) {
		unmodifiableWrapper.forEach(hole::consume);
	}

	@Benchmark
	public void iterator(Blackhole hole) {
		for (int i : unmodifiableWrapper)
			hole.consume(i);
	}

	@Benchmark
	public void linearAccess(Blackhole hole) {
		for (int i = 0; i < unmodifiableWrapper.size(); i++)
			hole.consume(unmodifiableWrapper.get(i));
	}

	@Benchmark
	public void randomAccess(Blackhole hole) {
		for (int at : getAts)
			hole.consume(unmodifiableWrapper.get(at));
	}

}
