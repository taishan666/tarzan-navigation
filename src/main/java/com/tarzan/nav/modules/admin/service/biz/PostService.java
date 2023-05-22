package com.tarzan.nav.modules.admin.service.biz;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.vo.CsdnArticleVO;
import com.tarzan.nav.utils.JsoupUtil;
import com.tarzan.nav.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PostService {

    private final static String POST_API="https://blog.csdn.net/community/home-api/v1/get-business-list?page=1&size=9999&businessType=blog&username=";

    private final ImageService imageService;

    public  Website getPost(String url){
        if(StringUtil.isNotBlank(url)){
            String username="";
            if(!url.contains("//blog")){
               Document doc=JsoupUtil.getDocument(url);
                url=doc.selectFirst("[rel=canonical]").attr("href");
            }
            if(url.contains("//blog")){
                int start=0;
                if(url.contains("https:")){
                    start=21;
                }
                if(url.contains("http:")){
                    start=20;
                }
                int end=url.indexOf("/article");
                 username=url.substring(start+1,end);
                System.out.println(username);
            }
            int index=url.lastIndexOf("/");
            String articleId=url.substring(index+1,index+10);
            System.out.println(articleId);
            Website post=new Website();
            String result= HttpUtil.get(POST_API+username);
            JSONObject json= JSON.parseObject(result);
            if(json.getInteger("code")==200){
                JSONObject data= json.getJSONObject("data");
                JSONArray list=data.getJSONArray("list");
                List<CsdnArticleVO> articles=list.toJavaList(CsdnArticleVO.class);
                CsdnArticleVO articleVO=articles.stream().filter(e->e.getArticleId().contains(articleId)).findFirst().orElse(null);
                if(Objects.nonNull(articleVO)){
                    post.setStatus(1);
                    post.setName(articleVO.getTitle());
                    post.setUrl(url);
                    String desc=articleVO.getDescription();
                    if(desc.length()>255){
                        desc=desc.substring(0,254);
                    }
                    post.setDescription(desc);
                    if(CollectionUtils.isNotEmpty(Arrays.asList(articleVO.getPicList()))){
                        String coverUrl=articleVO.getPicList()[0];
                        System.out.println(coverUrl);
                        BizImage image=imageService.upload(coverUrl+",image/png");
                        post.setImageId(image.getId());
                    }
                    System.out.println(post);
                    return post;
                }
            }
        }
        return null;
    }

}
