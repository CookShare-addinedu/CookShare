package com.cookshare.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.cookshare.security.dto.CustomUserDetails;

public class SecurityUtils {

	/**
	 * 현재 인증된 사용자의 UserDetails를 가져옵니다.
	 * @return 현재 사용자의 UserDetails
	 */
	public static UserDetails getCurrentUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
			return null;
		}
		return (UserDetails) authentication.getPrincipal();
	}

	/**
	 * 현재 인증된 사용자의 ID를 가져옵니다.
	 * @return 현재 사용자의 ID
	 */
	public static Long getCurrentUserId() {
		UserDetails userDetails = getCurrentUserDetails();
		if (userDetails instanceof CustomUserDetails) {
			return ((CustomUserDetails) userDetails).getUserId();
		}
		return null;
	}
}
