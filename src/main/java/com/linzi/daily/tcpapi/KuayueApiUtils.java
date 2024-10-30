package com.linzi.daily.tcpapi;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.linzi.daily.utils.OkHttpUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class KuayueApiUtils {
    //測試環境：https://ic-print-web-uat.kyslb.com/apisc
    //生產环境：https://ic-jiaboprint-web.kyslb.com/apisc
    private static final String apiUrl = "https://ic-print-web-uat.kyslb.com/apisc";
    private static final String devKey = "847682168B07B9A14405699157829243";
    private static final String devSecret = "P1B9AZGFHXRSI5CS9EBRMEDM8S64C0QZ";

    public void adddev(String deviceCode,String deviceName) throws IOException {
        System.out.println("===adddev===");
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey+reqTime+devSecret+deviceCode);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("deviceID",deviceCode);
        paramMap.put("memberCode",devKey);
        paramMap.put("devName",deviceName);
        paramMap.put("reqTime",reqTime);
        paramMap.put("securityCode",securityCode);
        System.out.println(JSONUtil.toJsonStr(paramMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(paramMap));
        Response response = OkHttpUtil.formJson(apiUrl+"/adddev",body, Collections.emptyMap());
        System.out.println(response.body().string());
    }

    public void deldev(String deviceCode) throws IOException {
        System.out.println("===deldev===");
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey+reqTime+devSecret+deviceCode);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("deviceID",deviceCode);
        paramMap.put("memberCode",devKey);
        paramMap.put("reqTime",reqTime);
        paramMap.put("securityCode",securityCode);
        System.out.println(JSONUtil.toJsonStr(paramMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(paramMap));
        Response response = OkHttpUtil.formJson(apiUrl+"/deldev",body, Collections.emptyMap());
        System.out.println(response.body().string());
    }

    public void deviceInfo(String deviceCode) throws IOException {
        System.out.println("===deviceInfo===");
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey+reqTime+devSecret+deviceCode);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("deviceID",deviceCode);
        paramMap.put("memberCode",devKey);
        paramMap.put("reqTime",reqTime);
        paramMap.put("securityCode",securityCode);
        System.out.println(JSONUtil.toJsonStr(paramMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(paramMap));
        Response response = OkHttpUtil.formJson(apiUrl+"/deviceInfo",body, Collections.emptyMap());
        System.out.println(response.body().string());
    }

    public void sendMsg(String deviceCode,Integer mode,Integer charset,String msgNo,String content) throws IOException {
        System.out.println("===sendMsg===");
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey+deviceCode+msgNo+reqTime+devSecret);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("deviceID",deviceCode);
        paramMap.put("memberCode",devKey);
        paramMap.put("mode",mode);
        paramMap.put("msgDetail",content);
        paramMap.put("charset",charset);
        paramMap.put("msgNo", msgNo);
        paramMap.put("reqTime",reqTime);
        paramMap.put("securityCode",securityCode);
        System.out.println(JSONUtil.toJsonStr(paramMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(paramMap));
        Response response = OkHttpUtil.formJson(apiUrl+"/sendMsg",body, Collections.emptyMap());
        System.out.println(response.body().string());
    }

    public void cancelPrint(String deviceCode) throws IOException {
        System.out.println("===cancelPrint===");
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey+reqTime+devSecret+deviceCode);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("deviceID",deviceCode);
        paramMap.put("memberCode",devKey);
        paramMap.put("reqTime",reqTime);
        paramMap.put("securityCode",securityCode);
        System.out.println(JSONUtil.toJsonStr(paramMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(paramMap));
        Response response = OkHttpUtil.formJson(apiUrl+"/cancelPrint",body, Collections.emptyMap());
        System.out.println(response.body().string());
    }

    public void reboot(String deviceCode) throws IOException {
        System.out.println("===reboot===");
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey+reqTime+devSecret+deviceCode);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("deviceID",deviceCode);
        paramMap.put("memberCode",devKey);
        paramMap.put("reqTime",reqTime);
        paramMap.put("securityCode",securityCode);
        System.out.println(JSONUtil.toJsonStr(paramMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(paramMap));
        Response response = OkHttpUtil.formJson(apiUrl+"/reboot",body, Collections.emptyMap());
        System.out.println(response.body().string());
    }

    public void realtimeStatus(String deviceCode) throws IOException {
        System.out.println("===realtimeStatus===");
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey+reqTime+devSecret+deviceCode);
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("deviceID",deviceCode);
        paramMap.put("memberCode",devKey);
        paramMap.put("reqTime",reqTime);
        paramMap.put("securityCode",securityCode);
        System.out.println(JSONUtil.toJsonStr(paramMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(paramMap));
        Response response = OkHttpUtil.formJson(apiUrl+"/realtimeStatus",body, Collections.emptyMap());
        System.out.println(response.body().string());
    }

    public static void main(String[] args) throws IOException {
        String deviceCode = "GP402410591603414";
        KuayueApiUtils kuayueApiUtils = new KuayueApiUtils();
        //kuayueApiUtils.adddev(deviceCode,"厂家样机3");
        kuayueApiUtils.realtimeStatus(deviceCode);
        //kuayueApiUtils.sendMsg(deviceCode,2,4,"1123223456","^XA\\r\\n^MD-10\\r\\n^MUd,200\\r\\n^POI^LL1023\\r\\n^PW597\\r\\n^LH0,0\\r\\n^CI28\\r\\n^CWA,Z:SYHT.FNT\\r\\n^FO13,141^GB567,2,2^FS\\r\\n^FO13,316^GB567,2,2^FS\\r\\n^FO13,358^GB567,2,2^FS\\r\\n^FO219,319^GB2,83,2^FS\\r\\n^FO13,403^GB567,2,2^FS\\r\\n^FO13,589^GB569,2,2^FS\\r\\n^FO61,405^GB2,183,2^FS\\r\\n^FO13,686^GB569,2,2^FS\\r\\n^FO13,875^GB569,2,2^FS\\r\\n^FO13,984^GB569,2,2^FS\\r\\n^FO581,141^GB2,842,2^FS\\r\\n^FO13,141^GB2,842,2^FS\\r\\n^FO13,508^GB567,2,2^FS\\r\\n^FO16,283^GB567,2,2^FS\\r\\n^FO419,50^AA41,41^FD省内次日^FS\\r\\n^FO420,50^AA41,41^FD省内次日^FS\\r\\n^FO421,50^AA41,41^FD省内次日^FS\\r\\n^FO102,158^BY2^BCN,58,N,N,N,\\r\\n^FDKY5000256021389^FS\\r\\n^FO511,243^AA19,19^FD已验视^FS\\r\\n^FO24,447^AA27,27^FD收^FS\\r\\n^FO25,447^AA27,27^FD收^FS\\r\\n^FO26,447^AA27,27^FD收^FS\\r\\n^FO19,444^GB38,41,38,W^FR^FS^FO24,533^AA27,27^FD寄^FS\\r\\n^FO25,533^AA27,27^FD寄^FS\\r\\n^FO26,533^AA27,27^FD寄^FS\\r\\n^FO359,323^AA27,27^FD深圳市^FS\\r\\n^FO360,323^AA27,27^FD深圳市^FS\\r\\n^FO361,323^AA27,27^FD深圳市^FS\\r\\n^FO333,368^AA27,27^FD壹方城点部^FS\\r\\n^FO334,368^AA27,27^FD壹方城点部^FS\\r\\n^FO335,368^AA27,27^FD壹方城点部^FS\\r\\n^FO66,422^AA22,22^FD****** 蟹老板 13888888888 ^FS\\r\\n^FO67,422^AA22,22^FD****** 蟹老板 13888888888 ^FS\\r\\n^FO68,422^AA22,22^FD****** 蟹老板 13888888888 ^FS\\r\\n^FO66,462^AA19,19^FD广东省深圳市宝安区新安街道创业一路1号(宝安中心地铁站E^FS\\r\\n^FO66,481^AA19,19^FD口步行200米)深圳市宝安区人民政府^FS\\r\\n^FO67,462^AA19,19^FD广东省深圳市宝安区新安街道创业一路1号(宝安中心地铁站E^FS\\r\\n^FO67,481^AA19,19^FD口步行200米)深圳市宝安区人民政府^FS\\r\\n^FO68,462^AA19,19^FD广东省深圳市宝安区新安街道创业一路1号(宝安中心地铁站E^FS\\r\\n^FO68,481^AA19,19^FD口步行200米)深圳市宝安区人民政府^FS\\r\\n^FO66,521^AA19,19^FD派大星三号 默*5 13580007886 ^FS\\r\\n^FO66,556^AA19,19^FD广东省佛山市禅城区汾江中路6号佛山汽车站候机楼^FS\\r\\n^FO19,695^AA22,22^FD备注：^FS\\r\\n^FO31,173^AA22,22^FD随^FS\\r\\n^FO32,173^AA22,22^FD随^FS\\r\\n^FO33,173^AA22,22^FD随^FS\\r\\n^FO22,600^AA19,19^FD托寄物：电池^FS\\r\\n^FO22,656^AA19,19^FD回单：回单图片1张^FS\\r\\n^FO358,600^AA19,19^FD件数：3^FS\\r\\n^FO358,628^AA19,19^FD实重：99.0kg^FS\\r\\n^FO358,656^AA19,19^FD计重：^FS\\r\\n^FO227,230^AA22,22^FDKY5000256021389^FS\\r\\n^FO228,230^AA22,22^FDKY5000256021389^FS\\r\\n^FO229,230^AA22,22^FDKY5000256021389^FS\\r\\n^FO165,962^AA22,22^FDKY5000256021389-100-300-^FS\\r\\n^FO166,962^AA22,22^FDKY5000256021389-100-300-^FS\\r\\n^FO167,962^AA22,22^FDKY5000256021389-100-300-^FS\\r\\n^FO13,115^AA18,18^FD第13次打印：2024-04-29 19:58:39^FS\\r\\n^FO319,115^AA18,18^FD寄件时间：^FS\\r\\n^FO27,820^AA38,38^FD1389    1/3^FS\\r\\n^FO28,820^AA38,38^FD1389    1/3^FS\\r\\n^FO29,820^AA38,38^FD1389    1/3^FS\\r\\n^FO22,628^AA18,18^FD付款方式：寄方付^FS\\r\\n^FO127,889^BY1^BCN,58,N,N,N,\\r\\n^FDKY5000256021389-100-300-|d^FS\\r\\n^FO28,294^AA18,18^FD始^FS\\r\\n^FO29,294^AA18,18^FD始^FS\\r\\n^FO30,294^AA18,18^FD始^FS\\r\\n^FO22,291^GB30,22,22,W^FR^FS^FO411,697^BQN,2,4\\r\\n^FDHA,KY5000256021389-100-300-|d^FS\\r\\n^FO31,204^AA22,22^FD货^FS\\r\\n^FO32,204^AA22,22^FD货^FS\\r\\n^FO33,204^AA22,22^FD货^FS\\r\\n^FO32,234^AA22,22^FD联^FS\\r\\n^FO33,234^AA22,22^FD联^FS\\r\\n^FO34,234^AA22,22^FD联^FS\\r\\n^XZ");
    }
}
