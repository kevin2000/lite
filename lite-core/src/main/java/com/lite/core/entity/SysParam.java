package com.lite.core.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author joe
 * @since 2020-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysParam implements Serializable {

    private static final long serialVersionUID=1L;

    private String code;

    private String name;

    private String value;

    private String groupCode;

    private String remark;

    private String status;

    private String approveStatus;

    private String creator;

    private LocalDateTime createTime;

    private String modifier;

    private LocalDateTime modifyTime;


}
