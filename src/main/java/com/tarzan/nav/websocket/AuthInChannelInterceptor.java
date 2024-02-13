package com.tarzan.nav.websocket;

import com.tarzan.nav.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.security.Principal;

/**
 * 权限拦截器，消息发送前进行拦截
 *
 * @author YiHui
 * @date 2023/6/8
 */
@Slf4j
public class AuthInChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }

        String destination = accessor.getDestination();
        if (StringUtils.isBlank(destination)) {
            return message;
        }


        Principal uid = accessor.getUser();
        if (uid == null) {
            return message;
        }

        // 正常登录的用户，这个uid实际上应该是 ReqInfo 对象
        log.info("初始化用户标识：{}", uid);
        return ChannelInterceptor.super.preSend(message, channel);
    }


    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StringUtils.equalsIgnoreCase(String.valueOf(message.getHeaders().get("simpMessageType")), "SUBSCRIBE")
                && accessor != null && accessor.getUser() != null) {
            // 订阅成功，返回用户历史聊天记录； 从请求头中，获取具体选择的大数据模型
            AuthUtil.addReqInfo((AuthUtil.ReqInfo) accessor.getUser());
            //AuthUtil.clear();
            return;
        }
        ChannelInterceptor.super.postSend(message, channel, sent);
    }

    @Override
    public boolean preReceive(MessageChannel channel) {
        log.info("preReceive!");
        return ChannelInterceptor.super.preReceive(channel);
    }

    @Override
    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
        log.info("postReceive");
        return ChannelInterceptor.super.postReceive(message, channel);
    }
}
