package com.tarzan.nav.modules.aichat.service.impl.tai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import com.tarzan.nav.utils.AsyncUtil;
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
        return AISourceEnum.TARZAN_AI;
    }

    @Override
    public AiChatStatEnum doAnswer(Integer userId, ChatItemVo chat) {
        return AiChatStatEnum.IGNORE;
    }

    @Override
    public AiChatStatEnum doAsyncAnswer(Integer userId, ChatRecordsVo response, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        ChatItemVo item = response.getRecords().get(0);
        AsyncUtil.execute(() -> {
            AsyncUtil.sleep(1000);
            item.appendAnswer("\n" + qa(item.getQuestion()));
            item.setAnswerType(ChatAnswerTypeEnum.STREAM_END);
            consumer.accept(AiChatStatEnum.END, response);
        });
        return AiChatStatEnum.IGNORE;
    }

    private String qa(String q) {
        String ans = "洛阳泰山技术博客 https://tarzan.blog.csdn.net/";
        return ans;
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 10;
    }
}
