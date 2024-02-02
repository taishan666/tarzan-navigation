package com.tarzan.nav.modules.admin.controller.biz;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.entity.biz.CategoryEntity;
import com.tarzan.nav.utils.ResultUtil;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.service.biz.CategoryService;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 后台类目管理
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("list")
    @ResponseBody
    public List<CategoryEntity> loadCategory() {
        return categoryService.selectCategories();
    }

    @GetMapping("/add")
    public String add(Model model) {
        Category bizCategory = new Category();
        long count=categoryService.lambdaQuery().eq(CategoryEntity::getPid,CoreConst.TOP_CATEGORY_ID).count();
        bizCategory.setSort((int) (count+1));
        bizCategory.setPid(CoreConst.TOP_CATEGORY_ID);
        bizCategory.setParentName(CoreConst.TOP_CATEGORY_NAME);
        model.addAttribute("category", bizCategory);
        return CoreConst.ADMIN_PREFIX + "category/form";
    }

    @GetMapping("/add/child")
    public String addChild(Model model,Integer pid) {
        Category bizCategory = new Category();
        bizCategory.setPid(pid);
        long count=categoryService.lambdaQuery().eq(CategoryEntity::getPid,pid).count();
        bizCategory.setSort((int) (count+1));
        bizCategory.setPid(pid);
        bizCategory.setParentName(getCategoryName(pid));
        model.addAttribute("category", bizCategory);
        return CoreConst.ADMIN_PREFIX + "category/form";
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo add(Category bizCategory) {
        if (categoryService.existWebsites(bizCategory.getPid())) {
             return ResultUtil.error("添加失败，父级分类不能存在网站");
        }
        bizCategory.setStatus(CoreConst.STATUS_VALID);
        boolean flag = categoryService.save(bizCategory);
        if (flag) {
            return ResultUtil.success("新增分类成功");
        } else {
            return ResultUtil.error("新增分类失败");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Category bizCategory = categoryService.selectById(id);
        if(bizCategory.getPid().equals(CoreConst.TOP_CATEGORY_ID)){
            bizCategory.setParentName(CoreConst.TOP_CATEGORY_NAME);
        }else{
            bizCategory.setParentName(getCategoryName(bizCategory.getPid()));
        }
        model.addAttribute("category", bizCategory);
        return CoreConst.ADMIN_PREFIX + "category/form";
    }


    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo edit(Category bizCategory) {
        if (categoryService.existWebsites(bizCategory.getPid())) {
            return ResultUtil.error("编辑失败，父级分类不能存在网站");
        }
        bizCategory.setUpdateTime(new Date());
        boolean flag = categoryService.updateById(bizCategory);
        if (flag) {
            return ResultUtil.success("编辑分类成功");
        } else {
            return ResultUtil.error("编辑分类失败");
        }
    }

    @PostMapping("/update/status")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo updateStatus(Category bizCategory) {
        boolean flag = categoryService.updateStatus(bizCategory);
        if (flag) {
            return ResultUtil.success("状态修改成功");
        } else {
            return ResultUtil.error("状态修改失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    @CacheEvict(value = "category", allEntries = true)
    public ResponseVo delete(Integer id) {
        if (CollectionUtils.isNotEmpty(categoryService.selectByPid(id))) {
            return ResultUtil.error("该分类下存在子分类！");
        }
        boolean flag = categoryService.removeById(id);
        if (flag) {
            return ResultUtil.success("删除分类成功");
        } else {
            return ResultUtil.error("删除分类失败");
        }
    }


    private String getCategoryName(Integer id){
        CategoryEntity category=categoryService.getById(id);
        return category==null?"":category.getName();
    }

}
