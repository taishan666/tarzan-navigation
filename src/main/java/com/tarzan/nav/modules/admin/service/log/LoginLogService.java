package com.tarzan.nav.modules.admin.service.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.admin.entity.log.LoginLogEntity;
import com.tarzan.nav.modules.admin.mapper.log.LoginLogMapper;
import com.tarzan.nav.modules.admin.model.log.LoginLog;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLogEntity> {
}
