package com.tarzan.nav.modules.admin.service.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.admin.entity.log.LogErrorEntity;
import com.tarzan.nav.modules.admin.mapper.log.LogErrorMapper;
import com.tarzan.nav.modules.admin.model.log.LogError;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
public class LogErrorService extends ServiceImpl<LogErrorMapper, LogErrorEntity> {
}
