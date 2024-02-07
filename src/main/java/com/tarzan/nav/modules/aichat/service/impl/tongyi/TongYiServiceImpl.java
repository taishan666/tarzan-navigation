package com.tarzan.nav.modules.aichat.service.impl.tongyi;

import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

/**
 * 泰聪明价值一个亿的AI
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Service
@Slf4j
public class TongYiServiceImpl extends AbsChatService {

    @Value("${tongyi.apiKey}")
    private String apiKey;

    @Override
    public AISourceEnum source() {
        return AISourceEnum.TONG_YI_AI;
    }

    @Override
    public AiChatStatEnum doAnswer(Integer userId, ChatItemVo chat) {
        return AiChatStatEnum.IGNORE;
    }

    @Override
    public AiChatStatEnum doAsyncAnswer(Integer userId, ChatRecordsVo response, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
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
            result.blockingForEach(msg -> {
                GenerationOutput out = msg.getOutput();
                item.stramAnswer(out.getText());
                if (CoreConst.STOP.equals(out.getFinishReason())) {
                    consumer.accept(AiChatStatEnum.END, response);
                } else {
                    consumer.accept(AiChatStatEnum.FIRST, response);
                }
            });
        } catch (ApiException | InputRequiredException | NoApiKeyException ex) {
            item.initAnswer("AI返回异常:" + ex.getMessage());
            consumer.accept(AiChatStatEnum.ERROR, response);
            log.error(ex.getMessage());
        }
        return AiChatStatEnum.IGNORE;
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 100;
    }
}
