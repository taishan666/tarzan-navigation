package com.tarzan.nacigation.shiro.redis;

import com.tarzan.nacigation.shiro.exception.CacheManagerPrincipalIdNotAssignedException;
import com.tarzan.nacigation.shiro.exception.PrincipalIdNullException;
import com.tarzan.nacigation.shiro.exception.PrincipalInstanceException;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class RedisCache<K, V> implements Cache<K, V> {

    private final static Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private final IRedisManager redisManager;
    private String keyPrefix = RedisCacheManager.DEFAULT_CACHE_KEY_PREFIX;
    private final int expire;
    private String principalIdFieldName = RedisCacheManager.DEFAULT_PRINCIPAL_ID_FIELD_NAME;

    /**
     *
     * @param redisManager redisManager
     * @param prefix authorization prefix
     * @param expire expire
     * @param principalIdFieldName id field name of principal object
     */
    public RedisCache(IRedisManager redisManager,String prefix, int expire, String principalIdFieldName) {
        if (redisManager == null) {
            throw new IllegalArgumentException("redisManager cannot be null.");
        }
        this.redisManager = redisManager;
        if (prefix != null && !"".equals(prefix)) {
            this.keyPrefix = prefix;
        }
        this.expire = expire;
        if (principalIdFieldName != null) {
            this.principalIdFieldName = principalIdFieldName;
        }
    }

    /**
     * get shiro authorization redis key-value
     * @param key key
     * @return value
     * @throws CacheException get cache exception
     */
    @Override
    public V get(K key) throws CacheException {
        if (key == null) {
            return null;
        }
        String redisCacheKey = getRedisCacheKey(key);
        return redisManager.get(redisCacheKey);

    }

    @Override
    public V put(K key, V value) throws CacheException {
        if (key == null) {
            logger.warn("Saving a null key is meaningless, return value directly without call Redis.");
            return value;
        }
        String redisCacheKey = getRedisCacheKey(key);
        redisManager.set(redisCacheKey,value, expire);
        return value;
    }

    @Override
    public V remove(K key) throws CacheException {
        logger.debug("remove key [" + key + "]");
        if (key == null) {
            return null;
        }
        String redisCacheKey = getRedisCacheKey(key);
        V previous = redisManager.get(redisCacheKey);
        redisManager.del(redisCacheKey);
        return previous;
    }

    private String getRedisCacheKey(K key) {
        if (key == null) {
            return null;
        }
        return this.keyPrefix + getStringRedisKey(key);
    }

    private String getStringRedisKey(K key) {
        String redisKey;
        if (key instanceof PrincipalCollection) {
            redisKey = getRedisKeyFromPrincipalIdField((PrincipalCollection) key);
        } else {
            redisKey = key.toString();
        }
        return redisKey;
    }



    private String getRedisKeyFromPrincipalIdField(PrincipalCollection key) {
        Object principalObject = key.getPrimaryPrincipal();
        if (principalObject instanceof String) {
            return principalObject.toString();
        }
        Method pincipalIdGetter = getPrincipalIdGetter(principalObject);
        return getIdObj(principalObject, pincipalIdGetter);
    }

    private String getIdObj(Object principalObject, Method pincipalIdGetter) {
        String redisKey;
        try {
            Object idObj = pincipalIdGetter.invoke(principalObject);
            if (idObj == null) {
                throw new PrincipalIdNullException(principalObject.getClass(), this.principalIdFieldName);
            }
            redisKey = idObj.toString();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName, e);
        }
        return redisKey;
    }

    private Method getPrincipalIdGetter(Object principalObject) {
        Method pincipalIdGetter;
        String principalIdMethodName = this.getPrincipalIdMethodName();
        try {
            pincipalIdGetter = principalObject.getClass().getMethod(principalIdMethodName);
        } catch (NoSuchMethodException e) {
            throw new PrincipalInstanceException(principalObject.getClass(), this.principalIdFieldName);
        }
        return pincipalIdGetter;
    }

    private String getPrincipalIdMethodName() {
        if (this.principalIdFieldName == null || "".equals(this.principalIdFieldName)) {
            throw new CacheManagerPrincipalIdNotAssignedException();
        }
        return "get" + this.principalIdFieldName.substring(0, 1).toUpperCase() + this.principalIdFieldName.substring(1);
    }


    @Override
    public void clear() throws CacheException {
        logger.debug("clear cache");
        Set<String> keys;
        keys = redisManager.keys(this.keyPrefix + "*");
        if (keys == null || keys.size() == 0) {
            return;
        }
        for (String key: keys) {
            redisManager.del(key);
        }
    }

    /**
     * get all authorization key-value quantity
     * @return key-value size
     */
    @Override
    public int size() {
        return redisManager.dbSize(this.keyPrefix + "*");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<K> keys() {
        return (Set<K>)  redisManager.keys(this.keyPrefix + "*");
    }

    @Override
    public Collection<V> values() {
        Set<String> keys;
        keys = redisManager.keys(this.keyPrefix + "*");

        if (CollectionUtils.isEmpty(keys)) {
            return Collections.emptySet();
        }

        List<V> values = new ArrayList<>(keys.size());
        for (String key : keys) {
            V value  = redisManager.get(key);
            if (value != null) {
                values.add(value);
            }
        }
        return Collections.unmodifiableList(values);
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    public String getPrincipalIdFieldName() {
        return principalIdFieldName;
    }

    public void setPrincipalIdFieldName(String principalIdFieldName) {
        this.principalIdFieldName = principalIdFieldName;
    }
}
