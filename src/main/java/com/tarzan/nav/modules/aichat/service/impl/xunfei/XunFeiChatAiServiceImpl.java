package com.tarzan.nav.modules.aichat.service.impl.xunfei;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatAnswerVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author tarzan
 */
@Service
public class XunFeiChatAiServiceImpl extends AbsChatAiService {

    @Value("${xunfei.day-limit}")
    private Integer dayLimit;
    @Resource
    private XunFeiIntegration xunFeiIntegration;

    @Override
    public AISourceEnum source() {
        return AISourceEnum.XUN_FEI_3_5;
    }

    @Override
    public Flux<ChatAnswerVo> doAsyncAnswer(String question,ChatAnswerVo answerVo) {
        XunFeiChatWrapper chat = new XunFeiChatWrapper(xunFeiIntegration,question,answerVo);
        chat.initAndQuestion();
       return chat.getListener().getMessageFlux();
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return dayLimit==null?super.getMaxQaCnt(userId):dayLimit;
    }


}
