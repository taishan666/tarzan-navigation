package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author tarzan
 */
@Data
@Accessors(chain = true)
@TableName("biz_image")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BizImage {

    private String id;
    private String base64;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
