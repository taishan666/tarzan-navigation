package com.tarzan.nav.modules.front;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Comment;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.*;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.modules.network.HotNewsService;
import com.tarzan.nav.modules.network.LocationService;
import com.tarzan.nav.utils.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
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
    private final ImageService imageService;
    private final SiteLookService siteLookService;

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


    @GetMapping("/search")
    public String search(String q,Model model) {
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("search",q);
        List<Website> websites=websiteService.listWithImage(new Website().setName(q));
        model.addAttribute("websites",websites);
        return  CoreConst.WEB_PREFIX+"search";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("comments",commentService.commentsBySid(-1));
        model.addAttribute("commentNum",commentService.commentsBySidNum(-1));
        return  CoreConst.WEB_PREFIX+"about";
    }

    @GetMapping("/bookmark")
    public String guestbook(Model model) {
        List<Website> websites=websiteService.randomList(12);
        model.addAttribute("websites",websites);
        return  CoreConst.WEB_PREFIX+"bookmark";
    }

    @GetMapping({"/notice/{noticeId}"})
    public String about(Model model, @PathVariable("noticeId") Integer noticeId) {
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("notice",noticeService.getById(noticeId));
        return  CoreConst.WEB_PREFIX+"notice";
    }


    @GetMapping("/apply")
    public String apply(Model model) {
        model.addAttribute("categories",categoryService.treeList());
        return  CoreConst.WEB_PREFIX+"apply";
    }

    @GetMapping("/site/{id}")
    public String apply(Model model,@PathVariable("id") Integer id) {
        String userIp= WebUtil.getIP();
        siteLookService.asyncLook(id,userIp);
        Website website=websiteService.getById(id);
        if(Objects.isNull(website)){
            model.addAttribute("website",new Website());
        }else {
            model.addAttribute("website",website);
        }
        return  CoreConst.WEB_PREFIX+"website";
    }

    @GetMapping("/douyin")
    public String douyin(Model model) {
        model.addAttribute("categories",categoryService.treeList());
        return  CoreConst.WEB_PREFIX+"douyin";
    }

    @PostMapping("/apply/submit")
    @ResponseBody
    public ResponseVo apply(@Valid @RequestBody Website website) {
        if(Objects.isNull(website.getName())){
            return ResultUtil.error("请填写网站名称！");
        }
        if(Objects.isNull(website.getCategoryId())||website.getCategoryId()==0){
            return ResultUtil.error("请选择分类！");
        }
        if(StringUtil.isBlank(website.getUrl())){
            return ResultUtil.error("请填写网址！");
        }
        if(StringUtil.isBlank(website.getDescription())){
            return ResultUtil.error("请填写描述！");
        }
        website.setStatus(CoreConst.STATUS_INVALID);
        boolean flag =websiteService.save(website);
        if (flag) {
            return ResultUtil.success("提交申请成功！");
        } else {
            return ResultUtil.error("提交申请失败！");
        }
    }

    @PostMapping("/comment/submit")
    @ResponseBody
    public ResponseVo saveComment(HttpServletRequest request, Comment comment) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(comment.getNickname())) {
            return ResultUtil.error("请输入昵称");
        }
        String content = comment.getContent();
        if (!XssKillerUtil.isValid(content)) {
            return ResultUtil.error("内容不合法");
        }
        content = XssKillerUtil.clean(content.trim()).replaceAll("(<p><br></p>)|(<p></p>)", "");
        comment.setContent(content);
        comment.setIp(IpUtil.getIpAddr(request));
        comment.setAvatar(imageService.letterAvatar(comment.getNickname()).getId());
        comment.setLocation(LocationService.getLocation(comment.getIp()));
        boolean a = commentService.insertComment(comment);
        if (a) {
            return ResultUtil.success("评论提交成功,系统正在审核");
        } else {
            return ResultUtil.error("评论提交失败");
        }
    }




}
