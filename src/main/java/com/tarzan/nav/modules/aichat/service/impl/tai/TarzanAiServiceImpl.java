package com.tarzan.nav.modules.aichat.service.impl.tai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * 泰聪明价值一个亿的AI
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Service
@Slf4j
public class TarzanAiServiceImpl extends AbsChatAiService {


    @Override
    public AISourceEnum source() {
        return AISourceEnum.TARZAN_AI;
    }


    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(Integer userId, ChatRecordsVo response) {
        return null;
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
