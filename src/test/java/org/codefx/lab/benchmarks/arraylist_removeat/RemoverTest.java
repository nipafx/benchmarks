package org.codefx.lab.benchmarks.arraylist_removeat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class RemoverTest {

	abstract <T> List<T> createEmptyList();

	abstract <T> List<T> remove(List<T> list, int... removeAts);

	protected final List<String> list = createEmptyList();

	// inheriting nested classes does not seem to work (junit-team/junit5#717),
	// so they are not nested here and implementers have to extend them

	class _EmptyList {

		@Test
		void removeNone_emptyList() throws Exception {
			List<String> removed = remove(list);
			assertThat(removed).isEmpty();
		}

		@Test
		void removeSome_exception() throws Exception {
			assertThrows(
					RuntimeException.class,
					() -> remove(list, 0));
		}

	}

	class _SingleElementList {

		@BeforeEach
		void fillList() {
			list.add("A");
		}

		@Test
		void removeNone_listHasSameElement() throws Exception {
			List<String> removed = remove(list);
			assertThat(removed).containsExactly("A");
		}

		@Test
		void removeOnlyElement_emptyList() throws Exception {
			List<String> removed = remove(list, 0);
			assertThat(removed).isEmpty();
		}

		@Test
		void removeInvalidIndex_exception() throws Exception {
			assertThrows(
					RuntimeException.class,
					() -> remove(list, 1));
		}

	}

	class _ThreeElementList {

		@BeforeEach
		void fillList() {
			list.addAll(asList("A", "B", "C"));
		}

		@Test
		void removeNone_listHasSameElements() throws Exception {
			List<String> removed = remove(list);
			assertThat(removed).containsExactly("A", "B", "C");
		}

		@Test
		void removeFirstElement_firstElementGone() throws Exception {
			List<String> removed = remove(list, 0);
			assertThat(removed).containsExactly("B", "C");
		}

		@Test
		void removeMiddleElement_middleElementGone() throws Exception {
			List<String> removed = remove(list, 1);
			assertThat(removed).containsExactly("A", "C");
		}

		@Test
		void removeLastElement_lastElementGone() throws Exception {
			List<String> removed = remove(list, 2);
			assertThat(removed).containsExactly("A", "B");
		}

		@Test
		void removeFirstTwoElements_firstTwoElementsGone() throws Exception {
			List<String> removed = remove(list, 0, 1);
			assertThat(removed).containsExactly("C");
		}

		@Test
		void removeLastTwoElements_lastTwoElementsGone() throws Exception {
			List<String> removed = remove(list, 1, 2);
			assertThat(removed).containsExactly("A");
		}

	}

	class _FiveElementList {

		@BeforeEach
		void fillList() {
			list.addAll(asList("A", "B", "C", "D", "E"));
		}

		@Test
		void removeTwoConsecutiveInnerElements_elementGone() throws Exception {
			List<String> removed = remove(list, 1, 2);
			assertThat(removed).containsExactly("A", "D", "E");
		}

		@Test
		void removeThreeConsecutiveInnerElements_elementGone() throws Exception {
			List<String> removed = remove(list, 1, 2, 3);
			assertThat(removed).containsExactly("A", "E");
		}

		@Test
		void removeThreeElementsIncludingFirst_elementGone() throws Exception {
			List<String> removed = remove(list, 0, 1, 3);
			assertThat(removed).containsExactly("C", "E");
		}

		@Test
		void removeThreeElementsIncludingLast_elementGone() throws Exception {
			List<String> removed = remove(list, 1, 3, 4);
			assertThat(removed).containsExactly("A", "C");
		}

	}

}
