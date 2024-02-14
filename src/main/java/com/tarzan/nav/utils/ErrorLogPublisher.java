package com.tarzan.nav.utils;

import com.tarzan.nav.common.event.ErrorLogEvent;
import com.tarzan.nav.modules.admin.model.log.LogError;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.FastStringWriter;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.util.Objects;


/**
 * @author tarzan
 */
@Component
public class ErrorLogPublisher {

    public static void publishEvent(Throwable error, String requestUri) {
        HttpServletRequest request = WebUtil.getRequest();
        LogError logError = new LogError();
        logError.setRequestUri(requestUri);
        if (Objects.nonNull(error)) {
            logError.setStackTrace(getStackTraceAsString(error));
            logError.setExceptionName(error.getClass().getName());
            logError.setMessage(error.getMessage());
            StackTraceElement[] elements = error.getStackTrace();
            if (Objects.nonNull(elements)) {
                StackTraceElement element = elements[0];
                logError.setMethodName(element.getMethodName());
                logError.setMethodClass(element.getClassName());
                logError.setFileName(element.getFileName());
                logError.setLineNumber(element.getLineNumber());
            }
            if (Objects.nonNull(request)) {
                logError.setRemoteIp(WebUtil.getIP(request));
                logError.setUserAgent(request.getHeader("user-agent"));
                logError.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
                logError.setMethod(request.getMethod());
                logError.setParams(WebUtil.getRequestContent(request));
            }
        }
        SpringUtil.publishEvent(new ErrorLogEvent(logError));
    }


    public static String getStackTraceAsString(Throwable ex) {
        FastStringWriter stringWriter = new FastStringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
