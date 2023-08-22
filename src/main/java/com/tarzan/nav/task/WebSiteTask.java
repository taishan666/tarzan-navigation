package com.tarzan.nav.task;

import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.WebsiteService;
import com.tarzan.nav.utils.JsoupUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author tarzan
 */
@Slf4j
@Component
@AllArgsConstructor
public class WebSiteTask {

    private final WebsiteService websiteService;

  //  @Scheduled(cron = "0/59 * * * * ?")
    public  void brokenLinkCheck() {
        List<Website> websites=websiteService.simpleList();
        log.info("---------------------  start  -----------------------------");
        websites.forEach(e->{
            if(JsoupUtil.checkLinkDead("https:"+e.getUrl())){
                log.info(e.getUrl());
              //  websiteService.updateStatus(e.getId(), CoreConst.STATUS_INVALID);
            }
        });
        log.info("---------------------  over  -----------------------------");
    }

  //  @Scheduled(cron = "0/59 * * * * ?")
    public  void noCategoryCheck() {
        List<Website> websites=websiteService.simpleList();
        log.info("---------------------  start  -----------------------------");
        websites.forEach(e->{
            if(Objects.isNull(e.getCategoryId())){
                System.out.println(e.getId());
                websiteService.deleteBatch(Collections.singletonList(e.getId()));
            }
        });
        log.info("---------------------  over  -----------------------------");
    }

    private static boolean checkIfDeadLink(String urlToCheck) {
        try {
            URL url = new URL(urlToCheck);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            int responseCode = connection.getResponseCode();
            System.err.println(responseCode);
            return (responseCode == HttpURLConnection.HTTP_NOT_FOUND);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return true;
        }
    }
}
