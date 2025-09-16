package com.linzi.daily.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.HexUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.linzi.daily.tcpapi.GpApiUtils;
import com.linzi.daily.template.gp.domain.AbstractLabelTemplate;
import com.linzi.daily.template.gp.domain.TscFunc;
import com.linzi.daily.template.gp.domain.ZplFunc;
import com.linzi.daily.template.gp.entity.*;
import com.linzi.daily.template.gp.enums.Element;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Lil
 */
@Tag(name = "模版请求")
@RequestMapping("/template")
@RestController
public class TemplateController {

    public static void main(String[] args) {
        String json = "{\"gap\": 0, \"mode\": \"THERMAL\", \"tear\": false, \"layer\": [{\"x\": 726, \"y\": 25, \"id\": \"FQDQ90R6ND6AMT59\", \"name\": \"条形码1\", \"type\": \"barcode\", \"value\": \"59681545802\", \"width\": 371, \"height\": 135, \"zIndex\": 0, \"isField\": false, \"fontSize\": 8, \"rotation\": 90, \"barformat\": \"CODE128\", \"fieldName\": \"barcodeiz2u3rg\", \"bardisplay\": 2, \"fontFamily\": \"ARIAL\"}, {\"x\": 570, \"y\": 25, \"id\": \"U8GC34V7TJCKOMHQ\", \"name\": \"文本1\", \"type\": \"text\", \"value\": \"17회 중국민간예술산꽃상\", \"width\": 400, \"height\": 57, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 12, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 511, \"y\": 27, \"id\": \"Q8Q9N0932VF7NUDM\", \"name\": \"文本2\", \"type\": \"text\", \"value\": \"심사하여산꽃상 비물질문화유산보호센터\", \"width\": 246, \"height\": 52, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 8, \"rotation\": 90, \"textWrap\": true, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 511, \"y\": 297, \"id\": \"QI7I6B6N5JO3P0GK\", \"name\": \"文本3\", \"type\": \"text\", \"value\": \"1-001688\", \"width\": 108, \"height\": 35, \"zIndex\": 0, \"isField\": false, \"fontBold\": true, \"fontSize\": 8, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 689, \"y\": 429, \"id\": \"OTN03CIUARPUSKRL\", \"name\": \"文本4\", \"type\": \"text\", \"value\": \"W - L34 - 1\", \"width\": 294, \"height\": 87, \"zIndex\": 0, \"isField\": false, \"fontBold\": true, \"fontSize\": 16, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 726, \"y\": 496, \"id\": \"2PLDTOSCV012555G\", \"name\": \"文本5\", \"type\": \"text\", \"value\": \"2024-11-29\", \"width\": 140, \"height\": 41, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 8, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 236, \"y\": 27, \"id\": \"55SA5TI2SV2UTFUT\", \"name\": \"条形码2\", \"type\": \"barcode\", \"value\": \"59681545802\", \"width\": 369, \"height\": 156, \"zIndex\": 0, \"isField\": false, \"fontSize\": 8, \"rotation\": 90, \"barformat\": \"CODE128\", \"fieldName\": \"barcodeiz2u3rg\", \"bardisplay\": 1, \"fontFamily\": \"ARIAL\"}, {\"x\": 488, \"y\": 537, \"id\": \"NAD1M2B7CGJGMHHJ\", \"name\": \"文本6\", \"type\": \"text\", \"value\": \"S\", \"width\": 49, \"height\": 98, \"zIndex\": 0, \"isField\": false, \"fontBold\": true, \"fontSize\": 32, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 726, \"y\": 946, \"id\": \"IVJ0KAEV7CC5F858\", \"name\": \"文本5\", \"type\": \"text\", \"value\": \"2024-11-29\", \"width\": 140, \"height\": 41, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 8, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 689, \"y\": 722, \"id\": \"VF4GBO9RDFQU4K9P\", \"name\": \"文本8\", \"type\": \"text\", \"value\": \"김홍화\", \"width\": 209, \"height\": 52, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 12, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 689, \"y\": 1132, \"id\": \"TAMST3J5B1T8FESI\", \"name\": \"文本9\", \"type\": \"text\", \"value\": \"010-7137-6322\", \"width\": 235, \"height\": 59, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 12, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 639, \"y\": 719, \"id\": \"555OCRVI5JRRK70N\", \"name\": \"文本10\", \"type\": \"text\", \"value\": \"병으로 발병이 빠르고 전염성이 강한 특성을 가지고 있으며 주로 비말, 사람간 접촉 또는 오염된 물품과의 접\", \"width\": 719, \"height\": 117, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 12, \"rotation\": 90, \"textWrap\": true, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMSUN\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 511, \"y\": 722, \"id\": \"FIUCPVI8G2SMU0TF\", \"name\": \"文本11\", \"type\": \"text\", \"value\": \"LIUXIAOLING\", \"width\": 194, \"height\": 61, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 12, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMHEI\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 456, \"y\": 724, \"id\": \"GPUOC6MKTPOEPP9A\", \"name\": \"文本12\", \"type\": \"text\", \"value\": \"98-QGSSDG21212235481SSAG\", \"width\": 356, \"height\": 56, \"zIndex\": 0, \"isField\": false, \"fontBold\": false, \"fontSize\": 10, \"rotation\": 90, \"textWrap\": false, \"fieldName\": \"text9vyfec\", \"textAlign\": \"LEFT\", \"fontDelete\": false, \"fontFamily\": \"SIMHEI\", \"fontItalic\": false, \"fontUnderline\": false, \"letterSpacing\": 0}, {\"x\": 394, \"y\": 724, \"id\": \"QGNB6J3J5RDBO4VF\", \"name\": \"条形码2\", \"type\": \"barcode\", \"value\": \"59681545802\", \"width\": 555, \"height\": 123, \"zIndex\": 0, \"isField\": false, \"fontSize\": 8, \"rotation\": 90, \"barformat\": \"CODE128\", \"fieldName\": \"barcodeiz2u3rg\", \"bardisplay\": 1, \"fontFamily\": \"ARIAL\"}], \"shift\": 0, \"speed\": 4, \"width\": \"100\", \"column\": 1, \"cutter\": 0, \"height\": \"180\", \"isBlin\": \"COILED\", \"rotate\": 0, \"density\": \"NORMAL\", \"direction\": 0, \"paperType\": \"COILED\", \"projectName\": \"韩文横版打印模版\", \"templateType\": \"ZPL\", \"backgroundColor\": \"\", \"backgroundImage\": \"\"}";
        JSONObject jsonObj = JSONObject.parseObject( json);
    }

    @PostMapping("/gp/test")
    public String gpTestTsc(@RequestBody String json) throws IOException {
        System.out.println("===请求json===");
        System.out.println(json);
        JSONObject jsonObj = JSONObject.parseObject( json);
        JSONArray layerArray = jsonObj.getJSONArray("layer");
        jsonObj.remove("layer");
        Layout layout = JSONObject.parseObject(json, Layout.class);
        List<BaseElement> layerList = new ArrayList<>();
        for(int i=0;i<layerArray.size();i++){
            JSONObject layerObj = layerArray.getJSONObject(i);
            switch (Element.explain(layerObj.getString("type"))){
                case TEXT -> layerList.add(JSONObject.parseObject(layerObj.toJSONString(), TextElement.class));
                case BARCODE -> layerList.add(JSONObject.parseObject(layerObj.toJSONString(), BarCodeElement.class));
                case QRCODE -> layerList.add(JSONObject.parseObject(layerObj.toJSONString(), QrCodeElement.class));
                case SHAPE -> layerList.add(JSONObject.parseObject(layerObj.toJSONString(), ShapeElement.class));
                case LINE -> layerList.add(JSONObject.parseObject(layerObj.toJSONString(), LineElement.class));
                case VLINE -> layerList.add(JSONObject.parseObject(layerObj.toJSONString(), VLineElement.class));
                case IMAGE -> layerList.add(JSONObject.parseObject(layerObj.toJSONString(), ImageElement.class));
                default -> {}
            }
        }
        layout.setLayer(layerList);
        AbstractLabelTemplate labelTemplate;
        if("TSC".equals(layout.getTemplateType())){
            labelTemplate = new TscFunc(layout);
        }else{
            labelTemplate = new ZplFunc(layout);
        }
        String labelStr = labelTemplate.parseLabelTemplate();
        System.out.println("===十六进制打印数据===");
        FileUtil.del("D:\\hex.txt");
        FileUtil.appendString(labelStr, FileUtil.file("D:\\hex.txt"), "UTF-8");
        System.out.println("===发送打印数据===");
        GpApiUtils apiUtils = new GpApiUtils();
        if("TSC".equals(layout.getTemplateType())){
            apiUtils.sendMsg("dmemrzw423t","TSPL","3","1",labelStr);
        }else{
            labelStr = HexUtil.encodeHexStr(labelStr, Charset.forName("UTF-8"));
            apiUtils.sendMsg("20190115017903075","ZPL","3","4",labelStr);
        }
        return "ok";
    }
}
