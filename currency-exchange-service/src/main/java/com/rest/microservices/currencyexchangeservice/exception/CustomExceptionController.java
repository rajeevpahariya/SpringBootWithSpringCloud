package com.rest.microservices.currencyexchangeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rest.microservices.currencyexchangeservice.bean.ExceptionBean;

@RestController
@ControllerAdvice
public class CustomExceptionController extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(CurrencyDataNotFound.class)
	public final ResponseEntity<Object> handleCurrnecyNotFound(CurrencyDataNotFound ex, WebRequest request){
		ExceptionBean exceptionBean = new ExceptionBean(ex.getMessage(), request.getDescription(false), HttpStatus.NOT_FOUND.toString());
		return new ResponseEntity<Object>(exceptionBean,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request){
		ExceptionBean exceptionBean = new ExceptionBean(ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.toString());
		return new ResponseEntity<Object>(exceptionBean,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
