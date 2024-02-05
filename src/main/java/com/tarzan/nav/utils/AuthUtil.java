package com.tarzan.nav.utils;

import com.tarzan.nav.modules.admin.model.sys.User;
import lombok.Data;
import org.apache.shiro.SecurityUtils;

import java.security.Principal;

/**
 * @author tarzan
 */
public class AuthUtil {

    private static ThreadLocal<ReqInfo> contexts = new InheritableThreadLocal<>();

    public static void addReqInfo(ReqInfo reqInfo) {
        contexts.set(reqInfo);
    }

    public static void clear() {
        contexts.remove();
    }

    public static ReqInfo getReqInfo() {
        return contexts.get();
    }

    @Data
    public static class ReqInfo implements Principal {
        /**
         * 用户id
         */
        private Integer userId;

        @Override
        public String getName() {
            return String.valueOf(userId);
        }
    }

    public  static User  getUser(){
        return  (User) SecurityUtils.getSubject().getPrincipal();
    }

    public  static Integer  getUserId(){
        if (getUser()==null){
            return -1;
        }
        return  getUser().getId();
    }

    public  static String  getUsername(){
        if (getUser()==null){
            return "";
        }
        return  getUser().getUsername();
    }

    public  static String  getEmail(){
        if (getUser()==null){
            return "";
        }
        return  getUser().getEmail();
    }
}
