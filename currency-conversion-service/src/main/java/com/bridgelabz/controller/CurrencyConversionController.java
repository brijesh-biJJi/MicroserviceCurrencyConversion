package com.bridgelabz.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.CurrencyConversionBean;
import com.bridgelabz.CurrencyExchangeServiceProxy;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeServiceProxy proxy;
	
	//Feign Problem 1
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrency(@PathVariable String from,
												  @PathVariable String to,
												  @PathVariable BigDecimal quantity) 
	{
		//Feign Problem 1
		Map<String,String> uriVariables=new HashMap<>();
		uriVariables.put("ffrom", from);
		uriVariables.put("tto", to);
		
		ResponseEntity<CurrencyConversionBean> responseEntity= new RestTemplate().getForEntity(
				"http://localhost:8000/currency-exchange/from/{ffrom}/to/{tto}", 
				CurrencyConversionBean.class,
				uriVariables);
		
		CurrencyConversionBean response=responseEntity.getBody();
		
		return new CurrencyConversionBean(response.getId(), response.getFrom(), response.getTo(),response.getConversionMultiple(), quantity,quantity.multiply(response.getConversionMultiple()), response.getPort());
	}
	
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from,
												  @PathVariable String to,
												  @PathVariable BigDecimal quantity) 
	{
		
		CurrencyConversionBean response=proxy.retrieveExchangeValue(from, to);
		
		return new CurrencyConversionBean(response.getId(), response.getFrom(), response.getTo(),response.getConversionMultiple(), quantity,quantity.multiply(response.getConversionMultiple()), response.getPort());
	}
}