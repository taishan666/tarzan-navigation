package com.tarzan.nav.modules.aichat.service.impl.xunfei;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.BiConsumer;

/**
 * 讯飞星火大模型
 * <a href="https://www.xfyun.cn/doc/spark/Web.html#_1-%E6%8E%A5%E5%8F%A3%E8%AF%B4%E6%98%8E"/>
 *
 * @author tarzan
 * @date 2023/6/12
 */
@Slf4j
@Service
public class XunFeiAiServiceImpl extends AbsChatService {


    @Resource
    private XunFeiIntegration xunFeiIntegration;

    /**
     * 不支持同步提问
     *
     * @param userId
     * @param chat
     * @return
     */
    @Override
    public AiChatStatEnum doAnswer(Integer userId, ChatItemVo chat) {
        return AiChatStatEnum.IGNORE;
    }

    /**
     * 异步回答提问
     *
     * @param userId
     * @param chatRes  保存提问 & 返回的结果，最终会返回给前端用户
     * @param consumer 具体将 response 写回前端的实现策略
     */
    @Override
    public AiChatStatEnum doAsyncAnswer(Integer userId, ChatRecordsVo chatRes, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        XunFeiChatWrapper chat = new XunFeiChatWrapper(xunFeiIntegration,String.valueOf(userId), chatRes, consumer);
        chat.initAndQuestion();
        return AiChatStatEnum.IGNORE;
    }

    @Override
    public AISourceEnum source() {
        return AISourceEnum.XUN_FEI_AI;
    }


    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 10;
    }


}
