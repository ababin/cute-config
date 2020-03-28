package ru.absoft.util.cuteconfig.exc;

import lombok.Getter;

public class ParamNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	
	private @Getter String paramName;
	
	public ParamNotFoundException(String paramName) {
		super();
		this.paramName = paramName;
	}
	
	public String getMessage() {
		return String.format("Parameter '%s' not found!", paramName);
	}
	
}
