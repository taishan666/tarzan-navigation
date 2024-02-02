package com.tarzan.nav.modules.admin.entity.biz;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("biz_website")
@Table(name="biz_website")
@Entity
@SuperBuilder
@NoArgsConstructor
public class WebsiteEntity extends BaseVo {

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
    private Integer categoryId;
    private String remark;

}
