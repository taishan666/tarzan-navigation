package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
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
import org.springframework.cache.annotation.Cacheable;
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

    private static final int TITLE_LEN=50;

    @CacheEvict(value = {"website", "category"}, allEntries = true)
    public boolean saveByUrl(String url,Integer categoryId){
        Website website=new Website();
        website.setUrl(url);
        website.setCategoryId(categoryId);
        Document doc= JsoupUtil.getDocument(url);
        assert doc != null;
        String title=JsoupUtil.getTitle(doc);
        String desc=JsoupUtil.getDescription(doc);
        if(StringUtil.isNotBlank(title)){
            if(title.length()>TITLE_LEN){
                title= title.substring(0,TITLE_LEN);
            }
        }
        website.setName(title);
        website.setDescription(desc);
        String webIconUrl=JsoupUtil.getWebIcon(doc);
        BizImage image=imageService.upload(webIconUrl);
        website.setImageId(image.getId());
        website.setStatus(CoreConst.STATUS_VALID);
       return super.save(website);
    }

    @Cacheable(value = "website", key = "'simple'")
    public List<Website> simpleList() {
        return super.lambdaQuery().select(Website::getId,Website::getUrl).eq(Website::getStatus,CoreConst.STATUS_VALID).list();
    }

    @Cacheable(value = "website", key = "'all'")
    public List<Website> listAll() {
        return super.lambdaQuery().eq(Website::getStatus,CoreConst.STATUS_VALID).orderByDesc(Website::getCreateTime).list();
    }

    @CacheEvict(value = {"website", "category"}, allEntries = true)
    public boolean updateStatus(Integer id,Integer status) {
        Website website=new Website();
        website.setId(id);
        website.setStatus(status);
        return super.updateById(website);
    }

    public Website getByIdWithImage(Integer id){
        Website website=super.getById(id);
        if(Objects.nonNull(website.getImageId())){
            return this.wrapper(website);
        }
        return website;
    }

    public List<Website> listWithImage(Website website) {
        List<Website> websites= super.lambdaQuery().like(Website::getName,website.getName()).orderByDesc(Website::getCreateTime).list();
        return  this.wrapper(websites);
    }

    public IPage<Website> pageList(Website website, Integer pageNumber, Integer pageSize) {
        IPage<Website> page = new Page<>(pageNumber, pageSize);
        IPage<Website> websitePage=super.page(page,Wrappers.<Website>lambdaQuery()
                .like(StringUtils.isNotBlank(website.getName()), Website::getName, website.getName())
                .like(StringUtils.isNotBlank(website.getUrl()), Website::getUrl, website.getUrl())
                .eq(Objects.nonNull(website.getStatus()), Website::getStatus, website.getStatus())
                .eq(Objects.nonNull(website.getCategoryId()), Website::getCategoryId, website.getCategoryId())
                .orderByDesc(Website::getCreateTime));
        this.wrapper(websitePage.getRecords());
        return  websitePage;
    }

    @CacheEvict(value = {"website","category"}, allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

    private Website wrapper(Website website){
        BizImage img=imageService.getById(website.getImageId());
        if(img!=null){
            website.setImg(img);
        }
        return website;
    }

    private List<Website> wrapper(List<Website> websites){
        Set<String> imageIds=websites.stream().map(Website::getImageId).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(imageIds)){
            List<BizImage> images= imageService.listByIds(imageIds);
            Map<String,BizImage> map=images.stream().collect(Collectors.toMap(BizImage::getId, e->e));
            websites.forEach(e->e.setImg(map.get(e.getImageId())));
        }
        return websites;
    }


    public Map<Integer,List<Website>> getCategoryWebsiteMap(){
        List<Website> websites=this.listAll();
        this.wrapper(websites);
        if(CollectionUtils.isNotEmpty(websites)){
            websites=websites.stream().filter(e->Objects.nonNull(e.getCategoryId())).collect(Collectors.toList());
            return websites.stream().collect(Collectors.groupingBy(Website::getCategoryId));
        }
        return new HashMap<>(0);
    }
}
