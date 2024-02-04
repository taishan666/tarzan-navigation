package com.tarzan.nav.modules.aichat.service.impl.pai;

import com.tarzan.nav.modules.aichat.service.AbsChatService;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import com.tarzan.nav.utils.AsyncUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.function.BiConsumer;

/**
 * 技术派价值一个亿的AI
 *
 * @author YiHui
 * @date 2023/6/9
 */
@Service
public class PaiAiDemoServiceImpl extends AbsChatService {

    @Override
    public AISourceEnum source() {
        return AISourceEnum.PAI_AI;
    }

    @Override
    public AiChatStatEnum doAnswer(Integer userId, ChatItemVo chat) {
        chat.initAnswer(qa(chat.getQuestion()));
        return AiChatStatEnum.END;
    }

    @Override
    public AiChatStatEnum doAsyncAnswer(Integer userId, ChatRecordsVo response, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        AsyncUtil.execute(() -> {
            AsyncUtil.sleep(1500);
            ChatItemVo item = response.getRecords().get(0);
            item.appendAnswer(qa(item.getQuestion()));
            consumer.accept(AiChatStatEnum.FIRST, response);

            AsyncUtil.sleep(1200);
            item.appendAnswer("\n" + qa(item.getQuestion()));
            item.setAnswerType(ChatAnswerTypeEnum.STREAM_END);
            consumer.accept(AiChatStatEnum.END, response);
        });
        return AiChatStatEnum.END;
    }

    private String qa(String q) {
        String ans = q.replace("吗", "");
        ans = StringUtils.replace(ans, "？", "!");
        ans = StringUtils.replace(ans, "?", "!");
        return ans;
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 65535;
    }
}
