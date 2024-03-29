package com.tarzan.nav.modules.admin.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.service.sys.UserService;
import com.tarzan.nav.modules.admin.vo.base.PageResultVo;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.utils.AuthUtil;
import com.tarzan.nav.utils.PasswordHelper;
import com.tarzan.nav.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 后台用户配置
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final UserService userService;


    /**
     * 用户列表数据
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo loadUsers(User user, Integer pageNumber, Integer pageSize) {
        IPage<User> userPage = userService.selectUsers(user, pageNumber, pageSize);
        return ResultUtil.table(userPage.getRecords(), userPage.getTotal());
    }

    @GetMapping("/add")
    public String add() {
        return CoreConst.ADMIN_PREFIX + "user/form";
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    @ResponseBody
    @CacheEvict(value = "user",allEntries = true)
    public ResponseVo add(User userForm, String confirmPassword) {
        String username = userForm.getUsername();
        if (userService.exists(username)) {
            return ResultUtil.error("用户名已存在");
        }
        String password = userForm.getPassword();
        //判断两次输入密码是否相等
        if (confirmPassword != null && password != null) {
            if (!confirmPassword.equals(password)) {
                return ResultUtil.error("两次密码不一致");
            }
        }
        userForm.setStatus(CoreConst.STATUS_VALID);
        PasswordHelper.encryptPassword(userForm);
        boolean flag = userService.save(userForm);
        if (flag) {
            return ResultUtil.success("添加用户成功");
        } else {
            return ResultUtil.error("添加用户失败");
        }
    }

    /**
     * 编辑用户详情
     */
    @GetMapping("/edit")
    public String userDetail(Model model, Integer id) {
        User user = userService.getByIdWithImage(id);
        model.addAttribute("user", user);
        return CoreConst.ADMIN_PREFIX + "user/form";
    }


    /**
     * 编辑用户
     */
    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo editUser(User userForm) {
        boolean flag = userService.updateByUserId(userForm);
        if (flag) {
            return ResultUtil.success("编辑用户成功！");
        } else {
            return ResultUtil.error("编辑用户失败");
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @ResponseBody
    public ResponseVo deleteUser(Integer id) {
        if(id.equals(AuthUtil.getUserId())){
            return ResultUtil.error("当前使用用户不能删除！");
        }
        return batchDeleteUser(Arrays.asList(id));
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batch/delete")
    @ResponseBody
    @CacheEvict(value = "user",allEntries = true)
    public ResponseVo batchDeleteUser(@RequestBody List<Integer> ids) {
        if(ids.contains(AuthUtil.getUserId())){
            return ResultUtil.error("当前使用用户不能删除！");
        }
        boolean a = userService.updateStatusBatch(ids, CoreConst.STATUS_INVALID);
        if (a) {
            return ResultUtil.success("删除用户成功");
        } else {
            return ResultUtil.error("删除用户失败");
        }
    }



}
