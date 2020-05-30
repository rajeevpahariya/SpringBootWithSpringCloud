package com.rest.microservices.currencyexchangeservice.bean;

public class ExceptionBean {
	private String message;
	private String description;
	private String errorCode;
	
	public ExceptionBean(String message, String description, String errorCode) {
		super();
		this.message = message;
		this.description = description;
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
}
