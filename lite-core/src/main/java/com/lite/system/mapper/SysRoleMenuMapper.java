package com.lite.system.mapper;

import com.lite.system.entity.SysRoleMenu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {
	@Select("select a.menu_id from sys_role_menu a " + 
			"		   		join sys_user_role c on a.role_id = c.role_id " + 
			"		   		where c.user_id = #{userId}")
	List<String> getMenusByUserId(@Param("userId") String userId);
}
