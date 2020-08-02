package com.lite.core.constant;

/**
 * Shiro通用常量
 * 
 * @author ruoyi
 */
public class ShiroConstants
{
    /**
     * 当前登录的用户
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     * 用户名
     */
    public static final String CURRENT_USERNAME = "username";

    /**
     * 消息key
     */
    public static final String MESSAGE = "message";

    /**
     * 错误key
     */
    public static final String ERROR = "errorMsg";

    /**
     * 编码格式
     */
    public static final String ENCODING = "UTF-8";

    /**
     * 当前在线会话
     */
    public static final String ONLINE_SESSION = "online_session";

    /**
     * 验证码key
     */
    public static final String CURRENT_CAPTCHA = "captcha";

    /**
     * 验证码开关
     */
    public static final String CURRENT_ENABLED = "captchaEnabled";

    /**
     * 验证码类型
     */
    public static final String CURRENT_TYPE = "captchaType";

    /**
     * 验证码
     */
    public static final String CURRENT_VALIDATECODE = "validateCode";

    /**
     * 验证码错误
     */
    public static final String CAPTCHA_ERROR = "captchaError";

    /**
     * 登录记录缓存
     */
    public static final String LOGINRECORDCACHE = "loginRecordCache";

    /**
     * 系统活跃用户缓存
     */
    public static final String SYS_USERCACHE = "sys-userCache";
    
    /**
     * ACCESS_TOKEN
     */
    public static final String ACCESS_TOKEN = "token";
    
    /**
     * 登录用户令牌缓存KEY前缀
     */
    public static final int TOKEN_EXPIRE_TIME = 3600; //3600秒即是一小时

    public static final String PREFIX_USER_TOKEN = "PREFIX_USER_TOKEN_";

    /**
     * 0：一级菜单
     */
    public static final Integer MENU_TYPE_0 = 0;

    /**
     * 1：子菜单
     */
    public static final Integer MENU_TYPE_1 = 1;

    /**
     * 2：按钮权限
     */
    public static final Integer MENU_TYPE_2 = 2;

    /**
     * 是否用户已被冻结 1(解冻)正常 2冻结
     */
    public static final Integer USER_UNFREEZE = 1;

    public static final Integer USER_FREEZE = 2;

    /**
     * 登录用户规则缓存
     */
    public static final String LOGIN_USER_RULES_CACHE = "loginUser_cacheRules";

    /**
     * 登录用户拥有角色缓存KEY前缀
     */
    public static String LOGIN_USER_CACHERULES_ROLE = "loginUser_cacheRules::Roles_";

    /**
     * 登录用户拥有权限缓存KEY前缀
     */
    public static String LOGIN_USER_CACHERULES_PERMISSION = "loginUser_cacheRules::Permissions_";
}
