package com.tarzan.nav.modules.admin.entity.log;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author tarzan
 */
@Data
@Table(name="sys_log_error")
@Entity
public class LogErrorEntity implements Serializable {

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    /**
     * 远程Ip
     */
    protected String remoteIp;
    /**
     * 用户代理
     */
    protected String userAgent;
    /**
     * 请求接口
     */
    protected String requestUri;
    /**
     * 方法
     */
    protected String method;
    /**
     * 方法类
     */
    protected String methodClass;
    /**
     * 方法名
     */
    protected String methodName;
    /**
     * 参数
     */
    protected String params;
    /**
     * 堆栈跟踪
     */
    private String stackTrace;
    /**
     * 异常名称
     */
    private String exceptionName;
    /**
     * 信息
     */
    private String message;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 错误行数
     */
    private Integer lineNumber;
    /**
     * 创建人
     */
    protected String createName;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    protected Date createTime;
}
