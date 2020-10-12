package com.lite.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lite.core.entity.SysDept;
import com.lite.core.service.SysDeptService;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.tree.Tree;

/**
 * 部门管理
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2017-09-27 14:40:36
 */

@Controller
@RequestMapping("/sys/dept")
public class DeptController {
	private String prefix = "core/sys/dept";
	@Autowired
	private SysDeptService sysDeptService;

	@GetMapping()
	String dept() {
		return prefix + "/dept";
	}

	@ResponseBody
	@GetMapping("/list")
	public List<SysDept> list() {
		List<SysDept> sysDeptList = sysDeptService.list();
		return sysDeptList;
	}

	@GetMapping("/add/{pId}")
	String add(@PathVariable("pId") Long pId, Model model) {
		model.addAttribute("pId", pId);
		if (pId == 0) {
			model.addAttribute("pName", "总部门");
		} else {
			model.addAttribute("pName", sysDeptService.getById(pId).getName());
		}
		return  prefix + "/add";
	}

	@GetMapping("/edit/{deptId}")
	String edit(@PathVariable("deptId") Long deptId, Model model) {
		SysDept sysDept = sysDeptService.getById(deptId);
		model.addAttribute("sysDept", sysDept);
		if(SysDeptService.DEPT_ROOT_ID.equals(sysDept.getParentId())) {
			model.addAttribute("parentDeptName", "无");
		}else {
			SysDept parDept = sysDeptService.getById(sysDept.getParentId());
			model.addAttribute("parentDeptName", parDept.getName());
		}
		return  prefix + "/edit";
	}

	/**
	 * 保存
	 */
	@ResponseBody
	@PostMapping("/save")
	public ResponseData<String> save(SysDept sysDept) {
		if (sysDeptService.save(sysDept)) {
			return ResponseData.getSuccess();
		}
		return ResponseData.getFailure();
	}

	/**
	 * 修改
	 */
	@ResponseBody
	@RequestMapping("/update")
	public ResponseData<String> update(SysDept sysDept) {
		if (sysDeptService.updateById(sysDept)) {
			return ResponseData.getSuccess();
		}
		return ResponseData.getFailure();
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	public ResponseData<String> remove(Long deptId) {
		if(sysDeptService.count(new QueryWrapper<SysDept>().lambda().eq(SysDept::getParentId, deptId))>0) {
			return ResponseData.getFailure("包含下级部门,不允许修改");
		}
		if(sysDeptService.checkDeptHasUser(deptId)) {
			if (sysDeptService.removeById(deptId)) {
				return ResponseData.getSuccess();
			}
		}else {
			return ResponseData.getFailure("部门包含用户,不允许修改");
		}
		return ResponseData.getFailure();
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	public ResponseData<String> remove(@RequestParam("ids[]") List<Long> deptIds) {
		sysDeptService.removeByIds(deptIds);
		return ResponseData.getSuccess();
	}

	@GetMapping("/tree")
	@ResponseBody
	public Tree<SysDept> tree() { 
		Tree<SysDept> tree = new Tree<SysDept>();
		tree = sysDeptService.getTree();
		return tree;
	}

	@GetMapping("/treeView")
	String treeView() {
		return  prefix + "/deptTree";
	}

}
