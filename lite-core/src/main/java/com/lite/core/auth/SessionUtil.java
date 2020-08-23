package com.lite.core.auth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import com.lite.core.dto.MenuDto;
import com.lite.system.entity.SysUser;


/**
 * 当前已登录用户Session
 * 
 * @author Joe
 */
public class SessionUtil {
	/**
	 * 用户信息
	 */
	public static final String SESSION_USER = "_sessionUser";

	/**
	 * 用户权限
	 */
	public static final String SESSION_USER_PERMISSION = "_sessionUserPermission";
	
	private static final String SESSION_USER_MENUS = "_sessionUserMenus";

	public static SessionUser getSessionUser(HttpServletRequest request) {
		return (SessionUser) WebUtils.getSessionAttribute(request, SESSION_USER);
	}

	public static void setSessionUser(HttpServletRequest request, SessionUser sessionUser) {
		WebUtils.setSessionAttribute(request, SESSION_USER, sessionUser);
	}

	public static HttpServletRequest getCurrentHttpReuqest() {
		try {
			ServletRequestAttributes attributes = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes());
			if (null != attributes) {
				return attributes.getRequest();
			}
			
		} catch (Exception e) {
			
		}
		//HttpSession session= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		return null;
	}
	
	public static String getCurrentUserName() {
		try {
			SessionUser  user = getSessionUser(getCurrentHttpReuqest());
			if (null != user)
				return user.getUser().getUserName();
		} catch (Exception e) {
			
		}
		//HttpSession session= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		return null;
	}
	
	public static SysUser getCurrentUser(HttpServletRequest request) {
		SessionUser  user = getSessionUser(getCurrentHttpReuqest());
		if (null != user)
			return user.getUser();
		return null;
	}
	
	public static SysUser getCurrentUser() {
		try {
			SessionUser  user = getSessionUser(getCurrentHttpReuqest());
			if (null != user)
				return user.getUser();
		} catch (Exception e) {
			
		}
		//HttpSession session= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
		return null;
	}
	
	public static List<MenuDto> getSessionUserMenus() {
		try {
			return getSessionUserMenus(getCurrentHttpReuqest());
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void setSessionUserMenus(List<MenuDto> menus) {
		WebUtils.setSessionAttribute(getCurrentHttpReuqest(), SESSION_USER_MENUS, menus);
	}
	
	public static List<MenuDto> getSessionUserMenus(HttpServletRequest request){
		return (List<MenuDto>) WebUtils.getSessionAttribute(request, SESSION_USER_MENUS);
	}
	
	public static SessionPermission getSessionPermission(HttpServletRequest request) {
		return (SessionPermission) WebUtils.getSessionAttribute(request, SESSION_USER_PERMISSION);
	}

	public static void setSessionPermission(HttpServletRequest request, SessionPermission sessionPermission) {
		WebUtils.setSessionAttribute(request, SESSION_USER_PERMISSION, sessionPermission);
	}
	
	public static void invalidate(HttpServletRequest request){
		setSessionUser(request, null);
		setSessionPermission(request, null);
		setSessionUserMenus(null);
	}
	class SessionPermission{
		
	}
}