package com.tarzan.nacigation.shiro.redis;

import java.util.Set;

/**
 * redisManager interface
 * @author tarzan
 * */

public interface IRedisManager {

    /**
     * get value from redis
     * @param key key
     * @return value
     */
    <T> T get(String key);

    /**
     * set value
     * @param key  key
     * @param value value
     * @param expire expire
     */
    <T>  void set(String key, T value, long expire);

    /**
     * del
     * @param key key
     */
    void del(String key);

    /**
     * dbsize
     * @param pattern pattern
     * @return key-value size
     */
    Integer dbSize(String pattern);

    /**
     * keys
     * @param pattern key pattern
     * @return key set
     */
    Set<String> keys(String pattern);

}
