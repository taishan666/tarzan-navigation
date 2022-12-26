package com.tarzan.navigation.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.common.constant.CoreConst;
import com.tarzan.navigation.modules.admin.mapper.biz.CategoryMapper;
import com.tarzan.navigation.modules.admin.model.biz.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {


    @Cacheable(value = "category", key = "'tree'")
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

    public List<Category> treeList() {
        List<Category> sourceList=this.selectCategories(CoreConst.STATUS_VALID);
        List<Category> topList=sourceList.stream().filter(e->e.getPid()==CoreConst.TOP_CATEGORY_ID).collect(Collectors.toList());
        topList.forEach(e->assemblyTree(sourceList,e));
        return topList;
    }

    /**
     * 目录-组装树
     *
     * @param sourceList&parent
     * @Description:list method
     * @Author: tarzan Liu
     * @Date: 2019/12/6 11:14
     */
    public void assemblyTree(List<Category> sourceList, Category parent) {
        if (CollectionUtils.isNotEmpty(sourceList)) {
            List<Category> resultList = sourceList.stream().filter(e -> e.getPid().equals(parent.getId())).collect(Collectors.toList());
            resultList.sort(Comparator.comparing(Category::getSort));
            parent.setChildren(resultList);
            resultList.forEach(e -> {
                assemblyTree(sourceList, e);
            });
        }
    }

    public List<Category> selectByPid(Integer pid) {
        return super.lambdaQuery().eq(Category::getPid, pid).list();
    }
}
