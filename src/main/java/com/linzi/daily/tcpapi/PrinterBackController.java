package com.linzi.daily.tcpapi;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public String gpCallBack(HttpServletRequest request){
        Map<String, String[]> reqMap = request.getParameterMap();
        System.out.println("====>"+ JSONUtil.toJsonStr(reqMap));
        return "{\"data\":\"OK\"}";
    }
}
