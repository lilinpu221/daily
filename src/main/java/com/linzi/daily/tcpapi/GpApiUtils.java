package com.linzi.daily.tcpapi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.linzi.daily.utils.OkHttpUtil;
import okhttp3.*;
import org.springframework.util.StringUtils;

public class GpApiUtils {
    //smarnet_api
    /**
     * 汇来米
     * https://printcloudh.huilaimi.com/api/apisc
     * nmnoksz8fhqizivph5xzscf23fufrtzk
     * ceawbn40
     *
     * 佳博
     * https://api.poscom.cn/apisc
     * a1492960c3224140b7a862557feb5e77
     * CRZYZ8WG
     *
     * 雷武森林
     * https://printer.aioptic.net/apisc
     * b6199c453b5b43f3b644ad21b48367e3
     * ZWWT9SQ9
     *
     * 富友
     * http://api.fuioupay.com/apisc
     * 2dc21bcfb28e4584a4f269c776d6e591
     * D4CPOKT7
     *
     * 初量科技
     * http://cloudprint.partydaysport.com/apisc
     * a7b3d733c81a44ba8349bc4048829f51
     * SCF9K7SS
     */
    private static final String apiUrl = "https://apitest.poscom.cn/apisc";
    private static final String devKey = "847682168B07B9A14405699157829243";
    private static final String devSecret = "P1B9AZGFHXRSI5CS9EBRMEDM8S64C0QZ";

    public void setPushUrl(String pushUrl) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + pushUrl);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("pushUrl", pushUrl).build();
        String reps = OkHttpUtil.formPost(apiUrl + "/setPushUrl", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendLabelMsg(String deviceCode,String content2,int seq) throws IOException {
        String mode2 = "3";
        String reqTime = String.valueOf(System.currentTimeMillis());
        String msgNo = String.valueOf(seq);
        String securityCode = SecureUtil.md5(devKey + deviceCode + msgNo + reqTime + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("mode", mode2)
                .add("msgDetail", content2)
                .add("msgNo", msgNo)
                .add("charset", "1")
                .add("reprint", "1")
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendMsg", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendMsg(String deviceCode,String cmdType,String mode,String charset,String content) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String msgNo = "123456789";
        String reprint = "1";
        String securityCode = SecureUtil.md5(devKey + deviceCode + msgNo + reqTime + devSecret);
//        JSONObject json = new JSONObject();
//        json.put("reqTime", reqTime);
//        json.put("securityCode", securityCode);
//        json.put("memberCode", devKey);
//        json.put("deviceID", deviceCode);
//        json.put("mode", mode);
//        json.put("msgDetail", content);
//        json.put("msgNo", msgNo);
//        json.put("charset", charset);
//        json.put("reprint", reprint);
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json.toString());
          RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("mode", mode)
                .add("msgDetail", content)
                .add("msgNo", msgNo)
                .add("charset", charset)
                .add("reprint", reprint).add("cmdType", cmdType)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendMsg", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendMsg(String deviceCode,String content, String mode, String charset, String msgNo,String times) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + msgNo + reqTime + devSecret);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("mode", mode)
                .add("msgDetail", content)
                .add("charset", charset)
                .add("msgNo", msgNo)
                .add("multi", "0")
                .add("reprint", "1")
                .add("times", times)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendMsg", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendMsgMulti(String deviceCode,String content, String mode, String charset, String multi,String times) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + reqTime + devSecret);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("mode", mode)
                .add("msgDetail", content)
                .add("charset", charset)
                .add("multi", multi)
                .add("times", times)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendMsg", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendCommand(String deviceCode,String cmd) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + cmd+reqTime + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("command", cmd)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendCommand", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendVoice(String deviceCode,String voice) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + reqTime + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("charset", "1")
                .add("msgDetail", "msgDetail")
                .add("mode","2")
                .add("voice", voice)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendVoice", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendMultiVoice(String deviceCode) throws IOException {
        Map<String, String> voice1 = new HashMap<String, String>(2);
        Map<String, String> voice2 = new HashMap<String, String>(2);
        voice1.put("volumn", "090");
        voice1.put("voice", "0|9|0|已|收|到|9|元");

        voice2.put("volumn", "060");
        voice2.put("voice", "0|6|0|已|收|到|6|元");

        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        list.add(voice1);
        list.add(voice2);

        String voiceJson = JSONUtil.toJsonStr(list);
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("voice", voiceJson)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendMultiVoice", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void printVolumeVoice(String deviceCode) throws IOException {
        String content = HexUtil.encodeHexStr("0901|2|3|4|5|6|7|8|9");
        RequestBody requestBody = new FormBody.Builder()
                .add("deviceID", deviceCode)
                .add("Content", content).build();
        String reps = OkHttpUtil.formPost(apiUrl + "/PrintVolumeVoice", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void queryState() throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String msgNo = "123456789";
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + msgNo);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("msgNo", msgNo)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/queryState", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void listDevice() throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/listDevice", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void deviceInfo(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + deviceCode + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/deviceInfo", requestBody).body().string();
        System.out.println(deviceCode+"==response：" + reps);
    }

    public void printhex(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + reqTime + reqTime + devSecret);
        String content3 = "1B401B61011D48001D68501D77021D6B49107B4259303132333435363738393031321B61011B64011B2138BCD1B2A9CDF8C2E7D4C6B4F2D3A11B21001B64011B61002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D1B6401B6C52A2A1B24900031333830303130303530301B64011B2108B1B1BEA9CAD0B2FDC6BDC7F8BFC6D0C7CEF7C2B7313036BAC5B9FAB7E7C3C0CCC6D7DBBACFC2A5362D313530351B21001B64012D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D1B6401D0F2BAC5A3BA303932351B242C01C8CBCAFDA3BA331B6401CAB1BCE4A3BA30312D30312031323A31321B6401B6A9B5A5A3BA59303132333435363738393031321B64012D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D1B6401CBB3D0F21B243C00C3FBB3C61B24A800B5A5BCDB1B24E400CAFDC1BF1B245001D0A1BCC61B64012D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D1B64011B241200311B249C0031352E30301B24F600331B24440134352E30301B64011B243000D0A1B3B4C8E21B64011B24080031311B249C0031352E30301B24F600331B24440134352E30301B64011B243000C5A3C8E228C8FDB7D6CAEC291B64011B241200331B249C0031352E30301B24F600311B24440131352E30301B64011B243000C8E2BDB4BCA6B5B01B64011B241200341B249C0031352E30301B24F600311B24440131352E30301B64011B243000D4C6C4CFB2CBC6B7D3D0B8AFD6F1C8E2CDE8D7D3D2BBBDEFB6E0B5E3C3BBD3D0B9C7CDB7CCC0C3B2CBC6BADCBAC3B3D4B0A11B64011B241200351B249C0031352E30301B24F600311B24440131352E30301B64011B243000C1FACBC9B7EFBDAC1B64012D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2DBACFBCC6BDF0B6EEA3BA1B2438013233352E30301B6401D3C5BBDDBDF0B6EEA3BA1B24440131352E30301B6401B0FCD7B0B7D1A3BA1B245001332E30301B6401C5E4CBCDB7D1A3BA1B245001382E30301B64012D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D1B6401CAB5CAD5BDF0B6EEA3BA1B2410011B21393232302E30301B64011B21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D1B6401B1B8D7A2A3BA1B64011B2139B2BBD2AAC0B1A3ACB2BBD2AACBE2A1A31B64041B61011D286B03003145311D286B03003143081D286B2E00315030687474703A2F2F77656978696E2E71712E636F6D2F722F6B4856336236374558504D6A72656F4D397943431D286B03003151301B4A101B6101C9A8C2EBB9D8D7A2BCD1B2A91B6401000A1D564200";

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("mode", "3")
                .add("msgNo", reqTime)
                .add("deviceID", deviceCode)
                .add("msgDetail", content3)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/printhex", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void addgroup() throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("grpName", "默认分组1")
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/addgroup", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void group() throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/group", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void delgroup(String groupId) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + groupId);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("grpID", groupId)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/delgroup", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void editgroup(String groupId) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + groupId);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("grpID", groupId)
                .add("grpName", "默认分组9")
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/editgroup", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void adddev(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("devName", deviceCode)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/adddev", requestBody).body().string();
        System.out.println(deviceCode+"==response：" + reps);
    }

    public void device(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/device", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void editdev(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("devName", "修改打印机名称")
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/editdev", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void deldev(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/deldev", requestBody).body().string();
        System.out.println(deviceCode+"==response：" + reps);
    }

    public void templetPrint(String deviceCode,String code, String data,String multi) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + reqTime + reqTime + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("templetID", code)
                .add("tData", data)
                .add("multi", multi)
                .add("msgNo", reqTime)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/templetPrint", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void cancelPrint(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        String all = "1";

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("all", all)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/cancelPrint", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void getStatus(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret);

        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/getStatus", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void setPause(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        String pause = "1";
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("pause", pause)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/setPause", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void update(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        String ip = "192.168.10.93";
        String port = "8080";
        String file = "8080";
        String version = "8080";
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("ip", ip)
                .add("port", port)
                .add("file", file)
                .add("version", version)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/update", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void voiceUpdate(String deviceCode,String version) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("version", version)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/voiceUpdate", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void romUpdate(String deviceCode,String version) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("version", version)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/romupdate", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void setLogo(String deviceCode,String imgUrl) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("imgUrl", imgUrl)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/setLogo", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void deleteLogo(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/deleteLogo", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void getLabelLen(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/getLabelLen", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendVolume(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String volumn = "90";
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("volume", volumn)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/sendVolume", requestBody).body().string();
        System.out.println("==response：" + reps);
    }


    public void setNet(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        String netType = "wifi";
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("netType", netType)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/setNet", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void setVoiceType(String deviceCode) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret + deviceCode);
        String voiceType = "0";
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("voiceType", voiceType)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/setVoiceType", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void listDeviceByGroup() throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String groupId = "14";
        String securityCode = SecureUtil.md5(groupId + devKey + reqTime + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("groupId", groupId)
                .build();

        String reps = OkHttpUtil.formPost(apiUrl + "/listDeviceByGroup", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void getstatus(String deviceCode) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("imsi", deviceCode)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/getStatus", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public static String barcode(String text, int charWidth) {
        //条码都默认居中
        StringBuilder barCode = new StringBuilder("1B6101")
                //文字显示位置
                .append("1D4802")
                //固定50像素高度，2毫米宽度
                .append("1D68501D7702")
                //CODE128
                .append("1D6B49");
        //CODE128条码，前后各需要2个标识字符
        int maxNumLen = charWidth - 4;
        if (NumberUtil.isNumber(text)) {
            //数字字符串使用CODEC
            if (text.length() > maxNumLen) {
                return "0A";
            }
            //两位数字转十六进制字符串
            int index = 0;
            StringBuilder result = new StringBuilder();
            while (index < text.length()) {
                String hexInt = HexUtil.toHex(Integer.valueOf(text.substring(index, index + 2 > text.length() ? text.length() : index + 2)));
                if (hexInt.length() == 1) {
                    hexInt = "0" + hexInt;
                }
                //两位转十六进制
                result.append(hexInt);
                index = index + 2;
            }
            //加两位因为需要预留2位CODEC编码长度
            String textLenStr = HexUtil.toHex(result.toString().length() / 2 + 2);
            if (textLenStr.length() == 1) {
                textLenStr = "0" + textLenStr;
            }
            barCode.append(textLenStr).append("7B43").append(result.toString()).append("0A");
        } else {
            //字母+数字字符串使用CODEB
            if (text.length() > maxNumLen / 2) {
                return "0A";
            }
            String hexText = HexUtil.encodeHexStr(text, Charset.forName("GB18030")).toUpperCase();
            String textLenStr = HexUtil.toHex(text.length() + 2).toUpperCase();
            if (textLenStr.length() == 1) {
                textLenStr = "0" + textLenStr;
            }
            barCode.append(textLenStr).append("7B42").append(hexText).append("0A");
        }
        System.out.println(barCode.toString());
        return barCode.toString();
    }

    public void pushDevice(String deviceCode,String url, String cmd, String status, String info) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("memberCode", devKey)
                .add("securityCode", SecureUtil.md5(deviceCode + reqTime + devSecret))
                .add("deviceID", deviceCode)
                .add("status", status)
                .add("info", info)
                .build();
        String reps = OkHttpUtil.formPost(url, requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void pushNotice(String deviceCode,String url, final String cmd, final String deviceID, final String pushInfo) throws Exception {
        // 创建OkHttpClient对象
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS).build();
        long reqTime = System.currentTimeMillis();
        FormBody.Builder newFormBody = new FormBody.Builder();
        newFormBody.add("cmd", cmd).add("deviceID", deviceID).add("reqTime", String.valueOf(reqTime));
        //更新订单信息
        switch (cmd) {
            case "printStatus": // 设备状态
                if (pushInfo.indexOf("#") > -1) {
                    String status = StringUtils.split(pushInfo, "#")[0];
                    String info = StringUtils.split(pushInfo, "#")[1];
                    newFormBody.add("status", status);
                    newFormBody.add("info", info);
                } else {
                    newFormBody.add("status", pushInfo);
                }
                break;
            case "printFinish": // 订单完成
                newFormBody.add("orderID", "123456");
                break;
            case "printError": // 订单指令错误丢弃打印
                newFormBody.add("orderID", "123456");
                break;
            case "printlabelLen": // 标签长度
                newFormBody.add("labelLen", pushInfo);
                break;
            case "printQueue": //取号
                newFormBody.add("status", pushInfo);
                break;
            default:
        }
        String securityCode = SecureUtil.md5(deviceCode + reqTime + devSecret);
        newFormBody.add("securityCode", securityCode);

        RequestBody requestBody = newFormBody.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        // 把请求对象传递给Call，创建call网络请求
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //call.cancel();
                StringBuilder params = new StringBuilder();
                call.cancel();
                for (int i = 0; i < newFormBody.build().size(); i++) {
                    params.append("&")
                            .append(newFormBody.build().encodedName(i))
                            .append("=")
                            .append(newFormBody.build().encodedValue(i));
                }
                System.out.println("=push error=" + url + "?t=" + params.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 未做返回判断是否推送成功  {"data": "OK"}
                //String string = response.body().string();
                StringBuilder params = new StringBuilder();
                for (int i = 0; i < newFormBody.build().size(); i++) {
                    params.append("&")
                            .append(newFormBody.build().encodedName(i))
                            .append("=")
                            .append(newFormBody.build().encodedValue(i));
                }
                System.out.println("=push success=" + url + "?t=" + params);
            }
        });
    }

    public static void main (String[]args) throws Exception {
        GpApiUtils api = new GpApiUtils();
        String mode2 = "2";
        String mode2Esc = "SIZE 40 mm, 30 mm \n" +
                "GAP 2 mm, 0 mm \n" +
                "REFERENCE 0, 0 \n" +
                "SPEED 4 \n" +
                "DENSITY 8 \n" +
                "DIRECTION 0 \n" +
                "SET HEAD ON\n" +
                "SET PRINTKEY OFF \n" +
                "SET RIBBON OFF \n" +
                "SET CUTTER OFF \n" +
                "SET TEAR ON \n" +
                "SHIFT 0 \n" +
                "SET KEY1 ON \n" +
                "SET KEY2 ON \n" +
                "CLS \n" +
                "TEXT 39,25,\"TSS24.BF2\",0,1,1,\"gainscha\" \n" +
                "BARCODE 13,149,\"128\",55,1,0,4,2,\"123456\" \n" +
                "QRCODE 200,50,L,4,A,0,\"cloud.poscom.cn\" \n" +
                "PRINT 1,1\n";
        String mode3 = "3";
        String mode3Esc = "1B61011D48001D68501D77021D6B490e7B43502632013e020e15247B42330A";
        String mode3Tsc = "53495A45203335206D6D2C3435206D6D0A4741502030206D6D2C30206D6D0A5245464552454E434520302C300A535045454420340A44454E5349545920380A444952454354494F4E20300A5345542048454144204F4E0A534554205052494E544B4559204F46460A534554204B455931204F4E0A534554204B455932204F4E0A534849465420300A434C530A54455854203138302C31302C2254535332342E424632222C302C312C312C225C5B225D305C5B225D220A544558542033302C31302C2254535332342E424632222C302C312C312C22BCD1B2A9D4C6B4F2D3A15C5B415DCEDECFDFBEDBC9CCBBFA220A424152434F44452033302C37352C22313238222C36342C312C302C322C342C22323030393032313235343130220A5152434F44452033302C3138302C4C2C362C412C302C22636C6F75642E706F73636F6D2E636E220A54455854203232302C3138302C2254535332342E424632222C39302C322C322C22C7EBC9A8C2EB220A5052494E5420312C310A";
        String mode3Zpl = "5E58410A5E4D43595E4D54445E4D44305E4C54305E4D4D545E4D4E4E5E50573332305E4C4C3236345E5052335E504D4E5E504F495E4A4D415E4C48302C345E4C524E5E4357412C453A4D53554E472E464E545E4357422C453A4D53554E472E464E545E434932380A5E585A0A5E58410A5E464F32372C30385E41424E2C33322C33325E4644E68BA7E9A699E98791E6A998E88CB65E46530A5E464F3232382C30385E41424E2C33322C33325E4644315E46530A5E464F3236362C30385E41424E2C33322C33325E4644E4BBBD5E46530A5E464F32372C34325E41424E2C33322C33325E4644EFBC88E4B8ADEFBC8CE5B091E586B0EFBC8CE6ADA3E5B8B8EFBC895E46530A5E464F32372C38305E41414E2C31382C31385E4644E58D95E58FB7EFBC9A5E46530A5E464F3131312C38305E41414E2C31382C31385E464432303138303731303232313035393933355E46530A5E464F32372C3130355E41414E2C31382C31385E4644E4B88BE58D95E697B6E997B4EFBC9A5E46530A5E464F3131372C3130355E41414E2C31382C31385E4644323031382D30372D31302032323A31313A30305E46530A5E464F32372C3133315E41414E2C32342C32345E4644E98791E9A29DEFBC9A5E46530A5E464F3131312C3133315E41414E2C32342C32345E46443138E585835E46530A5E464F32372C3136365E41414E2C32342C32345E4644E58F96E9A490E58FB7EFBC9A5E46530A5E464F3131342C3136365E41424E2C33322C33325E46443030335E46530A5E464F32382C3230305E47423238302C302C325E46530A5E464F32372C3230395E41414E2C31382C31385E4644E68993E58DB0E697B6E997B4EFBC9A5E46530A5E464F3131372C3230395E41414E2C31382C31385E4644323031382D30372D31302032323A31313A30365E46530A5E5051312C302C312C590A5E585A";
        String charset1 = "1";
        String charset4 = "4";
        String deviceCodes = "dmemrzw423t";
        String[] devices = deviceCodes.split(",");
        for(String device:devices){
            //添加打印机
            //api.adddev(device);
            //api.sendMsg(deviceCodes,mode2,charset1,mode2Esc);
            //api.deviceInfo(device);
            //api.getStatus(device);
            //取消未打印
            //api.cancelPrint(device);
            //api.romUpdate(device,"3.5.14.35");
            //api.sendVoice(device,"已|收|到|0|.|0|1|元");
            //Thread.sleep(1500);
            //删除打印机
            //api.deldev(device);
            api.sendMsg(device,"ESC","3","1","1B401B61021D21110A0A1B61011D21112339B6F6C1CBC3B4C9CCBCD20A1B401B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61011D21001B2D001B4500B9B1B2E828CBFECDE5B5EA290A1B401B61011D21011B2D001B4500D2D1D6A7B8B60A0A1B401B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61001D21001B2D001B4500B6A9B5A5B1E0BAC5A3BA383037353735363030313635373933393434380A1B401B61001D21001B2D001B4500CFC2B5A5CAB1BCE4A3BA323032342D31312D30332031383A30343A32340A1B401B61011D21011B2D001B4500C1A2BFCCCBCDB4EF0A1B400A1B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D20B1B8D7A2202D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61001D21111B2D001B4500D2C0BEDDB2CDC1BFCCE1B9A9B2CDBEDF0A1B400A1B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2031BAC5C0BAD7D3202D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B61001D21011B2D001B4500DCD4C0F2C4CCC2CC5BC8C82BD5FDB3A3CCC75D20202020202020202020202020202020202020202A3120202020352E390A1B61001D21011B2D001B4500BCA6B5B0D7D0A3A8B0C2C0FBB0C2CBE9A3A92020202020202020202020202020202020202020202A3120202031312E300A1B61001D21011B2D001B4500BCA6B5B0D7D0A3A8BFDACEB6A3A95BD4ADCEB65D202020202020202020202020202020202020202A3120202031302E300A1B61001D21011B2D001B4500D1E0C2F3C4CCB2E85BB3A3B9E6CCC72BC8C85D20202020202020202020202020202020202020202A3120202020382E300A1B400A1B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D20B7D1D3C3202D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B61001D21001B2D001B4500C5E4CBCDB7D1202020202020202020202020202020202020202020202020202020202020202020202020202020312E360A1B61001D21001B2D001B4500D4ADBCDB2020202020202020202020202020202020202020202020202020202020202020202020202020202033362E350A1B61001D21011B2D001B4500CAB5B8B62020202020202020202020202020202020202020202020202020202020202020202020202020202032382E300A1B401B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61001D21011B2D001B4500A1BED2FECBBDB1A3BBA4A1BFB9CBBFCDB5D8D6B7D2D1D2FEB2D8A3ACC4FABFC9B5C7C2BCB6F6C1CBC3B4C9CCBCD2B6CBBBF2C6EFCAD6B6CBB2E9BFB40A1B401B61001D21011B2D001B4500C0EE2A2A0A1B401B61001D21011B2D001B45003137363430323736383834D7AA3833320A1B401B61001D21011B2D001B45003138362A2A2A2A353031360A1B61011D48001D68501D77021D6B490e7B43504b4b3c01414f272c7B42380A1B61011D21002A2A2A2A2A2A2A2A1D21112339CDEA1D21002A2A2A2A2A2A2A2A0A0A1B61011D2100BCD1B2A9D4C6B4F2D3A10A0A0A0A1D564200");
        }
        //api.sendMsg("dmemrzw423t","TSPL","3","1","5345542048454144204F4E0A534554205052494E544B4559204F46460A534554204B455931204F4E0A534554204B455932204F4E0A434C530A53495A45203530206D6D2C3430206D6D0A4741502030206D6D2C30206D6D0A535045454420330A44454E5349545920370A5345542054454152204F46460A53455420524942424F4E204F46460A534849465420300A424F5820332C322C3339382C3331352C320A424152434F4445203131302C35302C22313238222C33332C302C302C312C332C22313233343536220A424152434F4445203131302C35302C22313238222C33332C302C39302C312C332C22313233343536220A424152434F4445203131302C35302C22313238222C33332C302C3138302C312C332C22313233343536220A5052494E5420312C31");
        //String tData = "{\"words1672107214122\":\"撒发发撒safsasd\",\"barcode1688023763027\":\"124141-23423213\",\"qrcode1688023770755\":\"https://www.poscom.cn/help/doc/\"}";
        //api.templetPrint(deviceCodes,"eeead728dacf4b37acd7e4ab1ae5c4ec",tData,"0");
        //api.setPushUrl("https://callback.0xiao.com/api/common/printCallBackJb");
        //修改打印机
//		api.editdev();
        //发送打印数据
        //api.sendMsg(deviceCodes,mode3,charset,mode3Zpl);
//		api.device(deviceCodes);
        //发送语音
        //api.sendVoice("1|2|3|4|5|6|7|8|9");
        //发送多个带音量语音(暂不使用)
//		api.sendMultiVoice();
        //发送单个带音量语音(暂不使用)
//		api.printVolumeVoice();
        //打印任务状态查询
//		api.queryState();
        //打印十六进制数据
//		api.printhex();

        //获取打印机列表
//        api.listDevice();
        //添加分组
//		api.addgroup();
        //查询分组列表
//		api.group();
        //修改分组
//		api.editgroup("48");
        //删除分组
//		api.delgroup("48");

        //for(int i=0;i<2;i++){
        //    String detail = "<gpWord Align=1 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>枫尚购物中心</gpWord><gpBr/><gpWord Align=1 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>门店联</gpWord><gpBr/><gpWord Align=1 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>订单号：0101230704114355618171</gpWord><gpBr/><gpTR2 Type=0><td>交易号：12524</td><td>小票号：1</td></gpTR2><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>收款台        收款员        日期</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>--------------------------------</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>888  00076  2023-07-04 11:43:56</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>--------------------------------</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>部门名称：Dickies</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>================================</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>顾客手机号：3312</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>================================</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>编码/名称  单价   数量   金额</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>200001    551.00  1.000   551.00</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>Dickies</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>  小票合计：551.00</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>================================</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>折扣金额：0.00</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>合计金额：551.00</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>实收金额：551.00</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>微信：551.00</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>参考号:：120230704114358591</gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>--------------------------------</gpWord><gpWord Align=1 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>谢谢惠顾，欢迎下次光临</gpWord><gpBr/><gpBr/><gpCut/>";
        //    api.sendMsg("00135465914346428",detail,"2","1", IdUtil.fastSimpleUUID(),"1");
        //}
        //api.sendCommand("00135465914346428","1");
        //api.sendVoice("00135465914346428","已|收|到|5|5|1|.|0|0|元");

        //api.getStatus();
        //设置打印机暂停(智能机才有效)
//		api.setPause();
        //打印机固件升级(未测试)
//		api.update();
//		//语音固件升级(未测试)
//		api.voiceUpdate("audio_v35");
        //设置打印logo
        //api.setLogo(deviceCodes,"https://www.poscom.cn/images/testlogo.jpg");
        //删除打印logo
//		api.deleteLogo();

        //获取标签长度
//		api.getLabelLen();
        //设置推送地址
//		api.setPushUrl();
        //设置音量大小
        //api.sendVolume();
        //设置联网方式
//		api.setNet();
        //设置语音播报类型
		//api.setVoiceType();
        //查询分组设备类别
//		api.listDeviceByGroup();


        //mode2打印
//		api.print();
        //mode3打印
//		api.printHex();

        //状态统计
        //api.status();
        //api.getstatus();

        //api.pushNotice("https://env-feature-light-1015108.x.k8s.guanmai.cn/v2/api/notify/gainscha/1.0", "printStatus", "00869537053702800","1#{\"signal\": 87, \"volumn\": \"10\", \"softWare\": \"11.2.12.2\", \"printType\": \"22\", \"voiceType\": \"1\", \"corresType\": \"02\", \"heartTimes\": 30}");
    }
}
