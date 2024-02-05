package com.tarzan.nav.modules.admin.controller.sys;

import com.tarzan.nav.modules.admin.service.biz.MatterService;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.utils.ResultUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tarzan
 * @version 1.0
 * @since JDK1.8
 */
@RestController
@RequestMapping("/matter")
@AllArgsConstructor
@Slf4j
public class MatterController {

    private final MatterService matterService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/todo")
    public ResponseVo loadNotify() {
        return ResultUtil.vo(matterService.todoItems());
    }

    @MessageMapping("/server/test")
    @SendTo("/topic/broadcast")
    public String broadcast(String message) {
        log.info(message);
        return "OK";
    }

    @MessageMapping("/app/test")
    public void broadcast1(String message) {
        simpMessagingTemplate.convertAndSendToUser("5", "/chat/rsp", "666");
    }

}
