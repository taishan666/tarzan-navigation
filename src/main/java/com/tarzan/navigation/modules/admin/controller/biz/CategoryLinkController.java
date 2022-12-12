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


    @GetMapping("/{categoryId}")
    public String page(@PathVariable("categoryId")Integer categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        return CoreConst.ADMIN_PREFIX + "categoryLinK/list";
    }

    @PostMapping("/{categoryId}")
    @ResponseBody
    public PageResultVo page(@PathVariable("categoryId") Integer categoryId, Integer pageNumber, Integer pageSize) {
        IPage<CategoryLink> page = new Page<>(pageNumber, pageSize);
        IPage<CategoryLink> categoryLinkPage= categoryLinkService.page(page, Wrappers.<CategoryLink>lambdaQuery().eq(CategoryLink::getCategoryId,categoryId).orderByDesc(CategoryLink::getCreateTime));
        if(CollectionUtils.isNotEmpty(categoryLinkPage.getRecords())){
            Set<Integer> linkIds=categoryLinkPage.getRecords().stream().map(CategoryLink::getLinkId).collect(Collectors.toSet());
            List<Link> links=linkService.lambdaQuery().in(Link::getId,linkIds).list();
            ResultUtil.table(links, categoryLinkPage.getTotal());
        }
        return ResultUtil.table(Collections.emptyList(),0L);
    }

    @GetMapping("/add/{categoryId}")
    public String addPage(@PathVariable("categoryId") Integer categoryId, Model model) {
        model.addAttribute("categoryId", categoryId);
        return CoreConst.ADMIN_PREFIX + "categoryLink/addList";
    }

    @PostMapping("/add/{categoryId}")
    @ResponseBody
    public PageResultVo addPage(@PathVariable("categoryId") Integer categoryId, Integer pageNumber, Integer pageSize) {
        IPage<CategoryLink> page = new Page<>(pageNumber, pageSize);
        IPage<CategoryLink> categoryLinkPage= categoryLinkService.page(page, Wrappers.<CategoryLink>lambdaQuery().eq(CategoryLink::getCategoryId,categoryId).orderByDesc(CategoryLink::getCreateTime));
        if(CollectionUtils.isNotEmpty(categoryLinkPage.getRecords())){
            Set<Integer> linkIds=categoryLinkPage.getRecords().stream().map(CategoryLink::getLinkId).collect(Collectors.toSet());
            List<Link> links=linkService.lambdaQuery().notIn(Link::getId,linkIds).list();
            ResultUtil.table(links, categoryLinkPage.getTotal());
        }
        return ResultUtil.table(Collections.emptyList(),0L);
    }

    @PostMapping("/add/{categoryId}")
    @ResponseBody
    public ResponseVo addPage(@PathVariable("categoryId") Integer categoryId, @RequestBody List<Integer> linkIds) {
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
        return ResultUtil.success("添加网址成功");
    }


}
