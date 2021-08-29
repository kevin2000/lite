package com.lite.core.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lite.core.dto.DicOption;
import com.lite.core.entity.SysDic;
import com.lite.core.service.SysDicService;
import com.lite.core.utils.PageData;
import com.lite.core.utils.ResponseData;
import com.lite.core.utils.SearchUtil;
import com.lite.core.utils.Util;

@Controller
@RequestMapping("/sys/dic")
public class DicController {
	private String prefix = "core/sys/dic";
	@Autowired
	private SysDicService dicService;
	
	@RequestMapping("/dic/dic")
	public String dic() {
		return prefix + "/dic";
	}
	
	@RequestMapping("/dic/getPageDic")
	@ResponseBody
	public PageData getPageDic(Map<String, Object> params) {
		return SearchUtil.selectPageData(dicService, null, params);
	}
	
	@RequestMapping("/dic/dicForm")
	public String dicForm(@RequestParam(value = "id", required = false) String code, Model model) {
		if (StringUtils.isNotBlank(code)) {
			model.addAttribute("model", dicService.getById(code));
		} else {
			SysDic dic = new SysDic();
			dic.setType("table");
			dic.setStatus("enabled");
			model.addAttribute("model", dic);
		}
		return "core/dic/dicForm";
	}
	
	/**
	 * 
	 * @param dic
	 * @param editType create/modify
	 * @return
	 */
	@RequestMapping("/dic/saveDic")
	@ResponseBody
	public ResponseData<String> saveDic(SysDic dic, @RequestParam("editType")String editType) {
		return dicService.saveDic(dic, editType);
	}
	
	/**
	 * 
	 * @param dicCode
	 * @param selectedCodes separated with comma
	 * @return
	 */
	@RequestMapping("/dic/getDicOptions")
	@ResponseBody
	public ResponseData<List<DicOption>> getOptions(@RequestParam("dicCode") String dicCode, @RequestParam("optionCodes") String optionCodes) {
		return ResponseData.getSuccess(dicService.getDicOptions(dicCode, Arrays.asList(StringUtils.split(optionCodes, ","))));
	}
	
	/**
	 * 
	 * @param dicCode
	 * @param selectedCodes separated with comma
	 * @return
	 */
	@RequestMapping("/dic/dicOptionSelector")
	public String dicOptionSelector(@RequestParam("dicCode") String dicCode, @RequestParam(value = "selectedCodes", required = false) String selectedCodes, Model model) {
		if (StringUtils.isNotBlank(selectedCodes)) {
			model.addAttribute("selectedOptions", dicService.getDicOptions(dicCode, Arrays.asList(StringUtils.split(selectedCodes, ","))));
		}
		return "core/dic/dicOptionSelector";
	}
	
	@RequestMapping("/dic/getPageOption")
	@ResponseBody
	public PageData getPageOption(@RequestParam("offset") int offset, @RequestParam("limit") int limit, @RequestParam("conditions") String conditions, @RequestParam("orders") String orders) {
		return dicService.getPage(Util.jsonStrToMap(conditions), offset, limit);
	}
}
