package org.codefx.lab.benchmarks.arraylist_removeat;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SwapRemoverTest extends RemoverTest {

	@Override
	<T> List<T> createEmptyList() {
		return new ArrayList<>();
	}

	@Override
	<T> List<T> remove(List<T> list, int... removeAts) {
		return SwapRemover.remove(list, removeAts);
	}

	@Nested class EmptyList extends _EmptyList { }
	@Nested class SingleElementList extends _SingleElementList {}
	@Nested class ThreeElementList extends _ThreeElementList {

		// override tests that verify the list order was not changed

		@Test
		void removeFirstElement_firstElementGone() throws Exception {
			List<String> removed = remove(list, 0);
			assertThat(removed).contains("B", "C");
		}

	}

	@Nested class FiveElementList extends _FiveElementList {

		// override tests that verify the list order was not changed

		@Test
		void removeTwoConsecutiveInnerElements_elementGone() throws Exception {
			List<String> removed = remove(list, 1, 2);
			assertThat(removed).contains("A", "D", "E");
		}

		@Test
		void removeThreeConsecutiveInnerElements_elementGone() throws Exception {
			List<String> removed = remove(list, 1, 2, 3);
			assertThat(removed).contains("A", "E");
		}

		@Test
		void removeThreeElementsIncludingFirst_elementGone() throws Exception {
			List<String> removed = remove(list, 0, 1, 3);
			assertThat(removed).contains("C", "E");
		}

		@Test
		void removeThreeElementsIncludingLast_elementGone() throws Exception {
			List<String> removed = remove(list, 1, 3, 4);
			assertThat(removed).contains("A", "C");
		}

	}

}
