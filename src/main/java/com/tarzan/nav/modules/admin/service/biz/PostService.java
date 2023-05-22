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

import java.util.*;

@Service
@AllArgsConstructor
public class PostService {

    private final static String POST_API="https://blog.csdn.net/community/home-api/v1/get-business-list?businessType=blog";

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
                CsdnArticleVO articleVO=getArticle(username,articleId);
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
        return null;
    }

    private CsdnArticleVO getArticle1(String username,String articleId){
        String result= HttpUtil.get(POST_API+"&size=100&page=1&&username="+username);
        JSONObject json= JSON.parseObject(result);
        List<CsdnArticleVO> articles=new ArrayList<>(100);
        if(json.getInteger("code")==200){
            JSONObject data= json.getJSONObject("data");
            JSONArray list=data.getJSONArray("list");
            int total=data.getInteger("total");
            articles.addAll(list.toJavaList(CsdnArticleVO.class));
            if(total>100){
                int pages=total/100+1;
                for (int i = 2; i < pages+1; i++) {
                    String body= HttpUtil.get(POST_API+"&size=100&page="+i+"&&username="+username);
                    JSONObject json1= JSON.parseObject(body);
                    JSONObject data1= json1.getJSONObject("data");
                    JSONArray list1=data1.getJSONArray("list");
                    articles.addAll(list1.toJavaList(CsdnArticleVO.class));
                }
            }
        }
        return articles.stream().filter(e->e.getArticleId().contains(articleId)).findFirst().orElse(null);
    }

    private CsdnArticleVO getArticle(String username,String articleId){
        String result= HttpUtil.get(POST_API+"&size=100&page=1&&username="+username);
        JSONObject json= JSON.parseObject(result);
        if(json.getInteger("code")==200){
            JSONObject data= json.getJSONObject("data");
            JSONArray list=data.getJSONArray("list");
            List<CsdnArticleVO> articles=list.toJavaList(CsdnArticleVO.class);
            Optional<CsdnArticleVO> optional= articles.stream().filter(e->e.getArticleId().contains(articleId)).findFirst();
            if(optional.isPresent()){
                return optional.get();
            }
            int total=data.getInteger("total");
            if(total>100){
                int pages=total/100+1;
                for (int i = 2; i < pages+1; i++) {
                    String body= HttpUtil.get(POST_API+"&size=100&page="+i+"&&username="+username);
                    JSONObject json1= JSON.parseObject(body);
                    JSONObject data1= json1.getJSONObject("data");
                    JSONArray list1=data1.getJSONArray("list");
                    List<CsdnArticleVO> articles1=list1.toJavaList(CsdnArticleVO.class);
                    Optional<CsdnArticleVO> optional1= articles1.stream().filter(e->e.getArticleId().contains(articleId)).findFirst();
                    if(optional1.isPresent()){
                        return optional1.get();
                    }
                }
            }
        }
        return null;
    }

}
