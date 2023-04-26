package com.tarzan.nav.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.modules.admin.vo.HotNewsVO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 百度热点
 *
 * @author tarzan
 * @version 1.0
 * @copyright (c) 2019 LuoYang TuLian Co'Ltd Inc. All rights reserved.
 * @date 2020/5/31$ 16:48$
 * @since JDK1.8
 */
public class BaiduHot {

    /**
     *
     * @param url 访问路径
     * @return
     */
    public static Document getDocument (String url){
        try {
            //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
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
        System.out.println(hotNews);
    }

}