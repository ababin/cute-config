package ru.absoft.util.cuteconfig.loader;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class LoaderFactory {
			
	public static Loader obtainLoader(Path filePath) throws FileNotFoundException {
		return obtainLoader(filePath.toString());
	}
	
	public static Loader obtainLoader(String filePath) throws FileNotFoundException {
		if(AbsolutePathFileLoader.fileExists(filePath)) {
			return new AbsolutePathFileLoader(filePath);
		}
		if(ClassPathFileLoader.fileExists(filePath)) {
			return new ClassPathFileLoader(filePath);
		}
		throw new FileNotFoundException("File '" + filePath + "' is not found!");		
	}
	
	
}
