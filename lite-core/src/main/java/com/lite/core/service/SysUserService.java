package com.lite.core.service;

import com.lite.core.dto.LoginUser;
import com.lite.core.dto.UserVO;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.tree.BuildTree;
import com.lite.core.utils.tree.Tree;
import com.lite.core.entity.SysDept;
import com.lite.core.entity.SysUser;
import com.lite.core.mapper.SysUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.EncryptUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {
	@Autowired
	private SysDeptService deptService;
	public ResponseData<LoginUser> login(String username, String password){
		String encryptPwd = EncryptUtils.md5Base64(username + password);
		SysUser user = getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username).eq(SysUser::getPassword, encryptPwd));
		
		return ResponseData.getSuccess(new LoginUser());
	}
	
	public ResponseData<LoginUser> register(SysUser user) {
		SysUser oldUser = getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, user.getUsername()));
		if (null != oldUser) {
			return ResponseData.getFailure("无效用户名");
		} else {
			if (StringUtils.isBlank(user.getPassword()))
				return ResponseData.getFailure("无效密码");
			user.setPassword(EncryptUtils.md5Base64(user.getUsername() + user.getPassword()));
			save(user);
			return ResponseData.getSuccess(new LoginUser());
		}
	}
	
	public ResponseData<String> resetPwd(UserVO userVO, SysUser userDO) {
        if (Objects.equals(userVO.getUserDO().getUserId(), userDO.getUserId())) {
            if (Objects.equals(EncryptUtils.md5Base64(userDO.getUsername() + userVO.getPwdOld()), userDO.getPassword())) {
                userDO.setPassword(EncryptUtils.md5Base64(userDO.getUsername() +  userVO.getPwdNew()));
                updateById(userDO);
                return ResponseData.getSuccess();
            } else {
                return ResponseData.getFailure("输入的旧密码有误！");
            }
        } else {
            return ResponseData.getFailure("你修改的不是你登录的账号！");
        }
    }

    public ResponseData<String> adminResetPwd(UserVO userVO) {
        SysUser userDO = getById(userVO.getUserDO().getUserId());
        if ("admin".equals(userDO.getUsername())) {
            return ResponseData.getFailure("超级管理员的账号不允许直接重置！");
        }
        userDO.setPassword(encryptPwd(userDO.getUsername(), userVO.getPwdNew()));
        updateById(userDO);
        return ResponseData.getSuccess();
    }
	
    private String encryptPwd(String username, String password) {
    	return EncryptUtils.md5Base64(username + password);
    }
    
	public int getDeptUserNumber(Long deptId) {
		return count(new QueryWrapper<SysUser>().lambda().eq(SysUser::getDeptId, deptId));
	}
	
	public Tree<SysDept> getTree() {
        List<Tree<SysDept>> trees = new ArrayList<Tree<SysDept>>();
        List<SysDept> depts = deptService.list();
        for (SysDept dept : depts) {
            Tree<SysDept> tree = new Tree<SysDept>();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setText(dept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        List<SysUser> users = list();
        for (SysUser user : users) {
            Tree<SysDept> tree = new Tree<SysDept>();
            tree.setId(user.getUserId().toString());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SysDept> t = BuildTree.build(trees);
        return t;
    }
	
	//============== session begin =====================================
	public SysUser getCurrUser() {
		return null;
	}

	public Long getCurrUserId() {
		return null;//getUser().getUserId();
	}

	public String getCurrUsername() {
		return null;//getUser().getUsername();
	}
	//============== session end =====================================
}
