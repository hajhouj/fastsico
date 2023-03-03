// StringSimilarityLibraryTest class
package com.hajhouj.oss.fastsico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import com.hajhouj.oss.fastsico.exception.OpenCLDeviceNotFoundException;
import com.hajhouj.oss.fastsico.factory.algorithm.EditDistance;

/**
 * Test class for {@link StringSimilarity} to validate its implementation of the string similarity calculation.
 */
public class StringSimilarityTest {
    private StringSimilarity stringSimilarity = new StringSimilarity();

    /**
     * Test method to validate the {@link StringSimilarity#calculateSimilarity(String, List, String)} method.
     *
    @Test
    @Ignore
    public void testCalculateSimilarity() {
        String query = "hello";
        String[] targets = new String[3];
        targets[0] = "hey";
        targets[1] = "hullo";
        targets[2] = "world";
        
        System.setProperty(IConstants.ED_ITEM_SIZE, "10");

        EditDistance editDistance = new EditDistance();
        
        List<Result> results;
		try {
			results = stringSimilarity.calculateSimilarity(query, targets, "EDIT_DISTANCE");
	        assertEquals(1, results.get(0).getIndex());
			assertEquals(results.get(0).getSimilarityScore(), editDistance.calculateSimilarity(query, targets[results.get(0).getIndex()]), 0);
			assertEquals(0.8, results.get(0).getSimilarityScore(), 0.001);

	        assertEquals(0, results.get(1).getIndex());
			assertEquals(results.get(1).getSimilarityScore(), editDistance.calculateSimilarity(query, targets[results.get(1).getIndex()]), 0);
			assertEquals(0.4, results.get(1).getSimilarityScore(), 0.001);
	        
	        assertEquals(2, results.get(2).getIndex());
			assertEquals(results.get(2).getSimilarityScore(), editDistance.calculateSimilarity(query, targets[results.get(2).getIndex()]), 0);
		} catch (OpenCLDeviceNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
    }/
    
    /**
     * Test method to validate the {@link StringSimilarity#calculateSimilarity(String, List, String)} method with a large dataset.
     */
    @Test
    @Ignore
    public void testCalculateSimilarityLargeData()  {
		try {
	        String query = "TGATGTTCCAAGAACCAGGGAACTCTCCACCCTGTAGAATCTGGGAGTCT";
	        String dataset = "/Users/nano/git/ss-benchmark/data/pufferfish-dna/data.txt";
	        
	        System.setProperty("use-device", "0.2");
  
	        System.setProperty(IConstants.ED_ITEM_SIZE, "80");

	        EditDistance editDistance = new EditDistance();
	        
	        List<Result> results;
	        
			results = stringSimilarity.calculateSimilarity(query, dataset, "EDIT_DISTANCE");
			
	        
	        String[] targets = Files.readAllLines(Paths.get(dataset)).toArray(new String[0]);
	      
	        for (int i=0; i < 10; i++) {
	        	System.out.println(results.get(i).getSimilarityScore() + " " + results.get(i).getIndex() + " " + targets[results.get(i).getIndex()]);
	        }
	        
			assertTrue(results.size() > 0);


		} catch (OpenCLDeviceNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}
    }
}