package com.linzi.daily.ocr.service;

import cn.hutool.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class TsplServiceImpl implements TsplService{
    @Override
    public String builderJDtemplate(JSONObject ocrJson) {
        //从ocr结果中获取结果
        String time = getValue(ocrJson,"0");
        String orderNo = getValue(ocrJson,"1");
        String title = getValue(ocrJson,"2");
        String seqNo = getValue(ocrJson,"3");
        String receiver = getValue(ocrJson,"9")+getValue(ocrJson,"10") + " "+
                getValue(ocrJson,"7") + " "+ getValue(ocrJson,"8");
        String sendAddr = getValue(ocrJson,"14") + getValue(ocrJson,"15");
        String sender = getValue(ocrJson,"11") + " "+ getValue(ocrJson,"12");
        String goods = getValue(ocrJson,"16");
        String cmd = "SIZE 50 mm, 60 mm\n" +
                "GAP 0 mm, 0 mm\n" +
                "REFERENCE 0, 0\n" +
                "SPEED 4\n" +
                "DENSITY 8\n" +
                "DIRECTION 0\n" +
                "SET HEAD ON\n" +
                "SET PRINTKEY OFF\n" +
                "SET RIBBON OFF\n" +
                "SET CUTTER OFF\n" +
                "SET TEAR ON\n" +
                "SHIFT 0\n" +
                "SET KEY1 ON\n" +
                "SET KEY2 ON\n" +
                "CLS\n" +
                "BARCODE 2,2,\"128\",55,1,0,3,2,\""+orderNo+"\"\n" +
                "BOX 2,88,400,418,2\n" +
                "TEXT 4,415,\"TSS24.BF2\",0,1,1,\"请您确认包裹完好\"\n" +
                "TEXT 210,415,\"TSS24.BF2\",0,1,1,\"客服电话:95060\"\n" +
                "BAR 3,189,391,4\n" +
                "BAR 3,230,393,4\n" +
                "BAR 4,272,393,4\n" +
                "BAR 181,89,2,103\n" +
                "BAR 296,89,2,102\n" +
                "TEXT 312,85,\"TSS24.BF2\",0,3,3,\"KA\"\n" +
                "TEXT 12,95,\"TSS24.BF2\",0,1,1,\""+title+"\"\n" +
                "TEXT 190,95,\"TSS24.BF2\",0,1,1,\""+seqNo+"\"\n" +
                "BAR 125,191,2,42\n" +
                "BAR 224,192,2,42\n" +
                "BAR 296,191,2,42\n" +
                "TEXT 309,176,\"TSS24.BF2\",0,2,2,\"1/1\"\n" +
                "TEXT 157,195,\"TSS24.BF2\",0,1,1,\"130\"\n" +
                "BAR 224,233,2,42\n" +
                "BAR 320,234,2,42\n" +
                "TEXT 224,238,\"TSS24.BF2\",0,1,1,\"客户签字\"\n" +
                "TEXT 7,239,\"TSS24.BF2\",0,1,1,\""+receiver+"\"\n" +
                "BAR 254,275,2,138\n" +
                "TEXT 258,275,\"TSS24.BF2\",0,1,1,\"已验视\"\n" +
                "QRCODE 302,319,L,4,A,0,\"cloud.poscom.cn\"\n" +
                "TEXT 9,280,\"TSS24.BF2\",0,1,1,\""+sendAddr+"\"\n" +
                "TEXT 13,315,\"TSS24.BF2\",0,1,1,\""+sender +"\"\n" +
                "BARCODE 9,353,\"128\",55,0,0,2,2,\""+goods+"\"\n" +
                "PRINT 1,1\n";
        return cmd;
    }

    private String getValue(JSONObject jsonObj,String key){
        return jsonObj.getJSONObject(key)==null?"":jsonObj.getJSONObject(key).getStr("rec_txt");
    }
}
