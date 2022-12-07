package com.tarzan.nacigation.modules.admin.service.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nacigation.modules.admin.mapper.log.LogErrorMapper;
import com.tarzan.nacigation.modules.admin.model.log.LogError;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
public class LogErrorService extends ServiceImpl<LogErrorMapper, LogError> {
}
