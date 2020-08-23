package com.lite.core.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * cookie util
 * 
 * @author Joe
 */
public class CookieUtils {

	private CookieUtils() {
	}

	public static void addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if ("https".equals(request.getScheme()))
			cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}
	/**
	 * get cookie
	 * 
	 * @param request
	 * @param name
	 * @return
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || StringUtils.isBlank(name)) {
			return null;
		}

		for (Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie.getValue();
			}
		}

		return null;
	}

	/**
	 * remove cookie
	 * 
	 * @param request
	 * @param response
	 * @param string
	 */
	public static void removeCookie(HttpServletResponse response, String name, String path, String domain) {

		Cookie cookie = new Cookie(name, null);

		if (path != null) {
			cookie.setPath(path);
		}

		if (domain != null) {
			cookie.setDomain(domain);
		}

		cookie.setMaxAge(-1000);
		response.addCookie(cookie);
	}
}
