package com.foodshare.security.config;

import com.foodshare.security.jwt.JwtAuthenticationFilter;
import com.foodshare.security.jwt.JwtAuthorizationFilter;
import com.foodshare.security.jwt.JwtUtil;
import com.foodshare.security.service.RedisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final AuthenticationConfiguration authenticationConfiguration;
	private final RedisService redisService;

	private final JwtUtil jwtUtil;

	@Autowired
	public WebSecurityConfig(AuthenticationConfiguration configuration, JwtUtil jwtUtil, RedisService redisService) {
		this.authenticationConfiguration = configuration;
		this.jwtUtil = jwtUtil;
		this.redisService = redisService;
	}

	@Bean
	public AuthenticationManager authenticationManager
		(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			//JWT 쓰면서 안쓰는 스프링 필터들 disable (세션관련)
			.formLogin((login) -> login.disable())
			.httpBasic((basic) -> basic.disable())
			.csrf((auth) -> auth.disable())
			.rememberMe((rememberMe) -> rememberMe.disable())
			.sessionManagement((sessionManagement) ->
				sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			// Role에 따라 API 접근 제한
			.authorizeHttpRequests((authz) -> authz
				.requestMatchers("/login",
					"/register",
                    "/login",
					"/api/chat/**",
					"/ws/**",
					"/api/user/login",
					"/api/register",
					"/api/user/reissue",
					"/home"

				).permitAll()
				.requestMatchers(
					"/api/room/**",
					"/api/createRoom/**",
					"/api/detailRoom/**",
					"/api/mypage"

				).hasRole("user")
				.requestMatchers("/api/admin/**").hasRole("admin")
				.anyRequest().authenticated() // 그 외 모든 요청은 인증 필요
			);
		http
			//                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), JwtAuthenticationFilter.class)
			//                .addFilterAt(new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService)
			//                        ,UsernamePasswordAuthenticationFilter.class);
			.addFilterBefore(new JwtAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(
				new JwtAuthenticationFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService),
				UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
