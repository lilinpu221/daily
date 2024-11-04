package com.linzi.daily.tcpapi;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@RequestMapping("/printerback")
@RestController
public class PrinterBackController {

    private static final String JOLIMARK_KEY = "4ZKtOGigcQHTQs9wEbeFENxAIfeP4ybV";

    @PostMapping("/jolimark")
    public String jolimark(HttpServletRequest request){
        Map<String, String[]> reqMap = request.getParameterMap();
        System.out.println("====>"+ JSONUtil.toJsonStr(reqMap));
        return "{\"status\": 1,\"data\": \"ok\"}";
    }

    @RequestMapping("/gpOauth")
    public String gpOauth(HttpServletRequest request){
        Map<String, String[]> reqMap = request.getParameterMap();
        System.out.println("====>"+ JSONUtil.toJsonStr(reqMap));
        return "{\"data\":\"OK\"}";
    }

    @RequestMapping("/gpCallBack")
    public String gpCallBack(HttpServletRequest request) throws InterruptedException {
        Map<String, String[]> reqMap = request.getParameterMap();
        System.out.println("====>"+ JSONUtil.toJsonStr(reqMap));
        TimeUnit.SECONDS.sleep(RandomUtil.randomInt(60));
        return "{\"data\":\"OK\"}";
    }
}
