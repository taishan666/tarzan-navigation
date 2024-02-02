package com.tarzan.nav.modules.admin.model.log;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.entity.log.LogErrorEntity;
import lombok.Data;

/**
 * @author tarzan
 */
@Data
@TableName("sys_log_error")
public class LogError extends LogErrorEntity {

}
