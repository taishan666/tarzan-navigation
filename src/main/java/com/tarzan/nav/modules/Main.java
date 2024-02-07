package com.tarzan.nav.modules;

// Copyright (c) Alibaba, Inc. and its affiliates.

import com.alibaba.dashscope.aigc.conversation.Conversation;
import com.alibaba.dashscope.aigc.conversation.ConversationParam;
import com.alibaba.dashscope.aigc.conversation.ConversationResult;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;

/**
 * @author Lenovo
 */
public class Main {
    public static void quickStart() throws ApiException, NoApiKeyException, InputRequiredException {
        Conversation conversation = new Conversation();
        String prompt = "用萝卜、土豆、茄子做饭，给我个菜谱。";
        ConversationParam param = ConversationParam
                .builder()
                .model(Conversation.Models.QWEN_MAX)
                .prompt(prompt)
                .apiKey("sk-80611638022146a2bc9a1ec9b566cc54")
                .build();
        ConversationResult result = conversation.call(param);
        System.out.println(result.getOutput().getText());
    }
    public static void main(String[] args) {
        try {
            quickStart();
        } catch (ApiException | NoApiKeyException e) {
            System.out.println(e.getMessage());
        } catch (InputRequiredException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

}