package com.tarzan.nav.modules.admin.model.sys;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.entity.sys.UserEntity;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import lombok.Data;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName(TableConst.USER)
public class User extends UserEntity {
    @TableField(exist = false)
    private BizImage img;
}