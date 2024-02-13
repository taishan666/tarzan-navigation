package com.tarzan.nav.modules.aichat.service.impl.wenxin;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
@Service
public class YiYanChatAiServiceImpl extends AbsWenXinAiService {


    @Override
    public AISourceEnum source() {
        return AISourceEnum.EB_INSTANT;
    }

    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(Integer userId, ChatRecordsVo response) {
        return super.doAsyncAnswer(userId,response,"eb-instant");
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 50;
    }
}
