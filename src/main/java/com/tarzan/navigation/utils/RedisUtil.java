package com.tarzan.navigation.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author TARZAN
 */
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> void set(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> void set(String key, T value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        Object o = redisTemplate.opsForValue().get(key);
        return Objects.isNull(o) ? null : (T) o;
    }

    public boolean expire(String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    public void del(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    public void delBatch(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    public void delBatch(String keyPrefix) {
        Set<String> keys = keySet(keyPrefix);
        if (CollectionUtils.isNotEmpty(keys)) {
            delBatch(keys);
        }
    }

    public <T> void setList(String key, List<T> list) {
        String value = JSON.toJSONString(list);
        set(key, value);
    }

    public <T> void setList(String key, List<T> list, long expire, TimeUnit timeUnit) {
        String value = JSON.toJSONString(list);
        set(key, value, expire, timeUnit);
    }

    public <T> List<T> getList(String key, Class<T> clz) {
        String json = get(key);
        if (json != null) {
            return JSON.parseArray(json, clz);
        }
        return null;
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    public Set<String> keySet(String keyPrefix) {
        return redisTemplate.keys(keyPrefix + '*');
    }
}
