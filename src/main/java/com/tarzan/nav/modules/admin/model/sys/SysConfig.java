package com.tarzan.nav.modules.admin.model.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.entity.sys.SysConfigEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName(TableConst.CONFIG)
@SuperBuilder
@NoArgsConstructor
public class SysConfig extends SysConfigEntity {

}