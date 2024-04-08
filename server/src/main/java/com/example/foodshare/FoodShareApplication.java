package com.example.foodshare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication (exclude = SecurityAutoConfiguration.class)

public class FoodShareApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodShareApplication.class, args);
	}

}
