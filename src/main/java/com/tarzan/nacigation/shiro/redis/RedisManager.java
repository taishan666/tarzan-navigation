package com.tarzan.nacigation.shiro.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author tarzan
 */
public class RedisManager implements IRedisManager{


    private final RedisTemplate<String, Object> sessionRedisTemplate;

    public RedisManager(RedisTemplate<String, Object> sessionRedisTemplate) {
        this.sessionRedisTemplate = sessionRedisTemplate;
    }


    @Override
    public <T> T get(String key) {
        Object o = sessionRedisTemplate.opsForValue().get(key);
        return Objects.isNull(o) ? null : (T) o;
    }

    @Override
    public <T> void set(String key, T value, long expire){
        sessionRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public void del(String key) {
        sessionRedisTemplate.opsForValue().getOperations().delete(key);
    }

    @Override
    public Integer dbSize(String pattern) {
        return  keys(pattern).size();
    }

    @Override
    public Set<String> keys(String pattern) {
        return sessionRedisTemplate.keys(pattern);
    }
}
