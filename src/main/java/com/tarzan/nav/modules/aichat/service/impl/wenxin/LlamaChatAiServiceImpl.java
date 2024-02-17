package com.tarzan.nav.modules.aichat.service.impl.wenxin;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatAnswerVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
@Service
public class LlamaChatAiServiceImpl extends AbsWenXinAiService {

    @Value("${wenxin.llama.day-limit}")
    private Integer dayLimit;

    @Override
    public AISourceEnum source() {
        return AISourceEnum.LLAMA_2_7B;
    }


    @Override
    protected int getMaxQaCnt(Integer userId) {
        return dayLimit==null?super.getMaxQaCnt(userId):dayLimit;
    }

}
