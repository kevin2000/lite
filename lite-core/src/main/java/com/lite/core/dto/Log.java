package com.lite.core.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log {
	private String type; // error, debug, warn, info, record, access, audit
	private String klass; // class
	private String method; // jobName, method
	private String user; // system, tom, ...
	private String target; // productId, sampleAsin, ...
	private String message; // errorMsg, dataContent
	private String extra; // stacktrace, ... //TODO may be big, need to manually delete from time to time
	private Date time;
}
