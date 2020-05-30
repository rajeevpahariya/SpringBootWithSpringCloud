package com.rest.microservices.currencyexchangeservice.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rest.microservices.currencyexchangeservice.bean.ExchangeValue;
import com.rest.microservices.currencyexchangeservice.exception.CurrencyDataNotFound;
import com.rest.microservices.currencyexchangeservice.repository.ExchangeValueRepository;

@RestController
public class CurrencyExchangeController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private Environment env;
	
	@Autowired
	private ExchangeValueRepository repo;
	
	@GetMapping("/currency-exchange/static/from/{from}/to/{to}")
	public ExchangeValue retrieveExchangeValue(@PathVariable String from,
			@PathVariable String to) {
		ExchangeValue exchangeValue = new ExchangeValue(100L, from, to, BigDecimal.valueOf(65D));
		exchangeValue.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		return exchangeValue;
	}
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public ExchangeValue retrieveExchangeValueFromDB(@PathVariable String from,
			@PathVariable String to) throws CurrencyDataNotFound{
		
		ExchangeValue exchangeValue = repo.findByFromAndTo(from, to);
		if(exchangeValue == null) {
			throw new CurrencyDataNotFound("Curreny Data not found for: from = " + from + " and To = " + to);
		}
		exchangeValue.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		logger.info("{}", exchangeValue);
		return exchangeValue;
	}
}
