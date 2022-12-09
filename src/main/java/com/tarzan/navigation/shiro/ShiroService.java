package com.tarzan.navigation.shiro;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 初始化、动态更新shiro用户权限
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class ShiroService {

    private final ShiroFilterFactoryBean shiroFilterFactoryBean;

    /**
     * 初始化权限
     */
    private Map<String, String> loadFilterChainDefinitions() {
        // 权限控制map.从数据库获取
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
/*        filterChainDefinitionMap.put("/index.html", "anon");
        filterChainDefinitionMap.put("/", "anon");
        filterChainDefinitionMap.put("/index", "anon");
        filterChainDefinitionMap.put("/blog/**", "anon");
        filterChainDefinitionMap.put("/search/**", "anon");
        filterChainDefinitionMap.put("/link", "anon");
        filterChainDefinitionMap.put("/feedback", "anon");
        filterChainDefinitionMap.put("/diary", "anon");
        filterChainDefinitionMap.put("/system/register", "anon");
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/kickOut", "anon");
        filterChainDefinitionMap.put("/error/**", "anon");
        filterChainDefinitionMap.put("/assets/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/**", "anon");
        filterChainDefinitionMap.put("/module/**", "anon");
        filterChainDefinitionMap.put("/verificationCode", "anon");
        filterChainDefinitionMap.put("/oauth/**", "anon");*/

       // filterChainDefinitionMap.put("/**", "anon");
        filterChainDefinitionMap.put("/admin/**","user");
/*        filterChainDefinitionMap.put(fileUploadProperties.getAccessPathPattern(), "anon");
        filterChainDefinitionMap.put(staticHtmlProperties.getAccessPathPattern(), "anon");
        List<Menu> menuList = menuService.selectAll(CoreConst.STATUS_VALID);
        for (Menu menu : menuList) {
            if (StringUtils.isNotBlank(menu.getUrl()) && StringUtils.isNotBlank(menu.getPerms())) {
                String perm = "perms[" + menu.getPerms() + ']';
                filterChainDefinitionMap.put(menu.getUrl(), perm + ",kickOut");
            }
        }
        filterChainDefinitionMap.put("/**", "user,kickOut");*/
        return filterChainDefinitionMap;
    }

    /**
     * 重新加载权限
     */
    @CacheEvict(value = "menu", allEntries = true)
    public void updatePermission() {
        synchronized (shiroFilterFactoryBean) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new RuntimeException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter
                    .getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                    .getFilterChainManager();
            // 清空老的权限控制
            manager.getFilterChains().clear();
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitions());
            // 重新构建生成
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            chains.forEach((url, perm) -> manager.createChain(url, StringUtils.deleteWhitespace(perm)));
        }
    }
}
