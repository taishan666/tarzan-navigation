package com.tarzan.navigation.modules.admin.service.biz;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.modules.admin.mapper.biz.ImageMapper;
import com.tarzan.navigation.modules.admin.mapper.biz.LinkMapper;
import com.tarzan.navigation.modules.admin.model.biz.BizImage;
import com.tarzan.navigation.modules.admin.model.biz.Link;
import com.tarzan.navigation.utils.JsoupUtil;
import com.tarzan.navigation.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
@Slf4j
public class LinkService extends ServiceImpl<LinkMapper, Link> {

    private final ImageMapper imageMapper;

    public boolean saveByUrl(String url,Integer categoryId){
        Link link=new Link();
        link.setUrl(url);
        link.setCategoryId(categoryId);
        Document doc= JsoupUtil.getDocument(url);
        assert doc != null;
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
        link.setName(title);
        link.setDescription(desc);
        Date date = new Date();
        link.setCreateTime(date);
        link.setUpdateTime(date);
        BizImage image=new BizImage();
        String iconUrl=getWebIcon(url,doc);
        image.setBase64(getBase64(iconUrl));
        imageMapper.insert(image);
        link.setImageId(image.getId());
       return super.save(link);
    }

    public static boolean checkImageExist(String src){
        try {
            URL url=new URL(src);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode()==200){
                return true;
            }
        } catch (IOException e) {
           // e.printStackTrace();
        }
        return false;
    }

    public static String getIconHref(String url,Element iconEle){
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
    public static String getDomain(String url){
        int fromIndex=url.indexOf(".");
        int endIndex=url.indexOf("/",fromIndex);
        return url.substring(0,endIndex+1);
    }

    public static String getWebIcon(String url, Document doc){
        url =StringUtils.appendIfMissing(url,"/");
        String iconUrl=getDomain(url)+"favicon.ico";
        Element iconEle=doc.selectFirst("[rel=icon]");
        Element shortIconEle=doc.selectFirst("[rel=shortcut icon]");
        String iconHref=getIconHref(url,iconEle);
        String shortIconHref=getIconHref(url,shortIconEle);
        if(checkImageExist(iconHref)){
           return iconHref;
        }
        if(checkImageExist(shortIconHref)){
            return shortIconHref;
        }
        if(checkImageExist(iconUrl)){
            return iconUrl;
        }
        return null;
    }

    public static String getBase64(String src) {
        return StringUtil.isEmpty(src)?"":"data:image/ico;base64,"+ Base64.getEncoder().encodeToString(HttpUtil.downloadBytes(src));
    }

    public IPage<Link> pageLinks(Link link, Integer pageNumber, Integer pageSize) {
        IPage<Link> page = new Page<>(pageNumber, pageSize);
        return page(page,Wrappers.<Link>lambdaQuery()
                .like(StringUtils.isNotBlank(link.getName()), Link::getName, link.getName())
                .like(StringUtils.isNotBlank(link.getUrl()), Link::getUrl, link.getUrl())
                .eq(Objects.nonNull(link.getStatus()), Link::getStatus, link.getStatus())
                .eq(Objects.nonNull(link.getCategoryId()), Link::getCategoryId, link.getCategoryId())
                .orderByDesc(Link::getCreateTime));
    }

    @CacheEvict(value = {"link","category"}, allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

    public void wrapper(Link link){
        BizImage img=imageMapper.selectById(link.getImageId());
        if(img!=null){
            link.setImg(img);
        }
    }

    public void wrapper(List<Link> links){
        Set<Integer> imageIds=links.stream().map(Link::getImageId).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(imageIds)){
            List<BizImage> images= imageMapper.selectBatchIds(imageIds);
            Map<Integer,BizImage> map=images.stream().collect(Collectors.toMap(BizImage::getId, e->e));
            links.forEach(e->e.setImg(map.get(e.getImageId())));
        }
    }


    public Map<Integer,List<Link>> getCategoryLinkMap(){
        List<Link> links=super.list();
        this.wrapper(links);
        if(CollectionUtils.isNotEmpty(links)){
            links=links.stream().filter(e->Objects.nonNull(e.getCategoryId())).collect(Collectors.toList());
            return links.stream().collect(Collectors.groupingBy(Link::getCategoryId));
        }
        return new HashMap<>(0);
    }
}
