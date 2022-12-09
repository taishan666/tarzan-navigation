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
import com.tarzan.navigation.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("categoryLink")
@AllArgsConstructor
public class CategoryLinkController {

    private final CategoryLinkService categoryLinkService;
    private final LinkService linkService;

    @PostMapping("list/{categoryId}")
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

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "categoryLink/form";
    }


}
