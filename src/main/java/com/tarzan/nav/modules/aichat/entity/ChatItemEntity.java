package com.tarzan.nav.modules.aichat.entity;

import com.tarzan.nav.common.constant.TableConst;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Lenovo
 */
@Data
@Table(name= TableConst.CHAT_ITEM)
@Entity
public class ChatItemEntity {

    /**
     * 唯一的聊天id，
     */
    @Id
    @Column(length = 50)
    private String chatUid;
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * AI 类型
     */
    private Integer aiType;

    /**
     * 提问的内容
     */
    @Column(columnDefinition="text")
    private String question;

    /**
     * 提问的时间点
     */
    private String questionTime;

    /**
     * 回答内容
     */
    @Column(columnDefinition="text")
    private String answer;

    /**
     * 回答的时间点
     */
    private String answerTime;

    /**
     * 回答的内容类型，文本、JSON 字符串
     */
    @Column(name = "answer_type")
    private Integer answer_type;
}
