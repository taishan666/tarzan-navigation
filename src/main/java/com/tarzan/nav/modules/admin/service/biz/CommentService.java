package com.tarzan.nav.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.modules.admin.entity.biz.BizImageEntity;
import com.tarzan.nav.modules.admin.entity.biz.CommentEntity;
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
public class CommentService extends ServiceImpl<CommentMapper, CommentEntity> {

    private final ImageService imageService;

    @CacheEvict(value = "comment", allEntries = true)
    public boolean deleteBatch(List<Integer> ids) {
        return remove(Wrappers.<CommentEntity>lambdaQuery().in(CommentEntity::getId,ids).or().in(CommentEntity::getPid,ids));
    }

    @Override
    @Cacheable(value = "comment", key = "'count'")
    public long count() {
        return count(Wrappers.<CommentEntity>lambdaQuery().eq(CommentEntity::getStatus, CoreConst.STATUS_VALID));
    }

    public IPage<Comment> selectComments(CommentConditionVo vo, Integer pageNumber, Integer pageSize){
        IPage<CommentEntity> page = new Page<>(pageNumber, pageSize);
        CommentEntity comment= BeanUtil.copy(vo,CommentEntity.class);
        page=page(page,Wrappers.lambdaQuery(comment).orderByDesc(CommentEntity::getCreateTime));
        return this.wrapperPage(page);
    }

    private IPage<Comment> wrapperPage(IPage<CommentEntity> entityPage){
        IPage<Comment> page=new Page<>(entityPage.getCurrent(),entityPage.getPages(),entityPage.getTotal());
        page.setRecords(this.wrapper(entityPage.getRecords()));
        return page;
    }

    private List<Comment> wrapper(List<CommentEntity> commentEntities){
        if(CollectionUtils.isNotEmpty(commentEntities)){
            List<Comment> comments=BeanUtil.copyList(commentEntities,Comment.class);
            Set<String> imageIds=comments.stream().map(CommentEntity::getAvatar).collect(Collectors.toSet());
            if(CollectionUtils.isNotEmpty(imageIds)){
                List<BizImageEntity> images= imageService.listByIds(imageIds);
                Map<String,BizImageEntity> map=images.stream().collect(Collectors.toMap(BizImageEntity::getId, e->e));
                comments.forEach(e->e.setImg(BeanUtil.copy(map.get(e.getAvatar()),BizImage.class)));
            }
            List<Integer> replyIds=comments.stream().map(CommentEntity::getReplyId).collect(Collectors.toList());
            List<CommentEntity> replyComments=listByIds(replyIds);
            Map<Integer,CommentEntity> map=replyComments.stream().collect(Collectors.toMap(CommentEntity::getId,e->e));
            comments.forEach(e->{
                CommentEntity reply=map.get(e.getReplyId());
                if(reply!=null){
                    e.setReplyName(reply.getNickname());
                    e.setReplyContent(reply.getContent());
                }
            });
            return comments;
        }
        return Collections.emptyList();
    }

    public Long commentsBySidNum(Integer sid){
        return super.lambdaQuery().eq(CommentEntity::getSid,sid).eq(CommentEntity::getStatus,CoreConst.STATUS_VALID).count();
    }
    public List<Comment> commentsBySid(Integer sid){
        List<Comment> commentTree = Collections.emptyList();
        List<CommentEntity> commentEntities=super.lambdaQuery().eq(CommentEntity::getSid,sid).eq(CommentEntity::getStatus,CoreConst.STATUS_VALID).orderByDesc(CommentEntity::getCreateTime).list();
        if(CollectionUtils.isNotEmpty(commentEntities)){
            List<Comment> comments= this.wrapper(commentEntities);
            commentTree=comments.stream().filter(e->CoreConst.ZERO.equals(e.getPid())).collect(Collectors.toList());
            Map<Integer,List<Comment>> childMap=comments.stream().collect(Collectors.groupingBy(Comment::getPid));
            commentTree.forEach(e->{
                List<Comment> childList=childMap.get(e.getId());
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
    public List<CommentEntity> toAudit(int num) {
       return super.lambdaQuery().select(CommentEntity::getContent).eq(CommentEntity::getStatus,CoreConst.STATUS_INVALID).last("limit "+num).list();
    }

    public Long toAuditNum() {
        return super.lambdaQuery().eq(CommentEntity::getStatus,CoreConst.STATUS_INVALID).count();
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
