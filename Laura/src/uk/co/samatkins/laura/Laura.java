package uk.co.samatkins.laura;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Laura {
	
	private BufferedReader lineInput;
	
	private File homeDir;
	private File preferencesFile;
	
	private Properties preferences;
	
	private ArrayList<Module> modules;

	public Laura() {
		modules = new ArrayList<Module>();
		lineInput = new BufferedReader( new InputStreamReader(System.in) );
	}
	
	public void start() {
		print("Waking LAURA...");
		createUserDir();
		loadPreferences();
		loadPlugins();
		print("LAURA is awake!");
		
		mainLoop();
	}
	
	public void mainLoop() {
		String input;
		String[] inputParts;
		while (true) {
			input = getInput();
			inputParts = input.split(" ");
			
			// Try each module in turn
			for (Module module: modules) {
				if (module.matches(inputParts)) {
					module.execute(inputParts);
					break;
				}
			}
		}
	}
	
	public void print(String output) {
		System.out.println(output);
	}
	
	public String getInput() {
		try {
			return lineInput.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Ensure the ~/.laura directory exists
	 */
	private void createUserDir() {
		homeDir = new File(System.getProperty("user.home") + File.separator + ".laura");
		if (!homeDir.isDirectory()) {
			if (homeDir.mkdir()) {
				print("Created .laura directory");
			} else {
				print("COULD NOT CREATE .laura DIRECTORY!");
			}
		}
	}
	
	private void loadPreferences() {
		print("Loading user preferences");
		preferences = new Properties();
		
		// Try and load from a file
		preferencesFile = new File(homeDir, "preferences.xml");
		if (preferencesFile.isFile()) {
			try {
				preferences.loadFromXML(new FileInputStream(preferencesFile));
			} catch (InvalidPropertiesFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void savePreferences() {
		try {
			preferencesFile.createNewFile();
			preferences.storeToXML(new FileOutputStream(preferencesFile), null);
			print("Preferences saved successfully.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			print("ERROR: Failed to save preferences.");
		}
	}
	
	public Properties getPreferences() {
		return this.preferences;
	}
	
	private void loadPlugins()  {
		print("Loading modules");
		
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
		
		if (pluginFiles.length == 0) {
			print("ERROR: No modules were loaded! LAURA will have no functionality!");
			exit();
		}
		
		// Iterate through .jar files
		JarFileLoader classLoader;
		JarFile jarFile;
		for (File plugin: pluginFiles) {
			try {
				classLoader = new JarFileLoader(new URL[]{plugin.toURI().toURL()});
				jarFile = new JarFile(plugin);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
			// Search through the JAR for class files
			Enumeration<JarEntry> entries = jarFile.entries();
			while (entries.hasMoreElements()) {
				JarEntry element = entries.nextElement();
				if (element.getName().endsWith(".class")) {
					try {
						// Instantiate a module and add it to Laura.modules
						Class<?> c = Class.forName(
							element.getName().replaceAll(".class", "").replaceAll("/", "."),
							true, classLoader
						);
						if (c.isAssignableFrom(Module.class)) {
							Module m = (Module)c.newInstance();
							m.init(this);
							modules.add(m);
						}
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassCastException e) {
						// Class isn't a module. No need to panic.
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		print("Successfully loaded " + modules.size() + " modules");
		
		// Add the DefaultModule last - it simply says the command was not understood.
		Module defaultModule = new DefaultModule();
		defaultModule.init(this);
		modules.add(defaultModule);
	}

	public void exit() {
		print("Goodbye!");
		savePreferences();
		System.exit(0);
	}

}
