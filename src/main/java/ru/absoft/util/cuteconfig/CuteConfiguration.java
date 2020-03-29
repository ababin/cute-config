package ru.absoft.util.cuteconfig;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Builder;
import ru.absoft.util.cuteconfig.exc.ParamNotFoundException;
import ru.absoft.util.cuteconfig.model.Value;

public class CuteConfiguration {
	        
	private final FileObserver observer;
	private final FileObserver overridedObserver;
	
	@Builder
	private CuteConfiguration(String filePath, String overridedFilePath, int refreshPeriodMS, ConfigurationListener confListener, PostProcessor postProcessor) throws FileNotFoundException {
	    observer = new FileObserver(filePath, refreshPeriodMS, confListener, postProcessor);
	    overridedObserver = overridedFilePath != null ?
	            new FileObserver(overridedFilePath, refreshPeriodMS, confListener, postProcessor)
	            : null;
	}
					
	public String getString(String paramName) {
		Value val = getValue(paramName);
		if(val == null) {
			throw new ParamNotFoundException(paramName);
		}
		return val.getString();
	}
	
	public String getString(String paramName, String defaultValue) {
		try {
			return getString(paramName);
		}catch(ParamNotFoundException e) {
			return defaultValue;
		}
	}
	
	public List<String> getStringList(String paramName) {
	    Value val = getValue(paramName);
	    if(val == null) {
			throw new ParamNotFoundException(paramName);
		}
		return val.getList(); 
	}
	
	public List<String> getStringList(String paramName, List<String> defList) {
		try {
			return getStringList(paramName);
		}catch(ParamNotFoundException e) {
			return defList;
		}
	}
	
	public Set<String> getStringSet(String paramName) {
	    Value val = getValue(paramName);
	    if(val == null) {
			throw new ParamNotFoundException(paramName);
		}
		return val.getSet(); 
	}
	
	public Set<String> getStringSet(String paramName, Set<String> defSet) {
		try {
			return getStringSet(paramName);
		}catch(ParamNotFoundException e) {
			return defSet;
		}
	}
		
	public Map<String,String> getStringMap(String paramName) {
	    Value val = getValue(paramName);
	    if(val == null) {
			throw new ParamNotFoundException(paramName);
		}
		return val.getMap(); 
	}
		
	public Map<String, Value> getParams(){
		if(overridedObserver == null) {
		    return observer.getValues(); 
		}
		Map <String, Value> map = new HashMap<>(observer.getValues());
		map.putAll(overridedObserver.getValues());
	    return map;	    
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
	
	private Value getValue(String paramName) {
	    return overridedConfigContains(paramName) ?
	            overridedObserver.getValues().get(paramName)
	            : observer.getValues().get(paramName);
	}
	
	private boolean overridedConfigContains(String paramName) {
	    return overridedObserver != null && overridedObserver.getValues().containsKey(paramName);
	}
}
