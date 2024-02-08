package com.tarzan.nav.modules.aichat.service.impl.xunfei;

import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.enums.ChatAnswerTypeEnum;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import com.tarzan.nav.utils.SpringUtil;
import com.tarzan.nav.websocket.WsConnectStateEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import javax.validation.constraints.NotNull;
import java.util.function.BiConsumer;

/**
 * @author tarzan
 */
@Getter
@Setter
@Slf4j
public class XunFeiMsgListener extends WebSocketListener {

    private volatile WsConnectStateEnum connectState;

    private String user;

    private ChatRecordsVo chatRecord;

    private BiConsumer<AiChatStatEnum, ChatRecordsVo> callback;

    private XunFeiIntegration xunFeiIntegration;

    public XunFeiMsgListener(String user, ChatRecordsVo chatRecord, BiConsumer<AiChatStatEnum, ChatRecordsVo> callback) {
        this.connectState = WsConnectStateEnum.INIT;
        this.user = user;
        this.chatRecord = chatRecord;
        this.callback = callback;
        this.xunFeiIntegration= SpringUtil.getBean(XunFeiIntegration.class);
    }

    /**
     * 方法描述: 重写onopen
     *
     * @param webSocket
     * @param response
     * @return
     * @throws
     * @author tarzan
     * @date 2024年02月08日 10:27:41
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        super.onOpen(webSocket, response);
        connectState = WsConnectStateEnum.CONNECTED;

        // 连接成功之后，发送消息
        webSocket.send(xunFeiIntegration.buildSendMsg(user, chatRecord.getRecords().get(0).getQuestion()));
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        ChatItemVo item = chatRecord.getRecords().get(0);
        XunFeiIntegration.ResponseData responseData = xunFeiIntegration.parse2response(text);
        if (responseData.successReturn()) {
            // 成功获取到结果
            StringBuilder msg = new StringBuilder();
            XunFeiIntegration.Payload pl = responseData.getPayload();
            pl.getChoices().getText().forEach(s -> {
                msg.append(s.getContent());
            });
            item.appendAnswer(msg.toString());

            if (responseData.firstResonse()) {
                callback.accept(AiChatStatEnum.FIRST, chatRecord);
            } else if (responseData.endResponse()) {
                // 标记流式回答已完成
                item.setAnswerType(ChatAnswerTypeEnum.STREAM_END);
                // 最后一次返回结果时，打印一下剩余的tokens
                XunFeiIntegration.UsageText tokens = pl.getUsage().getText();
                log.info("使用tokens:\n" + tokens);
                webSocket.close(1001, "会话结束");
                callback.accept(AiChatStatEnum.END, chatRecord);
            } else {
                callback.accept(AiChatStatEnum.MID, chatRecord);
            }
        } else {
            item.initAnswer("AI返回异常:" + responseData.getHeader());
            callback.accept(AiChatStatEnum.ERROR, chatRecord);
            webSocket.close(responseData.getHeader().getCode(), responseData.getHeader().getMessage());
        }
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        log.warn("websocket 连接失败! {}", response, t);
        connectState = WsConnectStateEnum.FAILED;
        chatRecord.getRecords().get(0).initAnswer("讯飞AI连接失败了!" + t.getMessage());
        callback.accept(AiChatStatEnum.ERROR, chatRecord);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        if (log.isDebugEnabled()) {
            log.debug("连接中断! code={}, reason={}", code, reason);
        }
        connectState = WsConnectStateEnum.CLOSED;
    }
}
