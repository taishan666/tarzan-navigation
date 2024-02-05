package com.tarzan.nav.modules.aichat.service;


import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;

import java.util.function.Consumer;

/**
 * @author tarzan
 * @date 2023/6/9
 */
public interface ChatService {

    /**
     * 具体AI选择
     *
     * @return
     */
    AISourceEnum source();

    /**
     * 是否时异步优先
     *
     * @return
     */
    default boolean asyncFirst() {
        return true;
    }

    /**
     * 开始进入聊天
     *
     * @param userId     提问人
     * @param question 聊天的问题
     * @return 返回的结果
     */
    ChatRecordsVo chat(Integer userId, String question);

    /**
     * 开始进入聊天
     *
     * @param userId     提问人
     * @param question 聊天的问题
     * @param consumer 接收到AI返回之后可执行的回调
     * @return 同步直接返回的结果
     */
    ChatRecordsVo chat(Integer userId, String question, Consumer<ChatRecordsVo> consumer);

    /**
     * 异步聊天
     *
     * @param userId
     * @param question
     * @param consumer 执行成功之后，直接异步回调的通知
     * @return 同步直接返回的结果
     */
    ChatRecordsVo asyncChat(Integer userId, String question, Consumer<ChatRecordsVo> consumer);


    /**
     * 查询聊天历史
     *
     * @param userId
     * @param aiSource
     * @return
     */
    ChatRecordsVo getChatHistory(Integer userId, AISourceEnum aiSource);

}
