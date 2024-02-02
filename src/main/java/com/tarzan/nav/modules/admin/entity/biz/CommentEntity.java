package com.tarzan.nav.modules.admin.entity.biz;

import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;

import javax.persistence.*;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@Table(name="biz_comment")
@Entity
public class CommentEntity extends BaseVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;
    /** 业务id */
    private Integer sid;
    /** 父id */
    private Integer pid;
    /** 回复id */
    private Integer replyId;
    private String qq;
    private String nickname;
    private String avatar;
    private String email;
    private String url;
    private Integer status;
    private String ip;
    private String lng;
    private String lat;
    private String address;
    private String os;
    private String osShortName;
    private String browser;
    private String browserShortName;
    private String content;
    private String location;

}
