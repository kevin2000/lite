package com.lite.core.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.lite.core.auth.AuthService;
import com.lite.core.auth.SessionUtil;
import com.lite.core.log.Logger;
import com.lite.core.utils.ResponseCode;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.Util;
import com.lite.core.utils.spring.SpringUtil;
import com.lite.system.entity.SysUser;
import com.lite.system.service.impl.SysUserService;

/**
 * 单点登录权限系统Filter基类
 * 
 * @author Joe
 */
@Component
public abstract class UserFilter implements Filter {
	private Logger logger = new Logger(UserFilter.class);

	private ILoginSuccessHandler loginSuccessHandler;
	private AuthService authService;
	//private IApiValidator apiValidator = null;
	
	public List<String> excludes = null;	
	protected PathMatcher pathMatcher = null;

	protected ILoginSuccessHandler getLoginSuccessHandler() {
		if (null == loginSuccessHandler)
			loginSuccessHandler = SpringUtil.getBean(ILoginSuccessHandler.class);
		return loginSuccessHandler;
	}
	
	protected AuthService getUserService() {
		if (null == authService)
			authService = SpringUtil.getBean(AuthService.class);
		return authService;
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String strExcludes = filterConfig.getInitParameter("excludes");
		if (StringUtils.isNotBlank(strExcludes)) {
			excludes = Arrays.asList(strExcludes.split(","));
			pathMatcher = new AntPathMatcher();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		if (FilterService.matchExcludePath(httpRequest.getServletPath(), excludes, pathMatcher))
			chain.doFilter(request, response);
		else {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			try {
				String appCode = httpRequest.getHeader("appCode");
				if (StringUtils.isNotBlank(appCode)) {
					//ResponseData<String> result = validForExteranlRequest(httpRequest);
					//if (!result.isSuccess()) {
						//processAuthFailed(httpResponse, result);
						return;
					//}

				} 
				doFilter(httpRequest, httpResponse, chain);
				
			}
			catch (Exception e) {
				logger.error(e.getMessage(), e.getCause());
				redirectError(httpRequest, httpResponse, ResponseData.getFailure(e.getMessage()));
					//throw new IllegalArgumentException(e.getMessage());//will catch by ErrController
			}
		}
	}
	
	protected void responseForAjax(HttpServletResponse httpResponse, ResponseData<String> msg){
		httpResponse.setContentType("application/json;charset=UTF-8");
		httpResponse.setStatus(HttpStatus.OK.value());
		PrintWriter writer;
		try {
			writer = httpResponse.getWriter();
			writer.write(Util.objToJsonStr(msg));
			//writer.write(JSON.toJSONString(Result.create(e.getCode()).setMessage(e.getMessage())));
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error("Error to responseForAjax " + msg, e);
		}
	}

	public abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException, Exception;
	
	protected boolean isAjaxRequest(HttpServletRequest request) {
		String requestedWith = request.getHeader("X-Requested-With");
		return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
	}
	
	@Override
	public void destroy() {
	}

	/**
	 * redirect to Error url and containd reason
	 * @throws IOException 
	 */
	protected void redirectError(HttpServletRequest request, HttpServletResponse response, ResponseData<String> errorMsg) throws IOException{
		if (isAjaxRequest(request)) {
			responseForAjax(response, errorMsg);
		} else {
			String redirectUrl = "/error/info?error=" + errorMsg.getMsg();
			response.sendRedirect(redirectUrl);
		}
	}
	
	/**
	 * redirect to login url
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void redirectLogin(HttpServletRequest request, HttpServletResponse response, String redirectUrl) throws IOException, Exception {
		if (isAjaxRequest(request)) {
			responseForAjax(response, ResponseData.getFailure(ResponseCode.USER_NOT_LOGGED_IN));
			//throw new Exception("Login please");
		}
		else {
			SessionUtil.invalidate(request);
			response.sendRedirect(redirectUrl);
		}
	}
	
	protected boolean validLogin() {
		SysUser currUser = getLoginSuccessHandler().getCurrUser();
		if (null == currUser) {
			currUser = getUserService().getCurrUser();
		}
		if (null == currUser)
			return false;
		return true;
	}
	
	/**
	 * if the path can be match and user has the authority then return true, else return false
	 */
	protected boolean validAuthority(String path){
		return getUserService().validAuthority(getUserService().getCurrUser(), path);
	}

	
	/**
	 * process for third party app's request
	 * @param req
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public ResponseData<String> validForExteranlRequest(HttpServletRequest httpRequest) throws Exception {
		String appCode = httpRequest.getHeader("appCode");//will be used to valid the secret, e.g. if ("dingding").equals(appCode) then valid by "11A771550D5841598f3097e0d04da6c7"
		String appToken = httpRequest.getHeader("appToken");
		String userToken = httpRequest.getHeader("userToken");
		String nonce = httpRequest.getHeader("nonce");
		String timespan = httpRequest.getHeader("timespan");
		String signature = httpRequest.getHeader("signature");
		
		/*
		 * if (null == apiValidator) apiValidator =
		 * SpringUtil.getBean(ApiValidator.class); ResponseData<String> result =
		 * apiValidator.validExternalRequest(appCode, appToken, userToken, nonce,
		 * timespan, signature); if (!result.isSuccess()) { return
		 * ResponseData.getFailure(result.getCode(), result.getMsg(), result.getData());
		 * } else {
		 */
			return ResponseData.getSuccess();
		//}
	}
	

	public void processAuthFailed(ServletResponse response, ResponseData<String> msg) throws UnsupportedEncodingException, IOException{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getOutputStream().write(Util.objToJsonStr(msg).getBytes("UTF-8"));
	}

}