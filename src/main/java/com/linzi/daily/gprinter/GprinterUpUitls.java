package com.linzi.daily.gprinter;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.linzi.daily.utils.OkHttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Lil
 */
public class GprinterUpUitls {

    static final String SIGN_SECRET = "bpypxyKwlt51smKxwYI0ygoVhl9S8dFt";
    static final String URL = "https://simtest.poscom.cn/admin/gprinterUp/upgrade";
    public static void upgrade(String model,String current,String target) throws IOException {
        String random = IdUtil.nanoId();
        String token = SecureUtil.sha1(model+current+random+SIGN_SECRET);
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Token",token);
        String param = "model="+model
                +"&current="+current+"&target="+target+"&random="+random;
        System.out.println(">>>"+URL+"?"+param);
        String reps = Objects.requireNonNull(OkHttpUtil.asyncGet(URL, param, headerMap).body()).string();
        System.out.println("<<<"+reps);
    }

    public static void main(String[] args) throws IOException {
        String model = "M324-JD";
        String current = "1.1.1";
        GprinterUpUitls.upgrade(model,current,"1.1.8");
    }
}
