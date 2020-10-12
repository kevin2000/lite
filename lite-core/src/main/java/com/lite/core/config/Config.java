package com.lite.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class Config {
	/********************Util********************/

	/********************Log********************/
	
	/********************Job********************/
	
	/********************filters********************/
	@Value("${com.eb.core.user.allow.userFilter:false}")
	private Boolean allowUserFilter;
	//Single Sign On
	@Value("${com.eb.core.user.allow.ssoFilter:false}")
	private Boolean allowSsoFilter;
	@Value("${com.eb.core.user.allow.apiFilter:false}")
	private Boolean allowApiFilter;
	
	@Value("${com.eb.core.navBar.quickSearchUrl:}")
	private String navBarQuickSearchUrl;	
	@Value("${com.eb.core.navBar.quickSearchPlaceHolder:}")
	private String navBarQuickSearchPlaceholder;

	@Value("${com.eb.core.mongodb.extraUris:}")
	private String extraUris;
	
	@Value("${com.lite.core.basePackage:com.lite.core}")
	private String basePackage;
}
