package com.tarzan.nacigation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年8月11日
 */
@EnableScheduling
@SpringBootApplication
public class CmsApplication {
    public static void main(String[] args){
        SpringApplication.run(CmsApplication.class, args);
    }
}
