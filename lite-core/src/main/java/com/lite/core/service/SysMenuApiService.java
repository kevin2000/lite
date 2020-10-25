package com.lite.core.service;

import com.lite.core.entity.SysMenuApi;
import com.lite.core.mapper.SysMenuApiMapper;
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
public class SysMenuApiService extends ServiceImpl<SysMenuApiMapper, SysMenuApi> {
	public void saveMenuPerm(SysMenuApi menuPerm) {
		//figure out relation between role and menu
		
		save(menuPerm);
	}

	public List<SysMenuApi> listByApiId(Long apiId){
		return list(new QueryWrapper<SysMenuApi>().lambda().eq(SysMenuApi::getApiId, apiId));
	}
	
	public List<SysMenuApi> listByMenuId(Long menuId){
		return list(new QueryWrapper<SysMenuApi>().lambda().eq(SysMenuApi::getMenuId, menuId));
	}

	public void clearUnavailablePerms() {
		
	}
}
