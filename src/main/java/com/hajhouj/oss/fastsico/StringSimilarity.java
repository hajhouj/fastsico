package com.hajhouj.oss.fastsico;

import java.io.IOException;
import java.util.List;

import com.hajhouj.oss.fastsico.exception.OpenCLDeviceNotFoundException;
import com.hajhouj.oss.fastsico.factory.AlgorithmFactory;
import com.hajhouj.oss.fastsico.factory.StringSimilarityAlgorithm;

/**
 * The StringSimilarity class provides a way to calculate the string similarity
 * between a query string and a list of target strings using a specified
 * algorithm.
 * 
 * @author Mohammed Hajhouj
 * @version 1.0
 * @since 2023-02-02
 */
public class StringSimilarity {
    /**
     * Calculates the similarity score between the query string and each target string in the list of targets using the specified algorithm.
     *
     * @param query the query string
     * @param targets the list of target strings
     * @param algorithm the name of the algorithm to use for string similarity calculation
     * @return a list of {@link Result} objects, each containing a target string and its similarity score to the query string
     * @throws IOException Thrown by {@link StringSimilarityAlgorithm}.calculateSimilarity() 
     * @throws OpenCLDeviceNotFoundException Thrown by {@link StringSimilarityAlgorithm}.calculateSimilarity() 
     */
    public List<Result> calculateSimilarity(String query, String[] targets, String algorithm) throws OpenCLDeviceNotFoundException, IOException {
        StringSimilarityAlgorithm stringSimilarityAlgorithm = AlgorithmFactory.getAlgorithm(algorithm);
        return stringSimilarityAlgorithm.calculateSimilarity(query, targets);
    }
}