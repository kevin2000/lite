package com.lite.core.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lite.core.service.SysPermService;
import com.lite.core.utils.PageData;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.SearchUtil;

@Controller
@RequestMapping("/sys/perm")
public class PermController {
	private String prefix = "core/sys/perm";
	@Autowired
	private SysPermService sysPermService;
	
	@GetMapping()
	String perm() {
		return prefix + "/perm";
	}

	@GetMapping("/page")
	@ResponseBody
	public PageData page(@RequestParam Map<String, Object> params) {
		return SearchUtil.selectPageData(sysPermService, null, params);
	}

	/**
	 * 初始化
	 */
	@ResponseBody
	@PostMapping("/init")
	public ResponseData<String> init() {
		sysPermService.initAllPerms();
		return ResponseData.getSuccess();
	}
}
