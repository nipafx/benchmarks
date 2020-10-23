package dev.nipafx.lab.benchmarks.nested_unmodifiable;

import org.openjdk.jmh.annotations.Param;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;

public class NestedUnmodifiableSetBenchmark extends NestedUnmodifiableCollectionBenchmark<Set<Integer>> {

	// SETUP

	@Param({ "HashSet", "LinkedHashSet" })
	private String type;

	@Override
	protected Set<Integer> createUnderlyingCollection() {
		switch (type) {
			case "HashSet":
				return new HashSet<>();
			case "LinkedHashSet":
				return new LinkedHashSet<>();
			default:
				throw new IllegalArgumentException("Unknown set type: " + type);
		}
	}

	@Override
	protected Set<Integer> wrap(Set<Integer> underlyingCollection, int wrappingDepth) {
		Set<Integer> unmodifiableSet = underlyingCollection;
		for (int i = 0; i < wrappingDepth; i++)
			unmodifiableSet = unmodifiableSet(unmodifiableSet);
		return unmodifiableSet;
	}

}
