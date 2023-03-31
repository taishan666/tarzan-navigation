package com.tarzan.navigation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年8月11日
 */
@EnableScheduling
@EnableCaching
@SpringBootApplication
public class NavigationApplication {
    public static void main(String[] args){
        SpringApplication.run(NavigationApplication.class, args);
    }
}
