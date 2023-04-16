package com.tarzan.nav.modules.admin.service.biz;

import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.admin.mapper.biz.ImageMapper;
import com.tarzan.nav.modules.admin.mapper.biz.LinkMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Link;
import com.tarzan.nav.utils.JsoupUtil;
import com.tarzan.nav.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        String title=JsoupUtil.getTitle(doc);
        String desc=JsoupUtil.getDescription(doc);
        if(StringUtil.isNotBlank(title)){
            if(title.length()>50){
                title= title.substring(0,50);
            }
        }
        link.setName(title);
        link.setDescription(desc);
        BizImage image=new BizImage();
        String webIconUrl=JsoupUtil.getWebIcon(doc);
        image.setBase64(getBase64(webIconUrl));
        imageMapper.insert(image);
        link.setImageId(image.getId());
       return super.save(link);
    }

    public static String getBase64(String src) {
        return StringUtil.isEmpty(src)?"":"data:"+src.split(",")[1]+";base64,"+ Base64.getEncoder().encodeToString(HttpUtil.downloadBytes(src.split(",")[0]));
    }

    public List<Link> list(Link link) {
        return super.lambdaQuery().like(Link::getName,link.getName()).orderByDesc(Link::getCreateTime).list();
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
