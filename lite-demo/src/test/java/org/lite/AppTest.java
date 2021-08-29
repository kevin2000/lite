package org.lite;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lite.App;
import com.lite.core.codeGenerator.AutoGenerator;
import com.lite.core.codeGenerator.ClassUtil;
import com.lite.core.codeGenerator.tpl.html.ListHtmlHandler;
import com.lite.core.codeGenerator.tpl.js.ListJsHandler;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
// @ActiveProfiles(/*"TST"*/"PRDHK")
public class AppTest {
    @Test
    public void test(){
    }
    
    public static void main(String[] args) {
    	AutoGenerator gen = new AutoGenerator();
    	gen.addGeneratorHandler(new ListJsHandler());
    	gen.execute();
    	
    	// 包下面的类
		/*
		 * Set<Class<?>> clazzs = ClassUtil.getClasses("com.lite.demo.entity"); if
		 * (clazzs == null) { return; }
		 * 
		 * System.out.printf(clazzs.size() + ""); // 某类或者接口的子类 Set<Class<?>> inInterface
		 * = ClassUtil.getByInterface(Object.class, clazzs);
		 * System.out.printf(inInterface.size() + "");
		 * 
		 * for (Class<?> clazz : clazzs) {
		 * 
		 * // 获取类上的注解 Annotation[] annos = clazz.getAnnotations(); for (Annotation anno
		 * : annos) { System.out.println(clazz.getSimpleName().concat(".").concat(anno.
		 * annotationType().getSimpleName())); }
		 * 
		 * // 获取方法上的注解 Method[] methods = clazz.getDeclaredMethods(); for (Method method
		 * : methods) { Annotation[] annotations = method.getDeclaredAnnotations(); for
		 * (Annotation annotation : annotations) {
		 * System.out.println(clazz.getSimpleName().concat(".").concat(method.getName())
		 * .concat(".") .concat(annotation.annotationType().getSimpleName())); } } }
		 */
	}
    
    
}
