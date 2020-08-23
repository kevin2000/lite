package com.lite.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class Config {
	/********************Util********************/

	@Value("${com.eb.core.util.util.defaultDateFormat}")
	private String utilDefaultDateFormat;

	@Value("${com.eb.core.util.util.defaultTimeZone}")
	private String utilDefaultTimeZone;
	
	@Value("${com.eb.core.util.util.defaultTimeZoneId:Asia/Kolkata}")
	private String utilDefaultTimeZoneId;
	
	@Value("${com.eb.core.util.util.defaultDateInputFormat}")
	private String utilDefaultDateInputFormat;

	/********************Log********************/

	@Value("#{'${com.eb.core.log.log.validTypes}'.split(',')}")
	private String[] logValidTypes;
	
	@Value("${com.eb.core.mail.mailFrom}")
	private String sendReviewMailFrom;
	
	/********************Job********************/
	@Value("#{'${com.eb.core.job.job.validStatuses}'.split(',')}")
	private String[] jobValidStatuses;

	@Value("#{'${com.eb.core.job.jobTarget.validStatuses}'.split(',')}")
	private String[] jobTargetValidStatuses;
	
	@Value("${com.eb.core.job.allow.cronjob}")
	private Boolean allowCronjob;
	
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
}
