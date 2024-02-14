package com.tarzan.nav.modules.aichat.constants;


/**
 * @author tarzan
 * @date 2023/6/2
 */
public final class ChatConstants {


    /**
     * 每个用户的最大使用次数
     */
    public static final int MAX_CHATGPT_QAS_CNT = 10;

    /**
     * 最多保存的历史聊天记录
     */
    public static final int MAX_HISTORY_RECORD_ITEMS = 500;

    /**
     * 两次提问的间隔时间，要求20s
     */
    public static final long QAS_TIME_INTERVAL = 20_000;


    public static final String CHAT_REPLY_RECOMMEND = "请注册泰聪明之后再来体验吧，我的技术博客： \n https://tarzan.blog.csdn.net";
    public static final String CHAT_REPLY_BEGIN = "让我们开始体验ChatGPT的魅力吧~";
    public static final String CHAT_REPLY_OVER = "体验结束，让我们下次再见吧~";
    public static final String CHAT_REPLY_CNT_OVER = "次数使用完了哦，勾搭一下博主，多申请点使用次数吧~\n微信：vxhqqh";


    public static final String CHAT_REPLY_TIME_WAITING = "chatgpt还在努力回答中，请等待几秒之后再问一次吧....";
    public static final String CHAT_REPLY_QAS_TOO_FAST = "提问太频繁了，喝一杯咖啡，暂缓一下...";


    public static final String TOKEN_OVER = "您的免费次数已经使用完毕了!";

    /**
     * 异步聊天时返回得提示文案
     */
    public static final String ASYNC_CHAT_TIP = "泰聪明正在努力回答中, 耐心等待一下吧...";


    public static final String SENSITIVE_QUESTION = "提问中包含敏感词:%s，请微信联系我「vxhqqh」加入白名单!";
}
