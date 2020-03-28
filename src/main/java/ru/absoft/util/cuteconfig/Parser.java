package ru.absoft.util.cuteconfig;

import java.util.List;

import ru.absoft.util.cuteconfig.model.Value;

public class Parser {
			
	public List<Value> parse(List<String> lines){
		Context  ctx = new Context();
		for(String line : lines) {
			if(!line.trim().isEmpty() && !line.trim().startsWith("#")) {
				ctx.processString(line);
			}
		}
		ctx.commit();
		return ctx.getResult();
	}

}
