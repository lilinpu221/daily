package com.linzi.daily.mqttapi;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Lil
 */
@Slf4j
public class MqttApi {

    static String apiUrl = "http://iot.poscom.cn/iot/openapi/v1";
    static String appKey = "54xyYHcA58L8Xo3X";
    static String appSecret = "zVTEpfBoMr9u8GRVT70CxPOSt9MGQNL2";

    public static String clearMessage(String productCode,String deviceCode){
        String command = "clearMessage";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        return call(command,params);
    }

    public static String deviceChange(String productCode,String deviceCode,String type){
        String command = "deviceChange";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        params.put("type",type);
        return call(command,params);
    }

    public static String deviceInfo(String productCode,String deviceCode){
        String command = "deviceInfo";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        return call(command,params);
    }

    public static String kickoff(String productCode,String deviceCode){
        String command = "kickoff";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        return call(command,params);
    }

    public static String pageDevices(String productCode,String deviceCode){
        String command = "pageDevices";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("pageNo",1);
        params.put("pageSize",15);
        return call(command,params);
    }

    public static String getMessage(String productCode,String deviceCode,String messageId){
        String command = "getMessage";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        params.put("messageId",messageId);
        return call(command,params);
    }

    public static String model(String productCode,String deviceCode,String funcType){
        String command = "model";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("funcType",funcType);
        return call(command,params);
    }

    public static String getProperty(String productCode,String deviceCode,String identifier){
        String command = "getProperty";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        params.put("identifier",identifier);
        return call(command,params);
    }

    public static String setProperty(String productCode,String deviceCode,String identifier,Object value){
        String command = "setProperty";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        params.put("identifier",identifier);
        params.put("body",value);
        return call(command,params);
    }

    public static String invokeService(String productCode,String deviceCode,String inDeviceCode){
        String command = "invokeService";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode", StrUtil.isBlank(inDeviceCode)?deviceCode:inDeviceCode);
        params.put("identifier","playvoice");
        JSONObject body = new JSONObject();
        body.put("payload","工行支付收款240.2元");
        params.put("body",body);
        return call(command,params);
    }

    public static String getShadow(String productCode,String deviceCode,String functype){
        String command = "getShadow";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        params.put("funcType",functype);
        return call(command,params);
    }

    public static String upgrade(String productCode,String deviceCode,String firmwareVersion){
        String command = "upgrade";
        JSONObject params = new JSONObject();
        params.put("productCode",productCode);
        params.put("deviceCode",deviceCode);
        params.put("firmwareVersion",firmwareVersion);
        return call(command,params);
    }

    final static String call(String command,JSONObject params){
        //拼接签名串secretparam1=value1&param2=value2secret
        //组装请求共同参数
        Map<String,String> paramMap = new TreeMap<>();
        paramMap.put("appKey",appKey);
        paramMap.put("command",command);
        paramMap.put("timestamp",String.valueOf(System.currentTimeMillis()));
        paramMap.put("params",params.toString());
        StringBuilder signSb = new StringBuilder(appSecret);
        paramMap.forEach((k,v)->{
            signSb.append(k).append("=").append(v).append("&");
        });
        signSb.deleteCharAt(signSb.length()-1).append(appSecret);
        log.info("签名字符串：{}", signSb);
        //计算签名并添加到请求参数
        paramMap.put("signature", SecureUtil.md5(signSb.toString()).toLowerCase());
        log.info("请求命令:{},请求参数:{}",command, JSON.toJSONString(paramMap));
        return HttpRequest.post(apiUrl)
                .header(Header.USER_AGENT, "Hutool http")
                .formStr(paramMap)
                .timeout(20000)
                .execute().body();
    }

    public static void main(String[] args) {
        String resp = MqttApi.deviceInfo("dmem","rypjtpv");
        log.info("请求返回：{}",resp);
    }
}
