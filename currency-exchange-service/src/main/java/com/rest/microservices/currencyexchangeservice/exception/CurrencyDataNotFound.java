package com.rest.microservices.currencyexchangeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class CurrencyDataNotFound extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CurrencyDataNotFound(String string) {
		super(string);
	}

}
