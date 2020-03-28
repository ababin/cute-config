package ru.absoft.util.cuteconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ru.absoft.util.cuteconfig.model.Value;

public class IntervalPostProcessor implements PostProcessor{
			
	public static final String INTERVAL_SEPARATOR = "-";
	
	@Override
	public Map<String, Value> process(Map<String, Value> map) {
		Map<String, Value> res = new HashMap<>();
		map.forEach((k,v) -> {
			res.put(k, postProcessValue(v));
		});
		return res;
	}
	
	private Value postProcessValue(Value v) {
		switch(v.getType()) {
		case STRING	:	return processString(v);
		case LIST	:	return processList(v);
		case MAP	:	return processMap(v);
		default		:	throw new RuntimeException("There are no rules for type " + v);
		}
	}
		
	private Value processMap(Value v) {
		Map<String,String> res = new HashMap<>();
		for(Entry<String, String> entry : v.getMap().entrySet()) {
			if(isInterval(entry.getKey())) {
				processIntervalForMap(res, entry.getKey(), entry.getValue());
			}else {
				res.put(entry.getKey(), entry.getValue());
			}
		}
		return Value.builder()
				.paramName(v.getParamName())
				.type(v.getType())
				.string(v.getString())
				.map(res)
				.build();
	}

	private Value processList(Value v) {
		List<String> res = new ArrayList<>();
		for(String str : v.getList()) {
			if(isInterval(str)) {
				processIntervalForList(res, str);
			}else {
				res.add(str);
			}
		}
				
		return Value.builder()
				.paramName(v.getParamName())
				.type(v.getType())
				.string(v.getString())
				.list(res)
				.set(new HashSet<>(res))
				.build();
		
	}
	
	private Value processString(Value v) {
		return processList(v);
	}
		
		
	private void processIntervalForList(List<String> list, String intervalStr) {
		String[] intervals = intervalStr.split(INTERVAL_SEPARATOR);
		int intervalFrom = Integer.parseInt(intervals[0].trim());
		int intervalTo = Integer.parseInt(intervals[1].trim());
		for (int i = intervalFrom; i <= intervalTo ; i++) {
		    list.add(String.valueOf(i));
		}
	}
	
	private void processIntervalForMap(Map<String, String> map, String intervalKey, String val) {
		String[] intervals = intervalKey.split(INTERVAL_SEPARATOR);
		int intervalFrom = Integer.parseInt(intervals[0].trim());
		int intervalTo = Integer.parseInt(intervals[1].trim());
		for (int i = intervalFrom; i <= intervalTo ; i++) {
		    map.put(String.valueOf(i), val);
		}
	}

	private boolean isInterval(String intervalStr) {
		return intervalStr.contains(INTERVAL_SEPARATOR);
	}
		
}
