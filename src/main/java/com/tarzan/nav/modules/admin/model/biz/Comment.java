package com.tarzan.nav.modules.admin.model.biz;

import com.tarzan.nav.modules.admin.entity.biz.CommentEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
public class Comment extends CommentEntity {

    private Long loveCount;
    private String replyName;
    private String replyContent;
    private List<Comment> children;
    private BizImage img;

}
