package com.tarzan.nav.modules.aichat.service.impl.xunfei;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.validation.constraints.NotNull;

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

    private XunFeiIntegration xunFeiIntegration;

    private  FluxSink<ChatRecordsVo> messageSink;
    private  Flux<ChatRecordsVo> messageFlux;

    public XunFeiMsgListener(String user, ChatRecordsVo chatRecord) {
        this.connectState = WsConnectStateEnum.INIT;
        this.user = user;
        this.chatRecord = chatRecord;
        this.xunFeiIntegration= SpringUtil.getBean(XunFeiIntegration.class);
        Flux<ChatRecordsVo> publisher = Flux.create(sink -> {
            this.messageSink = sink;
        });
        this.messageFlux = publisher.publish().autoConnect();
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
            item.stramAnswer(msg.toString());
            messageSink.next(chatRecord);
            if (responseData.endResponse()) {
                webSocket.close(1001, "会话结束");
                messageSink.complete();
            }
        } else {
            item.initAnswer("AI返回异常:" + responseData.getHeader());
            messageSink.next(chatRecord);
            webSocket.close(responseData.getHeader().getCode(), responseData.getHeader().getMessage());
        }
    }


    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        log.warn("websocket 连接失败! {}", response, t);
        connectState = WsConnectStateEnum.FAILED;
        chatRecord.getRecords().get(0).initAnswer("讯飞AI连接失败了!" + t.getMessage());
        messageSink.next(chatRecord);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        if (log.isDebugEnabled()) {
            log.debug("连接中断! code={}, reason={}", code, reason);
        }
        connectState = WsConnectStateEnum.CLOSED;
    }

    public Flux<ChatRecordsVo> getMessageFlux() {
        return messageFlux;
    }
}
