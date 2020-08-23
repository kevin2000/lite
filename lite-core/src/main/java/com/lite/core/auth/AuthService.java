package com.lite.core.auth;

import org.springframework.stereotype.Service;

import com.lite.system.entity.SysUser;

@Service
public class AuthService {
	public static String token = "token";
	
	public SysUser getCurrUser() {
		return null;
	}
	
	public boolean validAuthority(SysUser user, String path) {
		return true;
	}
}
