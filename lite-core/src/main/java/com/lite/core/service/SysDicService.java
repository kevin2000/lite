package com.lite.core.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lite.core.dto.DicOption;
import com.lite.core.entity.SysDic;
import com.lite.core.mapper.SysDicMapper;
import com.lite.core.utils.PageData;
import com.lite.core.utils.ResponseData;

@Service
public class SysDicService extends ServiceImpl<SysDicMapper, SysDic>{
	@Autowired
	SysUserService userService;
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	/**
	 * 
	 * @param dicCode
	 * @param optionCodes separat with comma
	 * @return
	 */
	public List<DicOption> getDicOptions(String dicCode, List<String> optionCodes){
		SysDic dic = getById(dicCode);
		if (null == dic)
			return null;
		String condition = "'" + StringUtils.join(optionCodes, "','") + "'";
		String sql = "select " + dic.getCodeField() + "," + dic.getNameField() + " where " + dic.getCodeField() + " in(" + condition + ")";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(sql);
		
		if (!CollectionUtils.isEmpty(result)) {
			return mapsToDicOptions(dic, result);
		}
		return null;
	}
	
	public PageData getPage(Map<String, Object> mapCondition, long offset, int limit) {
		String dicCode = (String) mapCondition.get("dicCode");
		SysDic dic = getById(dicCode);
		if (null == dic)
			return new PageData(null, 0);
		
		Map<String, Object> orders = new HashMap<>();
		if (StringUtils.isNotBlank(dic.getSort())) {
			String[] sorts = dic.getSort().split(",");
			for (String sort : sorts) {
				String[] subSort = StringUtils.split(sort, " ");
				orders.put(subSort[0], subSort[1]);	
			}
		}
		
		String sqlFilter = dic.getFilter();
		String searchValue = (String)mapCondition.get("searchValue");
		if (StringUtils.isNotBlank(searchValue)) {
			if (StringUtils.isBlank(dic.getFilter())) {
				//merge role_name like '%用户%'
				sqlFilter = dic.getNameField() + " like '%" + searchValue + "%'";
			} else {
				Pattern pattern = Pattern.compile(".*[ ]?(" + dic.getNameField() + " ).*");
				Matcher matcher = pattern.matcher(dic.getFilter());
				if (matcher.find()) {
					int index = matcher.end(1);
					//if (index < 2) {
					//merge name like '%用户%' to name = 'xx' 	->	 name like '%用户%' and name = 'xx'
					sqlFilter = dic.getFilter().substring(0, index) + dic.getNameField() +  " like '%" + searchValue + "%' and " + dic.getFilter().substring(index);
					/*}
						 * else { //merge name like '%用户%' to code = 123 and name = 'xx' -> code = 123
						 * and name like '%用户%' and name = 'xx' sql = dic.getFilter().substring(0,
						 * index) + dic.getNameField() + " like '%" + searchValue + "%' and " +
						 * dic.getFilter().substring(index); }
						 */
					
				} else {
					//merge name like '%用户%' to status = 'enabled' 	->	 status = 'enabled' and name like '%用户%'
					sqlFilter = dic.getFilter() + " and " + dic.getNameField() + " like '%" + searchValue + "%'";
				}
			}
		}
		
		PageData pageData = new PageData(null, 0);
		if ("table".equals(dic.getType())) {
			//总共数量
			pageData.setTotal(jdbcTemplate.queryForObject("select count (1) from " + dic.getTable() + " where " + sqlFilter, null ,Integer.class));
			if (pageData.getTotal() > offset) {
				List<Map<String, Object>> result = jdbcTemplate.queryForList("select " + dic.getCodeField() + "," + dic.getNameField() + " from " + dic.getTable() + " where" + sqlFilter);
				if (null != result) {
					pageData.setRows(mapsToDicOptions(dic, result));
				}
			}
		}
		return pageData;
	}
	
	private List<DicOption> mapsToDicOptions(SysDic dic, List<Map<String, Object>> maps) {
		if (!CollectionUtils.isEmpty(maps)) {
			List<DicOption> options = new ArrayList<>(maps.size());
			for (Map<String, Object> map : maps) {
				options.add(mapToDicOption(dic, map));
			}
			return options;
		} else {
			return null;
		}
	}
	
	private DicOption mapToDicOption(SysDic dic, Map<String, Object> map) {
		DicOption option;
		option = new DicOption();
		option.setCode(String.valueOf(map.get(dic.getCodeField())));
		option.setName(String.valueOf(map.get(dic.getNameField())));
		return option;
	}
	
	public ResponseData<String> saveDic(SysDic dic, String editType){
		SysDic oldDic = getById(dic.getCode());
		if (null != oldDic) {
			if ("create".equals(editType)) {
				return ResponseData.getFailure("The Code " + dic.getCode() + " already exists, please replace it");
			}
			//oldDic.setName(dic.getName());
			oldDic.setCodeField(dic.getCodeField());
			oldDic.setFilter(dic.getFilter());
			oldDic.setNameField(dic.getNameField());
			oldDic.setRemark(dic.getRemark());
			oldDic.setSort(dic.getSort());
			oldDic.setStatus(dic.getStatus());
			oldDic.setTable(dic.getTable());
			oldDic.setType(dic.getType());
			
			oldDic.setModifier(userService.getCurrUserId());
			oldDic.setModifyTime(LocalDateTime.now());
			save(oldDic);
		} else {
			if ("modify".equals(editType)) {
				return ResponseData.getFailure("Can't find date by Code " + dic.getCode());
			}
			dic.setCreator(userService.getCurrUserId());
			dic.setCreateTime(LocalDateTime.now());
			save(dic);
		}
		
		return ResponseData.getSuccess();
	}
}
