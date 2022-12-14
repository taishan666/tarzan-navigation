package com.tarzan.navigation.modules.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.navigation.common.constant.CoreConst;
import com.tarzan.navigation.modules.admin.model.biz.CategoryLink;
import com.tarzan.navigation.modules.admin.model.biz.Link;
import com.tarzan.navigation.modules.admin.service.biz.CategoryLinkService;
import com.tarzan.navigation.modules.admin.service.biz.LinkService;
import com.tarzan.navigation.modules.admin.vo.base.PageResultVo;
import com.tarzan.navigation.modules.admin.vo.base.ResponseVo;
import com.tarzan.navigation.utils.DateUtil;
import com.tarzan.navigation.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TARZAN
 */
@Controller
@RequestMapping("categoryLink")
@AllArgsConstructor
public class CategoryLinkController {

    private final CategoryLinkService categoryLinkService;
    private final LinkService linkService;

    /**
     * 分类管理
     */
    @GetMapping("/{categoryId}")
    public String categories(@PathVariable("categoryId")Integer categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        return CoreConst.ADMIN_PREFIX + "categoryLink/manage";
    }

    @PostMapping("/{categoryId}")
    @ResponseBody
    public PageResultVo page(@PathVariable("categoryId") Integer categoryId, Integer pageNumber, Integer pageSize) {
        IPage<CategoryLink> page = new Page<>(pageNumber, pageSize);
        IPage<CategoryLink> categoryLinkPage= categoryLinkService.page(page, Wrappers.<CategoryLink>lambdaQuery().eq(CategoryLink::getCategoryId,categoryId).orderByDesc(CategoryLink::getCreateTime));
        if(CollectionUtils.isNotEmpty(categoryLinkPage.getRecords())){
            Set<Integer> linkIds=categoryLinkPage.getRecords().stream().map(CategoryLink::getLinkId).collect(Collectors.toSet());
            List<Link> links=linkService.lambdaQuery().in(Link::getId,linkIds).list();
            Map<Integer,Link> map=links.stream().collect(Collectors.toMap(Link::getId,e->e));
            categoryLinkPage.getRecords().forEach(e->e.setLink(map.get(e.getLinkId())));
            return ResultUtil.table(categoryLinkPage.getRecords(), categoryLinkPage.getTotal());
        }
        return ResultUtil.table(Collections.emptyList(),0L);
    }

    @GetMapping("/addPage/{categoryId}")
    public String addPage(@PathVariable("categoryId") Integer categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        return CoreConst.ADMIN_PREFIX + "categoryLink/addList";
    }

    @PostMapping("/addPage/{categoryId}")
    @ResponseBody
    public PageResultVo addPage(@PathVariable("categoryId") Integer categoryId, Integer pageNumber, Integer pageSize) {
        IPage<Link> page = new Page<>(pageNumber, pageSize);
        List<CategoryLink> categoryLinks= categoryLinkService.lambdaQuery().eq(CategoryLink::getCategoryId,categoryId).list();
        if(CollectionUtils.isNotEmpty(categoryLinks)){
            Set<Integer> linkIds=categoryLinks.stream().map(CategoryLink::getLinkId).collect(Collectors.toSet());
            page=linkService.lambdaQuery().notIn(Link::getId,linkIds).orderByDesc(Link::getCreateTime).page(page);
        }else{
            page=linkService.lambdaQuery().orderByDesc(Link::getCreateTime).page(page);
        }
        linkService.wrapper(page.getRecords());
        return ResultUtil.table(page.getRecords(), page.getTotal());
    }

    @PostMapping("/add/{categoryId}")
    @ResponseBody
    public ResponseVo add(@PathVariable("categoryId") Integer categoryId, @RequestBody List<Integer> linkIds) {
        List<CategoryLink> list=new ArrayList<>(10);
        Date date= DateUtil.now();
        if(CollectionUtils.isNotEmpty(linkIds)){
            linkIds.forEach(linkId->{
                CategoryLink entity=new CategoryLink();
                entity.setCategoryId(categoryId);
                entity.setLinkId(linkId);
                entity.setCreateTime(date);
                list.add(entity);
            });
            categoryLinkService.saveBatch(list);
        }
        return ResultUtil.success("添加网站成功");
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestBody List<Integer> ids) {
        boolean flag = categoryLinkService.removeBatchByIds(ids);
        if (flag) {
            return ResultUtil.success("移除网站成功");
        } else {
            return ResultUtil.error("移除网站失败");
        }
    }

}
