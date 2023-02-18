package com.hajhouj.oss.fastsico;

public interface IConstants {
	//Algortihms tags
	public final static String EDIT_DISTANCE= "EDIT_DISTANCE";
	
	//Internal system properties keys
	//Maximum item size for Edit Distance Algorithm
	public final static String ED_ITEM_SIZE = "fastsico.ed.item.size";
	//System property key that defines the device to use in computation
	public final static String OPENCL_DEVICE = "use-device";
}
