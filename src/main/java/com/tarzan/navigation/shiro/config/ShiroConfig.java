package com.tarzan.navigation.shiro.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.tarzan.navigation.shiro.realm.UserRealm;
import com.tarzan.navigation.shiro.cache.OnlineSessionDAO;
import com.tarzan.navigation.shiro.credentials.RetryLimitHashedCredentialsMatcher;
import com.tarzan.navigation.shiro.filter.KickOutSessionControlFilter;
import com.tarzan.navigation.utils.ShiroAESKeyUtil;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Shiro相关配置
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Configuration
public class ShiroConfig {

    @Value("${cms.shiro-key}")
    private String shiroKey;

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

    /**
     * ShiroFilterFactoryBean 处理拦截资源文件问题。
     * 注意：单独一个ShiroFilterFactoryBean配置是或报错的，因为在
     * 初始化ShiroFilterFactoryBean的时候需要注入：SecurityManager
     * <p>
     * Filter Chain定义说明
     * 1、一个URL可以配置多个Filter，使用逗号分隔
     * 2、当设置多个过滤器时，全部验证通过，才视为通过
     * 3、部分过滤器可指定参数，如perms，roles
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 登录url
        shiroFilterFactoryBean.setLoginUrl("/system/login");
        // 登录成功后要跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/admin");
        //未授权界面;
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/403");
        //自定义拦截器
        Map<String, Filter> filtersMap = new LinkedHashMap<>(1);
        //限制同一帐号同时在线的个数。
        filtersMap.put("kickOut", kickoutSessionControlFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
        // 设置全局filter
        shiroFilterFactoryBean.setGlobalFilters(Collections.emptyList());
        return shiroFilterFactoryBean;
    }

    /**
     * cookie对象;
     *
     * @return
     */
    public SimpleCookie rememberMeCookie() {
        //这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        //<!-- 记住我cookie生效时间30天 ,单位秒;-->
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * cookie管理对象;记住我功能
     *
     * @return
     */
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //手动设置对称加密秘钥，防止重启系统后系统生成新的随机秘钥，防止导致客户端cookie无效
         cookieRememberMeManager.setCipherKey(Base64.decode(ShiroAESKeyUtil.getKey(shiroKey)));
        return cookieRememberMeManager;
    }

    @Bean(name = "securityManager")
    public SecurityManager securityManager(UserRealm shiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //设置realm.
        securityManager.setRealm(shiroRealm);
        /*记住我*/
        securityManager.setRememberMeManager(rememberMeManager());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(getEhCacheManager());
        // 自定义session管理 使用redis
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 缓存管理器 使用Ehcache实现
     */
    @Bean
    public EhCacheManager getEhCacheManager() {
        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("tarzan");
        EhCacheManager em = new EhCacheManager();
        if (Objects.isNull(cacheManager)) {
            em.setCacheManager(new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream()));
            return em;
        } else {
            em.setCacheManager(cacheManager);
            return em;
        }
    }


    /**
     * 返回配置文件流 避免ehcache配置文件一直被占用，无法完全销毁项目重新部署
     */
    protected InputStream getCacheManagerConfigFileInputStream() {
        String configFile = "classpath:ehcache/ehcache-shiro.xml";
        try {
            return ResourceUtils.getInputStreamForPath(configFile);
        } catch (IOException e) {
            throw new ConfigurationException(
                    "Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", e);
        }
    }

    /*  *//**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     *//*
    @Bean
    public OnlineSessionDAO onlineSessionDAO() {
        return new OnlineSessionDAO();
    }*/

    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 禁用url自动拼接的 ;jsessionid= ，因为shiro 1.6.0 新增了org.apache.shiro.web.filter.InvalidRequestFilter 会拦截url中的分号、反斜杠以及非ASCII码
        // 详情可参考 https://mp.weixin.qq.com/s?__biz=MzIxNjkwODg4OQ==&mid=2247484465&idx=1&sn=04db0c26159dee0a8443c74c7ab58ebe&chksm=97809107a0f718114c20a3ad881e31810272593007d515f03a372230a64adfff53ebdc55c141&scene=126&
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        sessionManager.setSessionDAO(new OnlineSessionDAO());
        return sessionManager;
    }

    /**
     * 限制同一账号登录同时登录人数控制
     *
     * @return
     */
    public KickOutSessionControlFilter kickoutSessionControlFilter() {
        KickOutSessionControlFilter kickoutSessionControlFilter = new KickOutSessionControlFilter();
        //使用cacheManager获取相应的cache来缓存用户登录的会话；用于保存用户—会话之间的关系的；
        //这里我们还是用之前shiro使用的redisManager()实现的cacheManager()缓存管理
        //也可以重新另写一个，重新配置缓存时间之类的自定义缓存属性
        kickoutSessionControlFilter.setCacheManager(getEhCacheManager());
        //用于根据会话ID，获取会话进行踢出操作的；
        kickoutSessionControlFilter.setSessionManager(sessionManager());
        //是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序。
        kickoutSessionControlFilter.setKickOutAfter(false);
        //同一个用户最大的会话数，默认3；比如5的意思是同一个用户允许最多同时三个人登录；
        kickoutSessionControlFilter.setMaxSession(3);
        //被踢出后重定向到的地址；
        kickoutSessionControlFilter.setKickOutUrl("/kickOut");
        return kickoutSessionControlFilter;
    }


    /**
     * 凭证匹配器（由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
     */
    @Bean
    public RetryLimitHashedCredentialsMatcher getCredentialsMatcher(EhCacheManager cacheManager) {
        RetryLimitHashedCredentialsMatcher matcher = new RetryLimitHashedCredentialsMatcher(cacheManager);
        matcher.setHashAlgorithmName("md5");
        matcher.setHashIterations(2);
        // 错误限制次数，5次
        matcher.setIncrementAndGet(5);
        return matcher;
    }

    /**
     * 自定义认证授权规则
     */
    @Bean
    public UserRealm loginRealm(EhCacheManager cacheManager) {
        // 登录认证规则
        UserRealm loginRealm = new UserRealm();
        // 自定义加密规则
        loginRealm.setCredentialsMatcher(getCredentialsMatcher(cacheManager));
        return loginRealm;
    }


}
