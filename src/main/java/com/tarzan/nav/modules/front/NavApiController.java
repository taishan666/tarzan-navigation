package com.tarzan.nav.modules.front;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.enums.NavigationTypeEnum;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Comment;
import com.tarzan.nav.modules.admin.model.biz.Link;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.service.biz.*;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.modules.front.query.ItemQuery;
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
import java.util.Collections;
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
public class NavApiController {

    private final CategoryService categoryService;
    private final WebsiteService websiteService;
    private final LinkService linkService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final HotNewsService hotNewsService;

    @PostMapping("/apply/submit")
    @ResponseBody
    public ResponseVo apply(@Valid @RequestBody Website website) {
        if(Objects.isNull(website.getName())){
            return ResultUtil.error("请填写网站名称！");
        }
        if(StringUtil.isBlank(website.getUrl())){
            return ResultUtil.error("请填写网址！");
        }
        if(StringUtil.isBlank(website.getDescription())){
            return ResultUtil.error("请填写描述！");
        }
        website.setStatus(CoreConst.STATUS_INVALID);
        boolean flag;
        if("link".equals(website.getApplyType())){
            Link link=BeanUtil.copy(website,Link.class);
            flag=linkService.save(link);
        }else{
            if(Objects.isNull(website.getCategoryId())||website.getCategoryId()==0){
                return ResultUtil.error("请选择分类！");
            }
            flag =websiteService.save(website);
        }
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



    @PostMapping("/tag/items")
    public String tagItemsHtml(@RequestParam("action") String action,
                               @RequestParam("data[title]") String title,
                               @RequestParam("data[type]") String type,
                               @RequestParam("data[order]") String order,
                               @RequestParam("data[num]") int num, Model model) {
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

    @GetMapping("/tag/items")
    public String tagItemsHtml(ItemQuery query, Model model) {
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

    @GetMapping("/hotSpot/{type}")
    @ResponseBody
    public ResponseVo hotSpot(@PathVariable("type") String type) {
        switch (type){
            case "csdn":
                return ResultUtil.vo(hotNewsService.cSDNHot());
            case "weibo":
                return ResultUtil.vo(hotNewsService.weiboHot());
            case "douyin":
                return ResultUtil.vo(hotNewsService.zhiHuHot());
            case "juejin":
                return ResultUtil.vo(hotNewsService.jueJinHot());
            case "zhihu":
                return ResultUtil.vo(hotNewsService.zhiHuHot());
            default:
                return ResultUtil.vo(hotNewsService.baiduHot());
        }
    }

}
