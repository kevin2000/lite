package com.lite.core.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.core.auth.PermissionUtil;
import com.lite.core.config.Config;
import com.lite.core.entity.SysApi;
import com.lite.core.entity.SysMenuApi;
import com.lite.core.entity.SysRoleApi;
import com.lite.core.entity.SysRoleMenu;
import com.lite.core.mapper.SysApiMapper;
import com.lite.core.utils.spring.SpringUtil;

/**
 * init all apis -> clear rolePerm, menuPerm of unavailable apis
 * associate apis and menu -> clear roleMenu of menu, associate menu and role 
 * associate perm and role -> clear roleMenu of role, associate menu and role
 * @author joe
 * @since 2020-10-05
 */
@Service
public class SysApiService extends ServiceImpl<SysApiMapper, SysApi> {
	@Autowired
	private Config config;
	@Autowired
	private SysRoleApiService rolePermService;
	@Autowired
	private SysMenuApiService menuPermService;
	@Autowired
	private SysRoleMenuService roleMenuService;
	
	public SysApi getByUrlAndMethod(String url, String method) {
		return getOne(Wrappers.<SysApi>lambdaQuery().eq(SysApi::getUrl, url).eq(SysApi::getMethod, method));
	}

	/**
	 * init all apis, clear rolePerm, menuPerm of unavailable apis, remove unavialbe apis
	 */
	public void initAllPerms() {
		List<SysApi> apis = PermissionUtil.listPermission(SpringUtil.getApplicationContext(), config.getBasePackage());

		LocalDateTime now = LocalDateTime.now();
		for (SysApi perm : apis) {
			SysApi oldPerm = getByUrlAndMethod(perm.getUrl(), perm.getMethod());
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
		apis = list(new QueryWrapper<SysApi>().lambda().lt(SysApi::getUpdateTime, now).select(SysApi::getId));
		if (!apis.isEmpty()) {
			List<Long> apiIds = new ArrayList<>();
			QueryWrapper<SysRoleApi> queryRolePerm = new QueryWrapper<>();
			QueryWrapper<SysMenuApi> queryMenuPerm = new QueryWrapper<>();
			for (SysApi perm : apis) {
				apiIds.add(perm.getId());
				if (apiIds.size() > 20) {
					queryRolePerm.lambda().in(SysRoleApi::getApiId, apiIds);
					rolePermService.remove(queryRolePerm);
					
					queryMenuPerm.lambda().in(SysMenuApi::getApiId, apiIds);
					menuPermService.remove(queryMenuPerm);
					
					removeByIds(apiIds);
					apiIds.clear();
				}
			}
			if (!apiIds.isEmpty()) {
				queryRolePerm.lambda().in(SysRoleApi::getApiId, apiIds);
				rolePermService.remove(queryRolePerm);
				
				queryMenuPerm.lambda().in(SysMenuApi::getApiId, apiIds);
				menuPermService.remove(queryMenuPerm);
				
				removeByIds(apiIds);
			}
		}
	}

	/**
	 * 
	 * @param permGroup
	 * @param menuId
	 */
	@Transactional
	public void associateMenuAndApi(Long menuId, List<Long> apiIds) {
		if (CollectionUtils.isNotEmpty(apiIds)) {
			List<SysMenuApi> oldMenuPerms = menuPermService.listByMenuId(menuId);
			if (CollectionUtils.isNotEmpty(oldMenuPerms)) {
				List<Long> invalidOldapiIds = new ArrayList<>();
				List<Long> existsapiIds = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(oldMenuPerms)) {
					for(SysMenuApi menuPerm : oldMenuPerms) {
						if (!apiIds.contains(menuPerm.getApiId())) {
							invalidOldapiIds.add(menuPerm.getApiId());
						} else {
							existsapiIds.add(menuPerm.getApiId());
						}
					}
				}
				
				//remove invalid role-menu, menu-perm
				if (!invalidOldapiIds.isEmpty()) {
					menuPermService.remove(menuPermService.lambdaQuery().eq(SysMenuApi::getMenuId, menuId).in(SysMenuApi::getApiId, invalidOldapiIds));
					List<SysRoleApi> rolePerms = rolePermService.list(rolePermService.lambdaQuery().in(SysRoleApi::getApiId, invalidOldapiIds));
					if (!rolePerms.isEmpty()) {
						List<Long> oldRoleIds = rolePerms.stream().map(f -> f.getRoleId()).collect(Collectors.toList());
						roleMenuService.remove(roleMenuService.lambdaQuery().eq(SysRoleMenu::getMenuId, menuId).in(SysRoleMenu::getRoleId, oldRoleIds));
					}
					
				}
				//create new menu-perm
				for (Long apiId : apiIds) {
					if (!existsapiIds.contains(apiId)) {
						SysMenuApi menuPerm = new SysMenuApi();
						menuPerm.setMenuId(menuId);
						menuPerm.setApiId(apiId);
						menuPermService.save(menuPerm);
					}
				}
			} 

			//re valid relation between role and menu
			List<SysRoleApi> rolePerms = rolePermService.list(rolePermService.lambdaQuery().in(SysRoleApi::getApiId, apiIds));
			for (SysRoleApi rolePerm : rolePerms) {
				if (isEligibleRoleAndMenu(rolePerm.getRoleId(), menuId)) {
					SysRoleMenu roleMenu = new SysRoleMenu();
					roleMenu.setRoleId(rolePerm.getRoleId());
					roleMenu.setMenuId(menuId);
					roleMenuService.save(roleMenu);
				}
			}
		} else {
			roleMenuService.remove(roleMenuService.lambdaQuery().eq(SysRoleMenu::getMenuId, menuId));
			menuPermService.remove(menuPermService.lambdaQuery().eq(SysMenuApi::getMenuId, menuId));
		}
		
		
	}
	
	/**
	 * 
	 * @param apiId
	 * @param roleIdId
	 */
	@Transactional
	public void associateApiAndRole(Long apiId, List<Long> roleIds) {
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
		List<SysRoleApi> oldRolePerms = rolePermService.listByapiId(apiId);
		List<SysMenuApi> menuPerms = menuPermService.listByApiId(apiId);
		List<Long> oldRoleIds = null;
		List<Long> menuIds = null;
		if (CollectionUtils.isNotEmpty(menuPerms)) {
			menuIds = menuPerms.stream().map(f -> f.getMenuId()).collect(Collectors.toList());
		}
		if (CollectionUtils.isNotEmpty(oldRolePerms)) {
			oldRoleIds = oldRolePerms.stream().map(f -> f.getRoleId()).collect(Collectors.toList());
			if (null != menuIds) {
				for (SysRoleApi oldRolePerm : oldRolePerms) {
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
					SysRoleApi rolePerm = new SysRoleApi();
					rolePerm.setApiId(apiId);
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
			rolePermService.remove(rolePermService.lambdaQuery().eq(SysRoleApi::getApiId, apiId));
		}
	}
 
	public boolean isEligibleRoleAndMenu(Long roleId, Long menuId) {
		List<SysMenuApi> menuPerms = menuPermService.list(menuPermService.lambdaQuery().eq(SysMenuApi::getMenuId, menuId));
		if (CollectionUtils.isNotEmpty(menuPerms)) {
			int count = rolePermService.count(rolePermService.lambdaQuery().eq(SysRoleApi::getRoleId, roleId).in(SysRoleApi::getApiId, menuPerms.stream().map(f -> f.getApiId()).collect(Collectors.toList())));
			if (count == menuPerms.size())
				return true;
			else
				return false;
		} else {
			return false;
		}
	}
	
}
