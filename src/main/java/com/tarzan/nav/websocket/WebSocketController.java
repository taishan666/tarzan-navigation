package com.tarzan.nav.websocket;

import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * websocket 测试
 *
 * @author caichengzhe
 * @version 1.0
 * @company 虎的一比有限责任公司
 * @copyright (c) 2021 Niubility Tiger Co.LTD.All rights reserved.
 * @date 2021年07月08日 11:13:57
 * @since JDK1.8
 */
@Controller
@AllArgsConstructor
public class WebSocketController {


    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 方法描述: 接收客户端信息，并给出反馈
     *
     * @param message
     * @Return {@link String}
     * @author tarzan
     * @date 2024年01月22日 11:20:59
     */
    @MessageMapping("/server/test")
    @SendTo("/topic/broadcast")
    public String broadcast(String message) {
        System.out.println(message);
        return "2";
    }

/*    @Scheduled(cron = "0/1 * * * * ?")
    public void  exec(){
        System.out.println("websocket ceshi");
        Message<String> message = new GenericMessage<>("测试");
        messagingTemplate.send("/topic/broadcast1",message);
        messagingTemplate.convertAndSend("/topic/broadcast1",true);
    }*/




}
