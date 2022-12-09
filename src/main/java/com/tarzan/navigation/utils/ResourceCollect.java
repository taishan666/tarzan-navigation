package com.tarzan.navigation.utils;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 文章采集
 * @author tarzan
 * @date 2021/5/31
 */
@Data
public class ResourceCollect {
    
    private  static  List<Resource>  list=new ArrayList<>();

    //网站地址
    private static String webUrl="https://download.csdn.net";

    private static String userId="ncusz";


    public static void main(String[] args) {
        new ResourceCollect().collect();
        double money=0;
        list.sort(Comparator.comparing(Resource::getDownloads));
        for (Resource resource : list) {
            money=money+(resource.getMoney()*resource.getDownloads());
            System.out.println(resource);
        }
        System.out.println("有收益的文件数："+list.size()+"个");
        System.out.println("收入总金额为："+money+"元");
        System.out.println("实际收入总金额为："+(money*0.6)+"元");
    }




    public  void  collect(){
        int pageNum=0;
        while (true){
            pageNum++;
            if(!readPage(webUrl,pageNum)){
                break;
            }
        }
    }


    /**
     * @param url 访问路径
     * @return
     */
    public static Document getDocument(String url) {
        try {
            //5000是设置连接超时时间，单位ms
            return Jsoup.connect(url).timeout(5000).get();
        } catch (IOException e) {
            return null;
        }
    }



    public static Resource readArticle(String url,String money) {
        Resource resource=new Resource();
        Document doc=  getDocument(url);
        if(doc==null){
            return null;
        }
        Elements elements = doc.select("div[class=mt-8 t-c-second line-h-1 flex flex-hc]").select("span");
        String text=elements.text();
        int index=0;
        if (text.contains("所需积分")){
            return null;
        }
        if(text.contains("星")){
            index=1;
            if(text.contains("· 超过")){
                index=2;
            }
            resource.setRemark(elements.get(0).text());
        }
        Elements vips = doc.select("i[class=icon-is-vip]");
        if(vips.size()>0){
            return null;
        }
        Element views = elements.get(0+index);
        Element downloads = elements.get(1+index);
        Element uploadTime = elements.get(3+index);
        resource.setUrl(url);
        resource.setMoney(Double.valueOf(money));
        resource.setViews(views.text());
        String downloadstr=downloads.text().split("·")[1];
        int num=Integer.valueOf(downloadstr);
        if(num==0){
            return null;
        }
        resource.setDownloads(Integer.valueOf(downloadstr));
        resource.setUploadTime(uploadTime.text());
       // System.out.println(resource);
        list.add(resource);
        return resource;
    }

    public  boolean readPage(String webUrl,int pageNum) {
        Document doc = getDocument(webUrl+"/user/"+userId+"/uploads/"+pageNum);
        if(doc!=null){
            // 获取目标HTML代码
            Elements elements = doc.select("div[class=my_resource]");
            //文章列表
            Elements resources = elements.select("div[class=content]");
            if (resources.size() == 0) {
                return false;
            }
            System.out.println("第"+pageNum+"页数据读取。。。。。。。。。。。。。。。。。。。。");
            resources.forEach(e -> {
                String url = e.select("a").attr("href");
                String money = e.select("div[class=score]").text();
                 readArticle(webUrl+url,money.split("：")[1]);
        /*    try {
                //等待3秒
                Thread.sleep(200);
            } catch (InterruptedException interruptedException) {
                System.out.println("线程中断故障");
            }*/
            });

        }
        return true;
    }


    @Data
   static class Resource{
        private String url;
        private String remark;
        private Double money;
        private String uploadTime;
        private String views;
        private Integer downloads;

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", money='" + money + '\'' +
                    ", uploadTime='" + uploadTime + '\'' +
                    ", views='" + views + '\'' +
                    ", downloads='" + downloads + '\'' +
                    '}';
        }
    }

}
