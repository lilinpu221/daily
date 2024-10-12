package com.linzi.daily.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

public class OkHttpUtil {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getHttpClient() {
        if (okHttpClient == null) {
            ConnectionPool connectionPool = new ConnectionPool(32, 1, TimeUnit.MINUTES);
            okHttpClient = new OkHttpClient().newBuilder()
                    .connectionPool(connectionPool)
                    .connectTimeout(3, TimeUnit.SECONDS)
                    .writeTimeout(5, TimeUnit.SECONDS)
                    .readTimeout(5, TimeUnit.SECONDS).build();
        }
        return okHttpClient;
    }

    public static void syncGet(String url,String param) {
        String callUrl = url+"?"+param;
        final Request request = new Request.Builder()
                .url(callUrl)
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = getHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call arg0, @NotNull Response arg1) {
            }

            @Override
            public void onFailure(@NotNull Call arg0, @NotNull IOException arg1) {
            }
        });
    }

    public static Response asyncGet(String url,String param) throws IOException {
        String callUrl;
        if(CharSequenceUtil.isBlank(param)){
            callUrl = url;
        }else{
            callUrl = url+"?"+param;
        }
        final Request request = new Request.Builder()
                .url(callUrl)
                .build();
        final Call call = getHttpClient().newCall(request);
        return call.execute();
    }

    public static Response asyncGet(String url,String param,Map<String,String> header) throws IOException {
        Request.Builder builder = new Request.Builder();
        header.forEach(builder::addHeader);
        String callUrl;
        if(CharSequenceUtil.isBlank(param)){
            callUrl = url;
        }else{
            callUrl = url+"?"+param;
        }
        Request request = builder.url(callUrl).build();
        return getHttpClient().newCall(request).execute();
    }

    public static Response formPost(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return getHttpClient().newCall(request).execute();
    }

    public static Response multiPost(String url, RequestBody requestBody) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return getHttpClient().newCall(request).execute();
    }

    public static Response formJson(String url, RequestBody requestBody, Map<String,String> header) throws IOException {
        Request.Builder builder = new Request.Builder();
        if(header!=null){
            header.forEach(builder::addHeader);
        }
        Request request = builder.url(url).post(requestBody).build();
        return getHttpClient().newCall(request).execute();
    }

    public static OkHttpClient buildBasicAuthClient(final String name, final String password) {
        return new OkHttpClient.Builder().authenticator((route, response) -> {
            String credential = Credentials.basic(name, password);
            return response.request().newBuilder().header("Authorization", credential).build();
        }).build();
    }

}
