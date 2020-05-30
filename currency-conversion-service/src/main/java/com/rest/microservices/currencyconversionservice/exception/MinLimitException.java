package com.rest.microservices.currencyconversionservice.exception;

public class MinLimitException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MinLimitException(String message) {
		super(message);
	}
}
