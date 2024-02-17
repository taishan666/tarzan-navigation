package com.tarzan.nav.modules.aichat.service;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatAnswerVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
public interface ChatAiService {

    /**
     * 具体AI选择
     *
     * @return
     */
    AISourceEnum source();

    /**
     * 流式对话
     *
     * @param msg
     * @param userId
     * @return {@link Flux<String>}
     */
    Flux<ChatAnswerVo> chatStream(Integer userId, String msg);


    /**
     * 查询聊天历史
     *
     * @param userId
     * @return
     */
    ChatRecordsVo getChatHistory(Integer userId);
}
