package com.tarzan.nav.modules.front.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
import com.tarzan.nav.modules.front.entity.ChatItemEntity;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@TableName(TableConst.CHAT_ITEM)
public class ChatItem extends ChatItemEntity {
    /**
     * 回答的内容类型，文本、JSON 字符串
     */
    private ChatAnswerTypeEnum answerType;
}
