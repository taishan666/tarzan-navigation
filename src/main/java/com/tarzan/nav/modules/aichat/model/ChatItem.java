package com.tarzan.nav.modules.aichat.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.aichat.entity.ChatItemEntity;
import lombok.Data;

/**
 * @author Lenovo
 */
@Data
@TableName(TableConst.CHAT_ITEM)
public class ChatItem extends ChatItemEntity {

    @TableField(exist = false)
    private String username;
}
