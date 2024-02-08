package com.tarzan.nav.modules.aichat.service.impl.xunfei;

import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.util.function.BiConsumer;

/**
 * 一个简单的ws装饰器，用于包装一下讯飞长连接的交互情况
 * 比较蛋疼的是讯飞建立连接60s没有返回主动断开，问了一次返回结果之后也主动断开，下次需要重连
 * @author TARZAN
 */
@Data
public class XunFeiChatWrapper {
    private OkHttpClient client;
    private WebSocket webSocket;
    private Request request;

    private BiConsumer<WebSocket, String> onMsg;

    private XunFeiMsgListener listener;

    private ChatItemVo item;

    public XunFeiChatWrapper(XunFeiIntegration xunFeiIntegration,String uid, ChatRecordsVo chatRes, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        client = xunFeiIntegration.getOkHttpClient();
        String url = xunFeiIntegration.buildXunFeiUrl();
        request = new Request.Builder().url(url).build();
        listener = new XunFeiMsgListener(uid, chatRes, consumer);
    }

    /**
     * 首次使用时，开启提问
     */
    public void initAndQuestion() {
        webSocket = client.newWebSocket(request, listener);
    }

    /**
     * 追加的提问, 主要是为了复用websocket的构造参数
     */
    public void appendQuestion(String uid, ChatRecordsVo chatRes, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        listener = new XunFeiMsgListener(uid, chatRes, consumer);
        webSocket = client.newWebSocket(request, listener);
    }

}
