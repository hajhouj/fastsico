package com.hajhouj.oss.fastsico.factory;

import com.hajhouj.oss.fastsico.factory.algorithm.EditDistance;

/**
 * 
 * AlgorithmFactory class Returns an instance of a string similarity algorithm
 * based on the given algorithm type.
 * 
 * @author Mohammed Hajhouj
 * @version 1.0
 * @since 2023-02-02
 */
public class AlgorithmFactory {
	/*
	 * Returns an instance of a string similarity algorithm based on the given
	 * algorithm type.
	 * 
	 * @param algorithmType the type of the string similarity algorithm
	 * 
	 * @return an instance of the string similarity algorithm
	 */
	public static StringSimilarityAlgorithm getAlgorithm(String algorithmType) {
		if (algorithmType.equals("EDIT_DISTANCE")) {
			return new EditDistance();
		}
		// add more algorithm types here
		return null;
	}
}