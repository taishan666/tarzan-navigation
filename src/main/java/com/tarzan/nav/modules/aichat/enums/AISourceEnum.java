package com.tarzan.nav.modules.aichat.enums;

import lombok.Getter;

/**
 * @author tarzan
 * @date 2023/6/9
 */
@Getter
public enum AISourceEnum {
    /**
     * chatgpt 3.5
     */
    CHAT_GPT_3_5(0, "chatGpt3.5"),
    /**
     * chatgpt 4
     */
    CHAT_GPT_4(1, "chatGpt4"),
    /**
     * 泰聪明的模拟AI
     */
    TARZAN_AI(2, "泰聪明"),
    /**
     * 讯飞
     */
    XUN_FEI_AI(3,"讯飞星火"),
    /**
     * 通义千问
     */
    TONG_YI_AI(4, "通义千问"),
    YI_YAN_AI(5, "文心一言"),
    YI_AI(6,"Yi-34B-Chat");

    private String name;
    private Integer code;

    AISourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 是否支持同步
     *
     * @return
     */
    public boolean syncSupport() {
        return true;
    }

    /**
     * 是否支持异步
     *
     * @return
     */
    public boolean asyncSupport() {
        return true;
    }
}
