package com.lite.core.service;

import com.lite.core.entity.SysRoleMenu;
import com.lite.core.mapper.SysRoleMenuMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色与菜单对应关系 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
@Service
public class SysRoleMenuService extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> {
	
	public List<Long> listMenuIdsByRoleId(Long roleId){
		List<SysRoleMenu> roleMenus =
				list(new QueryWrapper<SysRoleMenu>().lambda().eq(SysRoleMenu::getRoleId, roleId).select(SysRoleMenu::getMenuId));
		return roleMenus.stream().map(f -> f.getMenuId()).collect(Collectors.toList());
		
	}
	
	public List<Long> listRoleIdsByMenuId(Long menuId){
		List<SysRoleMenu> roleMenus =
				list(new QueryWrapper<SysRoleMenu>().lambda().eq(SysRoleMenu::getMenuId, menuId).select(SysRoleMenu::getRoleId));
		return roleMenus.stream().map(f -> f.getRoleId()).collect(Collectors.toList());
		
	}
}
