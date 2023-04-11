package com.tarzan.navigation.modules.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.navigation.common.constant.CoreConst;
import com.tarzan.navigation.utils.DateUtil;
import com.tarzan.navigation.utils.JsoupUtil;
import com.tarzan.navigation.utils.ResultUtil;
import com.tarzan.navigation.modules.admin.model.biz.Link;
import com.tarzan.navigation.modules.admin.service.biz.LinkService;
import com.tarzan.navigation.modules.admin.vo.base.PageResultVo;
import com.tarzan.navigation.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 后台友情链接管理
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("link")
@AllArgsConstructor
public class LinkController {

    private final LinkService linkService;

    @PostMapping("list")
    @ResponseBody
    public PageResultVo loadLinks(Link link, Integer pageNumber, Integer pageSize) {
        IPage<Link> linkPage = linkService.pageLinks(link, pageNumber, pageSize);
        if(CollectionUtils.isEmpty(linkPage.getRecords())){
            return  ResultUtil.table(Collections.emptyList(), 0L);
        }
        linkService.wrapper(linkPage.getRecords());
        return ResultUtil.table(linkPage.getRecords(), linkPage.getTotal());
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("link", new Link().setStatus(1));
        return CoreConst.ADMIN_PREFIX + "link/form";
    }

    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = {"link", "category"}, allEntries = true)
    public ResponseVo add(@Valid Link link) {
        if(Objects.nonNull(link.getCategoryId())&&link.getCategoryId()>0){
            boolean flag = linkService.saveByUrl(link.getUrl(),link.getCategoryId());
            if (flag) {
                return ResultUtil.success("新增网址成功");
            } else {
                return ResultUtil.error("新增网址失败");
            }
        }else{
            return ResultUtil.error("分类id不能为空！");
        }
    }

    @GetMapping("/edit")
    public String edit(Model model, Integer id) {
        Link link = linkService.getById(id);
        if(Objects.nonNull(link.getImageId())){
            linkService.wrapper(link);
        }
        model.addAttribute("link", link);
        return CoreConst.ADMIN_PREFIX + "link/form";
    }

    @PostMapping("/edit")
    @ResponseBody
    @CacheEvict(value = {"link", "category"}, allEntries = true)
    public ResponseVo edit(Link link) {
        link.setUpdateTime(DateUtil.now());
        boolean flag = linkService.updateById(link);
        if (flag) {
            return ResultUtil.success("编辑友链成功");
        } else {
            return ResultUtil.error("编辑友链失败");
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo delete(Integer id) {
        return deleteBatch(Arrays.asList(id));
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestBody List<Integer> ids) {
        boolean flag = linkService.deleteBatch(ids);
        if (flag) {
            return ResultUtil.success("删除友链成功");
        } else {
            return ResultUtil.error("删除友链失败");
        }
    }

}
