package uk.co.samatkins.laura;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Laura {
	
	File homeDir;
	
	ArrayList<Module> modules;

	public Laura() {
		modules = new ArrayList<Module>();
	}
	
	public void print(String output) {
		System.out.println(output);
	}
	
	public void start() {
		print("Waking LAURA...");
		createUserDir();
		loadPlugins();
		print("LAURA is awake!");
		
		mainLoop();
	}
	
	public void mainLoop() {
		
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
			print("ERROR: No modules found!");
			return;
		}
		
		URLClassLoader classLoader;// = new URLClassLoader(new URL[]{pluginsDir.toURI().toURL()});

		// Iterate through .jar files
		JarFile jarFile;
		for (File plugin: pluginFiles) {
			try {
				classLoader = new URLClassLoader(new URL[]{plugin.toURI().toURL()});
				jarFile = new JarFile(plugin);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
						Module m = (Module)c.newInstance();
						m.init(this);
						modules.add(m);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassCastException e) {
						
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		print("Successfully loaded " + modules.size() + " modules");
	}

	public void exit() {
		print("Going to sleep.");
		System.exit(0);
	}

}
