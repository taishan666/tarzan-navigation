package com.tarzan.nav.modules.front;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.front.mapper.ChatItemMapper;
import com.tarzan.nav.modules.front.model.ChatItem;
import com.tarzan.nav.utils.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author tarzan
 */
@Service
public class ChatItemService extends ServiceImpl<ChatItemMapper, ChatItem> {

    public void pushChatItem(AISourceEnum source, Integer user, ChatItemVo item) {
        ChatItem chatItem = BeanUtil.copy(item,ChatItem.class);
        chatItem.setAiType(source.getCode());
        chatItem.setUserId(user);
        super.save(chatItem);
    }

    public List<ChatItemVo> getChatHistory(AISourceEnum source,Integer userId,int num){
       List<ChatItem> list= super.list(Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId,userId).eq(ChatItem::getAiType,source.getCode()).last("LIMIT "+num));
       return BeanUtil.copyList(list,ChatItemVo.class);
    }

    public int getMaxChatCnt(Integer userId) {
        //todo
        return 1000;
    }

    public int queryUsedCnt(AISourceEnum source, Integer userId) {
       return (int) super.count(Wrappers.<ChatItem>lambdaQuery().eq(ChatItem::getUserId,userId).eq(ChatItem::getAiType,source.getCode()));
    }
}
