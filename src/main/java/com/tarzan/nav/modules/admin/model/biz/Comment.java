package com.tarzan.nav.modules.admin.model.biz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tarzan.nav.modules.admin.entity.biz.CommentEntity;
import lombok.Data;

import java.util.List;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Data
@TableName("biz_comment")
public class Comment extends CommentEntity {

    @TableField(exist = false)
    private Long loveCount;
    @TableField(exist = false)
    private String replyName;
    @TableField(exist = false)
    private String replyContent;
    @TableField(exist = false)
    private List<Comment> children;
    @TableField(exist = false)
    private BizImage img;

}
