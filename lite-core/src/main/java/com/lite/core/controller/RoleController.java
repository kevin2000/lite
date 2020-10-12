package com.lite.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.lite.core.entity.SysRole;
import com.lite.core.service.SysRoleService;
import com.lite.core.utils.ResponseData;

import java.util.List;

@RequestMapping("/sys/role")
@Controller
public class RoleController {
	String prefix = "core/sys/role";
	@Autowired
	SysRoleService roleService;

	@GetMapping()
	String role() {
		return prefix + "/role";
	}

	@GetMapping("/list")
	@ResponseBody()
	List<SysRole> list() {
		List<SysRole> roles = roleService.list();
		return roles;
	}

	@GetMapping("/add")
	String add() {
		return prefix + "/add";
	}

	@GetMapping("/edit/{id}")
	String edit(@PathVariable("id") Long id, Model model) {
		SysRole roleDO = roleService.getById(id);
		model.addAttribute("role", roleDO);
		return prefix + "/edit";
	}

	@PostMapping("/save")
	@ResponseBody()
	ResponseData<String> save(SysRole role) {
		if (roleService.save(role)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure("保存失败");
		}
	}

	@PostMapping("/update")
	@ResponseBody()
	ResponseData<String> update(SysRole role) {
		if (roleService.updateById(role)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure("保存失败");
		}
	}

	@PostMapping("/remove")
	@ResponseBody()
	ResponseData<String> save(Long id) {
		if (roleService.removeById(id)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure("删除失败");
		}
	}
	
	@PostMapping("/batchRemove")
	@ResponseBody
	ResponseData<String> batchRemove(@RequestParam("ids[]") List<Long> ids) {
		if (roleService.removeByIds(ids)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure();
		}
	}
}
