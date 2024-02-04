package com.tarzan.nav.modules.aichat.service.impl.chatgpt;

import com.plexpt.chatgpt.listener.AbstractStreamListener;
import com.tarzan.nav.modules.aichat.service.AbsChatService;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

/**
 * @author tarzan
 * @date 2023/6/12
 */
@Slf4j
@Service
public class ChatGptAiServiceImpl extends AbsChatService {
    @Autowired
    private ChatGptIntegration chatGptIntegration;

    @Override
    public AiChatStatEnum doAnswer(Integer userId, ChatItemVo chat) {
        if (chatGptIntegration.directReturn(userId, chat)) {
            return AiChatStatEnum.END;
        }
        return AiChatStatEnum.ERROR;
    }

    @Override
    public AiChatStatEnum doAsyncAnswer(Integer userId,  ChatRecordsVo chatRes, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        ChatItemVo item = chatRes.getRecords().get(0);
        AbstractStreamListener listener = new AbstractStreamListener() {
            @Override
            public void onMsg(String message) {
                // 成功返回结果的场景
                if (StringUtils.isNotBlank(message)) {
                    item.appendAnswer(message);
                    consumer.accept(AiChatStatEnum.MID, chatRes);
                    if (log.isDebugEnabled()) {
                        log.debug("ChatGpt返回内容: {}", lastMessage);
                    }
                }
            }

            @Override
            public void onError(Throwable throwable, String response) {
                // 返回异常的场景
                item.appendAnswer("Error:" + (StringUtils.isBlank(response) ? throwable.getMessage() : response))
                        .setAnswerType(ChatAnswerTypeEnum.STREAM_END);
                consumer.accept(AiChatStatEnum.ERROR, chatRes);
            }
        };

        // 注册回答结束的回调钩子
        listener.setOnComplate((s) -> {
            item.appendAnswer("\n")
                    .setAnswerType(ChatAnswerTypeEnum.STREAM_END);
            consumer.accept(AiChatStatEnum.END, chatRes);
        });
        chatGptIntegration.streamReturn(userId, item, listener);
        return AiChatStatEnum.IGNORE;
    }

    @Override
    public AISourceEnum source() {
        return AISourceEnum.CHAT_GPT_3_5;
    }

    @Override
    public boolean asyncFirst() {
        // true 表示优先使用异步返回； false 表示同步等待结果
        return true;
    }
}
