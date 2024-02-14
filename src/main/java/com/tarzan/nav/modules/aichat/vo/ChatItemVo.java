package com.tarzan.nav.modules.aichat.vo;

import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
import com.tarzan.nav.utils.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

/**
 * 一次qa的聊天记录
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Data
@Accessors(chain = true)
public class ChatItemVo implements Serializable, Cloneable {
    private static final long serialVersionUID = 7230339040247758226L;
    /**
     * 唯一的聊天id，不要求存在，主要用于简化流式输出时，前端对返回结果的处理
     */
    private String chatUid;

    /**
     * 提问的内容
     */
    private String question;

    /**
     * 提问的时间点
     */
    private String questionTime;

    /**
     * 回答内容
     */
    private String answer;

    /**
     * 回答的时间点
     */
    private String answerTime;

    /**
     * 回答的内容类型，文本、JSON 字符串
     */
    private ChatAnswerTypeEnum answerType;

    /**
     * 记录问题及记录时间
     *
     * @param question
     * @return
     */
    public ChatItemVo initQuestion(String question) {
        this.question = question;
        this.questionTime = DateUtil.getNewFormatDateString(DateUtil.now());
        return this;
    }

    /**
     * 记录返回结果及回答时间
     *
     * @param answer
     * @return
     */
    public ChatItemVo initAnswer(String answer) {
        this.answer = answer;
        this.answerType = ChatAnswerTypeEnum.JSON;
        setAnswerTime();
        return this;
    }

    public ChatItemVo initAnswer(String answer, ChatAnswerTypeEnum answerType) {
        this.answer = answer;
        this.answerType = answerType;
        setAnswerTime();
        return this;
    }

    /**
     * 流式的追加返回
     *
     * @param answer
     * @return
     */
    public ChatItemVo appendAnswer(String answer) {
        if (this.answer == null || this.answer.isEmpty()) {
            this.answer = answer;
            this.chatUid = UUID.randomUUID().toString().replaceAll("-", "");
        } else {
            this.answer += answer;
        }
        this.answerType = ChatAnswerTypeEnum.STREAM;
        setAnswerTime();
        return this;
    }

    public ChatItemVo stramAnswer(String answer) {
        if (this.answer == null || this.answer.isEmpty()) {
            this.answer = answer;
            this.chatUid = UUID.randomUUID().toString().replaceAll("-", "");
        } else {
            this.answer = answer;
        }
        this.answerType = ChatAnswerTypeEnum.STREAM;
        setAnswerTime();
        return this;
    }

    public ChatItemVo setAnswerTime() {
        this.answerTime = DateUtil.getNewFormatDateString(DateUtil.now());
        return this;
    }

    @Override
    public ChatItemVo clone() {
        ChatItemVo item = new ChatItemVo();
        item.question = question;
        item.questionTime = questionTime;
        return item;
    }
}
