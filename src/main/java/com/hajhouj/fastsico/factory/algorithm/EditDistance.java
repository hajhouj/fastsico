package com.hajhouj.fastsico.factory.algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.Sizeof;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_mem;
import org.jocl.cl_program;

import com.hajhouj.fastsico.IConstants;
import com.hajhouj.fastsico.Result;
import com.hajhouj.fastsico.exception.OpenCLDeviceNotFoundException;
import com.hajhouj.fastsico.factory.StringSimilarityAlgorithm;
import com.hajhouj.fastsico.helper.OpenCLDeviceSelector;

/**
 * 
 * EditDistance class Implements the string similarity algorithm using the edit
 * distance algorithm.
 * 
 * @author Mohammed Hajhouj
 * @version 1.0.1
 * @since 2023-05-17
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

		// Initialize the 2D array to store the dynamic programming values
		int[][] dp = new int[m + 1][n + 1];

		// Fill in the dp array using the Edit Distance algorithm
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

		// Calculate the similarity score
		return dp[m][n];
	}

	/**
	 * Calculates the similarity score between the query string and each target
	 * string in the list of targets.
	 *
	 * @param queryInput	the query string
	 * @param dataset 		the data path
	 * @return a list of {@link Result} objects, each containing a target index and
	 *         its similarity score to the query string
	 * @throws IOException                   if an I/O error occurs while reading
	 *                                       the file ed.cl (OpenCL implementation
	 *                                       of Edit Distance Algorithm)
	 * @throws OpenCLDeviceNotFoundException if an OpenCL device is not found
	 */
	@Override
	public List<Result> calculateSimilarityInDataSet(String queryInput, String dataset)
			throws IOException, OpenCLDeviceNotFoundException {
		// Get the GPU device to use for OpenCL
		String gpuDeviceQuery = System.getProperty(IConstants.OPENCL_DEVICE);


		// Get Max Memory Allocation of the GPU device
		long[] maxMemAllocSizeInfo = new long[1];
        cl_device_id selectedDevice = OpenCLDeviceSelector.selectDevice(gpuDeviceQuery);
        
		CL.clGetDeviceInfo(selectedDevice, CL.CL_DEVICE_MAX_MEM_ALLOC_SIZE, Sizeof.cl_long,
				Pointer.to(maxMemAllocSizeInfo), null);
		long MMA = maxMemAllocSizeInfo[0];
		//System.out.println("Max memory alloc size: " + humanReadableByteCount(MMA));
		
		//Maximum Java heap size
		//Get the current memory usage of the heap
		long XMX = Runtime.getRuntime().maxMemory();
		//System.out.println("Maximum heap space: " + humanReadableByteCount(XMX));
		
		int INT_SIZE = 4; // 4 bytes
		//Maximum Integer Buffer Size
		long MB = (long)Integer.MAX_VALUE  * 4;
		
		final int ITEM_SIZE = Integer.parseInt(System.getProperty(IConstants.ED_ITEM_SIZE, "80"));
		//vector size of each item in the input data
		int VECTOR_SIZE = ITEM_SIZE + 1; //first item contains the length, the rest contains data
		
		//read count of items in the input 
		BufferedReader readerCount = new BufferedReader(new FileReader(dataset));
		final AtomicInteger countLines = new AtomicInteger();
		readerCount.lines().forEach(item -> countLines.incrementAndGet());
		
		//compute memory size that will take after converting to vectors
		long inputMemorySize= countLines.longValue() * VECTOR_SIZE * INT_SIZE;
		readerCount.close();
		
		//query
		IntBuffer query = IntBuffer.allocate(VECTOR_SIZE);
		query.put(convertToVector(queryInput, VECTOR_SIZE));
		
		List<Long> memoryLimits = Arrays.asList(XMX, MMA, MB);
		
		//result
		IntBuffer result = IntBuffer.allocate(countLines.intValue());
		
		BufferedReader reader = new BufferedReader(new FileReader(dataset));
		//if memory size of input don't excess MMA and XMX, run kernel by passing the available input 
		if (inputMemorySize <= Collections.min(memoryLimits)) {
			//read input, convert each a line to a vector of a length of VECTOR_SIZE
			int bufferCapacity = (int) (countLines.longValue() * VECTOR_SIZE);
			IntBuffer in = IntBuffer.allocate(bufferCapacity);
			
			//fill in buffer
			reader.lines().peek(line -> {
				in.put(convertToVector(line , VECTOR_SIZE));
			});
			
			//run the kernel
			result.put(runKernel(selectedDevice, in, query, countLines.longValue(), VECTOR_SIZE));
		} else {
			//if memory size of input excess MMA or XMX, split input data to chunks
			int BUFFER_MEMORY_SIZE = (int)Collections.min(memoryLimits).longValue();
			int BUFFER_CAPACITY = BUFFER_MEMORY_SIZE / INT_SIZE;
			IntBuffer chunk = IntBuffer.allocate(BUFFER_CAPACITY);
			AtomicInteger count = new AtomicInteger();
			
			reader.lines().forEach(line -> {
				if (chunk.position() + VECTOR_SIZE > BUFFER_CAPACITY) {
					try {
						result.put(runKernel(selectedDevice,chunk, query, count.intValue(), VECTOR_SIZE));
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					chunk.clear();
					chunk.rewind();
					count.set(0);
				}
				try {
					chunk.put(convertToVector(line, VECTOR_SIZE));
				} catch (Exception e) {
					e.printStackTrace();
				}
				count.incrementAndGet();
            });
			result.put(runKernel(selectedDevice, chunk, query, count.longValue(), VECTOR_SIZE));			
		}
		
		reader.close();
		
        Integer[] indices = new Integer[result.capacity()];
        for (int i = 0; i < result.capacity(); i++) {
            indices[i] = i;
        }
        
        Arrays.sort(indices, (a, b) -> Integer.compare(result.get(a), result.get(b)));
       
        List<Result> results = new ArrayList<>();
        for (int i = 0; i < indices.length; i++) {
            int index = indices[i];
            int distance = result.get(index);
            results.add(new Result(index, distance));
        }
		
        return results;
	}



	public static int[] convertToVector(String str, int size) {
		int[] result = new int[size];
		result[0] = (int) Math.min(str.length(), size - 1);
		for (int i = 1; i <= result[0]; i++) {
			result[i] = str.charAt(i - 1);
		}
		return result;
	}

	public static IntBuffer runKernel(cl_device_id selectedDevice, Buffer input, Buffer query, long n, int VECTOR_SIZE) throws OpenCLDeviceNotFoundException, IOException {
        // Initialize the OpenCL context and command queue
        CL.setExceptionsEnabled(true);

		cl_context context = CL.clCreateContext(null, 1, new cl_device_id[] {selectedDevice }, null, null, null);
        cl_command_queue queue = CL.clCreateCommandQueue(context, selectedDevice, 0, null);

        // Create the input and output buffers
        cl_mem inputBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY, n * VECTOR_SIZE * Sizeof.cl_int, null, null);
        cl_mem queryBuffer = CL.clCreateBuffer(context, CL.CL_MEM_READ_ONLY, VECTOR_SIZE * Sizeof.cl_int, null, null);
        cl_mem outputBuffer = CL.clCreateBuffer(context, CL.CL_MEM_WRITE_ONLY, n * Sizeof.cl_int, null, null);

        // Copy the input data to the input buffer
        CL.clEnqueueWriteBuffer(queue, inputBuffer, true, 0, n * VECTOR_SIZE * Sizeof.cl_int, Pointer.to(input), 0, null, null);
        // Copy the query data to the query buffer
        CL.clEnqueueWriteBuffer(queue, queryBuffer, true, 0, VECTOR_SIZE * Sizeof.cl_int, Pointer.to(query), 0, null, null);
        

		// Read the OpenCL code for Edit Distance Algorithm from ed.cl file
		InputStream inputStream = EditDistance.class.getClassLoader().getResourceAsStream("ed.cl");
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		String editDistanceCode = sb.toString();

		// Replace the value of ITEM_SIZE in the OpenCL code with the value from system
		// properties or 80 as default
		String kernelSource = editDistanceCode.replace("#define ITEM_SIZE 80", "#define ITEM_SIZE " + (VECTOR_SIZE - 1));

        // Compile the kernel source code
        cl_program program = CL.clCreateProgramWithSource(context, 1, new String[] { kernelSource }, null, null);
        CL.clBuildProgram(program, 0, null, null, null, null);

        // Create the kernel object
        cl_kernel kernel = CL.clCreateKernel(program, "compute", null);

        // Set the kernel arguments
        CL.clSetKernelArg(kernel, 0, Sizeof.cl_mem, Pointer.to(inputBuffer));
        CL.clSetKernelArg(kernel, 1, Sizeof.cl_mem, Pointer.to(queryBuffer));        
        CL.clSetKernelArg(kernel, 2, Sizeof.cl_mem, Pointer.to(outputBuffer));

        // Execute the kernel
        long globalWorkSize[] = new long[] { n };
        CL.clEnqueueNDRangeKernel(queue, kernel, 1, null, globalWorkSize, null, 0, null, null);

        // Read the output data from the output buffer
        IntBuffer output = IntBuffer.allocate((int)n);
        CL.clEnqueueReadBuffer(queue, outputBuffer, true, 0, n * Sizeof.cl_int, Pointer.to(output), 0, null, null);

        /*
        // Print the output
        for (int i = 0; i < n; i++) {
            System.out.printf("%d^2 = %.0f\n", i, output.get(i));
        }*/

        // Release the resources
        CL.clReleaseMemObject(inputBuffer);
        CL.clReleaseMemObject(outputBuffer);
        CL.clReleaseKernel(kernel);
        CL.clReleaseProgram(program);
        CL.clReleaseCommandQueue(queue);
        CL.clReleaseContext(context);
        
        return output;
    }
}