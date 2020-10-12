package com.lite.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.core.utils.tree.BuildTree;
import com.lite.core.utils.tree.Tree;
import com.lite.core.entity.SysMenu;
import com.lite.core.mapper.SysMenuMapper;

/**
 * <p>
 * 菜单管理 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
@Service
public class SysMenuService extends ServiceImpl<SysMenuMapper, SysMenu> {
	@Autowired
	SysRoleMenuService roleMenuService;
	@Autowired
	SysMenuMapper menuMapper;

	public List<Tree<SysMenu>> listMenuTree(Long id) {
		List<Tree<SysMenu>> trees = new ArrayList<Tree<SysMenu>>();
		List<SysMenu> SysMenus = list();
		for (SysMenu sysSysMenu : SysMenus) {
			Tree<SysMenu> tree = new Tree<SysMenu>();
			tree.setId(sysSysMenu.getMenuId().toString());
			tree.setParentId(sysSysMenu.getParentId().toString());
			tree.setText(sysSysMenu.getName());
			Map<String, Object> attributes = new HashMap<>(16);
			attributes.put("url", sysSysMenu.getUrl());
			attributes.put("icon", sysSysMenu.getIcon());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		List<Tree<SysMenu>> list = BuildTree.buildList(trees, "0");
		return list;
	}
	
	/**
	 * @param
	 * @return 树形菜单
	 */
	@Cacheable
	public Tree<SysMenu> getSysMenuTree(Long roleId) {
		List<Tree<SysMenu>> trees = new ArrayList<Tree<SysMenu>>();
		List<SysMenu> SysMenus = menuMapper.listByRoleId(roleId);
		for (SysMenu sysSysMenu : SysMenus) {
			Tree<SysMenu> tree = new Tree<SysMenu>();
			tree.setId(sysSysMenu.getMenuId().toString());
			tree.setParentId(sysSysMenu.getParentId().toString());
			tree.setText(sysSysMenu.getName());
			Map<String, Object> attributes = new HashMap<>(16);
			attributes.put("url", sysSysMenu.getUrl());
			attributes.put("icon", sysSysMenu.getIcon());
			tree.setAttributes(attributes);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<SysMenu> t = BuildTree.build(trees);
		return t;
	}

	/*
	 * public List<SysMenu> list(Map<String, Object> params) { List<SysMenu> menus =
	 * menuMapper.selectByMap(params); return menus; }
	 */

	public Tree<SysMenu> getTree() {
		List<Tree<SysMenu>> trees = new ArrayList<Tree<SysMenu>>();
		List<SysMenu> SysMenus = listByMap(new HashMap<>(16));
		for (SysMenu sysSysMenu : SysMenus) {
			Tree<SysMenu> tree = new Tree<SysMenu>();
			tree.setId(sysSysMenu.getMenuId().toString());
			tree.setParentId(sysSysMenu.getParentId().toString());
			tree.setText(sysSysMenu.getName());
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<SysMenu> t = BuildTree.build(trees);
		return t;
	}

	public Tree<SysMenu> getTree(Long roleId) {
		// 根据roleId查询权限
		List<SysMenu> menus = listByMap(new HashMap<String, Object>(16));
		List<Long> menuIds = roleMenuService.listMenuIdsByRoleId(roleId);
		List<Long> temp = menuIds;
		for (SysMenu menu : menus) {
			if (temp.contains(menu.getParentId())) {
				menuIds.remove(menu.getParentId());
			}
		}
		List<Tree<SysMenu>> trees = new ArrayList<Tree<SysMenu>>();
		List<SysMenu> SysMenus = listByMap(new HashMap<String, Object>(16));
		for (SysMenu sysSysMenu : SysMenus) {
			Tree<SysMenu> tree = new Tree<SysMenu>();
			tree.setId(sysSysMenu.getMenuId().toString());
			tree.setParentId(sysSysMenu.getParentId().toString());
			tree.setText(sysSysMenu.getName());
			Map<String, Object> state = new HashMap<>(16);
			Long menuId = sysSysMenu.getMenuId();
			if (menuIds.contains(menuId)) {
				state.put("selected", true);
			} else {
				state.put("selected", false);
			}
			tree.setState(state);
			trees.add(tree);
		}
		// 默认顶级菜单为０，根据数据库实际情况调整
		Tree<SysMenu> t = BuildTree.build(trees);
		return t;
	}
}
