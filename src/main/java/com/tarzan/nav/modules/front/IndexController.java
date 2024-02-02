package com.tarzan.nav.modules.front;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.constant.LookTypeConst;
import com.tarzan.nav.common.enums.NavigationTypeEnum;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.*;
import com.tarzan.nav.modules.front.query.ItemQuery;
import com.tarzan.nav.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
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
        model.addAttribute("hotWebsites",websiteService.hotList(NavigationTypeEnum.SITE,12));
        return  CoreConst.WEB_PREFIX+"index";
    }


    @GetMapping("/search")
    public String search(String q,Integer type,Model model) {
        if(Objects.isNull(type)){
            type=1;
        }
        siteLookService.asyncLook(0,WebUtil.getIP(), LookTypeConst.SEARCH);
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("search",q);
        List<Website> websites=websiteService.search(Website.builder().name(q).description(q).build(),type);
        model.addAttribute("websites",websites);
        model.addAttribute("type",type);
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
        List<Website> websites=websiteService.randomList(NavigationTypeEnum.SITE,12);
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

    @GetMapping("/login")
    public String login() {
        return  CoreConst.WEB_PREFIX+"login";
    }

    @GetMapping("/register")
    public String register() {
        return  CoreConst.WEB_PREFIX+"register";
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

    @GetMapping("/lostpassword")
    public String lostPassword() {
        return  CoreConst.WEB_PREFIX+"lostpassword";
    }

    @GetMapping("/link")
    public String targetLink(String target,Model model) {
        siteLookService.asyncLook(target,WebUtil.getIP(), LookTypeConst.SITE);
        model.addAttribute("target","https:"+target);
        return  CoreConst.WEB_PREFIX+"link";
    }



    @GetMapping("/tag/items")
    public String tagItemsHtml(ItemQuery query, Model model) {
        model.addAttribute("flag",true);
        Integer id= query.getId();
        Category category=categoryService.getById(id);
        if(Objects.isNull(category)){
            return CoreConst.WEB_PREFIX+"card/sitecard";
        }
        model.addAttribute("websites",websiteService.getCategoryWebsiteMap().get(id));
        switch (category.getType()){
            case 2:
                return CoreConst.WEB_PREFIX+"card/postcard";
            default:
                return CoreConst.WEB_PREFIX+"card/sitecard";
        }
    }

    @PostMapping("/tag/items")
    public String tagItemsHtml(@RequestParam("action") String action,
                               @RequestParam("data[title]") String title,
                               @RequestParam("data[type]") String type,
                               @RequestParam("data[order]") String order,
                               @RequestParam("data[num]") int num, Model model) {
        model.addAttribute("flag",true);
        switch (title){
            case "热门网址":
                model.addAttribute("websites",websiteService.hotList(NavigationTypeEnum.SITE,num));
                break;
            case "随机推荐":
                model.addAttribute("websites",websiteService.randomList(NavigationTypeEnum.SITE,num));
                break;
            case "最新网址":
                model.addAttribute("websites",websiteService.newestList(NavigationTypeEnum.SITE,num));
                break;
            default:
                model.addAttribute("websites", Collections.emptyList());
                break;
        }
        return CoreConst.WEB_PREFIX+"card/minicard";
    }

    @GetMapping("/douyin")
    public String douyin(Model model) {
        model.addAttribute("categories",categoryService.treeList());
        return  CoreConst.WEB_PREFIX+"douyin";
    }

    @GetMapping("/chatgpt")
    public String chatgpt(Model model) {
        siteLookService.asyncLook(0,WebUtil.getIP(), LookTypeConst.CHATGPT);
        model.addAttribute("categories",categoryService.treeList());
        return  CoreConst.WEB_PREFIX+"chatgpt";
    }

    /**
     * 登出
     *
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        assert subject != null;
        subject.logout();
        return "redirect:/";
    }

}
