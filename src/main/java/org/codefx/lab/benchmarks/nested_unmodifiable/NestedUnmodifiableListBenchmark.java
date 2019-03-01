package org.codefx.lab.benchmarks.nested_unmodifiable;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.unmodifiableList;

public class NestedUnmodifiableListBenchmark extends NestedUnmodifiableCollectionBenchmark<List<Integer>> {

	// SETUP

	@Param({ "ArrayList", "LinkedList" })
	private String type = "ArrayList";

	private int[] getAts;

	@Setup(Level.Trial)
	public void createRandomAccessIndices() {
		Random random = new Random();
		int size = size();
		getAts = random
				.ints(0, size)
				.limit(size)
				.toArray();
	}

	@Override
	protected List<Integer> createUnderlyingCollection() {
		switch (type) {
			case "ArrayList":
				return new ArrayList<>();
			case "LinkedList":
				return new LinkedList<>();
			default:
				throw new IllegalArgumentException("Unknown list type: " + type);
		}
	}

	@Override
	protected List<Integer> wrap(List<Integer> underlyingCollection, int wrappingDepth) {
		List<Integer> unmodifiableList = underlyingCollection;
		for (int i = 0; i < wrappingDepth; i++)
			unmodifiableList = unmodifiableList(unmodifiableList);
		return unmodifiableList;
	}

	// BENCHMARKS

	@Benchmark
	public void linearAccess(Blackhole hole) {
		List<Integer> list = unmodifiableCollection();
		for (int i = 0; i < list.size(); i++)
			hole.consume(list.get(i));
	}

	@Benchmark
	public void randomAccess(Blackhole hole) {
		List<Integer> list = unmodifiableCollection();
		for (int at : getAts)
			hole.consume(list.get(at));
	}

}
