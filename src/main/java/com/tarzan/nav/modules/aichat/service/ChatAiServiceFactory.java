package com.tarzan.nav.modules.aichat.service;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.service.impl.wenxin.YiChatAiServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tarzan
 * @date 2023/7/2
 */
@Component
public class ChatAiServiceFactory {
    private final Map<AISourceEnum, ChatAiService> chatAiServiceMap;
    @Resource
    private YiChatAiServiceImpl yiChatAiService;


    public ChatAiServiceFactory(List<ChatAiService> chatServiceList) {
        chatAiServiceMap = new HashMap<>(chatServiceList.size());
        for (ChatAiService chatAiService : chatServiceList) {
            chatAiServiceMap.put(chatAiService.source(), chatAiService);
        }
    }

    public ChatAiService getChatService(AISourceEnum aiSource) {
        return chatAiServiceMap.getOrDefault(aiSource,yiChatAiService);
    }
}
