package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author tarzan
 */
@Data
@TableName("biz_site_look")
public class SiteLook{
    private Integer id;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private Integer siteId;
    private String userIp;
    private String province;
    private String type;
}
