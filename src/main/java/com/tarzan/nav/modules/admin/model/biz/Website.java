package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.entity.biz.WebsiteEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName(TableConst.WEBSITE)
@SuperBuilder
@NoArgsConstructor
public class Website extends WebsiteEntity {

    @TableField(exist = false)
    private String flag;
    @TableField(exist = false)
    private BizImage img;
    @TableField(exist = false)
    private String categoryName;
    @TableField(exist = false)
    private String applyType;

}
