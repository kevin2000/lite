package com.lite.core.service;

import com.lite.core.entity.SysMenuPerm;
import com.lite.core.mapper.SysMenuPermMapper;
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
public class SysMenuPermService extends ServiceImpl<SysMenuPermMapper, SysMenuPerm> {
	public void saveMenuPerm(SysMenuPerm menuPerm) {
		//figure out relation between role and menu
		
		save(menuPerm);
	}

	public List<SysMenuPerm> listByPermId(Long permId){
		return list(new QueryWrapper<SysMenuPerm>().lambda().eq(SysMenuPerm::getPermId, permId));
	}

	public void clearUnavailablePerms() {
		
	}
}
