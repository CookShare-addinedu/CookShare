package com.example.foodshare.config;

import com.example.foodshare.jwt.JwtFilter;
import com.example.foodshare.jwt.JwtUtil;
import com.example.foodshare.jwt.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;

    private final JwtUtil jwtUtil;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil){
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;

    }
    @Bean // (1) AuthenticationManager 빈으로 등록
    public AuthenticationManager authenticationManager
            (AuthenticationConfiguration configuration) throws Exception
    {
        //configuration인자 받아서 매니저로 사용한다.
        return configuration.getAuthenticationManager();
    }

    @Bean // (2)BCryptPasswordEncoder 비밀번호 암호화  빈등록
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // http 보안 설정을 정의 한다.
    // 이 설정에는 csrf, 보호 비활성화, 폼 로그인 비활성화, HTTP 기본인증 비활성화, 경로별 인가설정
    // 'LoginFilter'추가, 세션관리정책 설정(STATELESS)이 포함 된다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        /* 사이트 위조 못하게끔 의지와 무관하게, 토큰 발행해주면 무관하게 사용 가능 */
        httpSecurity.csrf((auth) -> auth.disable());

        /* 로그인폼 토큰을 사용할 것이기 때문에 사용안함*/
        httpSecurity.formLogin((auth) -> auth.disable());

        /* http basic 사용하는 인증방식 사용 안함 */
        httpSecurity.httpBasic((auth) -> auth.disable());

        /* 경로별 인가 작업 */
        httpSecurity.authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/enroll").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated());   /*인증이 반드시 필요하다*/


        //JWTFIlter 등록
        httpSecurity
                .addFilterBefore(new JwtFilter(jwtUtil),LoginFilter.class);

        // LoginFilter추가
        /*
            .addFilterAt -> 원하는 자리에 등록
            .addFilterBefore -> 해당하는 필터 전에 등록
            .addFilterAfter -> 해당하는 필터 후에 등록
         */


        httpSecurity                        /*위에서 갖고 온다*/     /* 이렇게 들어가야 인증 해준다 */
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil),
                        UsernamePasswordAuthenticationFilter.class);  //주의해서 작성한다

        // session 설정
        /*
            - stateless 상태 : 서버에서 HTTP와 같은 client의 이전 상태를 기록하지 않고 접속
            - REST 개념에서 각각의 요청은 독립적인 stateless 방식이고,
            - client가 상태정보를 모두 관리할 책임이 있다. (모든건 클라이언트 책임)
            - (니가알아서 해라 , 서버에서 관여하지 않겠다)
            - stateful 상태 : 서버에서 client의 이전 상태를 기록

         */
        /*httpSecurity
                    .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));*/


        /*----- 한줄로 줄여쓰기 가능
        httpSecurity.csrf((auth) -> auth.disable())
                .formLogin((auth) -> auth.disable())
                .httpBasic((auth) -> auth.disable())
                .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/enroll").permitAll()
                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()   *//*인증이 반드시 필요하다*//*
                .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return httpSecurity.build();
        )-----------------------------------------------*/

        // session 설정
        /*
            - stateless 상태 : 서버에서 HTTP와 같은 client의 이전 상태를 기록하지 않고 접속
            - REST 개념에서 각각의 요청은 독립적인 stateless 방식이고,
            - client가 상태정보를 모두 관리할 책임이 있다. (모든건 클라이언트 책임)
            - (니가알아서 해라 , 서버에서 관여하지 않겠다)
            - stateful 상태 : 서버에서 client의 이전 상태를 기록

         */
        httpSecurity
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //이 상태를 STATELESS로 만들어 주는 것이 중요
        return httpSecurity.build();


    }

}
