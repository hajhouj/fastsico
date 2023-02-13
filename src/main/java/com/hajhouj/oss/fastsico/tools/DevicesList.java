package com.hajhouj.oss.fastsico.tools;

import com.aparapi.device.OpenCLDevice;

import java.util.List;

import com.aparapi.device.Device.TYPE;

public class DevicesList {

	public static void main(String[] args) {
		TYPE[] devicesTypes = {TYPE.GPU, TYPE.CPU};
		
		System.out.println("DEVICE QUERY | DEVICE NAME");
		System.out.println("-------------+-------------");
		for (TYPE t : devicesTypes) {
			List<OpenCLDevice> devices = OpenCLDevice.listDevices(t);
			for (int i=0 ; i< devices.size(); i++) {
				System.out.println(t.name() + "." +  i + "        | " + devices.get(i).getName());
			}
		}
		
	}

}
