package dev.nipafx.lab.benchmarks.arraylist_removeat;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class ListIteratorRemover {

	// intended for LinkedList

	public static <T> List<T> remove(List<T> list, int[] removeAts) {
		if (removeAts.length == 0) {
			return list;
		}
		Arrays.sort(removeAts);

		int indexInRemoveAt = 0;
		int indexToRemove = removeAts[indexInRemoveAt];
		int indexInList = -1;
		ListIterator<T> iterator = list.listIterator();
		while (iterator.hasNext()) {
			iterator.next();
			indexInList++;
			if (indexInList == indexToRemove) {
				iterator.remove();
				indexInRemoveAt++;
				if (indexInRemoveAt < removeAts.length) {
					indexToRemove = removeAts[indexInRemoveAt];
				} else {
					break;
				}
			}
		}

		if (indexInRemoveAt < removeAts.length) {
			throw new IllegalArgumentException();
		}

		return list;
	}

}
