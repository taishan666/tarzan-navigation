package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.mapper.biz.CommentMapper;
import com.tarzan.nav.modules.admin.model.biz.BizImage;
import com.tarzan.nav.modules.admin.model.biz.Comment;
import com.tarzan.nav.modules.admin.model.sys.User;
import com.tarzan.nav.modules.admin.vo.CommentConditionVo;
import com.tarzan.nav.utils.AuthUtil;
import com.tarzan.nav.utils.BeanUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

    private final ImageService imageService;

    @CacheEvict(value = "comment", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return remove(Wrappers.<Comment>lambdaQuery().in(Comment::getId,ids).or().in(Comment::getPid,ids));
    }

    @Override
    @Cacheable(value = "comment", key = "'count'")
    public long count() {
        return count(Wrappers.<Comment>lambdaQuery().eq(Comment::getStatus, CoreConst.STATUS_VALID));
    }

    public IPage<Comment> selectComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize){
        IPage<Comment> page = new Page<>(pageNumber, pageSize);
        Comment comment= BeanUtil.copy(vo,Comment.class);
        page=page(page,Wrappers.lambdaQuery(comment).orderByDesc(Comment::getCreateTime));
        List<Comment> comments=page.getRecords();
        if(CollectionUtils.isNotEmpty(comments)){
            List<Integer> replyIds=comments.stream().map(Comment::getReplyId).collect(Collectors.toList());
            List<Comment> replyComments=listByIds(replyIds);
            Map<Integer,Comment> map=replyComments.stream().collect(Collectors.toMap(Comment::getId,e->e));
            comments.forEach(e->{
                Comment reply=map.get(e.getReplyId());
                if(reply!=null){
                    e.setReplyName(reply.getNickname());
                    e.setReplyContent(reply.getContent());
                }
            });
            page.setRecords(this.wrapper(comments));
        }
        return page;
    }

    private List<Comment> wrapper(List<Comment> comments){
        Set<String> imageIds=comments.stream().map(Comment::getAvatar).collect(Collectors.toSet());
        if(CollectionUtils.isNotEmpty(imageIds)){
            List<BizImage> images= imageService.listByIds(imageIds);
            Map<String,BizImage> map=images.stream().collect(Collectors.toMap(BizImage::getId, e->e));
            comments.forEach(e->e.setImg(map.get(e.getAvatar())));
        }
        return comments;
    }
    public Long commentsBySidNum(Integer sid){
        return super.lambdaQuery().eq(Comment::getSid,sid).eq(Comment::getStatus,CoreConst.STATUS_VALID).count();
    }
    public List<Comment> commentsBySid(Integer sid){
        List<Comment> commentTree = Collections.emptyList();
        List<Comment> comments=super.lambdaQuery().eq(Comment::getSid,sid).eq(Comment::getStatus,CoreConst.STATUS_VALID).orderByDesc(Comment::getCreateTime).list();
        if(CollectionUtils.isNotEmpty(comments)){
            this.wrapper(comments);
            commentTree=comments.stream().filter(e->CoreConst.ZERO.equals(e.getPid())).collect(Collectors.toList());
            Map<Integer,Comment> commentMap=comments.stream().collect(Collectors.toMap(Comment::getId,e->e));
            Map<Integer,List<Comment>> childMap=comments.stream().collect(Collectors.groupingBy(Comment::getPid));
            commentTree.forEach(e->{
                List<Comment> childList=childMap.get(e.getId());
                if(CollectionUtils.isNotEmpty(childList)){
                    this.wrapper(childList);
                    childList.forEach(c->{
                        Comment reply = commentMap.get(c.getReplyId());
                        if(Objects.nonNull(reply)){
                            c.setReplyName(reply.getNickname());
                        }
                    });
                }
                e.setChildren(childList);
            });
        }
        return commentTree;
    }

    @CacheEvict(value = "comment", allEntries = true)
    public boolean insertComment(Comment comment) {
        return super.save(comment);
    }

    @Cacheable(value = "comment", key = "'toAudit'")
    public List<Comment> toAudit(int num) {
       return super.lambdaQuery().select(Comment::getContent).eq(Comment::getStatus,CoreConst.STATUS_INVALID).last("limit "+num).list();
    }

    public Long toAuditNum() {
        return super.lambdaQuery().eq(Comment::getStatus,CoreConst.STATUS_INVALID).count();
    }

    @CacheEvict(value = "comment", allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public boolean audit(Comment bizComment, String replyContent) {
        if (StringUtils.isNotBlank(replyContent)) {
            User user = AuthUtil.getUser();
            Comment replyComment = new Comment();
            replyComment.setPid(bizComment.getId());
            replyComment.setSid(bizComment.getSid());
            replyComment.setReplyId(bizComment.getId());
            replyComment.setContent(replyContent);
            replyComment.setUserId(user.getId());
            replyComment.setNickname(user.getNickname());
            replyComment.setEmail(user.getEmail());
            replyComment.setAvatar(user.getImg().getBase64());
            replyComment.setIp(user.getLoginIpAddress());
            replyComment.setStatus(CoreConst.STATUS_VALID);
            this.insertComment(replyComment);
        }
        return super.updateById(bizComment);
    }
}
