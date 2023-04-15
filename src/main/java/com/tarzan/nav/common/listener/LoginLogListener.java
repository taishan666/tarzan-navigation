package com.tarzan.nav.common.listener;

import com.tarzan.nav.common.event.LoginLogEvent;
import com.tarzan.nav.modules.admin.model.log.LoginLog;
import com.tarzan.nav.modules.admin.service.log.LoginLogService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 登录日志事件监听
 *
 * @author tarzan
 * @version 1.0
 * @date 2021年04月19日 09:42:36
 */
@Slf4j
@AllArgsConstructor
@Component
public class LoginLogListener {

    private LoginLogService loginLogService;

    @Async
    @Order
    @EventListener(LoginLogEvent.class)
    public void saveLoginLog(LoginLogEvent event) {
        LoginLog loginLog = (LoginLog) event.getSource();
        if (!Objects.isNull(loginLog)) {
            if (Objects.isNull(loginLog.getId())) {
                loginLogService.save(loginLog);
            } else {
                loginLogService.updateById(loginLog);
            }
        }
    }
}
