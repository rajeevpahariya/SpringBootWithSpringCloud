package com.rest.microservices.currencyconversionservice.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.rest.microservices.currencyconversionservice.bean.CurrencyConversionBean;
import com.rest.microservices.currencyconversionservice.bean.LimitConfiguration;
import com.rest.microservices.currencyconversionservice.exception.MaxLimitReachedException;
import com.rest.microservices.currencyconversionservice.exception.MinLimitException;
import com.rest.microservices.currencyconversionservice.proxy.CurrencyExchangeServiceproxy;

@RestController
public class CurrencyConversionController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CurrencyExchangeServiceproxy proxy;
	
	@Autowired
	EurekaClient client;
	@Autowired
	DiscoveryClient dClient;
	@Autowired
	RestTemplateBuilder builder;
	
	@GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from,
			@PathVariable String to, @PathVariable BigDecimal quantity) {
		InstanceInfo instance = client.getNextServerFromEureka("currency-conversion-service", false);
		Map<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrencyConversionBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{from}/to/{to}",
				CurrencyConversionBean.class, uriVariables);
		CurrencyConversionBean response = responseEntity.getBody();
		return new CurrencyConversionBean(response.getId(), from, to, 
				response.getConversionMultiple(), quantity, 
				quantity.multiply(response.getConversionMultiple()), response.getPort(), instance.getPort());
	}
	
	@GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
			@PathVariable String to, @PathVariable BigDecimal quantity) 
					throws MinLimitException, MaxLimitReachedException {
		// Test for getting the port
		int port = getPort();
		// Getting limit configuration to get validate the input data.
		LimitConfiguration limitConfiguration = getLimitConfiguration();
		if(limitConfiguration == null) { 
			limitConfiguration = getDefaultLimitConfiguration(); 
		}
		// Validate the Input data
		validateInputData(limitConfiguration, quantity);
		CurrencyConversionBean response = proxy.retrieveExchangeValueFromDB(from, to);
		logger.info("{}",response);
		return new CurrencyConversionBean(response.getId(), from, to, 
				response.getConversionMultiple(), quantity, 
				quantity.multiply(response.getConversionMultiple()), response.getPort(),port);
	}
	
	private void validateInputData(LimitConfiguration limitConfiguration, BigDecimal quantity) 
			throws MinLimitException, MaxLimitReachedException {
		logger.info("Inside Validate Method");
		if(limitConfiguration.getMinimum() > quantity.intValue()) {
			logger.error("Min validateion failed");
			throw new MinLimitException("Amout to conversion is less than the min amount");
		}
		if(limitConfiguration.getMaximum() < quantity.intValue()) {
			logger.error("Max validateion failed");
			throw new MaxLimitReachedException("Amout to conversion is greater than the max amount");
		}
		
	}


	private LimitConfiguration getLimitConfiguration() {
		logger.info("Inside method to get the limit configuration");
		List<ServiceInstance> instances = dClient.getInstances("limits-service");
		String instanceAPI = null;
		if(instances != null && !instances.isEmpty()) {
			instanceAPI = instances.get(0).getUri().toString()+"/limits";
		}
		ResponseEntity<LimitConfiguration> exchange = builder.build().exchange(instanceAPI, HttpMethod.GET,
				null, LimitConfiguration.class);
		logger.info("Exit method to get the limit configuration");
		return exchange.getBody();
	}
	
	private LimitConfiguration getDefaultLimitConfiguration() {
		logger.info("Inside method to get the default limit configuration");
		List<ServiceInstance> instances = dClient.getInstances("limits-service");
		String instanceAPI = null;
		if(instances != null && !instances.isEmpty()) {
			instanceAPI = instances.get(0).getUri().toString()+"/limit";
		}
		ResponseEntity<LimitConfiguration> exchange = builder.build().exchange(instanceAPI, HttpMethod.GET,
				null, LimitConfiguration.class);
		logger.info("Exit method to get the default limit configuration");
		return exchange.getBody();
	}
	
	private int getPort() {
		int port = 0;
		// There are 2 way to make connection to other service using Eureka client or Discovery client
		// And we can pass the different micro service service name.
		
		//1
		InstanceInfo instance = client.getNextServerFromEureka("currency-conversion-service", false);
		port = instance.getPort();
		
		//2
		/*List<ServiceInstance> instances = dClient.getInstances("currency-conversion-service");
		if(instances != null && !instances.isEmpty()) {
			port= instances.get(0).getPort();
		}*/
		return port;
	}
	
}
