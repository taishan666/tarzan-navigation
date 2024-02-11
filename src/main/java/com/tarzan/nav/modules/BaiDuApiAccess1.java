package com.tarzan.nav.modules;

import com.tarzan.nav.utils.StringUtil;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;

public class BaiDuApiAccess1 {

    public static final String API_KEY = "gHzGfwbSyTMxcKKZKHb5YAL8";
    public static final String SECRET_KEY = "UYj7Q42COR16724LXcL2577Hd1FNEqpH";
    private static final String ACCESS_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id="+API_KEY+"&client_secret="+SECRET_KEY;
    private static final String CHAT_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/eb-instant";


    static String getAccessToken() throws IOException {
        OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY + "&client_secret=" + SECRET_KEY);
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token")
                .method("POST", body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        return new JSONObject(response.body().string()).getString("access_token");
    }
    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        String accessToken = getAccessToken();

        // 构造聊天请求参数
        JSONObject chatPayload = new JSONObject();
        JSONArray messagesArray = new JSONArray();
        JSONObject message = new JSONObject();
        message.put("role", "user");
        message.put("content", "给我推荐一些自驾游路线");
        messagesArray.put(message);
        chatPayload.put("messages", messagesArray);
        chatPayload.put("stream", true);

        // 发送聊天请求
        String chatUrlWithToken = CHAT_URL + "?access_token=" + accessToken;
        MediaType jsonMediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(jsonMediaType, chatPayload.toString());
        Request chatRequest = new Request.Builder()
                .url(chatUrlWithToken)
                .post(requestBody)
                .build();

        Response chatResponse = client.newCall(chatRequest).execute();
        if (!chatResponse.isSuccessful()) {
            throw new IOException("Failed to send chat request: " + chatResponse);
        }

/*        System.out.println("Response Headers:");
        for (Map.Entry<String, List<String>> header : chatResponse.headers().toMultimap().entrySet()) {
            System.out.println(header.getKey() + ": " + String.join(", ", header.getValue()));
        }*/
        Reader reader=chatResponse.body().charStream();
        System.err.println("reader---------------------------------------------------------");
        Scanner scanner = new Scanner(reader);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (StringUtil.isNotBlank(line)){
                System.out.println(line.substring(6));
                // System.out.println(new JSONObject(line.substring()).getJSONObject("data").getString("result"));
                System.err.println("---------------------------------------------------------");
            }
        }
    }
}

// 请确保导入必要的JSON处理库，例如org.json.JSONObject和JSONArray