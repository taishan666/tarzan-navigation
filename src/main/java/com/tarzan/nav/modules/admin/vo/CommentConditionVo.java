package com.tarzan.nav.modules.admin.vo;

import lombok.Data;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class CommentConditionVo {
    private String userId;
    private Integer sid;
    private Integer pid;
    private String qq;
    private String email;
    private String url;
    private Integer status;

}

