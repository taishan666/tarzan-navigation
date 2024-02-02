package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.entity.biz.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName(TableConst.CATEGORY)
public class Category extends CategoryEntity {

    @TableField(exist = false)
    private String parentName;
    @TableField(exist = false)
    private Category parent;
    @TableField(exist = false)
    private List<Category> children;
    @TableField(exist = false)
    private List<Website> websites;

}
