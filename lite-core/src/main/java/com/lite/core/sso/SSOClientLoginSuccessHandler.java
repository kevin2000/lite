package com.lite.core.sso;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lite.core.auth.SessionUser;
import com.lite.core.auth.SessionUtil;
import com.lite.core.config.Config;
import com.lite.core.filter.ILoginSuccessHandler;
import com.lite.core.utils.ResponseData;
import com.lite.system.entity.SysUser;

@Component
public class SSOClientLoginSuccessHandler implements ILoginSuccessHandler{
	@Autowired
	private Config config;
	
	@Override
	public String onLoginSuccess(HttpServletRequest request, HttpServletResponse response, SysUser currUser) {
		//SessionUtil.setSessionUser(request, new SessionUser(currUser.getUserId(), currUser));
		String backUrl = request.getParameter("backUrl");
    	if (StringUtils.isNotBlank(backUrl)) {
    		return backUrl;
    	}else {//local request    	
	        return "/";
    	}
	}

	@Override
	public SysUser getCurrUser() {
		return SessionUtil.getCurrentUser();
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		SessionUser sessionUser = SessionUtil.getSessionUser(request);
		if (config.getAllowSsoFilter()) {
			String userToken = sessionUser == null ? null : sessionUser.getToken();
			/*
			 * ResponseData<String> result = gatewayApi.userLogOut(userToken); if
			 * (result.isSuccess()) SessionUtil.invalidate(request);
			 */
		} else
			SessionUtil.invalidate(request);
	}
}
