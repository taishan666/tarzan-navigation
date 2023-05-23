package com.tarzan.nav.modules.front;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.constant.LookTypeConst;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.*;
import com.tarzan.nav.modules.network.HotNewsService;
import com.tarzan.nav.utils.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


/**
 * 导航首页相关接口
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@AllArgsConstructor
@Slf4j
public class IndexController {

    private final CategoryService categoryService;
    private final WebsiteService websiteService;
    private final LinkService linkService;
    private final NoticeService noticeService;
    private final HotNewsService hotNewsService;
    private final CommentService commentService;
    private final SiteLookService siteLookService;

    /**
     * 首页
     */
    @GetMapping({"/","index","home"})
    public String home(Model model) {
        siteLookService.asyncLook(0,WebUtil.getIP(), LookTypeConst.HOME);
        model.addAttribute("notices",noticeService.simpleList());
        model.addAttribute("categories",categoryService.treeLink());
        model.addAttribute("links",linkService.simpleList());
        model.addAttribute("hotSpot",hotNewsService.hotSpot());
        model.addAttribute("topWebsite",websiteService.topWebsites(12));
        return  CoreConst.WEB_PREFIX+"index";
    }


    @GetMapping("/search")
    public String search(String q,Model model) {
        siteLookService.asyncLook(0,WebUtil.getIP(), LookTypeConst.SEARCH);
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("search",q);
        List<Website> websites=websiteService.listWithImage(new Website().setName(q).setDescription(q));
        model.addAttribute("websites",websites);
        return  CoreConst.WEB_PREFIX+"search";
    }

    @GetMapping("/about")
    public String about(Model model) {
        siteLookService.asyncLook(0,WebUtil.getIP(), LookTypeConst.ABOUT);
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("comments",commentService.commentsBySid(-1));
        model.addAttribute("commentNum",commentService.commentsBySidNum(-1));
        return  CoreConst.WEB_PREFIX+"about";
    }

    @GetMapping("/bookmark")
    public String guestbook(Model model) {
        siteLookService.asyncLook(0,WebUtil.getIP(), LookTypeConst.BOOKMARK);
        List<Website> websites=websiteService.randomList(12);
        model.addAttribute("websites",websites);
        return  CoreConst.WEB_PREFIX+"bookmark";
    }

    @GetMapping({"/notice/{noticeId}"})
    public String about(Model model, @PathVariable("noticeId") Integer noticeId) {
        siteLookService.asyncLook(noticeId,WebUtil.getIP(), LookTypeConst.NOTICE);
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("notice",noticeService.getById(noticeId));
        return  CoreConst.WEB_PREFIX+"notice";
    }


    @GetMapping("/apply")
    public String apply(Model model) {
        siteLookService.asyncLook(0,WebUtil.getIP(), LookTypeConst.APPLY);
        model.addAttribute("categories",categoryService.treeList());
        return  CoreConst.WEB_PREFIX+"apply";
    }

    @GetMapping("/site/{id}")
    public String apply(@PathVariable("id") Integer id) {
        Website website=websiteService.get(id);
        siteLookService.asyncLook(id,WebUtil.getIP(), LookTypeConst.SITE);
        if(Objects.isNull(website)){
            return "forward:/";
        }else {
            return "redirect:https:"+website.getUrl();
        }
    }

    @GetMapping("/douyin")
    public String douyin(Model model) {
        model.addAttribute("categories",categoryService.treeList());
        return  CoreConst.WEB_PREFIX+"douyin";
    }

}
