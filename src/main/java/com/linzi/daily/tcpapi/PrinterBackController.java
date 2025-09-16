package com.linzi.daily.tcpapi;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("/printerback")
@RestController
public class PrinterBackController {

    private static final String JOLIMARK_KEY = "4ZKtOGigcQHTQs9wEbeFENxAIfeP4ybV";

    @PostMapping("/jolimark")
    public String jolimark(HttpServletRequest request){
        Map<String, String[]> reqMap = request.getParameterMap();
        System.out.println("====>"+ JSONObject.toJSONString(reqMap));
        return "{\"status\": 1,\"data\": \"ok\"}";
    }

    @RequestMapping("/gpOauth")
    public String gpOauth(HttpServletRequest request){
        Map<String, String[]> reqMap = request.getParameterMap();
        System.out.println("====>"+ JSONObject.toJSONString(reqMap));
        return "{\"data\":\"OK\"}";
    }

    @RequestMapping("/gpCallBack")
    public String gpCallBack(HttpServletRequest request) throws InterruptedException {
        Map<String, String[]> reqMap = request.getParameterMap();
        System.out.println(DateUtil.now()+"====>"+ JSONObject.toJSONString(reqMap));
        TimeUnit.SECONDS.sleep(RandomUtil.randomInt(60));
        return "{\"data\":\"OK\"}";
    }
}
