package com.tarzan.nav.modules.aichat;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * @author tarzan
 */
@RestController
@RequestMapping
@Slf4j
public class FluxController {

    public static final String API_KEY = "gHzGfwbSyTMxcKKZKHb5YAL8";
    public static final String SECRET_KEY = "UYj7Q42COR16724LXcL2577Hd1FNEqpH";
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY;
    private static final String CHAT_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant";


    @PostMapping(path = "/event-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> helloWorld() throws IOException {
      /*  return Flux.fromArray("hello world".split(" "))
                // 每个元素延迟500毫秒，以模拟逐字效果
                .delayElements(Duration.ofMillis(500));*/
        // 构造聊天请求参数
        JSONObject chatPayload = new JSONObject();
        JSONArray messagesArray = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", "为什么现在的农村出来的大学生，混的都不好？");
        messagesArray.put(message);
        chatPayload.put("messages", messagesArray);
        chatPayload.put("stream", true);
        StringBuffer answer=new StringBuffer();
        return WEB_CLIENT.post().uri("rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant?access_token=" + getAccessToken()).contentType(MediaType.APPLICATION_JSON)
                .bodyValue(chatPayload.toString())
                .retrieve()
                .bodyToFlux(String.class)
                // 可能需要其他流处理，比如map、filter等
                .map(data -> {
                    String result=JSON.parseObject(data).getString("result");
                    answer.append(result);
                    return result;
                }).doOnComplete(() -> {
                    // 当Flux完成时，输出结束消息
                    System.out.println("处理完毕，流已关闭。");
                    System.out.println(answer);
                });
    }

    private static final WebClient WEB_CLIENT = WebClient.builder().baseUrl("https://aip.baidubce.com").defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_STREAM_JSON_VALUE).build();

    static String getAccessToken() {
        // 构建请求体
        String requestBody = "grant_type=client_credentials"
                + "&client_id=" + API_KEY
                + "&client_secret=" + SECRET_KEY;
        return WEB_CLIENT.post().uri("/oauth/2.0/token").bodyValue(requestBody).retrieve()
                // 假设服务器返回的是 JSON 格式的字符串
                .bodyToMono(String.class)
                .map(data ->
                        JSON.parseObject(data).getString("access_token")
                ).block();
    }
}
