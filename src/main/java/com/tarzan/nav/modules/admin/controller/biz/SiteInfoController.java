package com.tarzan.nav.modules.admin.controller.biz;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.utils.ResultUtil;
import com.tarzan.nav.modules.admin.service.sys.SysConfigService;
import com.tarzan.nav.modules.admin.vo.base.ResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseVo save(@RequestParam Map<String, String> map) {
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
