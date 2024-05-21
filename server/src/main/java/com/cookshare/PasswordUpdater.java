package com.cookshare;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUpdater {
	public static BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		String password = "user08!@";
		System.out.println(bCryptPasswordEncoder().encode(password));
	}
}
