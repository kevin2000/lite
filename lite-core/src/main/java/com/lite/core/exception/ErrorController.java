package com.lite.core.exception;

import java.util.zip.DataFormatException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lite.core.utils.ResponseCode;
import com.lite.core.utils.ResponseData;

@ControllerAdvice
public class ErrorController {
	/**
	 * http请求的方法不正确
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	public ResponseData<String> httpRequestMethodNotSupportedExceptionHandler(
			HttpRequestMethodNotSupportedException e) {
		return ResponseData.getFailure("RequestMethodNotAllowed");
	}

	/**
	 * 请求参数不全
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public ResponseData<String> missingServletRequestParameterExceptionHandler(
			MissingServletRequestParameterException e) {
		return ResponseData.getFailure("MissingServletRequestParameter");
	}

	/**
	 * 请求参数类型不正确
	 */
	@ExceptionHandler(TypeMismatchException.class)
	@ResponseBody
	public ResponseData<String> typeMismatchExceptionHandler(TypeMismatchException e) {
		return ResponseData.getFailure("TypeMismatchException");
	}

	/**
	 * 数据格式不正确
	 */
	@ExceptionHandler(DataFormatException.class)
	@ResponseBody
	public ResponseData<String> dataFormatExceptionHandler(DataFormatException e) {
		return ResponseData.getFailure("DataFormatException");
	}
	/*
	*//**
		 * 用户没找到
		 */

	/*
	 * @ExceptionHandler(AuthenticationException.class)
	 * 
	 * @ResponseBody public ResponseData<String>
	 * userNotFoundExceptionHandler(AuthenticationException e) { return
	 * ResponseData.getFailure("UserNotExist"); }
	 * 
	 *//**
		 * 非法输入
		 *//*
			 * @ExceptionHandler(IllegalArgumentException.class)
			 * 
			 * @ResponseBody public ResponseData<String>
			 * illegalArgumentExceptionHandler(IllegalArgumentException e) { return
			 * ResponseData.getFailure("IllegalArgumentException"); }
			 * 
			 * @ExceptionHandler(UnauthenticatedException.class)
			 * 
			 * @ResponseBody public ResponseData<String>
			 * unauthenticatedExceptionHandler(UnauthenticatedException e){ return
			 * ResponseData.getFailure("please login first"); }
			 * 
			 * @ExceptionHandler(UnauthorizedException.class)
			 * 
			 * @ResponseBody public ResponseData<String>
			 * unauthorizedExceptionHandler(UnauthorizedException e){ return
			 * ResponseData.getFailure("you have not this permission"); }
			 */

	@ExceptionHandler // 处理其他异常
	@ResponseBody
	public ResponseData<String> allExceptionHandler(Exception e) {
		e.printStackTrace();
		return ResponseData.getFailure(ResponseCode.SYSTEM_ERROR);
	}
}
