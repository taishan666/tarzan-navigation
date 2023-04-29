package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("biz_website")
public class Website extends BaseVo {

    private String url;
    private String name;
    private String description;
    private String imageId;
    private String email;
    private String qq;
    private Integer status;
    private Integer origin;
    private Integer categoryId;
    private String remark;

    @TableField(exist = false)
    private BizImage img;
    @TableField(exist = false)
    private String categoryName;

}
