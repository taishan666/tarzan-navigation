package com.tarzan.navigation.modules.front;

import com.tarzan.navigation.common.constant.CoreConst;
import com.tarzan.navigation.modules.admin.service.biz.CategoryLinkService;
import com.tarzan.navigation.modules.admin.service.biz.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 导航首页相关接口
 *
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Controller
@AllArgsConstructor
public class IndexController {

    private final CategoryService categoryService;
    private final CategoryLinkService categoryLinkService;
    /**
     * 首页
     */
    @GetMapping({"/","index","home"})
    public String home(Model model) {
        model.addAttribute("categories",categoryService.treeList());
        model.addAttribute("categoryLinks",categoryLinkService.listAll());
        return  CoreConst.WEB_PREFIX+"index";
    }

    @GetMapping({"/about"})
    public String about() {
        return  CoreConst.WEB_PREFIX+"about";
    }


}
