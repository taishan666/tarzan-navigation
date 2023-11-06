package com.tarzan.nav.websocket;

import com.tarzan.nav.utils.SpringUtil;
import com.tarzan.nav.utils.StringUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Objects;

/**
 * websocket 工具类
 *
 * @author caichengzhe
 * @version 1.0
 * @company 虎的一比有限责任公司
 * @copyright (c) 2021 Niubility Tiger Co.LTD.All rights reserved.
 * @date 2021年07月08日 17:19:46
 * @since JDK1.8
 */
public class WebSocketUtil {

    private static SimpMessagingTemplate simpMessagingTemplate;

    static {
        simpMessagingTemplate = SpringUtil.getBean(SimpMessagingTemplate.class);
    }


    /**
     * 方法描述: 广播消息
     *
     * @param destination
     * @param data
     * @Return
     * @author caichengzhe
     * @date 2021年07月08日 17:22:20
     */
    public static void broadcast(String destination, Object data) {
        if (StringUtil.isBlank(destination) || Objects.isNull(data)) {
            return;
        }
        simpMessagingTemplate.convertAndSend(destination, data);
    }


    /**
     * 方法描述: 通知特定目标
     *
     * @param principal 目标唯一标识
     * @param destination 订阅地址
     * @param data 数据
     * @Return
     * @author caichengzhe
     * @date 2021年07月08日 17:25:13
     */
    public static void inform(String principal, String destination, Object data) {
        if (StringUtil.isBlank(principal) || Objects.isNull(data)) {
            return;
        }
        simpMessagingTemplate.convertAndSendToUser(principal, destination, data);
    }
}
