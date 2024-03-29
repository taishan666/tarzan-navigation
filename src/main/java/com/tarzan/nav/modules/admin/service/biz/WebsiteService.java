package com.tarzan.nav.modules.admin.service.biz;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.enums.NavigationTypeEnum;
import com.tarzan.nav.modules.admin.mapper.biz.CategoryMapper;
import com.tarzan.nav.modules.admin.mapper.biz.WebsiteMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Website;
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
public class WebsiteService extends ServiceImpl<WebsiteMapper, Website> {

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
    public List<Website> simpleList() {
        return super.lambdaQuery().select(Website::getId,Website::getUrl,Website::getCategoryId).eq(Website::getStatus,CoreConst.STATUS_VALID).list();
    }

    @Cacheable(value = "website", key = "#id")
    public Website get(Integer id) {
        return super.getById(id);
    }

    @Cacheable(value = "website", key = "'all'")
    public List<Website> listAll() {
        List<Website> websites= super.lambdaQuery().eq(Website::getStatus,CoreConst.STATUS_VALID).orderByDesc(Website::getCreateTime).list();
        if (CollectionUtils.isNotEmpty(websites)){
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
        Set<Integer> categoryIds=websites.stream().map(Website::getCategoryId).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(categoryIds)){
            List<Category> categories= categoryMapper.selectBatchIds(categoryIds);
            Map<Integer,String> map=categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));
            websites.forEach(e->e.setCategoryName(map.get(e.getCategoryId())));
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
       return super.lambdaQuery().eq(Website::getStatus,CoreConst.ZERO).count();
    }

    @Cacheable(value = "website", key = "'hot'")
    public List<Website>  hotList(NavigationTypeEnum typeEnum, int num) {
        Set<Integer> hotSites=siteLookService.topSites(num,typeEnum);
        if(CollectionUtils.isNotEmpty(hotSites)){
            return this.wrapper(this.lambdaQuery().in(Website::getId,hotSites).list());
        }else {
            return this.randomList(typeEnum,num);
        }
    }

    @Cacheable(value = "website", key = "'newest'")
    public List<Website>  newestList(NavigationTypeEnum typeEnum,int num) {
        List<Category> categories=categoryMapper.selectList(Wrappers.<Category>lambdaQuery().eq(Category::getType,typeEnum.getType()));
        if(CollectionUtils.isNotEmpty(categories)){
            Set<Integer> cateIds=categories.stream().map(Category::getId).collect(Collectors.toSet());
            return this.wrapper(super.lambdaQuery().eq(Website::getStatus,CoreConst.STATUS_VALID).in(Website::getCategoryId,cateIds).orderByDesc(Website::getCreateTime).last("limit "+num).list());
        }
        return Collections.emptyList();
    }

    @Cacheable(value = "website", key = "'random'")
    public List<Website> randomList(NavigationTypeEnum typeEnum,int count) {
        List<Category> categories=categoryMapper.selectList(Wrappers.<Category>lambdaQuery().eq(Category::getType,typeEnum.getType()));
        if(CollectionUtils.isNotEmpty(categories)){
            Set<Integer> cateIds=categories.stream().map(Category::getId).collect(Collectors.toSet());
            List<Website> websites= this.wrapper(super.lambdaQuery().eq(Website::getStatus,CoreConst.STATUS_VALID).in(Website::getCategoryId,cateIds).list());
            return RandomUtil.randomEles(websites,count);
        }
        return Collections.emptyList();
    }

    public List<Website> search(Website website,Integer type) {
        List<Category>  categories=categoryMapper.selectList(Wrappers.<Category>lambdaQuery().eq(Category::getType,type));
        if(CollectionUtils.isNotEmpty(categories)){
            Set<Integer> cateIds=categories.stream().map(Category::getId).collect(Collectors.toSet());
            List<Website> websites= super.lambdaQuery().and(wrap->wrap.like(Website::getName,website.getName()).or().like(Website::getDescription,website.getDescription())).in(Website::getCategoryId,cateIds).orderByDesc(Website::getCreateTime).list();
            return  this.wrapper(websites);
        }
       return Collections.emptyList();
    }
}
