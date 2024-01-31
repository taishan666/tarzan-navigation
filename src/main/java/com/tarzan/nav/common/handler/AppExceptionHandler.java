package com.tarzan.nav.common.handler;

import com.tarzan.nav.common.enums.ResponseStatus;
import com.tarzan.nav.common.exception.AppException;
import com.tarzan.nav.utils.ErrorLogPublisher;
import com.tarzan.nav.utils.UrlUtil;
import com.tarzan.nav.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 异常统一处理
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(AppException.class)
    public String handleAppException(Exception e, HttpServletRequest request) {
        request.setAttribute("javax.servlet.error.status_code", ResponseStatus.ERROR.getCode());
        Map<String, Object> map = new HashMap<>(2);
        map.put("status", ResponseStatus.ERROR.getCode());
        map.put("msg", StringUtils.isNotBlank(e.getMessage()) ? e.getMessage() : ResponseStatus.ERROR.getMessage());
        log.error("拦截到系统异常AppException: {}", e.getMessage(), e);
        request.setAttribute("ext", map);
        ErrorLogPublisher.publishEvent(e, UrlUtil.getPath(Objects.requireNonNull(WebUtil.getRequest()).getRequestURI()));
        return "forward:/error";
    }

    @ExceptionHandler(AuthorizationException.class)
    public String handleAuth(Exception e,HttpServletRequest request) {
        log.error("未鉴权异常: {}", e.getMessage(), e);
        request.setAttribute("javax.servlet.error.status_code", ResponseStatus.UNAUTHORIZED.getCode());
        return "forward:/error";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
        log.error("异常: {}", e.getMessage(), e);
        request.setAttribute("javax.servlet.error.status_code", ResponseStatus.ERROR.getCode());
        ErrorLogPublisher.publishEvent(e, UrlUtil.getPath(Objects.requireNonNull(WebUtil.getRequest()).getRequestURI()));
        return "forward:/error";
    }


}
