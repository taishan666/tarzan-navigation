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
@TableName("biz_image")
public class BizImage {

    private Integer id;
    private String base64;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
}
