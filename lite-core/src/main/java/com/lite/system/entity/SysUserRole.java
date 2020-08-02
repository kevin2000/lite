package com.lite.system.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户和角色关联表
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserRole implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID
     */
    private Long roleId;


}
