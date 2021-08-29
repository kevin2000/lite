package com.lite.core.service;

import com.lite.core.entity.SysDict;
import com.lite.core.entity.SysUser;
import com.lite.core.mapper.SysDictMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
@Service
public class SysDictService extends ServiceImpl<SysDictMapper, SysDict> {

	public String getName(String type, String value) {
        Map<String, Object> param = new HashMap<String, Object>(16);
        param.put("type", type);
        param.put("value", value);
        String rString = listByMap(param).get(0).getName();
        return rString;
    }

    public List<SysDict> getHobbyList(SysUser userDO) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "hobby");
        List<SysDict> hobbyList = listByMap(param);

        if (StringUtils.isNotEmpty(userDO.getHobby())) {
            String userHobbys[] = userDO.getHobby().split(";");
            for (String userHobby : userHobbys) {
                for (SysDict hobby : hobbyList) {
                    if (!Objects.equals(userHobby, hobby.getId().toString())) {
                        continue;
                    }
                    hobby.setRemark("true");
                    break;
                }
            }
        }

        return hobbyList;
    }

    public List<SysDict> getSexList() {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", "sex");
        return listByMap(param);
    }

    public List<SysDict> listByType(String type) {
        Map<String, Object> param = new HashMap<>(16);
        param.put("type", type);
        return listByMap(param);
    }
}
