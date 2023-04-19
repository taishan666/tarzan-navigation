package com.tarzan.nav.modules.admin.controller.common;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Category;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.service.biz.CategoryService;
import com.tarzan.nav.modules.admin.service.biz.CommentService;
import com.tarzan.nav.modules.admin.service.sys.MenuService;
import com.tarzan.nav.modules.admin.service.sys.SysConfigService;
import com.tarzan.nav.modules.admin.service.sys.UserService;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 后台管理页面跳转控制器
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/")
@AllArgsConstructor
public class AdminRouterController {

    private final SysConfigService sysConfigService;
    private final MenuService MenuService;
    private final CommentService commentService;
    private final CategoryService categoryService;
    private final UserService userService;

    /**
     * 后台首页
     */
    @RequestMapping("/admin")
    public String index(Model model) {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        model.addAttribute("menuTree", MenuService.selectMenuTreeByUserId(user.getId()));
        model.addAttribute("loginUser",userService.getByIdWithImage(user.getId()));
        model.addAttribute("comments",commentService.toAudit());
        return CoreConst.ADMIN_PREFIX + "index/index";
    }

    /**
     * 工作台
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("statistic", null);
        return CoreConst.ADMIN_PREFIX + "index/dashboard";
    }

    /**
     * 公告列表入口
     */
    @GetMapping("/notices")
    public String notices() {
        return CoreConst.ADMIN_PREFIX + "notice/list";
    }

    /**
     * 用户列表入口
     */
    @GetMapping("/users")
    public String userList() {
        return CoreConst.ADMIN_PREFIX + "user/list";
    }

    /**
     * 角色列表入口
     */
    @GetMapping("/roles")
    public String roleList() {
        return CoreConst.ADMIN_PREFIX + "role/list";
    }

    /**
     * 权限列表入口
     */
    @GetMapping("/menus")
    public String permissionList() {
        return CoreConst.ADMIN_PREFIX + "menu/list";
    }

    /**
     * 在线用户入口
     */
    @GetMapping("/online/users")
    public String onlineUsers() {
        return CoreConst.ADMIN_PREFIX + "onlineUsers/list";
    }

    /**
     * 登录日志入口
     */
    @GetMapping("/login/logs")
    public String loginLogs() {
        return CoreConst.ADMIN_PREFIX + "logLogin/list";
    }

    /**
     * 错误日志入口
     */
    @GetMapping("/error/logs")
    public String errorLogs() {
        return CoreConst.ADMIN_PREFIX + "logError/list";
    }

    /**
     * 数据备份入口
     */
    @GetMapping("/db/backup")
    public String backup() { return CoreConst.ADMIN_PREFIX + "backup/list"; }

    /**
     * 网站基本信息
     *
     * @param model
     */
    @GetMapping("/siteInfo")
    public String siteInfo(Model model) {
        model.addAttribute("siteInfo", sysConfigService.getInfo());
        return CoreConst.ADMIN_PREFIX + "site/siteInfo";
    }

    /**
     * 网站
     */
    @GetMapping("/websites")
    public String websites(Model model) {
        Category category=categoryService.lambdaQuery().last("limit 1").one();
        Integer categoryId=0;
        if(category!=null){
            categoryId=category.getId();
        }
        model.addAttribute("categoryId", categoryId);
        return CoreConst.ADMIN_PREFIX + "website/list";
    }

    /**
     * 友情链接
     */
    @GetMapping("/links")
    public String links(Model model) {
        return CoreConst.ADMIN_PREFIX + "link/list";
    }

    /**
     * 分类
     */
    @GetMapping("/categories")
    public String categories() {
        return CoreConst.ADMIN_PREFIX + "category/list";
    }


    /**
     * 标签
     */
    @GetMapping("/tags")
    public String tags() {
        return CoreConst.ADMIN_PREFIX + "tag/list";
    }

    /**
     * 评论
     */
    @GetMapping("/comments")
    public String comments() {
        return CoreConst.ADMIN_PREFIX + "comment/list";
    }



}
