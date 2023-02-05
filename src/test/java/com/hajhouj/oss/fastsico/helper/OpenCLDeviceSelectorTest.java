package com.hajhouj.oss.fastsico.helper;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aparapi.device.Device.TYPE;
import com.aparapi.device.OpenCLDevice;

public class OpenCLDeviceSelectorTest {
	@Test
	public void testSelectDevice() throws Exception {
		List<OpenCLDevice> cpuDevices = OpenCLDevice.listDevices(TYPE.CPU);

		List<OpenCLDevice> gpuDevices = OpenCLDevice.listDevices(TYPE.GPU);

		TYPE[] deviceTypes = { TYPE.CPU, TYPE.GPU };
		Map<Long, OpenCLDevice> availableDevices = new HashMap<Long, OpenCLDevice>();

		for (TYPE deviceType : deviceTypes) {
			List<OpenCLDevice> devices = OpenCLDevice.listDevices(deviceType);
			for (OpenCLDevice device : devices) {
				availableDevices.put(device.getDeviceId(), device);
			}
		}

		if (cpuDevices.size() > 0) {
			OpenCLDevice cpuDevice1 = OpenCLDeviceSelector.selectDevice("type:CPU");
			assertEquals(cpuDevice1.getType(), TYPE.CPU);
			for (int i = 0; i < cpuDevices.size(); i++) {
				OpenCLDevice cpuDevice2 = OpenCLDeviceSelector.selectDevice("type:CPU,index:" + i);
				assertEquals(cpuDevice2.getType(), TYPE.CPU);
				assertEquals(cpuDevice2.getDeviceId(), cpuDevices.get(i).getDeviceId());
			}
		}

		if (gpuDevices.size() > 0) {
			OpenCLDevice gpuDevice1 = OpenCLDeviceSelector.selectDevice("type:GPU");
			assertEquals(gpuDevice1.getType(), TYPE.GPU);
			for (int i = 0; i < gpuDevices.size(); i++) {
				OpenCLDevice gpuDevice2 = OpenCLDeviceSelector.selectDevice("type:GPU,index:" + i);
				assertEquals(gpuDevice2.getType(), TYPE.GPU);
				assertEquals(gpuDevice2.getDeviceId(), gpuDevices.get(i).getDeviceId());
			}
		}
	}
}
