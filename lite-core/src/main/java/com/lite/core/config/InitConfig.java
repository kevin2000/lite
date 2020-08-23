package com.lite.core.config;

import java.io.IOException;
import java.util.TimeZone;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.lite.core.filter.NonSSOUserFilter;
import com.lite.core.filter.UserFilter;

@Component
public class InitConfig{

	@Autowired
	private Config config;
	@Autowired 
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	@Value("${com.eb.core.api.excludeUrls:}")
	private String excludeUrlsForApi;
	@Value("${com.eb.core.user.excludeUrls:}")
	private String excludeUrlsForUser; 
	
	/*
	 * @Bean public FilterRegistrationBean<AccessFilter> accessFilterRegistration()
	 * { FilterRegistrationBean<AccessFilter> registration = new
	 * FilterRegistrationBean<AccessFilter>(new AccessFilter());
	 * registration.addUrlPatterns("/*"); registration.setName("accessFilter");
	 * registration.addInitParameter("excludes",
	 * "/lib/**,/css/**,/js/**,/images/**,/fonts/**," + "/," +
	 * "/user/signup,/favicon.ico"); return registration; }
	 */
	
	@Bean
	public FilterRegistrationBean<UserFilter> userFilterRegistration() {
		FilterRegistrationBean<UserFilter> registration = null;
		if (config.getAllowUserFilter()) {
			/*
			 * if (config.getAllowSsoFilter()) registration = new
			 * FilterRegistrationBean<UserFilter>(new SSOUserFilter()); else
			 */
				registration = new FilterRegistrationBean<UserFilter>(new NonSSOUserFilter());
			registration.addUrlPatterns("/*");			
			registration.addInitParameter("excludes",
					"/lib/**,/css/**,/js/**,/images/**,/fonts/**,/api/**,/user/signup,/error/**,/user/info,/user/userChangePwd,/user/changePwd,/user/userLogin/**,/user/login/**,/user/logout/**,/user/signup/**,/user/orgSelector/**,/favicon.ico"
					+ (StringUtils.isNotBlank(excludeUrlsForUser) ? ("," + excludeUrlsForUser) : ""));
		} else {
			registration = new FilterRegistrationBean<UserFilter>(new UserFilter() {				
				@Override
				public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
						throws IOException, ServletException, Exception {
					//do nothing		
				}
			});
			registration.addUrlPatterns("/test/test");;
		}
		registration.setName("userFilter");
		// registration.setOrder(1);
		return registration;
	}
	
	/*
	 * @Bean public FilterRegistrationBean<ApiServerFilter> apiFilterRegistration()
	 * { FilterRegistrationBean<ApiServerFilter> registration = new
	 * FilterRegistrationBean<ApiServerFilter>(new ApiServerFilter()); if
	 * (config.getAllowApiFilter()) { registration.addUrlPatterns("/api/*"); } else
	 * registration.addUrlPatterns("/api/test/*");
	 * registration.setName("apiServerFilter");
	 * registration.addInitParameter("excludes",
	 * "/api/login/**,/api/user/login/**,/api/app/login,/api/app/getToken" +
	 * (StringUtils.isNotBlank(excludeUrlsForApi) ? ("," + excludeUrlsForApi) :
	 * "")); // registration.setOrder(1); return registration; }
	 */
	
	/*
	 * @PostConstruct void setDefaultTimeZone() {
	 * //TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	 * TimeZone.setDefault(TimeZone.getTimeZone(config.getUtilDefaultTimeZoneId()));
	 * //"Asia/Kolkata" }
	 */
	
	/**
	 * trim all white place from parameters
	 */
	@PostConstruct
	public void addConversionConfig(){
		ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) requestMappingHandlerAdapter.getWebBindingInitializer();
		if(initializer.getConversionService() != null){
			GenericConversionService genericConversionService =(GenericConversionService) initializer.getConversionService();
			genericConversionService.addConverter(new ParamTrimConverter());
		}
	}

}
