package com.tarzan.nav.task;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author tarzan
 */
@Slf4j
@Component
public class SignInTask {

    @Value("${linkai.signInUrl}")
    private String signInUrl;
    @Value("${linkai.accessToken}")
    private String accessToken;

    @Scheduled(cron = "0 0 8 * * ?")
    public  void signIn() {
        String body=HttpUtil.createGet(signInUrl)
                .header("Authorization","Bearer "+accessToken).execute().body();
        log.info(JSON.parseObject(body).getString("message"));
    }

}
