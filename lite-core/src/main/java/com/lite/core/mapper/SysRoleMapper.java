package com.lite.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lite.core.entity.SysRole;


public interface SysRoleMapper extends BaseMapper<SysRole> {
	@Select("select * from sys_role where id in (select role_id from sys_user_role where user_id = #userId)")
	public List<SysRole> listByUserId(Long userId);
}
