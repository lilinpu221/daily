package com.linzi.daily.mqttapi;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class UrovoOpenApi {
    private static final String APP_URL = "https://speaker.urovo.com/iot/openapi/v1";
    /**
     * 授权码
     */
    private static final String APP_KEY = "U8tHgEkNQYVhf4SB";
    /**
     * 授权密钥(请填写真实的密钥)
     */
    private static final String APP_SECRET = "hOz2ofSovG48Ldu6QDYLcAGogIeIhDI7";

    /**
     * 单例OkHttpClient
     * 请根据具体场景调整ConnectionPool和timeOut参数
     */
    private static volatile OkHttpClient INSTANCE;

    static{
        if (INSTANCE == null) {
            synchronized (UrovoOpenApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkHttpClient().newBuilder()
                            .connectionPool(new ConnectionPool(8, 3, TimeUnit.MINUTES))
                            .connectTimeout(3, TimeUnit.SECONDS)
                            .writeTimeout(3, TimeUnit.SECONDS)
                            .readTimeout(10, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType URLENCODED
            = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

    private static String formPost(Map<String, Object> paramMap, Map<String, String> header) throws  IOException {
        String postData = getFormMapString(paramMap);
        RequestBody requestBody = RequestBody.create(URLENCODED, postData);
        Request request = new Request.Builder()
                .url(APP_URL)
                .post(requestBody)
                .build();
        for (Map.Entry<String, String> entry : header.entrySet()) {
            request = request.newBuilder().addHeader(entry.getKey(), entry.getValue()).build();
        }
        try (Response response = INSTANCE.newCall(request).execute()) {
            // 打印返回的状态码和返回体数据
            int statusCode = response.code();
            String responseBody = "";
            if (statusCode==200) {
                assert response.body() != null;
                responseBody = response.body().string();
            }
            return responseBody;
        }
    }

    private static String getFormMapString(Map<String, Object> queryParams) {
        int pos = 0;
        if (queryParams != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                if (pos > 0) {
                    sb.append("&");
                }
                sb.append(String.format("%s=%s", entry.getKey(), entry.getValue()));
                pos++;
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 签名方法(使用了hutool的MD5工具和JSON工具)
     * @param command 具体API函数名
     * @param bodyMap API消息内容
     * @return Map<String,Object>
     */
    private static Map<String,Object> signature(String command, Map<String,Object> bodyMap){
        Map<String,Object> paramMap = new TreeMap<>();
        paramMap.put("appKey",APP_KEY);
        paramMap.put("command",command);
        paramMap.put("timestamp",System.currentTimeMillis());
        paramMap.put("params", JSONUtil.toJsonStr(bodyMap));

        StringBuilder signSb = new StringBuilder(APP_SECRET);
        paramMap.forEach((k,v)->{
            signSb.append(k).append("=").append(v).append("&");
        });
        signSb.deleteCharAt(signSb.length()-1).append(APP_SECRET);
        String signStr = SecureUtil.md5(signSb.toString()).toLowerCase();
        paramMap.put("signature",signStr);
        return paramMap;
    }

    /**
     * 设备详细信息查询
     * @param productCode 产品编号
     * @param deviceCode 设备编号
     * @return String
     */
    public static String deviceInfo(String productCode, String deviceCode) throws IOException {
        Map<String,Object> bodyMap = new HashMap<>(2);
        bodyMap.put("productCode",productCode);
        bodyMap.put("deviceCode",deviceCode);
        Map<String,Object> paramMap = signature("deviceInfo",bodyMap);


        return formPost(paramMap, Collections.emptyMap());
    }

    /**
     * 调用设备服务
     * @param productCode 产品编号
     * @param deviceCode 设备编号
     * @param identifier 服务名称(播音固定为：playvoice)
     * @param command 服务数据内容
     * @return String
     */
    public static String invokeService(String productCode, String deviceCode, String identifier, JSONObject command) throws IOException {
        Map<String,Object> bodyMap = new HashMap<>(4);
        bodyMap.put("productCode",productCode);
        bodyMap.put("deviceCode",deviceCode);
        bodyMap.put("identifier",identifier);
        bodyMap.put("body",command);
        Map<String,Object> paramMap = signature("invokeService",bodyMap);
        return formPost(paramMap,Collections.emptyMap());
    }

    /**
     * 清空设备待处理消息缓存
     * @param productCode 产品编号
     * @param deviceCode 设备编号
     * @return String
     */
    public static String clearMessage(String productCode,String deviceCode) throws IOException {
        Map<String,Object> bodyMap = new HashMap<>(2);
        bodyMap.put("productCode",productCode);
        bodyMap.put("deviceCode",deviceCode);
        Map<String,Object> paramMap = signature("clearMessage",bodyMap);
        return formPost(paramMap,Collections.emptyMap());
    }

    public static void main(String[] args) throws IOException {
        String productCode = "8lfT8tBv";
        String deviceCode = "vKca3f23kPXKcxtG";
        String identifier = "payurl";
        JSONObject command = JSONUtil.createObj()
                .set("url", ("http://merchant=test_version&deviceCode=1502242640000&amt=2.00"));
        System.out.println(command.toString());
        System.out.println(UrovoOpenApi.invokeService(productCode, deviceCode, identifier, command));
    }
}
