package com.hajhouj.oss.fastsico.tools;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.hajhouj.oss.fastsico.IConstants;
import com.hajhouj.oss.fastsico.Result;
import com.hajhouj.oss.fastsico.StringSimilarity;

public class StringFinder {

	public static void main(String[] args) throws Exception {
		String dataFile= args[0];
		String query = args[1];
		
		String[] targets = Files.readAllLines(Paths.get(dataFile)).toArray(new String[0]);
		
		StringSimilarity ss = new StringSimilarity();
		List<Result> results = ss.calculateSimilarity(query, targets, IConstants.EDIT_DISTANCE);
		
		for (int i=0; i<10; i++) System.out.println(results.get(i).getSimilarityScore());
	}

}
