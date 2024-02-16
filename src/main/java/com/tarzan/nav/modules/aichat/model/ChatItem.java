package com.tarzan.nav.modules.aichat.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.aichat.entity.ChatItemEntity;
import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
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
    @TableField(exist = false)
    private ChatAnswerTypeEnum answerType;
    @TableField(exist = false)
    private String username;
}
