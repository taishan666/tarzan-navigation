package com.tarzan.navigation.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author tarzan
 */
@Data
@Accessors(chain = true)
@TableName("biz_category_link")
public class CategoryLink {
    private Integer id;
    private Integer categoryId;
    private Integer linkId;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(exist = false)
    private Link link;
}
