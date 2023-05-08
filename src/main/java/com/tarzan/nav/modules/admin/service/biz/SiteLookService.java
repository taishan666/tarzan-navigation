package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.tarzan.nav.modules.admin.mapper.biz.SiteLookMapper;
import com.tarzan.nav.modules.admin.model.biz.SiteLook;
import com.tarzan.nav.modules.network.LocationService;
import com.tarzan.nav.utils.DateUtil;
import com.tarzan.nav.utils.MapUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TARZAN
 */
@Service
public class SiteLookService extends ServiceImpl<SiteLookMapper, SiteLook> {


    public Set<Integer> topSites(int num) {
        List<SiteLook> looks= super.list();
        Map<Integer,Long> lookMap=looks.stream().collect(Collectors.groupingBy(SiteLook::getSiteId,Collectors.counting()));
        return MapUtil.topNByValue(lookMap,num).keySet();
    }

    private static Map<String, Long> buildRecentDayMap(int day) {
        Date now = DateUtil.now();
        LinkedHashMap<String, Long> map = Maps.newLinkedHashMap();
        for (int i = day; i >= 1; i--) {
            Long count = 0L;
            map.put(DateUtil.format(DateUtil.addDays(now, -i), DateUtil.webFormat), count);
        }
        return map;
    }

    @Async
    public void asyncLook(Integer siteId,String userIp) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*浏览次数*/
        System.out.println(userIp+" 查看1次");
        Date date = new Date();
        long checkCount = this.checkArticleLook(siteId, userIp, DateUtil.addHours(date, -1));
        if (checkCount == 0) {
            SiteLook siteLook = new SiteLook();
            siteLook.setSiteId(siteId);
            siteLook.setUserIp(userIp);
            siteLook.setProvince(LocationService.getProvince(userIp));
            super.save(siteLook);
        }
    }


    public long checkArticleLook(Integer siteId, String userIp, Date lookTime) {
        return super.lambdaQuery().eq(SiteLook::getSiteId,siteId).eq(SiteLook::getUserIp,userIp).eq(SiteLook::getCreateTime,lookTime).count();
    }

    public  Map<String,Long> looksByDay(Map<String, List<SiteLook>> lookMap, int day){
        Map<String,Long> looksByDayMap= buildRecentDayMap(day);
        lookMap.forEach((k,v)->looksByDayMap.put(k, (long) v.size()));
        return looksByDayMap;
    }

    public  Map<String,Long> usersByDay(Map<String,List<SiteLook>> lookMap,int day){
        Map<String,Long> usersByDayMap=buildRecentDayMap(day);
        lookMap.forEach((k,v)->{
            Set<String> users= v.stream().map(SiteLook::getUserIp).collect(Collectors.toSet());
            usersByDayMap.put(k, (long) users.size());
        });
        return usersByDayMap;
    }

    public Map<String,List<SiteLook>>  looksGroupMap(int day){
        List<SiteLook> list=looksRecentDays(day);
        return list.stream().collect(Collectors.groupingBy(e->DateUtil.format(e.getCreateTime(), DateUtil.webFormat)));
    }

    private List<SiteLook> looksRecentDays(int day){
        Date curDate=DateUtil.getDayBegin(DateUtil.now());
        Date beforeWeekDate= DateUtil.addDays(curDate,-day);
        LambdaQueryWrapper<SiteLook> wrapper=Wrappers.lambdaQuery();
        wrapper.select(SiteLook::getId,SiteLook::getUserIp,SiteLook::getCreateTime);
        wrapper.ge(SiteLook::getCreateTime,beforeWeekDate);
        wrapper.lt(SiteLook::getCreateTime,curDate);
        return super.list(wrapper);
    }

}
