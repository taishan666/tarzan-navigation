package com.tarzan.nacigation.modules.admin.controller.log;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.nacigation.modules.admin.model.log.LoginLog;
import com.tarzan.nacigation.modules.admin.service.log.LoginLogService;
import com.tarzan.nacigation.modules.admin.vo.base.PageResultVo;
import com.tarzan.nacigation.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录日志
 *
 * @since JDK1.8
 * @author tarzan
 * @date 2021年7月20日 10:24:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("log/login")
public class LogLoginController {

    private final LoginLogService loginLogService;

    /**
     * 用户登录日志列表
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo list(LoginLog param, Integer pageNumber, Integer pageSize) {
        IPage<LoginLog> page = new Page<>(pageNumber, pageSize);
        LambdaQueryWrapper<LoginLog>  wrapper= Wrappers.lambdaQuery();
        wrapper.like(StringUtils.isNotBlank(param.getLoginName()),LoginLog::getLoginName,param.getLoginName());
        wrapper.like(StringUtils.isNotBlank(param.getPhone()),LoginLog::getPhone,param.getPhone());
        wrapper.like(StringUtils.isNotBlank(param.getSourceIp()),LoginLog::getSourceIp,param.getSourceIp());
        wrapper.orderByDesc(LoginLog::getCreateTime);
        IPage<LoginLog> loginLogPage = loginLogService.page(page, wrapper);
        return ResultUtil.table(loginLogPage.getRecords(), loginLogPage.getTotal());
    }

}
