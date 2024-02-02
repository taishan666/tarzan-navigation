package com.tarzan.nav.modules.admin.entity.biz;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tarzan
 */
@Data
@TableName("biz_site_look")
@Table(name="biz_site_look")
@Entity
public class SiteLookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    private Integer siteId;
    private String userIp;
    private String province;
    private String type;
}
