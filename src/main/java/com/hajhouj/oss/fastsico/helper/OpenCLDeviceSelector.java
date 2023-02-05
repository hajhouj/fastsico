package com.hajhouj.oss.fastsico.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aparapi.device.Device.TYPE;
import com.aparapi.device.OpenCLDevice;

public class OpenCLDeviceSelector {

	private static Pattern deviceQueryPattern = Pattern
			.compile("^([a-z]+)([<=>])([0-9]+)|^(vendor):([a-zA-Z]+)|^(type):(GPU|CPU)|^(index):([0-9]+)$");

	public static OpenCLDevice selectDevice(String deviceQuery) {

		// final OpenCLDevice device = OpenCLDevice.listDevices(TYPE.GPU).get(0);
		TYPE[] deviceTypes = { TYPE.CPU, TYPE.GPU };
		Map<Long, OpenCLDevice> existingDevices = new HashMap<Long, OpenCLDevice>();

		for (TYPE deviceType : deviceTypes) {
			List<OpenCLDevice> devices = OpenCLDevice.listDevices(deviceType);
			for (OpenCLDevice device : devices) {
				existingDevices.put(device.getDeviceId(), device);
			}
		}

		List<OpenCLDevice> matchedDevices = new ArrayList<>();
		List<OpenCLDevice> devices = (List<OpenCLDevice>) existingDevices.values();

		for (OpenCLDevice device : devices) {
			Matcher matcher = deviceQueryPattern.matcher(deviceQuery);
			if (matcher.matches()) {
				String devicePropertiesGroup = matcher.group(1);
				String deviceVendorGroup = matcher.group(4);
				String deviceTypeGroup = matcher.group(6);

				if (devicePropertiesGroup != null) {
					// Property query
					String property = matcher.group(1);
					String operator = matcher.group(2);
					int value = Integer.parseInt(matcher.group(3));
					switch (property) {
					case "computeUnits":
						if (checkMatch(device.getMaxComputeUnits(), operator, value)) {
							matchedDevices.add(device);
						}
						break;
					case "globalMemSize":
						if (checkMatch(device.getGlobalMemSize(), operator, value)) {
							matchedDevices.add(device);
						}
						break;
					case "localMemSize":
						if (checkMatch(device.getLocalMemSize(), operator, value)) {
							matchedDevices.add(device);
						}
						break;
					default:
						throw new IllegalArgumentException("Invalid device property: " + property);
					}
				} else if (deviceVendorGroup != null) {
					// Vendor query
					String vendor = matcher.group(5);
					if (device.getOpenCLPlatform().getVendor().equalsIgnoreCase(vendor)) {
						matchedDevices.add(device);
					}
				} else if (deviceTypeGroup != null) {
					// Type query
					String type = matcher.group(7);
					if (device.getType().toString().equalsIgnoreCase(type)) {
						matchedDevices.add(device);
					}
				}
			} else {
				throw new IllegalArgumentException("Invalid device query: " + deviceQuery);
			}
		}
		Matcher matcher = deviceQueryPattern.matcher(deviceQuery);
		String deviceIndexGroup = matcher.group(8);
		
		if (matchedDevices.size() == 1) {
			return matchedDevices.get(0);
		} else if (matchedDevices.size() > 1) {
			if (deviceIndexGroup != null) {
				// Index query
				int index = Integer.parseInt(matcher.group(9));
				if (index < matchedDevices.size()) {
					return devices.get(index);
				} else {
					throw new IllegalArgumentException("Invalid device index: " + index);
				}
			} else {
				long maxMemory = 0;
				OpenCLDevice deviceWithMaxMemory = null;
				
				for (OpenCLDevice device: matchedDevices) {
					if (device.getGlobalMemSize() > maxMemory) {
						deviceWithMaxMemory = device;
						maxMemory = device.getGlobalMemSize();
					}
				}
				
				return deviceWithMaxMemory;
			}
		} else {
			throw new IllegalArgumentException("No devices matched the query: " + deviceQuery);
		}
	}

	private static boolean checkMatch(int deviceValue, String operator, int value) {
		switch (operator) {
		case "<":
			return deviceValue < value;
		case "=":
			return deviceValue == value;
		case ">":
			return deviceValue > value;
		default:
			throw new IllegalArgumentException("Invalid operator: " + operator);
		}
	}

	private static boolean checkMatch(long deviceValue, String operator, int value) {
		switch (operator) {
		case "<":
			return deviceValue < value;
		case "=":
			return deviceValue == value;
		case ">":
			return deviceValue > value;
		default:
			throw new IllegalArgumentException("Invalid operator: " + operator);
		}
	}
}