package com.lite.core.controller;

import com.lite.core.annotation.Log;
import com.lite.core.entity.SysMenu;
import com.lite.core.service.SysMenuService;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.tree.Tree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/sys/menu")
@Controller
public class MenuController {
	String prefix = "core/sys/menu";
	@Autowired
	SysMenuService menuService;

	@GetMapping()
	String menu(Model model) {
		return prefix+"/menu";
	}

	@RequestMapping("/list")
	@ResponseBody
	List<SysMenu> list(@RequestParam Map<String, Object> params) {
		params.remove("sort");
		List<SysMenu> menus = menuService.listByMap(params);
		return menus;
	}

	@Log("添加菜单")
	@GetMapping("/add/{pId}")
	String add(Model model, @PathVariable("pId") Long pId) {
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.getById(pId).getName());
		}
		return prefix + "/add";
	}

	@Log("编辑菜单")
	@GetMapping("/edit/{id}")
	String edit(Model model, @PathVariable("id") Long id) {
		SysMenu mdo = menuService.getById(id);
		Long pId = mdo.getParentId();
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "根目录");
		} else {
			model.addAttribute("pName", menuService.getById(pId).getName());
		}
		model.addAttribute("menu", mdo);
		return prefix+"/edit";
	}

	@Log("保存菜单")
	@PostMapping("/save")
	@ResponseBody
	ResponseData<String> save(SysMenu menu) {
		if (menuService.save(menu)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure("保存失败");
		}
	}

	@Log("更新菜单")
	@PostMapping("/update")
	@ResponseBody
	ResponseData<String> update(SysMenu menu) {
		if (menuService.updateById(menu)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure("保存失败");
		}
	}

	@Log("删除菜单")
	@PostMapping("/remove")
	@ResponseBody
	ResponseData<String> remove(Long id) {
		if (menuService.removeById(id)) {
			return ResponseData.getSuccess();
		} else {
			return ResponseData.getFailure("删除失败");
		}
	}

	@GetMapping("/tree")
	@ResponseBody
	Tree<SysMenu> tree() {
		Tree<SysMenu>  tree = menuService.getTree();
		return tree;
	}

	@GetMapping("/tree/{roleId}")
	@ResponseBody
	Tree<SysMenu> tree(@PathVariable("roleId") Long roleId) {
		Tree<SysMenu> tree = menuService.getTree(roleId);
		return tree;
	}
}
