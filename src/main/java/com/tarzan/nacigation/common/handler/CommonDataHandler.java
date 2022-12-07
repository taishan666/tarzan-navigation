package com.tarzan.nacigation.common.handler;

import com.tarzan.nacigation.common.constant.CoreConst;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tarzan liu
 */
@Service
@Component
@AllArgsConstructor
public class CommonDataHandler implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler){
        if (handler instanceof HandlerMethod) {
            String uri = request.getRequestURI();
            if (!CoreConst.IS_REGISTERED.get() && !CoreConst.SYSTEM_REGISTER.equals(uri)) {
                if (uri.lastIndexOf(".") < 1) {
                    try {
                        response.sendRedirect(CoreConst.SYSTEM_REGISTER);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                } else {
                    return true;
                }
            }
        }
        return true;
    }
}