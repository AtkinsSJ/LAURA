package uk.co.samatkins.laura;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class Laura {
	
	File homeDir;
	
	ArrayList<Module> modules;

	public Laura() {
		modules = new ArrayList<Module>();
	}
	
	public void start() {
		System.out.println("Waking LAURA...");
		createUserDir();
		loadPlugins();
		System.out.println("LAURA is awake!");
	}
	
	/**
	 * Ensure the ~/.laura directory exists
	 */
	private void createUserDir() {
		homeDir = new File(System.getProperty("user.home") + File.separator + ".laura");
		if (!homeDir.isDirectory()) {
			if (homeDir.mkdir()) {
				System.out.println("Created .laura directory");
			} else {
				System.out.println("COULD NOT CREATE .laura DIRECTORY!");
			}
		}
	}
	
	private void loadPlugins() {
		File pluginsDir = new File(homeDir, "plugins");
		// Create plugins folder if it doesn't exist
		if (!pluginsDir.isDirectory()) {
			pluginsDir.mkdir();
		}
		
		// Scan for plugins
		File[] pluginFiles = pluginsDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".jar");
			}
		});
		
		
	}

}
