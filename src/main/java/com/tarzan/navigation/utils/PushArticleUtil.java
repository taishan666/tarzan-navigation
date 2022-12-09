package com.tarzan.navigation.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文章推送到百度工具类
 *
 * @author tarzan
 * @version V1.0
 * @date 2021年9月11日
 */
@Slf4j
@UtilityClass
public class PushArticleUtil {

    /*推送百度*/
    public static String postBaidu(String postUrl, String[] parameters) {
        if (null == postUrl || null == parameters || parameters.length == 0) {
            return null;
        }
        StringBuilder result = new StringBuilder();

        //建立URL之间的连接
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(postUrl).openConnection();
            //设置通用的请求属性
            conn.setRequestProperty("Host", "data.zz.baidu.com");
            conn.setRequestProperty("User-Agent", "curl/7.12.1");
            conn.setRequestProperty("Content-Length", "83");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestMethod("POST");
            //发送POST请求必须设置如下两行
            conn.setDoInput(true);
            conn.setDoOutput(true);
        } catch (IOException e) {
            log.error("【推送百度】建立URL之间的连接失败:{}", e.getMessage(), e);
        }
        try (PrintWriter out = new PrintWriter(conn.getOutputStream())) {
            //发送请求参数
            StringBuilder param = new StringBuilder();
            for (String s : parameters) {
                param.append(s).append('\n');
            }
            out.print(param.toString().trim());
            //进行输出流的缓冲
            out.flush();
            //通过BufferedReader输入流来读取Url的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            log.error("推送百度出现异常:{}", e.getMessage(), e);
        }
        return result.toString();
    }
}
