package com.tarzan.navigation.modules.admin.controller.common;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一错误跳转页面，如403，404，500
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    /**
     * shiro无权限时进入
     *
     * @param response
     * @return
     */
    @RequestMapping("/403")
    public String noPermission(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "error/403";
    }

    /**
     * 找不到页面
     *
     * @param response
     * @return
     */
    @RequestMapping("/404")
    public String notFund(HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return "error/404";
    }

    /**
     * 系统服务错误
     *
     * @param response
     * @return
     */
    @RequestMapping("/500")
    public String sysError(HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return "error/500";
    }

}
