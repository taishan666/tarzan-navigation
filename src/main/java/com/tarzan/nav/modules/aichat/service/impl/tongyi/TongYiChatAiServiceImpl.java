package com.tarzan.nav.modules.aichat.service.impl.tongyi;

import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tarzan
 */
@Service
@Slf4j
public class TongYiChatAiServiceImpl extends AbsChatAiService {

    @Value("${tongyi.apiKey}")
    private String apiKey;

    @Override
    public AISourceEnum source() {
        return AISourceEnum.TONG_YI_AI;
    }


    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(Integer userId, ChatRecordsVo response) {
        ChatItemVo item = response.getRecords().get(0);
        Conversation conversation = new Conversation();
        ConversationParam param = ConversationParam
                .builder()
                .model(Conversation.Models.QWEN_MAX)
                .prompt(item.getQuestion())
                .apiKey(apiKey)
                .build();
        try {
            Flowable<ConversationResult> result = conversation.streamCall(param);
            AtomicReference<String> lastResult= new AtomicReference<>("");
            return Flux.from(result).map(data -> {
                String text=data.getOutput().getText();
                item.stramAnswer(text.replace(lastResult.get(),""));
                lastResult.set(text);
                return response;
            });
        } catch (NoApiKeyException | InputRequiredException e) {
            log.error(e.getMessage());
        }
        return Flux.just(response);
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 20;
    }


}
