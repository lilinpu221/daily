package com.linzi.daily.ocr.paddle;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
public class RapidOcrClient {

    private OkHttpClient httpClient;

    private static final String URL = "http://192.168.80.103:9003/ocr";

    @PostConstruct
    private void initHttpClient() {
        httpClient = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(8, 3, TimeUnit.MINUTES))
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
    }

    public String ocr(File file) throws IOException {
        MediaType mediaType = MediaType.Companion.parse("image/png");
        RequestBody fileBody  = RequestBody.Companion.create(file, mediaType);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_file", "image", fileBody).build();
        Request request = new Request.Builder()
                .url(URL)
                .post(requestBody)
                .build();
        Response response = httpClient.newCall(request).execute();
        if(!response.isSuccessful()){
            return CharSequenceUtil.EMPTY;
        }
        assert response.body() != null;
        return response.body().string();
    }
}
