package com.tarzan.nav.modules.aichat.service.impl.linkai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
@Service
public class WenXin3_5AiServiceImpl extends AbsLinkAiService {
    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(ChatRecordsVo response) {
        return super.doAsyncAnswer(response,source().getName());
    }

    @Override
    public AISourceEnum source() {
        return AISourceEnum.WEI_XIN_3_5;
    }
}
