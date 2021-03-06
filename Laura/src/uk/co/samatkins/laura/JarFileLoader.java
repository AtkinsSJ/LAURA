package uk.co.samatkins.laura;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarFileLoader extends URLClassLoader {

	public JarFileLoader(URL[] urls) {
		super(urls);
	}
	
	public void addFile (String path) throws MalformedURLException {
		String urlPath = "jar:file://" + path + "!/";
		addURL( new URL(urlPath) );
	}
}
