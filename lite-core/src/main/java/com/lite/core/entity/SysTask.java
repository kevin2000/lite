package com.lite.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class SysTask implements Serializable {

    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 任务调用的方法名
     */
    private String methodName;

    /**
     * 任务是否有状态
     */
    private String isConcurrent;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 更新者
     */
    private String modifier;

    /**
     * 任务执行时调用哪个类的方法 包名+类名
     */
    private String beanClass;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 任务状态
     */
    private String jobStatus;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 更新时间
     */
    private LocalDateTime mofifyTime;

    /**
     * 创建者
     */
    private String creator;

    /**
     * Spring bean
     */
    private String springBean;

    /**
     * 任务名
     */
    private String jobName;


}
