package com.lite.core.auth;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lite.core.dto.PermVo;
import com.lite.core.entity.SysApi;

public class PermissionUtil {

	
	/**
	 * sys/user/user
	 * sys/user/getPage
	 * sys/user/edit
	 * sys/user/detail
	 * sys/user/changePwd
	 * @param permissions
	 * @return
	 */
    public static List<PermVo> listPermVo(List<SysApi> permissions) {
        List<PermVo> root = new ArrayList<>();
        Map<String, PermVo> mapApiToPerm = new HashMap<>();
        PermVo perm = null;
        for (SysApi permission : permissions) {
            String api = permission.getUrl();
            String pre = null;
            int lastIndex = api.lastIndexOf("/");
            while(lastIndex > 0) {
            	pre = api.substring(0, lastIndex);
            	if (mapApiToPerm.containsKey(pre)) {
            		perm = new PermVo();
            		perm.setApi(api);
            		mapApiToPerm.get(pre).getChildren().add(perm);
            	} else {
            		perm = new PermVo();
            		perm.setApi(api);
            		
            	}
            	api = pre;
            	
            	lastIndex = api.lastIndexOf("/");
            }
            
            perm = new PermVo();
    		perm.setApi(api);
    		root.add(perm);
            /*String[] menus = requiresPermissionsDesc.menu();
            if (menus.length != 2) {
                throw new RuntimeException("目前只支持两级菜单");
            }
            String menu1 = menus[0];
            PermVo perm1 = null;
            for (PermVo permVo : root) {
                if (permVo.getLabel().equals(menu1)) {
                    perm1 = permVo;
                    break;
                }
            }
            if (perm1 == null) {
                perm1 = new PermVo();
                perm1.setId(menu1);
                perm1.setLabel(menu1);
                perm1.setChildren(new ArrayList<>());
                root.add(perm1);
            }
            String menu2 = menus[1];
            PermVo perm2 = null;
            for (PermVo permVo : perm1.getChildren()) {
                if (permVo.getLabel().equals(menu2)) {
                    perm2 = permVo;
                    break;
                }
            }
            if (perm2 == null) {
                perm2 = new PermVo();
                perm2.setId(menu2);
                perm2.setLabel(menu2);
                perm2.setChildren(new ArrayList<>());
                perm1.getChildren().add(perm2);
            }

            String button = requiresPermissionsDesc.button();
            PermVo leftPerm = null;
            for (PermVo permVo : perm2.getChildren()) {
                if (permVo.getLabel().equals(button)) {
                    leftPerm = permVo;
                    break;
                }
            }
            if (leftPerm == null) {
                leftPerm = new PermVo();
                leftPerm.setId(requiresPermissions.value()[0]);
                leftPerm.setLabel(requiresPermissionsDesc.button());
                leftPerm.setUrl(api);
                perm2.getChildren().add(leftPerm);
            } else {
                // TODO
                // 目前限制Controller里面每个方法的RequiresPermissionsDesc注解是唯一的
                // 如果允许相同，可能会造成内部权限不一致。
                throw new RuntimeException("权限已经存在，不能添加新权限");
            }*/

        }
        return root;
    }

    public static List<SysApi> listPermission(ApplicationContext context, String basicPackage) {
        Map<String, Object> map = context.getBeansWithAnnotation(Controller.class);
        List<SysApi> permissions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object bean = entry.getValue();
            if (!StringUtils.contains(ClassUtils.getPackageName(bean.getClass()), basicPackage)) {
                continue;
            }

            Class<?> clz = bean.getClass();
            RequestMapping clazzRequestMapping = AnnotationUtils.findAnnotation(clz, RequestMapping.class);
            String classUrl = "";
            if (clazzRequestMapping != null) {
            	classUrl = clazzRequestMapping.value()[0];
            }
            List<Method> methods = MethodUtils.getMethodsListWithAnnotation(clz, RequestMapping.class, false, true);
            for (Method method : methods) {
                RequestMapping mapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
                if (mapping != null) {
                	SysApi permission = new SysApi();
                	
                	if (mapping.value().length > 0)
                		permission.setUrl(classUrl + mapping.value()[0]);
                	else
                		continue;
                    if (null != mapping.method() && mapping.method().length > 0)
                    	permission.setMethod(mapping.method()[0].name());
                    else
                    	permission.setMethod("*");
                    
                    permissions.add(permission);
                }
            }
            
            methods = MethodUtils.getMethodsListWithAnnotation(clz, PostMapping.class, false, true);
            for (Method method : methods) {
            	PostMapping mapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
                if (mapping != null) {
                	SysApi permission = new SysApi();
                	
                	if (mapping.value().length > 0)
                		permission.setUrl(classUrl + mapping.value()[0]);
                	else
                		continue;
                    permission.setMethod(RequestMethod.POST.name());
                    
                    permissions.add(permission);
                }
            }
            
            methods = MethodUtils.getMethodsListWithAnnotation(clz, GetMapping.class, false, true);
            for (Method method : methods) {
            	GetMapping mapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
                if (mapping != null) {
                	SysApi permission = new SysApi();
                	
                	if (mapping.value().length > 0)
                		permission.setUrl(classUrl + mapping.value()[0]);
                	else
                		continue;
                    permission.setMethod(RequestMethod.GET.name());
                    
                    permissions.add(permission);
                }
            }
            
            methods = MethodUtils.getMethodsListWithAnnotation(clz, PutMapping.class, false, true);
            for (Method method : methods) {
            	PutMapping mapping = AnnotationUtils.getAnnotation(method, PutMapping.class);
                if (mapping != null) {
                	SysApi permission = new SysApi();
                	
                	if (mapping.value().length > 0)
                		permission.setUrl(classUrl + mapping.value()[0]);
                	else
                		continue;
                    permission.setMethod(RequestMethod.PUT.name());
                    
                    permissions.add(permission);
                }
            }
            
            methods = MethodUtils.getMethodsListWithAnnotation(clz, DeleteMapping.class, false, true);
            for (Method method : methods) {
            	DeleteMapping mapping = AnnotationUtils.getAnnotation(method, DeleteMapping.class);
                if (mapping != null) {
                	SysApi permission = new SysApi();
                	
                	if (mapping.value().length > 0)
                		permission.setUrl(classUrl + mapping.value()[0]);
                	else
                		continue;
                    permission.setMethod(RequestMethod.DELETE.name());
                    
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }
    
    public static Set<String> listPermissionString(List<SysApi> permissions) {
        Set<String> permissionsString = new HashSet<>();
        for (SysApi permission : permissions) {
            permissionsString.add(permission.getUrl());
        }
        return permissionsString;
    }
}
