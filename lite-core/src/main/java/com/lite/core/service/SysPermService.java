package com.lite.core.service;

import com.lite.core.auth.PermissionUtil;
import com.lite.core.config.Config;
import com.lite.core.entity.SysMenuPerm;
import com.lite.core.entity.SysPerm;
import com.lite.core.entity.SysRoleMenu;
import com.lite.core.entity.SysRolePerm;
import com.lite.core.mapper.SysPermMapper;
import com.lite.core.utils.spring.SpringUtil;

import cn.hutool.core.collection.CollectionUtil;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional
	public void associatePermGroupAndMenu(Long menuId, List<Long> permIds) {
		if (CollectionUtils.isNotEmpty(permIds)) {
			List<SysMenuPerm> oldMenuPerms = menuPermService.listByMenuId(menuId);
			if (CollectionUtils.isNotEmpty(oldMenuPerms)) {
				List<Long> invalidOldPermIds = new ArrayList<>();
				List<Long> existsPermIds = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(oldMenuPerms)) {
					for(SysMenuPerm menuPerm : oldMenuPerms) {
						if (!permIds.contains(menuPerm.getPermId())) {
							invalidOldPermIds.add(menuPerm.getPermId());
						} else {
							existsPermIds.add(menuPerm.getPermId());
						}
					}
				}
				
				//remove invalid role-menu, menu-perm
				if (!invalidOldPermIds.isEmpty()) {
					menuPermService.remove(menuPermService.lambdaQuery().eq(SysMenuPerm::getMenuId, menuId).in(SysMenuPerm::getPermId, invalidOldPermIds));
					List<SysRolePerm> rolePerms = rolePermService.list(rolePermService.lambdaQuery().in(SysRolePerm::getPermId, invalidOldPermIds));
					if (!rolePerms.isEmpty()) {
						List<Long> oldRoleIds = rolePerms.stream().map(f -> f.getRoleId()).collect(Collectors.toList());
						roleMenuService.remove(roleMenuService.lambdaQuery().eq(SysRoleMenu::getMenuId, menuId).in(SysRoleMenu::getRoleId, oldRoleIds));
					}
					
				}
				//create new menu-perm
				for (Long permId : permIds) {
					if (!existsPermIds.contains(permId)) {
						SysMenuPerm menuPerm = new SysMenuPerm();
						menuPerm.setMenuId(menuId);
						menuPerm.setPermId(permId);
						menuPermService.save(menuPerm);
					}
				}
			} 

			//re valid relation between role and menu
			List<SysRolePerm> rolePerms = rolePermService.list(rolePermService.lambdaQuery().in(SysRolePerm::getPermId, permIds));
			for (SysRolePerm rolePerm : rolePerms) {
				if (isEligibleRoleAndMenu(rolePerm.getRoleId(), menuId)) {
					SysRoleMenu roleMenu = new SysRoleMenu();
					roleMenu.setRoleId(rolePerm.getRoleId());
					roleMenu.setMenuId(menuId);
					roleMenuService.save(roleMenu);
				}
			}
		} else {
			roleMenuService.remove(roleMenuService.lambdaQuery().eq(SysRoleMenu::getMenuId, menuId));
			menuPermService.remove(menuPermService.lambdaQuery().eq(SysMenuPerm::getMenuId, menuId));
		}
		
		
	}
	
	/**
	 * 
	 * @param permId
	 * @param roleIdId
	 */
	@Transactional
	public void associatePermAndRole(Long permId, List<Long> roleIds) {
	  /*perm role
		1	1
		2	1
		3	2
		perm menu
		1	1
		2	2
		3	2
		role menu
		1	1*/
		//remove invalid old role-menu
		List<SysRolePerm> oldRolePerms = rolePermService.listByPermId(permId);
		List<SysMenuPerm> menuPerms = menuPermService.listByPermId(permId);
		List<Long> oldRoleIds = null;
		List<Long> menuIds = null;
		if (CollectionUtils.isNotEmpty(menuPerms)) {
			menuIds = menuPerms.stream().map(f -> f.getMenuId()).collect(Collectors.toList());
		}
		if (CollectionUtils.isNotEmpty(oldRolePerms)) {
			oldRoleIds = oldRolePerms.stream().map(f -> f.getRoleId()).collect(Collectors.toList());
			if (null != menuIds) {
				for (SysRolePerm oldRolePerm : oldRolePerms) {
					if (CollectionUtils.isEmpty(roleIds) || !roleIds.contains(oldRolePerm.getRoleId())) {
						roleMenuService.remove(roleMenuService.lambdaQuery().eq(SysRoleMenu::getRoleId, oldRolePerm.getRoleId()).in(SysRoleMenu::getMenuId, menuIds));
					}
				}
			}
		}
		
		
		if (CollectionUtils.isNotEmpty(roleIds)) {
			for (Long roleId : roleIds) {
				if (null == oldRoleIds || !oldRoleIds.contains(roleId)) {
					//create role-perm and eval new role-menu
					SysRolePerm rolePerm = new SysRolePerm();
					rolePerm.setPermId(permId);
					rolePerm.setRoleId(roleId);
					rolePermService.save(rolePerm);
					
					if (null != menuIds) {
						for (Long menuId : menuIds) {
							if (isEligibleRoleAndMenu(roleId, menuId)) {
								SysRoleMenu roleMenu = new SysRoleMenu();
								roleMenu.setRoleId(roleId);
								roleMenu.setMenuId(menuId);
								roleMenuService.save(roleMenu);
							}
						}
					}
				}
			}
		} else {
			//remove invalid role-perm
			rolePermService.remove(rolePermService.lambdaQuery().eq(SysRolePerm::getPermId, permId));
		}
	}
 
	public boolean isEligibleRoleAndMenu(Long roleId, Long menuId) {
		List<SysMenuPerm> menuPerms = menuPermService.list(menuPermService.lambdaQuery().eq(SysMenuPerm::getMenuId, menuId));
		if (CollectionUtils.isNotEmpty(menuPerms)) {
			int count = rolePermService.count(rolePermService.lambdaQuery().eq(SysRolePerm::getRoleId, roleId).in(SysRolePerm::getPermId, menuPerms.stream().map(f -> f.getPermId()).collect(Collectors.toList())));
			if (count == menuPerms.size())
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
	
}
