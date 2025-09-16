package com.linzi.daily.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import okhttp3.*;
import okio.Buffer;
import org.jetbrains.annotations.NotNull;

public class OkHttpUtil {

    private static OkHttpClient okHttpClient;

    public static void main(String[] args) throws IOException {
        JSONObject json  = new JSONObject();
        json.put("host","192.168.80.163");
        json.put("paper",58);
        for(int i=0;i<10;i++){
            if(i%2==0){
                json.put("content","<img  src=\"https://shouyin-oss.oss-cn-hangzhou.aliyuncs.com/goods/1730707606802940942.png\" style=\"width: 100%; height: 140px;\"><div ><div  style=\"text-align: center; font-size: 14px; font-weight: 700;\"> 出品单 </div><hr ><div  style=\"padding: 10px 0px 10px 3px; font-size: 14px;\"><div  style=\"margin-top: 10px;\"> 房&nbsp;&nbsp;号:<span >D02(谭娇)</span></div><div  style=\"margin-top: 5px;\"> 包厢类型: 外卖包 </div><div  style=\"margin-top: 5px; font-size: 14px;\"> 出品单号: P00128020250108171337TGF </div><div  style=\"margin-top: 5px;\"> 下单时间: 2025-01-08 17:13:37 </div><div  style=\"margin-top: 5px;\"> 出品位置: 厨房打印机 </div></div><hr ><div  style=\"padding: 10px 0px 10px 3px; font-size: 14px;\"><div ><table  cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr ><td  style=\"padding: 2px 10px; font-size: 14px;\"> 商品 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 数量 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 单价(元) </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 金额(元) </td></tr><tbody ><tr ><td  style=\"padding: 2px 10px; font-size: 14px;\"> 乐宝 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 1 <span >/瓶</span></td><td  style=\"padding: 2px 10px; font-size: 14px;\"> ￥25.00 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> ￥25.00 </td></tr></tbody></table></div></div><br ><hr ><div  style=\"margin: 10px 20px 0px 3px; font-size: 14px;\"><div  style=\"margin-top: 5px;\"> 操作人:王长宇 </div><div  style=\"margin-top: 5px;\"> 打印:1次 </div><div  style=\"margin-top: 5px;\"> 打印时间: 2025-01-08 17:13:37 </div><div  style=\"margin-top: 5px;\">签署:</div></div></div><div  style=\"margin-top: 5px;\"> 联数:1 </div>");
            }else{
                json.put("content","<img  src=\"https://shouyin-oss.oss-cn-hangzhou.aliyuncs.com/goods/1730707606802940942.png\" style=\"width: 100%; height: 140px;\"><div ><div  style=\"text-align: center; font-size: 14px; font-weight: 700;\"> 出品单 </div><hr ><div  style=\"padding: 10px 0px 10px 3px; font-size: 14px;\"><div  style=\"margin-top: 10px;\"> 房&nbsp;&nbsp;号:<span >D02(谭娇)</span></div><div  style=\"margin-top: 5px;\"> 包厢类型: 外卖包 </div><div  style=\"margin-top: 5px; font-size: 14px;\"> 出品单号: P00128020250108171337TGF </div><div  style=\"margin-top: 5px;\"> 下单时间: 2025-01-08 17:13:37 </div><div  style=\"margin-top: 5px;\"> 出品位置: 厨房打印机 </div></div><hr ><div  style=\"padding: 10px 0px 10px 3px; font-size: 14px;\"><div ><table  cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tr ><td  style=\"padding: 2px 10px; font-size: 14px;\"> 商品 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 数量 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 单价(元) </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 金额(元) </td></tr><tbody ><tr ><td  style=\"padding: 2px 10px; font-size: 14px;\"> 乐宝 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> 1 <span >/瓶</span></td><td  style=\"padding: 2px 10px; font-size: 14px;\"> ￥25.00 </td><td  style=\"padding: 2px 10px; font-size: 14px;\"> ￥25.00 </td></tr></tbody></table></div></div><br ><hr ><div  style=\"margin: 10px 20px 0px 3px; font-size: 14px;\"><div  style=\"margin-top: 5px;\"> 操作人:王长宇 </div><div  style=\"margin-top: 5px;\"> 打印:1次 </div><div  style=\"margin-top: 5px;\"> 打印时间: 2025-01-08 17:13:37 </div><div  style=\"margin-top: 5px;\">签署:</div></div></div><div  style=\"margin-top: 5px;\"> 联数:1 </div>");
            }
            MediaType mediaType = MediaType.Companion.parse("application/json");
            RequestBody requestBody = RequestBody.Companion.create(json.toString(), mediaType);
            System.out.println(DateUtil.now()+"======>"+json);
            Response resp = OkHttpUtil.formJson("http://192.168.80.163:9300/sendPrint",requestBody, Collections.emptyMap());
            System.out.println(DateUtil.now()+"<======"+resp.code()+"|"+resp.body().string());
        }
    }

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
