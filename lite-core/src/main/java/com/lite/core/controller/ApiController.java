package com.lite.core.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lite.core.service.SysApiService;
import com.lite.core.utils.PageData;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.SearchUtil;

@Controller
@RequestMapping("/sys/perm")
public class ApiController {
	private String prefix = "core/sys/perm";
	@Autowired
	private SysApiService sysApiService;
	
	@GetMapping()
	String perm() {
		return prefix + "/perm";
	}

	@GetMapping("/page")
	@ResponseBody
	public PageData page(@RequestParam Map<String, Object> params) {
		return SearchUtil.selectPageData(sysApiService, null, params);
	}

	/**
	 * 初始化
	 */
	@ResponseBody
	@PostMapping("/init")
	public ResponseData<String> init() {
		sysApiService.initAllPerms();
		return ResponseData.getSuccess();
	}
	
	@PostMapping("/associateApiAndRole")
	@ResponseBody	
	public ResponseData<String> associateApiAndRole(@RequestParam("apiId") Long apiId, @RequestParam("roleIds") List<Long> roleIds) {
		sysApiService.associateApiAndRole(apiId, roleIds);
		return ResponseData.getSuccess();
	}
	
	
}
