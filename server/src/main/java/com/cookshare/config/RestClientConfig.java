package com.cookshare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class RestClientConfig {

	@Bean
	public RestTemplate restTemplate() {
		log.info("Creating RestTemplate...");
		return new RestTemplate();
	}
}
