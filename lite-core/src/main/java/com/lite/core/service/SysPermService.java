package com.lite.core.service;

import com.lite.core.auth.PermissionUtil;
import com.lite.core.config.Config;
import com.lite.core.entity.SysMenuPerm;
import com.lite.core.entity.SysPerm;
import com.lite.core.entity.SysRolePerm;
import com.lite.core.mapper.SysPermMapper;
import com.lite.core.utils.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * init all perms -> clear rolePerm, menuPerm of unavailable perms
 * associate perms and menu -> clear roleMenu of menu, associate menu and role 
 * associate perm and role -> clear roleMenu of role, associate menu and role
 * @author joe
 * @since 2020-10-05
 */
@Service
public class SysPermService extends ServiceImpl<SysPermMapper, SysPerm> {
	@Autowired
	private Config config;
	@Autowired
	private SysRolePermService rolePermService;
	@Autowired
	private SysMenuPermService menuPermService;
	@Autowired
	private SysRoleMenuService roleMenuService;
	
	public SysPerm getByUrlAndMethod(String url, String method) {
		return getOne(Wrappers.<SysPerm>lambdaQuery().eq(SysPerm::getUrl, url).eq(SysPerm::getMethod, method));
	}

	/**
	 * init all perms, clear rolePerm, menuPerm of unavailable perms, remove unavialbe perms
	 */
	public void initAllPerms() {
		List<SysPerm> perms = PermissionUtil.listPermission(SpringUtil.getApplicationContext(), config.getBasePackage());

		LocalDateTime now = LocalDateTime.now();
		for (SysPerm perm : perms) {
			SysPerm oldPerm = getByUrlAndMethod(perm.getUrl(), perm.getMethod());
			if (null != oldPerm) {
				oldPerm.setUpdateTime(LocalDateTime.now());
				updateById(oldPerm);
			} else {
				perm.setCreateTime(LocalDateTime.now());
				perm.setUpdateTime(perm.getCreateTime());
				save(perm);
			}
		}
		
		//clear rolePerm, menuPerm, perm
		perms = list(new QueryWrapper<SysPerm>().lambda().lt(SysPerm::getUpdateTime, now).select(SysPerm::getId));
		if (!perms.isEmpty()) {
			List<Long> permIds = new ArrayList<>();
			QueryWrapper<SysRolePerm> queryRolePerm = new QueryWrapper<>();
			QueryWrapper<SysMenuPerm> queryMenuPerm = new QueryWrapper<>();
			for (SysPerm perm : perms) {
				permIds.add(perm.getId());
				if (permIds.size() > 20) {
					queryRolePerm.lambda().in(SysRolePerm::getPermId, permIds);
					rolePermService.remove(queryRolePerm);
					
					queryMenuPerm.lambda().in(SysMenuPerm::getPermId, permIds);
					menuPermService.remove(queryMenuPerm);
					
					removeByIds(permIds);
					permIds.clear();
				}
			}
			if (!permIds.isEmpty()) {
				queryRolePerm.lambda().in(SysRolePerm::getPermId, permIds);
				rolePermService.remove(queryRolePerm);
				
				queryMenuPerm.lambda().in(SysMenuPerm::getPermId, permIds);
				menuPermService.remove(queryMenuPerm);
				
				removeByIds(permIds);
			}
		}
	}

	/**
	 * 
	 * @param permGroup
	 * @param menuId
	 */
	public void associatePermGroupAndMenu(List<String> permGroup, String menuId) {
		
	}
	
	/**
	 * 
	 * @param permId
	 * @param roleIdId
	 */
	public void associatePermAndRole(String permId, String roleIdId) {
		List<SysMenuPerm> mernuPerms = menuPermService.list(new QueryWrapper<SysMenuPerm>().lambda().eq(SysMenuPerm::getPermId, permId));
		
	}
}
