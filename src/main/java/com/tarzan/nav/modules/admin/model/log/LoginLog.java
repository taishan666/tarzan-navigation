package com.tarzan.nav.modules.admin.model.log;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.entity.log.LoginLogEntity;
import lombok.Data;

/**
 * 登录日志
 *
 * @version 1.0
 * @author tarzan
 * @date 2021年07月20日 10:21:47
 */
@Data
@TableName(TableConst.LOGIN_LOG)
public class LoginLog extends LoginLogEntity {

}
