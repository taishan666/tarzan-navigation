package com.tarzan.nav.modules.aichat.service.impl.xunfei;

import com.alibaba.fastjson.JSON;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * @author tarzan
 */
@Service
public class XunFeiChatAiServiceImpl extends AbsChatAiService {

    private static final WebClient WEB_CLIENT = WebClient.builder().baseUrl("https://aip.baidubce.com").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE).build();


    @Value("${yiyan.apiKey}")
    private String apiKey;
    @Value("${yiyan.apiSecret}")
    private String apiSecret;

    @Override
    public AISourceEnum source() {
        return AISourceEnum.XUN_FEI_AI;
    }

    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(Integer userId, ChatRecordsVo response) {
        ChatItemVo item = response.getRecords().get(0);
        JSONObject chatPayload = new JSONObject();
        JSONArray messagesArray = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", item.getQuestion());
        messagesArray.put(message);
        chatPayload.put("messages", messagesArray);
        chatPayload.put("stream", true);
        StringBuffer answer=new StringBuffer();
        return WEB_CLIENT.post().uri("rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + getAccessToken()).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chatPayload.toString())
                .retrieve()
                .bodyToFlux(String.class)
                //  .filter(data-> StringUtil.isNotBlank(data))
                .map(data -> {
                    String result = JSON.parseObject(data).getString("result");
                    answer.append(result);
                    item.stramAnswer(result);
                    return response;
                });
    }

    public String getAccessToken() {
        // 构建请求体
        String requestBody = "grant_type=client_credentials"
                + "&client_id=" + apiKey
                + "&client_secret=" + apiSecret;
        return WEB_CLIENT.post().uri("/oauth/2.0/token").bodyValue(requestBody).retrieve()
                // 假设服务器返回的是 JSON 格式的字符串
                .bodyToMono(String.class)
                .map(data ->
                        JSON.parseObject(data).getString("access_token")
                ).block();
    }


}
