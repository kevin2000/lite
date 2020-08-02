package com.lite.system.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色和菜单关联表
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 菜单ID
     */
    private Long menuId;


}
