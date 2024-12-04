package com.example.shopping.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
	
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder)
	{
		return restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
	}

}
