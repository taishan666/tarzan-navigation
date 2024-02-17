package com.tarzan.nav.modules.aichat.service.impl.linkai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import org.springframework.stereotype.Service;

/**
 * @author tarzan
 */
@Service
public class WenXin3_5AiServiceImpl extends AbsLinkAiService {

    @Override
    public AISourceEnum source() {
        return AISourceEnum.WEI_XIN_3_5;
    }
}
