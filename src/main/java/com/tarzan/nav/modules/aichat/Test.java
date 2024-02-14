package com.tarzan.nav.modules.aichat;

import okhttp3.*;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n \"model\": \"xunfei\",\n \"stream\": true,\n \"app_code\": \"3ab3Ei5H\",\n  \"messages\": [\n    {\n      \"role\": \"user\",\n      \"content\": \"用java写一个hello world 小程序\"\n    }\n  ]\n}");
        Request request = new Request.Builder()
                .url("https://api.link-ai.chat/v1/chat/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer Link_OdSp0s7vitGnvkWigmTcfyUg5diyDvTIg1frolmt3Z")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
