package com.tarzan.nav.shiro.cache;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.stereotype.Component;


/**
 * 针对自定义的ShiroSession的db操作
 * 
 * @author ruoyi
 */
@Component
public class OnlineSessionDAO extends EnterpriseCacheSessionDAO {


    public OnlineSessionDAO() {
        super();
    }

    public OnlineSessionDAO(long expireTime) {
        super();
    }
}
