package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tarzan.nav.modules.admin.entity.biz.NoticeEntity;
import lombok.Data;

/**
 * @author TARZAN
 */
@Data
public class Notice extends NoticeEntity {

    private Integer days;
}
