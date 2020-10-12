package com.lite.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author joe
 * @since 2020-09-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysRole implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色标识
     */
    private String roleSign;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建用户id
     */
    private Long userIdCreate;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 创建时间
     */
    private LocalDateTime gmtModified;


}
