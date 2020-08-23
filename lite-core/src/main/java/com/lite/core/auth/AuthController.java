package com.lite.core.auth;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lite.core.constant.Constants;
import com.lite.core.domain.dto.LoginUser;
import com.lite.core.redis.RedisCache;
import com.lite.core.utils.ResponseData;
import com.lite.system.entity.SysUser;
import com.lite.system.service.impl.SysUserService;

@RestController
public class AuthController {
	@Autowired
	private SysUserService userService;
	@Autowired
	private RedisCache redisUtil;

	@RequestMapping(value = "/auth/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData<LoginUser> login(@RequestParam("username")String username, @RequestParam("password") String password) throws Exception {
		return userService.login(username, password);
	}

	/**
	 * 退出登录
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout")
	@ResponseBody
	public ResponseData<String> logout(HttpServletRequest request, HttpServletResponse response) {
		// 用户退出逻辑
		String token = request.getHeader(AuthService.token);
		if (StringUtils.isBlank(token)) {
			return ResponseData.getFailure("退出登录失败！");
		}
		/*
		 * SysUser sysUser = sysUserService.getUserByName(username); if (sysUser !=
		 * null) { log.info(" 用户名:  " + sysUser.getRealName() + ",退出成功！ ");
		 */
		// 清空用户Token缓存
		redisUtil.deleteObject(Constants.LOGIN_TOKEN_KEY + token);
		// 清空用户权限缓存：权限Perms和角色集合
		/*
		 * redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_ROLE + username);
		 * redisUtil.del(CommonConstant.LOGIN_USER_CACHERULES_PERMISSION + username);
		 */
		return ResponseData.getSuccess("退出登录成功！");
		/*
		 * } else { return Result.error("无效的token"); }
		 */
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public ResponseData<LoginUser> register(@RequestBody SysUser sysUser) throws Exception {
		return userService.register(sysUser);
		
	}
}
