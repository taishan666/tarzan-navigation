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
     * 这里定义的是客户端接收服务端消息的相关信息，如派聪明的回答： WsAnswerHelper#response 就是往 "/chat/rsp" 发送消息
     * 对应的前端订阅的也是 chat/index.html: stompClient.subscribe(`/user/chat/rsp`, xxx)
     *
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 开启一个简单的基于内存的消息代理，前缀是/user的将消息会转发给消息代理 broker
        // 然后再由消息代理，将消息广播给当前连接的客户端
        registry.enableSimpleBroker("/chat","/topic","/queue");

        // 表示配置一个或多个前缀，通过这些前缀过滤出需要被注解方法处理的消息。
        // 例如，前缀为 /app 的 destination 可以通过@MessageMapping注解的方法处理，
        // 而其他 destination （例如 /topic /queue）将被直接交给 broker 处理
        registry.setApplicationDestinationPrefixes("/app");
        // 客户端给服务端发消息的地址前缀 启用这个后@MessageMapping 不生效了
        //  registry.setApplicationDestinationPrefixes("/server");
        //服务器到特定客户端的映射前缀
        registry.setUserDestinationPrefix("/user");
    }

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
       // registry.addEndpoint("/simple").withSockJS();
        // 注册一个 /gpt/{id} 的 WebSocket endPoint; 其中 {id} 用于让用户连接终端时都可以有自己的路径
        // 作为 Principal 的标识，以便实现向指定用户发送信息
        // sockjs 可以解决浏览器对 WebSocket 的兼容性问题，
        registry.addEndpoint("/gpt/{id}/{aiType}")
                .setHandshakeHandler(new AuthHandshakeHandler())
                .addInterceptors(new AuthHandshakeInterceptor())
        // 注意下面这个，不要使用 setAllowedOrigins("*")，使用之后有啥问题可以实操验证一下🐕
        // setAllowedOrigins接受一个字符串数组作为参数，每个元素代表一个允许访问的客户端地址，内部的值为具体的 "http://localhost:8080"
        // setAllowedOriginPatterns接受一个正则表达式数组作为参数，每个元素代表一个允许访问的客户端地址的模式, 内部值可以为正则，如 "*", "http://*:8080"
                .setAllowedOrigins("*");
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



}
