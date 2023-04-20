package com.tarzan.nav.modules.front;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.CategoryService;
import com.tarzan.nav.modules.admin.service.biz.LinkService;
import com.tarzan.nav.modules.admin.service.biz.NoticeService;
import com.tarzan.nav.modules.admin.service.biz.WebsiteService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


/**
 * 导航首页相关接口
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@AllArgsConstructor
public class IndexController {

    private final CategoryService categoryService;
    private final WebsiteService websiteService;
    private final LinkService linkService;
    private final NoticeService noticeService;

    /**
     * 首页
     */
    @GetMapping({"/","index","home"})
    public String home(Model model) {
        model.addAttribute("notices",noticeService.simpleList());
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("links",linkService.simpleList());
        return  CoreConst.WEB_PREFIX+"index";
    }


    @GetMapping({"/search"})
    public String search(String q,Model model) {
        model.addAttribute("search",q);
        List<Website> websites=websiteService.listWithImage(new Website().setName(q));
        model.addAttribute("websites",websites);
        return  CoreConst.WEB_PREFIX+"search";
    }

    @GetMapping({"/about"})
    public String about(Model model) {
        model.addAttribute("categories",categoryService.treeList());
        return  CoreConst.WEB_PREFIX+"about";
    }



}
