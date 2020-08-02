package com.lite;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
// @ActiveProfiles(/*"TST"*/"PRDHK")
public class AppTest {

	@Test
	public void test() {
		System.out.println(("----- selectAll method test ------"));
	}
}
