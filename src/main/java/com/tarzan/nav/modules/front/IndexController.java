package com.tarzan.nav.modules.front;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.CategoryService;
import com.tarzan.nav.modules.admin.service.biz.LinkService;
import com.tarzan.nav.modules.admin.service.biz.NoticeService;
import com.tarzan.nav.modules.admin.service.biz.WebsiteService;
import com.tarzan.nav.modules.network.HotNewsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    private final HotNewsService hotNewsService;

    /**
     * 首页
     */
    @GetMapping({"/","index","home"})
    public String home(Model model) {
        model.addAttribute("notices",noticeService.simpleList());
        model.addAttribute("categories",categoryService.treeLink());
        model.addAttribute("links",linkService.simpleList());
        model.addAttribute("baiduHot",hotNewsService.baiduHot());
        model.addAttribute("weiboHot",hotNewsService.weiboHot());
        model.addAttribute("douYinHot",hotNewsService.douYinHot());
        model.addAttribute("jueJinHot",hotNewsService.jueJinHot());
        model.addAttribute("cSDNHot",hotNewsService.cSdnHot());
        return  CoreConst.WEB_PREFIX+"index";
    }


    @GetMapping({"/search"})
    public String search(String q,Model model) {
        model.addAttribute("categories",categoryService.treeList());
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

    @GetMapping({"/bookmark"})
    public String guestbook() {
        return  CoreConst.WEB_PREFIX+"bookmark";
    }

    @GetMapping({"/notice/{noticeId}"})
    public String about(Model model, @PathVariable("noticeId") Integer noticeId) {
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("notice",noticeService.getById(noticeId));
        return  CoreConst.WEB_PREFIX+"notice";
    }



}
