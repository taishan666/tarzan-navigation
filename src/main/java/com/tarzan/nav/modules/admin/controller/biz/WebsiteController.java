package com.tarzan.nav.modules.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.CategoryService;
import com.tarzan.nav.modules.admin.service.biz.WebsiteService;
import com.tarzan.nav.modules.admin.vo.base.PageResultVo;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.utils.DateUtil;
import com.tarzan.nav.utils.ResultUtil;
import com.tarzan.nav.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 后台导航网站管理
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/website")
@AllArgsConstructor
public class WebsiteController {

    private final WebsiteService websiteService;
    private final CategoryService categoryService;

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadWebsites(Website website, Integer pageNumber, Integer pageSize) {
        IPage<Website> websitePage = websiteService.pageList(website, pageNumber, pageSize);
        if(CollectionUtils.isEmpty(websitePage.getRecords())){
            return  ResultUtil.table(Collections.emptyList(), 0L);
        }
        return ResultUtil.table(websitePage.getRecords(), websitePage.getTotal());
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("website", new Website().setStatus(1));
        return CoreConst.ADMIN_PREFIX + "website/form";
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseVo add(@Valid Website website) {
        Integer categoryId=website.getCategoryId();
        if(Objects.nonNull(categoryId)&&categoryId>0){
            Category category= categoryService.getById(categoryId);
            if(Objects.nonNull(category)){
                if(Objects.nonNull(category.getType())||category.getType()==0){
                    boolean flag = websiteService.saveByUrl(website.getUrl(),website.getCategoryId(),category.getType());
                    if (flag) {
                        return ResultUtil.success("新增网址成功");
                    } else {
                        return ResultUtil.error("新增网址失败");
                    }
                }
                return ResultUtil.error("分类的类型有误！");
            }
            return ResultUtil.error("分类不存在！");
        }else{
            return ResultUtil.error("分类id不能为空！");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Website website = websiteService.getByIdWithImage(id);
        model.addAttribute("website", website);
        return CoreConst.ADMIN_PREFIX + "website/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = {"website", "category"}, allEntries = true)
    public ResponseVo edit(Website website) {
        website.setUpdateTime(DateUtil.now());
        if(StringUtil.isNotBlank(website.getUrl())){
            website.setUrl(websiteService.shortUrl(website.getUrl()));
        }
        boolean flag = websiteService.updateById(website);
        if (flag) {
            return ResultUtil.success("编辑网址成功");
        } else {
            return ResultUtil.error("编辑网址失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        return deleteBatch(Collections.singletonList(id));
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestBody List<Integer> ids) {
        boolean flag = websiteService.deleteBatch(ids);
        if (flag) {
            return ResultUtil.success("删除网站成功");
        } else {
            return ResultUtil.error("删除网站失败");
        }
    }


    @PostMapping("/move/{categoryId}")
    @ResponseBody
    @CacheEvict(value = {"website", "category"}, allEntries = true)
    public ResponseVo moveWebsite(@PathVariable("categoryId") Integer categoryId,@RequestBody List<Integer> ids) {
        boolean flag = websiteService.lambdaUpdate().in(Website::getId,ids).set(Website::getCategoryId,categoryId).update();
        if (flag) {
            return ResultUtil.success("移动分类成功成功");
        } else {
            return ResultUtil.error("移动分类失败");
        }
    }

    @PostMapping("/copy/{categoryId}")
    @ResponseBody
    @CacheEvict(value = {"website", "category"}, allEntries = true)
    public ResponseVo copyWebsite(@PathVariable("categoryId") Integer categoryId,@RequestBody List<Integer> ids) {
        List<Website> websites=websiteService.lambdaQuery().in(Website::getId,ids).list();
        if(!CollectionUtils.isEmpty(websites)){
            websites.forEach(e->{
                e.setId(null);
                e.setCategoryId(categoryId);
            });
            boolean flag = websiteService.saveBatch(websites);
            if (!flag) {
                return ResultUtil.error("复制分类失败");
            }
        }
        return ResultUtil.success("复制分类成功成功");
    }


    @GetMapping("/getInfo")
    @ResponseBody
    public ResponseVo getInfo(String url) {
        if(StringUtil.isBlank(url)){
            return ResultUtil.error("网站地址不能为空！");
        }else {
            Website website =websiteService.getInfo(url);
            return ResultUtil.vo(website);
        }
    }

}
