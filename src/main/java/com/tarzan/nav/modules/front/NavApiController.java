package com.tarzan.nav.modules.front;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.enums.NavigationTypeEnum;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.biz.Comment;
import com.tarzan.nav.modules.admin.model.biz.Link;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.service.biz.*;
import com.tarzan.nav.modules.admin.service.sys.UserService;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.modules.front.dto.RegisterDTO;
import com.tarzan.nav.modules.front.query.ItemQuery;
import com.tarzan.nav.modules.network.HotNewsService;
import com.tarzan.nav.modules.network.LocationService;
import com.tarzan.nav.utils.*;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
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
    private final MailService mailService;
    private final UserService userService;

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
                return ResultUtil.vo(hotNewsService.douYinHot());
            case "juejin":
                return ResultUtil.vo(hotNewsService.jueJinHot());
            case "zhihu":
                return ResultUtil.vo(hotNewsService.zhiHuHot());
            default:
                return ResultUtil.vo(hotNewsService.baiduHot());
        }
    }


    @PostMapping("/register")
    @ResponseBody
    public ResponseVo register(HttpServletRequest request,RegisterDTO dto) {
        if ("reg_email_or_phone_token".equals(dto.getAction())) {
            SpecCaptcha captcha = new SpecCaptcha(10, 10, 4);
            captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
           // System.out.println(captcha.text());
            request.getSession().setAttribute("captcha", captcha.text().toLowerCase());
            mailService.sendEmailCode(dto.getEmail_phone(),captcha.text());
            return ResultUtil.success("邮件已经发送");
        }//判断验证码
        return registerUser(request,dto);
    }

    public ResponseVo registerUser(HttpServletRequest request,RegisterDTO dto){
        //判断验证码
        if (!CaptchaUtil.ver(dto.getVerification_code(), request)) {
            return ResultUtil.error("验证码错误！");
        }
        // 清除session中的验证码
        CaptchaUtil.clear(request);
        String username = dto.getUser_login();
        if (userService.exists(username)) {
            return ResultUtil.error("用户名已存在！");
        }
        String email = dto.getEmail_phone();
        if (userService.existsEmail(email)) {
            return ResultUtil.error("邮箱已被注册！");
        }
        String password = dto.getUser_pass();
        String confirmPassword = dto.getUser_pass2();
        //判断两次输入密码是否相等
        if (confirmPassword != null && password != null) {
            if (!confirmPassword.equals(password)) {
                return ResultUtil.error("两次密码不一致！");
            }
        }
        User registerUser=new User();
        registerUser.setUsername(username);
        registerUser.setPassword(password);
        registerUser.setEmail(dto.getEmail_phone());
        registerUser.setNickname(null);
        registerUser.setStatus(CoreConst.STATUS_VALID);
        PasswordHelper.encryptPassword(registerUser);
        //注册
        boolean flag = userService.save(registerUser);
        if(flag){
            return ResultUtil.success("注册成功！");
        }else {
            return ResultUtil.error("注册失败，请稍后再试！");
        }

    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseVo<String> login(HttpServletRequest request, RegisterDTO dto) {
        //判断验证码
        if (!CaptchaUtil.ver(dto.getVerification_code(), request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            return ResultUtil.error("验证码错误！");
        }
/*        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (ExcessiveAttemptsException e) {
            // 密码输错次数达到上限
            token.clear();
            return ResultUtil.error("密码输错次数达到上限，请30分钟后重试。");
        } catch (UnknownAccountException e) {
            // 未知账号
            token.clear();
            return ResultUtil.error("用户账户不存在！");
        } catch (LockedAccountException e) {
            token.clear();
            return ResultUtil.error("用户已经被锁定不能登录，请联系管理员！");
        } catch (AuthenticationException e) {
            token.clear();
            return ResultUtil.error("用户名或者密码错误！");
        }*/
        //后续处理
     //   loginProcess(request);
        return ResultUtil.success("登录成功！");
    }


}
