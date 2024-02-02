package com.tarzan.nav.modules.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@Accessors(chain = true)
@Entity
@TableName("sys_config")
@Table(name = "sys_config")
public class SysConfigEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * key
     */
    private String sysKey;

    /**
     * value
     */
    private String sysValue;

    /**
     * 状态  1：有效 0：无效
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

}