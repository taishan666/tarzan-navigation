package com.tarzan.nav.modules.admin.service.network;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.modules.admin.vo.HotNewsVO;
import com.tarzan.nav.utils.JsoupUtil;
import com.tarzan.nav.utils.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Lenovo
 */
@Service
public class HotNewsService {

    public List<HotNewsVO> baiduHot(){
        Document doc= JsoupUtil.getDocument("https://top.baidu.com/board?tab=realtime");
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        // 获取目标HTML代码
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
            vo.setIndex(index);
            hotNews.add(vo);
        }
        return hotNews;
    }

    public List<HotNewsVO> weiboHot(){
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.get("https://weibo.com/ajax/statuses/hot_band");
        if(StringUtil.isNotBlank(text)){
            JSONObject json= JSON.parseObject(text);
            JSONObject data=json.getJSONObject("data");
            JSONArray list=data.getJSONArray("band_list");
            Iterator<JSONObject> iterator= list.stream().iterator();
            while (iterator.hasNext()){
                JSONObject e=iterator.next();
                String title=e.getString("note");
                String index=e.getString("raw_hot");
                String link="https://s.weibo.com/weibo?q=%23"+title+"%23";
                HotNewsVO vo=new HotNewsVO();
                vo.setTitle(title);
                vo.setLink(link);
                vo.setIndex(index);
                hotNews.add(vo);
            }
        }
        return hotNews;
    }

    public List<HotNewsVO> douYinHot(){
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.createGet("https://www.douyin.com/aweme/v1/web/hot/search/list")
                .cookie("passport_csrf_token=7aab835a69454ba4349a2dcc5c6e0efb")
                .execute().body();
        JSONObject json= JSON.parseObject(text);
        JSONObject data=json.getJSONObject("data");
        JSONArray list=data.getJSONArray("word_list");
        Iterator<JSONObject> iterator= list.stream().iterator();
        while (iterator.hasNext()){
            JSONObject e=iterator.next();
            String title=e.getString("word");
            String link="https://www.douyin.com/hot/"+e.getString("sentence_id");
            String index=e.getString("hot_value");
            HotNewsVO vo=new HotNewsVO();
            vo.setTitle(title);
            vo.setLink(link);
            vo.setIndex(index);
            hotNews.add(vo);
        }
        return hotNews;
    }

    public List<HotNewsVO> jueJinHot(){
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.get("https://api.juejin.cn/content_api/v1/content/article_rank?category_id=1&type=hot");
        JSONObject json= JSON.parseObject(text);
        JSONArray list=json.getJSONArray("data");
        Iterator<JSONObject> iterator= list.stream().iterator();
        while (iterator.hasNext()){
            JSONObject e=iterator.next();
            String title=e.getJSONObject("content").getString("title");
            String link="https://www.douyin.com/hot/"+e.getJSONObject("content").getString("content_id");
            String index=e.getJSONObject("content_counter").getString("hot_rank");
            HotNewsVO vo=new HotNewsVO();
            vo.setTitle(title);
            vo.setLink(link);
            vo.setIndex(index);
            hotNews.add(vo);
        }
        return hotNews;
    }

    public List<HotNewsVO> cSDNHot(){
        List<HotNewsVO> hotNews=new ArrayList<>(10);
        String text= HttpUtil.get("https://blog.csdn.net/phoenix/web/blog/hot-rank?page=0&pageSize=25&type=");
        JSONObject json= JSON.parseObject(text);
        JSONArray list=json.getJSONArray("data");
        Iterator<JSONObject> iterator= list.stream().iterator();
        while (iterator.hasNext()){
            JSONObject e=iterator.next();
            String title=e.getString("articleTitle");
            String link=e.getString("articleDetailUrl");
            String index=e.getString("hotRankScore");
            HotNewsVO vo=new HotNewsVO();
            vo.setTitle(title);
            vo.setLink(link);
            vo.setIndex(index);
            hotNews.add(vo);
        }
        return hotNews;
    }
}
