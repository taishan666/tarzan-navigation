package com.tarzan.nav.modules.aichat.service;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tarzan
 * @date 2023/7/2
 */
@Component
public class ChatServiceFactory {
    private final Map<AISourceEnum, ChatService> chatServiceMap;


    public ChatServiceFactory(List<ChatService> chatServiceList) {
        chatServiceMap = new HashMap<>(chatServiceList.size());
        for (ChatService chatService : chatServiceList) {
            chatServiceMap.put(chatService.source(), chatService);
        }
    }

    public ChatService getChatService(AISourceEnum aiSource) {
        return chatServiceMap.get(aiSource);
    }
}
