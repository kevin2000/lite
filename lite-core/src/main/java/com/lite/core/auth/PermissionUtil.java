package com.lite.core.auth;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lite.core.dto.PermVo;

import java.lang.reflect.Method;
import java.util.*;

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
    public static List<PermVo> listPermVo(List<Permission> permissions) {
        List<PermVo> root = new ArrayList<>();
        Map<String, List<String>> permTree = new HashMap<>();
        for (Permission permission : permissions) {
            String api = permission.getApi();
            permTree.put(api, new ArrayList<>());
            
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
                leftPerm.setApi(api);
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

    public static List<Permission> listPermission(ApplicationContext context, String basicPackage) {
        Map<String, Object> map = context.getBeansWithAnnotation(Controller.class);
        List<Permission> permissions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            Object bean = entry.getValue();
            if (!StringUtils.contains(ClassUtils.getPackageName(bean.getClass()), basicPackage)) {
                continue;
            }

            Class<?> clz = bean.getClass();
            Class<?> controllerClz = clz.getSuperclass();
            RequestMapping clazzRequestMapping = AnnotationUtils.findAnnotation(controllerClz, RequestMapping.class);
            List<Method> methods = MethodUtils.getMethodsListWithAnnotation(controllerClz, RequestMapping.class);
            for (Method method : methods) {
                String api = "";
                if (clazzRequestMapping != null) {
                    api = clazzRequestMapping.value()[0];
                }
                
                PostMapping postMapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
                if (postMapping != null) {
                    api = api + postMapping.value()[0];

                    Permission permission = new Permission();
                    permission.setApi(api);
                    permission.setMethods(Arrays.asList(RequestMethod.POST.toString()));
                    permissions.add(permission);
                    continue;
                }
                GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
                if (getMapping != null) {
                    api = api + getMapping.value()[0];
                    Permission permission = new Permission();
                    permission.setMethods(Arrays.asList(RequestMethod.GET.toString()));
                    permission.setApi(api);
                    permissions.add(permission);
                    continue;
                }
                
                RequestMapping requestMapping = AnnotationUtils.getAnnotation(method, RequestMapping.class);
                if (requestMapping != null) {
                    api = api + requestMapping.value()[0];
                    Permission permission = new Permission();
                    permission.setMethods(Arrays.asList(requestMapping.method().toString()));
                    permission.setApi(api);
                    permissions.add(permission);
                    continue;
                }
                // TODO
                // 这里只支持GetMapping注解或者PostMapping注解，应该进一步提供灵活性
                throw new RuntimeException("目前权限管理应该在method的前面使用GetMapping注解或者PostMapping注解");
            }
        }
        return permissions;
    }

    public static Set<String> listPermissionString(List<Permission> permissions) {
        Set<String> permissionsString = new HashSet<>();
        for (Permission permission : permissions) {
            permissionsString.add(permission.getApi());
        }
        return permissionsString;
    }
}
