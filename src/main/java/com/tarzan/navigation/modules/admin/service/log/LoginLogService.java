package com.tarzan.navigation.modules.admin.service.log;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.modules.admin.mapper.log.LoginLogMapper;
import com.tarzan.navigation.modules.admin.model.log.LoginLog;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
public class LoginLogService extends ServiceImpl<LoginLogMapper, LoginLog> {
}
