package com.tarzan.nav.modules.aichat.service.impl.xunfei;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * @author tarzan
 */
@Service
public class XunFeiChatAiServiceImpl extends AbsChatAiService {


    @Resource
    private XunFeiIntegration xunFeiIntegration;

    @Override
    public AISourceEnum source() {
        return AISourceEnum.XUN_FEI_AI;
    }

    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(Integer userId, ChatRecordsVo response) {
        XunFeiChatWrapper chat = new XunFeiChatWrapper(xunFeiIntegration,String.valueOf(userId), response);
        chat.initAndQuestion();
       return chat.getListener().getMessageFlux().map(data->{
           System.out.println(data);
           return response;
       });
    }




}
