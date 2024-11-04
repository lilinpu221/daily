package com.linzi.daily.bugreport.fastjson1;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
public class FastJson1_2_83 {
    public static void main(String[] args) {
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
}
