package com.hajhouj.fastsico.tools;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

/**
 * The DevicesList class is a program to query and display a list of available devices of 
 * two types: GPU and CPU. The devices are obtained using the Aparapi library.
 *
 * @author  Mohammed Hajhouj
 * @version 1.0
 * @since   2023-02-13
 */
public class DevicesList {

	public static void main(String[] args) {
		// Get the number of platforms
		int numPlatformsArray[] = new int[1];
		CL.clGetPlatformIDs(0, null, numPlatformsArray);
		int numPlatforms = numPlatformsArray[0];

		// Get the platforms
		cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
		CL.clGetPlatformIDs(numPlatforms, platforms, null);

		// Header for the output table
		System.out.println("DEVICE QUERY | DEVICE NAME");
		System.out.println("-------------+-------------");

		// Print the devices for each platform
		for (int i = 0; i < numPlatforms; i++) {
			int numDevicesArray[] = new int[1];
			CL.clGetDeviceIDs(platforms[i], CL.CL_DEVICE_TYPE_ALL, 0, null, numDevicesArray);
			int numDevices = numDevicesArray[0];
			cl_device_id devices[] = new cl_device_id[numDevices];
			CL.clGetDeviceIDs(platforms[i], CL.CL_DEVICE_TYPE_ALL, numDevices, devices, null);
			for (int j = 0; j < numDevices; j++) {
				String deviceName = getString(devices[j], CL.CL_DEVICE_NAME);

				// Print the device query and name for each device
				System.out.println(i + "." +  j + "        | " + deviceName);
			}
		}
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
