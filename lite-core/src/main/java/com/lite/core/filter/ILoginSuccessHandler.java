package com.lite.core.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lite.system.entity.SysUser;

public interface ILoginSuccessHandler {
	/**
	 * 
	 * @return backurl
	 */
	String onLoginSuccess(HttpServletRequest request, HttpServletResponse response, SysUser currUser);
	
	/**
	 * get current logined user
	 */
	SysUser getCurrUser();
	
	void logout(HttpServletRequest request, HttpServletResponse response);
}
