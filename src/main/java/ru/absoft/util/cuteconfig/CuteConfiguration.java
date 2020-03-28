package ru.absoft.util.cuteconfig;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.absoft.util.cuteconfig.exc.ParamNotFoundException;
import ru.absoft.util.cuteconfig.model.Value;

public class CuteConfiguration {
	
	private final FileObserver observer;
	
	public CuteConfiguration(String filePath, int refreshPeriodMS, ConfigurationListener confListener, PostProcessor ... postProcessors) throws FileNotFoundException {
		observer = new FileObserver(filePath, refreshPeriodMS, confListener, postProcessors);
	}
	
	public CuteConfiguration(String filePath, int refreshPeriodMS, ConfigurationListener confListener) throws FileNotFoundException {
		observer = new FileObserver(filePath, refreshPeriodMS, confListener);
	}
	
	public CuteConfiguration(String filePath, int refreshPeriodMS) throws FileNotFoundException {
		observer = new FileObserver(filePath, refreshPeriodMS, null);
	}
			
	public String getString(String paramName) {
		Map<String, Value> map = observer.getValues(); 
		if(!map.containsKey(paramName)) {
			throw new ParamNotFoundException(paramName);
		}
		return map.get(paramName).getString();
	}
	
	public String getString(String paramName, String defaultValue) {
		try {
			return getString(paramName);
		}catch(ParamNotFoundException e) {
			return defaultValue;
		}
	}
	
	public List<String> getStringList(String paramName) {
		if(!observer.getValues().containsKey(paramName)) {
			throw new ParamNotFoundException(paramName);
		}
		return observer.getValues().get(paramName).getList(); 
	}
	
	public List<String> getStringList(String paramName, List<String> defList) {
		try {
			return getStringList(paramName);
		}catch(ParamNotFoundException e) {
			return defList;
		}
	}
	
	public Set<String> getStringSet(String paramName) {
		if(!observer.getValues().containsKey(paramName)) {
			throw new ParamNotFoundException(paramName);
		}
		return observer.getValues().get(paramName).getSet(); 
	}
	
	public Set<String> getStringSet(String paramName, Set<String> defSet) {
		try {
			return getStringSet(paramName);
		}catch(ParamNotFoundException e) {
			return defSet;
		}
	}
		
	public Map<String,String> getStringMap(String paramName) {
		if(!observer.getValues().containsKey(paramName)) {
			throw new ParamNotFoundException(paramName);
		}
		return observer.getValues().get(paramName).getMap(); 
	}
		
	public Map<String, Value> getParams(){
		return observer.getValues();
	}
	  
	public boolean getBoolean(String paramName) {
		 String val = getString(paramName);
		 return Boolean.valueOf(val).booleanValue();
	}
	
	public boolean getBoolean(String paramName, boolean defaultValue) {
		try {
			return getBoolean(paramName);
		}catch(ParamNotFoundException e) {
			return defaultValue;
		}
	}
	
	
	
}
