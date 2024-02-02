package com.tarzan.nav.modules.admin.entity.biz;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.tarzan.nav.common.constant.TableConst;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author tarzan
 */
@Data
@Table(name= TableConst.SITE_LOOK)
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
