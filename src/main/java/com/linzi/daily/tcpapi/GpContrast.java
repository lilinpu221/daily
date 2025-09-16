package com.linzi.daily.tcpapi;

import cn.hutool.crypto.SecureUtil;
import com.linzi.daily.utils.OkHttpUtil;
import okhttp3.FormBody;
import okhttp3.RequestBody;

import java.io.IOException;

public class GpContrast {

    private static final String TEST_KEY = "847682168B07B9A14405699157829243";
    private static final String TEST_SECRET = "P1B9AZGFHXRSI5CS9EBRMEDM8S64C0QZ";

    public static void constrastTemplat(String templateCode,String proKey,String proSecret) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String proSign = SecureUtil.md5(proKey + reqTime + proSecret + templateCode);
        RequestBody proBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", proSign)
                .add("memberCode", proKey)
                .add("templetID", templateCode).build();
        String proResp = OkHttpUtil.formPost("https://api.poscom.cn/apisc/templet", proBody).body().string();
        System.out.println("===============生产模版信息===============");
        System.out.println(proResp);
        String testSign = SecureUtil.md5(TEST_KEY + reqTime + TEST_SECRET + templateCode);
        RequestBody testBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", testSign)
                .add("memberCode", TEST_KEY)
                .add("templetID", templateCode).build();
        String testResp = OkHttpUtil.formPost("https://apitest.poscom.cn/apisc/templet", testBody).body().string();
        System.out.println("===============测试模版信息===============");
        System.out.println(testResp);
    }

    public static void constrastListTemplate(String proKey,String proSecret) throws IOException {
        String reqTime = String.valueOf(System.currentTimeMillis());
        String proSign = SecureUtil.md5(proKey + reqTime + proSecret);
        RequestBody proBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", proSign)
                .add("memberCode", proKey).build();
        String proResp = OkHttpUtil.formPost("https://api.poscom.cn/apisc/listTemplate", proBody).body().string();
        System.out.println("===============生产模版列表信息===============");
        System.out.println(proResp);
        String testSign = SecureUtil.md5(TEST_KEY + reqTime + TEST_SECRET);
        RequestBody testBody = new FormBody.Builder()
                .add("reqTime", reqTime)
                .add("securityCode", testSign)
                .add("memberCode", TEST_KEY).build();
        String testResp = OkHttpUtil.formPost("https://apitest.poscom.cn/apisc/listTemplate", testBody).body().string();
        System.out.println("===============测试模版列表信息===============");
        System.out.println(testResp);
    }

    public static void main(String[] args) throws IOException {
        GpContrast.constrastTemplat("27d866c33cc74bb6ac20ad891f6b5301","ABE431A78FA57E2AF78B37CBF3B237F4","GS22LU12Z2DBOFA5GUM84NDM3UICSIFA");
        //GpContrast.constrastListTemplate("ABE431A78FA57E2AF78B37CBF3B237F4","GS22LU12Z2DBOFA5GUM84NDM3UICSIFA");
    }
}
