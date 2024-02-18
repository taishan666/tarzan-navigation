package com.tarzan.nav.modules.aichat.service.impl.xunfei;

import com.tarzan.nav.modules.aichat.vo.ChatAnswerVo;
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

    private ChatAnswerVo chatAnswer;

    private XunFeiIntegration xunFeiIntegration;

    private String question;

    private  FluxSink<ChatAnswerVo> messageSink;
    private  Flux<ChatAnswerVo> messageFlux;

    public XunFeiMsgListener(String question,ChatAnswerVo chatAnswer) {
        this.connectState = WsConnectStateEnum.INIT;
        this.chatAnswer = chatAnswer;
        this.question=question;
        this.xunFeiIntegration= SpringUtil.getBean(XunFeiIntegration.class);
        Flux<ChatAnswerVo> publisher = Flux.create(sink -> {
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
        webSocket.send(xunFeiIntegration.buildSendMsg(question));
    }

    @Override
    public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
        super.onMessage(webSocket, text);
        XunFeiIntegration.ResponseData responseData = xunFeiIntegration.parse2response(text);
        if (responseData.successReturn()) {
            // 成功获取到结果
            StringBuilder msg = new StringBuilder();
            XunFeiIntegration.Payload pl = responseData.getPayload();
            pl.getChoices().getText().forEach(s -> {
                msg.append(s.getContent());
            });
            chatAnswer.setAnswer(msg.toString());
            messageSink.next(chatAnswer);
            if (responseData.endResponse()) {
                webSocket.close(1001, "会话结束");
                chatAnswer.setUsedCnt(chatAnswer.getUsedCnt() + 1);
                messageSink.complete();
            }
        } else {
            chatAnswer.setAnswer("AI返回异常:" + responseData.getHeader());
            messageSink.next(chatAnswer);
            webSocket.close(responseData.getHeader().getCode(), responseData.getHeader().getMessage());
        }
    }


    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        log.warn("websocket 连接失败! {}", response, t);
        connectState = WsConnectStateEnum.FAILED;
        chatAnswer.setAnswer("讯飞AI连接失败了!" + t.getMessage());
        messageSink.next(chatAnswer);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        super.onClosed(webSocket, code, reason);
        if (log.isDebugEnabled()) {
            log.debug("连接中断! code={}, reason={}", code, reason);
        }
        connectState = WsConnectStateEnum.CLOSED;
    }

    public Flux<ChatAnswerVo> getMessageFlux() {
        return messageFlux;
    }
}
