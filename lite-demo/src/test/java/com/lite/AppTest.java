package com.lite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lite.core.codeGenerator.AutoGenerator;
import com.lite.core.codeGenerator.EnjoyGenerator;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
// @ActiveProfiles(/*"TST"*/"PRDHK")
public class AppTest {
	@Test
	public void test() {
	}

	private static List<String> getSqlList() {
        ArrayList<String> sqlList = new ArrayList<String>();
        
        sqlList.add("DROP TABLE IF EXISTS `t_test`;");
        sqlList.add("CREATE TABLE `t_test` ( " +
                "  `id` int(11) NOT NULL AUTO_INCREMENT, " +
                "  `test` varchar(255) DEFAULT NULL, " +
                "  PRIMARY KEY (`id`) " +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        
        return sqlList;
    }
	
	public static void main(String[] args) {
		String className = "Test";
        String tableName = "t_test";

        EnjoyGenerator.me
        .setSrcFolder("src/main/java")
        .setViewFolder("src/main/webapp")
        .setPackageBase("com")
        .setBasePath("demo")
        //.tableSql(getSqlList())
        //.javaRender(className, tableName)
        .htmlRender(className, tableName);

        System.out.println("---------OK-刷新一下项目吧---------");
		/*
		 * AutoGenerator gen = new AutoGenerator(); gen.execute();
		 */
		 

		// 包下面的类
		 
			/*
			 * Set<Class<?>> clazzs = ClassUtil.getClasses("com.lite.demo.entity"); if
			 * (clazzs == null) { return; }
			 * 
			 * Set<Class<?>> inInterface = ClassUtil.getByInterface(Object.class, clazzs);
			 * System.out.printf(inInterface.size() + "");
			 * 
			 * for (Class<?> clazz : clazzs) {
			 * 
			 * for (Field field : clazz.getDeclaredFields()) {
			 * System.out.println(field.getName() + " " + field.getType()); Annotation[]
			 * annos = field.getAnnotations(); for (Annotation anno : annos) {
			 * System.out.println(anno.toString()); } }
			 * 
			 * Annotation[] annos = clazz.getAnnotations(); for (Annotation anno : annos) {
			 * System.out.println(clazz.getSimpleName().concat(".").concat(anno.
			 * annotationType().getSimpleName())); }
			 * 
			 * Method[] methods = clazz.getDeclaredMethods(); for (Method method : methods)
			 * { Annotation[] annotations = method.getDeclaredAnnotations(); for (Annotation
			 * annotation : annotations) {
			 * System.out.println(clazz.getSimpleName().concat(".").concat(method.getName())
			 * .concat(".") .concat(annotation.annotationType().getSimpleName())); } } }
			 */
		 

	}

}
