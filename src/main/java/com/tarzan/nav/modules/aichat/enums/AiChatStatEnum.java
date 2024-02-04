package com.tarzan.nav.modules.aichat.enums;

/**
 * @author YiHui
 * @date 2023/6/15
 */
public enum AiChatStatEnum {
    IGNORE(-2) {
        @Override
        public boolean needResponse() {
            return false;
        }
    },
    /**
     * 会话异常
     */
    ERROR(-1),
    /**
     * 一次问答中，第一次返回
     */
    FIRST(0),
    /**
     * 一次问答中，中间的返回
     */
    MID(1),
    /**
     * 一次问答中，最后一次的回复
     */
    END(2),
    ;

    private int state;

    AiChatStatEnum(int state) {
        this.state = state;
    }

    public boolean needResponse() {
        return true;
    }
}
