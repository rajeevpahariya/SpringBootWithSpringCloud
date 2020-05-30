package com.rest.microservices.currencyconversionservice.exception;

public class MaxLimitReachedException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MaxLimitReachedException(String message) {
		super(message);
	}

}
