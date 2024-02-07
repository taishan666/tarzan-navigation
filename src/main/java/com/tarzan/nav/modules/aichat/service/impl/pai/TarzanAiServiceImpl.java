package com.tarzan.nav.modules.aichat.service.impl.pai;

import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.aigc.generation.GenerationOutput;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
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
public class TarzanAiServiceImpl extends AbsChatService {

    @Override
    public AISourceEnum source() {
        return AISourceEnum.PAI_AI;
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
                .apiKey("sk-80611638022146a2bc9a1ec9b566cc54")
                .build();
        try{
            Flowable<ConversationResult> result = conversation.streamCall(param);
            result.blockingForEach(msg->{
                GenerationOutput out= msg.getOutput();
                item.stramAnswer(out.getText());
                if("stop".equals(out.getFinishReason())){
                    System.out.println("stop");
                    consumer.accept(AiChatStatEnum.END, response);
                }else {
                    consumer.accept(AiChatStatEnum.FIRST, response);
                }
            });
        }catch(ApiException|InputRequiredException|NoApiKeyException ex){
            log.error(ex.getMessage());
        }
        return AiChatStatEnum.IGNORE;
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 100;
    }
}
