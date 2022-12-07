package com.tarzan.nacigation.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("biz_category_link")
public class CategoryLink {
    private Integer id;
    private Integer categoryId;
    private Integer linkId;
    private Date createTime;
    @TableField(exist = false)
    private Link link;
}
