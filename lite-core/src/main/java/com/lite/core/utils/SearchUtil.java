package com.lite.core.utils;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public class SearchUtil {

	/**
	 * compatible for bootstrap table
	 */
	public static <T> PageData selectPageData (IService<T> serviceImpl, List<SearchCondition> customConditions, Map<String, Object> params) {
		Page<T> page = selectPage(serviceImpl, customConditions, params);
		return new PageData(page.getRecords(), page.getTotal());
	}
	
	/**
	 * 
	 * @param <T>
	 * @param serviceImpl
	 * @param customConditions
	 * @param params must contains offset, limit
	 * @return
	 */
	public static <T> Page<T> selectPage(IService<T> serviceImpl, List<SearchCondition> customConditions, Map<String, Object> params) {
		int offset = Integer.parseInt((String)params.get("offset"));
		params.remove("offset");
		int limit = Integer.parseInt((String)params.get("limit"));
		params.remove("limit");
		//TODO 
		String order = (String)params.get("order");
		params.remove("order");
		String sort = (String)params.get("sort");
		params.remove("sort");
		Page<T> page = new Page<>(offset / limit + 1, limit);
		if (StringUtils.isNotBlank(sort)) {
			if ("asc".equals(order)) {
				page.addOrder(OrderItem.asc(sort));
			} else {
				page.addOrder(OrderItem.desc(sort));
			}
		}
		
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (CollectionUtils.isNotEmpty(customConditions)) {
			parseConditions(queryWrapper, customConditions);
		}
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			queryWrapper.eq(com.lite.core.utils.StringUtils.camelToUnderLine(entry.getKey()), entry.getValue());
		}
		return serviceImpl.getBaseMapper().selectPage(page, queryWrapper);
	}

	public static <T> void parseConditions(QueryWrapper<T> queryWrapper, List<SearchCondition> conditions) {
		for (SearchCondition conditionVo : conditions) {
			switch (conditionVo.getType()) {
			case "eq":
				queryWrapper.eq(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "ne":
				queryWrapper.ne(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "like":
				queryWrapper.like(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "leftlike":
				queryWrapper.likeLeft(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "rightlike":
				queryWrapper.likeRight(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "notlike":
				queryWrapper.notLike(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "gt":
				queryWrapper.gt(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "lt":
				queryWrapper.lt(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "ge":
				queryWrapper.ge(conditionVo.getColumn(), conditionVo.getValue());
				break;
			case "le":
				queryWrapper.le(conditionVo.getColumn(), conditionVo.getValue());
				break;
			}
		}
	}

	
}
