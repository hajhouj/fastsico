package com.hajhouj.fastsico.tools;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.hajhouj.fastsico.IConstants;
import com.hajhouj.fastsico.Result;
import com.hajhouj.fastsico.StringSimilarity;

/**
 * The Find class is a program to find and display the top 10 most
 * similar strings to a given query string, from a list of target strings read
 * from a data file. The similarity is calculated using the edit distance
 * algorithm from the Fastsico library.
 *
 * @author Mohammed Hajhouj
 * @version 1.0
 * @since 2023-02-13
 */
public class Find {

	/**
	 * Main method to run the program.
	 *
	 * @param args Command-line arguments: the first argument is the path to the
	 *             data file, and the second argument is the query string.
	 * @throws Exception If there is an error reading the data file.
	 */
	public static void main(String[] args) throws Exception {
		// Read the data file path from the command-line arguments
		String dataFile = args[0];

		// Read the query string from the command-line arguments
		String query = args[1];

		// Create an instance of the StringSimilarity class
		StringSimilarity ss = new StringSimilarity();

		// Calculate the similarity scores of the target strings to the query string
		List<Result> results = ss.calculateSimilarity(query, dataFile, IConstants.EDIT_DISTANCE);

		// Read the target strings from the data file
		String[] targets = Files.readAllLines(Paths.get(dataFile)).toArray(new String[0]);

		// Print the top 10 similarity scores
		for (int i = 0; i < 10; i++)
			System.out.println(results.get(i).getSimilarityScore() + " : " + targets[results.get(i).getIndex()]);
	}

}
