package com.tarzan.nav.modules.aichat.service.impl.linkai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
public class GeminiAiServiceImpl extends AbsLinkAiService {

    @Override
    public AISourceEnum source() {
        return AISourceEnum.GEMINI;
    }

}
