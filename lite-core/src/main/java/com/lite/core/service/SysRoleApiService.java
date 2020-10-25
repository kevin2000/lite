package com.lite.core.service;

import com.lite.core.entity.SysRoleApi;
import com.lite.core.mapper.SysRoleApiMapper;
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
public class SysRoleApiService extends ServiceImpl<SysRoleApiMapper, SysRoleApi> {
	public List<SysRoleApi> listByapiId(Long apiId){
		return list(new QueryWrapper<SysRoleApi>().lambda().eq(SysRoleApi::getApiId, apiId));
	}
}
