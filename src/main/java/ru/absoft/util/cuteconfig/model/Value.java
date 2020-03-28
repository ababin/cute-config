package ru.absoft.util.cuteconfig.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Value {
	
	@Getter private String paramName;
	@Getter private ValType type;
	
	@Getter private String string;
	@Getter private List <String> list;
	@Getter private Set <String> set;
	@Getter private Map <String, String> map;
		
}
