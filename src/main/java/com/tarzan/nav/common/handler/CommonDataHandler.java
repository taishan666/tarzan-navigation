package com.tarzan.nav.common.handler;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.service.sys.SysConfigService;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author tarzan liu
 */
@Service
@Component
@AllArgsConstructor
public class CommonDataHandler implements HandlerInterceptor {

    private final SysConfigService sysConfigService;

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
        response.addHeader("X-Content-Type-Options", "nosniff");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) throws Exception {
            if (mv != null) {
                if(!CoreConst.IS_REGISTERED.get()&&CoreConst.SYSTEM_REGISTER.equals(request.getServletPath())) {
                 mv.setViewName("admin/system/register");
               }
                mv.addObject("SITE_CONFIG",sysConfigService.getInfo());
                User user = (User) SecurityUtils.getSubject().getPrincipal();
                if(Objects.nonNull(user)){
                    mv.addObject("loginUser",user);
                }
            }
          // response.setHeader("Cache-Control","max-age=31536000");
    }
}