package com.lite.core.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class SysDic implements Serializable {

    private static final long serialVersionUID=1L;

    private String code;

    private String status;

    /**
     * table,option
     */
    private String type;

    private String table;

    private String codeField;

    private String nameField;

    private String filter;

    /**
     * e.g names asc, sn desc
     */
    private String sort;
    
    private String remark;

    private Long creator;

    private LocalDateTime createTime;

    private Long modifier;

    private LocalDateTime modifyTime;


}
