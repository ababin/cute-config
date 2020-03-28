package ru.absoft.util.cuteconfig;

import java.util.Map;

import ru.absoft.util.cuteconfig.model.Value;

public interface PostProcessor {
		
	Map<String, Value> process(Map<String, Value> map);
	
}
