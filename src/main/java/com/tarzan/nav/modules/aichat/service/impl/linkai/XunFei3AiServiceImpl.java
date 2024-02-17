package com.tarzan.nav.modules.aichat.service.impl.linkai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
@Service
public class XunFei3AiServiceImpl extends AbsLinkAiService {

    @Override
    public AISourceEnum source() {
        return AISourceEnum.XUN_FEI_3;
    }
}
