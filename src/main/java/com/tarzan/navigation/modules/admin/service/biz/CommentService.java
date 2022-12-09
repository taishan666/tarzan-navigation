package com.tarzan.navigation.modules.admin.service.biz;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.navigation.common.constant.CoreConst;
import com.tarzan.navigation.modules.admin.mapper.biz.CommentMapper;
import com.tarzan.navigation.modules.admin.model.biz.Comment;
import com.tarzan.navigation.modules.admin.model.sys.User;
import com.tarzan.navigation.modules.admin.vo.CommentConditionVo;
import com.tarzan.navigation.utils.AuthUtil;
import com.tarzan.navigation.utils.BeanUtil;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tarzan liu
 * @since JDK1.8
 * @date 2021年5月11日
 */
@Service
@AllArgsConstructor
public class CommentService extends ServiceImpl<CommentMapper, Comment> {

  //  private final ArticleService articleService;

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
            List<Integer> ids=comments.stream().map(Comment::getId).collect(Collectors.toList());
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
            page.setRecords(comments);
        }
        return page;
    }

    public IPage<Comment> commentsBySid(Integer sid, Integer pageNumber, Integer pageSize){
        IPage<Comment> page = new Page<>(pageNumber, pageSize);
        page=super.page(page,Wrappers.<Comment>lambdaQuery().eq(Comment::getSid,sid).eq(Comment::getStatus,CoreConst.STATUS_VALID).eq(Comment::getPid,CoreConst.ZERO).orderByDesc(Comment::getCreateTime));
        List<Comment> comments=page.getRecords();
        if(CollectionUtils.isNotEmpty(comments)){
            List<Integer> ids=comments.stream().map(Comment::getId).collect(Collectors.toList());
            List<Comment> children=super.lambdaQuery().eq(Comment::getStatus,CoreConst.STATUS_VALID).in(Comment::getPid,ids).orderByDesc(Comment::getCreateTime).list();
            Map<Integer,List<Comment>> childMap=children.stream().collect(Collectors.groupingBy(Comment::getPid));
            List<Comment> bizComments=new ArrayList<>();
            bizComments.addAll(comments);
            bizComments.addAll(children);
            List<Integer> bizIds=bizComments.stream().map(Comment::getId).collect(Collectors.toList());
            Map<Integer,Comment> bizMap=bizComments.stream().collect(Collectors.toMap(Comment::getId,e->e));
            comments.forEach(e->{
                List<Comment> childList=childMap.get(e.getId());
                if(CollectionUtils.isNotEmpty(childList)){
                    childList.forEach(c->{
                        Comment reply=bizMap.get(c.getReplyId());
                        c.setReplyName(reply.getNickname());
                        c.setReplyContent(reply.getContent());
                    });
                }
                e.setChildren(childList);
            });
            page.setRecords(comments);
        }
        return page;
    }

    @CacheEvict(value = "comment", allEntries = true)
    public boolean insertComment(Comment comment) {
        Date date = new Date();
        comment.setCreateTime(date);
        comment.setUpdateTime(date);
        return save(comment);
    }

    @Cacheable(value = "comment", key = "'toAudit'")
    public List<Comment> toAudit() {
       return super.lambdaQuery().select(Comment::getContent).eq(Comment::getStatus,CoreConst.STATUS_INVALID).list();
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
            replyComment.setAvatar(user.getImg());
            replyComment.setIp(user.getLoginIpAddress());
            replyComment.setStatus(CoreConst.STATUS_VALID);
            this.insertComment(replyComment);
        }
        return super.updateById(bizComment);
    }
}
