package com.tarzan.nav.modules.aichat.service.impl.wenxin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatAnswerVo;
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
public abstract class AbsWenXinAiService extends AbsChatAiService {

    @Value("${wenxin.apiKey}")
    private String apiKey;
    @Value("${wenxin.apiSecret}")
    private String apiSecret;
    @Value("${wenxin.baseUrl}")
    private String baseUrl;
    @Value("${wenxin.chatUrl}")
    private String chatUrl;

    public WebClient buildWebClient() {
        return WebClient.builder().baseUrl(baseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }
    @Override
    public Flux<ChatAnswerVo> doAsyncAnswer(String question, ChatAnswerVo response) {
        String model=source().getName();
        JSONObject chatPayload = new JSONObject();
        JSONArray messagesArray = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", question);
        messagesArray.add(message);
        chatPayload.put("messages", messagesArray);
        chatPayload.put("stream", true);
        return buildWebClient().post().uri(chatUrl+"/"+model+"?access_token=" + getAccessToken()).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chatPayload.toString())
                .retrieve()
                .bodyToFlux(String.class)
                .map(data -> {
                    String result = JSON.parseObject(data).getString("result");
                    response.setAnswer(result);
                    return response;
                });
    }

    public String getAccessToken() {
        // 构建请求体
        String requestBody = "grant_type=client_credentials"
                + "&client_id=" + apiKey
                + "&client_secret=" + apiSecret;
        return buildWebClient().post().uri("/oauth/2.0/token").bodyValue(requestBody).retrieve()
                // 假设服务器返回的是 JSON 格式的字符串
                .bodyToMono(String.class)
                .map(data ->
                        JSON.parseObject(data).getString("access_token")
                ).block();
    }

}
