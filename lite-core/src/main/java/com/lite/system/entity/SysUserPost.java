package com.lite.system.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户与岗位关联表
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserPost implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;


}
