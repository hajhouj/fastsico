package com.hajhouj.oss.fastsico.factory.algorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.aparapi.Range;
import com.aparapi.device.Device;
import com.aparapi.device.Device.TYPE;
import com.aparapi.device.OpenCLDevice;
import com.aparapi.opencl.OpenCL;
import com.hajhouj.oss.fastsico.IConstants;
import com.hajhouj.oss.fastsico.Result;
import com.hajhouj.oss.fastsico.exception.OpenCLDeviceNotFoundException;
import com.hajhouj.oss.fastsico.factory.StringSimilarityAlgorithm;
import com.hajhouj.oss.fastsico.helper.OpenCLDeviceSelector;
import com.hajhouj.oss.fastsico.helper.SortArrayWithIndexes;

/**
 * 
 * EditDistance class Implements the string similarity algorithm using the edit
 * distance algorithm.
 * 
 * @author Mohammed Hajhouj
 * @version 1.0
 * @since 2023-02-02
 */
public class EditDistance implements StringSimilarityAlgorithm {
	/*
	 * Calculates the similarity score between a query string and a target string
	 * using the Edit Distance algorithm.
	 * 
	 * @param query The query string.
	 * 
	 * @param target The target string.
	 * 
	 * @return The similarity score between the query and target strings.
	 */
	public double calculateSimilarity(String query, String target) {
		int m = query.length();
		int n = target.length();

		int[][] dp = new int[m + 1][n + 1];

		for (int i = 0; i <= m; i++) {
			for (int j = 0; j <= n; j++) {
				if (i == 0) {
					dp[i][j] = j;
				} else if (j == 0) {
					dp[i][j] = i;
				} else if (query.charAt(i - 1) == target.charAt(j - 1)) {
					dp[i][j] = dp[i - 1][j - 1];
				} else {
					dp[i][j] = 1 + Math.min(dp[i][j - 1], Math.min(dp[i - 1][j], dp[i - 1][j - 1]));
				}
			}
		}

		double s = (double) dp[m][n] / Math.max(m, n);
		return 1.0 - s;
	}
	
	/**
	 * Calculates the similarity score between the query string and each target
	 * string in the list of targets.
	 *
	 * @param queryInput   the query string
	 * @param targetsInput the list of target strings
	 * @return a list of {@link Result} objects, each containing a target index and
	 *         its similarity score to the query string
	 * @throws IOException if an I/O error occurs while reading the file ed.cl (OpenCL implementation of Edit Distance Algorithm)
	 * @throws OpenCLDeviceNotFoundException if an OpenCL device is not found
	 */
	@Override
	public List<Result> calculateSimilarity(String queryInput, String[] targetsInput) throws IOException, OpenCLDeviceNotFoundException {
		final int ITEM_SIZE = Integer.parseInt(System.getProperty(IConstants.ED_ITEM_SIZE, "80"));

		InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ed.cl");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
		  sb.append(line).append("\n");
		}
		String editDistanceCode = sb.toString();
		editDistanceCode = editDistanceCode.replace("#define ITEM_SIZE 80", "#define ITEM_SIZE " + ITEM_SIZE);
		
		final int[] targets = convertStringArrayToVector(targetsInput, ITEM_SIZE);
		final int[] query = convertStringToVector(queryInput, ITEM_SIZE);

		final Range range = Range.create(targetsInput.length);

		final int[] results = new int[targetsInput.length];

		String gpuQuery = System.getProperty(IConstants.OPENCL_DEVICE);
		
		Device device = null;
		if (gpuQuery == null) {
			device = OpenCLDevice.bestGPU();
		} else {
			device = OpenCLDeviceSelector.selectDevice(gpuQuery);
		}
		System.err.println("Using OpenCL Device : " + device.toString() + " " + device.getShortDescription());

		if (device instanceof OpenCLDevice) {
			final OpenCLDevice openclDevice = (OpenCLDevice) device;

			final EditDistanceCL editDist = openclDevice.bind(EditDistanceCL.class, editDistanceCode);
			editDist.compute(range, targets, query, results);

			int[] indexes = SortArrayWithIndexes.sortWithIndexes(results);
			
			List<Result> output = new LinkedList<Result>();
			
			for (int i = 0; i < indexes.length; i++) {
				double r = (double) results[indexes[i]];
				int l = Math.min(Math.max(queryInput.length(), targetsInput[indexes[i]].length()), ITEM_SIZE);
				double score = 1.0 -  r / l;
				output.add(new Result(indexes[i], score));				
			}

			return output;
		} else {
			throw new OpenCLDeviceNotFoundException("No OpenCL device was found on your system. Please make sure that you have the necessary hardware and software components installed and try again.");
		}
	}

	interface EditDistanceCL extends OpenCL<EditDistanceCL> {
        public EditDistanceCL compute(
                              Range _range,
                              @GlobalReadWrite("targets") int[] targets,//
                              @GlobalReadWrite("query") int[] query,
                              @GlobalReadWrite("results") int[] results);
    }

    public static int[] convertStringArrayToVector(String[] arr, int size) {
        int[] result = new int[arr.length * (size + 1)];
        for (int i = 0; i < arr.length; i++) {
          int[] res = convertStringToVector(arr[i], size);
          for (int j = 0; j < res.length; j++) {
            result[i * (size + 1) + j] = res[j];
          }
        }
        return result;
    }
    public static int[] convertStringToVector(String str, int size) {
        int[] result = new int[size + 1];
        result[0] = (int)Math.min(str.length(), size);
        for (int i=1; i <= result[0]; i++) {
            result[i] = str.charAt(i-1);
        }
        return result;
    }
}