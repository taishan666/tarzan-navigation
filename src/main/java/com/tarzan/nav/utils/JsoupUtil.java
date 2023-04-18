package com.tarzan.nav.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

/**
 * @author Lenovo
 */
@Slf4j
public class JsoupUtil {

    /**
     *
     * @param url 访问路径
     * @return
     */
    public static Document getDocument (String url){
        try {
            //先调用下忽略https证书的再请求才可以
            HttpsUrlValidator.retrieveResponseFromServer(url);
            //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1")
                    .timeout(5*1000).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTitle(Document doc){
        String title=doc.title();
        if(StringUtil.isEmpty(title)){
            Element titleEle=doc.selectFirst("title");
            if(Objects.nonNull(titleEle)){
                title= titleEle.text();
            }else {
                titleEle=doc.selectFirst("[property=og:title]");
                title=titleEle.attr("content");
            }
        }
        return title;
    }

    public static String getDescription(Document doc){
        Element descriptionEle=doc.selectFirst("[name=description]");
        String desc="";
        if(Objects.nonNull(descriptionEle)){
            desc= descriptionEle.attr("content");
        }else{
            descriptionEle=doc.selectFirst("[property=og:description]");
            if(Objects.nonNull(descriptionEle)){
                desc= descriptionEle.attr("content");
            }
        }
        return desc;
    }

    public static String getWebIcon(Document doc){
        String url = StringUtils.appendIfMissing(doc.baseUri(), File.separator);
        String iconUrl=getDomain(url)+"favicon.ico";
        Element iconEle=doc.selectFirst("[rel=icon]");
        Element shortIconEle=doc.selectFirst("[rel=shortcut icon]");
        String iconHref=getIconHref(url,iconEle);
        String shortIconHref=getIconHref(url,shortIconEle);
        String result1=checkImageExist(iconHref);
        if(result1!=null){
            return iconHref+","+result1;
        }
        String result2=checkImageExist(shortIconHref);
        if(result2!=null){
            return shortIconHref+","+result2;
        }
        String result3=checkImageExist(iconUrl);
        if(result3!=null){
            return iconUrl+","+result3;
        }
        return null;
    }

    public static String getDomain(String url){
        int fromIndex=url.indexOf(".");
        int endIndex=url.indexOf("/",fromIndex);
        return url.substring(0,endIndex+1);
    }

    private static String getIconHref(String url,Element iconEle){
        if(Objects.nonNull(iconEle)){
            String iconHref= iconEle.attr("href");
            if(iconHref.startsWith("http")||iconHref.startsWith("https")){
                return iconHref;
            }else {
                if(iconHref.startsWith("//")){
                    return url.split(":")[0]+":"+iconHref;
                }else {
                    return getDomain(url)+iconHref;
                }
            }
        }else {
            return null;
        }
    }

    public static String checkImageExist(String src){
        try {
            URL url=new URL(src);
            URLConnection urlConnection= url.openConnection();
            //设置读取超时
            urlConnection.setReadTimeout(1000 * 60);
            urlConnection.setRequestProperty("Accept", "*/*");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            urlConnection.setRequestProperty("Accept-Language", "zh-cn");
            urlConnection.setRequestProperty("Connection", "close"); //不进行持久化连接
            HttpURLConnection connection= (HttpURLConnection) urlConnection;
            int code=connection.getResponseCode();
            String contentType=connection.getContentType();
            if(code==200&&contentType.startsWith("image")){
                return contentType;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
