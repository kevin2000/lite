package com.lite.core.config;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

/**
 * trim all paramters
 *
 */
public class ParamTrimConverter implements Converter<String, String>{

	@Override
	public String convert(String source) {
		if(source != null) {
			return source.replaceFirst("^[ \t\r\n]+", "").replaceFirst("[ \t\r\n]+$", "");
		}
		return null;
	}

}