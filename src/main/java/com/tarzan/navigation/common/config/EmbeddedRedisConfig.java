package com.tarzan.navigation.common.config;


import com.tarzan.navigation.common.props.CmsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Lenovo
 */
@Configuration
@ConditionalOnProperty(prefix = "cms",name = "embedded-redis-enabled", havingValue = "true")
public class EmbeddedRedisConfig {

    private RedisServer redisServer;
    @Resource
    private CmsProperties cmsProperties;


    /**
     * 构造方法之后执行.
     */
    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = RedisServer.builder()
                //端口
                .port(cmsProperties.getEmbeddedRedisPort())
                //绑定ip
                .setting("bind localhost")
                //设置密码
                .setting("requirepass "+cmsProperties.getEmbeddedRedisPassword())
                //最大堆内存
                .setting("maxheap 500m")
                .build();
        redisServer.start();
    }

    /**
     * 析构方法之后执行.
     */
    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
