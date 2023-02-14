package com.hajhouj.oss.fastsico.helper;

import com.aparapi.device.Device.TYPE;
import com.aparapi.device.OpenCLDevice;
import com.hajhouj.oss.fastsico.exception.OpenCLDeviceNotFoundException;

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
	public static OpenCLDevice selectDevice(String deviceQuery) throws OpenCLDeviceNotFoundException {
		String query = deviceQuery.toUpperCase();

		// Check if the query starts with "GPU" or "CPU"
		if (query.startsWith("GPU") || query.startsWith("CPU")) {
			int index = 0;
			
			// Check if query contains the device index (e.g. "GPU.0" or "CPU.2")
			if (query.startsWith("GPU.") || query.startsWith("CPU.")) {
				try {
					// Extract the device index
					index = Integer.parseInt(query.replaceFirst("GPU\\.|CPU\\.", ""));
				} catch (NumberFormatException e) {
					// Throw an exception if the index is not numeric
					throw new OpenCLDeviceNotFoundException("Device query should contains numeric index.");
				}
			}
			
			// Return the GPU device if the query starts with "GPU"
			if (query.startsWith("GPU")) {
				return OpenCLDevice.listDevices(TYPE.GPU).get(index);
			} 
			// Return the CPU device if the query starts with "CPU"
			else {
				return OpenCLDevice.listDevices(TYPE.CPU).get(index);
			}
		} else {
			// Throw an exception if the query does not start with "GPU" or "CPU"
			throw new OpenCLDeviceNotFoundException("Device query should starts with either 'GPU' or 'CPU'.");
		}
	}
}