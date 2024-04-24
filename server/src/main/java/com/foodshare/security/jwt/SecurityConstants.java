package com.foodshare.security.jwt;

// Security 및 JWT 관련된 상수를 관리하는 클래스
/*
    http
        headers : {
            Authorization : Bearer ${jwt}
        }
 */
public class SecurityConstants {
    // JWT 토큰을 담을 HTTP 요청 헤더 이름
    public static final String TOKEN_HEADER = "Authorization";
    // 헤더의 접두사
    public static final String TOKEN_PREFIX = "Bearer";
    // 토큰 타입
    public  static final String TOKEN_TYPE = "JWT";
    // JWT 토큰이 담길 HTTP 헤더의 이름
    public static final String HEADER_STRING = "Authorization";
    // 토큰 만료 시간을 밀리초로 설정
    public static final long EXPIRATION_TIME = 5L * 60 * 1000; // 5 분

    // 리프레시 토큰을 담을 HTTP 요청 헤더 이름
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Token";
    // 토큰 만료 시간을 밀리초로 설정
    // 리프레시 토큰 만료 시간
    public static final long REFRESH_EXPIRATION_TIME = 60L * 60 * 1000; // 1  시간


}
