package com.tarzan.nav.modules.admin.entity.biz;

import com.tarzan.nav.common.constant.TableConst;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@Table(name= TableConst.LINK)
@Entity
@NoArgsConstructor
@SuperBuilder
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
