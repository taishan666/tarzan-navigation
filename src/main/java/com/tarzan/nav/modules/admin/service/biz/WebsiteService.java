package com.tarzan.nav.modules.admin.service.biz;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.enums.NavigationTypeEnum;
import com.tarzan.nav.modules.admin.entity.biz.WebsiteEntity;
import com.tarzan.nav.modules.admin.mapper.biz.CategoryMapper;
import com.tarzan.nav.modules.admin.mapper.biz.WebsiteMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.utils.BeanUtil;
import com.tarzan.nav.utils.DateUtil;
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
public class WebsiteService extends ServiceImpl<WebsiteMapper, WebsiteEntity> {

    private final ImageService imageService;
    private final CategoryMapper categoryMapper;
    private final SiteLookService siteLookService;
    private final PostService postService;
    private static final int TITLE_LEN=50;


    public String shortUrl(String url){
        String shortUrl=url;
        if(url.endsWith("/")){
            url=url.substring(0,url.length()-1);
        }
        if(url.contains("http://")){
            shortUrl=url.replaceFirst("http:","");
        }
        if(url.contains("https://")){
            shortUrl=url.replaceFirst("https:","");
        }
        return shortUrl;
    }

    @CacheEvict(value = {"website", "category"}, allEntries = true)
    public boolean saveByUrl(String url,Integer categoryId,Integer type){
        if(type==1){
            return saveSite(url,categoryId);
        }
        if(type==2){
            return savePost(url,categoryId);
        }
        return true;
    }

    public boolean savePost(String url,Integer categoryId){
        Website post= postService.getPost(url);
        post.setUrl(shortUrl(url));
        post.setCategoryId(categoryId);
        return super.save(post);
    }

    public boolean saveSite(String url,Integer categoryId){
        Website website=new Website();
        website.setUrl(shortUrl(url));
        website.setCategoryId(categoryId);
        Document doc= JsoupUtil.getDocument(url);
        String title=JsoupUtil.getTitle(doc);
        String desc=JsoupUtil.getDescription(doc);
        if(StringUtil.isNotBlank(title)){
            if(title.length()>TITLE_LEN){
                title= title.substring(0,TITLE_LEN);
            }
        }
        website.setName(title);
        website.setDescription(desc);
        String webIconUrl=JsoupUtil.getWebIcon(doc,url);
        BizImage image=imageService.upload(webIconUrl);
        website.setImageId(image.getId());
        website.setStatus(CoreConst.STATUS_VALID);
       return super.save(website);
    }

    @Cacheable(value = "website", key = "'simple'")
    public List<WebsiteEntity> simpleList() {
        return super.lambdaQuery().select(WebsiteEntity::getId,WebsiteEntity::getUrl,WebsiteEntity::getCategoryId).eq(WebsiteEntity::getStatus,CoreConst.STATUS_VALID).list();
    }

    @Cacheable(value = "website", key = "#id")
    public WebsiteEntity get(Integer id) {
        return super.getById(id);
    }

    @Cacheable(value = "website", key = "'all'")
    public List<Website> listAll() {
        List<WebsiteEntity> websiteEntities= super.lambdaQuery().eq(WebsiteEntity::getStatus,CoreConst.STATUS_VALID).orderByDesc(WebsiteEntity::getCreateTime).list();
        return this.wrapper(websiteEntities);
    }

    @CacheEvict(value = {"website", "category"}, allEntries = true)
    public boolean updateStatus(Integer id,Integer status) {
        Website website=new Website();
        website.setId(id);
        website.setStatus(status);
        return super.updateById(website);
    }

    public Website getByIdWithImage(Integer id){
        WebsiteEntity websiteEntity=super.getById(id);
        if(Objects.nonNull(websiteEntity.getImageId())){
            return this.wrapper(websiteEntity);
        }
        return BeanUtil.copy(websiteEntity,Website.class);
    }

    public IPage<Website> pageList(Website website, Integer pageNumber, Integer pageSize) {
        IPage<WebsiteEntity> page = new Page<>(pageNumber, pageSize);
        IPage<WebsiteEntity> websitePage=super.page(page,Wrappers.<WebsiteEntity>lambdaQuery()
                .like(StringUtils.isNotBlank(website.getName()), WebsiteEntity::getName, website.getName())
                .like(StringUtils.isNotBlank(website.getUrl()), WebsiteEntity::getUrl, website.getUrl())
                .eq(Objects.nonNull(website.getStatus()), WebsiteEntity::getStatus, website.getStatus())
                .eq(Objects.nonNull(website.getCategoryId()), WebsiteEntity::getCategoryId, website.getCategoryId())
                .orderByDesc(WebsiteEntity::getCreateTime));
        return  this.wrapperPage(websitePage);
    }



    @CacheEvict(value = {"website","category"}, allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return removeByIds(ids);
    }

    private IPage<Website> wrapperPage(IPage<WebsiteEntity> entityPage) {
        IPage<Website> page= new Page<>(entityPage.getCurrent(), entityPage.getPages(), entityPage.getTotal());
        page.setRecords(this.wrapper(entityPage.getRecords()));
        return page;
    }

    private Website wrapper(WebsiteEntity websiteEntity){
        Website website=BeanUtil.copy(websiteEntity,Website.class);
        BizImage img=imageService.getById(website.getImageId());
        if(img!=null){
            website.setImg(img);
        }
        return website;
    }

    private List<Website> wrapper(List<WebsiteEntity> websiteEntities){
        List<Website> websites=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(websiteEntities)){
            websites=BeanUtil.copyList(websiteEntities,Website.class);
            Set<String> imageIds=websites.stream().map(Website::getImageId).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(imageIds)){
                List<BizImage> images= imageService.listByIds(imageIds);
                Map<String,BizImage> map=images.stream().collect(Collectors.toMap(BizImage::getId, e->e));
                websites.forEach(e->e.setImg(map.get(e.getImageId())));
            }
            Set<Integer> categoryIds=websites.stream().map(Website::getCategoryId).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(categoryIds)){
                List<Category> categories= categoryMapper.selectBatchIds(categoryIds);
                Map<Integer,String> map=categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));
                websites.forEach(e->e.setCategoryName(map.get(e.getCategoryId())));
            }
            Date beforeDate= DateUtil.addDays(DateUtil.now(),-3);
            Set<Integer> hotSites=siteLookService.topSites(10,null);
            websites.forEach(e->{
                if(hotSites.contains(e.getId())){
                    e.setFlag("热");
                }else {
                    if(beforeDate.compareTo(e.getCreateTime()) < 0){
                        e.setFlag("新");
                    }
                }
            });
        }
        return websites;
    }


    public Map<Integer,List<Website>> getCategoryWebsiteMap(){
        List<Website> websites=this.listAll();
        if(CollectionUtils.isNotEmpty(websites)){
            websites=websites.stream().filter(e->Objects.nonNull(e.getCategoryId())).collect(Collectors.toList());
            return websites.stream().collect(Collectors.groupingBy(Website::getCategoryId));
        }
        return new HashMap<>(0);
    }



    public Website getInfo(String url) {
        Document doc= JsoupUtil.getDocument(url);
        String title=JsoupUtil.getTitle(doc);
        String desc=JsoupUtil.getDescription(doc);
        String webIcon=JsoupUtil.getWebIcon(doc,url);
        BizImage image=imageService.upload(webIcon);
        Website website=new Website();
        website.setName(title);
        website.setUrl(url);
        website.setDescription(desc);
        website.setImageId(image.getId());
        return website;
    }

    public Long toAuditNum() {
       return super.lambdaQuery().eq(WebsiteEntity::getStatus,CoreConst.ZERO).count();
    }

    @Cacheable(value = "website", key = "'hot'")
    public List<Website>  hotList(NavigationTypeEnum typeEnum, int num) {
        Set<Integer> hotSites=siteLookService.topSites(num,typeEnum);
        if(CollectionUtils.isNotEmpty(hotSites)){
            return this.wrapper(this.lambdaQuery().in(WebsiteEntity::getId,hotSites).list());
        }else {
            return this.randomList(typeEnum,num);
        }
    }

    @Cacheable(value = "website", key = "'newest'")
    public List<Website>  newestList(NavigationTypeEnum typeEnum,int num) {
        List<Category> categories=categoryMapper.selectList(Wrappers.<Category>lambdaQuery().eq(Category::getType,typeEnum.getType()));
        if(CollectionUtils.isNotEmpty(categories)){
            Set<Integer> cateIds=categories.stream().map(Category::getId).collect(Collectors.toSet());
            return this.wrapper(super.lambdaQuery().eq(WebsiteEntity::getStatus,CoreConst.STATUS_VALID).in(WebsiteEntity::getCategoryId,cateIds).orderByDesc(WebsiteEntity::getCreateTime).last("limit "+num).list());
        }
        return Collections.emptyList();
    }

    @Cacheable(value = "website", key = "'random'")
    public List<Website> randomList(NavigationTypeEnum typeEnum,int count) {
        List<Category> categories=categoryMapper.selectList(Wrappers.<Category>lambdaQuery().eq(Category::getType,typeEnum.getType()));
        if(CollectionUtils.isNotEmpty(categories)){
            Set<Integer> cateIds=categories.stream().map(Category::getId).collect(Collectors.toSet());
            List<Website> websites= this.wrapper(super.lambdaQuery().eq(WebsiteEntity::getStatus,CoreConst.STATUS_VALID).in(WebsiteEntity::getCategoryId,cateIds).list());
            return RandomUtil.randomEles(websites,count);
        }
        return Collections.emptyList();
    }

    public List<Website> search(Website website,Integer type) {
        List<Category>  categories=categoryMapper.selectList(Wrappers.<Category>lambdaQuery().eq(Category::getType,type));
        if(CollectionUtils.isNotEmpty(categories)){
            Set<Integer> cateIds=categories.stream().map(Category::getId).collect(Collectors.toSet());
            List<WebsiteEntity> websiteEntities= super.lambdaQuery().and(wrap->wrap.like(WebsiteEntity::getName,website.getName()).or().like(WebsiteEntity::getDescription,website.getDescription())).in(WebsiteEntity::getCategoryId,cateIds).orderByDesc(WebsiteEntity::getCreateTime).list();
            return  this.wrapper(websiteEntities);
        }
       return Collections.emptyList();
    }
}
