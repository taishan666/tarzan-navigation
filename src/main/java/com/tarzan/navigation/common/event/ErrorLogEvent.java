package com.tarzan.navigation.common.event;

import com.tarzan.navigation.modules.admin.model.log.LogError;
import org.springframework.context.ApplicationEvent;

/**
 * @author tarzan
 */
public class ErrorLogEvent extends ApplicationEvent {
    public ErrorLogEvent(LogError source) {
        super(source);
    }
}
