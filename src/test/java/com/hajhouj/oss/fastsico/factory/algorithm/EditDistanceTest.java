package com.hajhouj.oss.fastsico.factory.algorithm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link EditDistance} to validate its implementation of the {@link com.hajhouj.oss.fastsico.factory.StringSimilarityAlgorithm} interface.
 */
public class EditDistanceTest {
    private EditDistance editDistance = new EditDistance();

    /**
     * Test method to validate the {@link EditDistance#calculateSimilarity(String, String)} method.
     */
    @Test
    public void testCalculateSimilarity() {
        String query = "hello";
        String target = "hullo";
        double expectedSimilarityScore = 0.8;

        double similarityScore = editDistance.calculateSimilarity(query, target);

        assertEquals(expectedSimilarityScore, similarityScore, 0.001);
    }
}