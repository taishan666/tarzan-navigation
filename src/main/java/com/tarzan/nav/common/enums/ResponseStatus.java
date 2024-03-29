package com.tarzan.nav.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接口响应状态枚举类
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Getter
@AllArgsConstructor
public enum ResponseStatus {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功！"),
    /**
     * 未鉴权
     */
    UNAUTHORIZED(401, "未鉴权！"),
    /**
     * 无权限
     */
    FORBIDDEN(403, "您没有权限访问！"),
    /**
     * 未找到
     */
    NOT_FOUND(404, "资源不存在！"),
    /**
     * 服务器内部错误
     */
    ERROR(500, "服务器内部错误！");

    private final Integer code;
    private final String message;

}
