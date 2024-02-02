package com.tarzan.nav.modules.admin.model.sys;

import com.tarzan.nav.modules.admin.entity.sys.UserEntity;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import lombok.Data;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class User extends UserEntity {
    private BizImage img;
}