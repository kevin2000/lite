package com.lite.system.controller;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.lite.core.utils.ResponseData;
import com.lite.system.entity.SysUser;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@RestController
public class SysUserController {
	
	@RequiresPermissions("user:list")
	@RequestMapping(value = "/user/getCurrUser")
	@ResponseBody
	public ResponseData<SysUser> getCurrentUser(){
		 Object ojb = SecurityUtils.getSubject().getPrincipal();
		return null;
	}
}

