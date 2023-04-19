package com.tarzan.nav.modules.admin.controller.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.model.biz.Notice;
import com.tarzan.nav.modules.admin.service.biz.NoticeService;
import com.tarzan.nav.modules.admin.vo.base.PageResultVo;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import com.tarzan.nav.utils.DateUtil;
import com.tarzan.nav.utils.ResultUtil;
import com.tarzan.nav.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author tarzan
 */
@Controller
@RequestMapping("/notice")
@AllArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/add")
    public String add(Model model) {
        return CoreConst.ADMIN_PREFIX + "notice/publish";
    }


    @GetMapping("/edit")
    public String edit(Model model,Integer id) {
        model.addAttribute("notice", noticeService.getById(id));
        return CoreConst.ADMIN_PREFIX + "notice/publish";
    }


    @PostMapping("/submit")
    @ResponseBody
    @CacheEvict(value = "notice", allEntries = true)
    public ResponseVo submit(Notice notice) {
        Date endDate=DateUtil.addDays(new Date(),notice.getDays());
        notice.setEndTime(endDate);
        if(Objects.isNull(notice.getId())){
            if (noticeService.save(notice)) {
                return ResultUtil.success("添加公告成功");
            } else {
                return ResultUtil.error("添加公告失败");
            }
        }else {
            if (noticeService.updateById(notice)) {
                return ResultUtil.success("编辑公告成功");
            } else {
                return ResultUtil.error("编辑公告失败");
            }
        }
    }

    @PostMapping("/page")
    @ResponseBody
    public PageResultVo page(String title, Integer pageNumber, Integer pageSize) {
        IPage<Notice> page = new Page<>(pageNumber, pageSize);
        IPage<Notice> noticePage = noticeService.lambdaQuery().select(Notice::getId,Notice::getTitle,Notice::getCreateTime,Notice::getEndTime)
                .like(StringUtil.isNotBlank(title),Notice::getTitle,title).page(page);
        return ResultUtil.table(noticePage.getRecords(), noticePage.getTotal());
    }

    @PostMapping("/remove")
    @ResponseBody
    public ResponseVo remove(@RequestBody List<Integer> ids) {
        if (noticeService.removeByIds(ids)) {
            return ResultUtil.success("删除公告成功");
        } else {
            return ResultUtil.error("删除公告失败");
        }
    }
}
