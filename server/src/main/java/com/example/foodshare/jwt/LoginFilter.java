package com.example.foodshare.jwt;

import com.example.foodshare.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
        // 검증 담당 - 매니저가 검증을 한다.
        private final AuthenticationManager authenticationManager;

        private final JwtUtil jwtUtil;
        //생성자
        public LoginFilter(AuthenticationManager authenticationManager , JwtUtil jwtUtil ){
                                                                    //토큰 검증
            this.authenticationManager = authenticationManager;
            this.jwtUtil = jwtUtil;
        }
        @Override
        public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse)  {
            // 클라이언트 요청에서 username, password추출
            String username = obtainUsername(request);  //request에서 사용자 이름을 가져오는 메소드
            String password = obtainPassword(request);  //request에서 사용자 비번을 가져오는 메소드

            //스프링 시큐리티에서 username, password를 검증하기 위해 token에 담아야 함
            // 맞는지를 검증한다.
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password, null);

            //token에 담아 검증을 위한 authenticationManager로 전달
            return authenticationManager.authenticate(authenticationToken);
        }
            // 로그인 성공시 실행하는 메소드( 실제 jwt를 발급하면 됨)
            //내 맘대로 하는 것이 아니라 메소드가 정해져 있다.
            @Override
            protected void successfulAuthentication(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    FilterChain chain, Authentication authentication){

                CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal(); /* 이부분은 이대로*/

                String username = customUserDetails.getUsername();

                /*for문처럼 돌아서 갖고 와야 한다 .iterate로 돌면서 갖고 와야 한다. */
                /*컬렉션은순서가 없어서 한번에 다 갖고 오고 iterate에서 하나씩 배고  뺄 것이 없으면 끝나는 */

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
                GrantedAuthority auth = iterator.next();

                String role = auth.getAuthority();

                String token = jwtUtil.createJwt(username, role, 1000*60L);
                                                     /*글씨 넣어주고 한칸 띄고 토큰이 붙는다, 암호화가 되어 토큰 발행된 것을 헤더에 추가*/
                response.addHeader("Authorization","Bearer " + token);

            }
            //Authorization : Bearer 토큰  이런식으로 구성
            // 필터를 하나 추가 시켜줘야 함
            // 로그인 실패시 실행하는 메소드
            @Override
            protected void unsuccessfulAuthentication(HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    AuthenticationException failed){
                response.setStatus(401);

                /*서블릿 필터에서 유지되도록 하는 것 , 세션 유지 1번만 하는 것 추가 해야
                로그인 되었을 때, 일시적으로 세션에 넣어주기 */

        }
}


