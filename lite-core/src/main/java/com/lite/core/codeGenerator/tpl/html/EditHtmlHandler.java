package com.lite.core.codeGenerator.tpl.html;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import com.lite.core.codeGenerator.GenerateHandler;

/**
 * https://blog.csdn.net/wuqilianga/article/details/81416016
 * https://blog.csdn.net/gzt19881123/article/details/107204689?utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-4.essearch_pc_relevant&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7Edefault-4.essearch_pc_relevant
 * @author joe
 *
 */
public class EditHtmlHandler implements GenerateHandler{

	@Override
	public boolean handle(Class<?> clz) {
		
		MessageFormat.format("<!DOCTYPE HTML>" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\"" + 
				"	xmlns:th=\"http://www.thymeleaf.org\"" + 
				"	xmlns:sec=\"http://www.thymeleaf.org/thymeleaf-extras-springsecurity4\">" + 
				"<title>Advertise Group Edit</title>" + 
				"<head th:include=\"include :: header\">" + 
				"</head>" + 
				"" + 
				"<body>" + 
				"	<div class=\"ibox float-e-margins\">" + 
				"		<div class=\"ibox-title\">" + 
				"			<br>" + 
				"		</div>" + 
				"		<div class=\"ibox-content\">" + 
				"			<form class=\"form-horizontal m-t\" id=\"formEdit\">" + 
				"				<div class=\"form-group\">" + 
				"					<input type=\"hidden\" id=\"editType\" name=\"editType\"" + 
				"						th:value=\"${editType}\"> <label" + 
				"						class=\"col-sm-3 control-label\">Code</label>" + 
				"					<div class=\"col-sm-8\">" + 
				"						<input id=\"code\" name=\"code\" th:value=\"${model.code}\" type=\"text\"" + 
				"							class=\"form-control\" th:readonly=\"@{null != ${model.code}}\">" + 
				"					</div>" + 
				"				</div>" + 
				"				<div class=\"form-group\">" + 
				"					<div class=\"col-sm-8 col-sm-offset-3\">" + 
				"						<button type=\"submit\" class=\"btn btn-primary btn-sm\"" + 
				"							onclick=\"reutrn false\">Submit</button>" + 
				"						&nbsp;" + 
				"						<button type=\"button\" class=\"btn btn-info btn-sm\"" + 
				"							onclick=\"cancel()\">Cancel</button>" + 
				"					</div>" + 
				"				</div>" + 
				"			</form>" + 
				"		</div>" + 
				"	</div>" + 
				"	<div th:include=\"include::footer\"></div>" + 
				"	<script th:src=\"@{/js/core/dic/dicForm.js}\"></script>" + 
				"</body>" + 
				"</html>", "");
		// TODO Auto-generated method stub
		return true;
	}
	
	private String gen(Field[] fields) {
		if (null != fields) {
			StringBuilder sb = new StringBuilder();
			for (Field field : fields) {
				sb.append(MessageFormat.format(
						"				<div class=\"form-group\">\r\n" + 
						"					<label class=\"col-sm-3 control-label\">Name Field</label>\r\n" + 
						"					<div class=\"col-sm-8\">\r\n" + 
						"						<input id=\"{1}\" name=\"{1}\"\r\n" + 
						"							th:value=\"${model.{1}}\" type=\"text\" class=\"form-control\">\r\n" + 
						"					</div>\r\n" + 
						"				</div>", field.getName()));
			}
			return sb.toString();
		} else {
			return "";
		}
	}
}
