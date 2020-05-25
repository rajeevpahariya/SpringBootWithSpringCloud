package com.rest.microservices.limitsservice.limits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.rest.microservices.limitsservice.limits.bean.LimitConfiguration;

@RestController
public class LimitsConfigurationController {
	
	@Autowired
	private Configuration configuration;
	
	@GetMapping("/limit")
	public LimitConfiguration reterieveLimitsHardcoded() {
		return new LimitConfiguration(1000, 1);
	}
	
	@GetMapping("/limits")
	public LimitConfiguration reterieveLimitsConfiguration() {
		return new LimitConfiguration(configuration.getMaximum(),
				configuration.getMinimum());
	}
	
	@GetMapping("/limits-fault")
	@HystrixCommand(fallbackMethod = "faultReterieveLimitsValue")
	public LimitConfiguration reterieveLimitsValue() {
		throw new RuntimeException("Hytrix Validation");
	}
	
	public LimitConfiguration faultReterieveLimitsValue() {
		return new LimitConfiguration(999999,
				9);
	}
}
