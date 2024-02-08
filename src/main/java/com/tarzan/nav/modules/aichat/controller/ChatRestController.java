package com.tarzan.nav.modules.aichat.controller;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.helper.WsAnswerHelper;
import com.tarzan.nav.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * STOMP协议的ChatGpt聊天通讯实现方式
 *
 * @author tarzan
 * @date 2023/6/5
 */
@Slf4j
@RestController
public class ChatRestController {
    @Resource
    private WsAnswerHelper answerHelper;

    /**
     * 接收用户发送的消息
     *
     * @param msg
     * @param session
     * @param attrs
     * @return
     * @MessageMapping（"/chat/{session}"）注解的方法将用来接收"/app/chat/xxx路径发送来的消息，<br/> 如果有 @SendTo，则表示将返回结果，转发到其对应的路径上 （这个sendTo的路径，就是前端订阅的路径）
     * @DestinationVariable： 实现路径上的参数解析
     * @Headers 实现请求头格式的参数解析, @Header("headName") 表示获取某个请求头的内容
     */
    @MessageMapping("/chat/{session}")
    public void chat(String msg, @DestinationVariable("session") String session, @Header("simpSessionAttributes") Map<String, Object> attrs) {
        String aiType = (String) attrs.get(WsAnswerHelper.AI_SOURCE_PARAM);
        answerHelper.execute(attrs, () -> {
            log.info("{} 用户开始了对话: {} - {}", AuthUtil.getUsername(), aiType, msg);
            AISourceEnum source = aiType == null ? null : AISourceEnum.valueOf(aiType);
            answerHelper.sendMsgToUser(source, session, msg);
        });
    }
}
