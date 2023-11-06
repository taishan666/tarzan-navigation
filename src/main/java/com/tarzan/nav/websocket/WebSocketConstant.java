package com.tarzan.nav.websocket;

import org.springframework.messaging.support.GenericMessage;

/**
 * websocket 常量类
 *
 * @author caichengzhe
 * @version 1.0
 * @company 虎的一比有限责任公司
 * @copyright (c) 2021 Niubility Tiger Co.LTD.All rights reserved.
 * @date 2021年07月08日 10:21:11
 * @since JDK1.8
 */
public class WebSocketConstant {
    /**
     * token in websocket message header
     */
    public static final String WEBSOCKET_HEADER_TOKEN = "ws_token";
    /**
     * user_id in websocket message header
     */
    public static final String WEBSOCKET_HEADER_USER_ID = "ws_user_id";

    /**
     * 非法请求
     */
    public static final GenericMessage<String> ILLEGAL_REQUEST = new GenericMessage<>("非法请求");


}
