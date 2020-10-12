package com.lite.core.service;

import com.lite.core.entity.SysDept;
import com.lite.core.mapper.SysDeptMapper;
import com.lite.core.utils.tree.BuildTree;
import com.lite.core.utils.tree.Tree;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
@Service
public class SysDeptService extends ServiceImpl<SysDeptMapper, SysDept> {
	@Autowired
	private SysUserService userService;
	//部门根节点id
    public static Long DEPT_ROOT_ID = 0l;
    
    public Tree<SysDept> getTree() {
        List<Tree<SysDept>> trees = new ArrayList<Tree<SysDept>>();
        List<SysDept> sysDepts = list();
        for (SysDept sysDept : sysDepts) {
            Tree<SysDept> tree = new Tree<SysDept>();
            tree.setId(sysDept.getDeptId().toString());
            tree.setParentId(sysDept.getParentId().toString());
            tree.setText(sysDept.getName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            tree.setState(state);
            trees.add(tree);
        }
        // 默认顶级菜单为０，根据数据库实际情况调整
        Tree<SysDept> t = BuildTree.build(trees);
        return t;
    }
    
        
    public boolean checkDeptHasUser(Long deptId) {
        // TODO Auto-generated method stub
        //查询部门以及此部门的下级部门
        int result = userService.getDeptUserNumber(deptId);
        return result == 0 ? true : false;
    }

    public List<Long> listChildrenIds(Long parentId) {
        List<SysDept> SysDeptS = list();
        return treeMenuList(SysDeptS, parentId);
    }

    List<Long> treeMenuList(List<SysDept> menuList, long pid) {
        List<Long> childIds = new ArrayList<>();
        for (SysDept mu : menuList) {
            //遍历出父id等于参数的id，add进子节点集合
            if (mu.getParentId() == pid) {
                //递归遍历下一级
                treeMenuList(menuList, mu.getDeptId());
                childIds.add(mu.getDeptId());
            }
        }
        return childIds;
    }
}
