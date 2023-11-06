package com.tarzan.nav.websocket;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * web socket 配置类
 *
 * @author caichengzhe
 * @version 1.0
 * @company 虎的一比有限责任公司
 * @copyright (c) 2021 Niubility Tiger Co.LTD.All rights reserved.
 * @date 2021年07月08日 09:58:59
 * @since JDK1.8
 */
@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 方法描述: 端点配置
     *
     * @param registry
     * @Return
     * @author caichengzhe
     * @date 2021年07月08日 10:43:55
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/simple").withSockJS();
    }


    /**
     * 方法描述: 消息传输格式配置
     *
     * @param registry
     * @Return
     * @author caichengzhe
     * @date 2021年07月08日 10:43:38
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        //500MB
        registry.setMessageSizeLimit(500 * 1024 * 1024);
        //1G
        registry.setSendBufferSizeLimit(1024 * 1024 * 1024);
        //20s
        registry.setSendTimeLimit(20000);
    }


    /**
     * 方法描述: 入界通道拦截，此处用来进行身份验证与用户信息设置
     *
     * @param registration
     * @Return
     * @author caichengzhe
     * @date 2021年07月08日 10:43:15
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
/*        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader(WebSocketConstant.WEBSOCKET_HEADER_TOKEN);
                    String userId = accessor.getFirstNativeHeader(WebSocketConstant.WEBSOCKET_HEADER_USER_ID);
                    if (StringUtil.isAnyBlank(token, userId)) {
                        return null;
                    }
                    //进行双重校验，两参数非空，且token中解析的用户id应于header传递的一致，否则视为非法请求
                    //websocket此处不再进行登录功能，均采用jwttoken进行身份验证
                    BladeUser bu = AuthUtil.getUser(token);
                    if (Objects.isNull(bu) || !StringUtil.equals(String.valueOf(bu.getUserId()), userId)) {
                        return null;
                    }
                    log.info("用户：" + bu.getRealName() + "; userId:" + bu.getUserId() + " 连接成功");
                    accessor.setUser(() -> userId);
                }
                return message;
            }
        });*/
    }


    /**
     * 方法描述: 出界通道拦截配置，此处用来进行日志信息收集
     *
     * @param registration
     * @Return
     * @author caichengzhe
     * @date 2021年07月08日 10:44:06
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
                log.info("发出消息：" + JSON.toJSONString(message.getPayload()) + ";发送结果：" + (sent ? "成功" : "失败") + ";消息通道：" + channel);
            }
        });
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端接收服务器消息的地址前缀
        registry.enableSimpleBroker("/topic","/queue");
        // 客户端给服务端发消息的地址前缀 启用这个后@MessageMapping 不生效了
      //  registry.setApplicationDestinationPrefixes("/server");
        //服务器到特定客户端的映射前缀
        registry.setUserDestinationPrefix("/user");
    }
}
