package com.hajhouj.fastsico.factory;

import java.io.IOException;
import java.util.List;

import com.hajhouj.fastsico.Result;
import com.hajhouj.fastsico.exception.OpenCLDeviceNotFoundException;

/**
 * 
 * StringSimilarityAlgorithm interface Defines the contract for string
 * similarity algorithms. Algorithm implementations must implement the
 * calculateSimilarity method.
 * 
 * @author Mohammed Hajhouj
 * @version 1.0
 * @since 2023-02-02
 */
public interface StringSimilarityAlgorithm {
    /**
     * Calculates the similarity score between the query string and each target string in the list of targets.
     *
     * @param query the query string
     * @param dataset the dataset path
     * @return a list of {@link Result} objects, each containing a target string and its similarity score to the query string
	 * @throws IOException                   if an I/O error occurs while reading
	 *                                       the dataset file
	 * @throws OpenCLDeviceNotFoundException if an OpenCL device is not found
     */
	public List<Result> calculateSimilarityInDataSet(String query, String dataset) throws OpenCLDeviceNotFoundException, IOException;
	
	/**
	 * Calculates the similarity score between a query string and a target string.
	 * 
	 * @param query The query string.
	 * 
	 * @param target The target string.
	 * 
	 * @return The similarity score between the query and target strings.
	 */
	public double calculateSimilarity(String query, String target);
}