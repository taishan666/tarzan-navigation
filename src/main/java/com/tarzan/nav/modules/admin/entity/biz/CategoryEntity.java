package com.tarzan.nav.modules.admin.entity.biz;

import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;

import javax.persistence.*;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@Table(name = TableConst.CATEGORY)
@Entity
public class CategoryEntity extends BaseVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer pid;
    private String name;
    private String description;
    private Integer sort;
    private Integer status;
    private String icon;
    /**
     * 类型   0：目录   1：网站   2：文章
     */
    private Integer type;

}
