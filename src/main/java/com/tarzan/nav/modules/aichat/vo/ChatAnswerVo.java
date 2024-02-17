package com.tarzan.nav.modules.aichat.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天记录
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Data
@Accessors(chain = true)
public class ChatAnswerVo implements Serializable, Cloneable {
    private static final long serialVersionUID = -2666259615985932920L;

    /**
     * 当前用户最多可问答的次数
     */
    private Integer maxCnt;

    /**
     * 使用的次数
     */
    private Integer usedCnt;


    private String chatUid;

    private String answer;

    /**
     * 回答的时间点
     */
    private Date answerTime;

    /**
     * 判断是否拥有提问次数
     *
     * @return true 表示拥有提问次数
     */
    public boolean hasQaCnt() {
        return maxCnt > usedCnt;
    }
}
