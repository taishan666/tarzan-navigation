package com.tarzan.nav.shiro.credentials;

import lombok.Setter;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 凭证匹配器（密码规则校验）
 *
 * @author tarzan
  */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    /**
     * 错误次数
     */
    @Setter
    private int incrementAndGet;
    /**
     * 缓存标示
     */
    private final Cache<String, AtomicInteger> passwordRetryCache;

    /**
     * 构造
     */
    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = token.getPrincipal().toString();
        //设置次数
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
        }
        if (retryCount.incrementAndGet() > incrementAndGet) {
            //重试次数如果大于5次，就锁定
            throw new ExcessiveAttemptsException();
        }
        //并将其保存到缓存中
        passwordRetryCache.put(username, retryCount);
        boolean matches = super.doCredentialsMatch(token, info);
        if (matches) {
            //登陆成功，清除登陆次数
            passwordRetryCache.remove(username);
        }
        return matches;
    }
}
