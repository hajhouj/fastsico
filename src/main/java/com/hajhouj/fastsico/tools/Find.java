package com.hajhouj.fastsico.tools;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.hajhouj.fastsico.IConstants;
import com.hajhouj.fastsico.Result;
import com.hajhouj.fastsico.StringSimilarity;

/**
 * The Find class is a program to find and display the top N most
 * similar strings to a given query string, from a list of target strings read
 * from a data file. The similarity is calculated using the edit distance
 * algorithm from the Fastsico library.
 *
 * @author Mohammed Hajhouj
 * @version 1.0.1
 * @since 2023-05-16
 */
public class Find {

	/**
	 * Main method to run the program.
	 *
	 * @param args Command-line arguments: the first argument is the path to the
	 *             data file, the second argument is the query string, the third argument
     *             is the number of top results to return, and the fourth argument is
     *             the output format.
	 * @throws Exception If there is an error reading the data file.
	 */
	public static void main(String[] args) throws Exception {
		// Read the data file path from the command-line arguments
		String dataFile = args[0];

		// Read the query string from the command-line arguments
		String query = args[1];

		// Read the number of top results to return from the command-line arguments
		int topN = Integer.parseInt(args[2]);

		// Read the output format from the command-line arguments
		String outputFormat = args[3];

		// Create an instance of the StringSimilarity class
		StringSimilarity ss = new StringSimilarity();

		// Calculate the similarity scores of the target strings to the query string
		List<Result> results = ss.calculateSimilarity(query, dataFile, IConstants.EDIT_DISTANCE);

		// Read the target strings from the data file
		String[] targets = Files.readAllLines(Paths.get(dataFile)).toArray(new String[0]);

		// Print the top N similarity scores in the specified format
		printResults(results.subList(0, topN), targets, outputFormat);
	}

	/**
	 * Prints the results in the specified format.
	 *
	 * @param results The list of results.
	 * @param targets The array of target strings.
	 * @param format The output format.
	 */
	public static void printResults(List<Result> results, String[] targets, String format) {
		switch (format.toLowerCase()) {
			case "json":
				String json = results.stream()
						.map(result -> String.format("{\"index\":%d,\"similarityScore\":%.2f,\"target\":\"%s\"}", result.getIndex(), result.getSimilarityScore(), targets[result.getIndex()]))
						.collect(Collectors.joining(",\n", "[\n", "\n]"));
				System.out.println(json);
				break;
			case "xml":
				String xml = results.stream()
						.map(result -> String.format("<result>\n<index>%d</index>\n<similarityScore>%.2f</similarityScore>\n<target>%s</target>\n</result>", result.getIndex(), result.getSimilarityScore(), targets[result.getIndex()]))
						.collect(Collectors.joining("\n", "<results>\n", "\n</results>"));
				System.out.println(xml);
				break;
			case "csv":
				String csv = "index,similarityScore,target\n";
				csv += results.stream()
						.map(result -> result.getIndex() + "," + result.getSimilarityScore() + "," + targets[result.getIndex()])
						.collect(Collectors.joining("\n"));
				System.out.println(csv);
				break;
			default:
				System.out.println("Invalid output format. Please choose json, xml, or csv.");
				break;
		}
	}
}

