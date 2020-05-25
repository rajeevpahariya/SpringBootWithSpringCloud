package com.rest.microservices.currencyconversionservice.proxy;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.rest.microservices.currencyconversionservice.bean.CurrencyConversionBean;

// We don't need URL in Feign in because we are using Ribbon and URL will be getting from Application.properties file
//@FeignClient(name = "currency-exchange-service", url = "localhost:8000")
// Commented Feign - Now request from Conversion service to exchange service will go by the Zull API Gateway
//@FeignClient(name = "currency-exchange-service")
@FeignClient(name = "netflix-zuul-api-gateway-server")
@RibbonClient(name = "currency-exchange-service")
public interface CurrencyExchangeServiceproxy {
	
	// Now request should go through the Zuul gateway, hence adding the exchange service APP name.
	//@GetMapping("/currency-exchange/from/{from}/to/{to}")
	@GetMapping("/currency-exchange-service/currency-exchange/from/{from}/to/{to}")
	public CurrencyConversionBean retrieveExchangeValueFromDB(@PathVariable("from") String from,
			@PathVariable("to") String to);
	
}
