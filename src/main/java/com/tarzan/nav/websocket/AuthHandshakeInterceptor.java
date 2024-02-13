package com.tarzan.nav.websocket;

import com.tarzan.nav.common.constant.CoreConst;
import com.tarzan.nav.utils.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * 握手拦截器, 用于身份验证识别
 *
 * @author tarzan
 * @date 2023/6/8
 */
@Slf4j
public class AuthHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    /**
     * 握手前，进行用户身份校验识别
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes: 即对应的是Message中的 simpSessionAttributes 请求头
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("准备开始握手了!");
        AuthUtil.ReqInfo reqInfo = new AuthUtil.ReqInfo();
        reqInfo.setUserId(AuthUtil.getUserId());
        AuthUtil.addReqInfo(reqInfo);
        if (AuthUtil.getUser() == null) {
            log.info("websocket 握手失败，请登录之后再试");
            return false;
        }
        attributes.put(CoreConst.SESSION_KEY,reqInfo);
        return true;
    }

    private String initAiSource(String path) {
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        log.info("握手成功了!!!");
        super.afterHandshake(request, response, wsHandler, ex);
    }

}
