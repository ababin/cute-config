package ru.absoft.util.cuteconfig.loader;

import java.io.IOException;
import java.util.List;

public abstract interface Loader {
	
	List<String> readFile() throws IOException;
	
	long getLastTimeModified() throws IOException;
	
	boolean fileIsChanged() throws IOException;
	
		
}
