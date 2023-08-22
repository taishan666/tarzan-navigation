package com.tarzan.nav.modules.admin.controller.log;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.nav.modules.admin.model.log.LogError;
import com.tarzan.nav.modules.admin.service.log.LogErrorService;
import com.tarzan.nav.modules.admin.vo.base.PageResultVo;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.utils.ResultUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 登录日志
 *
 * @since JDK1.8
 * @author tarzan
 * @date 2021年7月20日 10:24:07
 */
@RestController
@AllArgsConstructor
@RequestMapping("log/error")
public class LogErrorController {

    private final LogErrorService logErrorService;

    /**
     * 用户登录日志列表:
     */
    @PostMapping("/list")
    @ResponseBody
    public PageResultVo list(LogError param, Integer pageNumber, Integer pageSize) {
        IPage<LogError> page = new Page<>(pageNumber, pageSize);
        LambdaQueryWrapper<LogError>  wrapper= Wrappers.lambdaQuery(param);
        wrapper.orderByDesc(LogError::getCreateTime);
        IPage<LogError> logPage = logErrorService.page(page, wrapper);
        return ResultUtil.table(logPage.getRecords(), logPage.getTotal());
    }

    @PostMapping("/batch/delete")
    @ResponseBody
    public ResponseVo deleteBatch(@RequestBody List<Integer> ids) {
        boolean flag = logErrorService.removeByIds(ids);
        if (flag) {
            return ResultUtil.success("删除成功");
        } else {
            return ResultUtil.error("删除失败");
        }
    }

}
