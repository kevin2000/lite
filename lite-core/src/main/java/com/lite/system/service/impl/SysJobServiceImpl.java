package com.lite.system.service.impl;

import com.lite.system.entity.SysJob;
import com.lite.system.mapper.SysJobMapper;
import com.lite.system.service.ISysJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务调度表 服务实现类
 * </p>
 *
 * @author joe
 * @since 2020-07-30
 */
@Service
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements ISysJobService {

}
