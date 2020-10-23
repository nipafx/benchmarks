package dev.nipafx.lab.benchmarks.arraylist_removeat;

import org.junit.jupiter.api.Nested;

import java.util.LinkedList;
import java.util.List;

class ListIteratorRemoverTest extends RemoverTest {

	@Override
	<T> List<T> createEmptyList() {
		return new LinkedList<>();
	}

	@Override
	<T> List<T> remove(List<T> list, int[] removeAts) {
		return ListIteratorRemover.remove(list, removeAts);
	}

	@Nested class EmptyList extends _EmptyList { }
	@Nested class SingleElementList extends _SingleElementList {}
	@Nested class ThreeElementList extends _ThreeElementList {}
	@Nested class FiveElementList extends _FiveElementList {}

}
