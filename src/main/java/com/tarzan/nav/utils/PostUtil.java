package com.tarzan.nav.utils;

import com.tarzan.nav.modules.admin.model.biz.Website;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class PostUtil {

    public static Website getPost(String url){
        Website post=new Website();
        Document doc=JsoupUtil.getDocument(url);
        System.out.println(doc.html());
        post.setStatus(1);
        String title= doc.getElementById("articleContentId").text();
        post.setName(title);
        Element descriptionEle=doc.selectFirst("[name=description]");
        String desc=descriptionEle.attr("content");
        post.setUrl(url);
        post.setDescription(desc);
        post.setImageId("");
     //   System.out.println(post);
        return post;
    }

    public static void main(String[] args) {
        getPost("https://blog.csdn.net/weixin_40986713/article/details/130771147");
    }
}
