package com.tarzan.nacigation.modules.admin.controller.biz;

import com.tarzan.nacigation.common.constant.CoreConst;
import com.tarzan.nacigation.utils.ResultUtil;
import com.tarzan.nacigation.modules.admin.service.sys.SysConfigService;
import com.tarzan.nacigation.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 后台网站信息配置
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@RequestMapping("siteInfo")
@AllArgsConstructor
public class SiteInfoController {

    private final SysConfigService configService;

    @PostMapping("/edit")
    @ResponseBody
    public ResponseVo save(@RequestParam Map<String, String> map, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (map.containsKey(CoreConst.SITE_STATIC_KEY)) {
                boolean siteStaticOn = "on".equalsIgnoreCase(map.get(CoreConst.SITE_STATIC_KEY));
                CoreConst.SITE_STATIC.set(siteStaticOn);
            }
            configService.updateAll(map);
            return ResultUtil.success("保存网站信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.error("保存网站信息失败");
        }
    }
}
