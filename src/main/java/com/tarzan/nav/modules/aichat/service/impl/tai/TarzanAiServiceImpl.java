package com.tarzan.nav.modules.aichat.service.impl.tai;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatAiService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * 泰聪明价值一个亿的AI
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Service
@Slf4j
public class TarzanAiServiceImpl extends AbsChatAiService {

    private final WebClient webClient= WebClient.builder().baseUrl("https://api.link-ai.chat").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();

    @Override
    public AISourceEnum source() {
        return AISourceEnum.TARZAN_AI;
    }


    @Override
    public Flux<ChatRecordsVo> doAsyncAnswer(ChatRecordsVo vo) {
        ChatItemVo item = vo.getRecords().get(0);
        JSONObject params=new JSONObject();
        JSONArray messages=new JSONArray();
        JSONObject message=new JSONObject();
        message.put("role","user");
        message.put("content",item.getQuestion());
        messages.add(message);
        params.put("app_code","3ab3Ei5H");
        params.put("messages",messages);
        params.put("stream",true);
        return webClient.post().uri("/v1/chat/completions")
                .header("Authorization", "Bearer Link_OdSp0s7vitGnvkWigmTcfyUg5diyDvTIg1frolmt3Z")
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


    private String qa(String q) {
        String ans = "洛阳泰山技术博客 https://tarzan.blog.csdn.net/";
        return ans;
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 10;
    }
}
