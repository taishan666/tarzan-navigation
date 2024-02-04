package com.tarzan.nav.common.proxy;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YiHui
 * @date 2023/6/2
 */
public class ProxyCenter {

    /**
     * 记录每个source使用的proxy索引
     */
    private static final Cache<String, Integer> HOST_PROXY_INDEX = Caffeine.newBuilder().maximumSize(16).build();
    /**
     * proxy
     */
    private static List<ProxyProperties.ProxyType> PROXIES = new ArrayList<>();


    public static void initProxyPool(List<ProxyProperties.ProxyType> proxyTypes) {
        PROXIES = proxyTypes;
    }

    /**
     * get proxy
     *
     * @return
     */
    static ProxyProperties.ProxyType getProxy(String host) {
        Integer index = HOST_PROXY_INDEX.getIfPresent(host);
        if (index == null) {
            index = -1;
        }

        ++index;
        if (index >= PROXIES.size()) {
            index = 0;
        }
        HOST_PROXY_INDEX.put(host, index);
        return PROXIES.get(index);
    }

    public static Proxy loadProxy(String host) {
        ProxyProperties.ProxyType proxyType = getProxy(host);
        if (proxyType == null) {
            return null;
        }
        return new Proxy(proxyType.getType(), new InetSocketAddress(proxyType.getIp(), proxyType.getPort()));
    }
}
