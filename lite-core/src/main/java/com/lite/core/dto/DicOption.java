package com.lite.core.dto;

import lombok.Data;

@Data
public class DicOption {
	private String id;
	private String dicCode;
	private String code;//man,woman
	private String name;//Man,Woman
	private String status;//enabled, paused
	private Integer sn;
	private String remark;
}
