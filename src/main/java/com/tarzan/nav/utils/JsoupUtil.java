package com.tarzan.nav.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author Lenovo
 */
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

}
