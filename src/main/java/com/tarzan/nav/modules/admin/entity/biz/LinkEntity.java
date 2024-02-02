package com.tarzan.nav.modules.admin.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("biz_link")
@Table(name="biz_link")
@Entity
public class LinkEntity extends BaseVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String url;
    private String name;
    private String description;
    private String imageId;
    private String email;
    private String qq;
    private Integer status;
    private Integer origin;
    private String remark;

}
