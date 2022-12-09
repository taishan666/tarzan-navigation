package com.tarzan.navigation.common.event;

import com.tarzan.navigation.modules.admin.model.log.LoginLog;
import org.springframework.context.ApplicationEvent;

/**
 * 登录日志事件
 *
 * @author tarzan
 * @version 1.0
 * @date 2021年07月19日 09:41:28
 */
public class LoginLogEvent extends ApplicationEvent {

    public LoginLogEvent(LoginLog source) {
        super(source);
    }
}
