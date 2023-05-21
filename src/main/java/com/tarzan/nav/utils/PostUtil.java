/*
package com.tarzan.nav.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Website;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PostUtil {
   static String postApi="https://blog.csdn.net/community/home-api/v1/get-business-list?page=1&size=1&businessType=blog&username=weixin_40986713&articleId=";

    public static Website getPost(String url){
        if(StringUtil.isNotBlank(url)){
            String articleId=url.substring(url.lastIndexOf("/"),9);
            Website post=new Website();
            String result= HttpUtil.get(postApi+articleId);
            JSONObject json= JSON.parseObject(result);
            if(json.getInteger("code")==200){
                JSONObject data= json.getJSONObject("data");
                JSONArray list=data.getJSONArray("list");
                JSONObject postJson=list.get(0)
                post.setStatus(1);
                String title=postJson.getString("title");
                post.setName(title);
                String desc=postJson.getString("description");
                post.setUrl(url);
                post.setDescription(desc);
                BizImage image=imageService.upload(webIconUrl);
                website.setImageId(image.getId());
            }
            Document doc=JsoupUtil.getDocument(url);
            System.out.println(doc.html());

            post.setImageId("");
            //   System.out.println(post);
            return post;
        }
        return null;
    }

    public static void main(String[] args) {
     //   getPost("https://blog.csdn.net/weixin_40986713/article/details/130771147");

        String url="https://blog.csdn.net/community/home-api/v1/get-business-list?page=1&size=1&businessType=blog&username=weixin_40986713&articleId=128725567";
        String html= HttpUtil.get(url);
        System.out.println(html);
    }
}
*/
