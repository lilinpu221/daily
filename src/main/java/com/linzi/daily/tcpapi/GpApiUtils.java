package com.linzi.daily.tcpapi;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson2.JSONObject;
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
     * <p>
     * 佳博
     * https://api.poscom.cn/apisc
     * a1492960c3224140b7a862557feb5e77
     * CRZYZ8WG
     * <p>
     * 雷武森林
     * https://printer.aioptic.net/apisc
     * b6199c453b5b43f3b644ad21b48367e3
     * ZWWT9SQ9
     * <p>
     * 富友
     * http://api.fuioupay.com/apisc
     * 2dc21bcfb28e4584a4f269c776d6e591
     * D4CPOKT7
     * <p>
     * 初量科技
     * http://cloudprint.partydaysport.com/apisc
     * a7b3d733c81a44ba8349bc4048829f51
     * SCF9K7SS
     */
    private static final String apiUrl = "https://api.poscom.cn/apisc";
    private static final String devKey = "a1492960c3224140b7a862557feb5e77";
    private static final String devSecret = "CRZYZ8WG";

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

    public void sendLabelMsg(String deviceCode, String content2, int seq) throws IOException {
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

    public void sendMsg(String deviceCode, String cmdType, String mode, String charset, String content) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String msgNo = "123456789";
        String reprint = "1";
        String securityCode = SecureUtil.md5(devKey + deviceCode + msgNo + reqTime + devSecret);
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

    public void sendMsgMulti(String deviceCode, String content, String mode, String charset, String multi) throws IOException {
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
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/sendMsg", requestBody).body().string();
        System.out.println("==response：" + reps);
    }

    public void sendMsgMulti(String deviceCode, String content, String mode, String charset, String multi, String times) throws IOException {
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

    public void sendCommand(String deviceCode, String cmd) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + cmd + reqTime + devSecret);
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

    public void sendVoice(String deviceCode, String voice) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + deviceCode + reqTime + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .add("deviceID", deviceCode)
                .add("charset", "1")
                .add("msgDetail", "msgDetail")
                .add("mode", "2")
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

        String voiceJson = JSONObject.toJSONString(list);
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
        System.out.println(deviceCode + "==response：" + reps);
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
        System.out.println(deviceCode + "==response：" + reps);
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
        System.out.println(deviceCode + "==response：" + reps);
    }

    public void templetPrint(String deviceCode, String code, String data, String multi) throws IOException {
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

    public void voiceUpdate(String deviceCode, String version) throws IOException {
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

    public void romUpdate(String deviceCode, String version) throws IOException {
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

    public void setLogo(String deviceCode, String imgUrl) throws IOException {
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

    public void listTemplate() throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String securityCode = SecureUtil.md5(devKey + reqTime + devSecret);
        RequestBody requestBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", securityCode)
                .add("memberCode", devKey)
                .build();
        String reps = OkHttpUtil.formPost(apiUrl + "/listTemplate", requestBody).body().string();
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

    public void pushDevice(String deviceCode, String url, String cmd, String status, String info) throws IOException {
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

    public void pushNotice(String deviceCode, String url, final String cmd, final String deviceID, final String pushInfo) throws Exception {
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

    public static void main(String[] args) throws Exception {
        GpApiUtils api = new GpApiUtils();
        String mode2 = "2";
        String mode2Esc = "<gpWord Align=1 Bold=1 Wsize=1  Hsize=1 Reverse=0 Underline=0>扫呗点餐</gpWord><gpWord Align=1 Bold=1 Wsize=1  Hsize=2 Reverse=0 Underline=0>后厨单</gpWord><gpWord Align=1 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>*绝味花甲*</gpWord><gpWord Align=1 Bold=0 Wsize=1 Hsize=1 Reverse=0 Underline=0>【打包】</gpWord><gpWord Align=1 Bold=0 Wsize=1 Hsize=1 Reverse=0 Underline=0>取餐号:D100</gpWord>--------------------------------<gpBr/><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>下单时间:05-19 10:34:15</gpWord>订单号:206612562505191034154875<gpBr/>--------------------------------<gpBr/><gpWord Align=0 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0><gpTR2 Type=0><td>品名</td><td>数量</td></gpTR2></gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=2 Reverse=0 Underline=0><gpTR2 Type=0><td>番茄/米线/粉丝/土豆粉/方便面</td><td>1</td></gpTR2></gpWord><gpWord Align=0 Bold=0 Wsize=0 Hsize=2 Reverse=0 Underline=0><gpTR2 Type=0><td>-要香菜、微辣、醋、土豆粉</td><td></td></gpTR2></gpWord>--------------------------------<gpBr/><gpWord Align=2 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>总  计:                          1份</gpWord><gpWord Align=2 Bold=0 Wsize=0 Hsize=0 Reverse=0 Underline=0>总金额:                          ￥14.00</gpWord>--------------------------------<gpBr/><gpBr/>打印时间:2025-05-19 10:34:22<gpBr/><gpCut/>";
        String mode3 = "3";
        String mode3Esc = "1B61011D48001D68501D77021D6B490e7B43502632013e020e15247B42330A";
        String mode2Tsc = "SIZE 80 mm,60 mm\n" +
                "GAP 0 mm,0 mm\n" +
                "REFERENCE 0,0\n" +
                "SPEED 4\n" +
                "DENSITY 6\n" +
                "DIRECTION 1\n" +
                "SHIFT 0\n" +
                "SET HEAD ON\n" +
                "SET PRINTKEY OFF\n" +
                "SET KEY1 ON\n" +
                "SET KEY2 ON\n" +
                "SET RIBBON OFF\n" +
                "SET CUTTER OFF\n" +
                "SET TEAR ON\n" +
                "CLS\n" +
                "TEXT 36,27,\"2\",0,1,1,\"Tyre\"\n" +
                "BARCODE 34,178,\"128\",90,0,0,2,2\"D2025062052617'\n" +
                "TEXT 36,135,\"3\",0,1,1,\"10.00-20\"\n" +
                "TEXT 495,433, \"2\",0,1,1,\"1\"\n" +
                "TEXT 495,395,\"2\",0,1 1\"1\"\n" +
                "TEXT 441,431,\"2\",0,1,1,\"QTY:\"\n" +
                "TEXT 453,393,\"2\",0,1,1,\"NO:\"\n" +
                "TEXT 35,93,\"TSS24,BF2\",0,1,1,\"生活\"\n" +
                "TEXT 35,287,\"TSS24,BF2\"0,1,1,\"JNSH25060188BW001C\"\n" +
                "TEXT 84,332,\"TSS24,BF2\",0,1,1,\"999\"\n" +
                "TEXT 36,331,\"TSS24,BF2\",0,1,1,\"P:\"\n" +
                "BAR 27,377,576,2\n" +
                "TEXT 580,433,\"2\",0,1,1,\"PC\"\n" +
                "BARCODE 35,396,\"128\",50,0,0,2,2\"D2025062052617\"\n" +
                "PRINT 30,1\n" +
                "\n";
        String mode3Zpl = "5e58410a5e4d43595e4d54445e4d44385e4c54305e4d4d545e4d4e4e5e50573430305e4c4c3332305e5052325e504d4e5e504f4e5e4a4d415e4c48302c345e4c524e5e4357412c5a3a4d53554e472e464e545e434932380a5e464f352c355e47423338382c3331342c335e46530a5e464f362c3133355e47423338352c332c335e46530a5e464f362c37305e47423338352c332c335e46530a5e464f352c3230305e47423338352c332c335e46530a5e464f3135302c365e4742322c3330362c325e46530a5e464f33302c32375e41412c32342c32345e4644e8b584e4baa7e7bc96e7a0815e46530a5e464f33302c38395e41412c32342c32345e4644e8b584e4baa7e7b1bbe59e8b5e46530a5e464f33302c3135335e41412c32342c32345e4644e697a5e69c9fefbc9a5e46530a5e464f3136302c32375e41412c32342c32345e4644e6ada3e69687e58685e5aeb95e46530a5e464f3136302c38395e41412c32342c32345e4644e6ada3e69687e58685e5aeb95e46530a5e464f3136302c3135355e41412c32342c32345e4644e6ada3e69687e58685e5aeb95e46530a7e444747524150484943312c313135322c31322c4966633363333049633363336630633063496663304966633363333049633363336630633063496663306348306330483366334a6663306648333063483063306348306330483366334a66633066483330634830633063664863664b303366493066633330636648633063664863664b30336649306663333063664863306366486366334866303366336663664933486366486330636648636633486630336633666366493348636648633063664863664830634a6663304a6648636648633063664863664830634a6663304a6648636648633063483063664963306663304866336349306348306330634830636649633066633048663363493063483063304966566349666330496656634966634b3063336630663063306633496633634f3063336630663063306633496633634b306348666333486648304966486366334866304866634830634866633348664830496648636633486630486663493066634833304866336630336333664a63306663493066634833304866336630336333664a633066634930664963336630486663336633306630634830333063483066496333663048666333663330663063483033306330336349306330666333306648333063483366333066633033634930633066633330664833306348336633306663306330486630633048663063666366493066486330334830633048663063304866306366636649306648633033483063493349664833664830633066336330664863664830634933496648336648306330663363306648636648303363336333496630634833663348303366483048334830336333633349663063483366334830336648304833483048663330486348336648336333303366483066483048633048663330486348336648336333303366483066483048633063306649633366336663666366634833663366483348306330664963336633666366636663483366336648334930666348306633636633483033486630636630336648634830666348306633636633483033486630636630336648633049634966493363486663304933483063336648304963496649336348666330493348306333664a3063483330486649306649306663306630663363493063483330486649306649306663306630663363304a6663333033304a666366336663496649304a6663333033304a666366336663496649303348633066493048336330663366483330633048664830334863306649304833633066336648333063304866483048664863664830664a636630483330664963493048664863664830664a6366304833306649634b3063306648336630486330663366634830633063306349306330664833663048633066336663483063306330633048334b6630634966636648304a666366483048334b6630634966636648304a66636649304863336663483330663363664a63333066336349304863336663483330663363664a63333066336349306648633363483366304833633033663348303366334930664863336348336630483363303366334830336633483033666333483063306348666349306633306630334863303366633348306330634866634930663330663033486330663349663330486648306633486648304866333048633066334966333048664830663348664830486633304863303366483330334866633348666330636663483363306348303366483330334866633348666330636663483363306349304863663048663063664930664830336348306633634830486366304866306366493066483033634830663363483066333033666366336349664833486348336348306348306633303366636633634966483348634833634830633063666366486348664830483330634933486630486648306366636648634866483048333063493348663048664930664833633048663033483066306366633348634866634830664833633048663033483066306366633348634866634930486333486333633063303348306648306633634b3048633348633363306330334830664830663363493033666330636663496663663048336630336648303363303366633063666349666366304833663033664830336330633348636648303348304a664933496648634830633348636648303348304a664933496648634c306366304833666330663049636663304866634b30636630483366633066304963666330486663304966634833664863304963304a334863663363304966634833664863304963304a334863663363306348304863304863304863306630336663306330664863306348304863304863304863306630336663306330664863306366496333663066304866486348336663486648634830636649633366306630486648634833666348664863483063664963333063483366483066636648304833486648306366496333306348336648306663664830483348664830636648636633493063336366483033483066486349306366486366334930633363664830334830664863493063483063483330336630664a33636649336333633063483063483330336630664a336366493363336330496663664830334966496330333066634833664830496663664830334966496330333066634833666d4c300a5e464f33342c3231335e584747524150484943310a7e444747524150484943312c333235362c33372c6a4930643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361635830643231393732363734656339636448336137483634654839644837624836483436633235643161386336643839313931376131613633616358306432313937323637346563396364483361374836346548396448376248364834366332356431613863366438393139313761316136336163583064323139373236373465633963644833613748363465483964483762483648343663323564316138633664383931393137613161363361637951303748303338303448306548303830333831633065306663376533663166386663333831633065303730333830323065483038674a3039383048343063304831303138304834483231333038483048323031483038303463323631333048384834303648313031386749303130384830323134493038323848303230313231303848303432303148303830384834483231483034303230613230383238674b303830384832343032303834383038323431304831493048343032303149303430323048313034383231483230383438674a303148303832303430323038343830383234313032316648303837633365316648303830343032313034383231483230383438674a3037483038323034303230493830383234313065314838303836323348314838333831633065313034384a32304938674b3038303832303430323039303830383234313031483034303830314830383034303430323048313034383234324831393038674b3034303832303430323061303830383234314830383034303830314830383034303230314830393034383238323065613038674b30343038323034303230626663303832343148303830343148303148303830343032303148303930343832486648306266636749303130344a30344b30384b303230393034313034313230393034383234313230384b30483230383038674a304838304834303430483148303830483448324831304838313048324831304838483448324831304838483430324831483038674a3037303433383034313065483038343338316330653037303130316330653037303338316330653037303338303230654830386a4c300a5e464f3136342c3231355e584747524150484943310a5e5051312c302c312c590a5e585a0a";
        String mode3Tsc = "53495a45203530206d6d2c3430206d6d0D0A4741502032206d6d2c30206d6d0D0A5245464552454e434520302c300D0A535045454420320D0A44454e5349545920380D0A444952454354494f4e20300D0A534849465420300D0A5345542048454144204f4e0D0A534554205052494e544b4559204f46460D0A534554204b455931204f4e0D0A534554204b455932204f4e0D0A53455420524942424f4e204f46460D0A53455420435554544552204f46460D0A5345542054454152204f4e0D0A434c530D0A424f5820352c352c3339332c3331392c330D0A42415220362c3133352c3338352c330D0A42415220362c37302c3338352c330D0A42415220352c3230302c3338352c330D0A424152203135302c362c322c3330360D0A544558542033302c32372c2254535332342e424632222c302c312c312c22d7cab2fab1e0c2eb220D0A544558542033302c38392c2254535332342e424632222c302c312c312c22d7cab2fac0e0d0cd220D0A544558542033302c3135332c2254535332342e424632222c302c312c312c22c8d5c6daa3ba220D0A54455854203136302c32372c2254535332342e424632222c302c312c312c22d5fdcec4c4dac8dd220D0A54455854203136302c38392c2254535332342e424632222c302c312c312c22d5fdcec4c4dac8dd220D0A54455854203136302c3135352c2254535332342e424632222c302c312c312c22d5fdcec4c4dac8dd220D0A4249544d41502033342c3231332c31322c39362c302c0003c3cf333c3c0f3f30003f0003c3cf333c3c0f3f30003f3ff3fcc0c00003f0ccf3ff3f3ff3fcc0c00003f0ccf3ff3f30330fffffc0fff03cf3033f30330fffffc0fff03cf3033f30330c00fc0c030ccc33033f30330c00fc0c030ccc33033f30330ff300003f000033033f30330ff300003f000033033f3ff30333f03f00c3fff3ff3f3ff30333f03f00c3fff3ff3f00033333333333333330003f00033333333333333330003fffff3c0f0f3f0c000c3fffffffff3c0f0f3f0c000c3fffff3003c00ff000330c00f003ff3003c00ff000330c00f003fff03ccf00c0fc3c03333f03fff03ccf00c0fc3c03333f03fff0333c0f003c0cf0f3ffcf3ff0333c0f003c0cf0f3ffcf3fc3fff3f03cf0ccf3cc0cf03fc3fff3f03cf0ccf3cc0cf03f3f00f3f00f3030fff033fcff3f00f3f00f3030fff033fcff3ccc000cc0ff3f0c3f0330ff3ccc000cc0ff3f0c3f0330ffc3c3c000f3cc0cffc0ffccffc3c3c000f3cc0cffc0ffccff00cf33cc0cc3cfc0ff0ff33f00cf33cc0cc3cfc0ff0ff33f3f0333c0c030303cc0c0ccff3f0333c0c030303cc0c0ccfff03ff0c30cffc00f30fc033ff03ff0c30cffc00f30fc033f333000ccc3003fcccff3c0ff333000ccc3003fcccff3c0ffff3ccf00fff0fff03f0f0c3fff3ccf00fff0fff03f0f0c3f00003cfcf000030c03000fff00003cfcf000030c03000fffc33f0fffcc3f0c0ccf3f00ffc33f0fffcc3f0c0ccf3f00ff00330ff033330fccf0333fff00330ff033330fccf0333fffff3f0cc0f33f0c03ff3f3f3fff3f0cc0f33f0c03ff3f3f3fcc00000f300030ff000030ffcc00000f300030ff000030fff33c03ccf0c303333cf0c3fff33c03ccf0c303333cf0c3fff033c3cc0fcc3fc0cffc0cfff033c3cc0fcc3fc0cffc0cffc03cff3f3003fff0cf0fc33fc03cff3f3003fff0cf0fc33f0c000cf00ff0c00ff00cf33f0c000cf00ff0c00ff00cf33fc0ccfc003c003f303cc3f3ffc0ccfc003c003f303cc3f3fff330f00f30fff0ffc3ff0c3ff330f00f30fff0ffc3ff0c3ff0cfc030c3000cc33cc3ff3ff0cfc030c3000cc33cc3ff3f30303300ffccf3ccc00f00ff30303300ffccf3ccc00f00fff0cc3f00fcff0f303c33003ff0cc3f00fcff0f303c33003fff33c33c3f3fcff0ff0c3fffff33c33c3f3fcff0ff0c3fffc03f30300030fcc0fc0ffc3fc03f30300030fcc0fc0ffc3f3c330ffcff0000ccc00033ff3c330ffcff0000ccc00033ffffff30fcc03f0f33303f003fffff30fcc03f0f33303f003f0003cc033f333fcccc330c3f0003cc033f333fcccc330c3f3ff33f33f33f0fc03f3f033f3ff33f33f33f0fc03f3f033f30333c0f0f0033cc030033ff30333c0f0f0033cc030033ff30333cf3cc0ff030ffcc00ff30333cf3cc0ff030ffcc00ff30330cfff3c30ffcff033fff30330cfff3c30ffcff033fff3ff3ccfc0f0cccc30ccc3c3f3ff3ccfc0f0cccc30ccc3c3f00030ffc000333fcf03cc0ff00030ffc000333fcf03cc0ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0D0A4249544d4150203136342c3231352c32372c38382c302cffffffffffffffffffffffffffffffffffffffffffffffffffffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffff2f625d38a213622cd3191b2364e45998b12298c4b9c53fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffe07f0fe07fe7fcfe07e07fcfe07fcfe0ffcfe07fe7e07e07e07e3fc03e07c03fc7fcfc03c03fcfc03fcfc07fcfc03fc7c03c03c03c3f8f1cf38f1f87f0f8f18f1f0f8f1f0fce3f0f8f1f878f18f18f1cff9f9cf39f9f87e0f9f99f9e0f9f9e0f9f3e0f9f9f879f99f99f99ffff99f9ff9f27c4fff9ff9c4fff9c4fff3c4fff9f27ff9ff9ff9fffff99f9ff9f27dcfff9ff9dcfff9dcffe7dcfff9f27ff9ff9ff9fffff99f9ff9e67fcfff9ff9fcfff9fcff8ffcfff9e67ff9ff9ff9fbfff39f9ff3c67fcfff3ff3fcfff3fcff87fcfff3c67ff3ff3ff3fbffe79f9fe7ce7fcffe7fe7fcffe7fcfff3fcffe7ce7fe7fe7fe7ffffcf9f9fcf9e7fcffcffcffcffcffcfff9fcffcf9e7fcffcffcfffff9f9f9f9f801fcff9ff9ffcff9ffcfff9fcff9f801f9ff9ff9fffff3f9f9f3f801fcff3ff3ffcff3ffcfff9fcff3f801f3ff3ff3ffffe7fcf3e7ffe7fcfe7fe7ffcfe7ffcf9f9fcfe7ffe7e7fe7fe7f9ffcffcf3cfffe7fcfcffcfffcfcfffcf8f3fcfcfffe7cffcffcff8ff801e07801fe7fcf801801fcf801fcfc07fcf801fe7801801801c3fffffffffffffffffffffffffffffffffffffffffffffffffffffff0D0A5052494e5420312c310D0A";
        String charset1 = "1";
        String charset4 = "4";
        String device1 = "00869537053704319";
        String device2 = "20190115017903075";
        String deviceCodes = "dmemrzwtmy7,00869537053704319,00865447064831081";
        String[] devices = deviceCodes.split(",");
        //for (String device : devices) {
            //添加打印机
            api.adddev(device2);
            //api.adddev(device2);
            //api.sendMsgMulti(device,mode2Esc,"2","1","1");
            //api.deviceInfo(device);
            //api.getStatus(device);
            //取消未打印
            //api.cancelPrint(device1);
            //api.romUpdate(device2,"11.x.14.26");
            //api.sendVoice(device,"已|收|到|0|.|0|1|元");
            //Thread.sleep(1500);
            //删除打印机
            //api.deldev(device);
            api.sendMsg(device2, "ZPL", "3", "4", mode3Zpl);
        //}
        //api.sendMsg("dmemryng45e","ESC","3","1","1B401B61021D2111BFCDBBA7C1AA0A0A1B61011D21112335C3C0CDC50A1B401B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61011D21011B2D001B4500C4C9D7C8B8F1C7E0EFFDC4F0C6A4A3A8D1D3B0B2C2B7B5EAA3A90A1B401B61011D21011B2D001B45000A1B401B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61001D21001B2D001B4500B6A9B5A5B1E0BAC5A3BA343830313733363534333835373930373733320A1B401B61001D21001B2D001B4500CFC2B5A5CAB1BCE4A3BA323032352D30382D31382032323A32343A34330A1B401B61011D21011B2D001B4500C1A2BFCCCBCDB4EF0A1B400A1B61011D21002D2D2D2D2D2D2D2D2D2D2D2031BAC5BFDAB4FC202D2D2D2D2D2D2D2D2D2D2D0A1B61001D21011B2D001B4500C2F3CBEBB3A681318D348131873281319133813186368131943881318530813183395B31C8CBB7DD2020202020583120202020342E350A1B61001D21011B2D001B45005D20202020202020202020202020202020202020202020202020202020202020200A1B61001D21011B2D001B4500D5C2D3E3D0A1CDE8D7D3A1A42BD2FBC1CF222B222020202020583120202020342E350A1B61001D21011B2D001B45005BB2BBC0B1A1A32C2CD4ADCEB6A1A32C2CBFC920202020202020202020202020200A1B61001D21011B2D001B4500C0D65DD2FBC1CF20202020202020202020202020202020202020202020202020200A1B61001D21011B2D001B4500C3C0CAB3C0B6BCA6C8E2B3A68131863981318733813185318131843981318339813187308131843220202020205832202020372E30300A1B61001D21011B2D001B450081318732813184368131933981319135813193388131853281318733813186368131943881318530813183395B31B4AE5D20202020202020202020202020202020200A1B61001D21011B2D001B4500F0C6F0C8B5B08131843081319338813184378131943081318730813195338131843281319339813184368131933981318639813187335B20202020205832202020372E30300A1B61001D21011B2D001B450031C8CBB7DD5D2020202020202020202020202020202020202020202020202020200A1B61001D21011B2D001B4500D7CFCAEDC7F2813184328131833981318432813186388131873381318636813187348131833981319133813187348131933981318530813187332020202020583120202020322E300A1B61001D21011B2D001B450081318436813186388131873381318636813187335B31C8CBB7DD5D2020202020202020202020202020202020202020200A1B61001D21011B2D001B4500E2CE8131873081318339813187305B31C8CBB7DD5D2020202020202020202020205832202020342E30300A1B401B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61001D21011B2D001B4500BAC3BEB0BBA8D4B7282A2A2A290A1B401B61001D21011B2D001B4500BFCFCFC8C9FA0A1B401B61001D21011B2D001B45003138343439303339383138D7AA343138390A1B401B61001D21011B2D001B45003133332A2A2A2A353431310A1B400A1B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D20B1B8D7A2202D2D2D2D2D2D2D2D2D2D2D2D2D0A1B401B61001D21111B2D001B4500B9CBBFCDD0E8D2AAB2CDBEDF0A1B401B61011D21002D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D2D0A1B61011D48001D68501D77021D6B490E7B424D32743970334167304949350A1B61011D21002A2A2A2A2A2A2A2A1D21112335CDEA1D21002A2A2A2A2A2A2A2A0A0A0A0A0A0A1D564200");
        //String tData = "{\"ordernum001\":[{\"standard\":\"FZ/T73011\",\"grade\":\"一等品1\",\"origin\":\"浙江省\",\"name\":\"吊带连衣群1\",\"ingredients\":\"棉60% 锦纶30%\",\"inspector\":\"111\",\"texthqfzfg\":\"1B11\",\"brand\":\"真丝系列\",\"barcode\":\"D-131311\"},{\"standard\":\"FZ/T73012\",\"grade\":\"一等品2\",\"origin\":\"浙江省\",\"name\":\"吊带连衣群2\",\"ingredients\":\"棉60% 锦纶30%\",\"inspector\":\"112\",\"texthqfzfg\":\"1B12\",\"brand\":\"真丝系列\",\"barcode\":\"D-131312\"}]}";
        //String tData = "{\"ordernum001\":[{\"text1\":\"测试10\",\"text2\":\"测试11\"},{\"text1\":\"测试20\",\"text2\":\"测试21\"}]}";
        //api.templetPrint(device2,"3eb67c5f602f4959997e668f70605c6d",tData,"1");
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
        //模版列表
        //api.listTemplate();
        //api.pushNotice("https://env-feature-light-1015108.x.k8s.guanmai.cn/v2/api/notify/gainscha/1.0", "printStatus", "00869537053702800","1#{\"signal\": 87, \"volumn\": \"10\", \"softWare\": \"11.2.12.2\", \"printType\": \"22\", \"voiceType\": \"1\", \"corresType\": \"02\", \"heartTimes\": 30}");
    }
}
