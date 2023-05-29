package com.tarzan.nav.modules.admin.service.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.mapper.biz.SiteLookMapper;
import com.tarzan.nav.modules.admin.mapper.biz.WebsiteMapper;
import com.tarzan.nav.modules.admin.model.biz.SiteLook;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.network.LocationService;
import com.tarzan.nav.utils.DateUtil;
import com.tarzan.nav.utils.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author TARZAN
 */
@Service
@Slf4j
public class SiteLookService extends ServiceImpl<SiteLookMapper, SiteLook> {

    @Resource
    private  WebsiteMapper websiteMapper;

    Cache<String, String> cache = Caffeine.newBuilder()
            .initialCapacity(5)
            // 超出时淘汰
            .maximumSize(100000)
            //设置写缓存后n秒钟过期
            .expireAfterWrite(60, TimeUnit.SECONDS)
            //设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
            //.expireAfterAccess(17, TimeUnit.SECONDS)
            .build();

    public Set<Integer> topSites(int num) {
        List<SiteLook> looks= super.lambdaQuery().ne(SiteLook::getSiteId, CoreConst.ZERO).list();
        Map<Integer,Long> lookMap=looks.stream().collect(Collectors.groupingBy(SiteLook::getSiteId,Collectors.counting()));
        return MapUtil.topNByValue(lookMap,num).keySet();
    }

    private static Map<String, Long> buildRecentDayMap(int day) {
        Date now = DateUtil.now();
        LinkedHashMap<String, Long> map = new LinkedHashMap<>(7);
        for (int i = day; i >= 1; i--) {
            Long count = 0L;
            map.put(DateUtil.format(DateUtil.addDays(now, -i), DateUtil.webFormat), count);
        }
        return map;
    }

    @Async
    public void asyncLook(Integer siteId,String userIp,String type) {
        boolean check = this.checkArticleLook(siteId, userIp);
        if (check) {
            log.info(userIp+"查看"+siteId+"一次！");
            SiteLook siteLook = new SiteLook();
            siteLook.setSiteId(siteId);
            siteLook.setUserIp(userIp);
            siteLook.setProvince(LocationService.getProvince(userIp));
            siteLook.setType(type);
            super.save(siteLook);
        }
        cache.put(userIp+"_"+siteId,"1");
    }

    @Async
    public void asyncLook(String url,String userIp,String type) {
        Website site=websiteMapper.selectOne(Wrappers.<Website>lambdaQuery().select(Website::getId).eq(Website::getUrl,url).last("limit 1"));
        if(Objects.nonNull(site)){
            asyncLook(site.getId(),userIp,type);
        }
    }


    public boolean checkArticleLook(Integer siteId, String userIp) {
        return cache.getIfPresent(userIp+"_"+siteId)==null;
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

    public  JSONArray usersByProv(List<SiteLook> lookList){
        String jsonStr="[{name:'北京',value:0},{name:'天津',value:0},{name:'上海',value:0},{name:'重庆',value:0},{name:'河北',value:0},{name:'河南',value:0},{name:'云南',value:0},{name:'辽宁',value:0},{name:'黑龙江',value:0},{name:'湖南',value:0},{name:'安徽',value:0},{name:'山东',value:0},{name:'新疆',value:0},{name:'江苏',value:0},{name:'浙江',value:0},{name:'江西',value:0},{name:'湖北',value:0},{name:'广西',value:0},{name:'甘肃',value:0},{name:'山西',value:0},{name:'内蒙古',value:0},{name:'陕西',value:0},{name:'吉林',value:0},{name:'福建',value:0},{name:'贵州',value:0},{name:'广东',value:0},{name:'青海',value:0},{name:'西藏',value:0},{name:'四川',value:0},{name:'宁夏',value:0},{name:'海南',value:0},{name:'台湾',value:0},{name:'香港',value:0},{name:'澳门',value:0}]";
        JSONArray array= JSON.parseArray(jsonStr);
        Map<String,Long> map= lookList.stream().collect(Collectors.groupingBy(e->e.getProvince().substring(0,e.getProvince().length()-1),Collectors.counting()));
        if(CollectionUtils.isNotEmpty(map)){
            for (int i = 0; i < array.size(); i++) {
                JSONObject json=array.getJSONObject(i);
                Long userNum=map.get(json.getString("name"));
                if(Objects.nonNull(userNum)){
                    json.put("value",userNum);
                }
            }
        }
        return array;
    }

    public Map<String,List<SiteLook>> looksGroupMap(List<SiteLook> lookList,int day){
        Date beforeDate= DateUtil.addDays(DateUtil.now(),-day);
        List<SiteLook> list=lookList.stream().filter(e->beforeDate.compareTo(e.getCreateTime()) < 0).collect(Collectors.toList());
        return list.stream().collect(Collectors.groupingBy(e->DateUtil.format(e.getCreateTime(), DateUtil.webFormat)));
    }


}
