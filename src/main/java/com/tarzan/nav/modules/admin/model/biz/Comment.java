package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("biz_comment")
public class Comment extends BaseVo {

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
    private String remark;

    @TableField(exist = false)
    private Long loveCount;
    @TableField(exist = false)
    private String replyName;
    @TableField(exist = false)
    private String replyContent;
    @TableField(exist = false)
    private List<Comment> children;

}
