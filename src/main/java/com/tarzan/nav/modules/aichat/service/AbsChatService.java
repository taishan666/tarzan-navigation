package com.tarzan.nav.modules.aichat.service;

import com.google.common.collect.Sets;
import com.tarzan.nav.modules.aichat.constants.ChatConstants;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import com.tarzan.nav.modules.front.ChatItemService;
import com.tarzan.nav.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 聊天的抽象模板类
 *
 * @author YiHui
 * @date 2023/6/9
 */
@Slf4j
@Service
public abstract class AbsChatService implements ChatService {
/*    @Autowired
    private UserAiService userAiService;*/
    @Resource
    private ChatItemService chatItemService;


    /**
     * 查询已经使用的次数
     *
     * @param userId
     * @return
     */
    protected int queryUsedCnt(Integer userId) {
        return chatItemService.queryUsedCnt(source(),userId);
    }


    /**
     * 使用次数+1
     *
     * @param userId
     * @return
     */
    protected Integer incrCnt(Integer userId) {
        Integer cnt = chatItemService.queryUsedCnt(source(),userId)+1;
        return cnt;
    }

    /**
     * 保存聊天记录
     */
    protected void recordChatItem(Integer userId, ChatItemVo item) {
        chatItemService.pushChatItem(source(), userId, item);
    }

    /**
     * 查询用户的聊天历史
     *
     * @return
     */
    @Override
    public ChatRecordsVo getChatHistory(Integer userId, AISourceEnum aiSource) {
        if (aiSource == null) {
            aiSource = source();
        }
        List<ChatItemVo> chats = chatItemService.getChatHistory(source(),userId,50);
        chats.add(0, new ChatItemVo().initAnswer(String.format("开始你和派聪明(%s-大模型)的AI之旅吧!", aiSource.getName())));
        ChatRecordsVo vo = new ChatRecordsVo();
        vo.setMaxCnt(getMaxQaCnt(userId));
        vo.setUsedCnt(queryUsedCnt(userId));
        vo.setSource(source());
        vo.setRecords(chats);
        return vo;
    }

    @Override
    public ChatRecordsVo chat(Integer user, String question) {
        // 构建提问、返回的实体类，计算使用次数，最大次数
        ChatRecordsVo res = initResVo(user, question);
        if (!res.hasQaCnt()) {
            return res;
        }

        // 执行提问
        answer(user, res);
        // 返回AI应答结果
        return res;
    }

    @Override
    public ChatRecordsVo chat(Integer user, String question, Consumer<ChatRecordsVo> consumer) {
        ChatRecordsVo res = initResVo(user, question);
        if (!res.hasQaCnt()) {
            return res;
        }

        // 同步聊天时，直接返回结果
        answer(user, res);
        consumer.accept(res);
        return res;
    }

    private ChatRecordsVo initResVo(Integer userId, String question) {
        ChatRecordsVo res = new ChatRecordsVo();
        res.setSource(source());
        int maxCnt = getMaxQaCnt(userId);
        int usedCnt = queryUsedCnt(userId);
        res.setMaxCnt(maxCnt);
        res.setUsedCnt(usedCnt);

        ChatItemVo item = new ChatItemVo().initQuestion(question);
        if (!res.hasQaCnt()) {
            // 次数已经使用完毕
            item.initAnswer(ChatConstants.TOKEN_OVER);
        }
        res.setRecords(Arrays.asList(item));
        return res;
    }

    protected AiChatStatEnum answer(Integer userId, ChatRecordsVo res) {
        ChatItemVo itemVo = res.getRecords().get(0);
        AiChatStatEnum ans;
        List<String> sensitiveWords =  new ArrayList<>();
        if (!CollectionUtils.isEmpty(sensitiveWords)) {
            itemVo.initAnswer(String.format(ChatConstants.SENSITIVE_QUESTION, sensitiveWords));
            ans = AiChatStatEnum.ERROR;
        } else {
            ans = doAnswer(userId, itemVo);
            if (ans == AiChatStatEnum.END) {
                processAfterSuccessAnswered(userId, res);
            }
        }
        return ans;
    }

    /**
     * 提问，并将结果写入chat
     *
     * @param user
     * @param chat
     * @return true 表示正确回答了； false 表示回答出现异常
     */
    public abstract AiChatStatEnum doAnswer(Integer user, ChatItemVo chat);

    /**
     * 成功返回之后的后置操作
     *
     * @param userId
     * @param response
     */
    protected void processAfterSuccessAnswered(Integer userId, ChatRecordsVo response) {
        // 回答成功，保存聊天记录，剩余次数-1
        response.setUsedCnt(incrCnt(userId).intValue());
        recordChatItem(userId, response.getRecords().get(0));
        if (response.getUsedCnt() > ChatConstants.MAX_HISTORY_RECORD_ITEMS) {
            // 最多保存五百条历史聊天记录
          //  RedisClient.lTrim(ChatConstants.getAiHistoryRecordsKey(source(), userId), 0, ChatConstants.MAX_HISTORY_RECORD_ITEMS);
        }
    }

    /**
     * 异步聊天，即提问并不要求直接得到接口；等后台准备完毕之后再写入对应的结果
     *
     * @param userId
     * @param question
     * @param consumer 执行成功之后，直接异步回调的通知
     * @return
     */
    @Override
    public ChatRecordsVo asyncChat(Integer userId, String question, Consumer<ChatRecordsVo> consumer) {
        ChatRecordsVo res = initResVo(userId, question);
        if (!res.hasQaCnt()) {
            // 次数使用完毕
            consumer.accept(res);
            return res;
        }

        List<String> sensitiveWord = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sensitiveWord)) {
            // 包含敏感词的提问，直接返回异常
            res.getRecords().get(0).initAnswer(String.format(ChatConstants.SENSITIVE_QUESTION, sensitiveWord));
            consumer.accept(res);
        } else {
            final ChatRecordsVo newRes = res.clone();
            AiChatStatEnum needReturn = doAsyncAnswer(userId, newRes, (ans, vo) -> {
                if (ans == AiChatStatEnum.END) {
                    // 只有最后一个会话，即ai的回答结束，才需要进行持久化，并计数
                    processAfterSuccessAnswered(userId, newRes);
                } else if (ans == AiChatStatEnum.ERROR) {
                    // 执行异常，更新AI模型
                    SpringUtil.getBean(ChatFacade.class).refreshAiSourceCache(Sets.newHashSet(source()));
                }
                // ai异步返回结果之后，我们将结果推送给前端用户
                consumer.accept(newRes);
            });

            if (needReturn.needResponse()) {
                // 异步响应时，为了避免长时间的等待，这里直接响应用户的提问，返回一个稍等得提示文案
                ChatItemVo nowItem = res.getRecords().get(0);
                nowItem.initAnswer(ChatConstants.ASYNC_CHAT_TIP);
                consumer.accept(res);
            }
        }
        return res;
    }

    /**
     * 异步返回结果
     *
     * @param userId
     * @param response 保存提问 & 返回的结果，最终会返回给前端用户
     * @param consumer 具体将 response 写回前端的实现策略
     * @return 返回的会话状态，控制是否需要将结果直接返回给前端
     */
    public abstract AiChatStatEnum doAsyncAnswer(Integer userId, ChatRecordsVo response, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer);

    /**
     * 查询当前用户最多可提问的次数
     *
     * @param userId
     * @return
     */
    protected int getMaxQaCnt(Integer userId) {
        return chatItemService.getMaxChatCnt(userId);
    }
}
