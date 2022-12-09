package com.tarzan.navigation.shiro;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

/**
 * js调用 thymeleaf 实现按钮权限
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Component("perms")
public class PermsService {
    public boolean hasPerm(String menu) {
        return SecurityUtils.getSubject().isPermitted(menu);
    }
}
