package com.hajhouj.oss.fastsico.helper;

import com.aparapi.device.Device.TYPE;
import com.aparapi.device.OpenCLDevice;
import com.hajhouj.oss.fastsico.exception.OpenCLDeviceNotFoundException;

public class OpenCLDeviceSelector {

	public static OpenCLDevice selectDevice(String deviceQuery) throws OpenCLDeviceNotFoundException {
		String query = deviceQuery.toUpperCase();
		
		if (query.startsWith("GPU") || query.startsWith("CPU")) {
			int index = 0;
			if (query.startsWith("GPU.") || query.startsWith("CPU.")) {
				try {
					index = Integer.parseInt(query.replaceFirst("GPU\\.|CPU\\.", ""));
				} catch (NumberFormatException e) {
					throw new OpenCLDeviceNotFoundException("Device query should contains numeric index.");
				}
			}
			if (query.startsWith("GPU")) {
				return OpenCLDevice.listDevices(TYPE.GPU).get(index);
			} else {
				return OpenCLDevice.listDevices(TYPE.CPU).get(index);
			}
		} else  {
			throw new OpenCLDeviceNotFoundException("Device query should starts with either 'GPU' or 'CPU'.");
		}
	}
}