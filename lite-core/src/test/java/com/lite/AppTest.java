package com.lite;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lite.core.entity.SysUser;
import com.lite.core.service.ISysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
// @ActiveProfiles(/*"TST"*/"PRDHK")
public class AppTest {
	@Autowired
	private ISysUserService userService;

	@Test
	public void test() {
		System.out.println(("----- selectAll method test ------"));
		List<SysUser> userList = userService.list();
		Assert.assertEquals(2, userList.size());
		userList.forEach(System.out::println);
	}
}
