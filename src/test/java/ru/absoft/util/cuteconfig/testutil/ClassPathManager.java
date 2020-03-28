package ru.absoft.util.cuteconfig.testutil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassPathManager {
	
	public static void addFolder(String folder) throws Exception {
		File file = new File(folder);
		addSoftwareLibrary(file);
	}
	
	public static void addSoftwareLibrary(File file) throws Exception {
	    Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
	    method.setAccessible(true);
	    method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
	}
	
	
}
