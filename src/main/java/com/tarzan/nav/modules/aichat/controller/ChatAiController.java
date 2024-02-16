package com.tarzan.nav.modules.aichat.controller;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.model.ChatItem;
import com.tarzan.nav.modules.aichat.service.ChatAiServiceFactory;
import com.tarzan.nav.modules.aichat.service.ChatItemService;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author tarzan
 */
@Slf4j
@RestController
public class ChatAiController {

    @Resource
    private ChatAiServiceFactory chatAiServiceFactory;
    @Resource
    private ChatItemService chatItemService;

    @PostMapping(path = "/chat/{userId}/{aiType}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatRecordsVo> chat(@PathVariable("userId")Integer userId, @PathVariable("aiType") String aiType, String qa) {
        AISourceEnum source = aiType == null ? AISourceEnum.YI_34B_CHAT : AISourceEnum.valueOf(aiType);
       return  chatAiServiceFactory.getChatService(source).chatStream(userId,qa);
    }

    @GetMapping(path = "/chat/history/{userId}/{aiType}")
    public ChatRecordsVo getChatHistory(@PathVariable("userId")Integer userId, @PathVariable("aiType") String aiType) {
        AISourceEnum source = aiType == null ? AISourceEnum.YI_34B_CHAT : AISourceEnum.valueOf(aiType);
        return  chatAiServiceFactory.getChatService(source).getChatHistory(userId);
    }

    @GetMapping(path = "/chat/{chatUid}")
    public ChatItem chatShare(@PathVariable("chatUid")String chatUid) {
        return  chatItemService.getChatByUid(chatUid);
    }
}
