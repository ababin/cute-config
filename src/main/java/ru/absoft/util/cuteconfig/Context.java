package ru.absoft.util.cuteconfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import ru.absoft.util.cuteconfig.model.ValType;
import ru.absoft.util.cuteconfig.model.Value;

public class Context {
	
		
	public static final String LIST_VALUES_DELIMETER = ",";
	public static final String MAP_KEY_VALUE_DELIMETER = ":";
	
	/*
	 * Current mode during the process of parsing
	 */
	private Mode mode = Mode.PROPS;
	
	/*
	 * Parameter name for LIST or MAP
	 */
	private String       paramName = null;
	
	private List<String> bufferList = null;
	
	/*
	 * result list of values !!!!
	 */
	private @Getter List<Value> result = new ArrayList<>(100);
	
	
	public void processString(String line) {
		if (mode == Mode.PROPS) {
			processSimpleMode(line);
		} else {
			processComplexMode(line);
		}
	}
	
	public void commit() {
		if(mode == Mode.LIST || mode == Mode.MAP) {
			addAccumulatedComplexProperty();
		}
	}

	private void processSimpleMode(String line) {
		if(isStartComplexProperty(line)) {
			setComplexMode(line);
			initParamName(line);
			initBuffer();
		}else {
			addSimpleProperty(line);
		}
	}
	
	private void processComplexMode(String line) {
		if(isStartComplexProperty(line)) {
			addAccumulatedComplexProperty();
			initParamName(line);
			initBuffer();
			setComplexMode(line);
		}else {
			bufferList.add(line);
		}
	}

	private void initParamName(String line) {
		if(isStartList(line)) {
			paramName = getListParamName(line);
		}else {
			paramName = getMapParamName(line);
		}
	}

	private void addAccumulatedComplexProperty() {
		String finalString = String.join(LIST_VALUES_DELIMETER, bufferList); 
		if(mode == Mode.LIST) {
			result.add(prepareListValue(paramName, finalString));
		}else {
			result.add(prepareMapValue(paramName, finalString));
		}
	}
	
	private Value prepareListValue(String paramName, String finalString) {
		List <String> list = finalString.trim().isEmpty() ?
				Collections.EMPTY_LIST
				:Arrays.asList(finalString.split(LIST_VALUES_DELIMETER));
		Set <String> set = finalString.trim().isEmpty() ? 
				Collections.EMPTY_SET
				: new HashSet<>(Arrays.asList(finalString.split(LIST_VALUES_DELIMETER)));
		return Value.builder()
				.paramName(paramName)
				.type(ValType.LIST)
				.string(finalString)
				.list(list)
				.set(set)
				.build();
	}
	
	private Value prepareStringValue(String paramName, String finalString) {
		List <String> list  = finalString.trim().isEmpty() ? 
				Collections.EMPTY_LIST
				: Arrays.asList(finalString.split(LIST_VALUES_DELIMETER));
		Set <String> set = finalString.trim().isEmpty() ?
				Collections.EMPTY_SET
				: new HashSet<>(Arrays.asList(finalString.split(LIST_VALUES_DELIMETER)));
		return Value.builder()
				.paramName(paramName)
				.type(ValType.STRING)
				.string(finalString)
				.list(list)
				.set(set)
				.build();
	}
	
	private Value prepareMapValue(String paramName, String finalString) {
		List<String> vals = Arrays.asList(finalString.split(LIST_VALUES_DELIMETER));
		Map<String, String> map = new HashMap<>();
		for(String str : vals) {
			String [] ar = str.split(Context.MAP_KEY_VALUE_DELIMETER);
			if(ar.length == 2) {
				map.put(ar[0].trim(), ar[1].trim());
			}
		}
		
		return Value.builder()
				.paramName(paramName)
				.type(ValType.MAP)
				.string(finalString)
				.map(map)
				.build();
	}

	private void initBuffer() {
		bufferList = new ArrayList<>();
	}

	private void setComplexMode(String line) {
		if(isStartList(line)) {
			mode = Mode.LIST;
		}else {
			mode = Mode.MAP;
		}
	}
	
	private void addSimpleProperty(String line) {
		Pair pair = parseLine(line);
		result.add(prepareStringValue(pair.key, pair.value));
	}
	
	boolean isStartComplexProperty(String line) {
		return isStartList(line) || isStartMap(line);
	}
	
	boolean isStartList(String line) {
		Pattern pattern = Pattern.compile("(\\[(.+)\\])");
		Matcher matcher = pattern.matcher(line.replaceAll("\\s", ""));
		return matcher.find();
	}
	
	String getListParamName(String line) {
		Pattern pattern = Pattern.compile("(\\[(.+)\\])");
		Matcher matcher = pattern.matcher(line.replaceAll("\\s", ""));
		if(matcher.find()) {
			return matcher.group(2);
		}else {
			return null;
		}
	}
	
	boolean isStartMap(String line) {
		Pattern pattern = Pattern.compile("(\\{.+\\})");
		Matcher matcher = pattern.matcher(line.replaceAll("\\s", ""));
		return matcher.find();
	}
	
	String getMapParamName(String line) {
		Pattern pattern = Pattern.compile("(\\{(.+)\\})");
		Matcher matcher = pattern.matcher(line.replaceAll("\\s", ""));
		if(matcher.find()) {
			return matcher.group(2);
		}else {
			return null;
		}
	}
		
	Pair parseLine(String line) {
		Pattern pattern = Pattern.compile("((.+)=(.*))");
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()) {
    	    String key = matcher.group(2).trim();
    	    String value = matcher.group(3).trim();
			return new Pair(key, value);
    	}
		return null;
	}
	
	private enum Mode {
		PROPS, LIST, MAP;
	}
	
	static class Pair{
		final String key;
		final String value;
		
		public Pair(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}
	
	

}
