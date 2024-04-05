package com.example.foodshare.jwt;

import com.example.foodshare.domain.User;
import com.example.foodshare.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    public JwtFilter(JwtUtil jwtUtil){

        this.jwtUtil = jwtUtil;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // request에서 Authorization을 찾음 (Loginfilter에서 Authorization헤더를 찾아서 넣어즘 )
        String authorization = request.getHeader("Authorization");

        // Authorization 검증
        if(authorization == null || !authorization.startsWith("Bearer ")){
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];  /*여기에는 순수히 토큰만*/
        System.out.println("token : " + token);

        // 토큰 소멸 시간 검증 (발행한 이후에 그사람 자체는 이미 DB로 검증 )
        if(jwtUtil.isExpired(token)){
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setUsername(username);
        user.setPassword("jwtpassword1234");
        user.setRole(role);

        // 세션에 일시적으로 넣어줌
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(customUserDetails
                        , null, customUserDetails.getAuthorities());

        //user 세션 만들기
        SecurityContextHolder.getContext().setAuthentication(authToken);
        filterChain.doFilter(request, response);




    }
}



