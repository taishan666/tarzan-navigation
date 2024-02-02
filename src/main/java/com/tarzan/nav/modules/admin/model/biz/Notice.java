package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.entity.biz.NoticeEntity;
import lombok.Data;

/**
 * @author TARZAN
 */
@Data
@TableName("biz_notice")
public class Notice extends NoticeEntity {
    @TableField(exist = false)
    private Integer days;
}
