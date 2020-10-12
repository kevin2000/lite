package com.lite.core.mapper;

import com.lite.core.entity.SysMenu;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 菜单管理 Mapper 接口
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
	@Select("select * from sys_menu where id in (select menu_id from sys_role_menu where role_id = #roleId)")
	public List<SysMenu> listByRoleId(Long roleId);
}
