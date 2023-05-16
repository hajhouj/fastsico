package com.hajhouj.fastsico.helper;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

import com.hajhouj.fastsico.exception.OpenCLDeviceNotFoundException;

/**
 * Class OpenCLDeviceSelector helps in selecting a device based on device query.
 * 
 * @author Mohammed Hajhouj
 * @version 1.0
 * @since 2023-02-13
 */
public class OpenCLDeviceSelector {

	/**
	 * Method selectDevice takes device query as input and returns the matching
	 * OpenCLDevice.
	 * 
	 * @param deviceQuery string to match the OpenCLDevice.
	 * @return OpenCLDevice matching the query.
	 * @throws OpenCLDeviceNotFoundException if no device matches the query or query
	 *                                       is in wrong format.
	 */
	public static cl_device_id selectDevice(String queryString) throws OpenCLDeviceNotFoundException {
		String[] queryParts = queryString.split("\\.");
		int platformId = Integer.parseInt(queryParts[0]);
		int deviceId = Integer.parseInt(queryParts[1]);

		// Get the number of platforms
		int numPlatformsArray[] = new int[1];
		CL.clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		// Get the platforms
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
		CL.clGetPlatformIDs(numPlatforms, platforms, null);

		// Print the devices for each platform
		for (int i = 0; i < numPlatforms; i++) {
			System.out.printf("Devices for platform %d:\n", i);
			int numDevicesArray[] = new int[1];
			CL.clGetDeviceIDs(platforms[i], CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
			int numDevices = numDevicesArray[0];
			cl_device_id devices[] = new cl_device_id[numDevices];
			CL.clGetDeviceIDs(platforms[i], CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);
			for (int j = 0; j < numDevices; j++) {
				String deviceName = getString(devices[j], CL.CL_DEVICE_NAME);

				if (i == platformId && j == deviceId) {
					System.out.println("Using " + deviceName);
					return devices[j];
				}
			}
		}

		throw new OpenCLDeviceNotFoundException("Device not found for query : " + queryString);
	}

	// Helper method to get a string property of a device
	private static String getString(cl_device_id device, int paramName) {
		long size[] = new long[1];
		CL.clGetDeviceInfo(device, paramName, 0, null, size);
		byte buffer[] = new byte[(int) size[0]];
		CL.clGetDeviceInfo(device, paramName, buffer.length, Pointer.to(buffer), null);
		return new String(buffer, 0, buffer.length - 1);
	}
}