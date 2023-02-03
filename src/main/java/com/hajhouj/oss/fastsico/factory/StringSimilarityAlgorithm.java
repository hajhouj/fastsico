package com.hajhouj.oss.fastsico.factory;

import java.io.IOException;
import java.util.List;

import com.hajhouj.oss.fastsico.Result;
import com.hajhouj.oss.fastsico.exception.OpenCLDeviceNotFoundException;

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
     * @param targets the list of target strings
     * @return a list of {@link Result} objects, each containing a target string and its similarity score to the query string
     * @throws OpenCLDeviceNotFoundException 
     * @throws IOException 
     */
	public List<Result> calculateSimilarity(String query, String[] targets) throws OpenCLDeviceNotFoundException, IOException;
	
	/*
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