package com.hajhouj.fastsico.helper;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

/**
 * Test class for the {@link SortArrayWithIndexes} class.
 */
public class SortArrayWithIndexesTest {
  /**
   * Tests the {@link SortArrayWithIndexes#sortWithIndexes(double[])} method.
   */
  @Test
  public void testSortWithIndexes() {
    int[] numbers = {3, 5, 1, 2, 4, 6};
    int[] expectedSortedIndexes = {2, 3, 0, 4, 1, 5};
    int[] sortedIndexes = SortArrayWithIndexes.sortWithIndexes(numbers);
    assertArrayEquals(expectedSortedIndexes, sortedIndexes);
  }
}