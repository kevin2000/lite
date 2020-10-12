package com.lite.core.service;

import com.lite.core.entity.SysRole;
import com.lite.core.mapper.SysRoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
@Service
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {
	@Autowired
	SysRoleMapper roleMapper;
	public List<SysRole> listByUserId(long userId){
		return roleMapper.listByUserId(userId);
	}
}
