package com.hajhouj.fastsico.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A helper class to sort an array of integers while retaining their indexes.
 * 
 * @author Mohammed Hajhouj
 * 
 * @version 1.0
 * 
 * @since 2023-02-03
 */
public class SortArrayWithIndexes {
	/**
	 * Sorts the array of doubles in ascending order and returns an array of their
	 * sorted indexes.
	 * 
	 * @param numbers the array of doubles to be sorted
	 * @return the array of sorted indexes
	 */
	public static int[] sortWithIndexes(int[] numbers) {
		List<IndexValue> indexValueList = new ArrayList<>();
		for (int i = 0; i < numbers.length; i++) {
			indexValueList.add(new IndexValue(i, numbers[i]));
		}

		Collections.sort(indexValueList, (o1, o2) -> Double.compare(o1.value, o2.value));

		int[] sortedIndexes = new int[indexValueList.size()];
		for (int i = 0; i < indexValueList.size(); i++) {
			sortedIndexes[i] = indexValueList.get(i).index;
		}

		return sortedIndexes;
	}

	/**
	 * A data structure to store the index and value of an element in the array.
	 */
	static class IndexValue {
		/** The index of the element in the array. */
		int index;
		/** The value of the element in the array. */
		double value;

		/**
		 * Constructs a new IndexValue object.
		 * 
		 * @param index the index of the element in the array
		 * @param value the value of the element in the array
		 */
		public IndexValue(int index, double value) {
			this.index = index;
			this.value = value;
		}
	}
}
