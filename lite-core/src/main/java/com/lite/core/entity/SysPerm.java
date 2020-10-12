package com.lite.core.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author joe
 * @since 2020-10-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysPerm implements Serializable {

    private static final long serialVersionUID=1L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private String url;

    private String method;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
