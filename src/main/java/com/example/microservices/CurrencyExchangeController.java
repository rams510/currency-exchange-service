package com.example.microservices;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

	Logger ZUUL_LOGG = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private Environment env;
	@Autowired
	private ExchangeValueRepository repo;
	

	// case 1: hardcoding the values and calling the micro service
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public ExchangeValue retriveExchangeValue1(@PathVariable("from") String from, @PathVariable("to") String to) {

		return new ExchangeValue(1000L, from, to, BigDecimal.valueOf(65),
				Integer.parseInt(env.getProperty("local.server.port")));
	}

	
	//CASE 2: calling the jpa repositor through the spring data jpa using H2 inmemory database
	@GetMapping("/currency-exchange-jpa/from/{from}/to/{to}")
	public ExchangeValue retriveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to) {
		
		ExchangeValue exchangeValue=repo.findByFromAndTo(from, to);
		exchangeValue.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		
		ZUUL_LOGG.info("{}",exchangeValue);
		return exchangeValue;

	}

}
