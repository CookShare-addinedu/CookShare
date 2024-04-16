////package com.example.foodshare.security.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
//import org.springframework.security.config.http.SessionCreationPolicy;
////import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//// 시큐리티에서 인증,인가 같이 관리
//import com.example.foodshare.security.jwt.JwtAuthenticationFilter;
//import com.example.foodshare.security.jwt.JwtAuthorizationFilter;
//import com.example.foodshare.security.jwt.JwtProp;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig {
////
////    private final JwtProp jwtProp;
////
////
////    @Autowired
////    public SecurityConfig(JwtProp jwtProp) {
////        this.jwtProp = jwtProp;
////    }
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 인스턴스를 반환
////    }
////
////    @Bean
////    public WebSecurityCustomizer webSecurityCustomizer() {
////        return (web) -> web.ignoring().requestMatchers(
////                "/api/users",
////                "/api/users/signup",
////                "/api/users/login",
////                "/Register"
////
////        );
////    }
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManagerBuilder auth) throws Exception {
////        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(
////                authenticationManagerBean(auth),
////                jwtProp
////        );
////
////        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(
////                (UserDetailsService) authenticationManagerBean(auth)
////        );
////
////        http
////                .csrf((csrf) -> csrf.disable())// CSRF 보호 비활성화
////                .formLogin((formLogin) -> formLogin.disable()) // 폼 기반 로그인 비활성화
////                .httpBasic((httpBasic) -> httpBasic.disable()) // 기본 HTTP 인증 비활성화
////                .sessionManagement( (sessionManagement) ->
////                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션을 사용하지 않고 STATELESS로 설정.authorizeHttpRequests((authorizeRequests) ->
////
////                .authorizeHttpRequests( (authorize) ->
////                    authorize
////                            .requestMatchers("/api/users/login", "/api/users/signup", "/Register").permitAll() // 로그인 및 회원가입 경로는 누구나 접근 가능
////                            .anyRequest().authenticated())
////
////                .addFilter(jwtAuthenticationFilter) // JwtAuthenticationFilter 추가
////                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class); // JwtAuthorizationFilter를 JwtAuthenticationFilter 뒤에 추가
////
////        return http.build();
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManagerBean(AuthenticationManagerBuilder auth) throws Exception {
////        return auth.build();
////    }
////
////    // 필터가 사용할 AuthenticationManagerBean을 노출
////    @Autowired
////    public  void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////    }
////
////}
////
////@Configuration
////@EnableWebSecurity
////public class SecurityConfig {
////
////    private final JwtProp jwtProp;
////    private final UserDetailsService userDetailsService;
////
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
////
////    @Autowired
////    public SecurityConfig(JwtProp jwtProp, UserDetailsService userDetailsService) {
////        this.jwtProp = jwtProp;
////        this.userDetailsService = userDetailsService;
////    }
////
////
////
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf( (csrf) -> csrf.disable()
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and()
////                .authorizeHttpRequests(auth -> auth
////                        .requestMatchers("/api/users/login", "/api/users/signup", "/Register").permitAll()
////                        .anyRequest().authenticated())
////                .addFilter(new JwtAuthenticationFilter(authenticationManager(http), jwtProp))
////                .addFilterBefore(new JwtAuthorizationFilter(userDetailsService), UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
////    }
////
////    @Bean
////    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
////        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
////    }
////
////}
//
//
//
//
//
//
//
//


package com.example.foodshare.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCryptPasswordEncoder 인스턴스를 반환
    }

    // Spring Security의 HTTP 요청에 대한 보안 설정을 추가합니다.

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(
                "/api/users" ,
                "/api/users/signup" ,
//                "/api/users/login",
                "/Register"

        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //폼 기반 로그인 비 활성화
                .formLogin( (login) -> login.disable() )
                //Http 기본 인증 비활성화
                .httpBasic( (basic) ->  basic.disable() )
                //.httpBasic(Customizer.withDefaults());
                .csrf( (csrf) -> csrf.disable() );

//                .securityMatchers((matchers) -> matchers
//                        .requestMatchers("/api/**")
//                )
//                .authorizeHttpRequests((authorize) -> authorize
//                        .requestMatchers("/api/users/login").permitAll() // 로그인 경로 허용
//                        .anyRequest().hasRole("USER")
//                );

        return http.build();
    }
}



