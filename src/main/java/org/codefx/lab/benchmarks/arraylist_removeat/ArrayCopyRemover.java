package org.codefx.lab.benchmarks.arraylist_removeat;

import java.util.Arrays;
import java.util.List;

public class ArrayCopyRemover {

	// intended for ArrayList

	public static <T> List<T> remove(List<T> list, int[] removeAts) {
		if (removeAts.length == 0) {
			return list;
		}

		Arrays.sort(removeAts);
		Object[] source = list.toArray(new Object[0]);
		Object[] target = new Object[source.length - removeAts.length];
		for (int i = 0; i < removeAts.length; i++) {
			int srcBegin = i == 0 ? 0 : removeAts[i - 1] + 1;
			int srcEnd = removeAts[i];
			int tgtBegin = srcBegin - i;
			System.arraycopy(source, srcBegin, target, tgtBegin, srcEnd - srcBegin);
		}

		int lastSourceIndex = source.length - 1;
		int lastRemoveIndex = removeAts[removeAts.length - 1];
		if (lastRemoveIndex < lastSourceIndex) {
			System.arraycopy(
					source, lastRemoveIndex + 1,
					target, lastRemoveIndex - removeAts.length + 1,
					lastSourceIndex - lastRemoveIndex);
		}

		return (List<T>) Arrays.asList(target);
	}

}
