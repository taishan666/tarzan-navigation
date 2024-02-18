package com.tarzan.nav.modules.aichat.service;

import com.tarzan.nav.modules.aichat.constants.ChatConstants;
import com.tarzan.nav.modules.aichat.vo.ChatAnswerVo;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import com.tarzan.nav.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author tarzan
 */
@Slf4j
@Service
public abstract class AbsChatAiService implements ChatAiService {

    @Resource
    private ChatItemService chatItemService;

    @Override
    public Flux<ChatAnswerVo> chatStream(Integer userId, String question) {
        ChatAnswerVo answerVo=initAnswerVo(userId);
        if (!answerVo.hasQaCnt()) {
            return Flux.just(answerVo);
        }
        List<String> sensitiveWord = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sensitiveWord)) {
            // 包含敏感词的提问，直接返回异常
            answerVo.setAnswer(String.format(ChatConstants.SENSITIVE_QUESTION, sensitiveWord));
            return Flux.just(answerVo);
        } else {
            StringBuffer fullAnswer=new StringBuffer();
            answerVo.setAnswer("请稍候，正在处理您的请求...");
            Flux<ChatAnswerVo> waitMessageFlux = Flux.just(answerVo);
            Flux<ChatAnswerVo> responseFlux=doAsyncAnswer(question,answerVo)
                    .timeout(Duration.ofSeconds(15))
                    .doOnNext(e -> fullAnswer.append(e.getAnswer()))
                    .doOnComplete(() -> {
                        // 当Flux完成时
                        ChatRecordsVo res = initResVo(userId, question,answerVo,fullAnswer.toString());
                        processAfterSuccessAnswered(userId, res);
                    })
                   .onErrorResume(e -> {
                          answerVo.setAnswer("抱歉，服务器暂时无法响应，请稍后再试...");
                        return Flux.just(answerVo);
                    });
          return  waitMessageFlux.concatWith(responseFlux);

        }
    }

    public Flux<ChatAnswerVo> chatStream1(Integer userId, String question) {
        ChatAnswerVo answerVo=initAnswerVo(userId);
        if (!answerVo.hasQaCnt()) {
            return Flux.just(answerVo);
        }
        List<String> sensitiveWord = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sensitiveWord)) {
            // 包含敏感词的提问，直接返回异常
            answerVo.setAnswer(String.format(ChatConstants.SENSITIVE_QUESTION, sensitiveWord));
            return Flux.just(answerVo);
        } else {
            StringBuffer fullAnswer=new StringBuffer();
            answerVo.setAnswer("请稍候，正在处理您的请求...");
            Flux<ChatAnswerVo> waitMessageFlux = Flux.just(answerVo);
            Flux<ChatAnswerVo> responseFlux=doAsyncAnswer(question,answerVo)
                    .timeout(Duration.ofSeconds(15))
                    .doOnNext(e -> fullAnswer.append(e.getAnswer()))
                    /*                .map(data -> {
                                        String result = data.getAnswer();
                                        // 将返回的结果逐字返回
                                        return Stream.of(result.split(""))
                                                .map(CharSequence::toString)
                                                .collect(Collectors.toList());
                                    }).flatMapIterable(Function.identity()).map(e->{
                                        answerVo.setAnswer(e);
                                        return answerVo;
                                    }).delayElements(Duration.ofMillis(100))*/
                    .doOnComplete(() -> {
                        // 当Flux完成时
                        ChatRecordsVo res = initResVo(userId, question,answerVo,fullAnswer.toString());
                        processAfterSuccessAnswered(userId, res);

                    })
                    .onErrorResume(e -> {
                        answerVo.setAnswer("抱歉，服务器暂时无法响应，请稍后再试...");
                        return Flux.just(answerVo);
                    });
            return  waitMessageFlux.concatWith(responseFlux);

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
        vo.setRecords(records);
        return vo;
    }

    /**
     * 异步返回结果
     * @param question 问题
     * @param response 保存提问 & 返回的结果，最终会返回给前端用户
     * @return 返回的会话状态，控制是否需要将结果直接返回给前端
     */
    public abstract Flux<ChatAnswerVo> doAsyncAnswer(String question,ChatAnswerVo response);

    private ChatAnswerVo initAnswerVo(Integer userId) {
        ChatAnswerVo res = new ChatAnswerVo();
        int maxCnt = getMaxQaCnt(userId);
        int usedCnt = queryUsedCnt(userId);
        res.setMaxCnt(maxCnt);
        res.setUsedCnt(usedCnt);
        res.setChatUid(UUID.randomUUID().toString().replaceAll("-", ""));
        res.setAnswerTime(DateUtil.now());
        if (!res.hasQaCnt()) {
            //次数已经使用完毕
            res.setAnswer(ChatConstants.TOKEN_OVER);
        }
        return res;
    }

    private ChatRecordsVo initResVo(Integer userId, String question, ChatAnswerVo answerVo,String fullAnswer) {
        ChatRecordsVo res = new ChatRecordsVo();
        res.setMaxCnt(answerVo.getMaxCnt());
        res.setUsedCnt(answerVo.getUsedCnt());
        ChatItemVo item = new ChatItemVo().initQuestion(question);
        item.setChatUid(answerVo.getChatUid());
        item.setUserId(userId);
        item.setAnswer(fullAnswer);
        item.setAnswerTime(answerVo.getAnswerTime());
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
        return ChatConstants.MAX_CHATGPT_QAS_CNT;
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
