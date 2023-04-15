package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.mapper.biz.CategoryMapper;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Link;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

    private final LinkService linkService;

    @Cacheable(value = "category", key = "'list'")
    public List<Category> selectCategories(int status) {
        return super.lambdaQuery().eq(Category::getStatus,status).orderByAsc(Category::getSort).list();
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

    @Cacheable(value = "category", key = "'tree'")
    public List<Category> treeList() {
        List<Category> sourceList=this.selectCategories(CoreConst.STATUS_VALID);
        List<Category> topList=sourceList.stream().filter(e-> e.getPid().equals(CoreConst.TOP_CATEGORY_ID)).collect(Collectors.toList());
        Map<Integer,List<Link>> map=linkService.getCategoryLinkMap();
        topList.forEach(e->assemblyTree(sourceList,e,map));
        return topList;
    }

    /**
     * 目录-组装树
     *
     * @param sourceList&parent
     * @Author: tarzan Liu
     * @Date: 2022/12/6 11:14
     */
    public void assemblyTree(List<Category> sourceList, Category parent,Map<Integer,List<Link>> map) {
        if (CollectionUtils.isNotEmpty(sourceList)) {
            List<Category> resultList = sourceList.stream().filter(e -> e.getPid().equals(parent.getId())).collect(Collectors.toList());
            resultList.sort(Comparator.comparing(Category::getSort));
            parent.setChildren(resultList);
            parent.setLinks(map.get(parent.getId()));
            resultList.forEach(e -> {
                assemblyTree(sourceList, e,map);
            });
        }
    }

    public List<Category> selectByPid(Integer pid) {
        return super.lambdaQuery().eq(Category::getPid, pid).list();
    }


    public boolean existLinks(Integer id){
        return linkService.lambdaQuery().eq(Link::getCategoryId,id).count()!=0L;
    }
}
