package ru.absoft.util.cuteconfig;

public interface ConfigurationListener{
	
	void onReadFileError(String message, Throwable t);
	
	void onFileChanged(long fileModifiedTime);
	
}