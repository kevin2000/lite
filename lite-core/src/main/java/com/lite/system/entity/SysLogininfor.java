package com.lite.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统访问记录
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysLogininfor implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 访问ID
     */
    @TableId(value = "info_id", type = IdType.AUTO)
    private Long infoId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 登录状态（0成功 1失败）
     */
    private String status;

    /**
     * 提示消息
     */
    private String msg;

    /**
     * 访问时间
     */
    private LocalDateTime loginTime;


}
