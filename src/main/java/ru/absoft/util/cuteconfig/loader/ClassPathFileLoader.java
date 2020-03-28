package ru.absoft.util.cuteconfig.loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ClassPathFileLoader implements Loader {

	private String filePath;
	private long lastTimeModified;

	public ClassPathFileLoader(String filePath) throws FileNotFoundException {
		this.filePath = filePath;
		if(!fileExists(filePath)) {
			throw new FileNotFoundException("File '"+filePath+"' is not found in classpath");
		}
	}

	@Override
	public List<String> readFile() throws IOException {
		InputStream stream  = ClassPathFileLoader.class.getClassLoader().getResourceAsStream(filePath);
		String str = readFromInputStream(stream);
		updateLastTimeModified();
		return  Arrays.asList(str.split("\n"));		
	}
	
	public static boolean fileExists(String filePath) {
		URL url = ClassPathFileLoader.class.getClassLoader().getResource(filePath);
		return url != null;
	}
	
	private static String readFromInputStream(InputStream inputStream) throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}

	@Override
	public long getLastTimeModified() throws IOException {
		URL url = getClass().getClassLoader().getResource(filePath);
		return url.openConnection().getLastModified();
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
