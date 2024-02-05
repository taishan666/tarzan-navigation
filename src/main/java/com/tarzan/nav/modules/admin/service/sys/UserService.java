package com.tarzan.nav.modules.admin.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.mapper.biz.ImageMapper;
import com.tarzan.nav.modules.admin.mapper.sys.UserMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.sys.User;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private final SessionManager sessionManager;
    private final CacheManager cacheManager;
    private final ImageMapper imageMapper;



    public User getByUsername(String username) {
        User user= super.lambdaQuery().eq(User::getUsername, username).one();
        if(Objects.nonNull(user)&& Objects.nonNull(user.getImageId())){
            this.wrapper(user);
        }
        return user;
    }

    public boolean exists(String username) {
        Long count= super.lambdaQuery().eq(User::getUsername, username).count();
        return count!=0;
    }
    public boolean existsEmail(String email) {
        Long count= super.lambdaQuery().eq(User::getEmail, email).count();
        return count!=0;
    }

    public void updateLastLoginTime(Integer userId) {
        Assert.notNull(userId, "param: userId is null");
        super.lambdaUpdate().set(User::getLastLoginTime,new Date()).eq(User::getId,userId).update();
    }

    public IPage<User> selectUsers(User user, Integer pageNumber, Integer pageSize) {
        IPage<User> page = new Page<>(pageNumber, pageSize);
        page=page(page,Wrappers.<User>lambdaQuery()
                .like(StringUtils.isNotBlank(user.getUsername()),User::getUsername,user.getUsername())
                .like(StringUtils.isNotBlank(user.getEmail()),User::getEmail,user.getEmail())
                .like(StringUtils.isNotBlank(user.getPhone()),User::getPhone,user.getPhone())
                .eq(User::getStatus,CoreConst.STATUS_VALID)
                .orderByDesc(User::getCreateTime));
        return page;
    }

    @Cacheable(value = "user", key = "#userId")
    public User getByIdWithImage(Integer userId) {
        User user=getById(userId);
        if(Objects.nonNull(user)&& Objects.nonNull(user.getImageId())){
          this.wrapper(user);
        }
        return user;
    }

    @CacheEvict(value = "user",allEntries = true)
    public boolean updateByUserId(User user) {
        Assert.notNull(user, "param: user is null");
        //获取当前登录用户信息
        Subject subject = SecurityUtils.getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        //realName认证信息的key，对应的value就是认证的user对象
        String realmName = principalCollection.getRealmNames().iterator().next();
        //创建一个PrincipalCollection对象，更新user
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        subject.runAs(newPrincipalCollection);
        return updateById(user);
    }

    @CacheEvict(value = "user",allEntries = true)
    public boolean updateStatusBatch(List<Integer> userIds, Integer status) {
        return this.lambdaUpdate().in(User::getId, userIds).set(User::getStatus, status).update();
    }


    public void kickOut(Serializable sessionId, String username) {
        getSessionBySessionId(sessionId).setAttribute("kickOut", true);
        //读取缓存,找到并从队列中移除
        Cache<String, Deque<Serializable>> cache = cacheManager.getCache(CoreConst.SHIRO_REDIS_CACHE_NAME);
        Deque<Serializable> deques = cache.get(username);
        if(deques!=null){
            for (Serializable deque : deques) {
                if (sessionId.equals(deque)) {
                    deques.remove(deque);
                    break;
                }
            }
            cache.put(username, deques);
        }
    }

    private Session getSessionBySessionId(Serializable sessionId) {
        return sessionManager.getSession(new DefaultSessionKey(sessionId));
    }


    private void wrapper(User user) {
        BizImage img=imageMapper.selectById(user.getImageId());
        if(img!=null){
            user.setImg(img);
        }
    }

}
