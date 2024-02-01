package com.tarzan.nav.modules.admin.controller.sys;

import com.tarzan.nav.modules.admin.service.biz.MatterService;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
public class MatterController {

    private final MatterService matterService;


    @PostMapping("/todo")
    public ResponseVo loadNotify() {
        return ResultUtil.vo(matterService.todoItems());
    }

    @MessageMapping("/server/test")
    @SendTo("/topic/broadcast")
    public String broadcast(String message) {
        System.out.println(message);
        return "OK";
    }

}
