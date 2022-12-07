package com.tarzan.nacigation.modules.admin.model.log;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_log_error")
@EqualsAndHashCode
public class LogError implements Serializable {

    /**
     * 主键id
     */
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
    protected Date createTime;
}
