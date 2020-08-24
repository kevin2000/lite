package com.lite.core.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lite.core.utils.ResponseCode;
import com.lite.core.utils.ResponseData;


/**
 * non single sign on filter
 * Description 
 * @author qiao.li@elephantboat.in
 * @date 2019-06-15
 *
 */
public class NonSSOUserFilter extends UserFilter {

	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException, Exception {
		if (validLogin()) {
			if (validAuthority(request.getServletPath()))
				chain.doFilter(request, response);
			else
				redirectError(request, response, ResponseData.getFailure(ResponseCode.PERMISSION_DENIED));
		} else {
			String currUrl = request.getRequestURL().toString();
			if (request.getQueryString() != null){
	            currUrl += "?" + request.getQueryString();
	        }
			try {
				currUrl = java.net.URLEncoder.encode(currUrl, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			redirectLogin(request, response, "/login?backUrl=" + currUrl);
		}
	}

}
