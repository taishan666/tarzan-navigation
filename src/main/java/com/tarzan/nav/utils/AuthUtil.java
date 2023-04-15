package com.tarzan.nav.utils;

import com.tarzan.nav.modules.admin.model.sys.User;
import org.apache.shiro.SecurityUtils;

public class AuthUtil {

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
}
