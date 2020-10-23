package dev.nipafx.lab.benchmarks.arraylist_removeat;

import java.util.Arrays;
import java.util.List;

public class SwapRemover {

	// intended for ArrayList

	public static <T> List<T> remove(List<T> list, int[] removeAts) {
		Arrays.sort(removeAts);
		int copyFrom = list.size();
		for (int i = removeAts.length - 1; i >= 0; i--) {
			int removeAt = removeAts[i];
			copyFrom--;
			if (removeAt == copyFrom) {
				continue;
			}
			list.set(removeAt, list.get(copyFrom));
		}
		list.subList(copyFrom, list.size())
				.clear();
		return list;
	}

}
