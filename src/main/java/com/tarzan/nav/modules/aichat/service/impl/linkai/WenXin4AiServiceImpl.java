package com.tarzan.nav.modules.aichat.service.impl.linkai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
@Service
public class WenXin4AiServiceImpl extends AbsLinkAiService {

    @Value("${linkai.wenxin4.day-limit}")
    private Integer dayLimit;


    @Override
    public AISourceEnum source() {
        return AISourceEnum.WEI_XIN_4;
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return dayLimit==null?super.getMaxQaCnt(userId):dayLimit;
    }

}
