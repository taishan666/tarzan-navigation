package com.tarzan.nav.modules.aichat.enums;

import lombok.Getter;

/**
 * @author tarzan
 * @date 2023/6/9
 */
@Getter
public enum AISourceEnum {

    /**
     * 讯飞
     */
    XUN_FEI_3_5(0,"讯飞星火3.5"),
    TONG_YI_MAX(1, "通义千问MAX"),
    /**
     * 文心一言
     */
    LLAMA_2_7B(2, "qianfan_chinese_llama_2_7b"),
    YI_34B_CHAT(3,"yi_34b_chat"),
    /**
     * link ai
     */
    CHAT_GPT_3_5(4, "gpt-3.5-turbo"),
    CHAT_GPT_3_5_16K(5, "gpt-3.5-turbo-16k"),
    CHAT_GPT_4(6, "gpt-4"),
    XUN_FEI_3(7,"xunfei"),
    WEI_XIN_3_5(8,"wenxin"),
    WEI_XIN_4(9,"wenxin-4"),
    GEMINI(10,"gemini"),
    GLM_4(11,"glm-4");

    private String name;
    private Integer code;

    AISourceEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }


}
