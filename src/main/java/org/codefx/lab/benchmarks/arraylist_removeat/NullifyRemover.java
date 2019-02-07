package org.codefx.lab.benchmarks.arraylist_removeat;

import java.util.List;
import java.util.Objects;

public class NullifyRemover {

	public static <T> List<T> remove(List<T> list, int[] removeAts) {
		for (int removeAt : removeAts) {
			list.set(removeAt, null);
		}
		list.removeIf(Objects::isNull);
		return list;
	}

}
