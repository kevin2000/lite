package com.lite.system.service.impl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.core.constant.Constants;
import com.lite.core.domain.dto.LoginUser;
import com.lite.core.redis.RedisCache;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.Util;
import com.lite.core.utils.auth.Md5Utils;
import com.lite.system.entity.SysUser;
import com.lite.system.mapper.SysRoleMenuMapper;
import com.lite.system.mapper.SysUserMapper;
import com.lite.system.mapper.SysUserRoleMapper;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> /* implements ISysUserService */ {
	@Autowired
	private RedisCache redisCache;
	@Autowired
	private SysUserRoleMapper userRoleMapper;
	@Autowired
	private SysRoleMenuMapper roleMenuMapper;
	

	public ResponseData<LoginUser> login(String username, String password) {
		QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>();
		queryWrapper.lambda().eq(SysUser::getUserName, username);

		//check user status
		SysUser user = getOne(queryWrapper);
		if (null == user)
			return ResponseData.getFailure("The account or password was wrong");
		if (!"0".equals(user.getStatus()) || !"0".equals(user.getDelFlag())) 
			return ResponseData.getFailure("The account had been frozen");
		//check password
		if (!user.getPassword().equals(encryptPassword(username, password)))
			return ResponseData.getFailure("The account or password was wrong");
		
		// create and cache token
		String token = Util.getGuid32();
		redisCache.setCacheObject(Constants.LOGIN_TOKEN_KEY + token, user, 11111 / 1000,
				TimeUnit.SECONDS);

		LoginUser loginUser = new LoginUser();
		loginUser.setToken(token);
		loginUser.setUsername(user.getUserName());
		return ResponseData.getSuccess(loginUser);
	}
	
	/**
     * 注册
     */
    public ResponseData<LoginUser> register(SysUser user) {
    	//check length
    	return null;
    }
	
	private String encryptPassword(String username, String password) {
		return Md5Utils.hash(username + password);
	}

	public List<String> getRoles(String userId) {
		return userRoleMapper.getRolesByUserId(userId);
	}
	
	public List<String> getMenus(String userId){
		return roleMenuMapper.getMenusByUserId(userId);
	}

}
