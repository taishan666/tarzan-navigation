package com.tarzan.nav.modules.aichat.service;

import com.tarzan.nav.modules.aichat.constants.ChatConstants;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author tarzan
 */
@Slf4j
@Service
public abstract class AbsChatAiService implements ChatAiService {

    @Resource
    private ChatItemService chatItemService;

    @Override
    public Flux<ChatRecordsVo> chatStream(Integer userId,String question) {
        ChatRecordsVo res = initResVo(userId, question);
        if (!res.hasQaCnt()) {
            return Flux.just(res);
        }
        List<String> sensitiveWord = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sensitiveWord)) {
            // 包含敏感词的提问，直接返回异常
            res.getRecords().get(0).initAnswer(String.format(ChatConstants.SENSITIVE_QUESTION, sensitiveWord));
            return Flux.just(res);
        } else {
            StringBuffer answer=new StringBuffer();
            return doAsyncAnswer(userId, res).doOnNext(e->answer.append(e.getRecords().get(0).getAnswer())).doOnComplete(() -> {
                res.getRecords().get(0).setAnswer(answer.toString());
                // 当Flux完成时
                processAfterSuccessAnswered(userId,res);
            });
        }
    }


    @Override
    public ChatRecordsVo getChatHistory(Integer userId) {
        List<ChatItemVo> records=new ArrayList<>(51);
        List<ChatItemVo> chats = chatItemService.getChatHistory(source(),userId,50);
        records.addAll(chats);
        records.add(0, new ChatItemVo().initAnswer(String.format("开始你和泰聪明(%s-大模型)的AI之旅吧!", source().getName())));
        ChatRecordsVo vo = new ChatRecordsVo();
        vo.setMaxCnt(getMaxQaCnt(userId));
        vo.setUsedCnt(queryUsedCnt(userId));
        vo.setSource(source());
        vo.setRecords(records);
        return vo;
    }

    /**
     * 异步返回结果
     *
     * @param userId
     * @param response 保存提问 & 返回的结果，最终会返回给前端用户
     * @return 返回的会话状态，控制是否需要将结果直接返回给前端
     */
    public abstract Flux<ChatRecordsVo> doAsyncAnswer(Integer userId, ChatRecordsVo response);

    private ChatRecordsVo initResVo(Integer userId, String question) {
        ChatRecordsVo res = new ChatRecordsVo();
        res.setSource(source());
        int maxCnt = getMaxQaCnt(userId);
        int usedCnt = queryUsedCnt(userId);
        res.setMaxCnt(maxCnt);
        res.setUsedCnt(usedCnt);

        ChatItemVo item = new ChatItemVo().initQuestion(question);
        if (!res.hasQaCnt()) {
            //次数已经使用完毕
            item.initAnswer(ChatConstants.TOKEN_OVER);
        }
        res.setRecords(Arrays.asList(item));
        return res;
    }

    /**
     * 成功返回之后的后置操作
     *
     * @param userId
     * @param response
     */
    protected void processAfterSuccessAnswered(Integer userId, ChatRecordsVo response) {
        // 回答成功，保存聊天记录，剩余次数-1
        response.setUsedCnt(response.getUsedCnt()+1);
        recordChatItem(userId, response.getRecords().get(0));
        if (response.getUsedCnt() > ChatConstants.MAX_HISTORY_RECORD_ITEMS) {
            // 最多保存五百条历史聊天记录
            chatItemService.lTrim(source(),userId,ChatConstants.MAX_HISTORY_RECORD_ITEMS);
        }
    }

    /**
     * 保存聊天记录
     */
    protected void recordChatItem(Integer userId, ChatItemVo item) {
        chatItemService.pushChatItem(source(), userId, item);
    }

    /**
     * 查询当前用户最多可提问的次数
     *
     * @param userId
     * @return
     */
    protected int getMaxQaCnt(Integer userId) {
        return 100;
    }

    /**
     * 查询已经使用的次数
     *
     * @param userId
     * @return
     */
    protected int queryUsedCnt(Integer userId) {
        return chatItemService.queryUsedCnt(source(),userId);
    }

}
