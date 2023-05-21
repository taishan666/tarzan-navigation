package com.tarzan.nav.modules.admin.service.biz;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private final static String POST_API="https://blog.csdn.net/community/home-api/v1/get-business-list?page=1&size=1&businessType=blog&username=weixin_40986713&articleId=";

    private final ImageService imageService;

    public  Website getPost(String url){
        if(StringUtil.isNotBlank(url)){
            int index=url.lastIndexOf("/");
            String articleId=url.substring(index+1,index+10);
            System.out.println(articleId);
            Website post=new Website();
            String result= HttpUtil.get(POST_API+articleId);
            JSONObject json= JSON.parseObject(result);
            if(json.getInteger("code")==200){
                JSONObject data= json.getJSONObject("data");
                JSONArray list=data.getJSONArray("list");
                JSONObject postJson=list.getJSONObject(0);
                post.setStatus(1);
                String title=postJson.getString("title");
                post.setName(title);
                String desc=postJson.getString("description");
                post.setUrl(url);
                post.setDescription(desc);
                JSONArray picList=postJson.getJSONArray("picList");
                if(picList.size()>0){
                    String coverUrl=picList.getString(0);
                    System.out.println(coverUrl);
                    BizImage image=imageService.upload(coverUrl+",image/png");
                    post.setImageId(image.getId());
                }
                System.out.println(post);
                return post;
            }
        }
        return null;
    }

}
