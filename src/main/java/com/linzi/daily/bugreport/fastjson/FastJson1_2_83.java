package com.linzi.daily.bugreport.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson2.JSONWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * fastjson1.2.83
 * 特殊符号序列化，并加上SerializerFeature.BrowserCompatible，会造成阻塞
 * 在fastjson2中已经解决
 */
@Slf4j
public class FastJson1_2_83 {

    public static void fastjson1(){
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("/".repeat(1000000));
        log.info("start to calculate");
        long start = System.currentTimeMillis();
        JSON.toJSONString(stringBuffer.toString(), SerializerFeature.BrowserCompatible);
        log.info("spend {} millisecond!", System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        JSON.toJSONString(stringBuffer.toString());
        log.info("spend {} millisecond!", System.currentTimeMillis() - start);
    }

    public static void fastjson2(){
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("/".repeat(1000000));
        log.info("start to calculate");
        long start = System.currentTimeMillis();
        com.alibaba.fastjson2.JSON.toJSONString(stringBuffer.toString(), JSONWriter.Feature.BrowserCompatible);
        log.info("spend {} millisecond!", System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        com.alibaba.fastjson2.JSON.toJSONString(stringBuffer.toString());
        log.info("spend {} millisecond!", System.currentTimeMillis() - start);
    }
    public static void main(String[] args) {
        log.info("=======fastjson1========");
        FastJson1_2_83.fastjson1();
        log.info("=======fastjson2========");
        FastJson1_2_83.fastjson2();
    }
}
