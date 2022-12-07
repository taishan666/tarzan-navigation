package com.tarzan.nacigation.modules.admin.service.biz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nacigation.common.constant.CoreConst;
import com.tarzan.nacigation.modules.admin.mapper.biz.CategoryMapper;
import com.tarzan.nacigation.modules.admin.model.biz.Category;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<Category> selectByPid(Integer pid) {
        return super.lambdaQuery().eq(Category::getPid, pid).list();
    }
}
