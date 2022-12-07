package com.tarzan.nacigation.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.tarzan.nacigation.modules.admin.model.sys.User;
import com.tarzan.nacigation.common.constant.CoreConst;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * 踢出用户
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Slf4j
@Setter
public class KickOutSessionControlFilter extends AccessControlFilter {

    /** 踢出后到的地址 */
    private String kickOutUrl;
    /** 用户session里的属性：是否应该被踢出 */
    private String kickOutSessionAttrName = "kickOut";
    /** 踢出之前登录的/之后登录的用户 默认踢出之前登录的用户 */
    private boolean kickOutAfter;
    /**  同一个帐号最大会话数 默认5 */
    private int maxSession = 5;

    private SessionManager sessionManager;

    private Cache<String, Deque<Serializable>> cache;
    /** 设置Cache的key的前缀 */
    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(CoreConst.SHIRO_REDIS_CACHE_NAME);
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (!subject.isAuthenticated() && !subject.isRemembered()) {
            //如果没有登录，直接进行之后的流程
            return true;
        }
        Session session = subject.getSession();
        User user = (User) subject.getPrincipal();
        String username = user.getUsername();
        Serializable sessionId = session.getId();
        //读取缓存   没有就存入
        Deque<Serializable> deque = cache.get(username);
        //如果此用户没有session队列，也就是还没有登录过，缓存中没有
        //就new一个空队列，不然deque对象为空，会报空指针
        if (deque == null) {
            deque = new LinkedList<>();
        }
        //如果队列里没有此sessionId，且用户没有被踢出；放入队列
        if (!deque.contains(sessionId) && session.getAttribute(kickOutSessionAttrName) == null) {
            //将sessionId存入队列
            deque.push(sessionId);
            //将用户的sessionId队列缓存
            cache.put(username, deque);
        }
        //如果队列里的sessionId数超出最大会话数，开始踢人
        while (deque.size() > maxSession) {
            Serializable kickoutSessionId;
            if (kickOutAfter) {
                //如果踢出后者
                kickoutSessionId = deque.removeFirst();
            } else { //否则踢出前者
                kickoutSessionId = deque.removeLast();
            }
            //踢出后再更新下缓存队列
            cache.put(username, deque);
            //获取被踢出的sessionId的session对象
            Session kickoutSession = sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
            if (kickoutSession != null) {
                //设置会话的kickout属性表示踢出了
                kickoutSession.setAttribute(kickOutSessionAttrName, true);
            }
        }
        //如果被踢出了，直接退出，重定向到踢出后的地址
        if (session.getAttribute(kickOutSessionAttrName) != null && (Boolean) session.getAttribute(kickOutSessionAttrName)) {
            //退出登录
            subject.logout();
            saveRequest(request);
            Map<String, String> resultMap = new HashMap<>(4);
            //判断是不是Ajax请求
            if ("XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"))) {
                resultMap.put("user_status", "300");
                resultMap.put("message", "您已经在其他地方登录，请重新登录！");
                //输出json串
                out(response, resultMap);
            } else {
                //重定向。设置redirectHttp10Compatible为false是为了防止shiro默认将https转为http协议。
                WebUtils.issueRedirect(request, response, kickOutUrl, null, true, false);
            }
            return false;
        }
        return true;
    }

    private static void out(ServletResponse response, Map<String, String> resultMap)  {
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(JSON.toJSONString(resultMap));
            out.flush();
        } catch (Exception e) {
            log.error("输出返回JSON发生异常:{}", e.getMessage(), e);
        }
    }
}
