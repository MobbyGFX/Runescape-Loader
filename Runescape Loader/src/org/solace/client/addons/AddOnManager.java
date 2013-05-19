package org.solace.client.addons;

import java.io.File;
import java.util.HashMap;

public class AddOnManager {

	public static HashMap<String, File> addOns = new HashMap<String, File>();
	
	public static void getAllAddOns() {
		File availablePlugins = new File(System.getProperty("user.home") + "//Solace Loader/plugins");
		for (File file : availablePlugins.listFiles()) {
			addOns.put(file.getName().replace(".replug", ""), file);
		}
		System.out.println("Fetched: "+addOns.size()+" Addons");
	}
		
}
