package com.linzi.daily.sim;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.linzi.daily.utils.OkHttpUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.*;

/**
 * 联通IOT API接口
 */
public class GwIotUtils {
    private static final String BASE_URL = "https://gwapi.10646.cn/api";
    private static final String APP_ID = "fzvtaD2ZUt";
    private static final String APP_SECRET = "TPGnZQjSWnR7trabCgyGJQeJcV3TzR";
    private static final String OPEN_ID = "40415ouC5Nzoe1W";
    private static final String VERSION = "1.0";

    public static void main(String[] args) throws IOException {
        JSONArray iccids = new JSONArray();
        iccids.put("89860624650055099583");
        System.out.println(GwIotUtils.simDetails(iccids));
        //2-已激活 3-已停机 4-已失效 5-已清除
        //System.out.println(GwIotUtils.simOpenClose("89860624650055099583",3));
    }

    public static String simDetails(JSONArray iccids) throws IOException {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("messageId", IdUtil.fastSimpleUUID());
        paramMap.put("openId",OPEN_ID);
        paramMap.put("version",VERSION);
        paramMap.put("iccids",iccids);
        Map<String,Object> headMap = buildParam(paramMap);
        System.out.println(">>>>>"+JSONUtil.toJsonStr(headMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(headMap));
        return Objects.requireNonNull(OkHttpUtil.formJson(BASE_URL + "/wsGetTerminalDetails/V1/1Main/vV1.1",
                body, Collections.emptyMap()).body()).string();
    }

    public static String simOpenClose(String iccid,int type) throws IOException {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("messageId", IdUtil.fastSimpleUUID());
        paramMap.put("openId",OPEN_ID);
        paramMap.put("version",VERSION);
        paramMap.put("asynchronous","0");
        paramMap.put("iccid",iccid);
        paramMap.put("changeType","3");
        paramMap.put("targetValue",type);
        Map<String,Object> headMap = buildParam(paramMap);
        System.out.println(">>>>>"+JSONUtil.toJsonStr(headMap));
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), JSONUtil.toJsonStr(headMap));
        return Objects.requireNonNull(OkHttpUtil.formJson(BASE_URL + "/wsGetInvoice/V1/1Main/vV1.1",
                body, Collections.emptyMap()).body()).string();
    }

    private static Map<String,Object> buildParam(Map<String,Object> paramMap){
        Date now = new Date();
        Map<String,Object> headMap = new HashMap<>();
        headMap.put("app_id",APP_ID);
        headMap.put("timestamp", DateUtil.format(now, "yyyy-MM-dd HH:mm:ss SSS"));
        headMap.put("trans_id",DateUtil.format(now, DatePattern.PURE_DATETIME_MS_PATTERN)+ RandomUtil.randomNumbers(6));
        headMap.put("data",paramMap);
        String signStr = buildSign(headMap);
        headMap.put("token",signStr);
        return headMap;
    }

    private static String buildSign(Map<String,Object> headMap){
        TreeMap<String,Object> treeMap = new TreeMap<>(headMap);
        StringBuilder signBuilder = new StringBuilder();
        treeMap.forEach((k,v)-> signBuilder.append(k).append(v));
        String signStr = signBuilder.append(APP_SECRET).toString();
        System.out.println(">>>>"+signStr);
        return SmUtil.sm3(signStr);
    }
}
