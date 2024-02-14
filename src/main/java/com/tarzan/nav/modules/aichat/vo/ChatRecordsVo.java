package com.tarzan.nav.modules.aichat.vo;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 聊天记录
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Data
@Accessors(chain = true)
public class ChatRecordsVo implements Serializable, Cloneable {
    private static final long serialVersionUID = -2666259615985932920L;
    /**
     * AI来源
     */
    private AISourceEnum source;

    /**
     * 当前用户最多可问答的次数
     */
    private int maxCnt;

    /**
     * 使用的次数
     */
    private int usedCnt;

    /**
     * 聊天记录，最新的在前面；最多返回50条
     */
    private List<ChatItemVo> records;

    /**
     * 判断是否拥有提问次数
     *
     * @return true 表示拥有提问次数
     */
    public boolean hasQaCnt() {
        return maxCnt > usedCnt;
    }
}
