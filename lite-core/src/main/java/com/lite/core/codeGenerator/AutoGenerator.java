package com.lite.core.codeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.lite.core.codeGenerator.tpl.html.EditHtmlHandler;
import com.lite.core.codeGenerator.tpl.html.ListHtmlHandler;

import lombok.Data;

@Data
public class AutoGenerator {
	private String outputDir;
	private String[] basePackages;
	private GenerateHandlerChain generateHandlerChain;
	
	public AutoGenerator() {
		generateHandlerChain = new GenerateHandlerChain().add(new EditHtmlHandler()).add(new ListHtmlHandler());
	}
	
	public void setGenerateHandlerChain(GenerateHandlerChain generateHandlerChain) {
		this.generateHandlerChain = generateHandlerChain;
	}
	
	public void setBasePackages(String... basePackages) {
		this.basePackages = basePackages;
	}
	
	public void execute() {
		 
		
		if (null != generateHandlerChain ) {
			if (null != basePackages) {
				for (String basePackage : basePackages) { // 包下面的类
					Set<Class<?>> clazzs = ClassUtil.getClasses(basePackage);
					if (clazzs == null) {
						return;
					}
					for (Class<?> clz : clazzs) {
						generateHandlerChain.handle(clz);
					}
				}
			} else {
				System.out.println("No specified basePackages");
			}
		} else {
			System.out.println("No any generateHandler");
		}
		 
		 
		

		/*
		 * System.out.printf(clazzs.size() + ""); // 某类或者接口的子类 Set<Class<?>> inInterface
		 * = getByInterface(Object.class, clazzs); System.out.printf(inInterface.size()
		 * + "");
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
