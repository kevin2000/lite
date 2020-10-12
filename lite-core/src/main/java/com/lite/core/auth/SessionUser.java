package com.lite.core.auth;

import java.io.Serializable;

import com.lite.core.entity.SysUser;

/**
 * 已登录用户信息
 * 
 * @author Joe
 */
public class SessionUser implements Serializable {

	private static final long serialVersionUID = 1764365572138947234L;

	// 登录用户访问Token
	private String token;
	// 登录名
	private SysUser user;

	public SessionUser() {
		super();
	}

	public SessionUser(String token, SysUser user) {
		super();
		this.token = token;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public SysUser getUser() {
		return user;
	}

	public void setUser(SysUser user) {
		this.user = user;
	}
}
