package com.lite.system.mapper;

import com.lite.system.entity.SysUserRole;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户和角色关联表 Mapper 接口
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {
	@Select("select role_id from sys_user_role where user_id = #{userId}")
	List<String> getRolesByUserId(@Param("userId") String userId);
}
