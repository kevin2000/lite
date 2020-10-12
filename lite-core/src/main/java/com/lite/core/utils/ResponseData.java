package com.lite.core.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * response for restful api
 *
 */
public class ResponseData<T> {
	private Integer code;//response state, 0: success, 1: failure, other: failure
	private String msg;//description for code
	private T data;//response data  
	
	public ResponseData() {}
	public ResponseData(Integer code, T data, String msg) {
		this.setCode(code);
		this.setMsg(msg);
		this.setData(data);
	}
	public ResponseData(ResponseCode responseCode, T data, Integer code) {
		this.setCode(responseCode.code());
		this.setMsg(msg);
		this.setData(data);
	}
	public ResponseData(ResponseCode responseCode, T data) {
		this.setCode(responseCode.code());
		this.setMsg(responseCode.msg());
		this.setData(data);
	}
	
	@JsonIgnore
	public boolean isSuccess() {
		return ResponseCode.SUCCESS.code().equals(code);
	}
	
	/*	success	 */
	public static <T> ResponseData<T> getSuccess() {
		return getSuccess(null, "Success");
	}
	public static <T> ResponseData<T> getSuccess(T data) {
		return getSuccess(data, "Success");
	}
	public static <T> ResponseData<T> getSuccess(T data, String msg) {
		return new ResponseData<T>(0, data, msg);
	} 
	
	
	/*	failure	 */
	public static <T> ResponseData<T> getFailure(ResponseCode code) {
		return getFailure(code, null);
	}	
	public static <T> ResponseData<T> getFailure(ResponseCode code, T data) {
		return new ResponseData<T>(code.code(), data, code.msg()); 
	}
	
	public static <T> ResponseData<T> getFailure() {
		return getFailure("fail");
	}
	
	public static <T> ResponseData<T> getFailure(String msg) {
		return getFailure(msg, null);
	}
	public static <T> ResponseData<T> getFailure(String msg, T data) {
		return getFailure(msg, data, 1);
	}
	public static <T> ResponseData<T> getFailure(String msg, T data, Integer code) {
		return new ResponseData<T>(code, data, msg);
	}
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	
	
}
