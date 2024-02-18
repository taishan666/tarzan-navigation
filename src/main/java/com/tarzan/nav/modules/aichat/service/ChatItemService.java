package com.tarzan.nav.modules.aichat.service;

import cn.hutool.core.util.RandomUtil;
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
        List<ChatItem> list = super.list(Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId, userId).eq(ChatItem::getAiType, source.getCode()).orderByDesc(ChatItem::getQuestionTime).last("LIMIT " + num));
        if (CollectionUtils.isNotEmpty(list)) {
            return BeanUtil.copyList(list, ChatItemVo.class);
        }
        return new ArrayList<>(1);
    }

    @Cacheable(value = "chatItem",key = "'usedcnt_'+#source+'_'+#userId")
    public int queryUsedCnt(AISourceEnum source, Integer userId) {
       return  baseMapper.getTodayUsedCnt(source.getCode(),userId);
    }

    @CacheEvict(value = "chatItem", allEntries = true)
    public void lTrim(AISourceEnum source,Integer userId,int num) {
        LambdaQueryWrapper wrapper=Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId, userId).eq(ChatItem::getAiType, source.getCode()).orderByDesc(ChatItem::getQuestionTime).last("LIMIT " + num+","+100);
        super.remove(wrapper);
    }

    public ChatItem getChatByUid(String chatUid) {
       return super.getOne(Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getChatUid, chatUid));
    }

    /**
     * 获取几分钟前的相同问题的答案
     *
     * 为了保证相同问题回答的多样性，时间越小越好
     *
     * @param source
     * @param question
     * @param minutes
     * @return {@link ChatItem}
     */
    public ChatItem getSameQuestionBefore(AISourceEnum source, String question,long minutes) {
        return getSameQuestionBefore(source,question,minutes,0);
    }

    public ChatItem getSameQuestionBefore(AISourceEnum source, String question,long minutes,long secs) {
        LambdaQueryWrapper<ChatItem> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ChatItem::getQuestion, question).eq(ChatItem::getAiType, source.getCode());
        Date lastMinute = DateUtil.addMinutes(DateUtil.now(), -minutes);
        Date lastSecs = DateUtil.addSeconds(lastMinute, -secs);
        wrapper.ge(ChatItem::getQuestionTime, lastSecs);
        List<ChatItem> list=super.list(wrapper);
        if(CollectionUtils.isNotEmpty(list)){
           return RandomUtil.randomEle(list);
        }
        return null;
    }
}
