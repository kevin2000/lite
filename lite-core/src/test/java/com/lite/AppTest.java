package com.lite;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lite.core.entity.SysUser;
import com.lite.core.mapper.SysUserMapper;
import com.lite.core.service.SysApiService;
import com.lite.core.service.SysUserService;
import com.lite.core.utils.StringUtils;

import cn.hutool.core.util.PageUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
// @ActiveProfiles(/*"TST"*/"PRDHK")
public class AppTest {
	@Autowired
	SysUserService userService;
	@Autowired
	SysUserMapper userMapper;
	@Autowired
	SysApiService perService;
	@Test
	public void test() {
		perService.initAllPerms();
		/*
		 * Map<String, Object> params = new HashMap<>(); params.put("offset", 0);
		 * params.put("limit", 3); params.put("status", 0); Page<SysUser> page =
		 * com.lite.core.utils.SearchUtil.selectPage(userService, null, params);
		 * System.out.println(page.getTotal()); page.getRecords().forEach(d ->
		 * System.out.println(d));
		 */
		
	}
	public static void main(String[] args) {
		System.out.println(StringUtils.camelToUnderLine("helloWorld"));
		System.out.println(StringUtils.camelToUnderLine("HelloWorlD"));
	}
}
