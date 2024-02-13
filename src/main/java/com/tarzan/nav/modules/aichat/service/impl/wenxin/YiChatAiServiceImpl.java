package com.tarzan.nav.modules.aichat.service.impl.wenxin;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
@Service
public class YiChatAiServiceImpl extends AbsWenXinAiService {

    @Value("${wenxin.yi_34b_chat.day-limit}")
    private Integer dayLimit;

    @Override
    public AISourceEnum source() {
        return AISourceEnum.YI_34B_CHAT;
    }

    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(Integer userId, ChatRecordsVo response) {
        return super.doAsyncAnswer(userId,response,"yi_34b_chat");
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return dayLimit;
    }

}
