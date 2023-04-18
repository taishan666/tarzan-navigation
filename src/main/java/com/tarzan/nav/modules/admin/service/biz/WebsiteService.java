package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.admin.mapper.biz.WebsiteMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.utils.JsoupUtil;
import com.tarzan.nav.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

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
public class WebsiteService extends ServiceImpl<WebsiteMapper, Website> {

    private final ImageService imageService;

    public boolean saveByUrl(String url,Integer categoryId){
        Website website=new Website();
        website.setUrl(url);
        website.setCategoryId(categoryId);
        Document doc= JsoupUtil.getDocument(url);
        assert doc != null;
        String title=JsoupUtil.getTitle(doc);
        String desc=JsoupUtil.getDescription(doc);
        if(StringUtil.isNotBlank(title)){
            if(title.length()>50){
                title= title.substring(0,50);
            }
        }
        website.setName(title);
        website.setDescription(desc);
        String webIconUrl=JsoupUtil.getWebIcon(doc);
        BizImage image=imageService.upload(webIconUrl);
        website.setImageId(image.getId());
       return super.save(website);
    }

    public List<Website> list(Website website) {
        return super.lambdaQuery().like(Website::getName,website.getName()).orderByDesc(Website::getCreateTime).list();
    }

    public IPage<Website> pageList(Website website, Integer pageNumber, Integer pageSize) {
        IPage<Website> page = new Page<>(pageNumber, pageSize);
        return page(page,Wrappers.<Website>lambdaQuery()
                .like(StringUtils.isNotBlank(website.getName()), Website::getName, website.getName())
                .like(StringUtils.isNotBlank(website.getUrl()), Website::getUrl, website.getUrl())
                .eq(Objects.nonNull(website.getStatus()), Website::getStatus, website.getStatus())
                .eq(Objects.nonNull(website.getCategoryId()), Website::getCategoryId, website.getCategoryId())
                .orderByDesc(Website::getCreateTime));
    }

    @CacheEvict(value = {"website","category"}, allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

    public void wrapper(Website website){
        BizImage img=imageService.getById(website.getImageId());
        if(img!=null){
            website.setImg(img);
        }
    }

    public void wrapper(List<Website> websites){
        Set<String> imageIds=websites.stream().map(Website::getImageId).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(imageIds)){
            List<BizImage> images= imageService.listByIds(imageIds);
            Map<String,BizImage> map=images.stream().collect(Collectors.toMap(BizImage::getId, e->e));
            websites.forEach(e->e.setImg(map.get(e.getImageId())));
        }
    }


    public Map<Integer,List<Website>> getCategoryWebsiteMap(){
        List<Website> websites=super.list();
        this.wrapper(websites);
        if(CollectionUtils.isNotEmpty(websites)){
            websites=websites.stream().filter(e->Objects.nonNull(e.getCategoryId())).collect(Collectors.toList());
            return websites.stream().collect(Collectors.groupingBy(Website::getCategoryId));
        }
        return new HashMap<>(0);
    }
}
