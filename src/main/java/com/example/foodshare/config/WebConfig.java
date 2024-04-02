package com.example.foodshare.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000") // 클라이언트 서버 주소 허용
			.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
			.allowCredentials(true);
	}


}


