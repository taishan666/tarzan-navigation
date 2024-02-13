package com.tarzan.nav.modules.aichat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.mapper.ChatItemMapper;
import com.tarzan.nav.modules.aichat.model.ChatItem;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.utils.BeanUtil;
import com.tarzan.nav.utils.DateUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author tarzan
 */
@Service
public class ChatItemService extends ServiceImpl<ChatItemMapper, ChatItem> {

    @Async
    @CacheEvict(value = "chatItem", allEntries = true)
    public void pushChatItem(AISourceEnum source, Integer user, ChatItemVo item) {
        ChatItem chatItem = BeanUtil.copy(item,ChatItem.class);
        chatItem.setAiType(source.getCode());
        chatItem.setUserId(user);
        super.save(chatItem);
    }

    @Cacheable(value = "chatItem",key = "#source+'_'+#userId")
    public List<ChatItemVo> getChatHistory(AISourceEnum source,Integer userId,int num) {
        List<ChatItem> list = super.list(Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId, userId).eq(ChatItem::getAiType, source.getCode()).orderByDesc(ChatItem::getAnswerTime).last("LIMIT " + num));
        if (CollectionUtils.isNotEmpty(list)) {
            return BeanUtil.copyList(list, ChatItemVo.class);
        }
        return new ArrayList<>(1);
    }

    @Cacheable(value = "chatItem",key = "'usedcnt_'+#source+'_'+#userId")
    public int queryUsedCnt(AISourceEnum source, Integer userId) {
        String dateStr=DateUtil.format(new Date(),DateUtil.chineseDtFormat);
        LambdaQueryWrapper wrapper=Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId,userId).eq(ChatItem::getAiType,source.getCode()).like(ChatItem::getQuestionTime,dateStr);
       return (int) super.count(wrapper);
    }

    @CacheEvict(value = "chatItem", allEntries = true)
    public void lTrim(AISourceEnum source,Integer userId,int num) {
        LambdaQueryWrapper wrapper=Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId, userId).eq(ChatItem::getAiType, source.getCode()).orderByDesc(ChatItem::getAnswerTime).last("LIMIT " + num+","+100);
        super.remove(wrapper);
    }
}
