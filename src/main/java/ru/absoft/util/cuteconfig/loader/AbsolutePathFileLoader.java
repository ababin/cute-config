package ru.absoft.util.cuteconfig.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AbsolutePathFileLoader implements Loader{

	private String filePath;
	private long lastTimeModified;
	
	@Override
	public List<String> readFile() throws IOException {
		List <String> lines = Files.readAllLines(Paths.get(filePath)); 
		updateLastTimeModified();		
		return lines; 
	}
	
	public AbsolutePathFileLoader(String filePath) throws FileNotFoundException {
		String normalizedPath = normalizePath(filePath);
		if(!fileExists(normalizedPath)) {
			throw new FileNotFoundException("File '" + normalizedPath + "' is not found!");
		}
		this.filePath = normalizedPath;
	}
		
	public static boolean fileExists(String filePath) {
		String normalizedPath = normalizePath(filePath);
		return Files.exists(Paths.get(normalizedPath)); 
	}
	
	private static String normalizePath(String path) {
		if(path.startsWith(File.separator)) {
			return path;
		}else {
			return Paths.get(System.getProperty("user.dir"), path).toString();
		}
	}

	@Override
	public long getLastTimeModified() throws IOException {
		File file=new File(filePath);
		return file.lastModified();
	}
	
	@Override
	public boolean fileIsChanged() throws IOException {
		long curTimeModified = getLastTimeModified();
		return curTimeModified != lastTimeModified;
	}
	
	private void updateLastTimeModified() throws IOException {
		lastTimeModified = getLastTimeModified();
	}
	
}
