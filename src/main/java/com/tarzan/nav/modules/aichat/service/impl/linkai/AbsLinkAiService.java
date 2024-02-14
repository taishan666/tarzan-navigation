package com.tarzan.nav.modules.aichat.service.impl.linkai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public abstract class AbsLinkAiService extends AbsChatAiService {

    @Value("${linkai.apiCode}")
    private String apiCode;
    @Value("${linkai.apiKey}")
    private String apiKey;
    @Value("${linkai.baseUrl}")
    private String baseUrl;
    @Value("${linkai.chatUrl}")
    private String chatUrl;

    public WebClient buildWebClient() {
        return WebClient.builder().baseUrl(baseUrl).defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
    }

    public Flux<ChatRecordsVo> doAsyncAnswer(ChatRecordsVo vo, String model) {
        ChatItemVo item = vo.getRecords().get(0);
        JSONObject params=new JSONObject();
        JSONArray messages=new JSONArray();
        JSONObject message=new JSONObject();
        message.put("role","user");
        message.put("content",item.getQuestion());
        messages.add(message);
        params.put("app_code",apiCode);
        params.put("model",model);
        params.put("messages",messages);
        params.put("stream",true);
        return buildWebClient().post().uri(chatUrl)
                .header("Authorization", "Bearer "+apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(params.toJSONString())
                .retrieve()
                .bodyToFlux(String.class)
                .filter(data-> !"[DONE]".equals(data))
                .map(response -> {
                    JSONObject data = JSON.parseObject(response);
                    JSONArray choices = data.getJSONArray("choices");
                    JSONObject choice=choices.getJSONObject(0);
                    if(!"stop".equals(choice.getString("finish_reason"))){
                        JSONObject delta=choice.getJSONObject("delta");
                        String content=delta.getString("content");
                        item.stramAnswer(content);
                    }
                    return vo;
                });
    }


}