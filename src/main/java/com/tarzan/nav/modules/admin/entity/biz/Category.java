package com.tarzan.nav.modules.admin.entity.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("biz_category")
public class Category extends BaseVo {

    private Integer pid;
    private String name;
    private String description;
    private Integer sort;
    private Integer status;
    private String icon;
    /**
     * 类型   0：目录   1：网站   2：文章
     */
    private Integer type;

    @TableField(exist = false)
    private String parentName;
    @TableField(exist = false)
    private Category parent;
    @TableField(exist = false)
    private List<Category> children;
    @TableField(exist = false)
    private List<Website> websites;

}
