package com.lite.core.service;

import com.lite.core.entity.SysRolePerm;
import com.lite.core.mapper.SysRolePermMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-10-05
 */
@Service
public class SysRolePermService extends ServiceImpl<SysRolePermMapper, SysRolePerm> {
	public List<SysRolePerm> listByPermId(String permId){
		return list(new QueryWrapper<SysRolePerm>().lambda().eq(SysRolePerm::getPermId, permId));
	}
}
