package com.tarzan.nav.modules.admin.model.log;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.entity.log.LogErrorEntity;
import lombok.Data;

/**
 * @author tarzan
 */
@Data
@TableName(TableConst.LOG_ERROR)
public class LogError extends LogErrorEntity {

}
