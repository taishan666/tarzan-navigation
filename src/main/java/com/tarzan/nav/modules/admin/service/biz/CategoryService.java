package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.mapper.biz.CategoryMapper;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Website;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    private final WebsiteService websiteService;

    @Cacheable(value = "category", key = "'list'")
    public List<Category> selectCategories(int status) {
        return super.lambdaQuery().eq(Category::getStatus,status).orderByAsc(Category::getSort).list();
    }

    @Cacheable(value = "category", key = "'all'")
    public List<Category> selectCategories() {
        return super.lambdaQuery().orderByAsc(Category::getSort).list();
    }

    @Override
    @Cacheable(value = "category", key = "'count'")
    public long count() {
        return super.lambdaQuery().eq(Category::getStatus, CoreConst.STATUS_VALID).count();
    }

    public Category selectById(Integer id) {
        Category category=getById(id);
        category.setParent(getById(category.getPid()));
        return category;
    }

    @Cacheable(value = "category", key = "'treeLink'")
    public List<Category> treeLink() {
        List<Category> sourceList=this.selectCategories(CoreConst.STATUS_VALID);
        Map<Integer,List<Website>> map=websiteService.getCategoryWebsiteMap();
        return binaryTree(sourceList,map);
    }

    @Cacheable(value = "category", key = "'tree'")
    public List<Category> treeList() {
        List<Category> sourceList=this.selectCategories(CoreConst.STATUS_VALID);
        return binaryTree(sourceList,null);
    }
    
    public List<Category> binaryTree(List<Category> sourceList,Map<Integer,List<Website>> map) {
        List<Category> topList=sourceList.stream().filter(e-> e.getPid().equals(CoreConst.TOP_CATEGORY_ID)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(topList)) {
            topList.forEach(category->{
                List<Category> children = sourceList.stream().filter(e -> e.getPid().equals(category.getId())).sorted(Comparator.comparing(Category::getSort)).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(children)){
                    category.setChildren(children);
                    if(CollectionUtils.isNotEmpty(map)){
                        Category firstChild=children.get(0);
                        firstChild.setWebsites(map.get(firstChild.getId()));
                    }
                }else {
                    if(CollectionUtils.isNotEmpty(map)){
                        category.setWebsites(map.get(category.getId()));
                    }
                }
            });
        }
        return topList;
    }

    public List<Category> selectByPid(Integer pid) {
        return super.lambdaQuery().eq(Category::getPid, pid).list();
    }


    public boolean existWebsites(Integer id){
        if(!CoreConst.TOP_MENU_ID.equals(id)){
            return websiteService.lambdaQuery().eq(Website::getCategoryId,id).count()!=0L;
        }
       return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Category bizCategory) {
        boolean flag= super.updateById(bizCategory);
        List<Category> children=super.lambdaQuery().eq(Category::getPid,bizCategory.getId()).list();
        if(CollectionUtils.isNotEmpty(children)){
            List<Integer> cateIds=children.stream().map(Category::getId).collect(Collectors.toList());
            return super.lambdaUpdate().set(Category::getStatus,bizCategory.getStatus()).in(Category::getId,cateIds).update();
        }
        return flag;
    }
}
