package dev.nipafx.lab.benchmarks.nested_unmodifiable;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Collection;
import java.util.Random;

import static java.util.stream.Collectors.toCollection;

public abstract class NestedUnmodifiableCollectionBenchmark<T extends Collection<Integer>> extends NestedBenchmark {

	// SETUP

	@Param({ "10_000", "100_000", "1_000_000" })
	private String size = "10_000";

	@Param({ "0", "1", "10", "100", "1_000", "10_000" })
	private String depth = "0";

	private T unmodifiableCollection;

	@Setup(Level.Trial)
	public void createWrappedCollection() {
		T underlyingCollection = new Random()
				.ints(size()).mapToObj(i -> i)
				.collect(toCollection(this::createUnderlyingCollection));
		unmodifiableCollection = wrap(underlyingCollection, depth());
	}

	protected abstract T createUnderlyingCollection();

	protected abstract T wrap(T underlyingCollection, int wrappingDepth);

	// ACCESSORS

	protected Integer size() {
		return Integer.valueOf(size.replace("_", ""));
	}

	protected Integer depth() {
		return Integer.valueOf(depth.replace("_", ""));
	}

	protected T unmodifiableCollection() {
		return unmodifiableCollection;
	}

	// BENCHMARKS

	@Benchmark
	public boolean isEmpty() {
		return unmodifiableCollection.isEmpty();
	}

	@Benchmark
	public void forEach(Blackhole hole) {
		unmodifiableCollection.forEach(hole::consume);
	}

	@Benchmark
	public void iterator(Blackhole hole) {
		for (int i : unmodifiableCollection)
			hole.consume(i);
	}

}
