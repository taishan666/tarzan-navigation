package com.tarzan.nav.modules.admin.model.log;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.utils.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 登录日志
 *
 * @version 1.0
 * @author tarzan
 * @date 2021年07月20日 10:21:47
 */
@Data
@TableName("sys_login_log")
@EqualsAndHashCode
public class LoginLog implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 来源IP地址
     */
    private String sourceIp;

    /**
     * 登录开始时间
     */
    @DateTimeFormat(pattern = DateUtil.datetimeFormat)
    private Date startTime;

    /**
     * 登录结束时间
     */
    @DateTimeFormat(pattern = DateUtil.datetimeFormat)
    private Date endTime;

    /**
     * 登录方式
     */
    private String source;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = DateUtil.datetimeFormat)
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
