package com.tarzan.nav.modules.admin.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.common.enums.UserEnum;
import com.tarzan.nav.common.event.LoginLogEvent;
import com.tarzan.nav.modules.admin.model.log.LoginLog;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.service.log.LoginLogService;
import com.tarzan.nav.modules.admin.service.sys.UserService;
import com.tarzan.nav.modules.admin.vo.ChangePasswordVo;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.shiro.realm.UserRealm;
import com.tarzan.nav.utils.*;
import com.wf.captcha.utils.CaptchaUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 后台首页、登录等接口
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/system")
public class SystemController {

    private final UserService userService;
    private final LoginLogService loginLogService;
    private final UserRealm shiroRealm;

    /**
     * 注册
     */
    @GetMapping(value = "/register")
    public ModelAndView register(){
        ModelAndView modelAndView = new ModelAndView();
        if (CoreConst.IS_REGISTERED.get()) {
            modelAndView.setView(new RedirectView("/admin", true, false));
            return modelAndView;
        }
        modelAndView.setViewName(CoreConst.ADMIN_PREFIX+"system/register");
        return modelAndView;
    }

    /**
     * 提交注册
     */
    @PostMapping("/register")
    @ResponseBody
    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = "user",allEntries = true)
    public ResponseVo register(HttpServletRequest request, User registerUser, String confirmPassword, String verification){
        //判断验证码
/*        if (!CaptchaUtil.ver(verification, request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            return ResultUtil.error("验证码错误！");
        }*/
        String username = registerUser.getUsername();
        if (userService.exists(username)) {
            return ResultUtil.error("用户名已存在！");
        }
        String password = registerUser.getPassword();
        //判断两次输入密码是否相等
        if (confirmPassword != null && password != null) {
            if (!confirmPassword.equals(password)) {
                return ResultUtil.error("两次密码不一致！");
            }
        }
        registerUser.setNickname(CoreConst.ADMIN_NAME);
        registerUser.setStatus(CoreConst.STATUS_VALID);
        PasswordHelper.encryptPassword(registerUser);
        CoreConst.IS_REGISTERED.set(true);
        //注册
        boolean flag = userService.save(registerUser);
        if(flag){
            return ResultUtil.success("注册成功！");
        }else {
            return ResultUtil.error("注册失败，请稍后再试！");
        }

    }


    /**
     * 访问登录
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        if (SecurityUtils.getSubject().isAuthenticated()) {
            modelAndView.setView(new RedirectView("/admin", true, false));
            return modelAndView;
        }
        modelAndView.setViewName(CoreConst.ADMIN_PREFIX+"system/login");
        return modelAndView;
    }

    /**
     * 提交登录
     *
     * @param request
     * @param username
     * @param password
     * @param verification
     * @param rememberMe
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public ResponseVo login(HttpServletRequest request, String username, String password, String verification,
                            @RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe) {
        //判断验证码
        if (!CaptchaUtil.ver(verification, request)) {
            // 清除session中的验证码
            CaptchaUtil.clear(request);
            return ResultUtil.error("验证码错误！");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            token.setRememberMe(1 == rememberMe);
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
        } catch (ExcessiveAttemptsException e) {
            // 密码输错次数达到上限
            return ResultUtil.error("密码输错次数达到上限，请30分钟后重试。");
        } catch (UnknownAccountException e) {
            // 未知账号
            return ResultUtil.error("用户账户不存在！");
        } catch (LockedAccountException e) {
            return ResultUtil.error("用户已经被锁定不能登录，请联系管理员！");
        } catch (AuthenticationException e) {
            return ResultUtil.error("用户名或者密码错误！");
        }finally {
            token.clear();
        }
        //后续处理
        loginProcess(request);
        return ResultUtil.success("登录成功！");
    }


    /**
     * 后续处理
     * @param request
     */
    @Async
    public void loginProcess(HttpServletRequest request) {
        User user=(User) SecurityUtils.getSubject().getPrincipal();
        userService.updateLastLoginTime(user.getId());
        String userType= request.getHeader(CoreConst.USER_TYPE_HEADER_KEY)==null?UserEnum.WEB.getName():request.getHeader(CoreConst.USER_TYPE_HEADER_KEY);
        saveLoginLog(userType,user);
    }
    /**
     * 保存日志
     *
     * @param userType
     * @param userInfo
     */
    @Async
    public void saveLoginLog(String userType,User userInfo) {
        Date now = new Date();
        LoginLog loginLog = new LoginLog();
        loginLog.setStartTime(now);
        loginLog.setSourceIp(IpUtil.getIpAddr(Objects.requireNonNull(WebUtil.getRequest())));
        if (userType.equals(UserEnum.WEB.getName())) {
            loginLog.setSource("PC登录");
        } else if (userType.equals(UserEnum.APP.getName())) {
            loginLog.setSource("APP登录");
        }
        loginLog.setName(userInfo.getNickname());
        loginLog.setPhone(userInfo.getPhone());
        loginLog.setLoginName(userInfo.getUsername());
        SpringUtil.publishEvent(new LoginLogEvent(loginLog));
    }

    /**
     * 踢出
     *
     * @param model
     * @return
     */
    @GetMapping("/kickOut")
    public String kickOut(Model model) {
        return  CoreConst.ADMIN_PREFIX+"/system/kickOut";
    }

    /**
     * 登出
     *
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout() {
        Subject subject = SecurityUtils.getSubject();
        if (null != subject) {
            String username = ((User) SecurityUtils.getSubject().getPrincipal()).getUsername();
            Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
            userService.kickOut(sessionId, username);
            LambdaQueryWrapper<LoginLog> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(LoginLog::getId).eq(LoginLog::getLoginName, username);
            queryWrapper.orderByDesc(LoginLog::getCreateTime);
            List<LoginLog> loginLogList = loginLogService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(loginLogList)) {
                LoginLog loginLog =new LoginLog();
                loginLog.setId(loginLogList.get(0).getId());
                loginLog.setEndTime(new Date());
                SpringUtil.publishEvent(new LoginLogEvent(loginLog));
            }
        }
        assert subject != null;
        subject.logout();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(CoreConst.ADMIN_PREFIX+"/system/login");
        return modelAndView;
    }

    /**
     * 编辑个人信息
     */
    @GetMapping("/userInfo")
    public String userDetail(Model model) {
        User user = userService.getByIdWithImage(AuthUtil.getUserId());
        model.addAttribute("user", user);
        return CoreConst.ADMIN_PREFIX + "user/info";
    }

    /**
     * 修改密码
     */
    @GetMapping("/password")
    public String password(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("user", user);
        return CoreConst.ADMIN_PREFIX + "user/password";
    }


    /**
     * 修改密码
     */
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVo changePassword(ChangePasswordVo changePasswordVo) {
        if (!changePasswordVo.getNewPassword().equals(changePasswordVo.getConfirmNewPassword())) {
            return ResultUtil.error("两次密码输入不一致");
        }
        User loginUser = userService.getById(((User) SecurityUtils.getSubject().getPrincipal()).getId());
        User newUser = BeanUtil.copy(loginUser, User.class);
        String sysOldPassword = loginUser.getPassword();
        newUser.setPassword(changePasswordVo.getOldPassword());
        String entryOldPassword = PasswordHelper.getEncryptPassword(newUser);
        if (sysOldPassword.equals(entryOldPassword)) {
            newUser.setPassword(changePasswordVo.getNewPassword());
            PasswordHelper.encryptPassword(newUser);
            userService.updateById(newUser);
            //*清除登录缓存*//
            List<Integer> userIds = new ArrayList<>();
            userIds.add(loginUser.getId());
            shiroRealm.removeCachedAuthenticationInfo(userIds);
        } else {
            return ResultUtil.error("您输入的旧密码有误");
        }
        return ResultUtil.success("修改密码成功");
    }




}
