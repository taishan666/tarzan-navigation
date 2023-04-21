package com.tarzan.nav.task;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.WebsiteService;
import com.tarzan.nav.utils.JsoupUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author tarzan
 */
@Slf4j
@Component
@AllArgsConstructor
public class WebSiteTask {

    private final WebsiteService websiteService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public  void brokenLinkCheck() {
        List<Website> websites=websiteService.simpleList();
        websites.forEach(e->{
            if(!JsoupUtil.checkUrl(e.getUrl())){
                websiteService.updateStatus(e.getId(), CoreConst.STATUS_INVALID);
            }
        });
        log.info("---------------------  over  -----------------------------");
    }
}
