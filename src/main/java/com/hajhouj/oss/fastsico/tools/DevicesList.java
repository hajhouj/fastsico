package com.hajhouj.oss.fastsico.tools;

import com.aparapi.device.OpenCLDevice;

import java.util.List;

import com.aparapi.device.Device.TYPE;

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
		// Array of device types to be queried
		TYPE[] devicesTypes = {TYPE.GPU, TYPE.CPU};
		
		// Header for the output table
		System.out.println("DEVICE QUERY | DEVICE NAME");
		System.out.println("-------------+-------------");
		
		// Loop through each device type in the array
		for (TYPE t : devicesTypes) {
			
			// Get a list of devices of the current type
			List<OpenCLDevice> devices = OpenCLDevice.listDevices(t);
			for (int i=0 ; i< devices.size(); i++) {
				
				// Print the device query and name for each device
				System.out.println(t.name() + "." +  i + "        | " + devices.get(i).getName());
			}
		}
		
	}

}
