package com.lite.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.lite.core.entity.SysMenu;
import com.lite.core.service.SysMenuService;
import com.lite.core.utils.tree.Tree;

@Controller
public class HomeController {
	@Autowired
	private SysMenuService menuService;
	@GetMapping("/")
	public String index(Model model) {
		List<Tree<SysMenu>> menus = menuService.listMenuTree(null);
        model.addAttribute("menus", menus);
        model.addAttribute("name", "Admin");
		/*
		 * FileDO fileDO = fileService.get(getUser().getPicId()); if (fileDO != null &&
		 * fileDO.getUrl() != null) { if (fileService.isExist(fileDO.getUrl())) {
		 * model.addAttribute("picUrl", fileDO.getUrl()); } else {
		 * model.addAttribute("picUrl", "/img/photo_s.jpg"); } } else {
		 */
            model.addAttribute("picUrl", "/img/photo_s.jpg");
        //}
        model.addAttribute("username", "Admin");
		return "index";
	}
	
	
	@GetMapping("/main")
    String main() {
        return "main";
    }
}
