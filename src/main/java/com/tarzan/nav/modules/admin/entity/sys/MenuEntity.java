package com.tarzan.nav.modules.admin.entity.sys;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@Accessors(chain = true)
@TableName("sys_menu")
@Table(name="sys_menu")
@Entity
public class MenuEntity extends BaseVo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    /**
     * 父级权限id
     */
    private Integer pid;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 权限访问路径
     */
    private String url;

    /**
     * 权限标识
     */
    private String perms;

    /**
     * 类型   0：目录   1：菜单   2：按钮
     */
    private Integer type;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String icon;
    /**
     * 状态：1有效; 0无效
     */
    private Integer status;


}