package com.tarzan.nav.modules.front;

import com.alibaba.fastjson.JSON;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Comment;
import com.tarzan.nav.modules.admin.model.biz.Link;
import com.tarzan.nav.modules.admin.model.biz.Website;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.service.biz.*;
import com.tarzan.nav.modules.admin.service.sys.UserService;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.modules.front.dto.LoginDTO;
import com.tarzan.nav.modules.front.dto.RegisterDTO;
import com.tarzan.nav.modules.network.HotNewsService;
import com.tarzan.nav.modules.network.LocationService;
import com.tarzan.nav.utils.*;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 导航首页相关接口
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@RestController
@AllArgsConstructor
@Slf4j
public class NavApiController {

    private final WebsiteService websiteService;
    private final LinkService linkService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final HotNewsService hotNewsService;
    private final MailService mailService;
    private final UserService userService;
    private final MatterService matterService;

    private static final String EMAIL_REGEX= "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final Cache<String, String> AUTH_CODE_ACHE = Caffeine.newBuilder()
            .initialCapacity(5)
            // 超出时淘汰
            .maximumSize(100000)
            //设置写缓存后n秒钟过期
            .expireAfterWrite(300, TimeUnit.SECONDS)
            .build();


    @PostMapping("/apply/submit")
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
            matterService.sendNotification();
            return ResultUtil.success("提交申请成功！");
        } else {
            return ResultUtil.error("提交申请失败！");
        }
    }

    @PostMapping("/comment/submit")
    @RequiresAuthentication
    public ResponseVo saveComment(HttpServletRequest request, Comment comment) throws UnsupportedEncodingException {
        comment.setNickname(AuthUtil.getUsername());
        comment.setEmail(AuthUtil.getEmail());
        String content = comment.getContent();
        if (!XssKillerUtil.isValid(content)) {
            return ResultUtil.error("内容不合法");
        }
        content = XssKillerUtil.clean(content.trim()).replaceAll("(<p><br></p>)|(<p></p>)", "");
        comment.setContent(content);
        comment.setIp(IpUtil.getIpAddr(request));
        comment.setAvatar(imageService.letterAvatar(comment.getNickname()).getId());
        comment.setLocation(LocationService.getLocation(comment.getIp()));
        boolean flag= commentService.insertComment(comment);
        if (flag) {
            matterService.sendNotification();
            return ResultUtil.success("评论提交成功,系统正在审核");
        } else {
            return ResultUtil.error("评论提交失败");
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
            request.getSession().setAttribute("captcha", captcha.text());
            Matcher matcher = EMAIL_PATTERN.matcher(dto.getEmail_phone());
            if(matcher.matches()){
                mailService.sendEmailCode(dto.getEmail_phone(),captcha.text());
                return ResultUtil.status(1,"邮件已经发送");
            }else{
                return ResultUtil.status(4,"邮箱格式错误");
            }
        }
        return registerUser(request,dto);
    }

    @PostMapping("/lostpassword")
    public ResponseVo lostPassword(RegisterDTO dto) {
        if ("lost_email_or_phone_token".equals(dto.getAction())) {
            Matcher matcher = EMAIL_PATTERN.matcher(dto.getEmail_phone());
            if(matcher.matches()){
                User user=userService.lambdaQuery().eq(User::getEmail,dto.getEmail_phone()).last("limit 1").one();
                if(Objects.nonNull(user)){
                    SpecCaptcha captcha = new SpecCaptcha(10, 10, 4);
                    captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
                    AUTH_CODE_ACHE.put(dto.getEmail_phone(),captcha.text());
                    mailService.sendEmailCode(dto.getEmail_phone(),captcha.text());
                    return ResultUtil.status(1,"邮件已经发送");
                }else {
                    return ResultUtil.status(4,"邮箱未注册");
                }
            }else{
                return ResultUtil.status(4,"邮箱格式错误");
            }
        }
        return  updatePassword(dto);
    }

    public ResponseVo updatePassword(RegisterDTO dto){
        String email=dto.getEmail_phone();
        if(StringUtil.isNotBlank(email)){
            String code=AUTH_CODE_ACHE.getIfPresent(email);
                //判断验证码
                if (Objects.isNull(code)||!code.equals(dto.getVerification_code())) {
                    return ResultUtil.status(4,"验证码错误！");
                }
                String password = dto.getUser_pass();
                String confirmPassword = dto.getUser_pass2();
                //判断两次输入密码是否相等
                if (confirmPassword != null && password != null) {
                    if (!confirmPassword.equals(password)) {
                        return ResultUtil.status(4,"两次密码不一致！");
                    }
                }
                User updateUser=new User();
                updateUser.setPassword(password);
                updateUser.setEmail(dto.getEmail_phone());
                PasswordHelper.encryptPassword(updateUser);
                //修改密码
                boolean flag = userService.lambdaUpdate().set(User::getPassword,updateUser.getPassword()).eq(User::getEmail,updateUser.getEmail()).update();
                if(flag){
                    return ResultUtil.status(1,"密码修改成功！");
                }else {
                    return ResultUtil.status(4,"密码修改，请稍后再试！");
                }
        }
        return null;
    }
    public ResponseVo registerUser(HttpServletRequest request,RegisterDTO dto){
        //判断验证码
        if (!CaptchaUtil.ver(dto.getVerification_code(), request)) {
            return ResultUtil.status(4,"验证码错误！");
        }
        // 清除session中的验证码
        CaptchaUtil.clear(request);
        String username = dto.getUser_login();
        if (userService.exists(username)) {
            return ResultUtil.status(4,"用户名已存在！");
        }
        String email = dto.getEmail_phone();
        if (userService.existsEmail(email)) {
            return ResultUtil.status(4,"邮箱已被注册！");
        }
        String password = dto.getUser_pass();
        String confirmPassword = dto.getUser_pass2();
        //判断两次输入密码是否相等
        if (confirmPassword != null && password != null) {
            if (!confirmPassword.equals(password)) {
                return ResultUtil.status(4,"两次密码不一致！");
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
            return ResultUtil.status(1,"注册成功！");
        }else {
            return ResultUtil.status(4,"注册失败，请稍后再试！");
        }

    }

    @PostMapping("/login")
    public ResponseVo login(LoginDTO dto, HttpServletResponse response) {
        UsernamePasswordToken token = new UsernamePasswordToken(dto.getUsername(), dto.getPassword());
        try {
            token.setRememberMe("forever".equals(dto.getRememberMe()));
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (ExcessiveAttemptsException e) {
            // 密码输错次数达到上限
            return ResultUtil.status(4,"密码输错次数达到上限，请30分钟后重试。");
        } catch (UnknownAccountException e) {
            // 未知账号
            return ResultUtil.status(4,"用户账户不存在！");
        } catch (LockedAccountException e) {
            return ResultUtil.status(4,"用户已经被锁定不能登录，请联系管理员！");
        } catch (AuthenticationException e) {
            return ResultUtil.status(4,"用户名或者密码错误！");
        }finally {
            token.clear();
        }
        //后续处理
     //   loginProcess(request);
        response.addCookie(SessionUtil.newCookie("f-session", AuthUtil.getUserId().toString()));
        return ResultUtil.vo(1,"登录成功！", JSON.parse("{goto:\"/\"}"));
    }


}
