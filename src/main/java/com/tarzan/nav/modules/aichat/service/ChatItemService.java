package com.tarzan.nav.modules.aichat.service;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.mapper.ChatItemMapper;
import com.tarzan.nav.modules.aichat.model.ChatItem;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.utils.BeanUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tarzan
 */
@Service
public class ChatItemService extends ServiceImpl<ChatItemMapper, ChatItem> {

    @Async
    public void pushChatItem(AISourceEnum source, Integer user, ChatItemVo item) {
        ChatItem chatItem = BeanUtil.copy(item,ChatItem.class);
        chatItem.setAiType(source.getCode());
        chatItem.setUserId(user);
        super.save(chatItem);
    }

    public List<ChatItemVo> getChatHistory(AISourceEnum source,Integer userId,int num) {
        List<ChatItem> list = super.list(Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId, userId).eq(ChatItem::getAiType, source.getCode()).orderByDesc(ChatItem::getAnswerTime).last("LIMIT " + num));
        if (CollectionUtils.isNotEmpty(list)) {
            return BeanUtil.copyList(list, ChatItemVo.class);
        }
        return new ArrayList<>(1);
    }

    public int getMaxChatCnt(Integer userId) {
        //todo
        return 100;
    }

    public int queryUsedCnt(AISourceEnum source, Integer userId) {
       return (int) super.count(Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId,userId).eq(ChatItem::getAiType,source.getCode()));
    }
}
