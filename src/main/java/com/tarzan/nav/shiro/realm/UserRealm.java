package com.tarzan.nav.shiro.realm;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.service.sys.MenuService;
import com.tarzan.nav.modules.admin.service.sys.UserService;
import com.tarzan.nav.shiro.cache.OnlineSessionDAO;
import com.tarzan.nav.utils.IpUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 自定义shiro realm，进行鉴权和认证
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
public class UserRealm extends AuthorizingRealm {

    @Lazy @Resource
    private UserService userService;
    @Lazy @Resource
    private MenuService menuService;
    @Lazy @Resource
    private OnlineSessionDAO onlineSessionDAO;

    /**
     * 鉴权
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("principals should not be null");
        }
        User user = (User) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
      //  info.setRoles(roleService.findRoleByUserId(user.getId()));
        info.setStringPermissions(menuService.findPermsByUserId(user.getId()));
        return info;
    }

    /**
     * 认证
     *
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        //获取用户的输入的账号.
        String username = (String) token.getPrincipal();
        User user = userService.getByUsername(username);
        if (user==null) {
            throw new UnknownAccountException();
        }
        if (CoreConst.STATUS_INVALID.equals(user.getStatus())) {
            // 帐号锁定
            throw new LockedAccountException();
        }
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        // 把ip放入user存入redis缓存里
        user.setLoginIpAddress(IpUtil.getIpAddr(request));
        return new SimpleAuthenticationInfo(
                user,
                user.getPassword(),
                ByteSource.Util.bytes(user.getCredentialsSalt()),
                getName()
        );
    }

    /**
     * 清除认证信息
     *
     * @param userIds 待清除认证信息的userId列表
     */
    public void removeCachedAuthenticationInfo(List<Integer> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            Set<SimplePrincipalCollection> set = getSpcListByUserIds(userIds);
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            UserRealm realm = (UserRealm) securityManager.getRealms().iterator().next();
            for (SimplePrincipalCollection simplePrincipalCollection : set) {
                realm.clearCachedAuthenticationInfo(simplePrincipalCollection);
            }
        }
    }

    /**
     * 根据userId 清除当前session存在的用户的权限缓存
     *
     * @param userIds 已经修改了权限的userId列表
     */
    public void clearAuthorizationByUserId(List<Integer> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            Set<SimplePrincipalCollection> set = getSpcListByUserIds(userIds);
            RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
            UserRealm realm = (UserRealm) securityManager.getRealms().iterator().next();
            for (SimplePrincipalCollection simplePrincipalCollection : set) {
                realm.clearCachedAuthorizationInfo(simplePrincipalCollection);
            }
        }

    }

    /**
     * 根据用户id获取所有spc
     *
     * @param userIds 已经修改了权限的userId
     * @return
     */
    private Set<SimplePrincipalCollection> getSpcListByUserIds(List<Integer> userIds) {
        //获取所有session
        Collection<Session> sessions = onlineSessionDAO.getActiveSessions();
        //定义返回
        Set<SimplePrincipalCollection> set = new HashSet<>();
        for (Session session : sessions) {
            //获取session登录信息。
            Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
            if (obj instanceof SimplePrincipalCollection) {
                //强转
                SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
                //判断用户，匹配用户ID。
                obj = spc.getPrimaryPrincipal();
                if (obj instanceof User) {
                    User user = (User) obj;
                    //比较用户ID，符合即加入集合
                    if (userIds.contains(user.getId())) {
                        set.add(spc);
                    }
                }
            }
        }
        return set;
    }

}
