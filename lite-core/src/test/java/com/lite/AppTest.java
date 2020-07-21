package com.lite;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lite.App;
import com.lite.dao.UserMapper;
import com.lite.entity.SysUser;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
// @ActiveProfiles(/*"TST"*/"PRDHK")
public class AppTest {
	@Autowired
	private UserMapper userMapper;

	@Test
	public void test() {
		System.out.println(("----- selectAll method test ------"));
		List<SysUser> userList = userMapper.selectByMap(new HashMap<String, Object>());
		Assert.assertEquals(2, userList.size());
		userList.forEach(System.out::println);
	}
}
