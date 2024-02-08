package com.tarzan.nav.modules.aichat.service.impl.pai;

import com.tarzan.nav.modules.aichat.enums.AISourceEnum;
import com.tarzan.nav.modules.aichat.enums.AiChatStatEnum;
import com.tarzan.nav.modules.aichat.service.AbsChatService;
import com.tarzan.nav.modules.aichat.vo.ChatItemVo;
import com.tarzan.nav.modules.aichat.vo.ChatRecordsVo;
import com.tarzan.nav.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Scanner;
import java.util.function.BiConsumer;

/**
 * Yi-34B-Chat 是一款由零一万物（或通过猎户星空的合作）开发的大型语言模型，它是基于 Yi-34B 基座模型微调后专为对话和聊天场景优化的版本。
 *
 * @author tarzan
 * @date 2023/6/9
 */
@Service
@Slf4j
public class YiAiServiceImpl extends AbsChatService {

    @Value("${yi.hostUrl}")
    private String hostUrl;
    @Value("${yi.apiKey}")
    private String apiKey;
    @Value("${yi.apiSecret}")
    private String apiSecret;

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    @Override
    public AISourceEnum source() {
        return AISourceEnum.YI_AI;
    }

    @Override
    public AiChatStatEnum doAnswer(Integer userId, ChatItemVo chat) {
        return AiChatStatEnum.IGNORE;
    }

    @Override
    public AiChatStatEnum doAsyncAnswer(Integer userId, ChatRecordsVo response, BiConsumer<AiChatStatEnum, ChatRecordsVo> consumer) {
        ChatItemVo item = response.getRecords().get(0);
        String accessToken = getAccessToken();
        // 构造聊天请求参数
        JSONObject chatPayload = new JSONObject();
        JSONArray messagesArray = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", item.getQuestion());
        messagesArray.put(message);
        chatPayload.put("messages", messagesArray);
        chatPayload.put("stream", true);

        // 发送聊天请求
        String chatUrlWithToken = hostUrl + "?access_token=" + accessToken;
        MediaType jsonMediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonMediaType, chatPayload.toString());
        Request chatRequest = new Request.Builder()
                .url(chatUrlWithToken)
                .post(requestBody)
                .build();

        Response chatResponse = null;
        try {
            chatResponse = HTTP_CLIENT.newCall(chatRequest).execute();
        } catch (IOException e) {
            item.initAnswer("AI返回异常:" + chatResponse);
            consumer.accept(AiChatStatEnum.ERROR, response);
        }
        if (chatResponse.isSuccessful()) {
            Scanner scanner = new Scanner(chatResponse.body().charStream());
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (StringUtil.isNotBlank(line)) {
                    System.out.println(line.substring(6));
                    JSONObject data = new JSONObject(line.substring(6));
                    item.appendAnswer(data.getString("result"));
                    if(data.getBoolean("is_end")){
                        consumer.accept(AiChatStatEnum.END, response);
                    }else {
                        consumer.accept(AiChatStatEnum.FIRST, response);
                    }
                }
            }
        } else {
            item.initAnswer("AI返回异常:" + chatResponse);
            consumer.accept(AiChatStatEnum.ERROR, response);
        }
        return AiChatStatEnum.IGNORE;
    }

    @Override
    protected int getMaxQaCnt(Integer userId) {
        return 50;
    }

    private String getAccessToken() {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response;
        try {
            response = HTTP_CLIENT.newCall(request).execute();
            return new JSONObject(response.body().string()).getString("access_token");
        } catch (IOException e) {
            log.error("AccessToken获取失败", e);
            return null;
        }
    }
}
