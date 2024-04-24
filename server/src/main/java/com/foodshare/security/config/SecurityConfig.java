package com.foodshare.security.config;

import com.foodshare.security.jwt.JwtAuthenticationFilter;
import com.foodshare.security.jwt.JwtAuthorizationFilter;
import com.foodshare.security.jwt.JwtProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProp jwtProp;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 인스턴스를 반환
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/api/users"

        );
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return auth.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(authenticationManager, jwtProp);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(userDetailsService, jwtProp);
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //폼 기반 로그인 비 활성화
                .formLogin( (login) -> login.disable() )
                //Http 기본 인증 비활성화
                .httpBasic( (basic) ->  basic.disable() )
                //.httpBasic(Customizer.withDefaults());
                .csrf( (csrf) -> csrf.disable() )
                .sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api").permitAll() // 로그인, 회원가입 등의 경로는 누구나 접근 가능
                        .anyRequest().authenticated()) // 그 외 모든 요청은 인증 필요
                .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtProp))
                .addFilterBefore(new JwtAuthorizationFilter(userDetailsService, jwtProp),
                UsernamePasswordAuthenticationFilter.class)
                );



        return http.build();
    }
}



