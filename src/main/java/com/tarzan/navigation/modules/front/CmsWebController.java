package com.tarzan.navigation.modules.front;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * CMS页面相关接口
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@AllArgsConstructor
public class CmsWebController {


    /**
     * 首页
     */
    @GetMapping({"/"})
    public String home() {
        return "forward:/index.html";
    }



}
