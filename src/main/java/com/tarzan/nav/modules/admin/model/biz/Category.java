package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tarzan.nav.modules.admin.entity.biz.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class Category extends CategoryEntity {

    private String parentName;
    private Category parent;
    private List<Category> children;
    private List<Website> websites;

}
