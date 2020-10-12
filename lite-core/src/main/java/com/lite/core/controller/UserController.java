package com.lite.core.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.lite.core.dto.LoginUser;
import com.lite.core.dto.UserVO;
import com.lite.core.entity.SysDept;
import com.lite.core.entity.SysRole;
import com.lite.core.entity.SysUser;
import com.lite.core.service.SysDictService;
import com.lite.core.service.SysRoleService;
import com.lite.core.service.SysUserService;
import com.lite.core.utils.PageData;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.SearchUtil;
import com.lite.core.utils.tree.Tree;

@RequestMapping("/sys/user")
@Controller
public class UserController {
	private String prefix="core/sys/user"  ;
	@Autowired
	SysUserService userService;
	@Autowired
	SysRoleService roleService;
	@Autowired
	SysDictService dictService;
	@GetMapping("")
	String user(Model model) {
		return prefix + "/user";
	}

	@GetMapping("/list")
	@ResponseBody
	PageData list(@RequestParam Map<String, Object> params) {
		return SearchUtil.selectPageData(userService, null, params);
	}

	@GetMapping("/add")
	String add(Model model) {
		List<SysRole> roles = roleService.list();
		model.addAttribute("roles", roles);
		return prefix + "/add";
	}

	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Long id) {
		SysUser SysUser = userService.getById(id);
		model.addAttribute("user", SysUser);
		List<SysRole> roles = roleService.listByUserId(id);
		model.addAttribute("roles", roles);
		return prefix+"/edit";
	}

	@PostMapping("/save")
	@ResponseBody
	ResponseData<LoginUser> save(SysUser user) {
		return userService.register(user);
	}

	@PostMapping("/update")
	@ResponseBody
	ResponseData<String> update(SysUser user) {
		if (userService.updateById(user)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure();
		}
	}


	@PostMapping("/updatePeronal")
	@ResponseBody
	ResponseData<String> updatePeronal(SysUser user) {
		if (userService.updateById(user)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure();
		}
	}


	@PostMapping("/remove")
	@ResponseBody
	ResponseData<String> remove(Long id) {
		if (userService.removeById(id)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure();
		}
	}

	@PostMapping("/batchRemove")
	@ResponseBody
	ResponseData<String> batchRemove(@RequestParam("ids[]") List<Long> userIds) {
		if (userService.removeByIds(userIds))
			return ResponseData.getSuccess();
		else
			return ResponseData.getFailure();
	}

	@PostMapping("/exit")
	@ResponseBody
	boolean exit(@RequestParam Map<String, Object> params) {
		// 存在，不通过，false
		//return !userService.exit(params);
		return true;
	}

	@GetMapping("/resetPwd/{id}")
	String resetPwd(@PathVariable("id") Long userId, Model model) {

		SysUser SysUser = new SysUser();
		SysUser.setUserId(userId);
		model.addAttribute("user", SysUser);
		return prefix + "/reset_pwd";
	}

	@PostMapping("/resetPwd")
	@ResponseBody
	ResponseData<String> resetPwd(UserVO userVO) {
		try{
			//TODO userService.resetPwd(userVO, getUser());
			return ResponseData.getSuccess();
		}catch (Exception e){
			return ResponseData.getFailure(e.getMessage());
		}

	}
	@PostMapping("/adminResetPwd")
	@ResponseBody
	ResponseData<String> adminResetPwd(UserVO userVO) {
		return userService.adminResetPwd(userVO);
	}
	
	@GetMapping("/tree")
	@ResponseBody
	public Tree<SysDept> tree() {
		Tree<SysDept> tree = new Tree<SysDept>();
		tree = userService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return  prefix + "/userTree";
	}

	@GetMapping("/personal")
	String personal(Model model) {
		SysUser SysUser  = userService.getById(userService.getCurrUserId());
		model.addAttribute("user",SysUser);
		model.addAttribute("hobbyList",dictService.getHobbyList(SysUser));
		model.addAttribute("sexList",dictService.getSexList());
		return prefix + "/personal";
	}
	
	
	@ResponseBody
	@PostMapping("/uploadImg")
	ResponseData<Map<String, Object>> uploadImg(@RequestParam("avatar_file") MultipartFile file, String avatar_data, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		try {
			//result = userService.updatePersonalImg(file, avatar_data, getUserId());
		} catch (Exception e) {
			return ResponseData.getFailure("更新图像失败！");
		}
		if(result!=null && result.size()>0){
			return ResponseData.getSuccess(result);
		}else {
			return ResponseData.getFailure("更新图像失败！");
		}
	}
}
