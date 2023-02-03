// StringSimilarityLibraryTest class
package com.hajhouj.oss.fastsico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.hajhouj.oss.fastsico.exception.OpenCLDeviceNotFoundException;

/**
 * Test class for {@link StringSimilarity} to validate its implementation of the string similarity calculation.
 */
public class StringSimilarityTest {
    private StringSimilarity stringSimilarity = new StringSimilarity();

    /**
     * Test method to validate the {@link StringSimilarity#calculateSimilarity(String, List, String)} method.
     */
    @Test
    public void testCalculateSimilarity() {
        String query = "hello";
        String[] targets = new String[3];
        targets[0] = "hey";
        targets[1] = "hullo";
        targets[2] = "world";

        List<Result> results;
		try {
			results = stringSimilarity.calculateSimilarity(query, targets, "EDIT_DISTANCE");
	        assertEquals(0.8, results.get(0).getSimilarityScore(), 0.001);
	        assertEquals(1, results.get(0).getIndex());
	        assertEquals(0.4, results.get(1).getSimilarityScore(), 0.001);
	        assertEquals(0, results.get(1).getIndex());
	        assertEquals(0, results.get(2).getSimilarityScore(), 0.001);
	        assertEquals(2, results.get(2).getIndex());
		} catch (OpenCLDeviceNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
    }
}