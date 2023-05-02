package com.tarzan.nav.modules.network;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.modules.admin.vo.HotNewsVO;
import com.tarzan.nav.utils.JsoupUtil;
import com.tarzan.nav.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
@Slf4j
public class HotNewsService {

    @Cacheable(value = "hotNews", key = "'baidu'")
    public List<HotNewsVO> baiduHot(){
        log.info("百度热点");
        Document doc= JsoupUtil.getDocument("https://top.baidu.com/board?tab=realtime");
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        // 获取目标HTML代码
        assert doc != null;
        Elements news = doc.select("div[class=category-wrap_iQLoo horizontal_1eKyQ]");
        for (Element hot : news) {
            Elements hotTitle=hot.select("div[class=c-single-text-ellipsis]");
            String title=  hotTitle.get(0).text();
            Elements hotA=hot.select("a");
            String link=  hotA.get(0).attr("href");
            Elements hotIndex=hot.select("div[class^=hot-index_]");
            String index=  hotIndex.get(0).text();
            HotNewsVO vo=new HotNewsVO();
            vo.setTitle(title);
            vo.setLink(link);
            vo.setIndex(numberFormat(Integer.parseInt(index)));
            hotNews.add(vo);
        }
        return hotNews;
    }

    @Cacheable(value = "hotNews", key = "'weibo'")
    public List<HotNewsVO> weiboHot(){
        log.info("微博热搜");
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.get("https://weibo.com/ajax/statuses/hot_band");
        if(StringUtil.isNotBlank(text)){
            JSONObject json= JSON.parseObject(text);
            JSONObject data=json.getJSONObject("data");
            JSONArray list=data.getJSONArray("band_list");
            Iterator<Object> iterator= list.stream().iterator();
            while (iterator.hasNext()){
                JSONObject e= (JSONObject) iterator.next();
                if(e.get("realpos")!=null){
                    String title=e.getString("note");
                    Integer index=e.getInteger("raw_hot");
                    String link="https://s.weibo.com/weibo?q=%23"+title+"%23";
                    HotNewsVO vo=new HotNewsVO();
                    vo.setTitle(title);
                    vo.setLink(link);
                    vo.setIndex(numberFormat(index));
                    hotNews.add(vo);
                }
            }
        }
        return hotNews;
    }

    @Cacheable(value = "hotNews", key = "'douyin'")
    public List<HotNewsVO> douYinHot(){
        log.info("抖音热点");
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.createGet("https://www.douyin.com/aweme/v1/web/hot/search/list")
                .cookie("passport_csrf_token=7aab835a69454ba4349a2dcc5c6e0efb")
                .execute().body();
        JSONObject json= JSON.parseObject(text);
        JSONObject data=json.getJSONObject("data");
        JSONArray list=data.getJSONArray("word_list");
        Iterator<Object> iterator= list.stream().iterator();
        while (iterator.hasNext()){
            JSONObject e= (JSONObject) iterator.next();
            String title=e.getString("word");
            String link="https://www.douyin.com/hot/"+e.getString("sentence_id");
            Integer index=e.getInteger("hot_value");
            HotNewsVO vo=new HotNewsVO();
            vo.setTitle(title);
            vo.setLink(link);
            vo.setIndex(numberFormat(index));
            hotNews.add(vo);
        }
        return hotNews;
    }

    @Cacheable(value = "hotNews", key = "'juejin'")
    public List<HotNewsVO> jueJinHot(){
        log.info("掘金热榜");
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.get("https://api.juejin.cn/content_api/v1/content/article_rank?category_id=1&type=hot");
        JSONObject json= JSON.parseObject(text);
        JSONArray list=json.getJSONArray("data");
        Iterator<Object> iterator= list.stream().iterator();
        while (iterator.hasNext()){
            JSONObject e= (JSONObject) iterator.next();
            String title=e.getJSONObject("content").getString("title");
            String link="https://www.douyin.com/hot/"+e.getJSONObject("content").getString("content_id");
            Integer index=e.getJSONObject("content_counter").getInteger("hot_rank");
            HotNewsVO vo=new HotNewsVO();
            vo.setTitle(title);
            vo.setLink(link);
            vo.setIndex(numberFormat(index));
            hotNews.add(vo);
        }
        return hotNews;
    }

    @Cacheable(value = "hotNews", key = "'csdn'")
    public List<HotNewsVO> cSdnHot(){
        log.info("csdn热榜");
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.get("https://blog.csdn.net/phoenix/web/blog/hot-rank?page=0&pageSize=25&type=");
        JSONObject json= JSON.parseObject(text);
        JSONArray list=json.getJSONArray("data");
        Iterator<Object> iterator= list.stream().iterator();
        while (iterator.hasNext()){
            JSONObject e= (JSONObject) iterator.next();
            String title=e.getString("articleTitle");
            String link=e.getString("articleDetailUrl");
            Integer index=e.getInteger("hotRankScore");
            HotNewsVO vo=new HotNewsVO();
            vo.setTitle(title);
            vo.setLink(link);
            vo.setIndex(numberFormat(index));
            hotNews.add(vo);
        }
        return hotNews;
    }

    private String numberFormat(int num){
        DecimalFormat df = new DecimalFormat("#0.0万");
        return df.format(num / 10000.0);
    }
}
