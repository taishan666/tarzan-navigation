package com.tarzan.nav.modules.aichat.vo;

import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
import com.tarzan.nav.modules.aichat.model.ChatItem;
import com.tarzan.nav.utils.DateUtil;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

/**
 * 一次qa的聊天记录
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Data
@Accessors(chain = true)
public class ChatItemVo extends ChatItem {

    /**
     * 记录问题及记录时间
     *
     * @param question
     * @return
     */
    public ChatItemVo initQuestion(String question) {
        super.setQuestion(question);
        super.setQuestionTime(DateUtil.now());
        return this;
    }

    /**
     * 记录返回结果及回答时间
     *
     * @param answer
     * @return
     */
    public ChatItemVo initAnswer(String answer) {
        this.setAnswer(answer);
        this.setAnswerType(ChatAnswerTypeEnum.JSON);
        setAnswerTime();
        return this;
    }

    public ChatItemVo initAnswer(String answer, ChatAnswerTypeEnum answerType) {
        this.setAnswer(answer);
        this.setAnswerType(answerType);
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
        if (super.getAnswer() == null || super.getAnswer().isEmpty()) {
            super.setAnswer(answer);
            super.setChatUid(UUID.randomUUID().toString().replaceAll("-", ""));
        } else {
            String lastAnswer=super.getAnswer();
            super.setAnswer(lastAnswer+answer);
        }
        this.setAnswerType(ChatAnswerTypeEnum.STREAM);
        setAnswerTime();
        return this;
    }

    public ChatItemVo streamAnswer(String answer) {
        if (super.getAnswer() == null || super.getAnswer().isEmpty()) {
            super.setAnswer(answer);
            super.setChatUid(UUID.randomUUID().toString().replaceAll("-", ""));
        } else {
            super.setAnswer(answer);
        }
        this.setAnswerType(ChatAnswerTypeEnum.STREAM);
        setAnswerTime();
        return this;
    }

    public ChatItemVo setAnswerTime() {
        this.setAnswerTime(DateUtil.now());
        return this;
    }

}
