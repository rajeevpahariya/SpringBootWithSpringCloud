package com.rest.microservices.currencyconversionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rest.microservices.currencyconversionservice.bean.ExceptionBean;


@RestController
@ControllerAdvice
public class CustomExceptionController extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(MaxLimitReachedException.class)
	public final ResponseEntity<Object> handleMaxLimitException(MaxLimitReachedException ex, WebRequest request){
		ExceptionBean exceptionBean = new ExceptionBean(ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<Object>(exceptionBean,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MinLimitException.class)
	public final ResponseEntity<Object> handleMinLimitException(MinLimitException ex, WebRequest request){
		ExceptionBean exceptionBean = new ExceptionBean(ex.getMessage(), request.getDescription(false), HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<Object>(exceptionBean,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request){
		ExceptionBean exceptionBean = new ExceptionBean(ex.getMessage(), request.getDescription(false), HttpStatus.INTERNAL_SERVER_ERROR.toString());
		return new ResponseEntity<Object>(exceptionBean,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
