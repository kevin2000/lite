package com.lite.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.lite.core.codeGenerator.annotation.Comment;

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
public class SysApi implements Serializable {

    private static final long serialVersionUID=1L;

    @Comment("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String url;

    @Comment("方法")
    private String method;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
