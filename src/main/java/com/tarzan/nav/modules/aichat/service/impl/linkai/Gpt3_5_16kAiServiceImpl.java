package com.tarzan.nav.modules.aichat.service.impl.linkai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
public class Gpt3_5_16kAiServiceImpl extends AbsLinkAiService {

    @Override
    public AISourceEnum source() {
        return AISourceEnum.CHAT_GPT_3_5_16K;
    }
}
