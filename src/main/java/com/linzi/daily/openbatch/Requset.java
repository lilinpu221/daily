package com.linzi.daily.openbatch;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson2.JSONObject;
import com.linzi.daily.utils.OkHttpUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.Objects;

public class Requset {

    private static final String API_URL = "http://sim.poscom.cn/iotmanager/openBatch/productline";

    public static String register(String batchCode,String batchSecret,String uuid) throws IOException {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,batchSecret.getBytes(),batchSecret.getBytes());
        String text = "{\"uuid\":\""+ uuid +"\"}";
        text = aes.encryptHex(text);
        MediaType mediaType = MediaType.Companion.parse("application/text");
        RequestBody requestBody = RequestBody.Companion.create(text, mediaType);
        String resp = Objects.requireNonNull(OkHttpUtil.formJson(API_URL + "/register/"+batchCode, requestBody, null).body()).string();
        JSONObject respJson = JSONObject.parseObject(resp);
        if(respJson.getIntValue("code")==200){
            return aes.decryptStr(respJson.getString("data"));
        }
        return resp;
    }

    public static String scan(String batchCode,String batchSecret,String sn) throws IOException {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,batchSecret.getBytes(),batchSecret.getBytes());
        String text = "{\"sn\":\""+ sn +"\"}";
        text = aes.encryptHex(text);
        MediaType mediaType = MediaType.Companion.parse("application/text");
        RequestBody requestBody = RequestBody.Companion.create(text, mediaType);
        String resp = Objects.requireNonNull(OkHttpUtil.formJson(API_URL + "/scan/"+batchCode, requestBody, null).body()).string();
        JSONObject respJson = JSONObject.parseObject(resp);
        if(respJson.getIntValue("code")==200){
            return aes.decryptStr(respJson.getString("data"));
        }
        return resp;
    }

    public static String validate(String batchCode,String batchSecret,String sn) throws IOException {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,batchSecret.getBytes(),batchSecret.getBytes());
        String text = "[{\"sn\":\""+ sn +"\"}]";
        text = aes.encryptHex(text);
        MediaType mediaType = MediaType.Companion.parse("application/text");
        RequestBody requestBody = RequestBody.Companion.create(text, mediaType);
        String resp = Objects.requireNonNull(OkHttpUtil.formJson(API_URL + "/validate/"+batchCode, requestBody, null).body()).string();
        JSONObject respJson = JSONObject.parseObject(resp);
        if(respJson.getIntValue("code")==200){
            return aes.decryptStr(respJson.getString("data"));
        }
        return resp;
    }

    public static String pack(String batchCode,String batchSecret,String sn,String boxno) throws IOException {
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding,batchSecret.getBytes(),batchSecret.getBytes());
        String text = "{\"boxno\":\""+ boxno +"\",\"dataType\":\"sn\",\"data\":\""+sn+"\"}";
        text = aes.encryptHex(text);
        MediaType mediaType = MediaType.Companion.parse("application/text");
        RequestBody requestBody = RequestBody.Companion.create(text, mediaType);
        String resp = Objects.requireNonNull(OkHttpUtil.formJson(API_URL + "/pack/"+batchCode, requestBody, null).body()).string();
        JSONObject respJson = JSONObject.parseObject(resp);
        if(respJson.getIntValue("code")==200&& CharSequenceUtil.isNotBlank(respJson.getString("data"))){
            return aes.decryptStr(respJson.getString("data"));
        }
        return resp;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("===========register================");
        System.out.println(Requset.register("GZMO000381","OZ6zDeWZxuPlU6Np","123123123"));
        System.out.println("===========scan================");
//        System.out.println(Requset.scan("240604_test","H4yRCRN67cGVskmH","123654"));
        System.out.println("===========validate================");
//        System.out.println(Requset.validate("123213","sYsk2TEJ9BJCympB","987654"));
        System.out.println("===========pack================");
//        System.out.println(Requset.pack("123213","sYsk2TEJ9BJCympB","987654",RandomUtil.randomString(6)));
    }
}
