package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tarzan.nav.modules.admin.model.biz.SiteLook;
import com.tarzan.nav.modules.admin.vo.StatisticVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author tarzan liu
 * @date 2021/1/12 3:35 下午
 */
@Service
@AllArgsConstructor
@Slf4j
public class StatisticService {

    private final LinkService linkService;
    private final CommentService commentService;
    private final WebsiteService websiteService;
    private final SiteLookService siteLookService;


    public StatisticVo getStatistic() {
        long linkCount = linkService.count();
        long commentCount = commentService.count();
        long siteCount = websiteService.count();
        List<SiteLook> lookList=siteLookService.list(Wrappers.<SiteLook>lambdaQuery().select(SiteLook::getUserIp));
        long lookCount = lookList.size();
        long userCount = lookList.stream().distinct().count();
        int recentDays = 7;
        Map<String,List<SiteLook>> lookMap=siteLookService.looksGroupMap(recentDays);
        Map<String, Long> lookCountByDay = siteLookService.looksByDay(lookMap,recentDays);
        Map<String, Long> userCountByDay = siteLookService.usersByDay(lookMap,recentDays);
        return StatisticVo.builder().siteCount(siteCount).linkCount(linkCount).commentCount(commentCount).lookCount(lookCount).userCount(userCount).lookCountByDay(lookCountByDay).userCountByDay(userCountByDay).build();
    }

}
