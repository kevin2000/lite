package com.lite.system.service.impl;

import com.lite.system.entity.SysJobLog;
import com.lite.system.mapper.SysJobLogMapper;
import com.lite.system.service.ISysJobLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度日志表 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@Service
public class SysJobLogServiceImpl extends ServiceImpl<SysJobLogMapper, SysJobLog> implements ISysJobLogService {

}
