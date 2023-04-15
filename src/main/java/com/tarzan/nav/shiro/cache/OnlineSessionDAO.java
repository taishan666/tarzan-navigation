package com.tarzan.nav.shiro.cache;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;


/**
 * 针对自定义的ShiroSession的db操作
 * 
 * @author ruoyi
 */
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO {


    public OnlineSessionDAO() {
        super();
    }

    public OnlineSessionDAO(long expireTime) {
        super();
    }
}
