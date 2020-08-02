package com.lite.core.utils;

/**
 * Response code for restful api
 *
 */
public enum ResponseCode {
	/* success*/
    SUCCESS(0, "Success"),

    /* invalid authen：10001-19999 */
    APP_TOKEN_INVALID(10001, "Invalid app token"),
    USER_NOT_LOGGED_IN(10002, "User Not Logged In"),
    APP_NOT_REGISTER(10011, "The app is not registered"),
    APP_ACCOUNT_INVALID(10012, "Invalid app account"),
    PERMISSION_DENIED(10013, "Permission denied"),
    USER_TOKEN_INVALID(10014, "Invalid user token"),
    SIGNATURE_INVALID(10015, "Invalid signature"),
    
    USER_ACCOUNT_ERROR(10003, "no account or password error"),
    USER_ACCOUNT_FORBIDDEN(10004, "the account has been disabled"),
    USER_NOT_EXIST(10005, "no account"),
    USER_HAS_EXISTED(10006, "accoutnt already exists"),
    
    /* invalid param：20001-29999 */
    PARAM_INVALID(20001, "Invalid Param"),
    IDEMPOTENT_PROCESSING(20002, "Duplicate request"),
    IDEMPOTENT_PARAM_MISSING(20003, "No idempotent param"),
    IDEMPOTENT_INVALID(20004, "Invalid idempotent"),
    /*PARAM_IS_BLANK(10002, "param is null or empty"),
    PARAM_TYPE_BIND_ERROR(10003, "param type error"),
    PARAM_NOT_COMPLETE(10004, "param missing"),*/

    /* other error：30001-39999 */
	SYSTEM_ERROR(30001, "System Error");

	
	private Integer code;
	private String msg;
	
	ResponseCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
	
	public Integer code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    public static String getMessage(String name) {
        for (ResponseCode item : ResponseCode.values()) {
            if (item.name().equals(name)) {
                return item.msg;
            }
        }
        return name;
    }

    public static Integer getCode(String name) {
        for (ResponseCode item : ResponseCode.values()) {
            if (item.name().equals(name)) {
                return item.code;
            }
        }
        return null;
    }
    
    /*//check repeat code
    public static void main(String[] args) {
    	ResponseCode[] ApiResultCodes = ResponseCode.values();
        List<Integer> codeList = new ArrayList<Integer>();
        for (ResponseCode ApiResultCode : ApiResultCodes) {
            if (codeList.contains(ApiResultCode.code)) {
                System.out.println(ApiResultCode.code);
            } else {
                codeList.add(ApiResultCode.code());
            }
        }
    }*/
}
