package com.tarzan.nacigation.common.event;

import com.tarzan.nacigation.modules.admin.model.log.LogError;
import org.springframework.context.ApplicationEvent;

/**
 * @author tarzan
 */
public class ErrorLogEvent extends ApplicationEvent {
    public ErrorLogEvent(LogError source) {
        super(source);
    }
}
